package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.screen.widgets.ContainerWidget;
import dev.toma.gunsrpg.client.screen.widgets.EnumButton;
import dev.toma.gunsrpg.client.screen.widgets.ScrollableListWidget;
import dev.toma.gunsrpg.common.container.TurretContainer;
import dev.toma.gunsrpg.common.entity.TurretEntity;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_TurretSettingsPacket;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TurretScreen extends ContainerScreen<TurretContainer> {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/screen/turret.png");
    private static final ITextComponent TEXT_ADD = new TranslationTextComponent("screen.gunsrpg.button.add");
    private static final ITextComponent TEXT_ENTER_PLAYERNAME = new TranslationTextComponent("screen.gunsrpg.text_field.enter_playername");
    private TurretEntity turret;

    private TextFieldWidget playerNameField;

    public TurretScreen(TurretContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        imageHeight = 179;
    }

    @Override
    protected void init() {
        super.init();
        Entity entity = minecraft.level.getEntity(menu.getEntityId());
        if (!(entity instanceof TurretEntity)) {
            minecraft.setScreen(null);
            GunsRPG.log.error("Unable to display turret screen, invalid entity received: Id {}, Entity {}", menu.getEntityId(), entity);
            return;
        }
        this.turret = (TurretEntity) entity;

        List<Pair<UUID, String>> whitelistedPlayers = turret.getNameCache().entrySet().stream()
                .map(entry -> Pair.of(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(Pair::getSecond))
                .collect(Collectors.toList());
        ScrollableListWidget<Pair<UUID, String>, WhitelistEntryWidget> whitelist = addButton(new ScrollableListWidget<>(leftPos + 81, topPos + 7, 88, 60, whitelistedPlayers, (entry, entryX, entryY, entryWidth, entryHeight) -> {
            RemoveWhitelistEntryHandler handler = this::removeWhitelistEntryClicked;
            return new WhitelistEntryWidget(entryX, entryY, entryWidth, entryHeight, font, entry, handler);
        }));
        whitelist.setEntryHeight(20);

        EnumButton<TurretEntity.TargettingMode> selector = addButton(new EnumButton<>(leftPos + 7, topPos + 47, 72, 20, turret.getTargettingMode(), turret.getAvailableTargettingModes(), this::targettingModeChanged));
        selector.setToComponentTransformer(EnumButton::upperCaseFormat);

        Button send = addButton(new Button(leftPos + 109, topPos + 69, 60, 20, TEXT_ADD, this::addToWhitelistClicked));

        String suggestion = TEXT_ENTER_PLAYERNAME.getString();
        playerNameField = addButton(new TextFieldWidget(font, leftPos + 7, topPos + 69, 100, 20, StringTextComponent.EMPTY));
        playerNameField.setSuggestion(suggestion);
        playerNameField.setResponder(value -> {
            playerNameField.setSuggestion(value.isEmpty() ? suggestion : "");
            send.active = !value.isEmpty() && isValidPlayerName(value);
        });
        send.active = false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        IGuiEventListener listener = this.getFocused();
        if (listener instanceof TextFieldWidget && ((TextFieldWidget) listener).isFocused()) {
            listener.keyPressed(keyCode, scanCode, modifiers);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    protected void renderLabels(MatrixStack stack, int mouseX, int mouseY) {
    }

    @Override
    protected void renderBg(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bind(TEXTURE);
        blit(stack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);
    }

    private void removeWhitelistEntryClicked(UUID uuid) {
        turret.removeFromWhitelist(uuid);
        NetworkManager.sendServerPacket(C2S_TurretSettingsPacket.whitelistPacket(turret, C2S_TurretSettingsPacket.WhitelistOperation.REMOVE, uuid));
        init(minecraft, width, height);
    }

    private void targettingModeChanged(TurretEntity.TargettingMode mode) {
        turret.setTargettingMode(mode);
        NetworkManager.sendServerPacket(C2S_TurretSettingsPacket.setTargettingMode(turret, mode));
        init(minecraft, width, height);
    }

    private void addToWhitelistClicked(Button button) {
        String name = playerNameField.getValue();
        UUID playerId = getPlayerByName(name);
        if (playerId != null) {
            turret.addToWhitelist(playerId);
            NetworkManager.sendServerPacket(C2S_TurretSettingsPacket.whitelistPacket(turret, C2S_TurretSettingsPacket.WhitelistOperation.ADD, playerId));
        }
        init(minecraft, width, height);
    }

    private boolean isValidPlayerName(String name) {
        return this.getPlayerByName(name) != null;
    }

    private UUID getPlayerByName(String name) {
        return minecraft.level.players().stream()
                .filter(player -> player.getName().getString().equalsIgnoreCase(name))
                .findFirst()
                .map(Entity::getUUID)
                .orElse(null);
    }

    private static final class WhitelistEntryWidget extends ContainerWidget {

        private final FontRenderer font;
        private final ITextComponent name;
        private final Button removeButton;

        public WhitelistEntryWidget(int x, int y, int width, int height, FontRenderer font, Pair<UUID, String> entry, RemoveWhitelistEntryHandler handler) {
            super(x, y, width, height);
            this.font = font;
            this.name = new StringTextComponent(entry.getSecond());
            this.removeButton = new Button(x + width - 20, y, 20, 20, new StringTextComponent("x"), btn -> {
                UUID uuid = entry.getFirst();
                handler.handleRemoval(uuid);
            });
            this.addWidget(removeButton);
            this.setRemoveButtonVisible(false);
        }

        @Override
        public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            this.setRemoveButtonVisible(this.isHovered);
            if (isHovered) {
                fill(stack, x, y, x + width, y + height, 0x66FFFFFF);
                this.font.drawShadow(stack, name, x + 3, y + (height - font.lineHeight) / 2.0F, 0xFFFF00);
            } else {
                this.font.draw(stack, name, x + 3, y + (height - font.lineHeight) / 2.0F, 0xFFFFFF);
            }
            super.renderButton(stack, mouseX, mouseY, partialTicks);
        }

        private void setRemoveButtonVisible(boolean visible) {
            this.removeButton.visible = visible;
            this.removeButton.active = visible;
        }
    }

    @FunctionalInterface
    private interface RemoveWhitelistEntryHandler {
        void handleRemoval(UUID uuid);
    }
}

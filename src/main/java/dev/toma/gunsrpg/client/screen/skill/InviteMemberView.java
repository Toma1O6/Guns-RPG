package dev.toma.gunsrpg.client.screen.skill;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.client.ScreenDataEventListener;
import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.client.screen.widgets.HeaderWidget;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.config.QuestConfig;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_InviteMember;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class InviteMemberView extends View implements ScreenDataEventListener {

    private static final ITextComponent TITLE = new TranslationTextComponent("view.invite.header");
    private static final ITextComponent IN_GROUP = new TranslationTextComponent("view.invite.player_in_group").withStyle(TextFormatting.YELLOW);
    private TextFieldWidget searchField;
    private String filter = "";

    public InviteMemberView(int width, int height, IViewManager manager) {
        super(width, height, manager);
    }

    @Override
    public void onQuestingDataReceived(IQuestingData questingData) {
        this.init();
    }

    @Override
    protected void init() {
        this.clear();

        addWidget(new HeaderWidget(0, 0, width, 20, TITLE, this.font));

        this.searchField = addWidget(new TextFieldWidget(this.font, 10, 25, width - 20, 20, StringTextComponent.EMPTY));
        this.searchField.setValue(this.filter);
        this.setFocused(this.searchField);
        this.searchField.setFocus(true);
        this.searchField.setResponder(filter -> {
            if (!this.filter.equals(filter)) {
                this.filter = filter;
                this.init();
            }
        });

        IQuestingData questing = QuestingDataProvider.getQuesting(this.client.level);
        QuestingGroup group = questing.getGroup(this.client.player.getUUID());

        Collection<NetworkPlayerInfo> playerInfo = this.client.getConnection().getOnlinePlayers();
        List<NetworkPlayerInfo> filtered = playerInfo.stream()
                .filter(npi -> this.canInviteMember(npi, group, questing))
                .sorted(Comparator.comparing(npi -> npi.getProfile().getName()))
                .collect(Collectors.toList());
        int profileWidth = 140;
        int profileHeight = 36;
        int maxCols = (this.width - 20) / profileWidth;
        int maxRows = (this.height - 60) / profileHeight;
        for (int y = 0; y < maxRows; y++) {
            for (int x = 0; x < maxCols; x++) {
                int index = x + y * maxRows;
                if (index >= filtered.size()) {
                    break;
                }
                NetworkPlayerInfo info = filtered.get(index);
                addWidget(new PlayerInviteWidget(10 + x * profileWidth, 50 + y * profileHeight, profileWidth, profileHeight, info, this::invitePlayerPressed));
            }
        }

        this.addWidget(new Button(width - 105, height - 25, 100, 20, new TranslationTextComponent("gui.back"), this::backButtonPressed));
    }

    @Override
    protected void renderView(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.renderView(stack, mouseX, mouseY, partialTicks);
    }

    private void backButtonPressed(Button button) {
        this.manager.setView(new QuestsView(width, height, manager));
    }

    private void invitePlayerPressed(NetworkPlayerInfo info) {
        NetworkManager.sendServerPacket(new C2S_InviteMember(info.getProfile().getId()));
    }

    private boolean canInviteMember(NetworkPlayerInfo info, QuestingGroup group, IQuestingData questing) {
        GameProfile profile = info.getProfile();
        if (group.isMember(profile.getId())) {
            return false;
        }
        if (!this.filter.isEmpty() && !profile.getName().startsWith(this.filter)) {
            return false;
        }
        QuestConfig config = GunsRPG.config.quests;
        QuestingGroup playerGroup = questing.getGroup(profile.getId());
        return playerGroup == null || config.allowInvitePlayersInGroup || playerGroup.getMembers().size() <= 1;
    }

    private static final class PlayerInviteWidget extends Widget {

        private final NetworkPlayerInfo playerInfo;
        private final Consumer<NetworkPlayerInfo> inviteHandler;

        public PlayerInviteWidget(int x, int y, int width, int height, NetworkPlayerInfo playerInfo, Consumer<NetworkPlayerInfo> inviteHandler) {
            super(x, y, width, height, StringTextComponent.EMPTY);
            this.playerInfo = playerInfo;
            this.inviteHandler = inviteHandler;
        }

        @Override
        public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            if (this.isHovered) {
                fill(stack, this.x, this.y, this.x + this.width, this.y + this.height, 0x44FFFFFF);
            }
            GameProfile profile = this.playerInfo.getProfile();
            ResourceLocation skinTexture = this.playerInfo.getSkinLocation();
            Minecraft client = Minecraft.getInstance();
            FontRenderer font = client.font;
            client.getTextureManager().bind(skinTexture);
            blit(stack, this.x + 2, this.y + 2, 32, 32, 8.0F, 8, 8, 8, 64, 64);
            font.draw(stack, TextFormatting.BOLD + profile.getName(), this.x + 37, this.y + 2, 0xFFFFFF);

            PlayerEntity player = client.level.getPlayerByUUID(profile.getId());
            if (player != null) {
                double distance = client.player.distanceTo(player);
                font.draw(stack, String.format(Locale.ROOT, "%.0fm", distance), this.x + 37, this.y + 14, 0xFFFFFF);
            }

            IQuestingData questing = QuestingDataProvider.getQuesting(client.level);
            QuestingGroup group = questing.getGroup(profile.getId());
            if (group != null && group.getMembers().size() > 1) {
                font.draw(stack, IN_GROUP, this.x + 37, this.y + 26, 0xFFFFFF);
            }
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            this.inviteHandler.accept(this.playerInfo);
        }
    }
}

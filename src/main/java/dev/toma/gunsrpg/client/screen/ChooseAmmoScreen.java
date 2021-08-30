package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.IAmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.ammo.IAmmoProvider;
import dev.toma.gunsrpg.common.item.guns.util.MaterialContainer;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSelectAmmo;
import dev.toma.gunsrpg.util.AmmoLocator;
import dev.toma.gunsrpg.util.Lifecycle;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ChooseAmmoScreen extends Screen {

    private final IAmmoProvider[] items;

    public ChooseAmmoScreen(GunItem item) {
        super(new TranslationTextComponent("screen.ammo_select"));
        Lifecycle lifecycle = GunsRPG.getModLifecycle();
        this.items = lifecycle.getAllCompatibleAmmoProviders(item);
    }

    @Override
    protected void init() {
        int scale = width / 6;
        double diff = 1.0 / items.length;
        int x = width / 2;
        int y = height / 2;
        int py = (int) (height / 3.5);
        for (int i = 0; i < items.length; i++) {
            double angle = Math.toRadians(i * diff * 360.0);
            double sin = Math.sin(angle);
            double cos = Math.abs(1.0 - Math.cos(angle));
            addButton(new AmmoButton(x + (int) (scale * sin) - 16, y + (int) (scale * cos) - py - 16, items[i]));

        }
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        ModUtils.renderColor(matrix.last().pose(), 0, 0, width, height, 0.0F, 0.0F, 0.0F, 0.4F);
        super.render(matrix, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
        minecraft.setScreen(null);
        return true;
    }

    private static class AmmoButton extends Widget {

        private final IAmmoProvider ammo;
        private final int count;
        private int requiredLevel;
        private ItemStack stack;

        public AmmoButton(int x, int y, IAmmoProvider ammo) {
            super(x, y, 32, 32, StringTextComponent.EMPTY);
            this.ammo = ammo;
            PlayerEntity player = Minecraft.getInstance().player;
            ItemStack stack = player.getMainHandItem();
            boolean isGun = stack.getItem() instanceof GunItem;
            this.count = new AmmoLocator().count(player.inventory, provider -> provider.equals(ammo));
            this.active = false;
            this.stack = new ItemStack((Item) ammo);
            PlayerData.get(player).ifPresent(data -> {
                PlayerSkills skills = data.getSkills();
                if (isGun) {
                    GunItem gun = (GunItem) stack.getItem();
                    MaterialContainer container = gun.getContainer();
                    int weaponLevel = skills.getGunData(gun).getLevel();
                    requiredLevel = container.getRequiredLevel(ammo.getMaterial());
                    active = skills.hasSkill(gun.getRequiredSkill()) && weaponLevel >= requiredLevel;
                }
            });
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            Minecraft mc = Minecraft.getInstance();
            FontRenderer font = mc.font;
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            Matrix4f pose = matrix.last().pose();
            ModUtils.renderColor(pose, x, y, x + width, y + height, 0.0F, 0.0F, 0.0F, 1.0F);
            if (active) {
                ModUtils.renderColor(pose, x + 1, y + 1, x + width - 1, y + height - 1, 0.2F, 0.7F, 0.2F, 1.0F);
            } else {
                ModUtils.renderColor(pose, x + 1, y + 1, x + width - 1, y + height - 1, 0.3F, 0.3F, 0.3F, 1.0F);
            }
            mc.getItemRenderer().renderGuiItem(stack, x + 8, y + 8);
            int countWidth = font.width(String.valueOf(count)) / 2;
            IAmmoMaterial material = ammo.getMaterial();
            ITextComponent name = material.getDisplayName();
            int nameWidth = font.width(name) / 2;
            font.drawShadow(matrix, name, x + 16 - nameWidth, y + height + 1, material.getTextColor());
            font.drawShadow(matrix, count + "", x + 24 - countWidth, y + height - 8, 0xffffff);
            if (isHovered && !active) {
                String text = String.format("Requires your weapon on level %d", requiredLevel);
                font.drawShadow(matrix, text, x + (width - font.width(text)) / 2.0F, y + height + 10, 0xdd0000);
            }
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }

        @Override
        public void onClick(double p_230982_1_, double p_230982_3_) {
            NetworkManager.sendServerPacket(new SPacketSelectAmmo(ammo.getMaterial()));
        }
    }
}

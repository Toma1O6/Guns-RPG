package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IAmmoProvider;
import dev.toma.gunsrpg.api.common.data.IProgressData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.setup.MaterialContainer;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_SelectAmmoPacket;
import dev.toma.gunsrpg.util.Lifecycle;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.locate.ammo.ItemLocator;
import dev.toma.gunsrpg.util.math.IDimensions;
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
        double diff = 1.0 / items.length;
        int x = width / 2;
        int y = height / 2;
        double horizontalScale = x / 2.4f;
        double verticalScale = y / 1.5f;
        IDimensions dimensions = IDimensions.of(width, height);
        for (int i = 0; i < items.length; i++) {
            double angle = Math.toRadians(i * diff * 360.0);
            double sin = Math.sin(angle);
            double cos = Math.cos(Math.PI - angle);
            int btnX = (int) (sin * horizontalScale) - 16;
            int btnY = (int) (cos * verticalScale) - 16;
            addButton(new AmmoButton(x + btnX, y + btnY, items[i], dimensions));
        }
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        RenderUtils.drawSolid(matrix.last().pose(), 0, 0, width, height, 0x66 << 24);
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

        private final IDimensions parentDimensions;
        private final IAmmoProvider ammo;
        private final int count;
        private int requiredLevel;
        private ItemStack stack;

        public AmmoButton(int x, int y, IAmmoProvider ammo, IDimensions parentDimensions) {
            super(x, y, 32, 32, StringTextComponent.EMPTY);
            this.ammo = ammo;
            this.parentDimensions = parentDimensions;
            PlayerEntity player = Minecraft.getInstance().player;
            ItemStack stack = player.getMainHandItem();
            boolean isGun = stack.getItem() instanceof GunItem;
            this.count = ItemLocator.sum(player.inventory, ItemLocator.filterByAmmoTypeAndMaterial(ammo));
            this.active = false;
            this.stack = new ItemStack((Item) ammo);
            PlayerData.get(player).ifPresent(data -> {
                IProgressData genericData = data.getProgressData();
                ISkillProvider provider = data.getSkillProvider();
                if (isGun) {
                    GunItem gun = (GunItem) stack.getItem();
                    MaterialContainer container = gun.getContainer();
                    int weaponLevel = genericData.getWeaponStats(gun).getLevel();
                    requiredLevel = container.getRequiredLevel(ammo.getMaterial());
                    active = provider.hasSkill(gun.getRequiredSkill()) && weaponLevel >= requiredLevel;
                }
            });
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            Minecraft mc = Minecraft.getInstance();
            FontRenderer font = mc.font;
            this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            Matrix4f pose = matrix.last().pose();
            RenderUtils.drawGradient(pose, x, y, x + width, y + height, 0xFF << 24, 0xFF << 24);
            if (active) {
                RenderUtils.drawGradient(pose, x + 1, y + 1, x + width - 1, y + height - 1, 0xFF22AA22, 0xFF008800);
            } else {
                RenderUtils.drawGradient(pose, x + 1, y + 1, x + width - 1, y + height - 1, 0xFF444444, 0xFF333333);
            }
            mc.getItemRenderer().renderGuiItem(stack, x + 8, y + 8);
            int countWidth = font.width(String.valueOf(count)) / 2;
            IAmmoMaterial material = ammo.getMaterial();
            ITextComponent name = material.getDisplayName();
            int nameWidth = font.width(name) / 2;
            font.drawShadow(matrix, name, x + 16 - nameWidth, y + height + 1, material.getTextColor());
            font.drawShadow(matrix, count + "", x + 25 - countWidth, y + height - 11, 0xffffff);
            if (isHovered && !active) {
                String text = String.format("Requires weapon level %d", requiredLevel);
                int textWidth = font.width(text);
                int centerX = parentDimensions.getWidth() / 2 - textWidth / 2;
                int centerY = parentDimensions.getHeight() / 2 - font.lineHeight / 2;
                font.drawShadow(matrix, text, centerX, centerY, 0xdd0000);
            }
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
        }

        @Override
        public void onClick(double p_230982_1_, double p_230982_3_) {
            NetworkManager.sendServerPacket(new C2S_SelectAmmoPacket(ammo.getMaterial()));
        }
    }
}

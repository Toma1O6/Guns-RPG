package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.IAmmoProvider;
import dev.toma.gunsrpg.api.common.data.IDebuffs;
import dev.toma.gunsrpg.api.common.data.IKillData;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IProgressData;
import dev.toma.gunsrpg.client.render.debuff.DebuffRenderManager;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.setup.AbstractGun;
import dev.toma.gunsrpg.config.ModConfig;
import dev.toma.gunsrpg.resource.util.functions.RangedFunction;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.Lifecycle;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.locate.ammo.ItemLocator;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class HUDRenderer {

    @SubscribeEvent
    public void cancelOverlays(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            if (!ModConfig.clientConfig.developerMode.get()) {
                ItemStack stack = player.getMainHandItem();
                if (stack.getItem() instanceof AbstractGun) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void renderOverlays(RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        LazyOptional<IPlayerData> optional = PlayerData.get(player);
        MatrixStack matrixStack = event.getMatrixStack();
        MainWindow window = event.getWindow();
        FontRenderer font = mc.font;
        float partialTicks = event.getPartialTicks();
        optional.ifPresent(data -> {
            IAttributeProvider attributeProvider = data.getAttributes();
            IDebuffs debuffs = data.getDebuffControl();
            renderBloodmoonInfo(matrixStack, font, window, player);
            int height = window.getGuiScaledHeight();
            renderDebuffs(matrixStack, attributeProvider, debuffs, 0, height - 50, partialTicks);
            renderProgressionOnScreen(matrixStack, font, window, data, player);
        });
    }

    // BLOODMOON -----------------------------------------

    private void renderBloodmoonInfo(MatrixStack matrixStack, FontRenderer font, MainWindow window, PlayerEntity player) {
        World world = player.level;
        long actualDay = world.getDayTime() / 24000L;
        int cycle = ModConfig.worldConfig.bloodmoonCycle.get();
        if (cycle == -1) return;
        boolean isBloodmoonDay = actualDay > 0 && actualDay % cycle == 0;
        int leftToBloodmoon = isBloodmoonDay ? 0 : (int) (cycle - actualDay % cycle);
        boolean isNight = world.getDayTime() % 24000L >= 12500L;
        String dayString = isNight ? leftToBloodmoon + "!" : String.valueOf(leftToBloodmoon);
        int dayStringWidth = font.width(dayString);
        int xPos = window.getGuiScaledWidth() - 9;
        boolean shouldRenderBackground = hasActiveVisibleEffect(player);
        int color = isBloodmoonDay ? 0xff << 16 : RangedFunction.BETWEEN_EXCLUSIVE.isWithinRange(leftToBloodmoon, 0, 3) ? 0xffff << 8 : 0xffffff;
        if (shouldRenderBackground) {
            Matrix4f pose = matrixStack.last().pose();
            renderBloodmoonWarningBackground(pose, xPos, dayStringWidth, color);
        }
        font.drawShadow(matrixStack, dayString, 0.5F + xPos - dayStringWidth / 2.0F, 6, color);
    }

    private void renderBloodmoonWarningBackground(Matrix4f pose, int x, int textWidth, int color) {
        float left = x - textWidth / 2.0F;
        float right = left + textWidth;
        RenderUtils.drawSolid(pose, left - 3, 3, right + 3, 16, 0xFF << 24 | color);
        RenderUtils.drawSolid(pose, left - 2, 4, right + 2, 15, 0xFF << 24);
    }

    private boolean hasActiveVisibleEffect(LivingEntity entity) {
        for (EffectInstance instance : entity.getActiveEffects()) {
            if (instance.isVisible()) {
                return true;
            }
        }
        return false;
    }

    // DEBUFFS ------------------------------------------------------

    private void renderDebuffs(MatrixStack poseStack, IAttributeProvider attributeProvider, IDebuffs debuffs, int left, int top, float partialTicks) {
        ClientSideManager manager = ClientSideManager.instance();
        DebuffRenderManager renderManager = manager.getDebuffRenderManager();
        renderManager.drawDebuffsOnScreen(poseStack, attributeProvider, debuffs, left, top, partialTicks);
    }

    // PROGRESSION -------------------------------------------------

    private void renderProgressionOnScreen(MatrixStack matrix, FontRenderer font, MainWindow window, IPlayerData data, PlayerEntity player) {
        int windowWidth = window.getGuiScaledWidth();
        int windowHeight = window.getGuiScaledHeight();
        int barWidth = 26;
        int x = windowWidth - barWidth - 34;
        int y = windowHeight - 22;
        ItemStack stack = player.getMainHandItem();
        Matrix4f pose = matrix.last().pose();
        IProgressData progression = data.getProgressData();
        if (stack.getItem() instanceof GunItem) {
            GunItem gunItem = (GunItem) stack.getItem();
            IKillData killData = progression.getWeaponStats(gunItem);
            Lifecycle lifecycle = GunsRPG.getModLifecycle();
            IAmmoProvider provider = lifecycle.getAmmoForWeapon(gunItem, stack);
            if (provider != null) {
                boolean jammed = gunItem.isJammed(stack);
                boolean broken = stack.getDamageValue() == stack.getMaxDamage();
                String text = this.getAmmoString(gunItem, stack, player.inventory, provider, jammed, broken);
                barWidth = font.width(text);
                x = windowWidth - barWidth - 34;
                renderProgressionBar(killData, pose, x, y, barWidth + 22, y + 7, 0xFFFFFF << 8, 0xFF8888 << 8);
                Minecraft mc = Minecraft.getInstance();
                mc.getItemRenderer().renderGuiItem(new ItemStack((Item) provider), x, y - 18);
                if (jammed || broken)
                    font.drawShadow(matrix, text, x + 19, y - 14, 0xCC << 16);
                else
                    font.draw(matrix, text, x + 19, y - 14, 0xFFFFFF);
            }
        }
        renderProgressionBar(progression, pose, x, y + 10, barWidth + 22, y + 17, 0xFF00FFFF, 0xFF008888);
    }

    private String getAmmoString(GunItem item, ItemStack stack, IInventory inventory, IAmmoProvider provider, boolean jammed, boolean broken) {
        int loaded = item.getAmmo(stack);
        int available = ItemLocator.countItems(inventory, ItemLocator.compatible(provider));
        return jammed ? "JAMMED" : broken ? "DESTROYED" : loaded + " / " + available;
    }

    private void renderProgressionBar(IKillData data, Matrix4f pose, int left, int top, int width, int bottom, int colorPrimary, int colorSecondary) {
        int count = data.getKills();
        int required = data.getRequiredKillCount();
        boolean isMaxLvl = data.getLevel() == data.getLevelLimit();
        float levelProgress = isMaxLvl ? 1.0F : count / (float) required;
        RenderUtils.drawSolid(pose, left, top, left + width, bottom, 0xFF << 24);
        RenderUtils.drawGradient(pose, left + 2, top + 2, left + (int) (levelProgress * (width - 2)), bottom - 2, colorPrimary, colorSecondary);
    }
}

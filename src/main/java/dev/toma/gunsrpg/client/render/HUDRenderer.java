package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.client.IHudSkillRenderer;
import dev.toma.gunsrpg.api.common.IAmmoProvider;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.*;
import dev.toma.gunsrpg.api.common.skill.ICooldown;
import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.client.OverlayPlacement;
import dev.toma.gunsrpg.client.render.debuff.DebuffRenderManager;
import dev.toma.gunsrpg.client.render.infobar.IDataModel;
import dev.toma.gunsrpg.client.render.skill.SkillRendererRegistry;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.item.StashDetectorItem;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.setup.AbstractGun;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.quest.QuestStatus;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.client.ConfigurableOverlay;
import dev.toma.gunsrpg.resource.util.functions.RangedFunction;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.Lifecycle;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.locate.ammo.ItemLocator;
import dev.toma.gunsrpg.util.math.IVec2i;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
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

import java.util.Set;
import java.util.stream.Collectors;

public final class HUDRenderer {

    @SubscribeEvent
    public void cancelOverlays(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            if (!ClientSideManager.config.developerMode) {
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
        if (mc.screen != null) return;
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
            renderDebuffs(matrixStack, window, attributeProvider, debuffs, 0, height - 50, partialTicks);
            renderProgressionOnScreen(matrixStack, font, window, data, player);
            renderSkillsOnHUD(matrixStack, window, data);
        });
        QuestingDataProvider.getData(mc.level).ifPresent(questing -> {
            renderQuestOverlay(matrixStack, font, window, questing, player);
            // TODO render current party info
        });
    }

    // QUEST ---------------------------------------------

    private void renderQuestOverlay(MatrixStack matrix, FontRenderer font, MainWindow window, IQuestingData data, PlayerEntity player) {
        Quest<?> quest = data.getActiveQuestForPlayer(player);
        if (quest == null)
            return;
        QuestStatus status = quest.getStatus();
        if (status != QuestStatus.ACTIVE && status != QuestStatus.COMPLETED)
            return;
        ConfigurableOverlay overlay = ClientSideManager.config.questOverlay;
        if (!overlay.enabled)
            return;
        IDataModel display = quest.getDisplayModel(player.getUUID());
        if (display != null) {
            int width = window.getGuiScaledWidth();
            int height = window.getGuiScaledHeight();
            IVec2i position = OverlayPlacement.getPlacement(overlay, 0, 0, width, height, display.getModelWidth(), display.getModelHeight());
            display.renderModel(matrix, font, position.x(), position.y(), true);
        }
    }

    // SKILLS --------------------------------------------

    @SuppressWarnings("unchecked")
    private <S extends ISkill & ICooldown> void renderSkillsOnHUD(MatrixStack stack, MainWindow window, IPlayerData data) {
        ISkillProvider provider = data.getSkillProvider();
        Set<S> displayables = SkillRendererRegistry.getDisplayableSkills().stream()
                .map(type -> (S) SkillUtil.getTopHierarchySkill(type, provider))
                .filter(skill -> skill != null && !skill.getType().isDisabled())
                .collect(Collectors.toSet());
        int renderIndex = 0;
        for (S skill : displayables) {
            if (skill.getCooldown() > 0) continue;
            SkillType<S> type = (SkillType<S>) skill.getType();
            IHudSkillRenderer<S> renderer = SkillRendererRegistry.getHudRenderer(type);
            renderer.renderOnHUD(stack, skill, 5, window.getGuiScaledHeight() - 20, renderIndex++);
        }
    }

    // BLOODMOON -----------------------------------------

    private void renderBloodmoonInfo(MatrixStack matrixStack, FontRenderer font, MainWindow window, PlayerEntity player) {
        World world = player.level;
        long actualDay = world.getDayTime() / 24000L;
        int cycle = GunsRPG.config.world.bloodmoonCycle;
        ConfigurableOverlay bloodmoonOverlay = ClientSideManager.config.bloodmoonCounterOverlay;
        if (cycle == -1 || !bloodmoonOverlay.enabled) return;
        boolean isBloodmoonDay = actualDay > 0 && (cycle == 0 || actualDay % cycle == 0);
        int leftToBloodmoon = isBloodmoonDay ? 0 : (int) (cycle - actualDay % cycle);
        boolean isNight = world.getDayTime() % 24000L >= 12500L;
        String dayString = isNight && isBloodmoonDay ? leftToBloodmoon + "!" : String.valueOf(leftToBloodmoon);
        int dayStringWidth = font.width(dayString);
        IVec2i placement = OverlayPlacement.getPlacement(bloodmoonOverlay, 0, 0, window.getGuiScaledWidth(), window.getGuiScaledHeight(), dayStringWidth, 13);
        boolean shouldRenderBackground = hasActiveVisibleEffect(player);
        int color = isBloodmoonDay ? 0xff << 16 : RangedFunction.BETWEEN_EXCLUSIVE.isWithinRange(leftToBloodmoon, 0, 3) ? 0xffff << 8 : 0xffffff;
        int xPos = placement.x();
        int yPos = placement.y();
        if (shouldRenderBackground) {
            Matrix4f pose = matrixStack.last().pose();
            renderBloodmoonWarningBackground(pose, xPos, yPos, dayStringWidth, color);
        }
        font.drawShadow(matrixStack, dayString, xPos, yPos, color);
    }

    private void renderBloodmoonWarningBackground(Matrix4f pose, int left, int top, int textWidth, int color) {
        float right = left + textWidth;
        RenderUtils.drawSolid(pose, left - 3, top - 3, right + 2, top + 10, 0xFF << 24 | color);
        RenderUtils.drawSolid(pose, left - 2, top - 2, right + 1, top + 9, 0xFF << 24);
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

    private void renderDebuffs(MatrixStack poseStack, MainWindow window, IAttributeProvider attributeProvider, IDebuffs debuffs, int left, int top, float partialTicks) {
        ClientSideManager manager = ClientSideManager.instance();
        DebuffRenderManager renderManager = manager.getDebuffRenderManager();
        renderManager.drawDebuffsOnScreen(poseStack, window, attributeProvider, debuffs, left, top, partialTicks);
    }

    // PROGRESSION -------------------------------------------------

    private void renderProgressionOnScreen(MatrixStack matrix, FontRenderer font, MainWindow window, IPlayerData data, PlayerEntity player) {
        int windowWidth = window.getGuiScaledWidth();
        int windowHeight = window.getGuiScaledHeight();
        int barWidth = 26;
        ConfigurableOverlay overlay = ClientSideManager.config.levelProgressOverlay;
        if (!overlay.enabled) // TODO implement position configuration
            return;
        ItemStack stack = player.getMainHandItem();
        boolean renderWeaponProgression = stack.getItem() instanceof GunItem;
        int x = windowWidth - barWidth - 34;
        int y = windowHeight - 22;
        IProgressData progression = data.getProgressData();
        if (renderWeaponProgression) {
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
                renderProgressionBar(killData, matrix, font, x, y, barWidth + 22, y + 7, 0xFFFFFF << 8, 0xFF8888 << 8);
                Minecraft mc = Minecraft.getInstance();
                mc.getItemRenderer().renderGuiItem(new ItemStack((Item) provider), x, y - 18);
                if (jammed || broken)
                    font.drawShadow(matrix, text, x + 19, y - 14, 0xCC << 16);
                else
                    font.draw(matrix, text, x + 19, y - 14, 0xFFFFFF);
            }
        } else if (stack.getItem() == ModItems.STASH_DETECTOR) {
            Minecraft mc = Minecraft.getInstance();
            int batteryCount = ItemLocator.sum(player.inventory, StashDetectorItem::isValidBatterySource);
            String text = String.valueOf(batteryCount);
            barWidth = font.width(text);
            x = windowWidth - barWidth - 34;
            mc.getItemRenderer().renderGuiItem(new ItemStack(ModItems.BATTERY), x, y - 8);
            font.draw(matrix, text, x + 19, y - 2, 0xFFFFFF);
        }
        renderProgressionBar(progression, matrix, font, x, y + 10, barWidth + 22, y + 17, 0xFF00FFFF, 0xFF008888);
    }

    private String getAmmoString(GunItem item, ItemStack stack, IInventory inventory, IAmmoProvider provider, boolean jammed, boolean broken) {
        int loaded = item.getAmmo(stack);
        return jammed ? "JAMMED" : broken ? "DESTROYED" : loaded + " / " + ItemLocator.sum(inventory, ItemLocator.filterByAmmoTypeAndMaterial(provider));
    }

    private void renderProgressionBar(IKillData data, MatrixStack matrixStack, FontRenderer font, int left, int top, int width, int bottom, int colorPrimary, int colorSecondary) {
        Matrix4f pose = matrixStack.last().pose();
        int count = data.getKills();
        int required = data.getRequiredKillCount();
        int level = data.getLevel();
        boolean isMaxLvl = level == data.getLevelLimit();
        float levelProgress = isMaxLvl ? 1.0F : count / (float) required;
        RenderUtils.drawSolid(pose, left, top, left + width, bottom, 0xFF << 24);
        RenderUtils.drawGradient(pose, left + 2, top + 2, left + (int) (levelProgress * (width - 2)), bottom - 2, colorPrimary, colorSecondary);
        // Level indicator
        String currentLevel = String.valueOf(level);
        int currentLevelWidth = font.width(currentLevel);
        font.draw(matrixStack, currentLevel, left - currentLevelWidth - 2, top, colorPrimary);
    }
}

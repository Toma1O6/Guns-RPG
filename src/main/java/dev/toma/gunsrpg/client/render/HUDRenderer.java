package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.client.IHudSkillRenderer;
import dev.toma.gunsrpg.api.client.IProgressionDetailProvider;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IDebuffs;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.api.common.skill.ICooldown;
import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.client.OverlayPlacement;
import dev.toma.gunsrpg.client.render.debuff.DebuffRenderManager;
import dev.toma.gunsrpg.client.render.infobar.IDataModel;
import dev.toma.gunsrpg.client.render.skill.SkillRendererRegistry;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.item.guns.setup.AbstractGun;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.quest.QuestStatus;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.client.ConfigurableOverlay;
import dev.toma.gunsrpg.config.client.PartyOverlayConfiguration;
import dev.toma.gunsrpg.resource.util.functions.RangedFunction;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.RenderUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.math.IVec2i;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
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
            renderPartyOverlay(matrixStack, font, window, questing, player);
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
            display.prepare(matrix, font);
            IVec2i position = OverlayPlacement.getPlacement(overlay, 0, 0, width, height, display.getModelWidth(), display.getModelHeight());
            display.renderModel(matrix, font, position.x(), position.y(), true);
        }
    }

    // SKILLS --------------------------------------------

    @SuppressWarnings("unchecked")
    private <S extends ISkill & ICooldown> void renderSkillsOnHUD(MatrixStack stack, MainWindow window, IPlayerData data) {
        ConfigurableOverlay overlay = ClientSideManager.config.activeSkillOverlay;
        if (!overlay.enabled)
            return;
        ISkillProvider provider = data.getSkillProvider();
        List<S> displayables = SkillRendererRegistry.getDisplayableSkills().stream()
                .map(type -> (S) SkillUtil.getTopHierarchySkill(type, provider))
                .filter(skill -> skill != null && !skill.getType().isDisabled() && skill.getCooldown() <= 0)
                .distinct()
                .collect(Collectors.toList());
        int skillsWidth = displayables.size() * 20;
        int skillsHeight = 20;
        IVec2i position = OverlayPlacement.getPlacement(overlay, 0, 0, window.getGuiScaledWidth(), window.getGuiScaledHeight(), skillsWidth, skillsHeight);
        for (int i = 0; i < displayables.size(); i++) {
            S skill = displayables.get(i);
            SkillType<S> type = (SkillType<S>) skill.getType();
            IHudSkillRenderer<S> renderer = SkillRendererRegistry.getHudRenderer(type);
            renderer.renderOnHUD(stack, skill, position.x() + i * 20, position.y(), 20, 20);
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
        ConfigurableOverlay overlay = ClientSideManager.config.levelProgressOverlay;
        if (!overlay.enabled)
            return;
        ProgressionRenderer buffer = ProgressionRenderer.getBuffer();
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof IProgressionDetailProvider) {
            IProgressionDetailProvider detailProvider = (IProgressionDetailProvider) stack.getItem();
            buffer.add(detailProvider);
        }
        buffer.add(data.getProgressData());
        buffer.draw(matrix, font, window, player, data);
    }

    // PARTY -------------------------------------------------

    private void renderPartyOverlay(MatrixStack matrix, FontRenderer font, MainWindow window, IQuestingData questing, PlayerEntity player) {
        PartyOverlayConfiguration overlay = ClientSideManager.config.partyOverlay;
        if (!overlay.enabled)
            return;
        QuestingGroup party = questing.getOrCreateGroup(player);
        Quest<?> quest = questing.getActiveQuest(party);
        if (party.getMemberCount() <= 1 || (overlay.requireActiveQuest && quest == null))
            return;
        Collection<UUID> members = party.getMembers();
        int index = 0;
        int width = 60;
        int singleEntryHeight = 11;
        int height = members.size() * singleEntryHeight;
        Minecraft client = Minecraft.getInstance();
        for (UUID uuid : members) {
            String name = party.getName(uuid);
            width = Math.max(width, font.width(name));
        }
        width += 20; // extra space for icon and health count
        ClientWorld level = client.level;
        IVec2i pos = OverlayPlacement.getPlacement(overlay, 0, 0, window.getGuiScaledWidth(), window.getGuiScaledHeight(), width, height);
        Matrix4f pose = matrix.last().pose();
        for (UUID uuid : members) {
            String name = party.getName(uuid);
            int health = party.getHealth(level, uuid);
            int x = pos.x();
            int y = pos.y() + singleEntryHeight * index;
            String healthStatus = String.valueOf(health);
            int healthWidth = font.width(healthStatus);
            RenderUtils.drawSolid(pose, x, y, x + width, y + singleEntryHeight, 0x66 << 24);
            client.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
            RenderUtils.drawTex(pose, x + width - healthWidth - 11, y + 1, x + width - healthWidth - 2, y + 10, 52.0F / 255.0F, 0.0F, 61.0F / 255.0F, 9.0F / 255.0F);
            font.draw(matrix, name, x + 2, y + 2, 0xFFFFFF);
            font.draw(matrix, healthStatus, x + width - healthWidth, y + 2, health <= 5 ? 0xCC0000 : 0xFFFFFF);
            ++index;
        }
    }
}

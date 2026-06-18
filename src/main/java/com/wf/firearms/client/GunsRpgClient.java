package com.wf.firearms.client;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.client.debuff.ClientDebuffState;
import com.wf.firearms.client.gui.SkillTreeScreen;
import com.wf.firearms.client.model.BloodmoonGolemModel;
import com.wf.firearms.client.model.RocketAngelModel;
import com.wf.firearms.client.model.FirearmClientModels;
import com.wf.firearms.client.FirearmClientTextures;
import com.wf.firearms.combat.FirearmCombat;
import com.wf.firearms.item.FirearmItem;
import com.wf.firearms.client.render.entity.AirdropRenderer;
import com.wf.firearms.client.render.entity.BloodmoonGolemRenderer;
import com.wf.firearms.client.render.entity.BulletEntityRenderer;
import com.wf.firearms.client.render.entity.GrenadeRenderer;
import com.wf.firearms.client.render.entity.ExplosiveSkeletonRenderer;
import com.wf.firearms.client.render.entity.LaunchedExplosiveRenderer;
import com.wf.firearms.client.render.entity.RocketAngelRenderer;
import com.wf.firearms.client.render.entity.ZombieGunnerRenderer;
import com.wf.firearms.registry.ModEntities;
import com.wf.firearms.registry.ModMenuTypes;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class GunsRpgClient {
    private GunsRpgClient() {}

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModMenuTypes.GUNSMITH_TABLE.get(), GunsmithScreen::new);
            MenuScreens.register(ModMenuTypes.CULINARY_TABLE.get(), CulinaryScreen::new);
            MenuScreens.register(ModMenuTypes.REPAIR_STATION.get(), RepairStationScreen::new);
            MenuScreens.register(ModMenuTypes.MEDICAL_STATION.get(), MedicalStationScreen::new);
            MenuScreens.register(ModMenuTypes.AIRDROP.get(), ContainerScreen::new);
            FirearmRenderers.init();
            FirearmClientModels.verifyGunCount();
            FirearmClientTextures.clearCache();
        });
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.BULLET.get(), BulletEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.AIRDROP.get(), AirdropRenderer::new);
        event.registerEntityRenderer(ModEntities.GRENADE.get(), GrenadeRenderer::new);
        event.registerEntityRenderer(ModEntities.LAUNCHED_EXPLOSIVE.get(), LaunchedExplosiveRenderer::new);
        event.registerEntityRenderer(ModEntities.BLOODMOON_GOLEM.get(), BloodmoonGolemRenderer::new);
        event.registerEntityRenderer(ModEntities.ROCKET_ANGEL.get(), RocketAngelRenderer::new);
        event.registerEntityRenderer(ModEntities.ZOMBIE_GUNNER.get(), ZombieGunnerRenderer::new);
        event.registerEntityRenderer(ModEntities.EXPLOSIVE_SKELETON.get(), ExplosiveSkeletonRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BloodmoonGolemModel.LAYER, BloodmoonGolemModel::createBodyLayer);
        event.registerLayerDefinition(RocketAngelModel.LAYER, RocketAngelModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(KeyBindings.OPEN_SKILL_TREE);
        event.register(KeyBindings.RELOAD);
        event.register(KeyBindings.UNJAM);
        event.register(KeyBindings.FIREMODE);
        event.register(KeyBindings.SIGHT_COLOR);
        event.register(KeyBindings.SIGHT_TYPE);
        event.register(KeyBindings.EMERGENCY_AIRDROP);
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("wf_debuffs", DebuffHudOverlay.OVERLAY);
        event.registerAboveAll("wf_firearm_hud", FirearmHudOverlay.OVERLAY);
        event.registerAbove(
                VanillaGuiOverlay.CROSSHAIR.id(),
                "wf_tacz_unjam",
                com.wf.firearms.client.compat.tacz.TaczUnjamHudOverlay.OVERLAY);
        event.registerAbove(VanillaGuiOverlay.HOTBAR.id(), "wf_weapon_wear", WeaponWearHotbarOverlay.OVERLAY);
    }

    @Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeBus {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END) {
                return;
            }
            ClientDebuffState.clientTick();
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.screen != null) {
                return;
            }
            while (KeyBindings.OPEN_SKILL_TREE.consumeClick()) {
                mc.setScreen(new SkillTreeScreen());
            }
            while (KeyBindings.EMERGENCY_AIRDROP.consumeClick()) {
                ClientSkillActions.requestActiveSkill("god_help_us");
            }
            ClientFirearmInput.tick(mc);
            com.wf.firearms.client.compat.tacz.TaczClientInput.tick(mc);
        }

        @SubscribeEvent
        public static void hideVanillaCrosshair(RenderGuiOverlayEvent.Pre event) {
            if (!VanillaGuiOverlay.CROSSHAIR.type().equals(event.getOverlay().id())) {
                return;
            }
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && !FirearmCombat.getHeldGun(mc.player).isEmpty()) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onComputeFov(ViewportEvent.ComputeFov event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                return;
            }
            var gun = FirearmCombat.getHeldGun(mc.player);
            if (gun.isEmpty() || !(gun.getItem() instanceof FirearmItem)) {
                return;
            }
            if (com.wf.firearms.combat.FirearmStackState.isAiming(gun)) {
                event.setFOV(event.getFOV() * 0.82f);
            }
        }
    }
}

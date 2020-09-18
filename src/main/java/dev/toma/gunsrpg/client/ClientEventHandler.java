package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.animation.AnimationManager;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IHandRenderer;
import dev.toma.gunsrpg.client.animation.impl.SprintingAnimation;
import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.capability.object.GunData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.capability.object.ScopeData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.IAmmoProvider;
import dev.toma.gunsrpg.common.item.guns.ammo.ItemAmmo;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.skills.core.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.interfaces.OverlayRenderer;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.debuffs.Debuff;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSetAiming;
import dev.toma.gunsrpg.network.packet.SPacketSetShooting;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.object.OptionalObject;
import dev.toma.gunsrpg.util.object.ShootingManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = GunsRPG.MODID)
public class ClientEventHandler {

    public static final ResourceLocation SCOPE = GunsRPG.makeResource("textures/icons/scope_overlay.png");
    public static final ResourceLocation SCOPE_OVERLAY = GunsRPG.makeResource("textures/icons/scope_full.png");
    private static final RisingEdgeChecker startSprintListener = new RisingEdgeChecker(() -> {
        EntityPlayer player = Minecraft.getMinecraft().player;
        ItemStack stack = player.getHeldItemMainhand();
        if (stack.getItem() instanceof GunItem && !PlayerDataFactory.get(player).getReloadInfo().isReloading()) {
            AnimationManager.sendNewAnimation(Animations.SPRINT, new SprintingAnimation());
        }
    }, EntityPlayer::isSprinting);
    public static OptionalObject<Float> preAimFov = OptionalObject.empty();
    public static OptionalObject<Float> preAimSens = OptionalObject.empty();
    static float prevAimingProgress;
    static boolean burst;
    static int shotsLeft = 2;

    @SubscribeEvent
    public static void cancelOverlays(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            ScaledResolution resolution = event.getResolution();
            ItemStack stack = player.getHeldItemMainhand();
            if (stack.getItem() instanceof GunItem) {
                event.setCanceled(true);
                PlayerData data = PlayerDataFactory.get(player);
                if (data.getAimInfo().progress >= 0.9F) {
                    if (stack.getItem() == ModRegistry.GRPGItems.SNIPER_RIFLE && PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.SR_SCOPE) || stack.getItem() == ModRegistry.GRPGItems.CROSSBOW && PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.CROSSBOW_SCOPE)) {
                        if (GRPGConfig.clientConfig.scopeRenderer.isTextureOverlay()) {
                            ModUtils.renderTexture(0, 0, resolution.getScaledWidth(), resolution.getScaledHeight(), SCOPE_OVERLAY);
                        } else {
                            int left = resolution.getScaledWidth() / 2 - 16;
                            int top = resolution.getScaledHeight() / 2 - 16;
                            ModUtils.renderTexture(left, top, left + 32, top + 32, SCOPE);
                        }
                    } else if ((PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.SMG_RED_DOT) && stack.getItem() == ModRegistry.GRPGItems.SMG) || (PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.AR_RED_DOT) && stack.getItem() == ModRegistry.GRPGItems.ASSAULT_RIFLE)) {
                        ScopeData scopeData = data.getScopeData();
                        float left = resolution.getScaledWidth() / 2f - 8f;
                        float top = resolution.getScaledHeight() / 2f - 8f;
                        float x2 = left + 16;
                        float y2 = top + 16;
                        double us = scopeData.getTexStartX();
                        double vs = scopeData.getTexStartY();
                        double ue = scopeData.getTexEndX();
                        double ve = scopeData.getTexEndY();
                        //draw red dot
                        Minecraft.getMinecraft().getTextureManager().bindTexture(ScopeData.TEXTURES);
                        GlStateManager.enableBlend();
                        Tessellator tessellator = Tessellator.getInstance();
                        BufferBuilder builder = tessellator.getBuffer();
                        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
                        builder.pos(left, y2, 0).tex(us, ve).endVertex();
                        builder.pos(x2, y2, 0).tex(ue, ve).endVertex();
                        builder.pos(x2, top, 0).tex(ue, vs).endVertex();
                        builder.pos(left, top, 0).tex(us, vs).endVertex();
                        tessellator.draw();
                        GlStateManager.disableBlend();
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.player;
            PlayerData data = PlayerDataFactory.get(player);
            FontRenderer renderer = mc.fontRenderer;
            long day = player.world.getWorldTime() / 24000L;
            int cycle = GRPGConfig.worldConfig.bloodmoonCycle;
            boolean b = day % cycle == 0 && day > 0;
            long l = b ? 0 : cycle - day % cycle;
            String remainingDays = l + "";
            ScaledResolution resolution = event.getResolution();
            mc.fontRenderer.drawStringWithShadow(remainingDays, resolution.getScaledWidth() - 10 - mc.fontRenderer.getStringWidth(remainingDays) / 2f, 6, b ? 0xff0000 : l > 0 && l < 3 ? 0xffff00 : 0xffffff);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            ItemStack stack = player.getHeldItemMainhand();
            int width = 26;
            int x = resolution.getScaledWidth() - width - 34;
            int y = resolution.getScaledHeight() - 22;
            PlayerSkills skills = data.getSkills();
            if (stack.getItem() instanceof GunItem) {
                GunItem gun = (GunItem) stack.getItem();
                GunData gunData = skills.getGunData((GunItem) stack.getItem());
                int gunKills = gunData.getKills();
                int gunRequiredKills = gunData.getRequiredKills();
                int ammo = gun.getAmmo(stack);
                int max = gun.getMaxAmmo(player);
                float f = gunData.isAtMaxLevel() ? 1.0F : gunKills / (float) gunRequiredKills;
                ItemAmmo itemAmmo = ItemAmmo.getAmmoFor(gun, stack);
                if (itemAmmo != null) {
                    int c = 0;
                    for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                        ItemStack itemStack = player.inventory.getStackInSlot(i);
                        if (itemStack.getItem() instanceof IAmmoProvider) {
                            IAmmoProvider ammoProvider = (IAmmoProvider) itemStack.getItem();
                            if (ammoProvider.getMaterial() == itemAmmo.getMaterial() && ammoProvider.getAmmoType() == itemAmmo.getAmmoType()) {
                                c += itemStack.getCount();
                            }
                        }
                    }
                    String text = ammo + " / " + c;
                    width = renderer.getStringWidth(text);
                    x = resolution.getScaledWidth() - width - 34;
                    ModUtils.renderColor(x, y, x + width + 22, y + 7, 0.0F, 0.0F, 0.0F, 1.0F);
                    ModUtils.renderColor(x + 2, y + 2, x + (int) (f * (width + 20)), y + 5, 1.0F, 1.0F, 0.0F, 1.0F);
                    mc.getRenderItem().renderItemIntoGUI(new ItemStack(itemAmmo), x, y - 18);
                    mc.fontRenderer.drawStringWithShadow(text, x + 19, y - 14, 0xffffff);
                }
            }
            int kills = skills.getKills();
            int required = skills.getRequiredKills();
            float levelProgress = skills.isMaxLevel() ? 1.0F : kills / (float) required;
            ModUtils.renderColor(x, y + 10, x + width + 22, y + 17, 0.0F, 0.0F, 0.0F, 1.0F);
            ModUtils.renderColor(x + 2, y + 12, x + (int) (levelProgress * (width + 20)), y + 15, 0.0F, 1.0F, 1.0F, 1.0F);
            if (data != null) {
                DebuffData debuffData = data.getDebuffData();
                int offset = 0;
                for (Debuff debuff : debuffData.getDebuffs()) {
                    if (!debuff.isActive()) continue;
                    int yStart = event.getResolution().getScaledHeight() + GRPGConfig.clientConfig.debuffOverlay.y - 50;
                    debuff.draw(GRPGConfig.clientConfig.debuffOverlay.x, yStart + offset * 18, 50, 18, event.getPartialTicks(), renderer);
                    ++offset;
                }
            }
            int renderIndex = 0;
            List<ISkill> list = skills.getUnlockedSkills().get(SkillCategory.SURVIVAL);
            if (list == null) return;
            int left = 5;
            int top = resolution.getScaledHeight() - 25;
            List<ISkill> renderSkills = new ArrayList<>();
            for (ISkill skill : list) {
                if (skill instanceof OverlayRenderer) {
                    skill = SkillUtil.getBestSkillFromOverrides(skill, player);
                    if(!renderSkills.contains(skill)) renderSkills.add(skill);
                }
            }
            for(ISkill skill : renderSkills) {
                OverlayRenderer overlayRenderer = (OverlayRenderer) skill;
                if(skill.apply(player) && overlayRenderer.shouldRenderOnHUD()) {
                    overlayRenderer.renderInHUD(skill, renderIndex, left, top);
                    ++renderIndex;
                }
            }
        }
    }

    @SubscribeEvent
    public static void mouseInputEvent(InputEvent.MouseInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        GameSettings settings = mc.gameSettings;
        if (player != null) {
            GunItem item = ShootingManager.getGunFrom(player);
            if (item != null) {
                if (settings.keyBindAttack.isPressed()) {
                    Firemode firemode = item.getFiremode(player.getHeldItemMainhand());
                    if (firemode == Firemode.FULL_AUTO) {
                        PlayerDataFactory.get(player).setShooting(true);
                        NetworkManager.toServer(new SPacketSetShooting(true));
                    } else if (firemode == Firemode.SINGLE) {
                        ShootingManager.shootSingle(player, player.getHeldItemMainhand());
                    } else {
                        if (!burst) {
                            burst = true;
                            shotsLeft = 2;
                        }
                    }
                } else if (settings.keyBindUseItem.isPressed() && AnimationManager.getAnimationByID(Animations.REBOLT) == null && !player.isSprinting()) {
                    boolean aim = !PlayerDataFactory.get(player).getAimInfo().aiming;
                    if (aim) {
                        preAimFov.map(settings.fovSetting);
                        preAimSens.map(settings.mouseSensitivity);
                        if (item == ModRegistry.GRPGItems.SNIPER_RIFLE && PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.SR_SCOPE)) {
                            settings.mouseSensitivity = preAimSens.get() * 0.3F;
                            settings.fovSetting = 15.0F;
                        } else if (item == ModRegistry.GRPGItems.CROSSBOW && PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.CROSSBOW_SCOPE)) {
                            settings.mouseSensitivity = preAimSens.get() * 0.4F;
                            settings.fovSetting = 25.0F;
                        }
                        AnimationManager.sendNewAnimation(Animations.AIMING, item.createAimAnimation());
                    } else {
                        preAimFov.ifPresent(value -> settings.fovSetting = value);
                        preAimSens.ifPresent(value -> settings.mouseSensitivity = value);
                    }
                    NetworkManager.toServer(new SPacketSetAiming(aim));
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderHandEvent(RenderSpecificHandEvent event) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        ItemStack stack = event.getItemStack();
        if(event.getHand() == EnumHand.OFF_HAND) {
            if(event.getItemStack().getItem() == Items.SHIELD && player.getHeldItemMainhand().getItem() instanceof GunItem) {
                event.setCanceled(true);
            }
        }
        if (PlayerDataFactory.get(player).getAimInfo().isAiming() && GRPGConfig.clientConfig.scopeRenderer.isTextureOverlay() && (PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.SR_SCOPE) && stack.getItem() == ModRegistry.GRPGItems.SNIPER_RIFLE || PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.CROSSBOW_SCOPE) && stack.getItem() == ModRegistry.GRPGItems.CROSSBOW)) {
            event.setCanceled(true);
            return;
        }
        float partial = event.getPartialTicks();
        if (stack.getItem() instanceof IHandRenderer) {
            event.setCanceled(true);
            float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * event.getPartialTicks();
            float swing = event.getSwingProgress();
            float equip = event.getEquipProgress();
            GlStateManager.pushMatrix();
            AnimationManager.renderingDualWield = false;
            AnimationManager.animateItemHands(partial);
            GlStateManager.pushMatrix();
            AnimationManager.animateHands(partial);
            renderGunFirstPerson(event.getEquipProgress(), (IHandRenderer) event.getItemStack().getItem(), partial);
            GlStateManager.popMatrix();
            AnimationManager.animateItem(partial);
            if (!AnimationManager.shouldCancelItemRender())
                Minecraft.getMinecraft().getItemRenderer().renderItemInFirstPerson(player, partial, pitch, EnumHand.MAIN_HAND, swing, stack, equip);
            GlStateManager.popMatrix();
            if (stack.getItem() == ModRegistry.GRPGItems.PISTOL && PlayerDataFactory.hasActiveSkill(player, ModRegistry.Skills.PISTOL_DUAL_WIELD)) {
                GlStateManager.pushMatrix();
                AnimationManager.renderingDualWield = true;
                AnimationManager.animateItemHands(partial);
                GlStateManager.pushMatrix();
                AnimationManager.animateHands(partial);
                renderGunFirstPerson(event.getEquipProgress(), (IHandRenderer) event.getItemStack().getItem(), partial);
                GlStateManager.popMatrix();
                AnimationManager.animateItem(partial);
                Minecraft.getMinecraft().getItemRenderer().renderItemInFirstPerson(player, partial, pitch, EnumHand.OFF_HAND, swing, stack, equip);
                GlStateManager.popMatrix();
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (event.phase == TickEvent.Phase.END && player != null) {
            AnimationManager.tick();
            startSprintListener.update(player);
            if (burst) {
                if (shotsLeft > 0) {
                    ItemStack stack = player.getHeldItemMainhand();
                    if (stack.getItem() instanceof GunItem) {
                        GunItem gun = (GunItem) stack.getItem();
                        CooldownTracker tracker = player.getCooldownTracker();
                        if (!tracker.hasCooldown(gun)) {
                            if (ShootingManager.canShoot(player, stack)) {
                                ShootingManager.shootSingle(player, stack);
                                shotsLeft--;
                            } else burst = false;
                        }
                    }
                } else burst = false;
            }
        }
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        AnimationManager.renderTick(event.renderTickTime, event.phase);
    }

    private static void renderGunFirstPerson(float equipProgress, IHandRenderer handRenderer, float partial) {
        float yOff = 0.5F * equipProgress;
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        {
            GlStateManager.translate(0, -yOff, 0);
            GlStateManager.pushMatrix();
            {
                AnimationManager.animateRightArm(partial);
                handRenderer.renderRightArm();
            }
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            {
                AnimationManager.animateLeftArm(partial);
                handRenderer.renderLeftArm();
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
        GlStateManager.enableCull();
    }

    private static class RisingEdgeChecker {

        private final Function<EntityPlayer, Boolean> stateGetter;
        private final Runnable onChange;
        private boolean lastState;

        public RisingEdgeChecker(Runnable onChange, Function<EntityPlayer, Boolean> stateGetter) {
            this.onChange = onChange;
            this.stateGetter = stateGetter;
        }

        public void update(EntityPlayer player) {
            boolean current = stateGetter.apply(player);
            if (!lastState && current) {
                onChange.run();
            }
            lastState = current;
        }
    }
}

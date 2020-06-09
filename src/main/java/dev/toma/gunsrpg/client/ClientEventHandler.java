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
import dev.toma.gunsrpg.common.capability.object.ScopeData;
import dev.toma.gunsrpg.common.capability.object.SkillData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.IAmmoProvider;
import dev.toma.gunsrpg.common.item.guns.ammo.ItemAmmo;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.skilltree.Ability;
import dev.toma.gunsrpg.common.skilltree.EntryInstance;
import dev.toma.gunsrpg.common.skilltree.SkillTreeEntry;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.debuffs.Debuff;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSetAiming;
import dev.toma.gunsrpg.network.packet.SPacketSetShooting;
import dev.toma.gunsrpg.util.ModUtils;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.function.Function;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = GunsRPG.MODID)
public class ClientEventHandler {

    private static final RisingEdgeChecker startSprintListener = new RisingEdgeChecker(() -> {
        EntityPlayer player = Minecraft.getMinecraft().player;
        ItemStack stack = player.getHeldItemMainhand();
        if(stack.getItem() instanceof GunItem && !PlayerDataFactory.get(player).getReloadInfo().isReloading()) {
            AnimationManager.sendNewAnimation(Animations.SPRINT, new SprintingAnimation());
        }
    }, EntityPlayer::isSprinting);

    private static final ResourceLocation ICON_BACKGROUND = GunsRPG.makeResource("textures/icons/background.png");
    private static final ResourceLocation SCOPE = GunsRPG.makeResource("textures/icons/scope_overlay.png");
    static float prevAimingProgress;

    @SubscribeEvent
    public static void cancelOverlays(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            ScaledResolution resolution = event.getResolution();
            ItemStack stack = player.getHeldItemMainhand();
            if (stack.getItem() instanceof GunItem) {
                event.setCanceled(true);
                PlayerData data = PlayerDataFactory.get(player);
                if(data.getAimInfo().progress >= 0.9F) {
                    if(stack.getItem() == ModRegistry.GRPGItems.SNIPER_RIFLE && PlayerDataFactory.hasActiveSkill(player, Ability.SCOPE)) {
                        int left = resolution.getScaledWidth() / 2 - 16;
                        int top = resolution.getScaledHeight() / 2 - 16;
                        ModUtils.renderTexture(left, top, left + 32, top + 32, SCOPE);
                    } else if((PlayerDataFactory.hasActiveSkill(player, Ability.SMG_RED_DOT) && stack.getItem() == ModRegistry.GRPGItems.SMG) || (PlayerDataFactory.hasActiveSkill(player, Ability.AR_RED_DOT) && stack.getItem() == ModRegistry.GRPGItems.ASSAULT_RIFLE)) {
                        ScopeData scopeData = data.getScopeData();
                        float left = resolution.getScaledWidth() / 2f - 8f;
                        float top = resolution.getScaledHeight() / 2f - 8f;
                        float x2 = left + 16;
                        float y2 = top + 16;
                        double us = scopeData.getTexStartX();
                        double vs = scopeData.getTexStartY();
                        double ue = scopeData.getTexEndX();
                        double ve = scopeData.getTexEndY();
                        //draw reddot
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
            boolean b = day % 7 == 0 && day > 0;
            long l = b ? 1 : 8 - day % 7;
            ScaledResolution resolution = event.getResolution();
            mc.fontRenderer.drawStringWithShadow(l + "", resolution.getScaledWidth() - 10, 6, b ? 0xff0000 : l > 1 && l < 4 ? 0xffff00 : 0xffffff);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            ItemStack stack = player.getHeldItemMainhand();
            if (stack.getItem() instanceof GunItem) {
                GunItem gun = (GunItem) stack.getItem();
                SkillData skillData = data.getSkillData();
                int kills = skillData.killCount.getOrDefault(gun, SkillData.KillData.empty()).getKillCount();
                int required = kills;
                List<EntryInstance> list = skillData.getSkillTree().getObtained();
                for(EntryInstance instance : list) {
                    SkillTreeEntry entry = instance.getType();
                    if(entry.child.length > 0 && entry.gun == gun && !ModUtils.contains(entry.child[0], list, (e, i) -> i.getType() == e)) {
                        required = entry.child[0].requiredKillCount;
                        break;
                    }
                }
                int ammo = gun.getAmmo(stack);
                int max = gun.getMaxAmmo(player);
                float f = kills / (float) required;
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
                    int width = renderer.getStringWidth(text);
                    int x = resolution.getScaledWidth() - width - 34;
                    int y = resolution.getScaledHeight() - 22;
                    ModUtils.renderColor(x, y, x + width + 22, y + 7, 0.0F, 0.0F, 0.0F, 1.0F);
                    ModUtils.renderColor(x + 2, y + 2, x + (int) (f * (width + 20)), y + 5, 0.0F, 1.0F, 1.0F, 1.0F);
                    mc.getRenderItem().renderItemIntoGUI(new ItemStack(itemAmmo), x, y - 18);
                    mc.fontRenderer.drawStringWithShadow(text, x + 19, y - 14, 0xffffff);
                }
            }
            if (data != null) {
                DebuffData debuffData = data.getDebuffData();
                int offset = 0;
                for (Debuff debuff : debuffData.getDebuffs()) {
                    if (!debuff.isActive()) continue;
                    int yStart = event.getResolution().getScaledHeight() + GRPGConfig.client.debuffs.y - 50;
                    ModUtils.renderTexture(GRPGConfig.client.debuffs.x, yStart + offset * 18, 50, yStart + (1 + offset) * 18, ICON_BACKGROUND);
                    ModUtils.renderTexture(GRPGConfig.client.debuffs.x + 2, yStart + 1 + offset * 18, 18, yStart + 1 + offset * 18 + 16, debuff.getIconTexture());
                    renderer.drawStringWithShadow(debuff.getLevel() + "%", 20, yStart + 5 + offset * 18, 0xFFFFFF);
                    ++offset;
                }
            }
        }
    }

    public static float preAimFov = 70.0F;

    @SubscribeEvent
    public static void mouseInputEvent(InputEvent.MouseInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        GameSettings settings = mc.gameSettings;
        if (player != null) {
            GunItem item = ShootingManager.getGunFrom(player);
            if (item != null) {
                if (settings.keyBindAttack.isPressed()) {
                    if(item.getFiremode(player) == Firemode.FULL_AUTO) {
                        PlayerDataFactory.get(player).setShooting(true);
                        NetworkManager.toServer(new SPacketSetShooting(true));
                    } else {
                        ShootingManager.shootSingle(player, player.getHeldItemMainhand());
                    }
                } else if (settings.keyBindUseItem.isPressed() && AnimationManager.getAnimationByID(Animations.REBOLT) == null && !player.isSprinting()) {
                    boolean aim = !PlayerDataFactory.get(player).getAimInfo().aiming;
                    if (aim) {
                        preAimFov = settings.fovSetting;
                        if(item == ModRegistry.GRPGItems.SNIPER_RIFLE && PlayerDataFactory.hasActiveSkill(player, Ability.SCOPE)) {
                            settings.fovSetting = 15.0F;
                        }
                        AnimationManager.sendNewAnimation(Animations.AIMING, item.createAimAnimation());
                    } else settings.fovSetting = preAimFov;
                    NetworkManager.toServer(new SPacketSetAiming(aim));
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderHandEvent(RenderSpecificHandEvent event) {
        ItemStack stack = event.getItemStack();
        EntityPlayerSP player = Minecraft.getMinecraft().player;
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
            if(!AnimationManager.shouldCancelItemRender()) Minecraft.getMinecraft().getItemRenderer().renderItemInFirstPerson(player, partial, pitch, EnumHand.MAIN_HAND, swing, stack, equip);
            GlStateManager.popMatrix();
            if(stack.getItem() == ModRegistry.GRPGItems.PISTOL && PlayerDataFactory.hasActiveSkill(player, Ability.DUAL_WIELD)) {
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
        if(event.phase == TickEvent.Phase.END && player != null) {
            startSprintListener.update(player);
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

        private boolean lastState;
        private final Function<EntityPlayer, Boolean> stateGetter;
        private final Runnable onChange;

        public RisingEdgeChecker(Runnable onChange, Function<EntityPlayer, Boolean> stateGetter) {
            this.onChange = onChange;
            this.stateGetter = stateGetter;
        }

        public void update(EntityPlayer player) {
            boolean current = stateGetter.apply(player);
            if(!lastState && current) {
                onChange.run();
            }
            lastState = current;
        }
    }
}

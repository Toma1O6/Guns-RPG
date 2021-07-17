package dev.toma.gunsrpg.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.animation.AnimationProcessor;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IHandRenderer;
import dev.toma.gunsrpg.client.animation.impl.SprintingAnimation;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.AimInfo;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.capability.object.GunData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.debuffs.Debuff;
import dev.toma.gunsrpg.common.init.GRPGItems;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.IAmmoProvider;
import dev.toma.gunsrpg.common.item.guns.ammo.ItemAmmo;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.skills.core.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.interfaces.OverlayRenderer;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.config.util.ScopeRenderer;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketSetAiming;
import dev.toma.gunsrpg.network.packet.SPacketShoot;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.object.OptionalObject;
import dev.toma.gunsrpg.util.object.ShootingManager;
import net.minecraft.client.GameSettings;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = GunsRPG.MODID)
public class ClientEventHandler {

    public static final ResourceLocation SCOPE = GunsRPG.makeResource("textures/icons/scope_overlay.png");
    public static final ResourceLocation SCOPE_OVERLAY = GunsRPG.makeResource("textures/icons/scope_full.png");
    private static final ChangeDetector startSprintListener = new ChangeDetector(() -> {
        PlayerEntity player = Minecraft.getInstance().player;
        ItemStack stack = player.getMainHandItem();
        LazyOptional<PlayerData> optional = PlayerDataFactory.get(player);
        optional.ifPresent(data -> {
            if (stack.getItem() instanceof GunItem && !data.getReloadInfo().isReloading())
                ClientSideManager.instance().processor().play(Animations.SPRINT, new SprintingAnimation());
        });
    }, PlayerEntity::isSprinting);
    public static OptionalObject<Double> preAimFov = OptionalObject.empty();
    public static OptionalObject<Double> preAimSens = OptionalObject.empty();
    static boolean burst;
    static int shotsLeft = 2;
    public static int shootDelay;

    @SubscribeEvent
    public static void cancelOverlays(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            MainWindow window = event.getWindow();
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof GunItem) {
                event.setCanceled(true);
                MatrixStack matrixStack = event.getMatrixStack();
                PlayerDataFactory.get(player).ifPresent(data -> {
                    int windowWidth = window.getGuiScaledWidth();
                    int windowHeight = window.getGuiScaledHeight();
                    if (data.getAimInfo().progress >= 0.9F) {
                        if (stack.getItem() == GRPGItems.SNIPER_RIFLE && PlayerDataFactory.hasActiveSkill(player, Skills.SR_SCOPE) || stack.getItem() == GRPGItems.CROSSBOW && PlayerDataFactory.hasActiveSkill(player, Skills.CROSSBOW_SCOPE)) {
                            Matrix4f pose = matrixStack.last().pose();
                            if (GRPGConfig.clientConfig.scopeRenderer.get() == ScopeRenderer.TEXTURE) {
                                ModUtils.renderTexture(pose, 0, 0, windowWidth, windowHeight, SCOPE_OVERLAY);
                            } else {
                                int left = window.getGuiScaledWidth() / 2 - 16;
                                int top = window.getGuiScaledHeight() / 2 - 16;
                                ModUtils.renderTexture(pose, left, top, left + 32, top + 32, SCOPE);
                            }
                        } else if ((PlayerDataFactory.hasActiveSkill(player, Skills.SMG_RED_DOT) && stack.getItem() == GRPGItems.SMG) || (PlayerDataFactory.hasActiveSkill(player, Skills.AR_RED_DOT) && stack.getItem() == GRPGItems.ASSAULT_RIFLE)) {
                            float left = windowWidth / 2f - 8f;
                            float top = windowHeight / 2f - 8f;
                            float x2 = left + 16;
                            float y2 = top + 16;
                            //draw red dot
                            int color = GRPGConfig.clientConfig.reticleColor.getColor();
                            float alpha = ModUtils.alpha(color);
                            float red = ModUtils.red(color);
                            float green = ModUtils.green(color);
                            float blue = ModUtils.blue(color);
                            mc.getTextureManager().bind(GRPGConfig.clientConfig.reticleVariants.getAsResource());
                            RenderSystem.enableBlend();
                            Tessellator tessellator = Tessellator.getInstance();
                            BufferBuilder builder = tessellator.getBuilder();
                            builder.begin(7, DefaultVertexFormats.POSITION_COLOR_TEX);
                            builder.vertex(left, y2, 0).color(red, green, blue, alpha).uv(0.0F, 1.0F).endVertex();
                            builder.vertex(x2, y2, 0).color(red, green, blue, alpha).uv(1.0F, 1.0F).endVertex();
                            builder.vertex(x2, top, 0).color(red, green, blue, alpha).uv(1.0F, 0.0F).endVertex();
                            builder.vertex(left, top, 0).color(red, green, blue, alpha).uv(0.0F, 0.0F).endVertex();
                            builder.end();
                            WorldVertexBufferUploader.end(builder);
                            RenderSystem.disableBlend();
                        }
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void renderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            PlayerEntity player = mc.player;
            MatrixStack matrixStack = event.getMatrixStack();
            LazyOptional<PlayerData> optional = PlayerDataFactory.get(player);
            optional.ifPresent(data -> {
                FontRenderer renderer = mc.font;
                long day = player.level.getGameTime() / 24000L;
                int cycle = GRPGConfig.worldConfig.bloodmoonCycle.get();
                MainWindow window = event.getWindow();
                if (cycle >= 0) {
                    boolean b = day % cycle == 0 && day > 0;
                    long l = b ? 0 : cycle - day % cycle;
                    String remainingDays = l + "";
                    mc.font.draw(matrixStack, remainingDays, window.getGuiScaledWidth() - 10 - mc.font.width(remainingDays) / 2f, 6, b ? 0xff0000 : l > 0 && l < 3 ? 0xffff00 : 0xffffff);
                }
                RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                ItemStack stack = player.getMainHandItem();
                int width = 26;
                int x = window.getGuiScaledWidth() - width - 34;
                int y = window.getGuiScaledHeight() - 22;
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
                        for (int i = 0; i < player.inventory.getContainerSize(); i++) {
                            ItemStack itemStack = player.inventory.getItem(i);
                            if (itemStack.getItem() instanceof IAmmoProvider) {
                                IAmmoProvider ammoProvider = (IAmmoProvider) itemStack.getItem();
                                if (ammoProvider.getMaterial() == itemAmmo.getMaterial() && ammoProvider.getAmmoType() == itemAmmo.getAmmoType()) {
                                    c += itemStack.getCount();
                                }
                            }
                        }
                        String text = ammo + " / " + c;
                        width = renderer.width(text);
                        x = window.getGuiScaledWidth() - width - 34;
                        Matrix4f pose = matrixStack.last().pose();
                        ModUtils.renderColor(pose, x, y, x + width + 22, y + 7, 0.0F, 0.0F, 0.0F, 1.0F);
                        ModUtils.renderColor(pose, x + 2, y + 2, x + (int) (f * (width + 20)), y + 5, 1.0F, 1.0F, 0.0F, 1.0F);
                        mc.getItemRenderer().renderGuiItem(new ItemStack(itemAmmo), x, y - 18);
                        mc.font.draw(matrixStack, text, x + 19, y - 14, 0xffffff);
                    }
                }
                int kills = skills.getKills();
                int required = skills.getRequiredKills();
                float levelProgress = skills.isMaxLevel() ? 1.0F : kills / (float) required;
                Matrix4f pose = matrixStack.last().pose();
                ModUtils.renderColor(pose, x, y + 10, x + width + 22, y + 17, 0.0F, 0.0F, 0.0F, 1.0F);
                ModUtils.renderColor(pose, x + 2, y + 12, x + (int) (levelProgress * (width + 20)), y + 15, 0.0F, 1.0F, 1.0F, 1.0F);
                if (data != null) {
                    DebuffData debuffData = data.getDebuffData();
                    int offset = 0;
                    for (Debuff debuff : debuffData.getDebuffs()) {
                        if (debuff == null) continue;
                        int yStart = window.getGuiScaledHeight() + GRPGConfig.clientConfig.debuffOverlay.getY() - 50;
                        debuff.draw(matrixStack, GRPGConfig.clientConfig.debuffOverlay.getX(), yStart + offset * 18, 50, 18, event.getPartialTicks(), renderer);
                        ++offset;
                    }
                }
                int renderIndex = 0;
                List<ISkill> list = skills.getUnlockedSkills().get(SkillCategory.SURVIVAL);
                if (list == null) return;
                int left = 5;
                int top = window.getGuiScaledHeight() - 25;
                List<ISkill> renderSkills = new ArrayList<>();
                for (ISkill skill : list) {
                    if (skill instanceof OverlayRenderer) {
                        skill = SkillUtil.getBestSkillFromOverrides(skill, player);
                        if (!renderSkills.contains(skill)) renderSkills.add(skill);
                    }
                }
                for (ISkill skill : renderSkills) {
                    OverlayRenderer overlayRenderer = (OverlayRenderer) skill;
                    if (skill.apply(player) && overlayRenderer.shouldRenderOnHUD()) {
                        overlayRenderer.renderInHUD(matrixStack, skill, renderIndex, left, top);
                        ++renderIndex;
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void mouseInputEvent(InputEvent.MouseInputEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        GameSettings settings = mc.options;
        if (player != null) {
            GunItem item = ShootingManager.getGunFrom(player);
            ItemStack stack = player.getMainHandItem();
            if (item != null) {
                if (settings.keyAttack.isDown()) {
                    Firemode firemode = item.getFiremode(stack);
                    if (firemode == Firemode.SINGLE && ShootingManager.canShoot(player, stack)) {
                        shoot(player, stack);
                    } else if (firemode == Firemode.BURST) {
                        if (!burst) {
                            burst = true;
                            shotsLeft = 2;
                        }
                    }
                } else if (settings.keyUse.isDown() && ClientSideManager.instance().processor().getByID(Animations.REBOLT) == null && !player.isSprinting()) {
                    LazyOptional<PlayerData> optional = PlayerDataFactory.get(player);
                    boolean aim = optional.isPresent() && optional.orElse(null).getAimInfo().aiming;
                    if (aim) {
                        preAimFov.map(settings.fov);
                        preAimSens.map(settings.sensitivity);
                        if (item == GRPGItems.SNIPER_RIFLE && PlayerDataFactory.hasActiveSkill(player, Skills.SR_SCOPE)) {
                            settings.sensitivity = preAimSens.get() * 0.3F;
                            settings.fov = 15.0F;
                        } else if (item == GRPGItems.CROSSBOW && PlayerDataFactory.hasActiveSkill(player, Skills.CROSSBOW_SCOPE)) {
                            settings.sensitivity = preAimSens.get() * 0.4F;
                            settings.fov = 25.0F;
                        }
                        ClientSideManager.instance().processor().play(Animations.AIMING, item.createAimAnimation());
                    } else {
                        preAimFov.ifPresent(value -> settings.fov = value);
                        preAimSens.ifPresent(value -> settings.sensitivity = value);
                    }
                    NetworkManager.sendServerPacket(new SPacketSetAiming(aim));
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderHandEvent(RenderHandEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        ItemStack stack = event.getItemStack();
        if (event.getHand() == Hand.OFF_HAND) {
            if (event.getItemStack().getItem() == Items.SHIELD && player.getMainHandItem().getItem() instanceof GunItem) {
                event.setCanceled(true);
            }
        }
        LazyOptional<PlayerData> optional = PlayerDataFactory.get(player);
        if (!optional.isPresent()) {
            event.setCanceled(true);
            return;
        } else {
            PlayerData data = optional.orElse(null);
            AimInfo info = data.getAimInfo();
            ScopeRenderer renderer = GRPGConfig.clientConfig.scopeRenderer.get();
            Item item = stack.getItem();
            if (info.isAiming() && renderer == ScopeRenderer.TEXTURE && (PlayerDataFactory.hasActiveSkill(player, Skills.SR_SCOPE) && item == GRPGItems.SNIPER_RIFLE || PlayerDataFactory.hasActiveSkill(player, Skills.CROSSBOW_SCOPE) && item == GRPGItems.CROSSBOW)) {
                event.setCanceled(true);
                return;
            }
        }
        float partial = event.getPartialTicks();
        if (stack.getItem() instanceof IHandRenderer) {
            IHandRenderer iHandRenderer = (IHandRenderer) stack.getItem();
            event.setCanceled(true);
            Minecraft mc = Minecraft.getInstance();
            FirstPersonRenderer fpRenderer = mc.getItemInHandRenderer();
            boolean mainHand = event.getHand() == Hand.MAIN_HAND;
            ItemCameraTransforms.TransformType transformType = mainHand ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
            float equip = event.getEquipProgress();
            MatrixStack matrix = event.getMatrixStack();
            IRenderTypeBuffer buffer = event.getBuffers();
            int packedLight = event.getLight();
            AnimationProcessor processor = ClientSideManager.instance().processor();

            matrix.pushPose();
            {
                processor.setDualWieldRender(false);
                processor.processItemAndHands(matrix, partial);
                matrix.pushPose();
                {
                    processor.processHands(matrix, partial);
                    renderAnimatedItemFP(matrix, buffer, packedLight, equip, iHandRenderer, partial, processor);
                }
                matrix.popPose();
                processor.processItem(matrix, partial);
                if (!processor.blocksItemRender())
                    fpRenderer.renderItem(player, stack, transformType, !mainHand, matrix, buffer, packedLight);
            }
            matrix.popPose();

            if (stack.getItem() == GRPGItems.PISTOL && PlayerDataFactory.hasActiveSkill(player, Skills.PISTOL_DUAL_WIELD)) {
                matrix.pushPose();
                {
                    processor.setDualWieldRender(true);
                    processor.processItemAndHands(matrix, partial);
                    matrix.pushPose();
                    {
                        processor.processHands(matrix, partial);
                        renderAnimatedItemFP(matrix, buffer, packedLight, equip, iHandRenderer, partial, processor);
                    }
                    matrix.popPose();
                    processor.processItem(matrix, partial);
                    fpRenderer.renderItem(player, stack, ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, true, matrix, buffer, packedLight);
                }
                matrix.popPose();
            }
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if (event.phase == TickEvent.Phase.END && player != null) {
            if (shootDelay > 0)
                --shootDelay;
            ClientSideManager.instance().processor().tick();
            startSprintListener.update(player);
            GameSettings settings = mc.options;
            if (burst) {
                if (shotsLeft > 0) {
                    ItemStack stack = player.getMainHandItem();
                    if (stack.getItem() instanceof GunItem) {
                        if (shootDelay == 0) {
                            if (ShootingManager.canShoot(player, stack)) {
                                shoot(player, stack);
                                shotsLeft--;
                            } else burst = false;
                        }
                    }
                } else burst = false;
            } else if (settings.keyAttack.isDown()) {
                ItemStack stack = player.getMainHandItem();
                if (stack.getItem() instanceof GunItem) {
                    GunItem gun = (GunItem) stack.getItem();
                    if (gun.getFiremode(stack) == Firemode.FULL_AUTO && shootDelay == 0 && ShootingManager.canShoot(player, stack)) {
                        shoot(player, stack);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            ClientSideManager.instance().processor().processFrame(event.renderTickTime);
    }

    private static void renderAnimatedItemFP(MatrixStack stack, IRenderTypeBuffer buffer, int packedLight, float equipProgress, IHandRenderer handRenderer, float partial, AnimationProcessor processor) {
        float yOff = -0.5F * equipProgress;
        RenderSystem.disableCull();
        stack.pushPose();
        {
            stack.translate(0, yOff, 0);
            stack.pushPose();
            {
                processor.processRightHand(stack, partial);
                renderHand(stack, HandSide.RIGHT, handRenderer, buffer, packedLight);
            }
            stack.popPose();
            stack.pushPose();
            {
                processor.processLeftHand(stack, partial);
                renderHand(stack, HandSide.LEFT, handRenderer, buffer, packedLight);
            }
            stack.popPose();
        }
        stack.popPose();
        RenderSystem.enableCull();
    }

    private static void shoot(PlayerEntity player, ItemStack stack) {
        GunItem gun = (GunItem) stack.getItem();
        player.xRot -= gun.getVerticalRecoil(player);
        player.yRot += gun.getHorizontalRecoil(player);
        NetworkManager.sendServerPacket(new SPacketShoot());
        gun.onShoot(player, stack);
        shootDelay = gun.getFirerate(player);
    }

    private static void renderHand(MatrixStack stack, HandSide side, IHandRenderer renderer, IRenderTypeBuffer buffer, int packedLight) {
        if (!renderer.shouldRenderForSide(side))
            return;
        boolean rightArm = side == HandSide.RIGHT;

        if (rightArm)
            renderer.transformRightArm(stack);
        else
            renderer.transformLeftArm(stack);

        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bind(mc.player.getSkinTextureLocation());
        EntityRenderer<? super ClientPlayerEntity> entityRenderer = mc.getEntityRenderDispatcher().getRenderer(mc.player);
        PlayerRenderer playerRenderer = (PlayerRenderer) entityRenderer;

        stack.pushPose();
        {
            stack.mulPose(Vector3f.YP.rotationDegrees(40.0F));
            stack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
            if (rightArm) {
                stack.translate(0.8F, -0.3F, -0.4F);
                playerRenderer.renderRightHand(stack, buffer, packedLight, mc.player);
            } else {
                stack.translate(-0.5F, 0.6F, -0.36F);
                playerRenderer.renderLeftHand(stack, buffer, packedLight, mc.player);
            }
            stack.mulPose(Vector3f.ZP.rotationDegrees(-41.0F));
        }
        stack.popPose();
    }

    private static class ChangeDetector {

        private final Function<PlayerEntity, Boolean> stateGetter;
        private final Runnable onChange;
        private boolean lastState;

        public ChangeDetector(Runnable onChange, Function<PlayerEntity, Boolean> stateGetter) {
            this.onChange = onChange;
            this.stateGetter = stateGetter;
        }

        public void update(PlayerEntity player) {
            boolean current = stateGetter.apply(player);
            if (!lastState && current) {
                onChange.run();
            }
            lastState = current;
        }
    }
}

package dev.toma.gunsrpg.common.item.guns.reload;

import com.google.common.base.Preconditions;
import dev.toma.gunsrpg.api.client.IModifiableProgress;
import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IReloader;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.client.animation.ReloadAnimation;
import dev.toma.gunsrpg.client.animation.StagedReloadAnimation;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.util.locate.ammo.ItemLocator;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.api.IAnimationLoader;
import lib.toma.animations.api.IAnimationPipeline;
import lib.toma.animations.api.IKeyframeProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import java.util.ArrayList;
import java.util.List;

public class StagedReloader implements IReloader {

    private final StageDefinitionContainer container;

    private GunItem reloadingGun;
    private ItemStack stack;

    protected StagedReloader(StageDefinitionContainer container) {
        this.container = container;
        this.container.setStageFinishCallback(this::stageFinished);
    }

    public static StageDefinitionContainerBuilder builder() {
        return new StageDefinitionContainerBuilder();
    }

    @Override
    public void initiateReload(PlayerEntity player, GunItem item, ItemStack stack) {
        this.reloadingGun = item;
        this.stack = stack;
        this.container.init(item.getReloadTime(PlayerData.getUnsafe(player).getAttributes(), stack));
        if (player.level.isClientSide) {
            container.clientInit(item.getReloadAnimation(player));
        }
    }

    @Override
    public boolean isReloading() {
        return !container.hasFinished();
    }

    @Override
    public void enqueueCancel() {
        container.onUserCancel();
    }

    @Override
    public void forceCancel() {
        DistExecutor.runWhenOn(Dist.CLIENT, () -> this::cancelAnimations);
    }

    @Override
    public void tick(PlayerEntity player) {
        container.tick(player);
    }

    private void loadBullet(PlayerEntity player) {
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof GunItem)) return;
        int max = reloadingGun.getMaxAmmo(PlayerData.getUnsafe(player).getAttributes());
        AmmoType type = reloadingGun.getAmmoType();
        IAmmoMaterial material = reloadingGun.getMaterialFromNBT(stack);
        if (player.isCreative()) {
            reloadingGun.setAmmoCount(stack, Math.min(reloadingGun.getAmmo(stack) + 1, max));
        } else {
            ItemLocator.consume(player.inventory, ItemLocator.filterByAmmoTypeAndMaterial(type, material), ctx -> {
                ItemStack ammo = ctx.getCurrectStack();
                if (!ammo.isEmpty()) {
                    ammo.shrink(1);
                    reloadingGun.setAmmoCount(stack, Math.min(reloadingGun.getAmmo(stack) + 1, max));
                }
            });
        }
    }

    private void stageFinished(PlayerEntity player, IReloadingStage stage, boolean wasLast) {
        ReloadingStageType type = stage.stageType();
        if (type == ReloadingStageType.LOADING) {
            loadBullet(player);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void cancelAnimations() {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        pipeline.remove(ModAnimations.RELOAD);
        pipeline.remove(ModAnimations.RELOAD_BULLET);
    }

    public interface IReloadingStage {
        ReloadingStageType stageType();
        void init();
        @OnlyIn(Dist.CLIENT) void initClient(ResourceLocation path, StageDefinitionContainer container);
        void tick(PlayerEntity player);
        boolean isFinished();
        void setRemainingTicks(int remainingTicks);
    }

    public static abstract class AbstractReloadingStage implements IReloadingStage {

        protected int ticks;

        @Override
        public boolean isFinished() {
            return ticks <= 0;
        }

        @Override
        public void tick(PlayerEntity player) {
            --ticks;
        }

        @Override
        public void setRemainingTicks(int remainingTicks) {
            this.ticks = remainingTicks;
        }
    }

    public static class PreparationStage extends AbstractReloadingStage {

        private final boolean inverseProgress;

        public PreparationStage(boolean inverseProgress) {
            this.inverseProgress = inverseProgress;
        }

        @Override
        public ReloadingStageType stageType() {
            return ReloadingStageType.PREPARATION;
        }

        @Override
        public void init() {
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        public void initClient(ResourceLocation path, StageDefinitionContainer container) {
            AnimationEngine engine = AnimationEngine.get();
            IAnimationPipeline pipeline = engine.pipeline();
            IAnimationLoader loader = engine.loader();
            IKeyframeProvider provider = loader.getProvider(path);
            StagedReloadAnimation animation = new StagedReloadAnimation(provider, container::hasFinished, container::getPreparationProgress);
            pipeline.insert(ModAnimations.RELOAD, animation);
        }
    }

    public static class LoadingStage extends AbstractReloadingStage {

        private final ResourceLocation animationPath;

        public LoadingStage(ResourceLocation animationPath) {
            this.animationPath = animationPath;
        }

        @Override
        public ReloadingStageType stageType() {
            return ReloadingStageType.LOADING;
        }

        @Override
        public void init() {
            DistExecutor.runWhenOn(Dist.CLIENT, () -> this::playLoadAnimation);
        }

        @Override
        public void initClient(ResourceLocation path, StageDefinitionContainer container) {
            // not called
        }

        @OnlyIn(Dist.CLIENT)
        private void playLoadAnimation() {
            AnimationEngine engine = AnimationEngine.get();
            IAnimationPipeline pipeline = engine.pipeline();
            IAnimationLoader loader = engine.loader();
            IKeyframeProvider provider = loader.getProvider(animationPath);
            IModifiableProgress modifiableProgressAnimation = new ReloadAnimation(provider, ticks);
            pipeline.insert(ModAnimations.RELOAD_BULLET, modifiableProgressAnimation);
        }
    }

    public enum ReloadingStageType {
        PREPARATION, LOADING
    }

    public interface IStageFinishCallback {
        void onStageFinished(PlayerEntity player, IReloadingStage stage, boolean wasLast);
    }

    public static class StageDefinitionContainer {

        private final int preparationStageLength;
        private final IReloadingStage[] definitions;
        private IStageFinishCallback stageFinishCallback;
        private int actualStage;
        private boolean finished;
        private boolean awaitingCancelation;

        private StageDefinitionContainer(StageDefinitionContainerBuilder builder) {
            this.preparationStageLength = (int) Math.ceil(builder.prepTicks / 2d);
            this.definitions = builder.reloadingStages.toArray(new IReloadingStage[0]);
        }

        public void onUserCancel() {
            awaitingCancelation = true;
        }

        public void setStageFinishCallback(IStageFinishCallback callback) {
            this.stageFinishCallback = callback;
        }

        public void clientInit(ResourceLocation reloadAnimation) {
            definitions[0].initClient(reloadAnimation, this);
            definitions[definitions.length - 1].initClient(reloadAnimation, this);
        }

        public void init(int reloadTime) {
            for (int i = 1; i < definitions.length - 1; i++) {
                definitions[i].setRemainingTicks(reloadTime);
            }
            definitions[definitions.length-1].setRemainingTicks(preparationStageLength);
            IReloadingStage first = definitions[0];
            first.setRemainingTicks(preparationStageLength);
            first.init();
        }

        public void tick(PlayerEntity player) {
            if (!finished) {
                IReloadingStage stage = getCurrent();
                if (stage.isFinished()) {
                    boolean willAdvance = canAdvance();
                    stageFinishCallback.onStageFinished(player, stage, !willAdvance);
                    if (!willAdvance) {
                        finished = true;
                        PlayerData.get(player).ifPresent(data -> data.getHandState().freeHands());
                        return;
                    } else {
                        if (awaitingCancelation) {
                            actualStage = definitions.length - 1;
                        } else {
                            ++actualStage;
                        }
                        stage = getCurrent();
                        stage.init();
                    }
                }
                stage.tick(player);
            }
        }

        public IReloadingStage getCurrent() {
            return definitions[actualStage];
        }

        public boolean hasFinished() {
            return finished;
        }

        public float getPreparationProgress() {
            IReloadingStage stage = getCurrent();
            boolean isPrepStage = stage.stageType() == ReloadingStageType.PREPARATION;
            if (isPrepStage) {
                PreparationStage preparationStage = (PreparationStage) stage;
                float stageProgress = preparationStage.ticks / (float) preparationStageLength;
                return preparationStage.inverseProgress ? stageProgress : 1.0F - stageProgress;
            }
            return 1.0F;
        }

        private boolean canAdvance() {
            return actualStage < definitions.length - 1;
        }
    }

    public static class StageDefinitionContainerBuilder {

        private int prepTicks;
        private final List<IReloadingStage> reloadingStages = new ArrayList<>();

        private StageDefinitionContainerBuilder() {
        }

        public StageDefinitionContainerBuilder prepTicks(int prepTicks) {
            this.prepTicks = prepTicks;
            return this;
        }

        public StageDefinitionContainerBuilder stage(IReloadingStage stage) {
            reloadingStages.add(stage);
            return this;
        }

        public StagedReloader build() {
            Preconditions.checkState(prepTicks > 5, "Preparation stage must be atleast 6 ticks long");
            //Preconditions.checkState(prepTicks % 2 == 0, "Preparation stage length must be even number");
            Preconditions.checkState(reloadingStages.size() > 2, "You must define atleast 3 stages");
            return new StagedReloader(new StageDefinitionContainer(this));
        }
    }
}

package dev.toma.gunsrpg.common.item.guns.reload;

import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.IReloader;
import net.minecraft.util.ResourceLocation;

public class StagedReloadManager implements IReloadManager {

    private final int prepLength;
    private final int loadTarget;
    private final ResourceLocation bulletLoadProviderPath;

    public StagedReloadManager(int preparationTicks, int loadTarget, ResourceLocation bulletLoadProviderPath) {
        this.prepLength = preparationTicks;
        this.loadTarget = loadTarget;
        this.bulletLoadProviderPath = bulletLoadProviderPath;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    @Override
    public IReloader createReloadHandler() {
        StagedReloader.StageDefinitionContainerBuilder builder = StagedReloader.builder().prepTicks(prepLength);
        builder.stage(new StagedReloader.PreparationStage(false));
        for (int i = 0; i < loadTarget; i++) {
            builder.stage(new StagedReloader.LoadingStage(bulletLoadProviderPath));
        }
        builder.stage(new StagedReloader.PreparationStage(true));
        return builder.build();
    }
}

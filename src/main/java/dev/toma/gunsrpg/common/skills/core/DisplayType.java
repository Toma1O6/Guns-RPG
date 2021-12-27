package dev.toma.gunsrpg.common.skills.core;

import dev.toma.gunsrpg.client.render.skill.SkillRenderFactories;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.concurrent.Callable;

public final class DisplayType<D> {

    public static final DisplayType<ResourceLocation> ICON = new DisplayType<>(() -> SkillRenderFactories::renderIcon);
    public static final DisplayType<ItemStack> ITEM = new DisplayType<>(() -> SkillRenderFactories::renderItem);

    private final Callable<IRenderFactory<D>> callable;

    public DisplayType(Callable<IRenderFactory<D>> callable) {
        this.callable = callable;
    }

    @OnlyIn(Dist.CLIENT)
    public IRenderFactory<D> getRenderFactory() {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    public interface IRenderFactory<D> {
        void render(int x, int y, D data);
    }
}

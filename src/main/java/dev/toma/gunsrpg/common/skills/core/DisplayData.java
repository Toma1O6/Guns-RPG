package dev.toma.gunsrpg.common.skills.core;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public final class DisplayData {

    private final DisplayType<?> type;
    private final Object data;

    public static <D> DisplayData create(DisplayType<D> type, D data) {
        return new DisplayData(type, data);
    }

    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unchecked")
    public <D> void renderAt(MatrixStack stack, int x, int y) {
        DisplayType.IRenderFactory<D> factory = (DisplayType.IRenderFactory<D>) type.getRenderFactory();
        D data = getRenderData();
        factory.render(stack, x, y, data);
    }

    @SuppressWarnings("unchecked")
    public <D> D getRenderData() {
        return (D) data;
    }

    private DisplayData(DisplayType<?> type, Object data) {
        this.type = type;
        this.data = data;
    }
}

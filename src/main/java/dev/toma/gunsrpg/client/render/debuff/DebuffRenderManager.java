package dev.toma.gunsrpg.client.render.debuff;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IDebuffs;
import dev.toma.gunsrpg.client.OverlayPlacement;
import dev.toma.gunsrpg.common.debuffs.IDebuff;
import dev.toma.gunsrpg.common.debuffs.IDebuffType;
import dev.toma.gunsrpg.config.client.ConfigurableOverlay;
import dev.toma.gunsrpg.sided.ClientSideManager;
import dev.toma.gunsrpg.util.math.IVec2i;
import net.minecraft.client.MainWindow;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

public final class DebuffRenderManager {

    private final Map<IDebuffType<?>, IDebuffRenderer<?>> renderers = new IdentityHashMap<>();

    public <D extends IDebuff> void registerRenderer(IDebuffType<D> type, IDebuffRenderer<D> renderer) {
        renderers.put(type, renderer);
    }

    public void drawDebuffsOnScreen(MatrixStack poseStack, MainWindow window, IAttributeProvider attributes, IDebuffs debuffs, int left, int top, float partialTicks) {
        ConfigurableOverlay overlay = ClientSideManager.config.debuffOverlay;
        if (!overlay.enabled)
            return;
        int index = 0;
        Collection<IDebuff> activeDebuffs = debuffs.getActiveDebuffs();
        int panelWidth = 65;
        int panelHeight = activeDebuffs.size() * 20;
        IVec2i pos = OverlayPlacement.getPlacement(overlay, 0, 0, window.getGuiScaledWidth(), window.getGuiScaledHeight(), panelWidth, panelHeight);
        for (IDebuff debuff : activeDebuffs) {
            if (tryRender(debuff, attributes, index, pos.x(), pos.y(), poseStack, partialTicks)) {
                ++index;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <D extends IDebuff> IDebuffRenderer<D> getRendererFor(IDebuffType<D> type) {
        return (IDebuffRenderer<D>) renderers.get(type);
    }

    @SuppressWarnings("unchecked")
    private <D extends IDebuff> boolean tryRender(D debuff, IAttributeProvider attributes, int renderIndex, int left, int top, MatrixStack poseStack, float partialTicks) {
        IDebuffType<D> type = (IDebuffType<D>) debuff.getType();
        IDebuffRenderer<D> renderer = getRendererFor(type);
        if (renderer != null) {
            renderer.drawOnScreen(debuff, attributes, poseStack, left, top + renderIndex * 20, 65, 20, partialTicks);
            return true;
        }
        return false;
    }
}

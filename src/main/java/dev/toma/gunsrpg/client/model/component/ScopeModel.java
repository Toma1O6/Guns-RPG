package dev.toma.gunsrpg.client.model.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.item.AbstractWeaponRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class ScopeModel extends AbstractAttachmentModel implements IOpticsProvider {

    private static ResourceLocation reticlePath = GunsRPG.makeResource("textures/scope/sniper_reticle.png");

    private final ModelRenderer scope;
    private final ModelRenderer main;
    private final ModelRenderer b2;
    private final ModelRenderer b1;
    private final ModelRenderer overlay;
    private final ModelRenderer reticle;

    public static void prepare(ResourceLocation reticle) {
        reticlePath = reticle;
    }

    @Override
    public void renderAttachment(MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay, float progress) {
        renderScopeWithGlass(stack, buffer, this, progress, light, overlay);
    }

    @Override
    public ResourceLocation getComponentTextureMap() {
        return AbstractWeaponRenderer.ATTACHMENTS;
    }

    @Override
    public ResourceLocation getReticleTextureMap() {
        return reticlePath;
    }

    @Override
    public ModelRenderer getGlassModel() {
        return reticle;
    }

    @Override
    public ModelRenderer getOverlayModel() {
        return overlay;
    }

    @Override
    public int getReticleTintARGB() {
        return 0;
    }

    @Override
    public void renderOptic(MatrixStack stack, IVertexBuilder builder, int light, int overlay) {
        scope.render(stack, builder, light, overlay);
    }

    public ScopeModel() {
        texWidth = 64;
        texHeight = 64;

        scope = new ModelRenderer(this);
        scope.setPos(0.0F, 24.0F, 0.0F);

        main = new ModelRenderer(this);
        main.setPos(0.0F, 0.0F, 0.0F);
        scope.addChild(main);
        main.texOffs(0, 0).addBox(-2.0F, -3.2F, -13.0F, 4.0F, 1.0F, 26.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(-4.85F, -9.1F, -13.0F, 1.0F, 4.0F, 26.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(-4.35F, -9.1F, -12.5F, 1.0F, 4.0F, 25.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(3.85F, -9.1F, -13.0F, 1.0F, 4.0F, 26.0F, 0.0F, false);
        main.texOffs(23, 21).addBox(4.05F, -9.5F, -9.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);
        main.texOffs(17, 14).addBox(-4.95F, -9.5F, -9.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(3.35F, -9.1F, -12.5F, 1.0F, 4.0F, 25.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(-2.0F, -11.95F, -13.0F, 4.0F, 1.0F, 26.0F, 0.0F, false);
        main.texOffs(33, 7).addBox(-2.0F, -12.45F, -9.0F, 4.0F, 1.0F, 2.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(4.05F, -9.5F, 7.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(-2.0F, -12.45F, 7.0F, 4.0F, 1.0F, 2.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(-4.95F, -9.5F, 7.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(-2.0F, -11.45F, -12.5F, 4.0F, 1.0F, 25.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(-2.0F, -3.7F, -12.5F, 4.0F, 1.0F, 25.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(1.05F, -2.0F, -9.5F, 2.0F, 2.0F, 19.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(-2.95F, -2.0F, -9.5F, 2.0F, 2.0F, 19.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(-0.95F, -2.0F, 8.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(-0.95F, -2.0F, -9.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(-0.95F, -2.0F, 2.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        main.texOffs(0, 0).addBox(-0.95F, -2.0F, -3.5F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        b2 = new ModelRenderer(this);
        b2.setPos(0.0F, 0.0F, 0.0F);
        main.addChild(b2);
        setRotationAngle(b2, 0.0F, 0.0F, -0.7854F);
        b2.texOffs(0, 0).addBox(8.85F, -7.0F, -13.0F, 1.0F, 4.0F, 26.0F, 0.0F, false);
        b2.texOffs(0, 0).addBox(9.2F, -7.35F, -9.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        b2.texOffs(0, 0).addBox(9.2F, -7.35F, 7.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        b2.texOffs(0, 0).addBox(8.35F, -7.0F, -12.5F, 1.0F, 4.0F, 25.0F, 0.0F, false);
        b2.texOffs(0, 0).addBox(0.15F, -7.0F, -13.0F, 1.0F, 4.0F, 26.0F, 0.0F, false);
        b2.texOffs(0, 0).addBox(-1.01F, -6.0F, 7.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        b2.texOffs(0, 0).addBox(-1.01F, -6.0F, -9.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        b2.texOffs(0, 0).addBox(0.65F, -7.0F, -12.5F, 1.0F, 4.0F, 25.0F, 0.0F, false);
        b2.texOffs(0, 0).addBox(-0.55F, -2.0F, 8.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        b2.texOffs(0, 0).addBox(-0.55F, -2.0F, -9.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        b2.texOffs(0, 0).addBox(-0.55F, -2.0F, 2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        b2.texOffs(0, 0).addBox(-0.55F, -2.0F, -3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        b1 = new ModelRenderer(this);
        b1.setPos(0.0F, 0.0F, 0.0F);
        main.addChild(b1);
        setRotationAngle(b1, 0.0F, 0.0F, 0.7854F);
        b1.texOffs(0, 0).addBox(-9.85F, -7.0F, -13.0F, 1.0F, 4.0F, 26.0F, 0.0F, false);
        b1.texOffs(31, 22).addBox(-10.2F, -7.4F, -9.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        b1.texOffs(0, 0).addBox(-10.2F, -7.4F, 7.0F, 1.0F, 4.0F, 2.0F, 0.0F, false);
        b1.texOffs(0, 0).addBox(-9.35F, -7.0F, -12.5F, 1.0F, 4.0F, 25.0F, 0.0F, false);
        b1.texOffs(0, 0).addBox(-1.2F, -7.0F, -13.0F, 1.0F, 4.0F, 26.0F, 0.0F, false);
        b1.texOffs(0, 0).addBox(0.1F, -6.0F, 7.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        b1.texOffs(0, 0).addBox(0.1F, -6.0F, -9.0F, 1.0F, 3.0F, 2.0F, 0.0F, false);
        b1.texOffs(0, 0).addBox(-1.7F, -7.0F, -12.5F, 1.0F, 4.0F, 25.0F, 0.0F, false);
        b1.texOffs(0, 0).addBox(-0.4F, -2.0F, 8.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        b1.texOffs(0, 0).addBox(-0.4F, -2.0F, -9.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        b1.texOffs(0, 0).addBox(-0.4F, -2.0F, 2.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        b1.texOffs(0, 0).addBox(-0.4F, -2.0F, -3.5F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        overlay = new ModelRenderer(this);
        overlay.setPos(0.0F, 24.0F, 0.0F);
        overlay.texOffs(50, 57).addBox(-3.5F, -10.55F, 12.1F, 7.0F, 7.0F, 0.0F, 0.0F, false);
        overlay.texOffs(50, 57).addBox(-3.5F, -10.55F, -11.9F, 7.0F, 7.0F, 0.0F, 0.0F, false);

        reticle = new ModelRenderer(this);
        reticle.setPos(0.0F, 24.0F, 0.0F);
        reticle.texOffs(50, 48).addBox(-3.5F, -10.55F, 12.0F, 7.0F, 7.0F, 0.0F, 0.0F, false);

        setBuiltInRender(overlay);
        setBuiltInRender(reticle);
    }
}

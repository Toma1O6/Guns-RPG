package dev.toma.gunsrpg.client.model.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.render.item.AbstractWeaponRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class PSOScopeModel extends AbstractAttachmentModel implements IOpticsProvider {

    private final ModelRenderer scope;
    private final ModelRenderer bone29;
    private final ModelRenderer bone30;
    private final ModelRenderer bone31;
    private final ModelRenderer bone22;
    private final ModelRenderer bone24;
    private final ModelRenderer bone21;
    private final ModelRenderer bone25;
    private final ModelRenderer bone26;
    private final ModelRenderer bone27;
    private final ModelRenderer bone20;
    private final ModelRenderer bone23;
    private final ModelRenderer reticle;
    private final ModelRenderer overlay;

    @Override
    public void renderAttachment(MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay, float aimingProgress) {
        renderScopeWithGlass(stack, buffer, this, aimingProgress, light, overlay);
    }

    @Override
    public ResourceLocation getComponentTextureMap() {
        return AbstractWeaponRenderer.WEAPON;
    }

    @Override
    public ResourceLocation getReticleTextureMap() {
        return AbstractWeaponRenderer.SNIPER_RETICLE;
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

    public PSOScopeModel() {
        texWidth = 512;
        texHeight = 512;

        scope = new ModelRenderer(this);
        scope.setPos(0.0F, 24.0F, 0.0F);
        scope.texOffs(88, 81).addBox(-1.0F, -39.3049F, -4.8726F, 2.0F, 1.0F, 9.0F, 0.0F, true);
        scope.texOffs(88, 81).addBox(-0.5F, -38.7567F, 3.3034F, 1.0F, 0.0F, 10.0F, 0.0F, false);
        scope.texOffs(88, 81).addBox(-1.0F, -39.3049F, 13.1274F, 2.0F, 1.0F, 4.0F, 0.0F, true);
        scope.texOffs(88, 81).addBox(-1.0F, -38.5978F, -14.8726F, 2.0F, 0.0F, 10.0F, 0.0F, true);
        scope.texOffs(88, 81).addBox(-1.866F, -37.3907F, 3.3034F, 0.0F, 1.0F, 10.0F, 0.0F, false);
        scope.texOffs(88, 81).addBox(-2.4142F, -37.8907F, 13.1274F, 1.0F, 2.0F, 4.0F, 0.0F, false);
        scope.texOffs(88, 81).addBox(-2.4142F, -37.8907F, -4.8726F, 1.0F, 2.0F, 9.0F, 0.0F, false);
        scope.texOffs(88, 81).addBox(-1.7071F, -37.8907F, -14.8726F, 0.0F, 2.0F, 10.0F, 0.0F, false);
        scope.texOffs(88, 81).addBox(1.7071F, -37.8907F, -14.8726F, 0.0F, 2.0F, 10.0F, 0.0F, true);
        scope.texOffs(88, 81).addBox(1.4142F, -37.8907F, -4.8726F, 1.0F, 2.0F, 9.0F, 0.0F, true);
        scope.texOffs(88, 81).addBox(1.866F, -37.3907F, 3.3034F, 0.0F, 1.0F, 10.0F, 0.0F, true);
        scope.texOffs(88, 81).addBox(1.4142F, -37.8907F, 13.1274F, 1.0F, 2.0F, 4.0F, 0.0F, true);
        scope.texOffs(88, 81).addBox(-1.0F, -35.1836F, -14.8726F, 2.0F, 0.0F, 10.0F, 0.0F, true);
        scope.texOffs(88, 81).addBox(-1.0F, -35.4765F, -4.8726F, 2.0F, 1.0F, 9.0F, 0.0F, true);
        scope.texOffs(88, 81).addBox(-0.5F, -35.0247F, 3.3034F, 1.0F, 0.0F, 10.0F, 0.0F, false);
        scope.texOffs(88, 81).addBox(-1.0F, -35.4765F, 13.1274F, 2.0F, 1.0F, 4.0F, 0.0F, true);
        scope.texOffs(77, 33).addBox(-1.0F, -40.3049F, -1.3726F, 2.0F, 1.0F, 2.0F, 0.0F, true);
        scope.texOffs(77, 33).addBox(-1.5F, -40.7049F, -1.8726F, 3.0F, 1.0F, 3.0F, 0.0F, true);
        scope.texOffs(77, 33).addBox(-3.4142F, -37.8907F, -1.3726F, 1.0F, 2.0F, 2.0F, 0.0F, false);
        scope.texOffs(77, 33).addBox(-3.8142F, -38.3907F, -1.8726F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        scope.texOffs(20, 158).addBox(3.064F, -36.0F, -4.14F, 1.0F, 7.0F, 2.0F, 0.0F, false);
        scope.texOffs(20, 158).addBox(2.064F, -38.0F, -4.14F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        scope.texOffs(20, 158).addBox(2.232F, -38.344F, 1.268F, 1.0F, 8.0F, 2.0F, 0.0F, false);
        scope.texOffs(20, 158).addBox(2.824F, -31.0F, 0.668F, 1.0F, 3.0F, 3.0F, 0.0F, false);
        scope.texOffs(20, 158).addBox(2.728F, -30.5F, -4.332F, 1.0F, 2.0F, 5.0F, 0.0F, false);

        bone29 = new ModelRenderer(this);
        bone29.setPos(-1.366F, -37.8547F, -0.3726F);
        scope.addChild(bone29);
        setRotationAngle(bone29, 0.0F, 0.0F, -0.7854F);
        bone29.texOffs(88, 81).addBox(1.6985F, 0.6476F, -4.5F, 1.0F, 2.0F, 9.0F, 0.0F, false);
        bone29.texOffs(88, 81).addBox(1.6985F, 0.6476F, 13.5F, 1.0F, 2.0F, 4.0F, 0.0F, false);
        bone29.texOffs(88, 81).addBox(2.1985F, 1.1476F, -14.5F, 0.0F, 1.0F, 10.0F, 0.0F, false);
        bone29.texOffs(88, 81).addBox(-0.2157F, -0.2666F, -14.5F, 1.0F, 0.0F, 10.0F, 0.0F, false);
        bone29.texOffs(88, 81).addBox(-0.2157F, 3.5618F, -14.5F, 1.0F, 0.0F, 10.0F, 0.0F, false);
        bone29.texOffs(88, 81).addBox(-1.6299F, 1.1476F, -14.5F, 0.0F, 1.0F, 10.0F, 0.0F, false);
        bone29.texOffs(88, 81).addBox(-0.7157F, 3.0618F, -4.5F, 2.0F, 1.0F, 9.0F, 0.0F, false);
        bone29.texOffs(88, 81).addBox(-0.7157F, 3.0618F, 13.5F, 2.0F, 1.0F, 4.0F, 0.0F, false);
        bone29.texOffs(88, 81).addBox(-0.7157F, -0.7666F, -4.5F, 2.0F, 1.0F, 9.0F, 0.0F, false);
        bone29.texOffs(88, 81).addBox(-0.7157F, -0.7666F, 13.5F, 2.0F, 1.0F, 4.0F, 0.0F, false);
        bone29.texOffs(88, 81).addBox(-2.1299F, 0.6476F, -4.5F, 1.0F, 2.0F, 9.0F, 0.0F, false);
        bone29.texOffs(88, 81).addBox(-2.1299F, 0.6476F, 13.5F, 1.0F, 2.0F, 4.0F, 0.0F, false);

        bone30 = new ModelRenderer(this);
        bone30.setPos(2.0F, -31.0547F, -32.5766F);
        scope.addChild(bone30);
        setRotationAngle(bone30, 0.0F, 0.0F, -0.5236F);
        bone30.texOffs(88, 81).addBox(0.6859F, -7.9201F, 35.88F, 1.0F, 0.0F, 10.0F, 0.0F, true);
        bone30.texOffs(88, 81).addBox(3.052F, -6.5541F, 35.88F, 0.0F, 1.0F, 10.0F, 0.0F, true);
        bone30.texOffs(88, 81).addBox(-0.6801F, -6.5541F, 35.88F, 0.0F, 1.0F, 10.0F, 0.0F, true);
        bone30.texOffs(88, 81).addBox(0.6859F, -4.1881F, 35.88F, 1.0F, 0.0F, 10.0F, 0.0F, true);

        bone31 = new ModelRenderer(this);
        bone31.setPos(-2.0F, -31.0547F, -32.5766F);
        scope.addChild(bone31);
        setRotationAngle(bone31, 0.0F, 0.0F, 0.5236F);
        bone31.texOffs(88, 81).addBox(-3.052F, -6.5541F, 35.88F, 0.0F, 1.0F, 10.0F, 0.0F, false);
        bone31.texOffs(88, 81).addBox(-1.6859F, -7.9201F, 35.88F, 1.0F, 0.0F, 10.0F, 0.0F, false);
        bone31.texOffs(88, 81).addBox(0.6801F, -6.5541F, 35.88F, 0.0F, 1.0F, 10.0F, 0.0F, false);
        bone31.texOffs(88, 81).addBox(-1.6859F, -4.1881F, 35.88F, 1.0F, 0.0F, 10.0F, 0.0F, false);

        bone22 = new ModelRenderer(this);
        bone22.setPos(-1.9142F, -36.8907F, 17.6274F);
        scope.addChild(bone22);
        setRotationAngle(bone22, 0.0F, -0.2618F, 0.7854F);
        bone22.texOffs(88, 81).addBox(-2.1892F, -2.3536F, -6.0721F, 0.0F, 2.0F, 2.0F, 0.0F, false);

        bone24 = new ModelRenderer(this);
        bone24.setPos(0.0F, -38.8049F, 17.6274F);
        scope.addChild(bone24);
        setRotationAngle(bone24, 0.2618F, 0.0F, 0.0F);
        bone24.texOffs(88, 81).addBox(-1.0F, -1.6476F, -6.2173F, 2.0F, 0.0F, 2.0F, 0.0F, true);

        bone21 = new ModelRenderer(this);
        bone21.setPos(-1.9142F, -36.8907F, 17.6274F);
        scope.addChild(bone21);
        setRotationAngle(bone21, 0.0F, -0.2618F, 0.0F);
        bone21.texOffs(88, 81).addBox(-1.6476F, -1.0F, -6.2173F, 0.0F, 2.0F, 2.0F, 0.0F, false);

        bone25 = new ModelRenderer(this);
        bone25.setPos(0.0F, -44.1951F, 17.6274F);
        scope.addChild(bone25);
        setRotationAngle(bone25, -0.2618F, 0.0F, 0.0F);
        bone25.texOffs(88, 81).addBox(-1.0F, 10.5522F, -3.8313F, 2.0F, 0.0F, 2.0F, 0.0F, true);

        bone26 = new ModelRenderer(this);
        bone26.setPos(-1.9142F, 36.8907F, 17.6274F);
        scope.addChild(bone26);
        setRotationAngle(bone26, 0.0F, -0.2618F, -0.7854F);
        bone26.texOffs(88, 81).addBox(48.2044F, -51.8178F, -19.5751F, 0.0F, 2.0F, 2.0F, 0.0F, false);

        bone27 = new ModelRenderer(this);
        bone27.setPos(1.9142F, 36.8907F, 17.6274F);
        scope.addChild(bone27);
        setRotationAngle(bone27, 0.0F, 0.2618F, 0.7854F);
        bone27.texOffs(88, 81).addBox(-48.2044F, -51.8178F, -19.5751F, 0.0F, 2.0F, 2.0F, 0.0F, true);

        bone20 = new ModelRenderer(this);
        bone20.setPos(1.9142F, -36.8907F, 17.6274F);
        scope.addChild(bone20);
        setRotationAngle(bone20, 0.0F, 0.2618F, 0.0F);
        bone20.texOffs(88, 81).addBox(1.6476F, -1.0F, -6.2173F, 0.0F, 2.0F, 2.0F, 0.0F, true);

        bone23 = new ModelRenderer(this);
        bone23.setPos(1.9142F, -36.8907F, 17.6274F);
        scope.addChild(bone23);
        setRotationAngle(bone23, 0.0F, 0.2618F, -0.7854F);
        bone23.texOffs(88, 81).addBox(2.1892F, -2.3536F, -6.0721F, 0.0F, 2.0F, 2.0F, 0.0F, true);

        reticle = new ModelRenderer(this);
        reticle.setPos(0.0F, 24.0F, 0.0F);
        reticle.texOffs(49, 465).addBox(-1.5F, -38.5F, 16.7F, 3.0F, 3.0F, 0.0F, 0.0F, false);

        overlay = new ModelRenderer(this);
        overlay.setPos(0.0F, 24.0F, 0.0F);
        overlay.texOffs(15, 461).addBox(-1.5F, -38.5F, 16.9F, 3.0F, 3.0F, 0.0F, 0.0F, false);
        overlay.texOffs(15, 461).addBox(-1.5F, -38.35F, -14.5F, 3.0F, 3.0F, 0.0F, 0.0F, false);

        setBuiltInRender(reticle);
        setBuiltInRender(overlay);
    }
}

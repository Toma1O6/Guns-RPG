package dev.toma.gunsrpg.client.model.component;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.toma.gunsrpg.client.render.item.AbstractWeaponRenderer;
import dev.toma.gunsrpg.config.ModConfig;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class ReflexSightModel extends AbstractAttachmentModel implements IOpticsProvider {

    private final ModelRenderer sight;
    private final ModelRenderer base;
    private final ModelRenderer b_a1;
    private final ModelRenderer b_a2;
    private final ModelRenderer top;
    private final ModelRenderer back_a;
    private final ModelRenderer front_a;
    private final ModelRenderer front_a2;
    private final ModelRenderer glass;
    private final ModelRenderer top_side;
    private final ModelRenderer reticle;

    @Override
    public void renderAttachment(MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay, float aimingProgress) {
        stack.mulPose(Vector3f.YP.rotation((float) Math.PI));
        renderReflexSight(stack, buffer, this, aimingProgress, light, overlay);
    }

    @Override
    public ResourceLocation getComponentTextureMap() {
        return AbstractWeaponRenderer.ATTACHMENTS;
    }

    @Override
    public ResourceLocation getReticleTextureMap() {
        return ModConfig.clientConfig.reticleVariants.getAsResource();
    }

    @Override
    public ModelRenderer getGlassModel() {
        return reticle;
    }

    @Override
    public ModelRenderer getOverlayModel() {
        throw new UnsupportedOperationException("This type of reticle doesn't support 'overlay' model");
    }

    @Override
    public int getReticleTintARGB() {
        return ModConfig.clientConfig.reticleColor.getColor();
    }

    @Override
    public void renderOptic(MatrixStack stack, IVertexBuilder builder, int light, int overlay) {
        sight.render(stack, builder, light, overlay);
    }

    public ReflexSightModel() {
        texWidth = 64;
        texHeight = 64;

        sight = new ModelRenderer(this);
        sight.setPos(0.0F, 24.0F, 0.0F);

        base = new ModelRenderer(this);
        base.setPos(0.0F, 0.0F, 0.0F);
        sight.addChild(base);
        base.texOffs(0, 0).addBox(-2.0F, -2.0F, -6.0F, 4.0F, 1.0F, 8.0F, 0.0F, false);
        base.texOffs(0, 0).addBox(-1.75F, -2.15F, -5.5F, 3.0F, 1.0F, 7.0F, 0.0F, false);
        base.texOffs(0, 0).addBox(0.75F, -2.15F, -5.5F, 1.0F, 1.0F, 7.0F, 0.0F, false);
        base.texOffs(0, 0).addBox(1.0F, -1.5F, -6.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);
        base.texOffs(0, 0).addBox(-2.0F, -1.5F, -6.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);

        b_a1 = new ModelRenderer(this);
        b_a1.setPos(0.0F, 0.0F, 0.0F);
        base.addChild(b_a1);
        setRotationAngle(b_a1, 0.0F, 0.0F, 0.7854F);
        b_a1.texOffs(0, 0).addBox(-0.6464F, -2.0607F, -6.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);

        b_a2 = new ModelRenderer(this);
        b_a2.setPos(0.0F, 0.0F, 0.0F);
        base.addChild(b_a2);
        setRotationAngle(b_a2, 0.0F, 0.0F, -0.7854F);
        b_a2.texOffs(0, 0).addBox(-0.3536F, -2.0607F, -6.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);

        top = new ModelRenderer(this);
        top.setPos(0.0F, 0.1F, 0.0F);
        sight.addChild(top);
        top.texOffs(0, 0).addBox(-2.0F, -3.2F, -6.0F, 4.0F, 1.0F, 8.0F, 0.0F, false);
        top.texOffs(0, 0).addBox(-2.0F, -3.9F, 0.0F, 4.0F, 1.0F, 2.0F, 0.0F, false);
        top.texOffs(0, 0).addBox(-2.0F, -3.884F, -7.8794F, 4.0F, 1.0F, 2.0F, 0.0F, false);

        back_a = new ModelRenderer(this);
        back_a.setPos(0.0F, 0.0F, 0.0F);
        top.addChild(back_a);
        setRotationAngle(back_a, -0.3491F, 0.0F, 0.0F);
        back_a.texOffs(0, 0).addBox(-2.0F, -1.0152F, -8.3906F, 4.0F, 1.0F, 2.0F, 0.0F, false);

        front_a = new ModelRenderer(this);
        front_a.setPos(0.0F, 0.0F, 0.0F);
        top.addChild(front_a);
        setRotationAngle(front_a, 0.7854F, 0.0F, 0.0F);
        front_a.texOffs(0, 0).addBox(-1.0F, -2.7577F, 1.7577F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        front_a.texOffs(0, 0).addBox(-2.0F, -6.9038F, -2.4109F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        front_a.texOffs(0, 0).addBox(1.0F, -6.9038F, -2.4109F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        front_a2 = new ModelRenderer(this);
        front_a2.setPos(0.0F, 0.0F, 0.0F);
        top.addChild(front_a2);
        setRotationAngle(front_a2, -1.309F, 0.0F, 0.0F);
        front_a2.texOffs(18, 9).addBox(1.0F, -1.0094F, -3.7671F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        front_a2.texOffs(18, 9).addBox(-2.0F, -1.0094F, -3.7671F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        glass = new ModelRenderer(this);
        glass.setPos(8.5F, 2.7F, 1.0F);
        sight.addChild(glass);
        glass.texOffs(0, 0).addBox(-10.0F, -11.0F, 0.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        glass.texOffs(0, 0).addBox(-10.0F, -7.0F, 0.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
        glass.texOffs(0, 0).addBox(-7.2929F, -10.2929F, 0.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);
        glass.texOffs(0, 0).addBox(-10.7071F, -10.2929F, 0.0F, 1.0F, 3.0F, 1.0F, 0.0F, false);

        top_side = new ModelRenderer(this);
        top_side.setPos(0.0F, 0.0F, 0.0F);
        glass.addChild(top_side);
        setRotationAngle(top_side, 0.0F, 0.0F, -0.7854F);
        top_side.texOffs(0, 0).addBox(1.8284F, -12.7279F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        top_side.texOffs(0, 0).addBox(-1.2929F, -10.6066F, 0.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        top_side.texOffs(0, 0).addBox(-0.2929F, -14.8492F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        top_side.texOffs(0, 0).addBox(-2.4142F, -12.7279F, 0.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        reticle = new ModelRenderer(this);
        reticle.setPos(0.0F, 24.0F, 0.0F);
        reticle.texOffs(53, 58).addBox(-1.5F, -7.5F, 1.5F, 3.0F, 3.0F, 0.0F, 0.0F, false);

        setBuiltInRender(reticle);
    }
}

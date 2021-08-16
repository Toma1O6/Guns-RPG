package dev.toma.gunsrpg.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.render.item.AbstractWeaponRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ModelRenderer;

public class SuppressorModel extends AbstractAttachmentModel {

    private final ModelRenderer suppressor;
    private final ModelRenderer a1;
    private final ModelRenderer a2;

    public SuppressorModel() {
        texWidth = 64;
        texHeight = 64;

        suppressor = new ModelRenderer(this);
        suppressor.setPos(0.0F, 24.0F, 0.0F);
        suppressor.texOffs(0, 0).addBox(-1.0F, -2.0F, -20.0F, 2.0F, 2.0F, 13.0F, 0.0F, false);
        suppressor.texOffs(0, 0).addBox(-1.0F, -4.8284F, -20.0F, 2.0F, 2.0F, 13.0F, 0.0F, false);
        suppressor.texOffs(0, 0).addBox(-2.4142F, -3.4142F, -20.0F, 2.0F, 2.0F, 13.0F, 0.0F, false);
        suppressor.texOffs(0, 0).addBox(0.4142F, -3.4142F, -20.0F, 2.0F, 2.0F, 13.0F, 0.0F, false);

        a1 = new ModelRenderer(this);
        a1.setPos(0.0F, 0.0F, 0.0F);
        suppressor.addChild(a1);
        setRotationAngle(a1, 0.0F, 0.0F, 0.7854F);
        a1.texOffs(0, 0).addBox(-2.7071F, -1.2929F, -20.0F, 2.0F, 2.0F, 13.0F, 0.0F, false);
        a1.texOffs(0, 0).addBox(-2.7071F, -4.1213F, -20.0F, 2.0F, 2.0F, 13.0F, 0.0F, false);

        a2 = new ModelRenderer(this);
        a2.setPos(0.0F, 0.0F, 0.0F);
        suppressor.addChild(a2);
        setRotationAngle(a2, 0.0F, 0.0F, -0.7854F);
        a2.texOffs(0, 0).addBox(0.7071F, -4.1213F, -20.0F, 2.0F, 2.0F, 13.0F, 0.0F, false);
        a2.texOffs(0, 0).addBox(0.7071F, -1.2929F, -20.0F, 2.0F, 2.0F, 13.0F, 0.0F, false);
    }

    @Override
    public void renderAttachment(MatrixStack stack, IRenderTypeBuffer buffer, int light, int overlay, float aimingProgress) {
        suppressor.render(stack, buffer.getBuffer(renderType(AbstractWeaponRenderer.ATTACHMENTS)), light, overlay);
    }
}

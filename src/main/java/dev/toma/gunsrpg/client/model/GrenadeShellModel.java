package dev.toma.gunsrpg.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class GrenadeShellModel extends Model {

    private final ModelRenderer grenade;

    public GrenadeShellModel() {
        super(RenderType::entityCutout);
        texWidth = 512;
        texHeight = 512;
        grenade = new ModelRenderer(this);
        grenade.setPos(0.0F, 24.0F, 3.0F);
        grenade.texOffs(221, 102).addBox(-2.0F, -4.0F, 0.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);
        grenade.texOffs(213, 106).addBox(-2.0F, -4.0F, -6.5F, 4.0F, 4.0F, 6.0F, 0.0F, false);
        grenade.texOffs(159, 26).addBox(-1.5F, -3.5F, -7.5F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        grenade.texOffs(166, 38).addBox(-1.75F, -3.75F, -7.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        grenade.texOffs(166, 45).addBox(0.75F, -3.75F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        grenade.texOffs(155, 26).addBox(-1.75F, -1.25F, -7.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        grenade.texOffs(142, 23).addBox(-1.25F, -3.25F, -7.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        grenade.texOffs(142, 23).addBox(-1.25F, -3.25F, -1.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        grenade.texOffs(166, 38).addBox(-1.75F, -3.75F, -1.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);
        grenade.texOffs(166, 45).addBox(0.75F, -3.75F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        grenade.texOffs(155, 26).addBox(-1.75F, -1.25F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
    }

    @Override
    public void renderToBuffer(MatrixStack matrix, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float alpha) {
        grenade.render(matrix, builder, light, overlay, red, green, blue, alpha);
    }
}

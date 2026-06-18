package com.wf.firearms.client.model.legacy;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

/** 1.16 ModelRenderer 移植（含原版 ModelBox 六面 UV）。 */
public class LegacyModelRenderer {
    public float x;
    public float y;
    public float z;
    public float xRot;
    public float yRot;
    public float zRot;
    public boolean visible = true;

    private final LegacyModel model;
    private final List<LegacyModelRenderer> children = new ArrayList<>();
    private final List<ModelBox> cubes = new ArrayList<>();
    private int pendingU;
    private int pendingV;

    public LegacyModelRenderer(LegacyModel model) {
        this.model = model;
    }

    public void setPos(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void addChild(LegacyModelRenderer child) {
        children.add(child);
    }

    public LegacyModelRenderer texOffs(int u, int v) {
        pendingU = u;
        pendingV = v;
        return this;
    }

    public void addBox(float x, float y, float z, float w, float h, float d, float expand, boolean mirror) {
        cubes.add(new ModelBox(pendingU, pendingV, x, y, z, w, h, d, expand, mirror));
    }

    public void render(PoseStack poseStack, VertexConsumer consumer, int light, int overlay) {
        render(poseStack, consumer, light, overlay, 1f, 1f, 1f, 1f);
    }

    public void render(
            PoseStack poseStack,
            VertexConsumer consumer,
            int light,
            int overlay,
            float red,
            float green,
            float blue,
            float alpha) {
        if (!visible) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(x / 16f, y / 16f, z / 16f);
        if (zRot != 0.0F) {
            poseStack.mulPose(new Quaternionf().rotationZ(zRot));
        }
        if (yRot != 0.0F) {
            poseStack.mulPose(new Quaternionf().rotationY(yRot));
        }
        if (xRot != 0.0F) {
            poseStack.mulPose(new Quaternionf().rotationX(xRot));
        }
        for (ModelBox cube : cubes) {
            cube.render(poseStack, consumer, light, overlay, red, green, blue, alpha, model.texWidth, model.texHeight);
        }
        for (LegacyModelRenderer child : children) {
            child.render(poseStack, consumer, light, overlay, red, green, blue, alpha);
        }
        poseStack.popPose();
    }

    /** 与 net.minecraft.client.model.ModelRenderer.ModelBox 一致的六面体。 */
    private static final class ModelBox {
        private final TexturedQuad[] quads;

        ModelBox(int texU, int texV, float x, float y, float z, float w, float h, float depth, float delta, boolean mirror) {
            float x0 = x - delta;
            float y0 = y - delta;
            float z0 = z - delta;
            float x1 = x + w + delta;
            float y1 = y + h + delta;
            float z1 = z + depth + delta;
            if (mirror) {
                float tmp = x0;
                x0 = x1;
                x1 = tmp;
            }

            float s = 1.0F / 16.0F;
            float ax0 = x0 * s;
            float ay0 = y0 * s;
            float az0 = z0 * s;
            float ax1 = x1 * s;
            float ay1 = y1 * s;
            float az1 = z1 * s;

            Vertex[] corners = new Vertex[8];
            corners[0] = new Vertex(ax0, ay0, az0);
            corners[1] = new Vertex(ax1, ay0, az0);
            corners[2] = new Vertex(ax1, ay1, az0);
            corners[3] = new Vertex(ax0, ay1, az0);
            corners[4] = new Vertex(ax0, ay0, az1);
            corners[5] = new Vertex(ax1, ay0, az1);
            corners[6] = new Vertex(ax1, ay1, az1);
            corners[7] = new Vertex(ax0, ay1, az1);

            quads = new TexturedQuad[6];
            quads[0] = new TexturedQuad(corners[5], corners[6], corners[2], corners[1], texU + depth, texV + depth, texU + depth + w, texV + depth + h);
            quads[1] = new TexturedQuad(corners[0], corners[3], corners[7], corners[4], texU + depth + w, texV + depth, texU + depth + w + w, texV + depth + h);
            quads[2] = new TexturedQuad(corners[5], corners[4], corners[0], corners[1], texU + depth, texV, texU + depth + depth, texV + depth);
            quads[3] = new TexturedQuad(corners[2], corners[3], corners[7], corners[6], texU + depth + w + depth, texV, texU + depth + w + depth + depth, texV + depth);
            quads[4] = new TexturedQuad(corners[1], corners[0], corners[4], corners[5], texU + depth, texV + depth, texU + depth + w, texV + depth + depth);
            quads[5] = new TexturedQuad(corners[6], corners[7], corners[3], corners[2], texU + depth + w, texV + depth, texU + depth + w + w, texV + depth + depth);
        }

        void render(
                PoseStack poseStack,
                VertexConsumer consumer,
                int light,
                int overlay,
                float r,
                float g,
                float b,
                float a,
                int texW,
                int texH) {
            for (TexturedQuad quad : quads) {
                quad.draw(poseStack, consumer, light, overlay, r, g, b, a, texW, texH);
            }
        }
    }

    private record Vertex(float x, float y, float z) {}

    private static final class TexturedQuad {
        private final Vertex[] vertices;
        private final float u0;
        private final float v0;
        private final float u1;
        private final float v1;

        TexturedQuad(Vertex a, Vertex b, Vertex c, Vertex d, float u0, float v0, float u1, float v1) {
            this.vertices = new Vertex[] {a, b, c, d};
            this.u0 = u0;
            this.v0 = v0;
            this.u1 = u1;
            this.v1 = v1;
        }

        void draw(
                PoseStack poseStack,
                VertexConsumer consumer,
                int light,
                int overlay,
                float r,
                float g,
                float b,
                float a,
                int texW,
                int texH) {
            PoseStack.Pose pose = poseStack.last();
            Matrix4f mat = pose.pose();
            Matrix3f normalMat = pose.normal();

            float[] us = {u0 / texW, u1 / texW, u1 / texW, u0 / texW};
            float[] vs = {v0 / texH, v0 / texH, v1 / texH, v1 / texH};

            Vector3f edge1 = new Vector3f(
                    vertices[1].x - vertices[0].x,
                    vertices[1].y - vertices[0].y,
                    vertices[1].z - vertices[0].z);
            Vector3f edge2 = new Vector3f(
                    vertices[2].x - vertices[0].x,
                    vertices[2].y - vertices[0].y,
                    vertices[2].z - vertices[0].z);
            Vector3f normal = edge1.cross(edge2).normalize();
            normal.mul(normalMat);

            for (int i = 0; i < 4; i++) {
                Vertex v = vertices[i];
                Vector4f p = new Vector4f(v.x, v.y, v.z, 1f);
                p.mul(mat);
                consumer.vertex(p.x(), p.y(), p.z())
                        .color(r, g, b, a)
                        .uv(us[i], vs[i])
                        .overlayCoords(overlay)
                        .uv2(light)
                        .normal(normal.x(), normal.y(), normal.z())
                        .endVertex();
            }
        }
    }
}

package dev.toma.gunsrpg.client.screen.skill;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPerkProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.IPointProvider;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.client.render.IOrderedRender;
import dev.toma.gunsrpg.client.screen.widgets.*;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_RequestExtensionSkillLockPacket;
import dev.toma.gunsrpg.util.RenderUtils;
import lib.toma.animations.QuickSort;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Comparator;

public class ExtensionsView extends View {

    private static final int RESET_PRICE = 15;

    private static final ResourceLocation PERKPOINT_BOOK = GunsRPG.makeResource("textures/item/perkpoint_book.png");
    private final View last;
    private final SkillType<?> head;

    private PannableWidget canvas;
    private FooterWidget footer;
    private SkillInfoWidget skillInfo;
    private SimpleButton reset;

    public ExtensionsView(int width, int height, IViewManager manager, View last, SkillType<?> head) {
        super(width, height, manager);
        this.last = last;
        this.head = head;
    }

    @Override
    protected void init() {
        clear();
        // skills
        canvas = addWidget(new PannableWidget(x, y + 30, width, height - 60));
        canvas.setEmptyClickResponder(this::skillClicked);
        // return button
        addWidget(new SimpleButton(x + 5, y + 5, 20, 20, this::backButtonClicked));
        // header
        ITextComponent title = new TranslationTextComponent("view.extension.header", head.getTitle());
        addWidget(new HeaderWidget(x, y, width, 30, title, font));
        // footer
        IViewContext context = manager.getContext();
        IPlayerData data = context.getData();
        ISkillProperties properties = head.getHierarchy().getExtensions()[0].getProperties();
        ITransactionValidator validator = properties.getTransactionValidator();
        IPointProvider provider = validator.getData(data);
        footer = addWidget(new FooterWidget(x, y + height - 20, width, 20, font, provider));
        footer.setColorSchema(0xFFFF00, 0xCCCC00);
        // info
        skillInfo = addWidget(new SkillInfoWidget(x, y + height - 80, width, 80, manager));
        reset = addWidget(new SimpleButton(x + width - 75, y + 5, 70, 20, new StringTextComponent("Reset skills"), this::resetSkillsClicked, this::renderResetSkillsButtonTooltip));

        // visibility
        showSkills();
        setSkillInfoVisibility(false);
        sortRenderOrder();
    }

    private void skillClicked(@Nullable SkillType<?> type) {
        skillInfo.updateSource(type);
        setSkillInfoVisibility(type != null);
    }

    private void backButtonClicked(Button button) {
        manager.setView(last);
    }

    private void showSkills() {
        canvas.fill(this::placeSkills);
        updateResetButtonState();
    }

    private void placeSkills(PannableWidget.IContentManager manager, int x, int y) {
        IViewContext context = this.manager.getContext();
        int index = 0;
        int level = 0;
        int posY = y;
        SkillType<?>[] ext = head.getHierarchy().getExtensions();
        QuickSort.sort(ext, Comparator.comparing(SkillType::getProperties, Comparator.comparingInt(ISkillProperties::getRequiredLevel)));
        for (SkillType<?> type : ext) {
            ISkillProperties properties = type.getProperties();
            int lvl = properties.getRequiredLevel();
            if (lvl > level) {
                level = lvl;
                posY += 40;
                index = 0;
            }
            SkillWidget widget = manager.add(new SkillWidget(x + 40 + (index++) * 35, 30 + posY, 22, 22, type, context, false));
            widget.setClickResponder(this::skillClicked);
        }
    }

    private void setSkillInfoVisibility(boolean visibility) {
        synchronized (lock) {
            footer.visible = !visibility;
            skillInfo.visible = visibility;
            canvas.updateSize(width, height - (visibility ? 110 : 50));
        }
    }

    private void resetSkillsClicked(Button button) {
        NetworkManager.sendServerPacket(new C2S_RequestExtensionSkillLockPacket(head));
        init();
    }

    private void updateResetButtonState() {
        IPlayerData data = manager.getContext().getData();
        IPerkProvider provider = data.getPerkProvider();
        boolean hasFunds = provider.getPoints() >= 15;
        ISkillProvider skillProvider = data.getSkillProvider();
        boolean hasAnySkill = false;
        SkillType<?>[] extensions = head.getHierarchy().getExtensions();
        for (SkillType<?> type : extensions) {
            if (skillProvider.hasSkill(type)) {
                hasAnySkill = true;
                break;
            }
        }
        reset.active = hasAnySkill && hasFunds;
    }

    private void renderResetSkillsButtonTooltip(Button button, MatrixStack matrix, int mouseX, int mouseY) {
        int x = mouseX + 12;
        int y = mouseY - 12;
        int z = 400;
        String text = String.valueOf(RESET_PRICE);
        int tooltipWidth = font.width(text) + 25;
        int tooltipHeight = 20;
        if (x + tooltipWidth > this.width) {
            x -= 28 + tooltipWidth;
        }
        if (y + tooltipHeight > this.height) {
            y = this.height - tooltipHeight;
        }
        int color1 = 0xf0100010;
        int color2 = 0x505000ff;
        int color3 = 0x5028007f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuilder();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        matrix.pushPose();
        Matrix4f pose = matrix.last().pose();
        fillGradient(pose, builder, x - 3, y - 4, x + tooltipWidth + 3, y - 3, z, color1, color1);
        fillGradient(pose, builder, x - 3, y + tooltipHeight + 3, x + tooltipWidth + 3, y + tooltipHeight + 4, z, color1, color1);
        fillGradient(pose, builder, x - 3, y - 3, x + tooltipWidth + 3, y + tooltipHeight + 3, z, color1, color1);
        fillGradient(pose, builder, x - 4, y - 3, x - 3, y + tooltipHeight + 3, z, color1, color1);
        fillGradient(pose, builder, x + tooltipWidth + 3, y - 3, x + tooltipWidth + 4, y + tooltipHeight + 3, z, color1, color1);
        fillGradient(pose, builder, x - 3, y - 3 + 1, x - 3 + 1, y + tooltipHeight + 3 - 1, 400, color2, color3);
        fillGradient(pose, builder, x + tooltipWidth + 2, y - 3 + 1, x + tooltipWidth + 3, y + tooltipHeight + 3 - 1, z, color2, color3);
        fillGradient(pose, builder, x - 3, y - 3, x + tooltipWidth + 3, y - 3 + 1, z, color2, color2);
        fillGradient(pose, builder, x - 3, y + tooltipHeight + 2, x + tooltipWidth + 3, y + tooltipHeight + 3, z, color3, color3);
        RenderSystem.enableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(GL11.GL_SMOOTH);
        tessellator.end();
        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        builder.vertex(pose, x + tooltipWidth - 20, y + 2, z).uv(0.0F, 0.0F).endVertex();
        builder.vertex(pose, x + tooltipWidth - 20, y + 18, z).uv(0.0F, 1.0F).endVertex();
        builder.vertex(pose, x + tooltipWidth - 2, y + 18, z).uv(1.0F, 1.0F).endVertex();
        builder.vertex(pose, x + tooltipWidth - 2, y + 2, z).uv(1.0F, 0.0F).endVertex();
        client.getTextureManager().bind(PERKPOINT_BOOK);
        tessellator.end();
        IRenderTypeBuffer.Impl typeBuffer = IRenderTypeBuffer.immediate(builder);
        matrix.translate(0, 0, z);
        font.drawInBatch(text, x + (16 - font.width(text)) / 2.0F, y + (tooltipHeight - 9) / 2.0F, -1, true, pose, typeBuffer, false, 0, 0xf000f0);
        typeBuffer.endBatch();
        matrix.popPose();
    }

    private static class SimpleButton extends Button implements IOrderedRender {

        SimpleButton(int x, int y, int width, int height, IPressable clickEvent) {
            this(x, y, width, height, new StringTextComponent("<<<"), clickEvent, (button, matrix, mouseX, mouseY) -> {});
        }

        SimpleButton(int x, int y, int width, int height, ITextComponent component, IPressable clickEvent, ITooltip tooltip) {
            super(x, y, width, height, component, clickEvent, tooltip);
        }

        @Override
        public void renderButton(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
            if (isHovered) {
                RenderUtils.drawSolid(matrix.last().pose(), x, y, x + width, y + height, 0x66FFFFFF);
            }
            FontRenderer renderer = Minecraft.getInstance().font;
            RenderUtils.drawCenteredShadowText(matrix, getMessage(), renderer, this, active ? 0xFFFFFF : 0x888888);
            if (isHovered) {
                renderToolTip(matrix, mouseX, mouseY);
            }
        }

        @Override
        public int getRenderIndex() {
            return 1;
        }
    }
}

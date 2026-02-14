package dev.toma.gunsrpg.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DialogScreen extends Screen {

    private final Screen parent;
    private final ITextComponent header;
    private List<ITextComponent> content;
    private DialogEventHandler onConfirm;
    private int dialogWidth, dialogHeight;
    private ITextComponent confirmText = new TranslationTextComponent("screen.dialog.confirm");
    private ITextComponent denyText = new TranslationTextComponent("screen.dialog.deny");
    private List<IReorderingProcessor> formattedContents;

    private int left, top;

    private DialogScreen(Screen parent, ITextComponent header) {
        super(parent.getTitle());
        this.parent = parent;
        this.header = header;
        this.content = Collections.emptyList();
        this.onConfirm = () -> {};
    }

    public static DialogScreen create(Screen parent, ITextComponent header) {
        return new DialogScreen(parent, header);
    }

    public void setContent(List<ITextComponent> content) {
        this.content = content;
    }

    public void setContent(ITextComponent... content) {
        this.setContent(Arrays.asList(content));
    }

    public void setConfirmHandler(DialogEventHandler onConfirm) {
        this.onConfirm = onConfirm;
    }

    public void setConfirmText(ITextComponent confirmText) {
        this.confirmText = confirmText;
    }

    public void setDenyText(ITextComponent denyText) {
        this.denyText = denyText;
    }

    public void setActive(Minecraft client) {
        client.setScreen(this);
    }

    @Override
    protected void init() {
        this.parent.init(this.minecraft, this.width, this.height);
        this.dialogWidth = Math.max(this.font.width(this.header) + 10, 150);
        this.formattedContents = this.content.stream()
                .map(text -> this.font.split(text, this.dialogWidth - 10))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        this.dialogHeight = 50 + this.formattedContents.size() * 10;
        this.left = (this.width - this.dialogWidth) / 2;
        this.top = (this.height - this.dialogHeight) / 2;

        int btnWidth = (this.dialogWidth - 15) / 2;
        addButton(new Button(this.left + 5, this.top + this.dialogHeight - 25, btnWidth, 20, this.denyText, btn -> this.navigateToParentScreen()));
        addButton(new Button(this.left + this.dialogWidth - 5 - btnWidth, this.top + this.dialogHeight - 25, btnWidth, 20, this.confirmText, this::accepted));
    }

    private void navigateToParentScreen() {
        this.minecraft.setScreen(this.parent);
    }

    private void accepted(Button button) {
        if (this.onConfirm != null) {
            this.onConfirm.onButtonEvent();
        }
        this.navigateToParentScreen();
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        this.parent.render(matrix, -1, -1, partialTicks);
        matrix.pushPose();
        matrix.translate(0, 0, 900);
        Matrix4f pose = matrix.last().pose();
        RenderUtils.drawSolid(pose, 0, 0, this.width, this.height, 0xDD << 24);
        RenderUtils.drawGradient(pose, this.left, this.top, this.left + this.dialogWidth, this.top + this.dialogHeight, 0xFFCCCC00, 0xFFAAAA00);
        RenderUtils.drawSolid(pose, this.left + 1, this.top + 1, this.left + this.dialogWidth - 1, this.top + this.dialogHeight - 1, 0xFF << 24);
        super.render(matrix, mouseX, mouseY, partialTicks);
        // header
        this.font.draw(matrix, this.header, this.left + 5, this.top + 5, 0xFFFFFF);
        // contents
        for (int i = 0; i < this.formattedContents.size(); i++) {
            this.font.draw(matrix, this.formattedContents.get(i), this.left + 5, this.top + 20 + i * 10, 0xFFFFFF);
        }
        matrix.popPose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @FunctionalInterface
    public interface DialogEventHandler {
        void onButtonEvent();
    }
}

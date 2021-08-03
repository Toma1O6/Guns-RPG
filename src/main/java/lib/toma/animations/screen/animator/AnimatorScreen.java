package lib.toma.animations.screen.animator;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.screen.animator.widget.ControlButton;
import lib.toma.animations.screen.animator.widget.IconButton;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class AnimatorScreen extends Screen {

    private static final ITextComponent NEW_PROJECT = new TranslationTextComponent("screen.animator.new_project");
    private static final ITextComponent OPEN = new TranslationTextComponent("screen.animator.open");
    private static final ITextComponent SAVE = new TranslationTextComponent("screen.animator.save");
    private static final ITextComponent SAVE_AS = new TranslationTextComponent("screen.animator.save_as");
    private static final ITextComponent SETTINGS = new TranslationTextComponent("screen.animator.settings");
    private static final ITextComponent PAUSED = new TranslationTextComponent("screen.animator.paused");

    public AnimatorScreen() {
        super(new TranslationTextComponent("screen.dev_tool.animator"));
    }

    @Override
    protected void init() {
        // ---- TOOLBAR (top)
        addButton(new IconButton(5, 5, 20, 20, 0, this::buttonNewProject_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, NEW_PROJECT, mx, my)));
        addButton(new IconButton(30, 5, 20, 20, 1, this::buttonOpen_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, OPEN, mx, my)));
        addButton(new IconButton(55, 5, 20, 20, 2, this::buttonSave_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, SAVE, mx, my)));
        addButton(new IconButton(80, 5, 20, 20, 2, this::buttonSaveAs_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, SAVE_AS, mx, my)));
        addButton(new IconButton(105, 5, 20, 20, 3, this::buttonSettings_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, SETTINGS, mx, my)));
        addButton(new ControlButton(width - 70, 5, 65, 20, PAUSED, this::isPaused, this::setPaused));
        // ----
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float deltaRenderTime) {
        drawToolbarBackground(stack);
        super.render(stack, mouseX, mouseY, deltaRenderTime);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void drawToolbarBackground(MatrixStack stack) {
        int toolbarHeight = 30;
        fill(stack, 0, 0, width, toolbarHeight, 0x67 << 24);
    }

    private void buttonNewProject_Clicked(Button button) {
        DialogScreen dialog = new NewProjectDialog(this);
        minecraft.setScreen(dialog);
    }

    private void buttonOpen_Clicked(Button button) {
        minecraft.setScreen(new ImportProjectScreen(this));
    }

    private void buttonSave_Clicked(Button button) {
        Animator animator = Animator.get();
        AnimationProject project = animator.getLatestProject();
        if (project.isNamed()) {
            project.saveProject();
        } else {
            buttonSaveAs_Clicked(button);
        }
    }

    private void buttonSaveAs_Clicked(Button button) {
        DialogScreen dialog = new SaveAsDialog(this);
        minecraft.setScreen(dialog);
    }

    private void buttonSettings_Clicked(Button button) {
        DialogScreen dialog = new SettingsDialog(this);
        minecraft.setScreen(dialog);
    }

    private boolean isPaused() {
        return Animator.get().getLatestProject().getAnimationControl().isPaused();
    }

    private void setPaused(boolean paused) {
        Animator.get().getLatestProject().getAnimationControl().setPaused(paused);
    }
}

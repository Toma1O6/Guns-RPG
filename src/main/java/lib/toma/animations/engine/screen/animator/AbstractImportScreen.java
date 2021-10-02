package lib.toma.animations.engine.screen.animator;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.engine.screen.animator.dialog.DialogScreen;
import lib.toma.animations.engine.screen.animator.dialog.SuggestionResponder;
import lib.toma.animations.engine.screen.animator.widget.ListView;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.function.Function;

public abstract class AbstractImportScreen extends Screen {

    protected final AnimatorScreen screen;
    protected ListView<String> paths;
    protected Button confirm;
    protected Button cancel;
    private String selectedPath;

    public AbstractImportScreen(AnimatorScreen screen) {
        super(new TranslationTextComponent("screen.animator.import"));
        this.screen = screen;
        Animator.get().refreshUserAnimations();
    }

    @Override
    public final void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        fill(stack, 0, 0, width, height, 0x67 << 24);
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public final boolean keyPressed(int code, int scanCode, int modifier) {
        DialogScreen.handleDefaultKeys(code, confirm, cancel);
        return super.keyPressed(code, scanCode, modifier);
    }

    @Override
    public final boolean isPauseScreen() {
        return false;
    }

    @Override
    public final boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected final void init() {
        TextFieldWidget fileFilter = addButton(new TextFieldWidget(font, 5, 5, width - 10, 20, StringTextComponent.EMPTY));
        fileFilter.setResponder(new SuggestionResponder("Find project", fileFilter, this::fileFilterChanged));

        Animator animator = Animator.get();
        paths = addButton(new ListView<>(5, 30, width - 10, height - getPathViewHeight(), animator.getPaths()));
        paths.setFormatter(Function.identity());
        paths.setResponder(this::pathSelected);

        int buttonsWidth = width - 10;
        int buttonWidth = (buttonsWidth - 5) / 2;
        placeControlButtons(buttonsWidth, buttonWidth);
        updatePathDependents(selectedPath);
    }

    protected abstract void placeControlButtons(int totalButtonWidth, int buttonWidth);

    protected void fileFilterChanged(String filterValue) {
        paths.setFilter(s -> acceptPath(s, filterValue));
    }

    protected void confirmClicked(Button button) {
        showParent();
    }

    protected void cancelClicked(Button button) {
        showParent();
    }

    protected void showParent() {
        minecraft.setScreen(screen);
    }

    protected String getSelectedPath() {
        return selectedPath;
    }

    protected int getPathViewHeight() {
        return 60;
    }

    protected void updatePathDependents(@Nullable String path) {
        confirm.active = path != null;
    }

    private boolean acceptPath(String path, String filter) {
        if (filter == null || filter.length() == 0) {
            return true;
        }
        return path.contains(filter);
    }

    private void pathSelected(String path) {
        selectedPath = path;
        updatePathDependents(path);
    }
}

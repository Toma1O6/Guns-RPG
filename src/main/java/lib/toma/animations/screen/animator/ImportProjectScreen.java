package lib.toma.animations.screen.animator;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.screen.animator.dialog.DialogScreen;
import lib.toma.animations.screen.animator.dialog.SuggestionResponder;
import lib.toma.animations.screen.animator.widget.ListView;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Function;

public class ImportProjectScreen extends Screen {

    private final AnimatorScreen screen;
    private ListView<String> files;
    private Button cancel;
    private Button confirm;
    private String filePath;

    public ImportProjectScreen(AnimatorScreen screen) {
        super(new TranslationTextComponent("screen.animator.import"));
        this.screen = screen;
    }

    @Override
    protected void init() {
        TextFieldWidget fileFilter = addButton(new TextFieldWidget(font, 5, 5, width - 10, 20, StringTextComponent.EMPTY));
        fileFilter.setSuggestion("Find project");
        fileFilter.setResponder(new SuggestionResponder("Find project", fileFilter, this::fileFilter_Change));
        Animator animator = Animator.get();
        files = addButton(new ListView<>(5, 30, width - 10, height - 60, animator.getPaths()));
        files.setFormatter(Function.identity());
        files.setResponder(this::file_Selected);

        int btnWidthP = width - 10;
        int btnWidth = (btnWidthP - 5) / 2;
        cancel = addButton(new Button(5, height - 25, btnWidth, 20, new StringTextComponent("Cancel"), this::cancel_clicked));
        confirm = addButton(new Button(10 + btnWidth, height - 25, btnWidth, 20, new StringTextComponent("Import"), this::confirm_clicked));
        confirm.active = filePath != null;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        fill(stack, 0, 0, width, height, 0x67 << 24);
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int code, int scanCode, int modifier) {
        DialogScreen.handleDefaultKeys(code, confirm, cancel);
        return super.keyPressed(code, scanCode, modifier);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    private void fileFilter_Change(String value) {
        files.setFilter(s -> accept(s, value));
    }

    private void file_Selected(String path) {
        filePath = path;
        confirm.active = filePath != null;
    }

    private void confirm_clicked(Button button) {
        Animator animator = Animator.get();
        FrameProviderWrapper wrapper = animator.getWrapper(filePath);
        if (wrapper != null) {
            animator.setUsingProject(new AnimationProject(wrapper));
        }
        minecraft.setScreen(screen);
    }

    private void cancel_clicked(Button button) {
        minecraft.setScreen(screen);
    }

    private boolean accept(String entry, String filter) {
        if (filter == null || filter.length() == 0)
            return true;
        else return entry.contains(filter);
    }
}

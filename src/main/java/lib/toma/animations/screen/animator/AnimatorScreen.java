package lib.toma.animations.screen.animator;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.pipeline.frame.IKeyframe;
import lib.toma.animations.pipeline.frame.MutableKeyframe;
import lib.toma.animations.screen.animator.dialog.*;
import lib.toma.animations.screen.animator.widget.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AnimatorScreen extends Screen {

    private static final ITextComponent NEW_PROJECT = new TranslationTextComponent("screen.animator.new_project");
    private static final ITextComponent OPEN = new TranslationTextComponent("screen.animator.open");
    private static final ITextComponent SAVE = new TranslationTextComponent("screen.animator.save");
    private static final ITextComponent SAVE_AS = new TranslationTextComponent("screen.animator.save_as");
    private static final ITextComponent SETTINGS = new TranslationTextComponent("screen.animator.settings");
    private static final ITextComponent PAUSED = new TranslationTextComponent("screen.animator.paused");
    private static final ITextComponent ADD_FRAME = new TranslationTextComponent("screen.animator.timeline.add_frame");
    private static final ITextComponent ADD_EVENT = new TranslationTextComponent("screen.animator.timeline.add_event");
    private static final ITextComponent REMOVE_FRAME = new TranslationTextComponent("screen.animator.timeline.remove_frame");
    private static final ITextComponent COPY_FRAME = new TranslationTextComponent("screen.animator.timeline.copy_frame");
    private static final ITextComponent END_FRAMES = new TranslationTextComponent("screen.animator.timeline.end_frames");
    private static final ITextComponent PROGRESS2FRAME = new TranslationTextComponent("screen.animator.timeline.progress2frame");
    private static final ITextComponent SET2BEGINNING = new TranslationTextComponent("screen.animator.timeline.to_beginning");
    private static final ITextComponent SET2END = new TranslationTextComponent("screen.animator.timeline.to_end");
    private static final ITextComponent NEXT_FRAME = new TranslationTextComponent("screen.animator.timeline.next_frame");
    // TODO remove non-api references
    private static final ResourceLocation ANIMATOR_ICONS = GunsRPG.makeResource("textures/icons/animator/animator_icons.png");
    private static final ResourceLocation TIMELINE_ICONS = GunsRPG.makeResource("textures/icons/animator/timeline_icons.png");

    private final Pattern posScaleDeg = Pattern.compile("-?[0-9]+(\\.[0-9]*)?");
    private final Pattern rotVec = Pattern.compile("-?(1(\\.0)?)|(0(\\.[0-9]+)?)");
    private final Pattern endpoint = Pattern.compile("(1(\\.0)?)|(0(\\.[0-9]+)?)");

    @Nullable
    private Timeline.IKeyframeSelectContext selectionContext;

    private WidgetContainer keyframeEditor;
    private TextFieldWidget posX;
    private TextFieldWidget posY;
    private TextFieldWidget posZ;
    private TextFieldWidget scaleX;
    private TextFieldWidget scaleY;
    private TextFieldWidget scaleZ;
    private TextFieldWidget rotX;
    private TextFieldWidget rotY;
    private TextFieldWidget rotZ;
    private TextFieldWidget deg;
    private TextFieldWidget end;

    private Timeline timeline;
    private IconButton addFrame;
    private IconButton removeFrame;
    private IconButton addEvent;
    private IconButton copyFrame;
    private IconButton endFrames;
    private IconButton progress2Frame;
    private IconButton toBeginning;
    private IconButton toEnd;
    private IconButton nextFrame;

    public AnimatorScreen() {
        super(new TranslationTextComponent("screen.dev_tool.animator"));
    }

    @Override
    protected void init() {
        // ---- TOOLBAR (top)
        IconButton.SETTINGS.set(ANIMATOR_ICONS, 2, 16);
        addButton(new IconButton(5, 5, 20, 20, 0, this::buttonNewProject_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, NEW_PROJECT, mx, my)));
        addButton(new IconButton(30, 5, 20, 20, 1, this::buttonOpen_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, OPEN, mx, my)));
        addButton(new IconButton(55, 5, 20, 20, 2, this::buttonSave_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, SAVE, mx, my)));
        addButton(new IconButton(80, 5, 20, 20, 2, this::buttonSaveAs_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, SAVE_AS, mx, my)));
        addButton(new IconButton(105, 5, 20, 20, 3, this::buttonSettings_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, SETTINGS, mx, my)));
        addButton(new ControlButton(width - 70, 5, 65, 20, PAUSED, this::isPaused, this::setPaused));
        // ---- KEYFRAME INSPECTOR
        keyframeEditor = addButton(new WidgetContainer(0, height - 200, 140, 120));
        keyframeEditor.backgroundColor = 0x40 << 24;
        keyframeEditor.frameSize = 1;
        keyframeEditor.frameColor = 0x67 << 24;
        keyframeEditor.addWidget(new LabelWidget(5, 2, 130, 15, new StringTextComponent("Keyframe inspector"), font));
        posX = keyframeEditor.addWidget(new TextFieldWidget(font, 5, 20, 40, 20, StringTextComponent.EMPTY));
        posX.setResponder(new SuggestionResponder("X pos", posX, this::posX_change));
        posX.setSuggestion("X pos");
        posY = keyframeEditor.addWidget(new TextFieldWidget(font, 50, 20, 40, 20, StringTextComponent.EMPTY));
        posY.setResponder(new SuggestionResponder("Y pos", posY, this::posY_change));
        posY.setSuggestion("Y pos");
        posZ = keyframeEditor.addWidget(new TextFieldWidget(font, 95, 20, 40, 20, StringTextComponent.EMPTY));
        posZ.setResponder(new SuggestionResponder("Z pos", posZ, this::posZ_change));
        posZ.setSuggestion("Z pos");
        scaleX = keyframeEditor.addWidget(new TextFieldWidget(font, 5, 45, 40, 20, StringTextComponent.EMPTY));
        scaleX.setResponder(new SuggestionResponder("X scl", scaleX, this::scaleX_change));
        scaleX.setSuggestion("X scl");
        scaleY = keyframeEditor.addWidget(new TextFieldWidget(font, 50, 45, 40, 20, StringTextComponent.EMPTY));
        scaleY.setResponder(new SuggestionResponder("Y scl", scaleY, this::scaleY_change));
        scaleY.setSuggestion("Y scl");
        scaleZ = keyframeEditor.addWidget(new TextFieldWidget(font, 95, 45, 40, 20, StringTextComponent.EMPTY));
        scaleZ.setResponder(new SuggestionResponder("Z scl", scaleZ, this::scaleZ_change));
        scaleZ.setSuggestion("Z scl");
        rotX = keyframeEditor.addWidget(new TextFieldWidget(font, 5, 70, 40, 20, StringTextComponent.EMPTY));
        rotX.setResponder(new SuggestionResponder("X rot", rotX, this::rotX_change));
        rotX.setSuggestion("X rot");
        rotY = keyframeEditor.addWidget(new TextFieldWidget(font, 50, 70, 40, 20, StringTextComponent.EMPTY));
        rotY.setResponder(new SuggestionResponder("Y rot", rotY, this::rotY_change));
        rotY.setSuggestion("Y rot");
        rotZ = keyframeEditor.addWidget(new TextFieldWidget(font, 95, 70, 40, 20, StringTextComponent.EMPTY));
        rotZ.setResponder(new SuggestionResponder("Z rot", rotZ, this::rotZ_change));
        rotZ.setSuggestion("Z rot");
        deg = keyframeEditor.addWidget(new TextFieldWidget(font, 95, 95, 40, 20, StringTextComponent.EMPTY));
        deg.setResponder(new SuggestionResponder("Deg", deg, this::degrees_change));
        deg.setSuggestion("Deg");
        end = keyframeEditor.addWidget(new TextFieldWidget(font, 5, 95, 40, 20, StringTextComponent.EMPTY));
        end.setResponder(new SuggestionResponder("Pos", end, this::endpoint_change));
        end.setSuggestion("Pos");
        // ---- TIMELINE
        timeline = addButton(new Timeline(0, height - 80, width, 80, () -> selectionContext));
        timeline.setProgressBarClickHandler(this::animationProgressBar_clicked);
        timeline.setKeyframeSelectHandler(this::keyframe_select);
        timeline.setEventClickHandler(this::event_clicked);
        IconButton.SETTINGS.set(TIMELINE_ICONS, 3, 16);
        addFrame = timeline.addWidget(new IconButton(5, 2, 16, 16, 0, this::buttonAddFrame_clicked, (btn, poses, mx, my) -> renderTooltip(poses, ADD_FRAME, mx, my)));
        removeFrame = timeline.addWidget(new IconButton(30, 2, 16, 16, 1, this::buttonRemoveFrame_clicked, (btn, poses, mx, my) -> renderTooltip(poses, REMOVE_FRAME, mx, my)));
        addEvent = timeline.addWidget(new IconButton(55, 2, 16, 16, 2, this::buttonAddEvent_clicked, (btn, poses, mx, my) -> renderTooltip(poses, ADD_EVENT, mx, my)));
        copyFrame = timeline.addWidget(new IconButton(80, 2, 16, 16, 3, this::buttonCopyFrame_clicked, (btn, poses, mx, my) -> renderTooltip(poses, COPY_FRAME, mx, my)));
        endFrames = timeline.addWidget(new IconButton(105, 2, 16, 16, 4, this::buttonEndFrames_clicked, (btn, poses, mx, my) -> renderTooltip(poses, END_FRAMES, mx, my)));
        progress2Frame = timeline.addWidget(new IconButton(130, 2, 16, 16, 5, this::buttonSetProgressToFrame_clicked, (btn, poses, mx, my) -> renderTooltip(poses, PROGRESS2FRAME, mx, my)));
        toBeginning = timeline.addWidget(new IconButton(155, 2, 16, 16, 6, this::resetToBeginning_clicked, (btn, poses, mx, my) -> renderTooltip(poses, SET2BEGINNING, mx, my)));
        toEnd = timeline.addWidget(new IconButton(180, 2, 16, 16, 7, this::setToEnd_clicked, (btn, poses, mx, my) -> renderTooltip(poses, SET2END, mx, my)));
        nextFrame = timeline.addWidget(new IconButton(205, 2, 16, 16, 8, this::setToNextFrame_clicked, (btn, poses, mx, my) -> renderTooltip(poses, NEXT_FRAME, mx, my)));
        if (!timeline.getProject().hasEvents()) {
            addEvent.active = false;
        }
        timeline.playAnimation();

        updateSelectionDependents(selectionContext != null);
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

    private void posX_change(String value) {
        if (tryValidate(value, posScaleDeg, posX)) {
            double x = Double.parseDouble(value);
            MutableKeyframe selectedFrame = selectionContext.frame();
            Vector3d oldpos = selectedFrame.position;
            selectedFrame.setPosition(new Vector3d(x, oldpos.y, oldpos.z));
        }
    }

    private void posY_change(String value) {
        if (tryValidate(value, posScaleDeg, posY)) {

        }
    }

    private void posZ_change(String value) {
        if (tryValidate(value, posScaleDeg, posZ)) {

        }
    }

    private void scaleX_change(String value) {
        if (tryValidate(value, posScaleDeg, scaleX)) {

        }
    }

    private void scaleY_change(String value) {
        if (tryValidate(value, posScaleDeg, scaleY)) {

        }
    }

    private void scaleZ_change(String value) {
        if (tryValidate(value, posScaleDeg, scaleZ)) {

        }
    }

    private void rotX_change(String value) {
        if (tryValidate(value, rotVec, rotX)) {

        }
    }

    private void rotY_change(String value) {
        if (tryValidate(value, rotVec, rotY)) {

        }
    }

    private void rotZ_change(String value) {
        if (tryValidate(value, rotVec, rotZ)) {

        }
    }

    private void degrees_change(String value) {
        if (tryValidate(value, posScaleDeg, deg)) {

        }
    }

    private void endpoint_change(String value) {
        if (tryValidate(value, endpoint, end)) {

        }
    }

    private boolean tryValidate(String value, Pattern pattern, TextFieldWidget widget) {
        if (pattern.matcher(value).matches()) {
            widget.setTextColor(0xE0E0E0);
            return true;
        } else {
            widget.setTextColor(0xE0 << 16);
            return false;
        }
    }

    private void buttonAddFrame_clicked(Button button) {
        // add frame dialog
        DialogScreen dialogScreen = new AddFrameDialog(this, timeline.getAnimationProgress(), this::addFrames);
        minecraft.setScreen(dialogScreen);
    }

    private void addFrames(float progress, List<AnimationStage> frameOwners) {
        AnimationStage lastStage = null;
        MutableKeyframe mutable = null;
        for (AnimationStage stage : frameOwners) {
            lastStage = stage;
            mutable = new MutableKeyframe();
            mutable.setEndpoint(progress);
            timeline.add(stage, mutable);
            timeline.init();
        }
        if (lastStage != null && mutable != null) {
            keyframe_select(Timeline.IKeyframeSelectContext.of(mutable, lastStage));
        }
    }

    private void buttonRemoveFrame_clicked(Button button) {
        // remove selected frame
    }

    private void buttonAddEvent_clicked(Button button) {
        // add event dialog
    }

    private void buttonCopyFrame_clicked(Button button) {
        // copy frame dialog
    }

    private void buttonEndFrames_clicked(Button button) {
        // fill end frames
    }

    private void buttonSetProgressToFrame_clicked(Button button) {
        // set progress to selected frame
        timeline.getAnimation().setProgress(selectionContext.frame().endpoint());
    }

    private void resetToBeginning_clicked(Button button) {
        // set progress to start
        timeline.getAnimation().setProgress(0.0F);
    }

    private void setToEnd_clicked(Button button) {
        // set progress to end
        timeline.getAnimation().setProgress(1.0F);
    }

    private void setToNextFrame_clicked(Button button) {
        // find next frame (or animation end)
        AnimationProject project = timeline.getProject();
        AnimatorFrameProvider provider = project.getFrameControl().getProvider();
        Map<AnimationStage, IKeyframe[]> frames = provider.getFrameMap();
        List<IKeyframe> list = frames.values().stream().flatMap(Arrays::stream).sorted(Comparator.comparingDouble(IKeyframe::endpoint)).collect(Collectors.toList());
        int selIndex = selectionContext != null ? list.indexOf(selectionContext.frame()) : -1;
        float value;
        if (selIndex == -1) {
            value = list.isEmpty() ? 0.0F : list.get(0).endpoint();
        } else {
            if (++selIndex >= list.size()) {
                value = 1.0F;
            } else {
                value = list.get(selIndex).endpoint();
            }
        }
        timeline.getAnimation().setProgress(value);
    }

    private void animationProgressBar_clicked(float value) {
        // adjust timeline progress
        timeline.getAnimation().setProgress(value);
    }

    private void keyframe_select(Timeline.IKeyframeSelectContext context) {
        selectionContext = context;
        updateSelectionDependents(selectionContext != null);
    }

    private void event_clicked(IAnimationEvent event) {

    }

    private void updateSelectionDependents(boolean hasFrame) {
        keyframeEditor.visible = hasFrame;
        removeFrame.active = hasFrame;
        copyFrame.active = hasFrame;
        progress2Frame.active = hasFrame;
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
        AnimationProject project = animator.getProject();
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
        return Animator.get().getProject().getAnimationControl().isPaused();
    }

    private void setPaused(boolean paused) {
        Animator.get().getProject().getAnimationControl().setPaused(paused);
    }
}

package lib.toma.animations.engine.screen.animator;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.IKeyframe;
import lib.toma.animations.api.event.IAnimationEvent;
import lib.toma.animations.engine.frame.MutableKeyframe;
import lib.toma.animations.engine.screen.animator.dialog.*;
import lib.toma.animations.engine.screen.animator.widget.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnimatorScreen extends Screen {

    public static final DecimalFormat TRANSFORM_FORMAT = new DecimalFormat("0.0##");
    public static final DecimalFormat POSITION_FORMAT = new DecimalFormat("0.0###");
    private static final ITextComponent NEW_PROJECT = new TranslationTextComponent("screen.animator.new_project");
    private static final ITextComponent OPEN = new TranslationTextComponent("screen.animator.open");
    private static final ITextComponent OPEN_FROM = new TranslationTextComponent("screen.animator.open_from");
    private static final ITextComponent MERGE = new TranslationTextComponent("screen.animator.merge");
    private static final ITextComponent SAVE = new TranslationTextComponent("screen.animator.save");
    private static final ITextComponent SAVE_AS = new TranslationTextComponent("screen.animator.save_as");
    private static final ITextComponent SETTINGS = new TranslationTextComponent("screen.animator.settings");
    private static final ITextComponent PAUSED = new TranslationTextComponent("screen.animator.paused");
    private static final ITextComponent ADD_FRAME = new TranslationTextComponent("screen.animator.timeline.add_frame");
    private static final ITextComponent ADD_EVENT = new TranslationTextComponent("screen.animator.timeline.add_event");
    private static final ITextComponent REMOVE_FRAME = new TranslationTextComponent("screen.animator.timeline.remove_frame");
    private static final ITextComponent COPY_FRAME = new TranslationTextComponent("screen.animator.timeline.copy_frame");
    private static final ITextComponent DUPLICATE_FRAME = new TranslationTextComponent("screen.animator.timeline.duplicate_frame");
    private static final ITextComponent PROGRESS2FRAME = new TranslationTextComponent("screen.animator.timeline.progress2frame");
    private static final ITextComponent SET2BEGINNING = new TranslationTextComponent("screen.animator.timeline.to_beginning");
    private static final ITextComponent SET2END = new TranslationTextComponent("screen.animator.timeline.to_end");
    private static final ITextComponent NEXT_FRAME = new TranslationTextComponent("screen.animator.timeline.next_frame");
    private static final ResourceLocation ANIMATOR_ICONS = new ResourceLocation("textures/icons/animator/animator_icons.png");
    private static final ResourceLocation TIMELINE_ICONS = new ResourceLocation("textures/icons/animator/timeline_icons.png");

    private final Pattern posScaleDeg = Pattern.compile("-?[0-9]+(\\.[0-9]*)?");
    private final Pattern rotVec = Pattern.compile("-?(1(\\.0)?)|-?(0(\\.[0-9]+)?)");
    private final Pattern endpoint = Pattern.compile("(1(\\.0)?)|(0(\\.[0-9]+)?)");

    @Nullable
    private Timeline.IKeyframeSelectContext selectionContext;

    private WidgetContainer keyframeEditor;
    private TextFieldWidget posX;
    private TextFieldWidget posY;
    private TextFieldWidget posZ;
    private TextFieldWidget rotX;
    private TextFieldWidget rotY;
    private TextFieldWidget rotZ;
    private TextFieldWidget deg;
    private TextFieldWidget end;

    private Timeline timeline;
    private IconButton removeFrame;
    private IconButton copyFrame;
    private IconButton duplicateFrame;
    private IconButton progress2Frame;

    public AnimatorScreen() {
        super(new TranslationTextComponent("screen.dev_tool.animator"));
    }

    @Override
    protected void init() {
        // ---- TOOLBAR (top)
        IconButton.SETTINGS.set(ANIMATOR_ICONS, 3, 16);
        addButton(new IconButton(5, 5, 20, 20, 0, this::buttonNewProject_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, NEW_PROJECT, mx, my)));
        addButton(new IconButton(30, 5, 20, 20, 1, this::buttonOpen_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, OPEN, mx, my)));
        addButton(new IconButton(55, 5, 20, 20, 4, this::buttonOpenFrom_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, OPEN_FROM, mx, my)));
        addButton(new IconButton(80, 5, 20, 20, 5, this::buttonMerge_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, MERGE, mx, my)));
        addButton(new IconButton(105, 5, 20, 20, 2, this::buttonSave_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, SAVE, mx, my)));
        addButton(new IconButton(130, 5, 20, 20, 2, this::buttonSaveAs_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, SAVE_AS, mx, my)));
        addButton(new IconButton(155, 5, 20, 20, 3, this::buttonSettings_Clicked, (btn, poses, mx, my) -> renderTooltip(poses, SETTINGS, mx, my)));
        addButton(new ControlButton(width - 70, 5, 65, 20, PAUSED, this::isPaused, this::setPaused));
        // ---- KEYFRAME INSPECTOR
        keyframeEditor = addButton(new WidgetContainer(0, height - 175, 140, 95));
        keyframeEditor.backgroundColor = 0x40 << 24;
        keyframeEditor.frameSize = 1;
        keyframeEditor.frameColor = 0x67 << 24;
        keyframeEditor.addWidget(new LabelWidget(5, 2, 130, 15, new StringTextComponent("Keyframe inspector"), font));
        posX = keyframeEditor.addWidget(new TextFieldWidget(font, 5, 20, 40, 20, StringTextComponent.EMPTY));
        posX.setResponder(new SuggestionResponder("X pos", posX, this::posX_change));
        posY = keyframeEditor.addWidget(new TextFieldWidget(font, 50, 20, 40, 20, StringTextComponent.EMPTY));
        posY.setResponder(new SuggestionResponder("Y pos", posY, this::posY_change));
        posZ = keyframeEditor.addWidget(new TextFieldWidget(font, 95, 20, 40, 20, StringTextComponent.EMPTY));
        posZ.setResponder(new SuggestionResponder("Z pos", posZ, this::posZ_change));
        rotX = keyframeEditor.addWidget(new TextFieldWidget(font, 5, 45, 40, 20, StringTextComponent.EMPTY));
        rotX.setResponder(new SuggestionResponder("X rot", rotX, this::rotX_change));
        rotY = keyframeEditor.addWidget(new TextFieldWidget(font, 50, 45, 40, 20, StringTextComponent.EMPTY));
        rotY.setResponder(new SuggestionResponder("Y rot", rotY, this::rotY_change));
        rotZ = keyframeEditor.addWidget(new TextFieldWidget(font, 95, 45, 40, 20, StringTextComponent.EMPTY));
        rotZ.setResponder(new SuggestionResponder("Z rot", rotZ, this::rotZ_change));
        deg = keyframeEditor.addWidget(new TextFieldWidget(font, 95, 70, 40, 20, StringTextComponent.EMPTY));
        deg.setResponder(new SuggestionResponder("Deg", deg, this::degrees_change));
        end = keyframeEditor.addWidget(new TextFieldWidget(font, 5, 70, 85, 20, StringTextComponent.EMPTY));
        end.setResponder(new SuggestionResponder("Pos", end, this::endpoint_change));
        // ---- TIMELINE
        timeline = addButton(new Timeline(0, height - 80, width, 80, () -> selectionContext));
        timeline.setProgressBarClickHandler(this::animationProgressBar_clicked);
        timeline.setKeyframeSelectHandler(this::keyframe_select);
        timeline.setEventClickHandler(this::event_clicked);
        IconButton.SETTINGS.set(TIMELINE_ICONS, 3, 16);
        timeline.addWidget(new IconButton(5, 2, 16, 16, 0, this::buttonAddFrame_clicked, (btn, poses, mx, my) -> renderTooltip(poses, ADD_FRAME, mx, my)));
        removeFrame = timeline.addWidget(new IconButton(30, 2, 16, 16, 1, this::buttonRemoveFrame_clicked, (btn, poses, mx, my) -> renderTooltip(poses, REMOVE_FRAME, mx, my)));
        IconButton addEvent = timeline.addWidget(new IconButton(55, 2, 16, 16, 2, this::buttonAddEvent_clicked, (btn, poses, mx, my) -> renderTooltip(poses, ADD_EVENT, mx, my)));
        copyFrame = timeline.addWidget(new IconButton(80, 2, 16, 16, 3, this::buttonCopyFrame_clicked, (btn, poses, mx, my) -> renderTooltip(poses, COPY_FRAME, mx, my)));
        duplicateFrame = timeline.addWidget(new IconButton(105, 2, 16, 16, 4, this::buttonDuplicateFrame_clicked, (btn, poses, mx, my) -> renderTooltip(poses, DUPLICATE_FRAME, mx, my)));
        progress2Frame = timeline.addWidget(new IconButton(130, 2, 16, 16, 5, this::buttonSetProgressToFrame_clicked, (btn, poses, mx, my) -> renderTooltip(poses, PROGRESS2FRAME, mx, my)));
        timeline.addWidget(new IconButton(155, 2, 16, 16, 6, this::resetToBeginning_clicked, (btn, poses, mx, my) -> renderTooltip(poses, SET2BEGINNING, mx, my)));
        timeline.addWidget(new IconButton(180, 2, 16, 16, 7, this::setToEnd_clicked, (btn, poses, mx, my) -> renderTooltip(poses, SET2END, mx, my)));
        timeline.addWidget(new IconButton(205, 2, 16, 16, 8, this::setToNextFrame_clicked, (btn, poses, mx, my) -> renderTooltip(poses, NEXT_FRAME, mx, my)));
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
            setPos(value, (old, x) -> new Vector3d(x, old.y, old.z));
        }
    }

    private void posY_change(String value) {
        if (tryValidate(value, posScaleDeg, posY)) {
            setPos(value, (old, y) -> new Vector3d(old.x, y, old.z));
        }
    }

    private void posZ_change(String value) {
        if (tryValidate(value, posScaleDeg, posZ)) {
            setPos(value, (old, z) -> new Vector3d(old.x, old.y, z));
        }
    }

    private void setPos(String value, BiFunction<Vector3d, Double, Vector3d> setter) {
        double x = Double.parseDouble(value);
        MutableKeyframe keyframe = selectionContext.frame();
        Vector3d old = keyframe.position;
        keyframe.setPosition(setter.apply(old, x));
        timeline.recompile(selectionContext.owner());
    }

    private void rotX_change(String value) {
        if (tryValidate(value, rotVec, rotX)) {
            setRotation(value, (pair, x) -> {
                Vector3f vec3f = pair.getRight();
                vec3f.setX(x);
                return new Quaternion(vec3f, pair.getLeft(), true);
            });
        }
    }

    private void rotY_change(String value) {
        if (tryValidate(value, rotVec, rotY)) {
            setRotation(value, (pair, y) -> {
                Vector3f vec3f = pair.getRight();
                vec3f.setY(y);
                return new Quaternion(vec3f, pair.getLeft(), true);
            });
        }
    }

    private void rotZ_change(String value) {
        if (tryValidate(value, rotVec, rotZ)) {
            setRotation(value, (pair, z) -> {
                Vector3f vec3f = pair.getRight();
                vec3f.setZ(z);
                return new Quaternion(vec3f, pair.getLeft(), true);
            });
        }
    }

    private void degrees_change(String value) {
        if (tryValidate(value, posScaleDeg, deg)) {
            setRotation(value, (pair, d) -> new Quaternion(pair.getRight(), d, true));
        }
    }

    private void setRotation(String value, BiFunction<Pair<Float, Vector3f>, Float, Quaternion> setter) {
        float f = Float.parseFloat(value);
        MutableKeyframe keyframe = selectionContext.frame();
        Pair<Float, Vector3f> rotationPair = AnimationUtils.getVectorWithRotation(keyframe.rotation);
        keyframe.setRotation(setter.apply(rotationPair, f));
        timeline.recompile(selectionContext.owner());
    }

    private void endpoint_change(String value) {
        if (tryValidate(value, endpoint, end)) {
            float val = Float.parseFloat(value);
            selectionContext.frame().setEndpoint(val);
            timeline.getProject().getFrameControl().getProvider().sort(selectionContext.owner());
            timeline.init();
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
        }
        timeline.init();
        if (lastStage != null && mutable != null) {
            keyframe_select(Timeline.IKeyframeSelectContext.of(mutable, lastStage));
        }
    }

    private void buttonRemoveFrame_clicked(Button button) {
        // remove selected frame
        timeline.remove(selectionContext.owner(), selectionContext.frame());
        timeline.init();
    }

    private void buttonAddEvent_clicked(Button button) {
        // add event dialog
        DialogScreen dialog = new AddEventDialog(this, timeline.getAnimationProgress(), timeline::add);
        minecraft.setScreen(dialog);
    }

    private void buttonCopyFrame_clicked(Button button) {
        // copy frame dialog
        DialogScreen dialog = new CopyFrameDialog(this, this::copyFrames, selectionContext.owner());
        minecraft.setScreen(dialog);
    }

    private void buttonDuplicateFrame_clicked(Button button) {
        // duplicate selected frame into the same timeline
        IKeyframe keyframe = selectionContext.frame();
        MutableKeyframe duplicated = MutableKeyframe.copyOf(keyframe);
        duplicated.setEndpoint(Math.min(keyframe.endpoint() + 0.05F, 1.0F));
        timeline.add(selectionContext.owner(), duplicated);
        timeline.init();
        keyframe_select(Timeline.IKeyframeSelectContext.of(duplicated, selectionContext.owner()));
    }

    private void copyFrames(List<AnimationStage> targets) {
        for (AnimationStage stage : targets) {
            MutableKeyframe kf = MutableKeyframe.copyOf(selectionContext.frame());
            timeline.add(stage, kf);
        }
        timeline.init();
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
        Map<AnimationStage, List<MutableKeyframe>> frames = provider.getFrames();
        float currentProgress = timeline.getAnimationProgress();
        if (currentProgress == 1.0F) {
            currentProgress = 0.0F;
        }
        List<Timeline.IKeyframeSelectContext> list = frames.entrySet().stream().flatMap(this::doMapping).sorted(Timeline.IKeyframeSelectContext::compareTo).collect(Collectors.toList());
        Timeline.IKeyframeSelectContext next = null;
        for (Timeline.IKeyframeSelectContext context : list) {
            if (context.frame().endpoint() > currentProgress) {
                next = context;
                break;
            }
        }
        float value = next != null ? next.frame().endpoint() : 1.0F;
        timeline.getAnimation().setProgress(value);
        if (next != null) {
            keyframe_select(next);
        }
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
        // event editor
        DialogScreen screen = new EditEventDialog(this, event, timeline::replace);
        minecraft.setScreen(screen);
    }

    private void updateSelectionDependents(boolean hasFrame) {
        keyframeEditor.visible = hasFrame;
        removeFrame.active = hasFrame;
        copyFrame.active = hasFrame;
        duplicateFrame.active = hasFrame && selectionContext.frame().endpoint() <= 0.95F;
        progress2Frame.active = hasFrame;
        if (hasFrame) {
            IKeyframe frame = selectionContext.frame();
            Vector3d position = frame.positionTarget();
            Pair<Float, Vector3f> rotation = AnimationUtils.getVectorWithRotation(frame.rotationTarget());
            Vector3f rotV = rotation.getRight();
            posX.setValue(TRANSFORM_FORMAT.format(position.x));
            posY.setValue(TRANSFORM_FORMAT.format(position.y));
            posZ.setValue(TRANSFORM_FORMAT.format(position.z));
            deg.setValue(TRANSFORM_FORMAT.format(rotation.getLeft()));
            rotX.setValue(TRANSFORM_FORMAT.format(rotV.x()));
            rotY.setValue(TRANSFORM_FORMAT.format(rotV.y()));
            rotZ.setValue(TRANSFORM_FORMAT.format(rotV.z()));
            end.setValue(POSITION_FORMAT.format(frame.endpoint()));
        }
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

    private void buttonOpenFrom_Clicked(Button button) {
        minecraft.setScreen(new ImportFromAnimationScreen(this));
    }

    private void buttonMerge_Clicked(Button button) {

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

    private Stream<Timeline.IKeyframeSelectContext> doMapping(Map.Entry<AnimationStage, List<MutableKeyframe>> entry) {
        List<Timeline.IKeyframeSelectContext> list = new ArrayList<>();
        for (MutableKeyframe frame : entry.getValue()) {
            list.add(Timeline.IKeyframeSelectContext.of(frame, entry.getKey()));
        }
        return list.stream();
    }

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ROOT);
        TRANSFORM_FORMAT.setDecimalFormatSymbols(symbols);
        POSITION_FORMAT.setDecimalFormatSymbols(symbols);
    }
}

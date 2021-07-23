package lib.toma.animations;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.HandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class HandRenderScreen extends Screen {

    private final IChangeCallback callback;
    private boolean enabled;
    private final RenderConfig.Mutable leftHand;
    private final RenderConfig.Mutable rightHand;
    private SelectorWidget<HandSide> handSelector;
    private TextFieldWidget posX;
    private TextFieldWidget posY;
    private TextFieldWidget posZ;
    private TextFieldWidget scaleX;
    private TextFieldWidget scaleY;
    private TextFieldWidget scaleZ;
    private TextFieldWidget degrees;
    private TextFieldWidget rotationX;
    private TextFieldWidget rotationY;
    private TextFieldWidget rotationZ;

    public HandRenderScreen(RenderConfig.Mutable left, RenderConfig.Mutable right, IChangeCallback callback, boolean isEnabled) {
        super(new StringTextComponent("screen.dev_tool.hand_config"));
        this.leftHand = left;
        this.rightHand = right;
        this.callback = callback;
        this.enabled = isEnabled;
    }

    @Override
    protected void init() {
        // to clipboard text
        Button toClipboard = new Button(5, 40, 50, 20, new StringTextComponent("Copy"), this::copy_Clicked);
        Button reset = new Button(5, 65, 50, 20, new StringTextComponent("Clear"), this::reset_Clicked);
        // on/off
        CheckboxWidget active = new CheckboxWidget(10, height - 30, 60, 20, new StringTextComponent("Active"), enabled).onClicked(this::checkbox_Change);
        // add hand selector
        handSelector = new SelectorWidget<>(5, 15, 50, 20, HandSide.values(), side -> new StringTextComponent(side.name())).onValueChanged(this::selector_OnChange);
        // position
        posX = new TextFieldWidget(font, 60, 15, 50, 20, StringTextComponent.EMPTY);
        posX.setResponder(this::posX_Changed);
        posY = new TextFieldWidget(font, 60, 40, 50, 20, StringTextComponent.EMPTY);
        posY.setResponder(this::posY_Changed);
        posZ = new TextFieldWidget(font, 60, 65, 50, 20, StringTextComponent.EMPTY);
        posZ.setResponder(this::posZ_Changed);
        // scale
        scaleX = new TextFieldWidget(font, 115, 15, 50, 20, StringTextComponent.EMPTY);
        scaleX.setResponder(this::scaleX_Changed);
        scaleY = new TextFieldWidget(font, 115, 40, 50, 20, StringTextComponent.EMPTY);
        scaleY.setResponder(this::scaleY_Changed);
        scaleZ = new TextFieldWidget(font, 115, 65, 50, 20, StringTextComponent.EMPTY);
        scaleZ.setResponder(this::scaleZ_Changed);
        // rotation
        degrees = new TextFieldWidget(font, 170, 15, 50, 20, StringTextComponent.EMPTY);
        degrees.setResponder(this::degrees_Changed);
        rotationX = new TextFieldWidget(font, 170, 40, 50, 20, StringTextComponent.EMPTY);
        rotationX.setResponder(this::rotationX_Changed);
        rotationY = new TextFieldWidget(font, 170, 65, 50, 20, StringTextComponent.EMPTY);
        rotationY.setResponder(this::rotationY_Changed);
        rotationZ = new TextFieldWidget(font, 170, 90, 50, 20, StringTextComponent.EMPTY);
        rotationZ.setResponder(this::rotationZ_Changed);

        selector_OnChange(HandSide.LEFT, HandSide.LEFT);
        addButton(toClipboard);
        addButton(reset);
        addButton(active);
        addButton(handSelector);
        addButton(posX);
        addButton(posY);
        addButton(posZ);
        addButton(scaleX);
        addButton(scaleY);
        addButton(scaleZ);
        addButton(degrees);
        addButton(rotationX);
        addButton(rotationY);
        addButton(rotationZ);
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float deltaRenderTime) {
        renderBackground(matrix);
        font.draw(matrix, "Handside", 5, 5, 0xFFFFFF);
        font.draw(matrix, "Pos", 60, 5, 0xFFFFFF);
        font.draw(matrix, "Scale", 115, 5, 0xFFFFFF);
        font.draw(matrix, "Rotation", 170, 5, 0xFFFFFF);
        super.render(matrix, mouseX, mouseY, deltaRenderTime);
    }

    private void posX_Changed(String value) {
        updatePos(value, posX, (value1, old) -> new Vector3d(value1, old.y, old.z));
    }

    private void posY_Changed(String value) {
        updatePos(value, posY, (value1, old) -> new Vector3d(old.x, value1, old.z));
    }

    private void posZ_Changed(String value) {
        updatePos(value, posZ, (value1, old) -> new Vector3d(old.x, old.y, value1));
    }

    private void scaleX_Changed(String value) {
        updateScale(value, scaleX, Vector3f::setX);
    }

    private void scaleY_Changed(String value) {
        updateScale(value, scaleY, Vector3f::setY);
    }

    private void scaleZ_Changed(String value) {
        updateScale(value, scaleZ, Vector3f::setZ);
    }

    private void degrees_Changed(String value) {
        updateRotation(value, degrees, (degrees1, x, y, z, val) -> new Vector3f(x, y, z).rotationDegrees(val));
    }

    private void rotationX_Changed(String value) {
        updateRotation(value, rotationX, (degrees1, x, y, z, val) -> new Vector3f(val, y, z).rotationDegrees(degrees1));
    }

    private void rotationY_Changed(String value) {
        updateRotation(value, rotationY, (degrees1, x, y, z, val) -> new Vector3f(x, val, z).rotationDegrees(degrees1));
    }

    private void rotationZ_Changed(String value) {
        updateRotation(value, rotationZ, (degrees1, x, y, z, val) -> new Vector3f(x, y, val).rotationDegrees(degrees1));
    }

    private void updatePos(String value, TextFieldWidget widget, PosSetFunction function) {
        RenderConfig.Mutable cfg = getActiveConfig();
        double val;
        try {
            val = Double.parseDouble(value);
            widget.setTextColor(0xE0E0E0);
            Vector3d old = cfg.pos;
            cfg.pos = function.getPos(val, old);
        } catch (NumberFormatException nfe) {
            widget.setTextColor(0xE0 << 16);
        }
    }

    private void updateScale(String value, TextFieldWidget widget, BiConsumer<Vector3f, Float> setter) {
        RenderConfig.Mutable cfg = getActiveConfig();
        float f;
        try {
            f = Float.parseFloat(value);
            widget.setTextColor(0xE0E0E0);
            setter.accept(cfg.scale, f);
        } catch (NumberFormatException nfe) {
            widget.setTextColor(0xE0 << 16);
        }
    }

    private void updateRotation(String value, TextFieldWidget widget, RotationSetFunction function) {
        RenderConfig.Mutable cfg = getActiveConfig();
        float f;
        try {
            f = Float.parseFloat(value);
            widget.setTextColor(0xE0E0E0);
            float d = getFloat(degrees.getValue());
            float x = getFloat(rotationX.getValue());
            float y = getFloat(rotationY.getValue());
            float z = getFloat(rotationZ.getValue());
            Quaternion quaternion = function.getRot(d, x, y, z, f);
            cfg.setRotation(quaternion);
        } catch (NumberFormatException nfe) {
            widget.setTextColor(0xE0 << 16);
        }
    }

    private float getFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException nfe) {
            return 0.0F;
        }
    }

    private void reset_Clicked(Button resetButton) {
        rightHand.toDefaults();
        leftHand.toDefaults();
        HandSide side = handSelector.getValue();
        selector_OnChange(side, side);
    }

    private void copy_Clicked(Button copyButton) {
        String left = getConfigDef(leftHand);
        String right = getConfigDef(rightHand);
        minecraft.keyboardHandler.setClipboard("//Hand render config\n//right\n" + right + "\n//left\n" + left);
    }

    private String getConfigDef(RenderConfig.Mutable cfg) {
        Vector3d position = cfg.pos;
        Vector3f scale = cfg.scale;
        Quaternion rotation = cfg.rotation;
        if (scale.equals(new Vector3f(1.0F, 1.0F, 1.0F)) && rotation.equals(Quaternion.ONE)) {
            // only position
            return String.format(Locale.ROOT, "new PositionConfig(new Vector3d(%.3f, %.3f, %.3f));", position.x, position.y, position.z);
        } else {
            // full config
            return String.format(Locale.ROOT, "RenderConfig.newDef().withPosition(%.3f, %.3f, %.3f).withScale(%.3fF, %.3fF, %.3fF).withRotation(new Quaternion(%.3fF, %.3fF, %.3fF, %.3fF)).finish();",
                    position.x, position.y, position.z, scale.x(), scale.y(), scale.z(), rotation.i(), rotation.j(), rotation.k(), rotation.r());
        }
    }

    private void checkbox_Change(boolean isChecked) {
        enabled = isChecked;
        callback.stateChanged(isChecked);
    }

    private void selector_OnChange(HandSide current, HandSide old) {
        RenderConfig.Mutable config = current == HandSide.RIGHT ? rightHand : leftHand;
        posX.setValue(String.valueOf(config.pos.x));
        posY.setValue(String.valueOf(config.pos.y));
        posZ.setValue(String.valueOf(config.pos.z));
        scaleX.setValue(String.valueOf(config.scale.x()));
        scaleY.setValue(String.valueOf(config.scale.y()));
        scaleZ.setValue(String.valueOf(config.scale.z()));
        Quaternion rotation = config.rotation;
        Pair<Float, Vector3f> transformed = AnimationUtils.getVectorWithRotation(rotation);
        float f = transformed.getLeft();
        Vector3f vec = transformed.getRight();
        degrees.setValue(String.format(Locale.ROOT, "%.2f", f));
        rotationX.setValue(String.format(Locale.ROOT, "%.2f", vec.x()));
        rotationY.setValue(String.format(Locale.ROOT, "%.2f", vec.y()));
        rotationZ.setValue(String.format(Locale.ROOT, "%.2f", vec.z()));
    }

    private RenderConfig.Mutable getActiveConfig() {
        return handSelector.getValue() == HandSide.RIGHT ? rightHand : leftHand;
    }

    public interface IChangeCallback {
        void stateChanged(boolean active);
    }

    public static class SelectorWidget<T> extends Widget {

        private final T[] values;
        private final Function<T, ITextComponent> translator;
        private int currentIndex;
        private IValueChanged<T> valueChanged;

        public SelectorWidget(int x, int y, int width, int height, T[] values, Function<T, ITextComponent> translator) {
            super(x, y, width, height, StringTextComponent.EMPTY);
            this.values = values;
            this.translator = translator;
            setMessage(getDisplayName());
        }

        public SelectorWidget<T> onValueChanged(IValueChanged<T> callback) {
            valueChanged = callback;
            return this;
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            T old = getValue();
            int nextIndex = currentIndex + 1;
            if (nextIndex >= values.length) {
                nextIndex = 0;
            }
            currentIndex = nextIndex;
            T next = getValue();
            setMessage(translator.apply(next));
            if (valueChanged != null)
                valueChanged.valueChanged(next, old);
        }

        public ITextComponent getDisplayName() {
            return translator.apply(getValue());
        }

        public T getValue() {
            return values[currentIndex];
        }

        public interface IValueChanged<T> {
            void valueChanged(T current, T old);
        }
    }

    public static class CheckboxWidget extends Widget {

        private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/checkbox.png");
        private boolean checked;
        private ISelectionChanged callback;

        public CheckboxWidget(int x, int y, int width, int height, ITextComponent text, boolean initialState) {
            super(x, y, width, height, text);
            checked = initialState;
        }

        public CheckboxWidget onClicked(ISelectionChanged event) {
            callback = event;
            return this;
        }

        @Override
        public void onClick(double p_230982_1_, double p_230982_3_) {
            checked = !checked;
            if (callback != null)
                callback.onChange(checked);
        }

        @Override
        public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
            Minecraft minecraft = Minecraft.getInstance();
            minecraft.getTextureManager().bind(TEXTURE);
            RenderSystem.enableDepthTest();
            FontRenderer fontrenderer = minecraft.font;
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            blit(p_230431_1_, this.x, this.y, this.isFocused() ? 20.0F : 0.0F, this.checked ? 20.0F : 0.0F, 20, this.height, 64, 64);
            this.renderBg(p_230431_1_, minecraft, p_230431_2_, p_230431_3_);
            if (this.getMessage() != null) {
                drawString(p_230431_1_, fontrenderer, this.getMessage(), this.x + 24, this.y + (this.height - 8) / 2, 14737632 | MathHelper.ceil(this.alpha * 255.0F) << 24);
            }

        }

        public interface ISelectionChanged {
            void onChange(boolean isChecked);
        }
    }

    private interface PosSetFunction {
        Vector3d getPos(double value, Vector3d old);
    }

    private interface RotationSetFunction {
        Quaternion getRot(float degrees, float x, float y, float z, float val);
    }
}

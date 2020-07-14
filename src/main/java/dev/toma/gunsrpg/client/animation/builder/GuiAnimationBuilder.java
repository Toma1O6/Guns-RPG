package dev.toma.gunsrpg.client.animation.builder;

import dev.toma.gunsrpg.client.animation.AnimationManager;
import dev.toma.gunsrpg.client.animation.builder.component.*;
import dev.toma.gunsrpg.client.animation.builder.export.BuiltAnimationExporter;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.object.OptionalObject;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiAnimationBuilder extends GuiScreen {

    private int index = 0;
    public final OptionalObject<InputComponent> selectedInput = OptionalObject.empty();
    int center;
    int entryCount;

    private final List<UIComponent> componentList = new ArrayList<>();

    @Override
    public void initGui() {
        componentList.clear();
        center = width / 2;
        entryCount = (height - 70) / 35;
        if(entryCount == 0) {
            addComponent(new PlainTextComponent(10, 70, TextFormatting.RED + "Screen is too small to display animation data"));
        }
        addComponent(new PlainTextComponent(10, 3, TextFormatting.BOLD + "General"));
        addComponent(new PressableComponent(10, 20, 75, 20, "Play Animation", ac -> AnimationManager.sendNewAnimation(-1, BuilderData.asAnimation())));
        addComponent(new PressableComponent(95, 20, 75, 20, "Export as .txt", ac -> BuiltAnimationExporter.exportAnimation()));
        addComponent(new PlainTextComponent(10, 45, "Animation length:"));
        PlainTextComponent textComponent = new PlainTextComponent(95, 45, TextFormatting.BOLD.toString() + BuilderData.animationLength + TextFormatting.RESET + " ticks");
        addComponent(new PressableComponent(145, 45, 20, 20, "-", ac -> {
            BuilderData.animationLength = Math.max(1, GuiScreen.isShiftKeyDown() ? BuilderData.animationLength - 10 : BuilderData.animationLength - 1);
            textComponent.setName(TextFormatting.BOLD.toString() + BuilderData.animationLength + TextFormatting.RESET + " ticks");
        }));
        addComponent(new PressableComponent(170, 45, 20, 20, "+", ac -> {
            BuilderData.animationLength = isShiftKeyDown() ? BuilderData.animationLength + 10 : BuilderData.animationLength + 1;
            textComponent.setName(TextFormatting.BOLD.toString() + BuilderData.animationLength + TextFormatting.RESET + " ticks");
        }));
        addComponent(textComponent);
        for(int i = index; i < index + entryCount; i++) {
            int id = i - index;
            if(i >= BuilderData.steps.size()) break;
            BuilderAnimationStep step = BuilderData.steps.get(i);
            addComponent(new AnimationStepComponent(this, i, 10, 70 + id * 35, center - 30, 30, step));
        }
        addComponent(new PlainTextComponent(center + 10, 3, TextFormatting.BOLD + "Animation control"));
        addComponent(new PlainTextComponent(center + 10, 20, "Context"));
        addComponent(new UIComponent(center + 70, 20, 80, 20, "Translate", ac -> {
            BuilderData.context = BuilderData.Context.TRANSLATION;
            initGui();
        }).setState(BuilderData.context == BuilderData.Context.TRANSLATION));
        addComponent(new UIComponent(center + 160, 20, 80, 20, "Rotate", ac -> {
            BuilderData.context = BuilderData.Context.ROTATION;
            initGui();
        }).setState(BuilderData.context == BuilderData.Context.ROTATION));
        addComponent(new PlainTextComponent(center + 10, 45, "Axis"));
        addComponent(new UIComponent(center + 40, 45, 40, 20, "X", ac -> {
            BuilderData.axis = BuilderData.Axis.X;
            initGui();
        }).setState(BuilderData.axis == BuilderData.Axis.X));
        addComponent(new UIComponent(center + 90, 45, 40, 20, "Y", ac -> {
            BuilderData.axis = BuilderData.Axis.Y;
            initGui();
        }).setState(BuilderData.axis == BuilderData.Axis.Y));
        addComponent(new UIComponent(center + 140, 45, 40, 20, "Z", ac -> {
            BuilderData.axis = BuilderData.Axis.Z;
            initGui();
        }).setState(BuilderData.axis == BuilderData.Axis.Z));
        addComponent(new PlainTextComponent(center + 10, 70, "Part"));
        addComponent(new UIComponent(center + 40, 70, 80, 20, "Item and hands", ac -> {
            BuilderData.part = BuilderData.Part.ITEM_AND_HANDS;
            initGui();
        }).setState(BuilderData.part == BuilderData.Part.ITEM_AND_HANDS));
        addComponent(new UIComponent(center + 40, 95, 80, 20, "Hands", ac -> {
            BuilderData.part = BuilderData.Part.HANDS;
            initGui();
        }).setState(BuilderData.part == BuilderData.Part.HANDS));
        addComponent(new UIComponent(center + 40, 120, 80, 20, "Right hand", ac -> {
            BuilderData.part = BuilderData.Part.RIGHT_HAND;
            initGui();
        }).setState(BuilderData.part == BuilderData.Part.RIGHT_HAND));
        addComponent(new UIComponent(center + 40, 145, 80, 20, "Left hand", ac -> {
            BuilderData.part = BuilderData.Part.LEFT_HAND;
            initGui();
        }).setState(BuilderData.part == BuilderData.Part.LEFT_HAND));
        addComponent(new UIComponent(center + 40, 170, 80, 20, "Item", ac -> {
            BuilderData.part = BuilderData.Part.ITEM;
            initGui();
        }).setState(BuilderData.part == BuilderData.Part.ITEM));
    }

    @Override
    public void handleMouseInput() throws IOException {
        int i = -Integer.signum(Mouse.getEventDWheel());
        if(i != 0 && index + i >= 0 && index + i < BuilderData.steps.size()) {
            index += i;
            initGui();
        }
        super.handleMouseInput();
    }

    public void addComponent(UIComponent component) {
        componentList.add(component);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ModUtils.renderColor(0, 0, width, height, 0.0F, 0.0F, 0.0F, 0.4F);
        ModUtils.renderLine(center, 0, center, height, 1.0F, 1.0F, 1.0F, 1.0F, 3);
        componentList.forEach(c -> c.draw(mc.fontRenderer));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        selectedInput.ifPresent(cp -> cp.keyPressed(typedChar, keyCode));
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton != 0) return;
        selectedInput.clear();
        for(UIComponent component : componentList) {
            if(component.isMouseOver(mouseX, mouseY)) {
                if(component instanceof InputComponent) {
                    InputComponent inputComponent = (InputComponent) component;
                    if(inputComponent == selectedInput.get()) selectedInput.clear();
                    else selectedInput.map(inputComponent);
                }
                component.onClick();
                mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 0.75F, 1.0F);
                break;
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}

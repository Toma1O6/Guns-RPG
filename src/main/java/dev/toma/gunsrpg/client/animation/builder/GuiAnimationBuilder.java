package dev.toma.gunsrpg.client.animation.builder;

import dev.toma.gunsrpg.client.animation.Animation;
import dev.toma.gunsrpg.client.animation.AnimationManager;
import dev.toma.gunsrpg.client.animation.builder.component.InputComponent;
import dev.toma.gunsrpg.client.animation.builder.component.PlainTextComponent;
import dev.toma.gunsrpg.client.animation.builder.component.UIComponent;
import dev.toma.gunsrpg.client.animation.builder.export.BuiltAnimationExporter;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.object.LazyLoader;
import dev.toma.gunsrpg.util.object.OptionalObject;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.init.SoundEvents;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiAnimationBuilder extends GuiScreen {

    private final int xSize = 200;
    private final int ySize = 160;
    private int guiLeft;
    private int guiTop;
    private int index = 0;
    public final OptionalObject<InputComponent> selectedInput = OptionalObject.empty();

    private boolean displayOverview = true;
    private final List<UIComponent> componentList = new ArrayList<>();
    private final LazyLoader<UIComponent> overview = new LazyLoader<>(() -> new UIComponent(guiLeft + 10, guiTop + 10, 85, 20, "Overview", b -> {
        displayOverview = true;
        b.setState(true);
        GuiAnimationBuilder.this.settings.get().setState(false);
        initGui();
    }));
    private final LazyLoader<UIComponent> settings = new LazyLoader<>(() -> new UIComponent(guiLeft + 105, guiTop + 10, 85, 20, "Animation", b -> {
        displayOverview = false;
        b.setState(true);
        GuiAnimationBuilder.this.overview.get().setState(false);
        initGui();
    }).setState(true));

    @Override
    public void initGui() {
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
        componentList.clear();
        this.addComponent(overview.get());
        this.addComponent(settings.get());

        if(settings.get().enabled) {
            this.addComponent(new PlainTextComponent(guiLeft + 10, guiTop + 30, "Context"));
            this.addComponent(new UIComponent(guiLeft + 10, guiTop + 50, 85, 20, "Translation", b -> {
                BuilderData.context = BuilderData.Context.TRANSLATION;
                initGui();
            }).setState(BuilderData.context == BuilderData.Context.TRANSLATION));
            this.addComponent(new UIComponent(guiLeft + 105, guiTop + 50, 85, 20, "Rotation", b -> {
                BuilderData.context = BuilderData.Context.ROTATION;
                initGui();
            }).setState(BuilderData.context == BuilderData.Context.ROTATION));
            this.addComponent(new PlainTextComponent(guiLeft + 10, guiTop + 75, "Axis"));
            this.addComponent(new UIComponent(guiLeft + 50, guiTop + 75, 40, 20, "X", b -> {
                BuilderData.axis = BuilderData.Axis.X;
                initGui();
            }).setState(BuilderData.axis == BuilderData.Axis.X));
            this.addComponent(new UIComponent(guiLeft + 100, guiTop + 75, 40, 20, "Y", b -> {
                BuilderData.axis = BuilderData.Axis.Y;
                initGui();
            }).setState(BuilderData.axis == BuilderData.Axis.Y));
            this.addComponent(new UIComponent(guiLeft + 150, guiTop + 75, 40, 20, "Z", b -> {
                BuilderData.axis = BuilderData.Axis.Z;
                initGui();
            }).setState(BuilderData.axis == BuilderData.Axis.Z));
            this.addComponent(new PlainTextComponent(guiLeft + 10, guiTop + 100, "Part"));
            this.addComponent(new UIComponent(guiLeft + 40, guiTop + 100, 70, 20, "Item & hands", b -> {
                BuilderData.part = BuilderData.Part.ITEM_AND_HANDS;
                initGui();
            }).setState(BuilderData.part == BuilderData.Part.ITEM_AND_HANDS));
            this.addComponent(new UIComponent(guiLeft + 120, guiTop + 100, 70, 20, "Right hand", b -> {
                BuilderData.part = BuilderData.Part.RIGHT_HAND;
                initGui();
            }).setState(BuilderData.part == BuilderData.Part.RIGHT_HAND));
            this.addComponent(new UIComponent(guiLeft + 10, guiTop + 125, 60, 20, "Left hand", b -> {
                BuilderData.part = BuilderData.Part.LEFT_HAND;
                initGui();
            }).setState(BuilderData.part == BuilderData.Part.LEFT_HAND));
            this.addComponent(new UIComponent(guiLeft + 80, guiTop + 125, 50, 20, "Hands", b -> {
                BuilderData.part = BuilderData.Part.HANDS;
                initGui();
            }).setState(BuilderData.part == BuilderData.Part.HANDS));
            this.addComponent(new UIComponent(guiLeft + 140, guiTop + 125, 50, 20, "Item", b -> {
                BuilderData.part = BuilderData.Part.ITEM;
                initGui();
            }).setState(BuilderData.part == BuilderData.Part.ITEM));
        } else if(overview.get().enabled) {
            this.addComponent(new UIComponent(guiLeft + 10, guiTop + 35, 85, 20, "Play animation", b -> {
                b.setState(false);
                AnimationManager.sendNewAnimation(-1, BuilderData.asAnimation());
            }));
            this.addComponent(new UIComponent(guiLeft + 105, guiTop + 35, 85, 20, "Export", b -> {
                b.setState(false);
                BuiltAnimationExporter.exportAnimation();
                initGui();
            }));
            this.addComponent(new PlainTextComponent(guiLeft + 10, guiTop + 60, "Length:"));
            PlainTextComponent textComponent1 = new PlainTextComponent(guiLeft + 60, guiTop + 60, BuilderData.animationLength + "");
            this.addComponent(new UIComponent(guiLeft + 100, guiTop + 60, 20, 20, "-", b -> {
                BuilderData.animationLength = Math.max(1, GuiScreen.isShiftKeyDown() ? BuilderData.animationLength - 10 : BuilderData.animationLength - 1);
                textComponent1.setName("" + BuilderData.animationLength);
                b.setState(false);
            }));
            this.addComponent(new UIComponent(guiLeft + 130, guiTop + 60, 20, 20, "+", b -> {
                BuilderData.animationLength = GuiScreen.isShiftKeyDown() ? BuilderData.animationLength + 10 : BuilderData.animationLength + 1;
                textComponent1.setName(BuilderData.animationLength + "");
                b.setState(false);
            }));
            this.addComponent(textComponent1);
            for(int i = index; i < index + 3; i++) {
                int id = i - index;
                if(i >= BuilderData.steps.size()) break;
                BuilderAnimationStep step = BuilderData.steps.get(i);
                this.addComponent(new InputComponent(this, i, guiLeft + 10, guiTop + 85 + id * 25, 100, 20, step.getIntValue()));
                this.addComponent(new UIComponent(guiLeft + 120, guiTop + 85 + id * 25, 20, 20, "P", b -> {
                    b.setState(false);
                    Animation animation = step.asTempAnimation();
                    if(animation == null) return;
                    AnimationManager.sendNewAnimation(-1, animation);
                }));
                this.addComponent(new UIComponent(guiLeft + 145, guiTop + 85 + id * 25, 20, 20, "-", b -> {
                    b.setState(false);
                    BuilderData.steps.remove(id + index);
                    if(BuilderData.steps.isEmpty()) {
                        BuilderData.current = new BuilderAnimationStep();
                        BuilderData.steps.add(BuilderData.current);
                    } else {
                        BuilderData.current = BuilderData.steps.get(BuilderData.steps.size() - 1);
                    }
                    initGui();
                }));
                this.addComponent(new UIComponent(guiLeft + 170, guiTop + 85 + id * 25, 20, 20, "+", b -> {
                    b.setState(false);
                    BuilderAnimationStep next = new BuilderAnimationStep(BuilderData.current);
                    BuilderData.steps.add(next);
                    BuilderData.current = next;
                    initGui();
                }));
            }
        }
    }

    public void addComponent(UIComponent component) {
        componentList.add(component);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        ModUtils.renderColor(guiLeft, guiTop, guiLeft + xSize, guiTop + ySize, 0.0F, 0.0F, 0.0F, 0.75F);
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

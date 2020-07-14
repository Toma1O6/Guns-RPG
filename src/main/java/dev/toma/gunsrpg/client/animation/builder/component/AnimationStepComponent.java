package dev.toma.gunsrpg.client.animation.builder.component;

import dev.toma.gunsrpg.client.animation.Animation;
import dev.toma.gunsrpg.client.animation.AnimationManager;
import dev.toma.gunsrpg.client.animation.builder.BuilderAnimationStep;
import dev.toma.gunsrpg.client.animation.builder.BuilderData;
import dev.toma.gunsrpg.client.animation.builder.GuiAnimationBuilder;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class AnimationStepComponent extends UIComponent {

    private final int id;
    private final GuiAnimationBuilder screen;
    private boolean selected;

    public AnimationStepComponent(GuiAnimationBuilder screen, int ID, int x, int y, int width, int height, BuilderAnimationStep step) {
        super(x, y, width, height);
        this.screen = screen;
        this.id = ID;
        screen.addComponent(new PlainTextComponent(x + 5, y + 5, ID + ""));
        screen.addComponent(new InputComponent(screen, ID, x + 20, y + 5, 50, 20, step.getIntValue()));
        screen.addComponent(new PressableComponent(x + 75, y + 5, 40, 20, "Play", ac -> {
            Animation animation = step.asTempAnimation();
            if(animation == null) {
                Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + "Unable to create animation!"));
                return;
            }
            AnimationManager.sendNewAnimation(-1, animation);
        }));
        screen.addComponent(new PressableComponent(x + 120, y + 5, 40, 20, "Add", ac -> {
            BuilderAnimationStep newStep = new BuilderAnimationStep(step);
            BuilderData.steps.add(ID + 1, newStep);
            BuilderData.current = newStep;
            screen.initGui();
        }));
        screen.addComponent(new PressableComponent(x + 165, y + 5, 40, 20, "Remove", ac -> {
            BuilderData.steps.remove(ID);
            if(BuilderData.steps.isEmpty()) {
                BuilderAnimationStep step1 = new BuilderAnimationStep();
                BuilderData.steps.add(step1);
                BuilderData.current = step1;
            } else if(BuilderData.current == null) {
                BuilderData.current = BuilderData.steps.get(BuilderData.steps.size() - 1);
            }
            screen.initGui();
        }));
        this.selected = BuilderData.current == step;
    }

    @Override
    public void onClick() {
        if(!selected) {
            if(id < BuilderData.steps.size()) {
                BuilderData.current = BuilderData.steps.get(id);
                screen.initGui();
            }
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void draw(FontRenderer renderer) {
        if(selected) {
            ModUtils.renderLine(x, y, x + w, y, 0.0F, 0.8F, 0.0F, 1.0F, 4);
            ModUtils.renderLine(x, y, x, y + h, 0.0F, 0.8F, 0.0F, 1.0F, 4);
            ModUtils.renderLine(x + w, y, x + w, y + h, 0.0F, 0.8F, 0.0F, 1.0F, 4);
            ModUtils.renderLine(x, y + h, x + w, y + h, 0.0F, 0.8F, 0.0F, 1.0F, 4);
        } else {
            ModUtils.renderLine(x, y, x + w, y, 1.0F, 1.0F, 1.0F, 1.0F, 4);
            ModUtils.renderLine(x, y, x, y + h, 1.0F, 1.0F, 1.0F, 1.0F, 4);
            ModUtils.renderLine(x + w, y, x + w, y + h, 1.0F, 1.0F, 1.0F, 1.0F, 4);
            ModUtils.renderLine(x, y + h, x + w, y + h, 1.0F, 1.0F, 1.0F, 1.0F, 4);
        }
    }
}

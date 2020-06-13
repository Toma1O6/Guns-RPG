package dev.toma.gunsrpg.client.animation.builder;

import dev.toma.gunsrpg.client.animation.AnimationManager;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.object.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.text.DecimalFormat;
import java.util.Map;

public class BuilderMain {

    public static KeyBinding openToolUI;
    public static KeyBinding add;
    public static KeyBinding subtract;
    public static KeyBinding activate;
    public static KeyBinding reset;

    public static void init() {
        if(!GRPGConfig.client.loadAnimationTool) return;
        MinecraftForge.EVENT_BUS.register(new Handler());
        openToolUI = register("open_ui", Keyboard.KEY_M);
        add = register("add", Keyboard.KEY_UP);
        subtract = register("subtract", Keyboard.KEY_DOWN);
        activate = register("activate", Keyboard.KEY_N);
        reset = register("reset", Keyboard.KEY_X);
    }

    private static KeyBinding register(String name, int key) {
        KeyBinding keyBinding = new KeyBinding("grpg.key." + name, key, "grpg.category.animation");
        ClientRegistry.registerKeyBinding(keyBinding);
        return keyBinding;
    }

    public static class Handler {
        @SubscribeEvent
        public void onInput(InputEvent.KeyInputEvent event) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            if(openToolUI.isPressed()) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiAnimationBuilder());
            } else if(add.isPressed()) {
                BuilderData.add(player.isSneaking());
            } else if(subtract.isPressed()) {
                BuilderData.subtract(player.isSneaking());
            } else if(activate.isPressed()) {
                boolean b = AnimationManager.getAnimationByID(-1) != null;
                if(b) AnimationManager.cancelAnimation(-1);
                else {
                    BuilderData.current.setProgress(1.0F);
                    AnimationManager.sendNewAnimation(-1, BuilderData.current);
                }
            } else if(reset.isPressed()) {
                BuilderData.resetToDefaultState();
                AnimationManager.sendNewAnimation(-1, BuilderData.current);
            }
        }

        @SubscribeEvent
        public void renderScreenOverlay(RenderGameOverlayEvent.Post event) {
            if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
                FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
                BuilderAnimationStep step = BuilderData.current;
                int totalLength = BuilderData.animationLength;
                renderer.drawStringWithShadow("Animation render: " + (AnimationManager.getAnimationByID(-1) != null ? TextFormatting.GREEN + "Active" : TextFormatting.RED + "Inactive"), 10, 10, 0xffffff);
                renderer.drawStringWithShadow("Animation steps: " + TextFormatting.YELLOW + BuilderData.steps.size(), 10, 20, 0xffffff);
                renderer.drawStringWithShadow(String.format("Animation length: %s ticks", TextFormatting.YELLOW.toString() + totalLength), 10, 30, 0xffffff);
                renderer.drawStringWithShadow("Context: " + TextFormatting.AQUA + BuilderData.context.name(), 10, 40, 0xffffff);
                renderer.drawStringWithShadow("Axis: " + TextFormatting.AQUA + BuilderData.axis.name(), 10, 50, 0xffffff);
                renderer.drawStringWithShadow("Part: " + TextFormatting.AQUA + BuilderData.part.name(), 10, 60, 0xffffff);
                renderer.drawStringWithShadow(TextFormatting.BOLD + "Current animation", 10, 75, 0xffffff);
                renderer.drawStringWithShadow("Length: " + TextFormatting.YELLOW.toString() + (int)(totalLength * step.length), 10, 85, 0xffffff);
                int j = 0;
                DecimalFormat df = new DecimalFormat("###.##");
                for(BuilderData.Part part : BuilderData.Part.values()) {
                    BuilderAnimationStep.Data data = step.map.get(part);
                    if(data.isEmpty()) continue;
                    renderer.drawStringWithShadow(TextFormatting.UNDERLINE + part.name(), 10, 95 + j * 10, 0xffffff);
                    BuilderAnimationStep.TranslationContext ctx0 = data.translationContext;
                    if(!ctx0.isEmpty()) {
                        ++j;
                        renderer.drawStringWithShadow(String.format(TextFormatting.YELLOW + "Move" + TextFormatting.WHITE + ":[%s+%s, %s+%s, %s+%s]", TextFormatting.RED + df.format(ctx0.baseX) + TextFormatting.WHITE, TextFormatting.GREEN + df.format(ctx0.newX) + TextFormatting.WHITE, TextFormatting.RED + df.format(ctx0.baseY) + TextFormatting.WHITE, TextFormatting.GREEN + df.format(ctx0.newY) + TextFormatting.WHITE, TextFormatting.RED + df.format(ctx0.baseZ) + TextFormatting.WHITE, TextFormatting.GREEN + df.format(ctx0.newZ) + TextFormatting.WHITE), 10, 95 + j * 10, 0xffffff);
                    }
                    BuilderAnimationStep.RotationContext ctx = data.rotationContext;
                    if(!ctx.isEmpty()) {
                        ++j;
                        StringBuilder builder = new StringBuilder();
                        builder.append(TextFormatting.YELLOW).append("Rotate").append(TextFormatting.WHITE).append(":[");
                        for(Map.Entry<BuilderData.Axis, Pair<Float, Float>> entry : ctx.rotations.entrySet()) {
                            Pair<Float, Float> pair = entry.getValue();
                            builder.append(TextFormatting.AQUA).append(entry.getKey().name()).append(TextFormatting.WHITE).append(":").append(TextFormatting.RED).append(df.format(pair.getLeft())).append(TextFormatting.WHITE).append("+").append(TextFormatting.GREEN).append(df.format(pair.getRight())).append(TextFormatting.WHITE).append(";");
                        }
                        builder.append("]");
                        renderer.drawStringWithShadow(builder.toString(), 10, 95 + j * 10, 0xffffff);
                    }
                    ++j;
                }
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, -1);
                ModUtils.renderColor(0, 0, 170, 95 + j * 10 + 10, 0.0F, 0.0F, 0.0F, 0.4F);
                GlStateManager.popMatrix();
            }
        }
    }
}

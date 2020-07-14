package dev.toma.gunsrpg.client.animation.builder.export;

import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.client.animation.builder.BuilderAnimationStep;
import dev.toma.gunsrpg.client.animation.builder.BuilderData;
import dev.toma.gunsrpg.util.object.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class BuiltAnimationExporter {

    public static void exportAnimation() {
        LocalDateTime time = LocalDateTime.now();
        String fileName = "animation_" + normal(time.getDayOfMonth()) + normal(time.getMonthValue()) + time.getYear() + "_" + normal(time.getHour()) + normal(time.getMinute());
        File file = findSuitableNameFor(fileName);
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(export());
            writer.close();
            TextComponentString textComponent = new TextComponentString("File has been created, path: " + file.getCanonicalPath());
            textComponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getParentFile().getCanonicalPath()));
            Minecraft.getMinecraft().player.sendMessage(textComponent);
        } catch (IOException e) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + "Could not create file: " + e.toString()));
            e.printStackTrace();
        } catch (JsonParseException e) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + "Error serializing animation: " + e.toString()));
            e.printStackTrace();
        } catch (Exception e) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + "Unknown error: " + e.toString()));
            e.printStackTrace();
        }
    }

    private static String export() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("###.##", symbols);
        StringBuilder builder = new StringBuilder();
        float first = 0.0F;
        for(BuilderAnimationStep step : BuilderData.steps) {
            float last = first + step.length;
            builder.append("addStep(").append(df.format(first)).append("F, ").append(df.format(last)).append("F, SimpleAnimation.newSimpleAnimation()");
            first = last;
            for(BuilderData.Part part : BuilderData.Part.values()) {
                BuilderAnimationStep.Data data = step.map.get(part);
                if(data.isEmpty()) continue;
                builder.append(getFunctionName(part)).append(generateAnimation(data, df)).append("})");
            }
            builder.append(".create());\n");
        }
        if(first > 1.0F) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Animation step range is over 100%! Your animation will be cut out. Got " + df.format(first)));
        } else if(first < 1.0F) {
            Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "Animation step range is not equal 100%! This will mess up your animation. Got " + df.format(first)));
        }
        return builder.toString();
    }

    private static String generateAnimation(BuilderAnimationStep.Data data, DecimalFormat df) {
        StringBuilder builder = new StringBuilder();
        BuilderAnimationStep.TranslationContext tctx = data.translationContext;
        if(!tctx.isEmpty()) {
            builder.append("GlStateManager.translate(");
            handleTranslationValue(builder, df, tctx.baseX, tctx.newX, true);
            handleTranslationValue(builder, df, tctx.baseY, tctx.newY, true);
            handleTranslationValue(builder, df, tctx.baseZ, tctx.newZ, false);
            builder.append(");");
        }
        BuilderAnimationStep.RotationContext ctx = data.rotationContext;
        if(!ctx.isEmpty()) {
            List<Map.Entry<BuilderData.Axis, Pair<Float, Float>>> list = new ArrayList<>(ctx.rotations.entrySet());
            list.sort(Comparator.comparingInt(o -> o.getKey().getIndex()));
            for(Map.Entry<BuilderData.Axis, Pair<Float, Float>> entry : list) {
                builder.append("GlStateManager.rotate(");
                Pair<Float, Float> sdP = entry.getValue();
                handleTranslationValue(builder, df, sdP.getLeft(), sdP.getRight(), true);
                switch (entry.getKey()) {
                    case X: builder.append("1.0F, 0.0F, 0.0F);"); break;
                    case Y: builder.append("0.0F, 1.0F, 0.0F);"); break;
                    case Z: builder.append("0.0F, 0.0F, 1.0F);"); break;
                }
            }
        }
        return builder.toString();
    }

    private static void handleTranslationValue(StringBuilder builder, DecimalFormat format, float s, float d, boolean addComma) {
        boolean flag = false;
        if(s != 0) {
            builder.append(format.format(s)).append("F");
            flag = true;
        }
        if(d != 0) {
            flag = true;
            if(d < 0) builder.append(format.format(d)).append("F");
            else builder.append("+").append(format.format(d)).append("F");
            builder.append("*f");
        }
        if(!flag) builder.append("0.0F");
        if(addComma) builder.append(", ");
    }

    private static String getFunctionName(BuilderData.Part part) {
        switch (part) {
            case ITEM: return ".item(f -> {";
            case ITEM_AND_HANDS: default: return ".itemHand(f -> {";
            case LEFT_HAND: return ".leftHand(f -> {";
            case RIGHT_HAND: return ".rightHand(f -> {";
            case HANDS: return ".hands(f -> {";
        }
    }

    private static String normal(int input) {
        if(input < 10) {
            return "0" + input;
        } else return input + "";
    }

    private static File findSuitableNameFor(String name) {
        File export = new File(Minecraft.getMinecraft().mcDataDir, "export");
        if(!export.exists()) {
            export.mkdirs();
        }
        File file = new File(export, name + ".txt");
        if(!file.exists()) {
            return file;
        } else {
            int added = 0;
            while (true) {
                String newName = name + "_" + added;
                File ff = new File(export, newName + ".txt");
                if(!ff.exists()) {
                    return ff;
                }
                added++;
            }
        }
    }
}

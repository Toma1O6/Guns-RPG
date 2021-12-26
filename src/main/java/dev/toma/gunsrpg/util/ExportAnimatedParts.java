package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.client.model.WeaponModels;
import dev.toma.gunsrpg.client.model.weapon.AbstractWeaponModel;
import lib.toma.animations.api.AnimationStage;
import net.minecraft.client.resources.I18n;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

public final class ExportAnimatedParts {

    public static void main(String[] args) {
        Map<AbstractWeaponModel, Collection<AnimationStage>> map = WeaponModels.getSupportedPartAnimations();
        String filename = String.format("animatable-components-%s.txt", LocalDateTime.now().toString().replaceAll(":", "_").replaceAll("\\..+", ""));
        try {
            File file = new File("./export/" + filename);
            if (!file.exists()) {
                if (file.createNewFile())
                    System.out.println("File created");
            }
            PrintWriter writer = new PrintWriter(file);
            for (Map.Entry<AbstractWeaponModel, Collection<AnimationStage>> entry : map.entrySet()) {
                String key = getWeaponModelName(entry.getKey().getClass().getSimpleName());
                writer.println(key + " {");
                for (AnimationStage stage : entry.getValue()) {
                    String st = "\t" + I18n.get("animation.stage." + stage.getKey().toString());
                    writer.println(st);
                }
                writer.println("}\n");
            }
            writer.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    private static String getWeaponModelName(String clsName) {
        return clsName.replaceAll("(.*[.])|Model", "");
    }
}

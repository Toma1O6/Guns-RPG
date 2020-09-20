package dev.toma.gunsrpg.client.gui.skills;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.GunsRPGRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SkillTreePlacement {

    public static final Map<SkillCategory, Tree> treeMap = new HashMap<>();

    public static void generatePlacement() {
        new Thread("Skill tree init thread") {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                GunsRPG.log.info("Creating skill placement map");
                Map<SkillCategory, Tree> temp = new HashMap<>();
                for(SkillCategory category : SkillCategory.mainCategories()) {
                    temp.put(category, getTree(category));
                }
                synchronized (treeMap) {
                    for(Map.Entry<SkillCategory, Tree> entry : temp.entrySet()) {
                        treeMap.put(entry.getKey(), entry.getValue());
                    }
                }
                GunsRPG.log.info("Skill tree placement map created in {} ms", System.currentTimeMillis() - startTime);
            }
        }.start();
    }

    private static Tree getTree(SkillCategory category) {
        Predicate<SkillType<?>> filter = type -> category.hasChild() && type.category == category.getChild() || type.category == category;
        List<SkillType<?>> list = GunsRPGRegistries.SKILLS.getValuesCollection().stream().filter(filter).collect(Collectors.toList());
        return new Tree(list);
    }
}

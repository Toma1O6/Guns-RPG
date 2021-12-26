package dev.toma.gunsrpg.client.screen.skill;

import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;

import java.util.List;
import java.util.stream.Collectors;

public class SkillTrees {

    private final SkillCategory category;
    private Tree[] trees;

    public SkillTrees(SkillCategory category) {
        this.category = category;
    }

    public void populate(List<SkillType<?>> list) {
        List<SkillType<?>> roots = list.stream().filter(this::isRoot).collect(Collectors.toList());
        trees = new Tree[roots.size()];
        for (int i = 0; i < roots.size(); i++) {
            trees[i] = new Tree(category, roots.get(i));
        }
    }

    private boolean isRoot(SkillType<?> type) {
        return type.getParent() == null;
    }
}

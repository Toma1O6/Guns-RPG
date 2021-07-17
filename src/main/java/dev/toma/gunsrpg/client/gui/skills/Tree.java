package dev.toma.gunsrpg.client.gui.skills;

import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.ModUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tree {

    public List<Branch> branches = new ArrayList<>();

    public Tree(List<SkillType<?>> types) {
        Node.prepareNewCategory();
        types.sort(Comparator.comparingInt(type -> type.levelRequirement));
        for (SkillType<?> head : types) {
            boolean isChild = false;
            for (SkillType<?> type : types) {
                if (ModUtils.contains(head, type.getChilds())) {
                    isChild = true;
                    break;
                }
            }
            if (!isChild) {
                branches.add(new Branch(head));
                Node.furthestPoint += Node.spacingHorizontal;
            }
        }
    }
}

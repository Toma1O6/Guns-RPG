package dev.toma.gunsrpg.client.gui.skills;

import dev.toma.gunsrpg.common.skills.core.SkillType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tree {

    public List<Branch> branches = new ArrayList<>();

    public Tree(List<SkillType<?>> types) {
        Node.prepareNewCategory();
        types.sort(Comparator.comparingInt(type -> type.levelRequirement));
        for(SkillType<?> skillType : types) {
            if(skillType.isHead()) {
                branches.add(new Branch(skillType));
                Node.furthestPoint += Node.spacingHorizontal;
            }
        }
    }
}

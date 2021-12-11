package dev.toma.gunsrpg.client.screen.skill;

import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.math.IVec2i;
import dev.toma.gunsrpg.util.math.TreeDimensions;

import java.util.HashMap;
import java.util.Map;

public class Tree {

    private final SkillCategory category;
    private final int treeHeight;
    private int[] treeLevelWidths;
    private Map<SkillType<?>, IVec2i> positionMap = new HashMap<>();

    public Tree(SkillCategory category, SkillType<?> root) {
        this.category = category;
        TreeDimensions<SkillType<?>> treeDimensionsNodes = new TreeDimensions<>(root, SkillType::getChildren);
        this.treeHeight = treeDimensionsNodes.getHeight();
        //SkillType<?>[][] tree = buildTree(root);

    }

    private void calculateHeight(SkillType<?> root) {

    }
}

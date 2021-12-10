package dev.toma.gunsrpg.client.screen.skill;

import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.math.Vec2i;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tree {

    private int treeHeight;
    private int[] treeLevelWidths;
    private SkillType<?> root;
    private Map<SkillType<?>, Vec2i> positionMap = new HashMap<>();

    public Tree(SkillType<?> root) {
        this.root = root;
        buildTree();
    }

    private void buildTree() {
        List<SkillType<?>> children = root.getChilds();
    }
}

package dev.toma.gunsrpg.client.screen.skill;

import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.math.IDimensions;
import dev.toma.gunsrpg.util.math.IVec2i;
import dev.toma.gunsrpg.util.math.TreeDimensions;
import dev.toma.gunsrpg.util.math.Vec2iMutable;
import dev.toma.gunsrpg.util.object.Pair;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

public class Tree {

    public static final int GRID_UNIT_SIZE = 4;
    public static final int HALF_UNIT = GRID_UNIT_SIZE / 2;

    private final SkillCategory category;
    private final Map<SkillType<?>, SkillViewData> dataViewMap = new IdentityHashMap<>();

    public Tree(SkillCategory category, SkillType<?> root) {
        this.category = category;
        fillPositionMap(root);
    }

    private void fillPositionMap(SkillType<?> root) {
        TreeNode node = new TreeNode(root, 0, 0);
        generateViewDataForNode(node);
        filterSkills();
    }

    private void filterSkills() {
        Iterator<SkillViewData> iterator = dataViewMap.values().iterator();
        while (iterator.hasNext()) {
            SkillViewData data = iterator.next();
            if (data.isValidForDisplay()) {
                continue;
            }
            iterator.remove();
        }
    }

    private void generateViewDataForNode(TreeNode node) {
        int childCount = node.childrenNodes != null ? node.childrenNodes.length : 0;
        boolean shouldCenter = childCount > 1;
        Vec2iMutable pos = new Vec2iMutable(node.xLevel * GRID_UNIT_SIZE, node.yLevel * GRID_UNIT_SIZE);
        if (childCount > 0) {
            for (TreeNode child : node.childrenNodes) {
                generateViewDataForNode(child);
            }
        }
        if (shouldCenter) {
            int toCenter = (childCount - 1) * HALF_UNIT;
            pos.growX(toCenter);
        }
        dataViewMap.put(node.value, new SkillViewData(node.value, category, pos));
    }

    private static class TreeNode {

        private final int xLevel;
        private final int yLevel;
        private final SkillType<?> value;
        private final TreeNode[] childrenNodes;

        TreeNode(SkillType<?> value, int xLevel, int yLevel) {
            this.value = value;
            this.xLevel = xLevel;
            this.yLevel = yLevel;
            this.childrenNodes = getChildrenNodes();
        }

        TreeNode[] getChildrenNodes() {
            SkillType<?>[] children = value.getChildren();
            if (ModUtils.isNullOrEmpty(children)) {
                return null;
            }
            TreeNode[] out = new TreeNode[children.length];
            for (int i = 0; i < children.length; i++) {
                SkillType<?> child = children[i];
                TreeNode childNode = new TreeNode(child, xLevel + i, yLevel + 1);
                out[i] = childNode;
            }
            return out;
        }
    }

    private static class SkillLink {

        private Pair<IVec2i, IVec2i>[] lines;
    }
}

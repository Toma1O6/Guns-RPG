package dev.toma.gunsrpg.client.screen.skill;

import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.math.IDimensions;
import dev.toma.gunsrpg.util.math.IVec2i;
import dev.toma.gunsrpg.util.math.Vec2iMutable;
import dev.toma.gunsrpg.util.object.Pair;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Tree implements IDimensions {

    public static final int GRID_UNIT_SIZE = 4;
    public static final int HALF_UNIT = GRID_UNIT_SIZE / 2;

    private final SkillCategory category;
    private final Map<SkillType<?>, SkillViewData> dataViewMap = new IdentityHashMap<>();
    private final SkillType<?> root;
    private final int width, height;
    private final boolean useSimplePlacement;

    public Tree(SkillCategory category, SkillType<?> root) {
        this.category = category;
        this.root = root;
        this.useSimplePlacement = this.shouldUseSimplePlacement(root);
        fillPositionMap(root);
        this.width = calculateMaxWidth();
        this.height = calculateMaxHeight();
    }

    public SkillType<?> getRoot() {
        return root;
    }

    public void move(int xCorrection, int yCorrection) {
        dataViewMap.values().forEach(data -> data.move(xCorrection, yCorrection));
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public Set<Map.Entry<SkillType<?>, SkillViewData>> getDataSet() {
        return dataViewMap.entrySet();
    }

    private void fillPositionMap(SkillType<?> root) {
        TreeNode node = new TreeNode(root, 0, 0);
        generateViewDataForNode(node);
    }

    private void generateViewDataForNode(TreeNode node) {
        int childCount = node.childrenNodes != null ? node.childrenNodes.length : 0;
        boolean shouldCenter = !useSimplePlacement && childCount > 1;
        Vec2iMutable pos = new Vec2iMutable(node.xLevel * GRID_UNIT_SIZE, node.yLevel * GRID_UNIT_SIZE);
        if (shouldCenter) {
            int toCenter = (childCount - 1) * HALF_UNIT;
            pos.growX(toCenter);
            centerBranch(node, toCenter);
        }
        if (childCount > 0) {
            for (TreeNode child : node.childrenNodes) {
                generateViewDataForNode(child);
            }
        }
        dataViewMap.put(node.value, new SkillViewData(node.value, category, pos));
    }

    private void centerBranch(TreeNode node, int correction) {
        TreeNode parent;
        while ((parent = node.parent) != null) {
            TreeNode[] children = parent.childrenNodes;
            Optional.ofNullable(dataViewMap.get(node.value)).ifPresent(data -> data.getPos().growX(correction));
            if (children != null && children.length > 1) {
                break;
            }
            node = parent;
        }
    }

    private int calculateMaxWidth() {
        return dataViewMap.values().stream().mapToInt(value -> value.getPos().x()).max().orElse(GRID_UNIT_SIZE);
    }

    private int calculateMaxHeight() {
        return dataViewMap.values().stream().mapToInt(value -> value.getPos().y()).max().orElse(GRID_UNIT_SIZE);
    }

    private boolean shouldUseSimplePlacement(SkillType<?> root) {
        return root == Skills.WOODEN_AMMO_SMITH;
    }

    private static class TreeNode {

        private final int xLevel;
        private final int yLevel;
        private final SkillType<?> value;
        private final TreeNode parent;
        private final TreeNode[] childrenNodes;

        TreeNode(SkillType<?> value, int xLevel, int yLevel) {
            this(value, null, xLevel, yLevel);
        }

        TreeNode(SkillType<?> value, TreeNode parent, int xLevel, int yLevel) {
            this.value = value;
            this.parent = parent;
            this.xLevel = xLevel;
            this.yLevel = yLevel;
            this.childrenNodes = getChildrenNodes();
        }

        TreeNode[] getChildrenNodes() {
            ISkillHierarchy<?> hierarchy = value.getHierarchy();
            SkillType<?>[] children = hierarchy.getChildren();
            if (ModUtils.isNullOrEmpty(children)) {
                return null;
            }
            TreeNode[] out = new TreeNode[children.length];
            for (int i = 0; i < children.length; i++) {
                SkillType<?> child = children[i];
                TreeNode childNode = new TreeNode(child, this, xLevel + i, yLevel + 1);
                out[i] = childNode;
            }
            return out;
        }
    }

    private static class SkillLink {

        private Pair<IVec2i, IVec2i>[] lines;
    }
}

package com.wf.firearms.client.gui.layout;

import com.wf.firearms.data.SkillDatabase;
import com.wf.firearms.data.SkillNode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** 移植自 Guns RPG {@code SkillTrees}：每分类多棵根树横向排列。 */
public final class GunsRpgSkillTrees {
    private final String category;
    private GunsRpgTree[] trees = new GunsRpgTree[0];
    private int canvasWidth;
    private int canvasHeight;

    public GunsRpgSkillTrees(String category) {
        this.category = category;
    }

    public void build() {
        Set<String> extensionIds = collectExtensionIds(category);
        List<String> roots = new ArrayList<>(
                SkillDatabase.getRootsByCategory().getOrDefault(category, List.of()));
        roots.removeIf(id -> extensionIds.contains(id));
        roots.sort(compareRoots());

        trees = new GunsRpgTree[roots.size()];
        int xCorrection = GunsRpgTree.GRID_UNIT_SIZE + GunsRpgTree.HALF_UNIT;
        int maxH = 0;
        for (int i = 0; i < roots.size(); i++) {
            GunsRpgTree tree = new GunsRpgTree(category, roots.get(i));
            tree.move(xCorrection, 0);
            tree.makeConnections(tree.getRootId());
            trees[i] = tree;
            xCorrection += tree.getWidth() + GunsRpgTree.GRID_UNIT_SIZE + GunsRpgTree.HALF_UNIT;
            maxH = Math.max(maxH, tree.getHeight());
        }
        canvasWidth = xCorrection;
        canvasHeight = maxH + GunsRpgTree.GRID_UNIT_SIZE * 3;
    }

    private static Set<String> collectExtensionIds(String category) {
        Set<String> out = new HashSet<>();
        for (SkillNode node : SkillDatabase.getAllNodes().values()) {
            if (!category.equals(node.getCategory())) {
                continue;
            }
            for (String ext : node.getExtensions()) {
                out.add(SkillNode.stripNamespace(ext));
            }
        }
        return out;
    }

    private Comparator<String> compareRoots() {
        return Comparator.<String>comparingInt(id -> SkillDatabase.getNode(id).map(SkillNode::getLevel).orElse(0))
                .thenComparingInt(id -> childCount(id))
                .thenComparing(id -> id);
    }

    private static int childCount(String rootId) {
        return SkillDatabase.getNode(rootId)
                .map(n -> countRecursive(n.getId(), new HashSet<>()))
                .orElse(0);
    }

    private static int countRecursive(String id, Set<String> seen) {
        if (!seen.add(id)) {
            return 0;
        }
        int c = 1;
        for (String child : SkillDatabase.getNode(id).map(SkillNode::getChildren).orElse(List.of())) {
            c += countRecursive(child, seen);
        }
        return c;
    }

    public GunsRpgTree[] getTrees() {
        return trees;
    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }
}

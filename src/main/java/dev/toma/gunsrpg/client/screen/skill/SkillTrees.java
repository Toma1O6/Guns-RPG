package dev.toma.gunsrpg.client.screen.skill;

import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.helper.TreeHelper;
import lib.toma.animations.QuickSort;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SkillTrees {

    private final SkillCategory category;
    private Tree[] trees;

    public SkillTrees(SkillCategory category) {
        this.category = category;
    }

    public void populate(List<SkillType<?>> list) {
        Set<SkillType<?>> extensions = list.stream().map(SkillType::getHierarchy).filter(this::hasExtensions).flatMap(this::extensionsAsStream).collect(Collectors.toSet());
        List<SkillType<?>> roots = list.stream().filter(this::isRoot).filter(type -> notAnExtension(type, extensions)).collect(Collectors.toList());
        trees = new Tree[roots.size()];
        for (int i = 0; i < roots.size(); i++) {
            trees[i] = new Tree(category, roots.get(i));
        }
        QuickSort.sort(trees, this::compareTrees);
        int xCorrection = Tree.GRID_UNIT_SIZE + Tree.HALF_UNIT;
        for (Tree tree : trees) {
            tree.move(xCorrection, 0);
            int width = tree.getWidth();
            xCorrection += width + Tree.GRID_UNIT_SIZE + Tree.HALF_UNIT;
        }
    }

    public Tree[] getTrees() {
        return trees;
    }

    private boolean isRoot(SkillType<?> type) {
        return type.getHierarchy().getParent() == null;
    }

    private boolean notAnExtension(SkillType<?> in, Set<SkillType<?>> extensions) {
        return !extensions.contains(in);
    }

    private Stream<SkillType<?>> extensionsAsStream(ISkillHierarchy<?> hierarchy) {
        return Arrays.stream(hierarchy.getExtensions());
    }

    private boolean hasExtensions(ISkillHierarchy<?> hierarchy) {
        return hierarchy.getExtensions() != null;
    }

    private int compareTrees(Tree tree1, Tree tree2) {
        SkillType<?> root1 = tree1.getRoot();
        SkillType<?> root2 = tree2.getRoot();
        int level1 = root1.getProperties().getRequiredLevel();
        int level2 = root2.getProperties().getRequiredLevel();
        int levelDiff = level1 - level2;
        if (levelDiff == 0) {
            return compareChildren(root1, root2);
        }
        return levelDiff;
    }

    private int compareChildren(SkillType<?> type1, SkillType<?> type2) {
        Function<SkillType<?>, SkillType<?>[]> child = type -> type.getHierarchy().getChildren();
        return TreeHelper.getChildCount(type1, child) - TreeHelper.getChildCount(type2, child);
    }
}

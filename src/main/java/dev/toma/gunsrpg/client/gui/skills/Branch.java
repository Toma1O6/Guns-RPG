package dev.toma.gunsrpg.client.gui.skills;

import dev.toma.gunsrpg.common.skills.core.SkillType;

import java.util.ArrayList;
import java.util.List;

public class Branch {

    private final List<PlacementContext> placements = new ArrayList<>();

    public Branch(SkillType<?> head) {
        if (Node.currentX != Node.furthestPoint) Node.currentX = Node.furthestPoint;
        placements.addAll(Node.createNode(null, head).resetY().create());
        if (!head.isCustomDisplayEnabled()) placeChilds(head, head.getChilds().toArray(new SkillType[0]));
    }

    private void placeChilds(SkillType<?> parent, SkillType<?>[] types) {
        if (types.length == 0) return;
        placements.addAll(Node.createNode(parent, types).create());
        for (SkillType<?> child : types) {
            if (!child.isCustomDisplayEnabled()) placeChilds(child, child.getChilds().toArray(new SkillType[0]));
        }
    }

    public List<PlacementContext> getPlacements() {
        return placements;
    }
}

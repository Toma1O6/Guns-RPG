package dev.toma.gunsrpg.client.gui.skills;

import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.util.math.Vec2Di;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node {

    protected final SkillType<?>[] types;
    protected final SkillType<?> parent;
    public static int currentX, currentY;
    protected static final Vec2Di startingPoint = new Vec2Di(40, 60);
    public static int furthestPoint = startingPoint.x;
    protected static final int spacingHorizontal = GRPGConfig.client.skillTreeSpacings.x;
    protected static final int spacingVerical = GRPGConfig.client.skillTreeSpacings.y;

    public Node(SkillType<?> parent, SkillType<?> type) {
        this(parent, new SkillType[] {type});
    }

    public Node(SkillType<?> parent, SkillType<?>[] types) {
        this.parent = parent;
        this.types = types;
    }

    public Node resetY() {
        currentY = startingPoint.y;
        return this;
    }

    public static void prepareNewCategory() {
        currentX = startingPoint.x;
        currentY = startingPoint.y;
        furthestPoint = currentX;
    }

    public static Node createNode(SkillType<?> parent, SkillType<?> skillType) {
        return new Node(parent, skillType);
    }

    public static Node createNode(SkillType<?> parent, SkillType<?>[] skillTypes) {
        return new Node(parent, skillTypes);
    }

    public List<PlacementContext> create() {
        boolean flag = types.length > 1;
        if(flag) {
            List<PlacementContext> ctxs = new ArrayList<>();
            int backup = currentX;
            for(int i = 0; i < types.length; i++) {
                SkillType<?> type = types[i];
                ctxs.add(new PlacementContext(parent, type, new Vec2Di(currentX, currentY)));
                if(i < types.length - 1) currentX += spacingHorizontal;
                if(currentX > furthestPoint) furthestPoint = currentX;
            }
            currentY += spacingVerical;
            currentX = backup;
            return ctxs;
        } else {
            Vec2Di pos = new Vec2Di(currentX, currentY);
            currentY += spacingVerical;
            return Collections.singletonList(new PlacementContext(parent, types[0], pos));
        }
    }
}

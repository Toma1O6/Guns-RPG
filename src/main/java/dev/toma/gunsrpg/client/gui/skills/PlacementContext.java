package dev.toma.gunsrpg.client.gui.skills;

import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.math.ConfigurableVec2i;
import dev.toma.gunsrpg.util.math.Vec2i;
import dev.toma.gunsrpg.util.object.LazyLoader;

import java.util.List;

public class PlacementContext {

    public LazyLoader<Vec2i> parentPos = new LazyLoader<>(() -> null);
    public SkillType<?> parent;
    public SkillType<?> type;
    public Vec2i pos;

    public PlacementContext(SkillType<?> parent, SkillType<?> type, Vec2i pos) {
        this.type = type;
        this.pos = pos;
        this.parent = parent;
        this.getParentPos(parent);
    }

    public void getParentPos(SkillType<?> parent) {
        if(parent == null) return;
        parentPos = new LazyLoader<>(() -> {
            SkillCategory category = null;
            for (SkillCategory temp : SkillCategory.mainCategories()) {
                if (temp.hasChild() && temp.getChild() == parent.category || temp == parent.category) {
                    category = temp;
                    break;
                }
            }
            List<Branch> branches = SkillTreePlacement.treeMap.get(category).branches;
            for (Branch branch : branches) {
                for (PlacementContext ctx : branch.getPlacements()) {
                    if (ctx.type.equals(parent)) {
                        return ctx.pos;
                    }
                }
            }
            return null;
        });
    }
}

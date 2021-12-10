package dev.toma.gunsrpg.client.screen.skill.placement;

import dev.toma.gunsrpg.common.skills.core.SkillType;

public class SkillPlacementData implements IPlacementData {

    private final SkillType<?> context;
    private final UiPosition position;
    private final DisplayStyle style;

    public SkillPlacementData(SkillType<?> context, UiPosition position, DisplayStyle style) {
        this.context = context;
        this.position = position;
        this.style = style;
    }

    @Override
    public UiPosition getUiPosition() {
        return position;
    }

    public SkillType<?> getContext() {
        return context;
    }

    public DisplayStyle getStyle() {
        return style;
    }

    public enum DisplayStyle {

        DEFAULT,
        PARENT,
        FOREIGN
    }
}

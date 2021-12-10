package dev.toma.gunsrpg.client.screen.skill.placement;

public class PerkPlacementData implements IPlacementData {

    private final UiPosition position;

    public PerkPlacementData(UiPosition position) {
        this.position = position;
    }

    @Override
    public UiPosition getUiPosition() {
        return position;
    }
}

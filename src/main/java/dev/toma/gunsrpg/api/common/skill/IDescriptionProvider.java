package dev.toma.gunsrpg.api.common.skill;

import net.minecraft.util.text.ITextComponent;

@FunctionalInterface
public interface IDescriptionProvider {

    ITextComponent[] supplyDescription(int desiredLineCount);
}

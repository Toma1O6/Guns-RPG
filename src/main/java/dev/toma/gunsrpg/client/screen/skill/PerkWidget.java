package dev.toma.gunsrpg.client.screen.skill;

import dev.toma.configuration.api.client.widget.ITickable;
import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.api.common.data.IPerkProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.perk.IPerk;
import dev.toma.gunsrpg.common.perk.Perk;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.StringTextComponent;

public class PerkWidget extends Widget implements ITickable {

    private final FontRenderer font;
    private final boolean isModified;
    private final Perk perk;
    private final IViewContext context;

    public PerkWidget(int x, int y, int width, int height, FontRenderer font, Perk perk, IViewContext context) {
        super(x, y, width, height, StringTextComponent.EMPTY);
        this.font = font;
        this.perk = perk;
        this.context = context;
        this.isModified = false;
    }

    private boolean isModified() {
        IPlayerData data = context.getData();
        IAttributeProvider attributeProvider = data.getAttributes();
        IAttributeId attributeId =
    }

    private int getValue() {
        IPerk iPerk = provider.getPerk(perk);
        if (iPerk == null) {

        }
    }
}

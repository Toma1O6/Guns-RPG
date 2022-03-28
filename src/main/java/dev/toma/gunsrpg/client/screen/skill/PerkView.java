package dev.toma.gunsrpg.client.screen.skill;

import dev.toma.gunsrpg.api.common.data.IPerkProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.perk.IPerkStat;
import dev.toma.gunsrpg.client.screen.widgets.*;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.item.perk.CrystalAttribute;
import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.common.perk.PerkRegistry;
import dev.toma.gunsrpg.common.perk.PerkType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class PerkView extends View {

    private PannableWidget pannableWidget;
    private FooterWidget footerWidget;
    private ViewSwitchWidget viewSwitchWidget;

    public PerkView(int windowWidth, int windowHeight, IViewManager manager) {
        super(windowWidth, windowHeight, manager);
    }

    @Override
    protected void init() {
        clear();
        // view switch
        viewSwitchWidget = addWidget(new ViewSwitchWidget(x + width - 42, height - 62, 32, 32, new ItemStack(ModItems.SKILLPOINT_BOOK)));
        viewSwitchWidget.setClickEvent(this::openSkillView);
        viewSwitchWidget.setColorSchema(0x49A1FF, 0x49D8FF);
        // perk widgets
        pannableWidget = addWidget(new PannableWidget(x, y + 20, width, height - 40));
        pannableWidget.fill((handler, px, py) -> {
            Set<Perk> allPerks = PerkRegistry.getRegistry().getPerks();
            IPerkProvider provider = manager.getContext().getData().getPerkProvider();
            List<Perk> ownedPerks = new ArrayList<>(provider.getActivePerks());
            Comparator<Perk> valueComparator = Comparator.comparing(perk -> provider.getPerkStat(perk).getAttribute(), Comparator.comparing(attribute -> -Math.abs(attribute.getValue())));
            Comparator<Perk> typeComparator = Comparator.comparing(perk -> provider.getPerkStat(perk).getAttribute(), Comparator.comparing(attr -> attr.getType().ordinal()));
            ownedPerks.sort(typeComparator.thenComparing(valueComparator));
            int index = 0;
            int yOffset = 0;
            int maxWidth = (width - 85) / 85;
            for (Perk perk : ownedPerks) {
                allPerks.remove(perk);
                IPerkStat stat = provider.getPerkStat(perk);
                CrystalAttribute crystalAttribute = stat.getAttribute();
                PerkType type = crystalAttribute.getType();
                float value = crystalAttribute.getValue();
                PerkWidget.State state = value == 0 ? PerkWidget.State.NULLIFIED : type == PerkType.BUFF ? PerkWidget.State.BUFF : PerkWidget.State.DEBUFF;
                int x = 30 + px + (index % maxWidth) * 85;
                int y = 5 + py + (yOffset + index / maxWidth) * 35;
                handler.add(new PerkWidget(x, y, 80, 20, font, perk, value, state));
                ++index;
            }
            yOffset = 1 + ((index - 1) / maxWidth);
            if (ownedPerks.isEmpty()) yOffset -= 1;
            index = 0;
            for (Perk perk : allPerks) {
                int x = 30 + px + (index % maxWidth) * 85;
                int y = 5 + py + (yOffset + index / maxWidth) * 35;
                handler.add(new PerkWidget(x, y, 80, 20, font, perk, 0.0F, PerkWidget.State.NONE));
                ++index;
            }
        });
        // header
        ITextComponent userName = client.player.getName();
        ITextComponent headerTitle = new TranslationTextComponent("view.perk.header", userName.getString());
        addWidget(new HeaderWidget(x, y, width, 20, headerTitle, font));
        // footer
        IViewContext viewContext = manager.getContext();
        IPlayerData data = viewContext.getData();
        IPerkProvider perkProvider = data.getPerkProvider();
        footerWidget = addWidget(new FooterWidget(x, y + height - 20, width, 20, client.font, perkProvider));
        footerWidget.setPointColor(0xFFFF);
    }

    private void openSkillView() {
        manager.setView(new SkillsView(width, height, manager));
    }
}

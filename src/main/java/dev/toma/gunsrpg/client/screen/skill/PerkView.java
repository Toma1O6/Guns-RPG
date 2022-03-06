package dev.toma.gunsrpg.client.screen.skill;

import dev.toma.gunsrpg.api.common.data.IPerkProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.client.screen.widgets.FooterWidget;
import dev.toma.gunsrpg.client.screen.widgets.HeaderWidget;
import dev.toma.gunsrpg.client.screen.widgets.PannableWidget;
import dev.toma.gunsrpg.client.screen.widgets.ViewSwitchWidget;
import dev.toma.gunsrpg.common.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
        // header
        ITextComponent userName = client.player.getName();
        ITextComponent headerTitle = new TranslationTextComponent("view.perk.header", userName.getString());
        addWidget(new HeaderWidget(x, y, width, 20, headerTitle, font));
        // footer
        IViewContext viewContext = manager.getContext();
        IPlayerData data = viewContext.getData();
        IPerkProvider perkProvider = data.getPerkProvider();
        footerWidget = addWidget(new FooterWidget(x, y + height - 20, width, 20, client.font, perkProvider));
    }

    private void openSkillView() {
        manager.setView(new SkillsView(width, height, manager));
    }
}

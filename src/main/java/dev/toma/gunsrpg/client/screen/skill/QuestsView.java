package dev.toma.gunsrpg.client.screen.skill;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.client.ScreenDataEventListener;
import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.client.render.infobar.IDataModel;
import dev.toma.gunsrpg.client.screen.widgets.HeaderWidget;
import dev.toma.gunsrpg.client.screen.widgets.ViewSwitchWidget;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.quest.QuestStatus;
import dev.toma.gunsrpg.common.quests.sharing.GroupInvite;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import lib.toma.animations.engine.screen.animator.widget.LabelWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;
import java.util.UUID;
import java.util.function.BiFunction;

public class QuestsView extends View implements ScreenDataEventListener {

    private static final ITextComponent HEADER = new TranslationTextComponent("view.quest.header");
    private static final BiFunction<Integer, Integer, ITextComponent> ACTIVE_QUESTS = (current, limit) -> new TranslationTextComponent("view.quest.active_quests", current, limit);
    private static final BiFunction<Integer, Integer, ITextComponent> MY_PARTY = (current, limit) -> new TranslationTextComponent("view.quest.my_party", current, limit);
    private static final BiFunction<Integer, Integer, ITextComponent> INVITES = (current, limit) -> new TranslationTextComponent("view.quest.my_invites", current, limit);

    private Quest<?> quest;
    private ViewSwitchWidget perkView;
    private ViewSwitchWidget skillView;

    public QuestsView(int windowWidth, int windowHeight, IViewManager manager) {
        super(windowWidth, windowHeight, manager);
    }

    @Override
    public void onQuestingDataReceived(IQuestingData questingData) {
        this.init();
    }

    @Override
    protected void init() {
        clear();
        IQuestingData questing = QuestingDataProvider.getQuesting(client.level);
        this.quest = questing.getActiveQuestForPlayer(client.player);
        UUID clientId = this.client.player.getUUID();
        QuestingGroup group = questing.getOrCreateGroup(client.player);
        Collection<GroupInvite> invites = questing.listInvitesForPlayer(clientId);

        // View switches
        perkView = addWidget(new ViewSwitchWidget(x + width - 42, height - 62, 32, 32, new ItemStack(ModItems.PERKPOINT_BOOK)));
        perkView.setClickEvent(() -> this.manager.setView(new PerkView(this.width, this.height, this.manager)));
        perkView.setColorSchema(0x49A1FF, 0x49D8FF);
        skillView = addWidget(new ViewSwitchWidget(x + width - 42, height - 99, 32, 32, new ItemStack(ModItems.SKILLPOINT_BOOK)));
        skillView.setClickEvent(() -> this.manager.setView(new SkillsView(this.width, this.height, this.manager)));
        skillView.setColorSchema(0xFFA60C, 0xFFD21E);

        // header
        addWidget(new HeaderWidget(0, 0, width, 20, HEADER, this.font));

        LabelWidget questsLabel = addWidget(new LabelWidget(5, 25, 100, 10, ACTIVE_QUESTS.apply(this.quest != null ? 1 : 0, 1), this.font));
        questsLabel.castShadow = false;

        LabelWidget partyLabel = addWidget(new LabelWidget(5 + (width / 3), 25, 100, 10, MY_PARTY.apply(group.getMembers().size(), group.getMaxMemberCount()), this.font));
        partyLabel.castShadow = false;

        // TODO change the 5 to actually displayed count
        LabelWidget inviteLabel = addWidget(new LabelWidget(5 + (width / 3 * 2), 25, 100, 10, INVITES.apply(5, invites.size()), this.font));
        inviteLabel.castShadow = false;

        // Party
        if (group != null) {
            int left = 5 + width / 3;
            Collection<UUID> memberSet = group.getMembers();
            int index = 0;
            for (UUID uuid : memberSet) {
                boolean isLeader = group.isLeader(uuid);
                boolean isMe = uuid.equals(clientId);
                String username = group.getName(uuid);
                LabelWidget nameWidget = addWidget(new LabelWidget(left, 40 + 11 * index++, 100, 9, new StringTextComponent(username), this.font));
                nameWidget.castShadow = false;
                nameWidget.foregroundColor = isLeader ? 0xFFFF00 : 0xFFFFFF;
                nameWidget.hoverBackgroundColor = 0x44FFFFFF;
            }
        }

        // Invites - received
        Button inviteMemberButton = addWidget(new Button(width - 105, height - 25, 100, 20, new TranslationTextComponent("gunsrpg.quest.party.invite"), btn -> {
            View view = new InviteMemberView(width, height, manager);
            this.manager.setView(view);
        }));
        inviteMemberButton.active = group.isLeader(clientId) && (quest == null || (quest.getStatus() != QuestStatus.ACTIVE && !quest.isStarted())) && group.getMembers().size() < group.getMaxMemberCount();
    }

    @Override
    protected void renderView(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (this.quest != null) {
            IDataModel model = this.quest.getDisplayModel(this.client.player.getUUID());
            model.renderModel(stack, this.font, 5, 40, false, false);
        }
    }
}

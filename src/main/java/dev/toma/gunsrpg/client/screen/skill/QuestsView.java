package dev.toma.gunsrpg.client.screen.skill;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.client.ScreenDataEventListener;
import dev.toma.gunsrpg.api.common.data.IQuestingData;
import dev.toma.gunsrpg.client.render.infobar.IDataModel;
import dev.toma.gunsrpg.client.screen.widgets.ContainerWidget;
import dev.toma.gunsrpg.client.screen.widgets.HeaderWidget;
import dev.toma.gunsrpg.client.screen.widgets.ViewSwitchWidget;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.quest.QuestStatus;
import dev.toma.gunsrpg.common.quests.sharing.GroupInvite;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.C2S_InviteEvent;
import dev.toma.gunsrpg.network.packet.C2S_RemoveFromGroup;
import dev.toma.gunsrpg.world.cap.QuestingDataProvider;
import lib.toma.animations.engine.screen.animator.widget.LabelWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;
import java.util.UUID;
import java.util.function.BiFunction;

public class QuestsView extends View implements ScreenDataEventListener {

    private static final ITextComponent HEADER = new TranslationTextComponent("view.quest.header");
    private static final BiFunction<Integer, Integer, ITextComponent> ACTIVE_QUESTS = (current, limit) -> new TranslationTextComponent("view.quest.active_quests", current, limit).withStyle(TextFormatting.UNDERLINE);
    private static final BiFunction<Integer, Integer, ITextComponent> MY_PARTY = (current, limit) -> new TranslationTextComponent("view.quest.my_party", current, limit).withStyle(TextFormatting.UNDERLINE);
    private static final BiFunction<Integer, Integer, ITextComponent> INVITES = (current, limit) -> new TranslationTextComponent("view.quest.my_invites", current, limit).withStyle(TextFormatting.UNDERLINE);

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

        LabelWidget partyLabel = addWidget(new LabelWidget(5 + (width / 3), 25, 100, 10, MY_PARTY.apply(group.getMemberCount(), group.getMaxMemberCount()), this.font));
        partyLabel.castShadow = false;

        int inviteWidgetWidth = 120;
        int inviteWidgetHeight = 40;
        int displayedInvites = (this.height - 50) / inviteWidgetHeight;
        LabelWidget inviteLabel = addWidget(new LabelWidget(5 + (width / 3 * 2), 25, 100, 10, INVITES.apply(Math.min(displayedInvites, invites.size()), invites.size()), this.font));
        inviteLabel.castShadow = false;

        // Party
        int partyLeft = 5 + width / 3;
        Collection<UUID> memberSet = group.getMembers();
        int index = 0;
        for (UUID uuid : memberSet) {
            boolean isLeader = group.isLeader(uuid);
            boolean isMe = uuid.equals(clientId);
            String username = group.getName(uuid);

            LabelWidget nameWidget = addWidget(new LabelWidget(partyLeft, 40 + 11 * index++, 100, 9, new StringTextComponent(username), this.font));
            nameWidget.castShadow = false;
            nameWidget.foregroundColor = isMe
                    ? 0x00FFFF
                    : (isLeader ? 0xFFFF00 : 0xFFFFFF);
            nameWidget.hoverBackgroundColor = 0x44FFFFFF;
            if ((isMe || group.isLeader(clientId)) && group.getMemberCount() > 1) {
                LabelButton button = addWidget(new LabelButton(partyLeft + 102, nameWidget.y, 9, 9, new StringTextComponent("x"), this.font, () -> this.removeButtonClicked(uuid)));
                button.castShadow = false;
                button.foregroundColor = 0xFF0000;
                button.hoverBackgroundColor = 0x44FFFFFF;
                button.horizontalOffset = 2F;
                button.verticalOffset = -1F;
            }
        }

        // Invites - received
        index = 0;
        int invitesLeft = 5 + width / 3 * 2;
        for (GroupInvite invite : invites) {
            addWidget(new InviteWidget(invitesLeft, 40 + inviteWidgetHeight * index++, inviteWidgetWidth, inviteWidgetHeight, invite));
        }

        Button inviteMemberButton = addWidget(new Button(width - 105, height - 25, 100, 20, new TranslationTextComponent("gunsrpg.quest.party.invite"), btn -> {
            View view = new InviteMemberView(width, height, manager);
            this.manager.setView(view);
        }));
        inviteMemberButton.active = group.isLeader(clientId) && (quest == null || (quest.getStatus() == QuestStatus.ACTIVE && !quest.isStarted())) && group.getMemberCount() < group.getMaxMemberCount();
    }

    @Override
    protected void renderView(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        if (this.quest != null) {
            IDataModel model = this.quest.getDisplayModel(this.client.player.getUUID());
            model.renderModel(stack, this.font, 5, 40, false);
        }
    }

    private void removeButtonClicked(UUID target) {
        NetworkManager.sendServerPacket(new C2S_RemoveFromGroup(target));
    }

    private static final class LabelButton extends LabelWidget {

        private final Runnable onPress;

        public LabelButton(int x, int y, int width, int height, ITextComponent message, FontRenderer font, Runnable onPress) {
            super(x, y, width, height, message, font);
            this.onPress = onPress;
        }

        @Override
        protected boolean isValidClickButton(int button) {
            return true;
        }

        @Override
        public void onClick(double x, double y) {
            this.onPress.run();
        }
    }

    private static final class InviteWidget extends ContainerWidget {

        private final GroupInvite invite;
        private final NetworkPlayerInfo source;

        public InviteWidget(int x, int y, int width, int height, GroupInvite invite) {
            super(x, y, width, height);
            this.invite = invite;
            this.source = this.getSource(invite.getGroupId());
            if (this.source == null) {
                NetworkManager.sendServerPacket(new C2S_InviteEvent(false, invite.getGroupId()));
            } else {
                addWidget(new Button(this.x, this.y + this.height - 20, 59, 20, new TranslationTextComponent("label.gunsrpg.accept"), this::inviteAccepted));
                addWidget(new Button(this.x + 62, this.y + this.height - 20, 59, 20, new TranslationTextComponent("label.gunsrpg.reject"), this::inviteRejected));
            }
        }

        @Override
        public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
            if (this.source == null)
                return;
            fill(stack, this.x, this.y, this.x + this.width, this.y + this.height, 0x44 << 24);
            ResourceLocation skin = this.source.getSkinLocation();
            Minecraft client = Minecraft.getInstance();
            FontRenderer font = client.font;
            client.getTextureManager().bind(skin);
            blit(stack, this.x, this.y, 16, 16, 8.0F, 8, 8, 8, 64, 64);
            IQuestingData questing = QuestingDataProvider.getQuesting(client.level);
            QuestingGroup group = questing.getGroup(this.invite.getGroupId());
            if (group != null) {
                ITextComponent label = new TranslationTextComponent("label.gunsrpg.group", group.getName());
                font.draw(stack, label, this.x + 16 + (this.width - 16 - font.width(label)) / 2.0F, this.y + (16 - font.lineHeight) / 2.0F, 0xFFFFFF);
            }
            super.renderButton(stack, mouseX, mouseY, partialTicks);
        }

        private NetworkPlayerInfo getSource(UUID id) {
            Minecraft client = Minecraft.getInstance();
            ClientPlayNetHandler conn = client.getConnection();
            return conn.getPlayerInfo(id);
        }

        private void inviteAccepted(Button button) {
            NetworkManager.sendServerPacket(new C2S_InviteEvent(true, invite.getGroupId()));
        }

        private void inviteRejected(Button button) {
            NetworkManager.sendServerPacket(new C2S_InviteEvent(false, invite.getGroupId()));
        }
    }
}

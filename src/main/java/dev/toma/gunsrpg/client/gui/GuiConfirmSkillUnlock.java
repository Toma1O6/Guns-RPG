package dev.toma.gunsrpg.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.client.gui.skills.GuiPlayerSkills;
import dev.toma.gunsrpg.client.gui.skills.PlacementContext;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketUnlockSkill;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class GuiConfirmSkillUnlock extends Screen {

    private final int xSize = 215;
    private final int ySize = 60;
    private int left;
    private int top;
    private final GuiPlayerSkills parent;
    private final SkillType<?> type;
    @Nullable
    private final PlacementContext ctx;

    public GuiConfirmSkillUnlock(GuiPlayerSkills parent, SkillType<?> type, @Nullable PlacementContext ctx) {
        super(new TranslationTextComponent("screen.confirm_skillUnlock"));
        this.parent = parent;
        this.type = type;
        this.ctx = ctx;
    }

    @Override
    public void init() {
        left = (width - xSize) / 2;
        top = (height - ySize) / 2;
        addButton(new Button(left + 5, top + ySize - 25, 100, 20, new StringTextComponent("Confirm"), this::buttonConfirm_Click));
        addButton(new Button(left + 110, top + ySize - 25, 100, 20, new StringTextComponent("Cancel"), this::buttonCancel_Click));
    }

    private void buttonConfirm_Click(Button button) {
        if (ctx != null) {
            if (ctx.parent != null) {
                PlayerDataFactory.get(minecraft.player).ifPresent(data -> {
                    if (data.getSkills().hasSkill(ctx.parent) && type.getCriteria().isUnlockAvailable(data, type)) {
                        NetworkManager.sendServerPacket(new SPacketUnlockSkill(type, ctx.parent));
                        minecraft.setScreen(parent);
                    }
                });
            }
        } else {
            PlayerDataFactory.get(minecraft.player).ifPresent(data -> {
                if (type.getCriteria().isUnlockAvailable(data, type)) {
                    NetworkManager.sendServerPacket(new SPacketUnlockSkill(type));
                    minecraft.setScreen(parent);
                }
            });
        }
    }

    private void buttonCancel_Click(Button button) {
        minecraft.setScreen(parent);
    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        matrix.pushPose();
        matrix.translate(0, 0, -200);
        parent.drawScreen(mouseX, mouseY, partialTicks);
        matrix.popPose();
        ModUtils.renderColor(left, top, left + xSize, top + ySize, 0, 0, 0, 1.0F);
        ModUtils.renderColor(left + 1, top + 1, left + xSize - 1, top + ySize - 1, 0.25F, 0.25F, 0.25F, 1.0F);
        String question = "Are you sure you want to buy this skill?";
        int qw = font.width(question);
        font.drawShadow(matrix, question, left + (xSize - qw) / 2.0F, top + 5, 0xffffff);
        String skillName = type.getDisplayName();
        int sw = font.width(skillName);
        font.drawShadow(matrix, skillName, left + (xSize - sw) / 2.0F, top + 17, 0xffff00);
        super.render(matrix, mouseX, mouseY, partialTicks);
    }
}

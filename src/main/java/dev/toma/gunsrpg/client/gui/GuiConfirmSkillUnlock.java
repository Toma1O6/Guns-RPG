package dev.toma.gunsrpg.client.gui;

import dev.toma.gunsrpg.client.gui.skills.GuiPlayerSkills;
import dev.toma.gunsrpg.client.gui.skills.PlacementContext;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketUnlockSkill;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nullable;

public class GuiConfirmSkillUnlock extends GuiScreen {

    private final int xSize = 215;
    private final int ySize = 60;
    private int left;
    private int top;
    private final GuiPlayerSkills parent;
    private final SkillType<?> type;
    @Nullable
    private final PlacementContext ctx;

    public GuiConfirmSkillUnlock(GuiPlayerSkills parent, SkillType<?> type, @Nullable PlacementContext ctx) {
        this.parent = parent;
        this.type = type;
        this.ctx = ctx;
    }

    @Override
    public void initGui() {
        left = (width - xSize) / 2;
        top = (height - ySize) / 2;
        addButton(new GuiButton(0, left + 5, top + ySize - 25, 100, 20, "Confirm"));
        addButton(new GuiButton(1, left + 110, top + ySize - 25, 100, 20, "Cancel"));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                if(ctx != null) {
                    if(ctx.parent != null) {
                        if(PlayerDataFactory.hasActiveSkill(mc.player, ctx.parent) && type.getCriteria().isUnlockAvailable(PlayerDataFactory.get(mc.player), type)) {
                            NetworkManager.toServer(new SPacketUnlockSkill(type, ctx.parent));
                            mc.displayGuiScreen(parent);
                        }
                    }
                } else if(type.getCriteria().isUnlockAvailable(PlayerDataFactory.get(mc.player), type)) {
                    NetworkManager.toServer(new SPacketUnlockSkill(type));
                    mc.displayGuiScreen(parent);
                }
                break;
            case 1:
                mc.displayGuiScreen(parent);
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, -200);
        parent.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.popMatrix();
        ModUtils.renderColor(left, top, left + xSize, top + ySize, 0, 0, 0, 1.0F);
        ModUtils.renderColor(left + 1, top + 1, left + xSize - 1, top + ySize - 1, 0.25F, 0.25F, 0.25F, 1.0F);
        String question = "Are you sure you want to buy this skill?";
        int qw = mc.fontRenderer.getStringWidth(question);
        mc.fontRenderer.drawStringWithShadow(question, left + (xSize - qw) / 2.0F, top + 5, 0xffffff);
        String skillName = type.getDisplayName();
        int sw = mc.fontRenderer.getStringWidth(skillName);
        mc.fontRenderer.drawStringWithShadow(skillName, left + (xSize - sw) / 2.0F, top + 17, 0xffff00);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}

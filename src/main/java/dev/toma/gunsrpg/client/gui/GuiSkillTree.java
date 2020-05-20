package dev.toma.gunsrpg.client.gui;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.AbilityData;
import dev.toma.gunsrpg.common.capability.object.SkillData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skilltree.Ability;
import dev.toma.gunsrpg.common.skilltree.EntryInstance;
import dev.toma.gunsrpg.common.skilltree.PlayerSkillTree;
import dev.toma.gunsrpg.common.skilltree.SkillTreeEntry;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketAbilityUpdate;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.math.Vec2Di;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuiSkillTree extends GuiScreen {

    private static final ResourceLocation SKILL_TREE_TEXTURES = GunsRPG.makeResource("textures/icons/skill_tree_bg.png");
    private final List<DisplayObject> objectList = new ArrayList<>();
    private final List<Pair<DisplayObject, DisplayObject>> lines = new ArrayList<>();
    private final List<SkillWidget> skillWidgets = new ArrayList<>();
    private SkillData skillData;
    private int x, y;

    private String cachedHeaderText;
    private int headerWidth;

    private int lastClickX, lastClickY;
    private long lastTime;

    private void drawTree() {
        for(DisplayObject object : objectList) {
            Vec2Di coord = object.coord;
            if(coord.x >= x - 32 && coord.x + 16 <= x + width && coord.y >= y - 57 && coord.y <= y + height - 25) {
                int tx = coord.x - x;
                int ty = 25 + coord.y - y;
                ModUtils.renderColor(tx, ty, tx + 32, ty + 32, 0.0F, 0.0F, 0.0F, 1.0F);
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder builder = tessellator.getBuffer();
                GlStateManager.disableTexture2D();
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
                builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
                if(object.obtained) {
                    builder.pos(tx + 1, ty + 31, 0).color(0.6F, 0.6F, 0.6F, 1.0F).endVertex();
                    builder.pos(tx + 31, ty + 31, 0).color(0.5F, 0.5F, 0.5F, 1.0F).endVertex();
                    builder.pos(tx + 31, ty + 1, 0).color(0.6F, 0.6F, 0.6F, 1.0F).endVertex();
                    builder.pos(tx + 1, ty + 1, 0).color(0.7F, 0.7F, 0.7F, 1.0F).endVertex();
                } else {
                    builder.pos(tx + 1, ty + 31, 0).color(0.3F, 0.3F, 0.3F, 1.0F).endVertex();
                    builder.pos(tx + 31, ty + 31, 0).color(0.2F, 0.2F, 0.2F, 1.0F).endVertex();
                    builder.pos(tx + 31, ty + 1, 0).color(0.3F, 0.3F, 0.3F, 1.0F).endVertex();
                    builder.pos(tx + 1, ty + 1, 0).color(0.4F, 0.4F, 0.4F, 1.0F).endVertex();
                }
                tessellator.draw();
                GlStateManager.enableTexture2D();
                GlStateManager.shadeModel(GL11.GL_FLAT);
                Item icon = object.entry.iconItem;
                if(icon == null) continue;
                mc.getRenderItem().renderItemIntoGUI(new ItemStack(icon), tx + 8, ty + 8);
            }
        }
    }

    private void drawUnlockables() {
        for(SkillWidget widget : skillWidgets) {
            widget.draw();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        drawBackground(0);
        long time = System.currentTimeMillis();
        boolean diff = time - lastTime > 500L;
        for(Pair<DisplayObject, DisplayObject> line : lines) {
            Vec2Di pos1 = line.getLeft().coord;
            Vec2Di pos2 = line.getRight().coord;
            int x1 = pos1.x - x + 16;
            int y1 = pos1.y - y + 41;
            int x2 = pos2.x - x + 16;
            int y2 = pos2.y - y + 41;
            boolean unlocked = line.getLeft().obtained && line.getRight().obtained;
            float r = unlocked ? 1.0F : 0.3F;
            float g = unlocked ? 1.0F : 0.3F;
            float b = unlocked ? 1.0F : 0.3F;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuffer();
            GlStateManager.disableTexture2D();
            GlStateManager.glLineWidth(5);
            builder.begin(3, DefaultVertexFormats.POSITION_COLOR);
            builder.pos(x1, y1, 0).color(r, g, b, 1.0F).endVertex();
            builder.pos(x2, y2, 0).color(r, g, b, 1.0F).endVertex();
            tessellator.draw();
            if(diff && unlocked) {
                GlStateManager.glLineWidth(3);
                builder.begin(3, DefaultVertexFormats.POSITION_COLOR);
                builder.pos(x1, y1, 0).color(0.5F, 1.0F, 0.0F, 1.0F).endVertex();
                builder.pos(x2, y2, 0).color(0.5F, 1.0F, 0.0F, 1.0F).endVertex();
                tessellator.draw();
            }
            GlStateManager.enableTexture2D();
            GlStateManager.glLineWidth(1);
        }
        this.drawTree();
        this.drawUnlockables();
        ModUtils.renderColor(0, 0, width, 25, 0, 0, 0, 0.4f);
        FontRenderer renderer = mc.fontRenderer;
        renderer.drawStringWithShadow(cachedHeaderText, (width - headerWidth) / 2f, 8, 0xFFFFFFFF);
        Map<GunItem, SkillData.KillData> killData = skillData.killCount;
        int count = killData.size();
        int killDataY = height - 5 - 12 * count;
        int id = 0;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, 101);
        for(Map.Entry<GunItem, SkillData.KillData> entry : killData.entrySet()) {
            String name = entry.getKey().getRegistryName().getResourcePath();
            name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
            SkillData.KillData data = entry.getValue();
            String text = String.format("%s - %d kills / %d points", name, data.getKillCount(), data.getSkillPoints());
            mc.fontRenderer.drawStringWithShadow(text, 10, killDataY + id * 12, 0xFFFFFF);
            ++id;
        }
        GlStateManager.popMatrix();
        int mx = x + mouseX;
        int my = y + mouseY - 25;
        for(DisplayObject object : objectList) {
            Vec2Di c = object.coord;
            if(mx >= c.x && mx <= c.x + 32 && my >= c.y && my <= c.y + 32) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0, 102);
                drawHovereredInfo(object, mouseX, mouseY);
                GlStateManager.popMatrix();
                break;
            }
        }
        for(SkillWidget widget : skillWidgets) {
            Vec2Di c = widget.type.skillTreeVec;
            if(mx >= c.x && mx <= c.x + 32 && my >= c.y && my <= c.y + 32) {
                widget.renderHoveredInfo(mx, my, width, height);
            }
        }
        if(time - lastTime > 1000) {
            lastTime = time;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0) {
            this.setLastClickedPos(mouseX, mouseY);
            int mx = x + mouseX;
            int my = y + mouseY - 25;
            for(SkillWidget widget : skillWidgets) {
                Vec2Di c = widget.type.skillTreeVec;
                if(mx >= c.x && mx <= c.x + 32 && my >= c.y && my <= c.y + 32) {
                    boolean sound = widget.handleClicked();
                    if(sound) mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    break;
                }
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if(clickedMouseButton == 0) {
            int x = lastClickX - mouseX;
            int y = lastClickY - mouseY;
            this.x = ModUtils.wrap(this.x + x, 0, 1000);
            this.y = ModUtils.wrap(this.y + y, 0, 1500);
            this.setLastClickedPos(mouseX, mouseY);
        }
    }

    @Override
    public void initGui() {
        this.onDataUpdate();
        FontRenderer renderer = mc.fontRenderer;
        cachedHeaderText = String.format("\u00a7l%s's skill tree", mc.player.getName());
        headerWidth = renderer.getStringWidth(cachedHeaderText);
    }

    public void onDataUpdate() {
        this.skillData = PlayerDataFactory.get(mc.player).getSkillData();
        this.skillWidgets.clear();
        objectList.clear();
        PlayerSkillTree tree = skillData.getSkillTree();
        SkillTreeEntry entry = tree.getObtained().get(0).getType();
        int row = 0;
        int column = 0;
        DisplayObject object = new DisplayObject(entry, new Vec2Di(60, 30), true);
        objectList.add(object);
        for(SkillTreeEntry treeEntry : entry.child) {
            populate(treeEntry, row + 1, column, ModUtils.contains(treeEntry.registryName, tree.getObtained(), this::eq), tree, object);
        }
        for(Ability.UnlockableType type : Ability.UNLOCKABLE_ABILITY_TYPES) {
            if(type.levelRequirement.test(skillData.getSkillTree().getObtained())) {
                skillWidgets.add(this.new SkillWidget(type));
            }
        }
    }

    private void drawHovereredInfo(DisplayObject entry, int mouseX, int mouseY) {
        SkillTreeEntry treeEntry = entry.entry;
        String title = treeEntry.displayName.getFormattedText();
        String desc = treeEntry.description.getFormattedText();
        int titleWidth = mc.fontRenderer.getStringWidth(title);
        int descWidth = mc.fontRenderer.getStringWidth(desc);
        int width = Math.max(titleWidth, descWidth) + 6;
        boolean obt = entry.obtained;
        float r = obt ? 0.0F : 0.8F;
        float g = obt ? 0.8F : 0.0F;
        float b = obt ? 0.2F : 0.0F;
        if(mouseX + width > this.width) {
            ModUtils.renderTextureWithColor(mouseX - width - 3, mouseY + 10, mouseX, mouseY + 40, SKILL_TREE_TEXTURES, Math.max(r - 0.3F, 0.0F), Math.max(g - 0.3F, 0.0F), Math.max(b - 0.3F, 0.0F), 1.0F);
            ModUtils.renderTextureWithColor(mouseX - width - 3, mouseY, mouseX, mouseY + 20, SKILL_TREE_TEXTURES, r, g, b, 1.0F);
            mc.fontRenderer.drawStringWithShadow(treeEntry.displayName.getFormattedText(), mouseX - width + 3, mouseY + 6, 0xffffff);
            mc.fontRenderer.drawStringWithShadow(treeEntry.description.getFormattedText(), mouseX - width + 3, mouseY + 25, 0xffffff);
        } else {
            ModUtils.renderTextureWithColor(mouseX + 3, mouseY + 10, mouseX + width + 3, mouseY + 40, SKILL_TREE_TEXTURES, Math.max(r - 0.3F, 0.0F), Math.max(g - 0.3F, 0.0F), Math.max(b - 0.3F, 0.0F), 1.0F);
            ModUtils.renderTextureWithColor(mouseX + 3, mouseY, mouseX + width + 3, mouseY + 20, SKILL_TREE_TEXTURES, r, g, b, 1.0F);
            mc.fontRenderer.drawStringWithShadow(treeEntry.displayName.getFormattedText(), mouseX + 6, mouseY + 6, 0xffffff);
            mc.fontRenderer.drawStringWithShadow(treeEntry.description.getFormattedText(), mouseX + 6, mouseY + 25, 0xffffff);
        }
    }

    private void populate(SkillTreeEntry treeEntry, int row, int col, boolean contains, PlayerSkillTree tree, DisplayObject parent) {
        DisplayObject object = new DisplayObject(treeEntry, new Vec2Di(60 + col * 256, 30 + row * 64), contains);
        objectList.add(object);
        lines.add(Pair.of(parent, object));
        if(contains) {
            boolean b0 = treeEntry.child.length > 0;
            boolean b1 = treeEntry.child.length > 1;
            if(!b0) return;
            populate(treeEntry.child[0], row + 1, col, ModUtils.contains(treeEntry.child[0].registryName, tree.getObtained(), this::eq), tree, object);
            if(b1) {
                populate(treeEntry.child[1], row, col + 1, ModUtils.contains(treeEntry.child[1].registryName, tree.getObtained(), this::eq), tree, object);
            }
        }
    }

    private boolean eq(ResourceLocation location, EntryInstance instance) {
        return instance.getType().registryName.equals(location);
    }

    private void setLastClickedPos(int x, int y) {
        this.lastClickX = x;
        this.lastClickY = y;
    }

    private static class DisplayObject {

        private final SkillTreeEntry entry;
        private final Vec2Di coord;
        private final boolean obtained;

        public DisplayObject(SkillTreeEntry entry, Vec2Di coord, boolean obtained) {
            this.entry = entry;
            this.coord = coord;
            this.obtained = obtained;
        }
    }

    private class SkillWidget {

        private final Ability.UnlockableType type;
        // 0 = locked, 1 = disabled, 2 = active
        private int state = 0;
        int width;
        int height;
        String price;

        public SkillWidget(Ability.UnlockableType type) {
            this.type = type;
            update();
            boolean f = type.price == 1;
            price = type.price == 0 ? "This skill can be unlocked for free" : "Costs " + type.price + (f ? " skillpoint" : " skillpoints");
            String title = type.title;
            String[] desc = type.lines;
            width = checkWidth(price);
            width = checkWidth(title);
            for(String line : desc) {
                width = checkWidth(line);
            }
            height = desc.length + 1;
        }

        int checkWidth(String line) {
            FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
            return Math.max(width, renderer.getStringWidth(line));
        }

        public boolean handleClicked() {
            AbilityData data = GuiSkillTree.this.skillData.getAbilityData();
            switch (state) {
                case 0: {
                    boolean b = data.canPurchase(type);
                    if(b) NetworkManager.toServer(new SPacketAbilityUpdate(SPacketAbilityUpdate.PacketInfoType.UNLOCK, type));
                    update();
                    return b;
                }
                case 1: case 2: {
                    NetworkManager.toServer(new SPacketAbilityUpdate(SPacketAbilityUpdate.PacketInfoType.TOGGLE, type));
                    update();
                    return true;
                }
                default: return false;
            }
        }

        public void draw() {
            Vec2Di coord = type.skillTreeVec;
            int x = coord.x - GuiSkillTree.this.x;
            int y = 25 + coord.y - GuiSkillTree.this.y;
            if(x < -32 || y < -32) return;
            ModUtils.renderColor(x, y, x + 32, y + 32, 0, 0, 0, 1.0F);
            switch (state) {
                case 0: {
                    ModUtils.renderColor(x + 1, y + 1, x + 31, y + 31, 0.2F, 0.2F, 0.2F, 1.0F);
                    break;
                }
                case 1: {
                    ModUtils.renderColor(x + 1, y + 1, x + 31, y + 31, 0.75F, 0.75F, 0.75F, 1.0F);
                    break;
                }
                case 2: {
                    ModUtils.renderColor(x + 1, y + 1, x + 31, y + 31, 0.0F, 0.8F, 0.2F, 1.0F);
                    break;
                }
            }
            if(type.icon != null) {
                ModUtils.renderTexture(x + 8, y + 8, x + 24, y + 24, type.icon);
            }
        }

        public void renderHoveredInfo(int mx, int my, int width, int height) {
            float r = state == 0 ? 0.25F : state == 1 ? 0.65F : 0.0F;
            float g = state == 0 ? 0.25F : state == 1 ? 0.65F : 0.8F;
            float b = state == 0 ? 0.25F : state == 1 ? 0.65F : 0.1F;
            float a = 1.0F;
            int x = mx - GuiSkillTree.this.x;
            int y = my - GuiSkillTree.this.y;
            FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
            if(x + this.width > width) {
                ModUtils.renderTextureWithColor(x - this.width - 9, y + 35, x, y + 47 + 14 * this.height, SKILL_TREE_TEXTURES, Math.max(0.0F, r - 0.1F), Math.max(0.0F, g - 0.1F), Math.max(0.0F, b - 0.1F), a);
                ModUtils.renderTextureWithColor(x - this.width - 9, y + 25, x, y + 45, SKILL_TREE_TEXTURES, r, g, b, a);
                renderer.drawStringWithShadow(type.title, x - this.width - 3, y + 31, 0xFFFFFF);
                for(int i = 0; i < type.lines.length; i++) {
                    renderer.drawStringWithShadow(type.lines[i], x - this.width - 3, y + 50 + i * 12, 0xFFFFFF);
                }
                renderer.drawStringWithShadow(price, x - this.width - 3, y + 50 + (this.height - 1) * 12, 0xFFFFFF);
            } else {
                ModUtils.renderTextureWithColor(x + 3, y + 35, x + 12 + this.width, y + 47 + 14 * this.height, SKILL_TREE_TEXTURES, Math.max(0.0F, r - 0.1F), Math.max(0.0F, g - 0.1F), Math.max(0.0F, b - 0.1F), a);
                ModUtils.renderTextureWithColor(x + 3, y + 25, x + 12 + this.width, y + 45, SKILL_TREE_TEXTURES, r, g, b, a);
                renderer.drawStringWithShadow(type.title, x + 9, y + 31, 0xFFFFFF);
                for(int i = 0; i < type.lines.length; i++) {
                    renderer.drawStringWithShadow(type.lines[i], x + 9, y + 50 + i * 12, 0xFFFFFF);
                }
                renderer.drawStringWithShadow(price, x + 9, y + 50 + (this.height - 1) * 12, 0xFFFFFF);
            }
        }

        public void update() {
            EntityPlayer player = Minecraft.getMinecraft().player;
            AbilityData data = PlayerDataFactory.get(player).getSkillData().getAbilityData();
            Ability ability = data.unlockedSkills.get(type);
            state = ability == null ? 0 : ability.enabled ? 2 : 1;
        }
    }
}

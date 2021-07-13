package dev.toma.gunsrpg.client.gui.skills;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.gui.GuiConfirmSkillUnlock;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.GunData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skills.core.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.interfaces.Clickable;
import dev.toma.gunsrpg.common.skills.interfaces.OverlayRenderer;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.math.Vec2i;
import dev.toma.gunsrpg.util.object.OptionalObject;
import dev.toma.gunsrpg.util.object.Pair;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class GuiPlayerSkills extends Screen {

    private static final ResourceLocation SKILL_TREE_TEXTURES = GunsRPG.makeResource("textures/icons/skill_tree_bg.png");
    private final List<Component> uiComponents = new ArrayList<>();
    private SkillCategory displayedCategory;
    private String headerText;
    private int headerWidth;
    private Tree displayTree;
    private int posX, posY;
    private int clickedX, clickedY;
    private final OptionalObject<GunDisplay> display = OptionalObject.empty();
    private PlayerSkills skills;

    public GuiPlayerSkills() {
        this(SkillCategory.GUN);
    }

    public GuiPlayerSkills(SkillCategory category) {
        super(new StringTextComponent("screen.playerSkills"));
        this.displayedCategory = category;
    }

    @Override
    public void init() {
        uiComponents.clear();
        headerText = String.format("%s's skill tree", minecraft.player.getName());
        headerWidth = minecraft.font.width(headerText);
        Tree tree = SkillTreePlacement.treeMap.get(displayedCategory);
        PlayerEntity player = minecraft.player;
        skills = PlayerDataFactory.getUnsafe(player).getSkills();
        int level = skills.getLevel();
        for(Branch branch : tree.branches) {
            for(PlacementContext ctx : branch.getPlacements()) {
                if(ctx.type.levelRequirement <= level) {
                    addComponent(new SkillComponent(ctx));
                }
            }
        }
        this.placeCategoryButtons();
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);

        for(Component component : uiComponents) {
            component.draw(stack, minecraft, mouseX, mouseY, partialTicks, posX, posY);
        }
        display.ifPresent(dsp -> dsp.draw(stack, minecraft, mouseX, mouseY, partialTicks, posX, posY));
        // HEADER
        ModUtils.renderColor(0, 0, width, 25, 0, 0, 0, 0.4f);
        font.drawShadow(stack, TextFormatting.UNDERLINE + headerText, (width - headerWidth) / 2f, 8, 0xFFFFFFFF);
        // FOOTER
        ModUtils.renderColor(0, height - 20, width, height, 0, 0, 0, 0.4F);
        int level = skills.getLevel();
        int killed = skills.getKills();
        int required = skills.getRequiredKills();
        float decimal = skills.isMaxLevel() ? 1.0F : killed / (float) required;
        String foot = skills.isMaxLevel() ? String.format("Level: %d - %d kills", level, killed) : String.format("Level: %d (%d%%) - %d / %d kills", level, (int)(decimal * 100), killed, required);
        // level progress
        int length = width / 2;
        ModUtils.renderColor(15, height - 15, length, height - 5, 0.0F, 0.0F, 0.0F, 1.0F);
        ModUtils.renderColor(17, height - 13, (int)(decimal * (length - 2)), height - 7, 0.0F, 1.0F, 1.0F, 1.0F);
        font.drawShadow(stack, foot, 4 + length, height - 14, 0xffffff);
        String text2 = skills.getSkillPoints() + " pts";
        font.drawShadow(stack, text2, width - font.width(text2) - 5, height - 14, 0xffff00);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if(button == 0) {
            int x = (int) (clickedX - mouseX);
            int y = (int) (clickedY - mouseY);
            if(hasShiftDown()) {
                x *= 2;
                y *= 2;
            }
            posX = ModUtils.clamp(posX + x, 0, 350);
            posY = ModUtils.clamp(posY + y, 0, 400);
            setClickPos((int) mouseX, (int) mouseY);
            display.clear();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(button == 0) setClickPos((int) mouseX, (int) mouseY);
        if(display.isPresent()) {
            if(display.get().isMouseOver(mouseX + posX, mouseY + posY)) {
                display.get().mouseClicked(mouseX, mouseY, posX, posY, button);
                return true;
            }
        }
        boolean clicked = false;
        for(Component component : uiComponents) {
            if(component.isMouseOver(mouseX, mouseY, posX, posY) && component.onClick(button)) {
                minecraft.getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                init(minecraft, width, height);
                clicked = true;
                break;
            }
        }
        return clicked;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void setClickPos(int mouseX, int mouseY) {
        this.clickedX = mouseX;
        this.clickedY = mouseY;
    }

    private void switchCategory(SkillCategory category) {
        this.display.clear();
        this.displayedCategory = category;
        this.posX = 0;
        this.posY = 0;
        this.setClickPos(0, 0);
        init(minecraft, width, height);
    }

    private void placeCategoryButtons() {
        SkillCategory[] categories = SkillCategory.mainCategories();
        int btnWidth = width / categories.length;
        int missedPixels = width - ((categories.length) * btnWidth);
        for (int i = 0; i < categories.length; i++) {
            int px = i * btnWidth;
            boolean last = i == categories.length - 1;
            addComponent(new CategoryComponent(px, 25, last ? btnWidth + missedPixels : btnWidth, 20, categories[i]));
        }
    }

    private void addComponent(Component component) {
        this.uiComponents.add(component);
    }

    private static class Component {
        protected final int x, y, w, h;
        protected boolean hovered;

        public Component(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        public void draw(MatrixStack stack, Minecraft mc, int mouseX, int mouseY, float partialTicks, int offsetX, int offsetY) {
            hovered = isMouseOver(mouseX, mouseY, offsetX, offsetY);
            ModUtils.renderColor(x, y, x + w, y + h, 0.0F, 0.0F, 0.0F, 0.4F);
            if (hovered) ModUtils.renderColor(x, y, x + w, y + h, 1.0F, 1.0F, 1.0F, 0.4F);
        }

        public boolean isMouseOver(double mouseX, double mouseY, int px, int py) {
            return mouseX >= this.x && mouseX <= this.x + this.w && mouseY >= this.y && mouseY <= this.y + this.h;
        }

        public boolean onClick(int mouseButton) {
            return false;
        }

        public void drawCenteredString(FontRenderer renderer, MatrixStack stack, String text, int x, int y, int color) {
            renderer.drawShadow(stack, text, (float) (x - renderer.width(text) / 2), (float) y, color);
        }
    }

    private class SkillComponent extends Component {

        private final PlacementContext ctx;
        private final SkillType<?> type;
        private final List<Pair<Vec2i, Vec2i>> lines = new ArrayList<>();
        private final boolean obtained;
        private long hoverStartTime;
        private boolean hasStartedCounting;
        private final ITextComponent[] comments;
        private int infoWidth;
        private OverlayRenderer renderer;

        @SuppressWarnings({"UseBulkOperation", "ManualArrayToCollectionCopy"})
        public SkillComponent(PlacementContext context) {
            super(context.pos.x(), context.pos.y(), 20, 20);
            this.ctx = context;
            this.type = context.type;
            Vec2i parentPos = context.parentPos != null ? context.parentPos.get() : null;
            if(parentPos != null) {
                Vec2i p1 = context.pos;
                if(parentPos.x() == context.pos.x()) {
                    lines.add(Pair.of(new Vec2i(p1.x() + w / 2, p1.y()), new Vec2i(parentPos.x() + w / 2, parentPos.y() + h)));
                } else {
                    int heightDiffDivided = Math.abs((parentPos.y() + 20) - p1.y()) / 2;
                    int distanceBetweenSkills = Node.spacingHorizontal;
                    lines.add(Pair.of(new Vec2i(p1.x() + w / 2, p1.y()), new Vec2i(p1.x() + w / 2, p1.y() - heightDiffDivided)));
                    lines.add(Pair.of(new Vec2i(p1.x() + w / 2, p1.y() - heightDiffDivided), new Vec2i(parentPos.x() + w / 2, p1.y() - heightDiffDivided)));
                }
            }
            this.obtained = PlayerDataFactory.hasActiveSkill(Minecraft.getInstance().player, type);
            List<ITextComponent> components = new ArrayList<>();
            for(ITextComponent itc : type.getDescription()) {
                components.add(itc);
            }
            components.add(new StringTextComponent(TextFormatting.AQUA + "Unlocks at level " + type.levelRequirement));
            components.add(new StringTextComponent(TextFormatting.RED + "Costs " + type.price + " points"));
            if(obtained && PlayerDataFactory.getSkill(Minecraft.getInstance().player, type) instanceof Clickable) {
                components.add(new StringTextComponent(TextFormatting.YELLOW + "Left-click to use this skill"));
            }
            if(type.hasCustomRenderFactory()) {
                components.add(new StringTextComponent(TextFormatting.YELLOW + "Right-click to view unlockable skills"));
            }
            this.comments = components.toArray(new ITextComponent[0]);
            for(ITextComponent component : comments) {
                int cw = Minecraft.getInstance().font.width(component);
                if(cw > infoWidth) {
                    infoWidth = cw;
                }
            }
            if(obtained) {
                ISkill skill = PlayerDataFactory.getSkill(Minecraft.getInstance().player, type);
                if(skill instanceof OverlayRenderer) {
                    renderer = (OverlayRenderer) skill;
                }
            }
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY, int px, int py) {
            return super.isMouseOver(mouseX + px, mouseY + py, 0, 0);
        }

        @Override
        public boolean onClick(int mouseButton) {
            if(mouseButton == 1 && type.hasCustomRenderFactory()) {
                GuiPlayerSkills.this.display.map(new GunDisplay(ctx));
                return true;
            } else if(mouseButton == 0) {
                PlayerEntity player = Minecraft.getInstance().player;
                if(obtained) {
                    ISkill skill = PlayerDataFactory.getSkill(player, type);
                    if(skill instanceof Clickable) {
                        ((Clickable) skill).clientHandleClicked();
                        return true;
                    }
                } else {
                    if(ctx.parent != null) {
                        if(PlayerDataFactory.hasActiveSkill(player, ctx.parent) && type.getCriteria().isUnlockAvailable(PlayerDataFactory.getUnsafe(player), type)) {
                            minecraft.setScreen(new GuiConfirmSkillUnlock(GuiPlayerSkills.this, type, ctx));
                            return true;
                        }
                    } else if(type.getCriteria().isUnlockAvailable(PlayerDataFactory.getUnsafe(player), type)) {
                        minecraft.setScreen(new GuiConfirmSkillUnlock(GuiPlayerSkills.this, type, null));
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public void draw(MatrixStack stack, Minecraft mc, int mouseX, int mouseY, float partialTicks, int offsetX, int offsetY) {
            int px = x - offsetX;
            int py = y - offsetY;
            for (Pair<Vec2i, Vec2i> line : lines) {
                int x1 = line.getLeft().x() - offsetX;
                int y1 = line.getLeft().y() - offsetY;
                int x2 = line.getRight().x() - offsetX;
                int y2 = line.getRight().y() - offsetY;
                ModUtils.renderLine(x1, y1, x2, y2, 0.0F, 0.0F, 0.0F, 1.0F, 8);
                ModUtils.renderLine(x1, y1, x2, y2, 1.0F, 1.0F, 1.0F, 1.0F, 2);
            }
            hovered = isMouseOver(offsetX + mouseX, offsetY + mouseY, 0, 0);
            ModUtils.renderColor(px, py, px + w, py + h, 0.0F, 0.0F, 0.0F, 1.0F);
            if(obtained) {
                ModUtils.renderColor(px + 1, py + 1, px + w - 1, py + h - 1, 0.25F, 0.75F, 0.1F, 1.0F);
            } else {
                ModUtils.renderColor(px + 1, py + 1, px + w - 1, py + h - 1, 0.25F, 0.25F, 0.25F, 1.0F);
            }
            if(type.hasCustomRenderFactory()) {
                mc.getItemRenderer().renderGuiItem(type.getRenderItem(), px + 2, py + 2);
            } else {
                ModUtils.renderTexture(px + 2, py + 2, px + w - 2, py + h - 2, type.icon);
            }
            if(renderer != null) {
                renderer.drawOnTop(px, py, w, h);
            }
            if(type.isFreshUnlock()) {
                ModUtils.renderColor(px + 17, py, px + 20, py + 3, 1.0F, 1.0F, 0.0F, 1.0F);
            }
            if(hovered) {
                if(type.isFreshUnlock()) {
                    type.setFresh(false);
                }
                if(!hasStartedCounting || System.currentTimeMillis() - hoverStartTime <= 1000L) {
                    stack.pushPose();
                    stack.translate(0, 0, 1);
                    String displayName = type.getDisplayName();
                    int width = mc.font.width(displayName);
                    ModUtils.renderColor(5 + px - width / 2, py + h + 2, 15 + px + width / 2, py + h + 16, 0.0F, 0.0F, 0.0F, 1.0F);
                    mc.font.drawShadow(stack, displayName, px + (20 - width) / 2.0F, py + h + 5, 0xffffff);
                    stack.popPose();
                }

                if(!hasStartedCounting) {
                    hoverStartTime = System.currentTimeMillis();
                    hasStartedCounting = true;
                } else if(System.currentTimeMillis() - hoverStartTime > 1000L) {
                    stack.pushPose();
                    stack.translate(0, 0, 150);
                    int left = mouseX + infoWidth > GuiPlayerSkills.this.width ? mouseX - infoWidth - 10 : mouseX;
                    int top = mouseY + 5 + comments.length * 12 > GuiPlayerSkills.this.height ? mouseY - comments.length * 12 : mouseY + 5;
                    int panelHeight = comments.length * 12 + 5;
                    ModUtils.renderTextureWithColor(left, top, left + infoWidth + 10, top + panelHeight, SKILL_TREE_TEXTURES, 0.25F, obtained ? 0.75F : 0.25F, obtained ? 0.1F : 0.25F, 1.0F);
                    for(int i = 0; i < comments.length; i++) {
                        mc.font.drawShadow(stack, comments[i], left + 5, top + 5 + 12 * i, 0xffffff);
                    }
                    stack.popPose();
                }
            } else {
                hasStartedCounting = false;
            }
        }
    }

    private class CategoryComponent extends Component {
        private final SkillCategory category;
        private final boolean selected;

        public CategoryComponent(int x, int y, int w, int h, SkillCategory category) {
            super(x, y, w, h);
            this.category = category;
            this.selected = GuiPlayerSkills.this.displayedCategory == category;
        }

        @Override
        public void draw(MatrixStack stack, Minecraft mc, int mouseX, int mouseY, float partialTicks, int offsetX, int offsetY) {
            super.draw(stack, mc, mouseX, mouseY, partialTicks, offsetX, offsetY);
            Tree tree = SkillTreePlacement.treeMap.get(this.category);
            boolean flag = false;
            for (Branch branch : tree.branches) {
                for (PlacementContext ctx : branch.getPlacements()) {
                    if(ctx.type.isFreshUnlock()) {
                        flag = true;
                        break;
                    }
                }
            }
            if(flag) {
                ModUtils.renderColor(this.x + this.w - 3, this.y, this.x + this.w, this.y + 3, 1.0F, 1.0F, 0.0F, 1.0F);
            }
            if(selected) {
                fadeAway(x, y, x + 2, y + h);
                ModUtils.renderColor(x, y + h - 2, x + w, y + h, 1.0F, 1.0F, 1.0F, 1.0F);
                fadeAway(x + w - 2, y, x + w, y + h);
            }
            this.drawCenteredString(mc.font, stack, this.category.name(), this.x + this.w / 2, this.y + (this.h - 8) / 2, hovered ? 0xffff88 : 0xffffff);
        }

        @Override
        public boolean onClick(int mouseButton) {
            if(mouseButton != 0) return false;
            if(GuiPlayerSkills.this.displayedCategory != this.category) {
                GuiPlayerSkills.this.switchCategory(category);
                return true;
            }
            return false;
        }

        private void fadeAway(int x1, int y1, int x2, int y2) {
            float fadeMin = 0.1F;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.getBuilder();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
            builder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            builder.vertex(x1, y2, 0).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            builder.vertex(x2, y2, 0).color(1.0F, 1.0F, 1.0F, 1.0F).endVertex();
            builder.vertex(x2, y1, 0).color(1.0F, 1.0F, 1.0F, fadeMin).endVertex();
            builder.vertex(x1, y1, 0).color(1.0F, 1.0F, 1.0F, fadeMin).endVertex();
            builder.end();
            WorldVertexBufferUploader.end(builder);
            RenderSystem.shadeModel(GL11.GL_FLAT);
            RenderSystem.disableBlend();
            RenderSystem.enableTexture();
        }
    }

    private class GunDisplay {
        private final PlacementContext ctx;
        private final GunItem gun;
        private int x, y, w, h;
        private final List<Component> list = new ArrayList<>();
        private GunData gunData;

        public GunDisplay(PlacementContext ctx) {
            this.ctx = ctx;
            this.gun = (GunItem) ctx.type.getRenderItem().getItem();
            init();
        }

        public boolean isMouseOver(double mouseX, double mouseY) {
            return mouseX >= this.x && mouseX <= this.x + this.w && mouseY >= this.y && mouseY <= this.y + this.h;
        }

        public void mouseClicked(double mouseX, double mouseY, int xOff, int yOff, int mouseButton) {
            for(Component component : list) {
                if(component.isMouseOver(mouseX, mouseY, xOff, yOff) && component.onClick(mouseButton)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    MainWindow window = Minecraft.getInstance().getWindow();
                    GuiPlayerSkills.this.init(Minecraft.getInstance(), window.getGuiScaledWidth(), window.getGuiScaledHeight());
                    break;
                }
            }
        }

        public void init() {
            list.clear();
            int width = ctx.type.getChilds().size() * 25 + 15;
            Vec2i pos = ctx.pos;
            this.x = pos.x() - width / 2 + 10;
            this.y = pos.y() + 25;
            this.w = width;
            this.h = 60;
            int px = x - GuiPlayerSkills.this.posX;
            int py = y - GuiPlayerSkills.this.posY;
            for(int i = 0; i < ctx.type.getChilds().size(); i++) {
                SkillType<?> child = ctx.type.getChilds().get(i);
                list.add(new SkillComponent(new PlacementContext(null, child, new Vec2i(x + 10 + 25 * i, y + 10))));
            }
            this.gunData = PlayerDataFactory.getUnsafe(Minecraft.getInstance().player).getSkills().getGunData(gun);
        }

        public void draw(MatrixStack stack, Minecraft mc, int mouseX, int mouseY, float partialTicks, int xOffset, int yOffset) {
            int px = x - xOffset;
            int py = y - yOffset;
            ModUtils.renderColor(px, py, px + w, py + h, 0.0F, 0.0F, 0.0F, 1.0F);
            ModUtils.renderColor(px + 1, py + 1, px + w - 1, py + h - 1, 0.5F, 0.5F, 0.5F, 1.0F);
            String txt;
            if(gunData.isAtMaxLevel()) {
                txt = String.format("Level %d, kills %d", gunData.getLevel(), gunData.getKills());
            } else {
                txt = String.format("Level %d, kills %d/%d", gunData.getLevel(), gunData.getKills(), gunData.getRequiredKills());
            }
            mc.font.drawShadow(stack, txt, px + 10, py + h - 24, 0xffffff);
            String txt2 = gunData.getGunPoints() + " pts";
            mc.font.drawShadow(stack, txt2, px + w - 10 - mc.font.width(txt2), py + h - 24, 0xffff00);
            ModUtils.renderColor(px + 10, py + h - 15, px + w - 10, py + h - 5, 0.0F, 0.0F, 0.0F, 1.0F);
            float levelPrg = gunData.isAtMaxLevel() ? 1.0F : ((float) gunData.getKills() / gunData.getRequiredKills());
            ModUtils.renderColor(px + 12, py + h - 13, (int)(px + (w - 12) * levelPrg), py + h - 7, 1.0F, 1.0F, 0.0F, 1.0F);
            list.forEach(component -> component.draw(stack, mc, mouseX, mouseY, partialTicks, xOffset, yOffset));
        }
    }
}

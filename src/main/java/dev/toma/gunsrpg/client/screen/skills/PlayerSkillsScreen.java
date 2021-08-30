package dev.toma.gunsrpg.client.screen.skills;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.screen.ConfirmSkillUnlockScreen;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.object.GunData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.api.common.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.api.common.IClickableSkill;
import dev.toma.gunsrpg.api.common.IOverlayRender;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.RenderUtils;
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
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class PlayerSkillsScreen extends Screen {

    private static final ResourceLocation SKILL_TREE_TEXTURES = GunsRPG.makeResource("textures/icons/skill_tree_bg.png");
    private final List<Component> uiComponents = new ArrayList<>();
    private final List<CategoryComponent> categories = new ArrayList<>();
    private final List<SkillComponent> skillElements = new ArrayList<>();
    private final OptionalObject<GunDisplay> display = OptionalObject.empty();
    private SkillCategory displayedCategory;
    private String headerText;
    private int headerWidth;
    private Tree displayTree;
    private int posX, posY;
    private int clickedX, clickedY;
    private PlayerSkills skills;
    private SkillComponent hoveredSkill;
    private long hoverStartTime;
    private boolean wasHovered;

    public PlayerSkillsScreen() {
        this(SkillCategory.GUN);
    }

    public PlayerSkillsScreen(SkillCategory category) {
        super(new StringTextComponent("screen.playerSkills"));
        this.displayedCategory = category;
    }

    @Override
    public void init() {
        uiComponents.clear();
        categories.clear();
        skillElements.clear();
        headerText = String.format("%s's skill tree", minecraft.player.getName().getString());
        headerWidth = minecraft.font.width(headerText);
        Tree tree = SkillTreePlacement.treeMap.get(displayedCategory);
        PlayerEntity player = minecraft.player;
        skills = PlayerData.getUnsafe(player).getSkills();
        int level = skills.getLevel();
        for (Branch branch : tree.branches) {
            for (PlacementContext ctx : branch.getPlacements()) {
                if (ctx.type.levelRequirement <= level) {
                    addComponent(new SkillComponent(ctx));
                }
            }
        }
        this.placeCategoryButtons();
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        hoveredSkill = null;
        int gradientMin = 0x66 << 24;
        int gradientMax = 0xDD << 24;
        int killed = skills.getKills();
        int required = skills.getRequiredKills();
        float decimal = skills.isMaxLevel() ? 1.0F : killed / (float) required;
        int length = width / 2;
        Matrix4f pose = stack.last().pose();
        renderSkills(stack, mouseX, mouseY, partialTicks);
        RenderUtils.drawGradient(pose, 0, 0, width, 45, 0, gradientMax, gradientMin); // header
        renderCategories(stack, mouseX, mouseY, partialTicks); // categories
        display.ifPresent(dsp -> dsp.draw(stack, minecraft, mouseX, mouseY, partialTicks, posX, posY)); // weapon skills
        renderHoverInfo(stack, mouseX, mouseY, partialTicks); // hovered data
        RenderUtils.drawGradient(pose, 0, height - 20, width, height, 0, gradientMin, gradientMax); // footer background
        RenderUtils.drawGradient(pose, 15, height - 15, length, height - 5, 0, 0xFF << 24, 0xFF << 24); // level progress background
        RenderUtils.drawGradient(pose, 17, height - 13, (int) (decimal * (length - 2)), height - 7, 0, 0xFF00FFFF, 0xFF006565); // level progress
        font.drawShadow(stack, TextFormatting.UNDERLINE + headerText, (width - headerWidth) / 2f, 8, 0xFFFFFFFF);
        int level = skills.getLevel();
        String foot = skills.isMaxLevel() ? String.format("Level: %d - %d kills", level, killed) : String.format("Level: %d (%d%%) - %d / %d kills", level, (int) (decimal * 100), killed, required);
        font.drawShadow(stack, foot, 4 + length, height - 14, 0xffffff);
        String text2 = skills.getSkillPoints() + " pts";
        font.drawShadow(stack, text2, width - font.width(text2) - 5, height - 14, 0xffff00);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0) {
            int x = (int) (clickedX - mouseX);
            int y = (int) (clickedY - mouseY);
            if (hasShiftDown()) {
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
        if (button == 0) setClickPos((int) mouseX, (int) mouseY);
        if (display.isPresent()) {
            if (display.get().isMouseOver(mouseX + posX, mouseY + posY)) {
                display.get().mouseClicked(mouseX, mouseY, posX, posY, button);
                return true;
            }
        }
        boolean clicked = false;
        for (Component component : uiComponents) {
            if (component.isMouseOver(mouseX, mouseY, posX, posY) && component.onClick(button)) {
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

    private void renderHoverInfo(MatrixStack stack, int mouseX, int mouseY, float deltaRenderTime) {
        if (hoveredSkill == null) {
            wasHovered = false;
            return;
        }
        SkillType<?> type = hoveredSkill.type;
        if (type.isFreshUnlock()) {
            type.setFresh(false);
        }
        long msDescDisplayDelay = 1000L;
        Matrix4f pose = stack.last().pose();
        if (!wasHovered || System.currentTimeMillis() - hoverStartTime <= msDescDisplayDelay) {
            String title = type.getDisplayName();
            int width = font.width(title);
            int px = hoveredSkill.x - posX;
            int py = hoveredSkill.y - posY;
            RenderUtils.drawSolid(pose, 5 + px - width / 2, py + hoveredSkill.h + 2, 15 + px + width / 2, py + hoveredSkill.h + 16, 0xFF << 24);
            font.drawShadow(stack, title, px + (20 - width) / 2.0F, py + hoveredSkill.h + 5, 0xFFFFFF);
        }
        if (!wasHovered) {
            hoverStartTime = System.currentTimeMillis();
            wasHovered = true;
        } else if (System.currentTimeMillis() - hoverStartTime > msDescDisplayDelay) {
            ITextComponent[] comments = hoveredSkill.comments;
            int left = mouseX + hoveredSkill.infoWidth > width ? mouseX - hoveredSkill.infoWidth - 10 : mouseX;
            int top = mouseY + 5 + comments.length * 12 > height ? mouseY - comments.length * 12 : mouseY + 5;
            int panelWidth = left + hoveredSkill.infoWidth + 10;
            int panelHeight = top + comments.length * 12 + 5;
            drawDescriptionBg(left, top, panelWidth, panelHeight, comments, stack, hoveredSkill.obtained);
        }
    }

    private void drawDescriptionBg(int x1, int y1, int x2, int y2, ITextComponent[] comments, MatrixStack stack, boolean unlocked) {
        stack.pushPose();
        stack.translate(0, 0, 400);
        RenderUtils.setupColorRenderState();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.getBuilder();
        builder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        Matrix4f pose = stack.last().pose();
        RenderUtils.fillSolid(builder, pose, x1, y1, x2, y2, 0, 0xFF << 24);
        int light = unlocked ? 0xFF00DD44 : 0xFF767676;
        int dark = RenderUtils.multiplyRGB(light, 0.55);
        RenderUtils.fillGradient(builder, pose, x1 + 1, y1 + 1, x2 - 1, y2 - 1, 0, light, dark);
        tessellator.end();
        RenderUtils.resetColorRenderState();
        for (int i = 0; i < comments.length; i++) {
            font.drawShadow(stack, comments[i], x1 + 5, y1 + 5 + 12 * i, 0xFFFFFF);
        }
        stack.popPose();
    }

    private void renderCategories(MatrixStack stack, int mouseX, int mouseY, float deltaRenderTime) {
        categories.forEach(cat -> cat.draw(stack, minecraft, mouseX, mouseY, deltaRenderTime, posX, posY));
    }

    private void renderSkills(MatrixStack stack, int mouseX, int mouseY, float deltaRenderTime) {
        skillElements.forEach(el -> el.draw(stack, minecraft, mouseX, mouseY, deltaRenderTime, posX, posY));
        if (hoveredSkill == null) hoverStartTime = 0L;
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
        if (component instanceof CategoryComponent) {
            categories.add((CategoryComponent) component);
        } else if (component instanceof SkillComponent) {
            skillElements.add((SkillComponent) component);
        }
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
            //ModUtils.renderColor(stack.last().pose(), x, y, x + w, y + h, 0.0F, 0.0F, 0.0F, 0.4F);
            if (hovered) ModUtils.renderColor(stack.last().pose(), x, y, x + w, y + h, 1.0F, 1.0F, 1.0F, 0.4F);
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
        private final ITextComponent[] comments;
        private long hoverStartTime;
        private boolean hasStartedCounting;
        private int infoWidth;
        private IOverlayRender renderer;

        @SuppressWarnings({"UseBulkOperation", "ManualArrayToCollectionCopy"})
        public SkillComponent(PlacementContext context) {
            super(context.pos.x(), context.pos.y(), 20, 20);
            this.ctx = context;
            this.type = context.type;
            Vec2i parentPos = context.parentPos != null ? context.parentPos.get() : null;
            if (parentPos != null) {
                Vec2i p1 = context.pos;
                if (parentPos.x() == context.pos.x()) {
                    lines.add(Pair.of(new Vec2i(p1.x() + w / 2, p1.y()), new Vec2i(parentPos.x() + w / 2, parentPos.y() + h)));
                } else {
                    int heightDiffDivided = Math.abs((parentPos.y() + 20) - p1.y()) / 2;
                    int distanceBetweenSkills = Node.spacingHorizontal;
                    lines.add(Pair.of(new Vec2i(p1.x() + w / 2, p1.y()), new Vec2i(p1.x() + w / 2, p1.y() - heightDiffDivided)));
                    lines.add(Pair.of(new Vec2i(p1.x() + w / 2, p1.y() - heightDiffDivided), new Vec2i(parentPos.x() + w / 2, p1.y() - heightDiffDivided)));
                }
            }
            this.obtained = PlayerData.hasActiveSkill(Minecraft.getInstance().player, type);
            List<ITextComponent> components = new ArrayList<>();
            for (ITextComponent itc : type.getDescription()) {
                components.add(itc);
            }
            components.add(new StringTextComponent(TextFormatting.AQUA + "Unlocks at level " + type.levelRequirement));
            components.add(new StringTextComponent(TextFormatting.RED + "Costs " + type.price + " points"));
            if (obtained && PlayerData.getSkill(Minecraft.getInstance().player, type) instanceof IClickableSkill) {
                components.add(new StringTextComponent(TextFormatting.YELLOW + "Left-click to use this skill"));
            }
            if (type.hasCustomRenderFactory()) {
                components.add(new StringTextComponent(TextFormatting.YELLOW + "Right-click to view unlockable skills"));
            }
            this.comments = components.toArray(new ITextComponent[0]);
            for (ITextComponent component : comments) {
                int cw = Minecraft.getInstance().font.width(component);
                if (cw > infoWidth) {
                    infoWidth = cw;
                }
            }
            if (obtained) {
                ISkill skill = PlayerData.getSkill(Minecraft.getInstance().player, type);
                if (skill instanceof IOverlayRender) {
                    renderer = (IOverlayRender) skill;
                }
            }
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY, int px, int py) {
            return super.isMouseOver(mouseX + px, mouseY + py, 0, 0);
        }

        @Override
        public boolean onClick(int mouseButton) {
            if (mouseButton == 1 && type.hasCustomRenderFactory()) {
                PlayerSkillsScreen.this.display.map(new GunDisplay(ctx));
                return true;
            } else if (mouseButton == 0) {
                PlayerEntity player = Minecraft.getInstance().player;
                if (obtained) {
                    ISkill skill = PlayerData.getSkill(player, type);
                    if (skill instanceof IClickableSkill) {
                        ((IClickableSkill) skill).clientHandleClicked();
                        return true;
                    }
                } else {
                    if (ctx.parent != null) {
                        if (PlayerData.hasActiveSkill(player, ctx.parent) && type.getCriteria().isUnlockAvailable(PlayerData.getUnsafe(player), type)) {
                            minecraft.setScreen(new ConfirmSkillUnlockScreen(PlayerSkillsScreen.this, type, ctx));
                            return true;
                        }
                    } else if (type.getCriteria().isUnlockAvailable(PlayerData.getUnsafe(player), type)) {
                        minecraft.setScreen(new ConfirmSkillUnlockScreen(PlayerSkillsScreen.this, type, null));
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
            Matrix4f pose = stack.last().pose();
            for (Pair<Vec2i, Vec2i> line : lines) {
                int x1 = line.getLeft().x() - offsetX;
                int y1 = line.getLeft().y() - offsetY;
                int x2 = line.getRight().x() - offsetX;
                int y2 = line.getRight().y() - offsetY;
                ModUtils.renderLine(pose, x1, y1, x2, y2, 0.0F, 0.0F, 0.0F, 1.0F, 8);
                ModUtils.renderLine(pose, x1, y1, x2, y2, 1.0F, 1.0F, 1.0F, 1.0F, 2);
            }
            hovered = isMouseOver(offsetX + mouseX, offsetY + mouseY, 0, 0);
            if (hovered) {
                PlayerSkillsScreen.this.hoveredSkill = this;
            }
            ModUtils.renderColor(pose, px, py, px + w, py + h, 0.0F, 0.0F, 0.0F, 1.0F);
            if (obtained) {
                ModUtils.renderColor(pose, px + 1, py + 1, px + w - 1, py + h - 1, 0.25F, 0.75F, 0.1F, 1.0F);
            } else {
                ModUtils.renderColor(pose, px + 1, py + 1, px + w - 1, py + h - 1, 0.25F, 0.25F, 0.25F, 1.0F);
            }
            if (type.hasCustomRenderFactory()) {
                mc.getItemRenderer().renderGuiItem(type.getRenderItem(), px + 2, py + 2);
            } else {
                ModUtils.renderTexture(pose, px + 2, py + 2, px + w - 2, py + h - 2, type.icon);
            }
            if (renderer != null) {
                renderer.drawOnTop(stack, px, py, w, h);
            }
            if (type.isFreshUnlock()) {
                ModUtils.renderColor(pose, px + 17, py, px + 20, py + 3, 1.0F, 1.0F, 0.0F, 1.0F);
            }
        }
    }

    private class CategoryComponent extends Component {
        private final SkillCategory category;
        private final boolean selected;

        public CategoryComponent(int x, int y, int w, int h, SkillCategory category) {
            super(x, y, w, h);
            this.category = category;
            this.selected = PlayerSkillsScreen.this.displayedCategory == category;
        }

        @Override
        public void draw(MatrixStack stack, Minecraft mc, int mouseX, int mouseY, float partialTicks, int offsetX, int offsetY) {
            super.draw(stack, mc, mouseX, mouseY, partialTicks, offsetX, offsetY);
            Tree tree = SkillTreePlacement.treeMap.get(this.category);
            boolean flag = false;
            for (Branch branch : tree.branches) {
                for (PlacementContext ctx : branch.getPlacements()) {
                    if (ctx.type.isFreshUnlock()) {
                        flag = true;
                        break;
                    }
                }
            }
            if (flag) {
                ModUtils.renderColor(stack.last().pose(), this.x + this.w - 3, this.y, this.x + this.w, this.y + 3, 1.0F, 1.0F, 0.0F, 1.0F);
            }
            if (selected) {
                fadeAway(x, y, x + 2, y + h);
                ModUtils.renderColor(stack.last().pose(), x, y + h - 2, x + w, y + h, 1.0F, 1.0F, 1.0F, 1.0F);
                fadeAway(x + w - 2, y, x + w, y + h);
            }
            this.drawCenteredString(mc.font, stack, this.category.name(), this.x + this.w / 2, this.y + (this.h - 8) / 2, hovered ? 0xffff88 : 0xffffff);
        }

        @Override
        public boolean onClick(int mouseButton) {
            if (mouseButton != 0) return false;
            if (PlayerSkillsScreen.this.displayedCategory != this.category) {
                PlayerSkillsScreen.this.switchCategory(category);
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
        private final List<Component> list = new ArrayList<>();
        private int x, y, w, h;
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
            for (Component component : list) {
                if (component.isMouseOver(mouseX, mouseY, xOff, yOff) && component.onClick(mouseButton)) {
                    Minecraft.getInstance().getSoundManager().play(SimpleSound.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    MainWindow window = Minecraft.getInstance().getWindow();
                    PlayerSkillsScreen.this.init(Minecraft.getInstance(), window.getGuiScaledWidth(), window.getGuiScaledHeight());
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
            int px = x - PlayerSkillsScreen.this.posX;
            int py = y - PlayerSkillsScreen.this.posY;
            for (int i = 0; i < ctx.type.getChilds().size(); i++) {
                SkillType<?> child = ctx.type.getChilds().get(i);
                list.add(new SkillComponent(new PlacementContext(null, child, new Vec2i(x + 10 + 25 * i, y + 10))));
            }
            this.gunData = PlayerData.getUnsafe(Minecraft.getInstance().player).getSkills().getGunData(gun);
        }

        public void draw(MatrixStack stack, Minecraft mc, int mouseX, int mouseY, float partialTicks, int xOffset, int yOffset) {
            int px = x - xOffset;
            int py = y - yOffset;
            Matrix4f pose = stack.last().pose();
            ModUtils.renderColor(pose, px, py, px + w, py + h, 0.0F, 0.0F, 0.0F, 1.0F);
            ModUtils.renderColor(pose, px + 1, py + 1, px + w - 1, py + h - 1, 0.5F, 0.5F, 0.5F, 1.0F);
            String txt;
            if (gunData.isAtMaxLevel()) {
                txt = String.format("Level %d, kills %d", gunData.getLevel(), gunData.getKills());
            } else {
                txt = String.format("Level %d, kills %d/%d", gunData.getLevel(), gunData.getKills(), gunData.getRequiredKills());
            }
            mc.font.drawShadow(stack, txt, px + 10, py + h - 24, 0xffffff);
            String txt2 = gunData.getGunPoints() + " pts";
            mc.font.drawShadow(stack, txt2, px + w - 10 - mc.font.width(txt2), py + h - 24, 0xffff00);
            ModUtils.renderColor(pose, px + 10, py + h - 15, px + w - 10, py + h - 5, 0.0F, 0.0F, 0.0F, 1.0F);
            float levelPrg = gunData.isAtMaxLevel() ? 1.0F : ((float) gunData.getKills() / gunData.getRequiredKills());
            ModUtils.renderColor(pose, px + 12, py + h - 13, (int) (px + (w - 12) * levelPrg), py + h - 7, 1.0F, 1.0F, 0.0F, 1.0F);
            list.forEach(component -> component.draw(stack, mc, mouseX, mouseY, partialTicks, xOffset, yOffset));
        }
    }
}

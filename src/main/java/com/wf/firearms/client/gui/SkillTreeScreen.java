package com.wf.firearms.client.gui;

import com.wf.firearms.debuff.DebuffService;
import com.wf.firearms.data.PerkDef;
import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.data.SkillCatalog;
import com.wf.firearms.data.SkillDatabase;
import com.wf.firearms.data.SkillNode;
import com.wf.firearms.gameplay.SkillUnlockService;
import com.wf.firearms.client.ClientSkillActions;
import com.wf.firearms.network.AdjustPerkPacket;
import com.wf.firearms.network.ModNetwork;
import com.wf.firearms.network.UnlockSkillPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import com.wf.firearms.client.gui.layout.GunsRpgSkillTrees;
import com.wf.firearms.client.gui.layout.GunsRpgTree;
import com.wf.firearms.client.gui.layout.GunsRpgTree.Connector;
import com.wf.firearms.client.gui.layout.GunsRpgTree.GridPos;
import com.wf.firearms.client.gui.layout.GunsRpgTree.NodeViewData;
import com.wf.firearms.registry.ModItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Guns RPG 技能树 UI：分类标签切换整页 + {@link GunsRpgTree} 网格布局（移植自 Toma1O6/Guns-RPG）。
 */
public class SkillTreeScreen extends Screen {
    private static final String[] CATEGORIES = {"gun", "survival", "mining", "resistance"};
    private static final int HEADER_H = 28;
    private static final int CATEGORY_BAR_H = 22;
    private static final int FOOTER_H = 26;
    private static final int DETAIL_W = 132;
    private static final int DETAIL_BTN_H = 20;
    private static final int EXT_DETAIL_H = 86;
    private static final int TREE_PAD = 8;
    /** 与原版 SkillsView 一致 */
    private static final int X_UNIT = 6;
    private static final int Y_UNIT = 10;
    private static final int NODE_SIZE = 22;
    private static final int LINE_OFF = NODE_SIZE / 2;

    private FirearmsView view = FirearmsView.SKILL_TREE;
    /** 当前显示的技能列（等同 Vue 里 v-show 只亮一个） */
    private String activeCategory = "gun";
    private String selectedNodeId = "";
    private String selectedAssemblyId = "";
    /** 扩展页当前选中的配件节点 */
    private String selectedExtNodeId = "";
    private double panX;
    private double panY;
    private boolean panning;
    private double panGrabMouseX;
    private double panGrabMouseY;
    private double panGrabX;
    private double panGrabY;
    private int perkScroll;
    private String selectedPerkId = "";
    private int extListScroll;
    private int extContentScroll;

    private GunsRpgSkillTrees cachedSkillTrees;
    private String cachedTreesCategory = "";
    private int lastLayoutScreenW;
    private int lastLayoutScreenH;

    public SkillTreeScreen() {
        super(Component.translatable("screen.gunsrpg.skill_tree"));
    }

    @Override
    protected void init() {
        rebuildAllWidgets();
    }

    /** 网络同步后刷新按钮与解锁状态。 */
    public void refreshAfterNetwork() {
        rebuildAllWidgets();
    }

    private void rebuildAllWidgets() {
        rebuildHeaderButtons();
        rebuildDetailWidgets();
        rebuildExtensionWidgets();
    }

    private int detailPanelX() {
        return treeAreaRight() + 4;
    }

    private int detailButtonY() {
        return contentBottom() - DETAIL_BTN_H - 6;
    }

    private int detailTextBottom() {
        return detailButtonY() - 6;
    }

    private void rebuildExtensionWidgets() {
        if (view != FirearmsView.EXTENSIONS
                || selectedExtNodeId.isEmpty()
                || minecraft == null
                || minecraft.player == null) {
            return;
        }
        addDetailUnlockButton(
                6 + 136 + 8,
                Math.min(width - (6 + 136 + 8) - 8 - 16, 110),
                selectedExtNodeId);
    }

    private void rebuildDetailWidgets() {
        if (view != FirearmsView.SKILL_TREE || selectedNodeId.isEmpty() || minecraft == null || minecraft.player == null) {
            return;
        }
        addDetailUnlockButton(detailPanelX() + 6, DETAIL_W - 16, selectedNodeId);
    }

    /** 解锁按钮：单人直接服务端执行，多人走网络包（避免依赖额外 client 类加载链）。 */
    private void requestSkillUnlock(String skillId) {
        if (skillId == null || skillId.isEmpty() || minecraft == null || minecraft.player == null) {
            return;
        }
        var client = minecraft.player;
        MinecraftServer server = minecraft.getSingleplayerServer();
        if (server != null) {
            ServerPlayer sp = server.getPlayerList().getPlayer(client.getUUID());
            if (sp != null) {
                SkillUnlockService.UnlockResult result = SkillUnlockService.tryUnlock(sp, skillId);
                if (result.success()) {
                    PlayerFirearmsData.copyProgressFrom(sp, client);
                }
                String text = result.message() == null || result.message().isEmpty()
                        ? (result.success() ? "已解锁" : "无法解锁")
                        : result.message();
                client.displayClientMessage(
                        Component.literal((result.success() ? "§a[火器]§r " : "§c[火器]§r ") + text), false);
                refreshAfterNetwork();
                return;
            }
        }
        ModNetwork.sendToServer(new UnlockSkillPacket(skillId));
    }

    private void addDetailUnlockButton(int x, int buttonW, String skillId) {
        boolean unlocked = PlayerFirearmsData.isUnlocked(minecraft.player, skillId);
        boolean canUnlock = !unlocked
                && SkillUnlockService.tryUnlockPreview(minecraft.player, skillId).isEmpty();
        String label;
        if (unlocked) {
            label = "§a已解锁";
        } else if (canUnlock) {
            label = "§a解锁";
        } else {
            label = "§7条件不足";
        }
        Button btn = Button.builder(Component.literal(label), b -> requestSkillUnlock(skillId))
                .bounds(x, detailButtonY(), buttonW, DETAIL_BTN_H)
                .build();
        btn.active = !unlocked && canUnlock;
        addRenderableWidget(btn);
        if (unlocked && SkillCatalog.isActiveSkill(skillId)) {
            Button useBtn =
                    Button.builder(Component.literal("§e使用技能"), b -> ClientSkillActions.requestActiveSkill(skillId))
                            .bounds(x, detailButtonY() - DETAIL_BTN_H - 4, buttonW, DETAIL_BTN_H)
                            .build();
            addRenderableWidget(useBtn);
        }
    }

    private void rebuildHeaderButtons() {
        clearWidgets();
        int y = 6;
        int x = 8;
        addRenderableWidget(bookTabButton(x, y, ModItems.SKILLPOINT_BOOK.get(), FirearmsView.SKILL_TREE, "生存技能书"));
        x += 26;
        addRenderableWidget(bookTabButton(x, y, ModItems.WEAPON_BOOK.get(), FirearmsView.EXTENSIONS, "枪械技能书"));
        x += 26;
        addRenderableWidget(bookTabButton(x, y, ModItems.PERKPOINT_BOOK.get(), FirearmsView.PERKS, "属性技能书"));
        if (view == FirearmsView.SKILL_TREE) {
            addCategoryTabs();
        }
    }

    private void addCategoryTabs() {
        int y = HEADER_H + 2;
        int x = 8;
        int tabW = Math.max(52, (treeAreaRight() - 16) / CATEGORIES.length - 4);
        for (String cat : CATEGORIES) {
            String label = SkillUiText.categoryLabel(cat);
            boolean active = cat.equals(activeCategory);
            String finalCat = cat;
            addRenderableWidget(Button.builder(
                            Component.literal(active ? "§6§l" + label : "§7" + label),
                            b -> switchCategory(finalCat))
                    .bounds(x, y, tabW, 18)
                    .build());
            x += tabW + 4;
        }
    }

    private void switchCategory(String cat) {
        if (cat.equals(activeCategory)) {
            return;
        }
        activeCategory = cat;
        selectedNodeId = "";
        cachedSkillTrees = null;
        cachedTreesCategory = "";
        centerPanOnCanvas();
        rebuildAllWidgets();
    }

    private Button bookTabButton(int x, int y, Item item, FirearmsView target, String tooltip) {
        boolean active = view == target;
        ItemStack icon = new ItemStack(item);
        return new SkillTreeBookTabButton(
                x,
                y,
                icon,
                active,
                b -> {
                    view = target;
                    rebuildAllWidgets();
                },
                Component.literal(tooltip));
    }

    private int contentTop() {
        return view == FirearmsView.SKILL_TREE ? HEADER_H + CATEGORY_BAR_H + 4 : HEADER_H + 6;
    }

    private int contentBottom() {
        return height - FOOTER_H - 4;
    }

    private int treeAreaRight() {
        return width - DETAIL_W - 6;
    }

    private int treeCanvasWidth() {
        return treeAreaRight() - TREE_PAD * 2;
    }

    private int treeCanvasTop() {
        return contentTop() + 4;
    }

    private void rebuildSkillTrees() {
        if (!SkillDatabase.isLoaded()) {
            cachedSkillTrees = null;
            return;
        }
        GunsRpgSkillTrees trees = new GunsRpgSkillTrees(activeCategory);
        trees.build();
        cachedSkillTrees = trees;
        cachedTreesCategory = activeCategory;
        lastLayoutScreenW = width;
        lastLayoutScreenH = height;
        centerPanOnCanvas();
    }

    private void centerPanOnCanvas() {
        if (cachedSkillTrees == null) {
            panX = 24;
            panY = 16;
            return;
        }
        int pixelW = cachedSkillTrees.getCanvasWidth() * X_UNIT + NODE_SIZE;
        int pixelH = cachedSkillTrees.getCanvasHeight() * Y_UNIT + NODE_SIZE;
        int viewW = treeCanvasWidth();
        int viewH = contentBottom() - treeCanvasTop();
        panX = Math.max(12, (viewW - pixelW) / 2.0);
        panY = Math.max(8, (viewH - pixelH) / 2.0);
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTick) {
        renderBackground(gfx);
        rebuildSkillTreesIfNeeded();
        super.render(gfx, mouseX, mouseY, partialTick);

        gfx.drawString(font, title.getString(), width - DETAIL_W - 4 - font.width(title), 8, 0xFFFFFF, false);
        if (minecraft != null && minecraft.player != null) {
            gfx.drawString(
                    font,
                    PlayerFirearmsData.playerLevelProgressText(minecraft.player).getString(),
                    width - DETAIL_W - 8 - font.width(PlayerFirearmsData.playerLevelProgressText(minecraft.player).getString()),
                    18,
                    0x88CC88,
                    false);
        }

        if (!SkillDatabase.isLoaded()) {
            gfx.drawString(
                    font,
                    "数据未加载: " + SkillDatabase.getLoadError(),
                    12,
                    contentTop() + 20,
                    0xFF5555,
                    false);
            drawFooter(gfx);
            return;
        }

        switch (view) {
            case SKILL_TREE -> renderSkillsView(gfx, mouseX, mouseY);
            case EXTENSIONS -> renderExtensionsView(gfx, mouseX, mouseY);
            case PERKS -> renderPerksView(gfx);
        }
        drawFooter(gfx);
    }

    private void rebuildSkillTreesIfNeeded() {
        if (width != lastLayoutScreenW
                || height != lastLayoutScreenH
                || !activeCategory.equals(cachedTreesCategory)
                || (cachedSkillTrees == null && SkillDatabase.isLoaded())) {
            rebuildSkillTrees();
        }
    }

    private void renderSkillsView(GuiGraphics gfx, int mouseX, int mouseY) {
        int top = contentTop();
        int canvasTop = treeCanvasTop();
        int bottom = contentBottom();
        int canvasLeft = TREE_PAD;
        int canvasW = treeCanvasWidth();

        gfx.fill(canvasLeft, HEADER_H, treeAreaRight(), top, 0x90080810);
        gfx.fill(canvasLeft, top, treeAreaRight(), bottom, 0xC0101018);
        gfx.enableScissor(canvasLeft, top, treeAreaRight(), bottom);

        if (cachedSkillTrees != null) {
            int ox = canvasLeft + (int) panX;
            int oy = canvasTop + (int) panY;
            Player treePlayer = minecraft != null ? minecraft.player : null;
            for (GunsRpgTree tree : cachedSkillTrees.getTrees()) {
                for (Connector c : tree.getConnectorList()) {
                    drawConnector(gfx, ox, oy, c, tree, treePlayer);
                }
            }
            for (GunsRpgTree tree : cachedSkillTrees.getTrees()) {
                for (NodeViewData data : tree.getDataViewMap().values()) {
                    GridPos gp = data.getPos();
                    int nx = ox + gp.x() * X_UNIT;
                    int ny = oy + gp.y() * Y_UNIT;
                    drawSkillNodeIcon(gfx, data.getId(), nx, ny, data.getId().equals(selectedNodeId));
                }
            }
        }
        gfx.disableScissor();
        gfx.drawString(
                font,
                "拖拽平移 · 滚轮滚动",
                canvasLeft + 6,
                bottom - 12,
                0x666666,
                false);

        renderDetailPanel(gfx, treeAreaRight() + 4, top, DETAIL_W, bottom - top);
    }

    private void drawConnector(GuiGraphics gfx, int ox, int oy, Connector c, GunsRpgTree tree, Player player) {
        int x1 = ox + c.start().x() * X_UNIT + LINE_OFF;
        int y1 = oy + c.start().y() * Y_UNIT + LINE_OFF;
        int x2 = ox + c.end().x() * X_UNIT + LINE_OFF;
        int y2 = oy + c.end().y() * Y_UNIT + LINE_OFF;
        boolean active = player != null && nodeAt(tree, c.end())
                .map(id -> PlayerFirearmsData.isUnlocked(player, id))
                .orElse(false);
        int color = active ? 0xFF4a9e4a : 0xFFCCCCCC;
        gfx.fill(x1, y1, x2, y2 + (x1 == x2 ? 0 : 1), color);
        if (x1 != x2) {
            gfx.fill(x2, y2, x2 + 1, y1 + 1, color);
        }
    }

    private static java.util.Optional<String> nodeAt(GunsRpgTree tree, GridPos pos) {
        for (NodeViewData data : tree.getDataViewMap().values()) {
            if (data.getPos().equals(pos)) {
                return java.util.Optional.of(data.getId());
            }
        }
        return java.util.Optional.empty();
    }

    /** 22×22 技能格（背景框 + 图标；已解锁为绿色） */
    private void drawSkillNodeIcon(GuiGraphics gfx, String id, int x, int y, boolean selected) {
        boolean assembly = id.endsWith("_assembly");
        boolean unlocked = minecraft != null
                && minecraft.player != null
                && PlayerFirearmsData.isUnlocked(minecraft.player, id);
        int primary = selected ? 0xFFc9a227 : unlocked ? 0xFF2d6b2d : 0xFF343434;
        int secondary = unlocked ? 0xFF1a4a1a : assembly ? 0xFF2a4a7a : 0xFF232323;
        gfx.fill(x, y, x + NODE_SIZE, y + NODE_SIZE, 0xFF000000);
        gfx.fill(x + 1, y + 1, x + NODE_SIZE - 1, y + NODE_SIZE - 1, primary);
        gfx.fill(x + 2, y + NODE_SIZE / 2, x + NODE_SIZE - 2, y + NODE_SIZE - 2, secondary);
        SkillIcons.blitNodeIcon(gfx, id, x, y, NODE_SIZE);
        if (selected) {
            gfx.fill(x, y, x + NODE_SIZE, y + 1, 0xFFFFFFFF);
            gfx.fill(x, y + NODE_SIZE - 1, x + NODE_SIZE, y + NODE_SIZE, 0xFFFFFFFF);
        }
    }

    private void renderDetailPanel(GuiGraphics gfx, int x, int y, int w, int h) {
        gfx.fill(x, y, x + w, y + h, 0xCC101018);
        gfx.drawString(font, "§l详情", x + 6, y + 6, 0xFFEECC, false);
        int textTop = y + 20;
        int textBottom = detailTextBottom();
        if (selectedNodeId.isEmpty()) {
            gfx.drawString(font, "点击左侧技能", x + 6, textTop, 0xAAAAAA, false);
            gfx.drawString(font, "查看效果与条件", x + 6, textTop + 10, 0x888888, false);
            return;
        }
        gfx.enableScissor(x + 2, textTop - 2, x + w - 2, textBottom);
        SkillDatabase.getNode(selectedNodeId).ifPresentOrElse(
                node -> drawNodeDetail(gfx, x, textTop, textBottom, node),
                () -> gfx.drawString(font, "?", x + 6, textTop, 0xFF5555, false));
        gfx.disableScissor();
        gfx.fill(x + 4, textBottom, x + w - 4, y + h - 2, 0xCC101018);
    }

    private void renderExtensionsView(GuiGraphics gfx, int mouseX, int mouseY) {
        int top = contentTop();
        int bottom = contentBottom();
        int listW = 136;
        gfx.fill(6, top, 6 + listW, bottom, 0x88080808);
        gfx.drawString(font, "§l枪械天赋", 12, top + 4, 0xFFCCAA, false);

        List<String> assemblies = SkillDatabase.getAssemblyIds();
        int rowH = 16;
        int visible = (bottom - top - 20) / rowH;
        gfx.enableScissor(6, top + 16, 6 + listW, bottom);
        for (int i = 0; i < visible; i++) {
            int idx = i + extListScroll;
            if (idx >= assemblies.size()) {
                break;
            }
            String asm = assemblies.get(idx);
            int ry = top + 18 + i * rowH;
            boolean sel = asm.equals(selectedAssemblyId);
            if (sel) {
                gfx.fill(8, ry, 6 + listW - 2, ry + rowH - 2, 0xFF8B6914);
            }
            gfx.drawString(font, SkillUiText.titleComponent(asm), 12, ry + 4, sel ? 0xFFFFFF : 0xBBBBBB, false);
        }
        gfx.disableScissor();

        int panelX = 6 + listW + 8;
        int panelW = width - panelX - 8;
        gfx.fill(panelX, top, panelX + panelW, bottom, 0x88101018);
        if (selectedAssemblyId.isEmpty()) {
            gfx.drawString(font, "← 选择枪械", panelX + 8, top + 12, 0xAAAAAA, false);
            return;
        }
        gfx.drawString(
                font,
                Component.literal("§l").append(SkillUiText.titleComponent(selectedAssemblyId)),
                panelX + 8,
                top + 4,
                0xFFEECC,
                false);
        if (minecraft != null && minecraft.player != null) {
            String weapon = PlayerFirearmsData.weaponKeyFromAssembly(selectedAssemblyId);
            int wLv = PlayerFirearmsData.getWeaponLevel(minecraft.player, weapon);
            int weaponPts = PlayerFirearmsData.getExtensionPoints(minecraft.player, weapon);
            int sharedPts = PlayerFirearmsData.getSharedWeaponPoints(minecraft.player);
            int totalPts = weaponPts + sharedPts;
            String extLine = sharedPts > 0
                    ? String.format(Locale.ROOT, "武器 Lv%d  ·  扩展点 %d (+%d 共享)", wLv, totalPts, sharedPts)
                    : String.format(Locale.ROOT, "武器 Lv%d  ·  扩展点 %d", wLv, totalPts);
            gfx.drawString(font, extLine, panelX + 8, top + 14, 0xAAAAAA, false);
        }
        List<String> ext = extensionEntries(selectedAssemblyId);
        int extRowH = 14;
        int listTop = top + 26;
        int listBottom = bottom;
        if (!selectedExtNodeId.isEmpty()) {
            listBottom = detailButtonY() - EXT_DETAIL_H - 8;
        }
        gfx.enableScissor(panelX, listTop, panelX + panelW, listBottom);
        int row = 0;
        Player player = minecraft != null ? minecraft.player : null;
        for (int i = extContentScroll; i < ext.size(); i++) {
            int iy = listTop + row * extRowH;
            if (iy + extRowH > listBottom) {
                gfx.drawString(font, "…", panelX + 8, listBottom - extRowH + 2, 0x888888, false);
                break;
            }
            String id = ext.get(i);
            boolean sel = id.equals(selectedExtNodeId);
            boolean unlocked = player != null && PlayerFirearmsData.isUnlocked(player, id);
            int textColor = unlocked ? 0xFF55CC55 : sel ? 0xFFFFEE88 : 0xDDDDDD;
            if (sel) {
                gfx.fill(panelX + 4, iy, panelX + panelW - 4, iy + extRowH, 0x44FFFFFF);
            }
            SkillDatabase.getNode(id).ifPresentOrElse(
                    n -> {
                        gfx.drawString(
                                font,
                                String.format(
                                        Locale.ROOT,
                                        "%-14s  Lv%-2d  %d pt",
                                        SkillUiText.title(id),
                                        n.getLevel(),
                                        n.getPrice()),
                                panelX + 8,
                                iy + 2,
                                textColor,
                                false);
                        if (unlocked) {
                            gfx.drawString(font, "✓", panelX + panelW - 18, iy + 2, 0xFF55CC55, false);
                        }
                    },
                    () -> gfx.drawString(
                            font, SkillUiText.titleComponent(id), panelX + 8, iy + 2, textColor, false));
            row++;
        }
        gfx.disableScissor();

        if (!selectedExtNodeId.isEmpty() && player != null) {
            int detailY = detailButtonY() - EXT_DETAIL_H - 4;
            gfx.fill(panelX + 2, detailY, panelX + panelW - 2, detailButtonY() - 2, 0xCC0A0A12);
            gfx.fill(panelX + 2, detailY, panelX + panelW - 2, detailY + 1, 0xFF666666);
            gfx.drawString(font, "§l天赋说明", panelX + 8, detailY + 4, 0xFFEECC, false);
            SkillDatabase.getNode(selectedExtNodeId).ifPresentOrElse(
                    node -> drawExtensionDetail(gfx, panelX + 6, detailY + 16, panelW - 12, detailButtonY() - 6, node),
                    () -> gfx.drawString(font, "?", panelX + 8, detailY + 16, 0xFF5555, false));
        }
    }

    /** 扩展页只列配件节点，跳过装配体解锁链 */
    private static List<String> extensionEntries(String assemblyId) {
        List<String> raw = SkillDatabase.getAssemblies().getOrDefault(assemblyId, List.of());
        List<String> out = new ArrayList<>();
        for (String id : raw) {
            if (!id.endsWith("_assembly")) {
                out.add(id);
            }
        }
        return out;
    }

    private void renderPerksView(GuiGraphics gfx) {
        int top = contentTop();
        int bottom = contentBottom();
        int listRight = width - DETAIL_W - 8;
        final int nameW = 100;
        final int barLeft = 12 + nameW;
        final int valueW = 56;
        final int barMaxW = Math.max(40, listRight - barLeft - valueW - 8);
        final float barScale = 1f / 0.35f;

        gfx.drawString(font, "§l天赋总览", 12, top + 2, 0xFFEECC, false);
        gfx.drawString(font, "左键选中 · 再左键+1步 · 右键-1步收回", 12, top + 12, 0x666666, false);

        List<PerkDef> perks = visiblePerks();
        int rowH = 14;
        int y = top + 24;
        for (int i = perkScroll; i < perks.size(); i++) {
            if (y + rowH > bottom) {
                break;
            }
            PerkDef p = perks.get(i);
            boolean selected = p.getId().equals(selectedPerkId);
            if (selected) {
                gfx.fill(8, y, listRight, y + rowH, 0x44FFEECC);
            }
            int nameColor = selected ? 0xFFFFFF : 0xCCCCCC;
            gfx.drawString(font, perkTitleForRow(p.getId()), 12, y + 2, nameColor, false);
            int invest = minecraft != null && minecraft.player != null
                    ? PlayerFirearmsData.getPerkInvestment(minecraft.player, p.getId())
                    : 0;
            double current = invest * p.getScaling() * 100;
            int buffW = (int) (Math.max(0, current) / 100.0 * barMaxW * barScale);
            buffW = Mth.clamp(buffW, 0, barMaxW);
            gfx.fill(barLeft, y + 3, barLeft + buffW, y + rowH - 3, 0xFF3d8a3d);
            String pct = PerkUiText.formatPercentDisplay(p.getId(), current);
            gfx.drawString(font, pct, listRight - valueW, y + 2, 0x999999, false);
            y += rowH;
        }

        drawPerkDetail(gfx, listRight + 6, top, bottom);
    }

    private void drawPerkDetail(GuiGraphics gfx, int x, int top, int bottom) {
        int panelW = DETAIL_W - 4;
        gfx.fill(x, top, x + panelW, bottom, 0xCC111111);
        gfx.drawString(font, "§l详情", x + 6, top + 4, 0xFFEECC, false);
        if (selectedPerkId.isEmpty()) {
            gfx.drawString(font, "点击左侧天赋查看说明与实现状态", x + 6, top + 20, 0x888888, false);
            return;
        }
        int invest = minecraft != null && minecraft.player != null
                ? PlayerFirearmsData.getPerkInvestment(minecraft.player, selectedPerkId)
                : 0;
        gfx.drawString(font, perkTitleForRow(selectedPerkId), x + 6, top + 18, 0xFFFFFF, false);
        int line = 0;
        for (String wrapped : wrapText(PerkUiText.detailBlock(selectedPerkId, invest), panelW - 12)) {
            gfx.drawString(font, wrapped, x + 6, top + 32 + line * 10, 0xBBBBBB, false);
            line++;
            if (top + 32 + line * 10 > bottom - 12) {
                break;
            }
        }
    }

    private void drawFooter(GuiGraphics gfx) {
        int footerY = height - FOOTER_H;
        gfx.fill(0, footerY, width, height, 0xDD000000);
        gfx.fill(0, footerY, width, footerY + 1, 0xFF666666);

        Player player = minecraft != null ? minecraft.player : null;
        String levelLine;
        if (player != null) {
            levelLine = PlayerFirearmsData.playerLevelProgressText(player).getString();
        } else {
            levelLine = "—";
        }
        gfx.drawString(font, levelLine, 10, footerY + 8, 0xFFEECC, false);
        if (player != null && PlayerFirearmsData.getTotalKills(player) == 0) {
            gfx.drawString(font, "用枪射杀怪物可涨火器等级（近战不计）", 10, footerY + 18, 0x888866, false);
        }
        if (view == FirearmsView.SKILL_TREE) {
            gfx.drawString(font, "拖拽平移画布", width / 2 - 28, footerY + 8, 0x888888, false);
        }
        if (player != null) {
            gfx.drawString(
                    font,
                    "Debuff: " + DebuffService.statusLine(player),
                    width - 160,
                    footerY - 2,
                    0xAA6666,
                    false);
            String pointsLine = footerPointsLine(player);
            int pointsColor = switch (view) {
                case SKILL_TREE -> 0xFFFFAA00;
                case PERKS -> 0xFFFFFF00;
                case EXTENSIONS -> 0xFFFFEE00;
            };
            gfx.drawString(
                    font,
                    pointsLine,
                    width - font.width(pointsLine) - 10,
                    footerY + 8,
                    pointsColor,
                    false);
        }
    }

    private String footerPointsLine(Player player) {
        return switch (view) {
            case SKILL_TREE -> String.format(
                    Locale.ROOT, "%d 技能点", PlayerFirearmsData.getSkillPoints(player));
            case PERKS -> String.format(
                    Locale.ROOT, "%d 属性点", PlayerFirearmsData.getPerkPoints(player));
            case EXTENSIONS -> {
                int shared = PlayerFirearmsData.getSharedWeaponPoints(player);
                if (!selectedAssemblyId.isEmpty()) {
                    String weapon = PlayerFirearmsData.weaponKeyFromAssembly(selectedAssemblyId);
                    int total = PlayerFirearmsData.getTotalExtensionPoints(player, weapon);
                    if (shared > 0) {
                        yield String.format(Locale.ROOT, "%d 扩展点 (+%d 共享)", total, shared);
                    }
                    yield String.format(Locale.ROOT, "%d 扩展点", total);
                }
                yield String.format(Locale.ROOT, "%d 共享武器点", shared);
            }
        };
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if ((button == 0 || button == 1) && view == FirearmsView.PERKS && minecraft != null && minecraft.player != null) {
            if (mouseX >= width - DETAIL_W - 8) {
                return super.mouseClicked(mouseX, mouseY, button);
            }
            int hit = hitTestPerkRow(mouseY);
            if (hit >= 0) {
                List<PerkDef> perks = visiblePerks();
                if (hit < perks.size()) {
                    String perkId = perks.get(hit).getId();
                    int invest = PlayerFirearmsData.getPerkInvestment(minecraft.player, perkId);
                    if (button == 0 && !perkId.equals(selectedPerkId)) {
                        selectedPerkId = perkId;
                    } else if (button == 0) {
                        selectedPerkId = perkId;
                        ModNetwork.sendToServer(new AdjustPerkPacket(perkId, 1));
                    } else if (button == 1 && invest > 0) {
                        selectedPerkId = perkId;
                        ModNetwork.sendToServer(new AdjustPerkPacket(perkId, -1));
                    }
                    return true;
                }
            }
        }
        if (button == 0 && view == FirearmsView.SKILL_TREE && SkillDatabase.isLoaded()) {
            if (mouseX >= detailPanelX() && mouseY >= contentTop() && mouseY < contentBottom()) {
                return super.mouseClicked(mouseX, mouseY, button);
            }
            if (isInTreeCanvas(mouseX, mouseY)) {
                String hit = hitTestNode(mouseX, mouseY);
                if (hit != null) {
                    selectedNodeId = hit;
                    if (hit.endsWith("_assembly")) {
                        selectedAssemblyId = hit;
                    }
                    rebuildAllWidgets();
                    return true;
                }
                panning = true;
                panGrabMouseX = mouseX;
                panGrabMouseY = mouseY;
                panGrabX = panX;
                panGrabY = panY;
                return true;
            }
        }
        if (button == 0 && view == FirearmsView.EXTENSIONS) {
            String asm = hitTestAssembly(mouseX, mouseY);
            if (asm != null && !asm.equals(selectedAssemblyId)) {
                selectedAssemblyId = asm;
                selectedExtNodeId = "";
                extContentScroll = 0;
                rebuildAllWidgets();
                return true;
            }
            String extId = hitTestExtensionRow(mouseX, mouseY);
            if (extId != null) {
                selectedExtNodeId = extId;
                rebuildAllWidgets();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isInTreeCanvas(double mouseX, double mouseY) {
        return mouseX >= TREE_PAD
                && mouseX < treeAreaRight()
                && mouseY >= contentTop()
                && mouseY < contentBottom();
    }

    private String hitTestNode(double mouseX, double mouseY) {
        if (cachedSkillTrees == null) {
            return null;
        }
        int ox = TREE_PAD + (int) panX;
        int oy = treeCanvasTop() + (int) panY;
        for (GunsRpgTree tree : cachedSkillTrees.getTrees()) {
            for (NodeViewData data : tree.getDataViewMap().values()) {
                GridPos gp = data.getPos();
                int nx = ox + gp.x() * X_UNIT;
                int ny = oy + gp.y() * Y_UNIT;
                if (mouseX >= nx && mouseX < nx + NODE_SIZE && mouseY >= ny && mouseY < ny + NODE_SIZE) {
                    return data.getId();
                }
            }
        }
        return null;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            panning = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (panning && button == 0) {
            panX = panGrabX + (mouseX - panGrabMouseX);
            panY = panGrabY + (mouseY - panGrabMouseY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    private String hitTestAssembly(double mouseX, double mouseY) {
        int top = contentTop();
        int listW = 136;
        if (mouseX < 6 || mouseX > 6 + listW) {
            return null;
        }
        List<String> assemblies = SkillDatabase.getAssemblyIds();
        int rowH = 16;
        int idx = (int) ((mouseY - top - 18) / rowH) + extListScroll;
        if (idx >= 0 && idx < assemblies.size()) {
            return assemblies.get(idx);
        }
        return null;
    }

    private String hitTestExtensionRow(double mouseX, double mouseY) {
        if (selectedAssemblyId.isEmpty()) {
            return null;
        }
        int listW = 136;
        int panelX = 6 + listW + 8;
        int panelW = width - panelX - 8;
        if (mouseX < panelX || mouseX > panelX + panelW) {
            return null;
        }
        int top = contentTop();
        int listTop = top + 26;
        int extRowH = 14;
        int listBottom =
                selectedExtNodeId.isEmpty() ? contentBottom() : detailButtonY() - EXT_DETAIL_H - 8;
        if (mouseY < listTop || mouseY >= listBottom) {
            return null;
        }
        List<String> ext = extensionEntries(selectedAssemblyId);
        int idx = (int) ((mouseY - listTop) / extRowH) + extContentScroll;
        if (idx >= 0 && idx < ext.size()) {
            return ext.get(idx);
        }
        return null;
    }

    private int hitTestPerkRow(double mouseY) {
        int top = contentTop() + 26;
        int rowH = 14;
        if (mouseY < top) {
            return -1;
        }
        return (int) ((mouseY - top) / rowH) + perkScroll;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (view == FirearmsView.SKILL_TREE && isInTreeCanvas(mouseX, mouseY)) {
            panY += delta * 16;
            return true;
        }
        if (view == FirearmsView.PERKS) {
            perkScroll = Math.max(0, perkScroll - (int) delta);
            return true;
        }
        if (view == FirearmsView.EXTENSIONS) {
            if (mouseX < 150) {
                extListScroll = Math.max(0, extListScroll - (int) delta);
            } else {
                extContentScroll = Math.max(0, extContentScroll - (int) delta);
            }
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    private void drawExtensionDetail(GuiGraphics gfx, int x, int ty, int panelW, int textBottom, SkillNode node) {
        drawNodeDetail(gfx, x, ty, panelW, textBottom, node, true);
    }

    private void drawNodeDetail(GuiGraphics gfx, int x, int ty, int textBottom, SkillNode node) {
        drawNodeDetail(gfx, x, ty, DETAIL_W - 12, textBottom, node, false);
    }

    /**
     * @param reserveStatusLine 为 true 时底部固定一行显示解锁条件（红/绿字），避免动态数值说明挤掉提示。
     */
    private void drawNodeDetail(
            GuiGraphics gfx, int x, int ty, int panelW, int textBottom, SkillNode node, boolean reserveStatusLine) {
        int line = 0;
        int lineH = 10;
        if (ty + line * lineH > textBottom) {
            return;
        }
        gfx.drawString(font, SkillDetailFormatter.title(node), x + 6, ty + line * lineH, 0xFFFFFF, false);
        line++;
        Player player = minecraft != null ? minecraft.player : null;
        int bodyBottom = textBottom;
        if (player != null && reserveStatusLine) {
            bodyBottom = textBottom - lineH;
        }
        if (player != null) {
            boolean truncated = false;
            for (String body : SkillDetailFormatter.bodyLines(node, player)) {
                for (String wrapped : wrapText(body, panelW)) {
                    if (ty + line * lineH > bodyBottom - lineH) {
                        truncated = true;
                        break;
                    }
                    gfx.drawString(font, wrapped, x + 6, ty + line * lineH, 0xBBBBBB, false);
                    line++;
                }
                if (truncated) {
                    break;
                }
            }
            if (truncated && ty + line * lineH <= bodyBottom) {
                gfx.drawString(font, "…", x + 6, ty + line * lineH, 0x888888, false);
            }
            int statusY = reserveStatusLine ? textBottom - lineH : ty + line * lineH;
            if (!reserveStatusLine && statusY > textBottom - lineH) {
                return;
            }
            if (statusY >= ty) {
                String status = SkillDetailFormatter.statusLine(node, player);
                gfx.drawString(font, status, x + 6, statusY, 0xFFFFFF, false);
            }
        }
    }

    private List<String> wrapText(String text, int maxWidth) {
        List<String> out = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return out;
        }
        String remain = text;
        while (!remain.isEmpty()) {
            if (font.width(remain) <= maxWidth) {
                out.add(remain);
                break;
            }
            int cut = remain.length();
            while (cut > 1 && font.width(remain.substring(0, cut)) > maxWidth) {
                cut--;
            }
            out.add(remain.substring(0, cut));
            remain = remain.substring(cut);
        }
        return out;
    }

    private static Component perkTitleForRow(String perkId) {
        try {
            return PerkUiText.perkTitleComponent(perkId);
        } catch (Throwable t) {
            return Component.literal(PerkUiText.perkTitlePlain(perkId));
        }
    }

    private static List<PerkDef> visiblePerks() {
        List<PerkDef> perks = new ArrayList<>(SkillDatabase.getAllPerks().values());
        perks.sort((a, b) -> a.getId().compareTo(b.getId()));
        return perks;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}

package com.wf.firearms.client.gui.layout;

import com.wf.firearms.data.SkillDatabase;
import com.wf.firearms.data.SkillNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** 移植自 Guns RPG {@code dev.toma.gunsrpg.client.screen.skill.Tree}（网格坐标 + 连线）。 */
public final class GunsRpgTree {
    public static final int GRID_UNIT_SIZE = 4;
    public static final int HALF_UNIT = GRID_UNIT_SIZE / 2;

    private final String category;
    private final Map<String, NodeViewData> dataViewMap = new HashMap<>();
    private final String rootId;
    private int width;
    private int height;
    private final List<Connector> connectorList = new ArrayList<>();

    public GunsRpgTree(String category, String rootId) {
        this.category = category;
        this.rootId = rootId;
        fillPositionMap(rootId);
        this.width = calculateMaxWidth();
        this.height = calculateMaxHeight();
    }

    public void makeConnections(String headId) {
        SkillDatabase.getNode(headId).ifPresent(head -> {
            for (String child : head.getChildren()) {
                if (dataViewMap.containsKey(child)) {
                    connect(headId, child);
                    makeConnections(child);
                }
            }
        });
    }

    public String getRootId() {
        return rootId;
    }

    public void move(int xCorrection, int yCorrection) {
        dataViewMap.values().forEach(data -> data.move(xCorrection, yCorrection));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Map<String, NodeViewData> getDataViewMap() {
        return dataViewMap;
    }

    public List<Connector> getConnectorList() {
        return connectorList;
    }

    private void fillPositionMap(String root) {
        if ("wooden_ammo_smith".equals(rootId)) {
            AmmoSmithTreeLayout.populate(this, category);
            return;
        }
        TreeNode node = new TreeNode(root, 0, 0);
        generateViewDataForNode(node);
    }

    private void generateViewDataForNode(TreeNode node) {
        int childCount = node.childrenNodes != null ? node.childrenNodes.length : 0;
        boolean shouldCenter = childCount > 1;
        GridPos pos = new GridPos(node.xLevel * GRID_UNIT_SIZE, node.yLevel * GRID_UNIT_SIZE);
        if (shouldCenter) {
            int toCenter = (childCount - 1) * HALF_UNIT;
            pos = pos.growX(toCenter);
            centerBranch(node, toCenter);
        }
        if (childCount > 0) {
            for (TreeNode child : node.childrenNodes) {
                generateViewDataForNode(child);
            }
        }
        dataViewMap.put(node.id, new NodeViewData(node.id, category, pos));
    }

    private void centerBranch(TreeNode node, int correction) {
        TreeNode parent;
        TreeNode current = node;
        while ((parent = current.parent) != null) {
            TreeNode[] children = parent.childrenNodes;
            NodeViewData data = dataViewMap.get(current.id);
            if (data != null) {
                data.getPos().growX(correction);
            }
            if (children != null && children.length > 1) {
                break;
            }
            current = parent;
        }
    }

    private int calculateMaxWidth() {
        return dataViewMap.values().stream().mapToInt(v -> v.getPos().x()).max().orElse(GRID_UNIT_SIZE);
    }

    private int calculateMaxHeight() {
        return dataViewMap.values().stream().mapToInt(v -> v.getPos().y()).max().orElse(GRID_UNIT_SIZE);
    }

    private void connect(String parentId, String childId) {
        GridPos pos1 = getPosition(parentId);
        GridPos pos2 = getPosition(childId);
        if (pos1.x() == pos2.x()) {
            connectorList.add(new Connector(pos1, pos2));
        } else {
            int diffY = pos2.y() - pos1.y();
            int halfY = pos1.y() + diffY / 2;
            GridPos pos3 = new GridPos(pos1.x(), halfY);
            GridPos pos4 = new GridPos(pos2.x(), halfY);
            connectorList.add(new Connector(pos1, pos3));
            connectorList.add(new Connector(pos3, pos4));
            connectorList.add(new Connector(pos4, pos2));
        }
    }

    private GridPos getPosition(String id) {
        NodeViewData data = dataViewMap.get(id);
        return data != null ? data.getPos() : new GridPos(0, 0);
    }

    private final class TreeNode {
        private final int xLevel;
        private final int yLevel;
        private final String id;
        private final TreeNode parent;
        private final TreeNode[] childrenNodes;

        TreeNode(String id, int xLevel, int yLevel) {
            this(id, null, xLevel, yLevel);
        }

        TreeNode(String id, TreeNode parent, int xLevel, int yLevel) {
            this.id = id;
            this.parent = parent;
            this.xLevel = xLevel;
            this.yLevel = yLevel;
            this.childrenNodes = buildChildren();
        }

        private TreeNode[] buildChildren() {
            Optional<SkillNode> opt = SkillDatabase.getNode(id);
            if (opt.isEmpty()) {
                return null;
            }
            List<String> children = new ArrayList<>();
            for (String child : opt.get().getChildren()) {
                SkillDatabase.getNode(child).ifPresent(c -> {
                    if (category.equals(c.getCategory())) {
                        children.add(child);
                    }
                });
            }
            if (children.isEmpty()) {
                return null;
            }
            int xStep = siblingXStep(children.size());
            TreeNode[] out = new TreeNode[children.size()];
            for (int i = 0; i < children.size(); i++) {
                out[i] = new TreeNode(children.get(i), this, xLevel + i * xStep, yLevel + 1);
            }
            return out;
        }

        /** 枪匠根节点并列装配体过多时加宽横向间距，避免 AKM/SKS 等图标重叠。 */
        private int siblingXStep(int childCount) {
            if ("gun_parts_smith".equals(id) && childCount >= 6) {
                return 2;
            }
            return 1;
        }
    }

    public record Connector(GridPos start, GridPos end) {}

    public static final class NodeViewData {
        private final String id;
        private final String category;
        private GridPos pos;

        NodeViewData(String id, String category, GridPos pos) {
            this.id = id;
            this.category = category;
            this.pos = pos;
        }

        public String getId() {
            return id;
        }

        public GridPos getPos() {
            return pos;
        }

        void move(int dx, int dy) {
            pos = pos.offset(dx, dy);
        }
    }

    public record GridPos(int x, int y) {
        GridPos growX(int amount) {
            return new GridPos(x + amount, y);
        }

        GridPos offset(int dx, int dy) {
            return new GridPos(x + dx, y + dy);
        }
    }
}

package com.wf.firearms.client.gui.layout;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 子弹装配技能树：3 列 × 多行，从左到右、从上到下为材料档位单元。
 *
 * <pre>
 *   木    石    铜
 *   铁    金  青金
 *   绿宝  红石 石英
 *   黄铜  青铜  钻石
 *   钢  下界合金  因瓦
 *   终极  海王  星陨
 *   恒星  虚空  星光秘银
 *   暗冰晶 宇宙极光 永恒合金
 * </pre>
 */
final class AmmoSmithTreeLayout {
    /** skillId → [gridCol, gridRow] */
    private static final Map<String, int[]> CELLS = new LinkedHashMap<>();

    static {
        put("wooden_ammo_smith", 0, 0);
        put("stone_ammo_smith", 1, 0);
        put("copper_ammo_smith", 2, 0);

        put("iron_ammo_smith", 0, 1);
        put("gold_ammo_smith", 1, 1);
        put("lapis_ammo_smith", 2, 1);

        put("emerald_ammo_smith", 0, 2);
        put("redstone_ammo_smith", 1, 2);
        put("quartz_ammo_smith", 2, 2);

        put("brass_ammo_smith", 0, 3);
        put("bronze_ammo_smith", 1, 3);
        put("diamond_ammo_smith", 2, 3);

        put("steel_ammo_smith", 0, 4);
        put("netherite_ammo_smith", 1, 4);
        put("invar_ammo_smith", 2, 4);

        put("ultimate_ammo_smith", 0, 5);
        put("neptunium_ammo_smith", 1, 5);
        put("starinium_ammo_smith", 2, 5);

        put("stellar_ammo_smith", 0, 6);
        put("void_ammo_smith", 1, 6);
        put("starlight_mythril_ammo_smith", 2, 6);

        put("dark_cryopla_ammo_smith", 0, 7);
        put("cosmos_aurora_ammo_smith", 1, 7);
        put("abiding_alloy_ammo_smith", 2, 7);
    }

    private AmmoSmithTreeLayout() {}

    static void populate(GunsRpgTree tree, String category) {
        Map<String, GunsRpgTree.NodeViewData> map = tree.getDataViewMap();
        for (Map.Entry<String, int[]> e : CELLS.entrySet()) {
            int[] cell = e.getValue();
            map.put(
                    e.getKey(),
                    new GunsRpgTree.NodeViewData(
                            e.getKey(),
                            category,
                            new GunsRpgTree.GridPos(
                                    cell[0] * GunsRpgTree.GRID_UNIT_SIZE,
                                    cell[1] * GunsRpgTree.GRID_UNIT_SIZE)));
        }
    }

    private static void put(String id, int col, int row) {
        CELLS.put(id, new int[] {col, row});
    }
}

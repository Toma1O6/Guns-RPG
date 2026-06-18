package com.wf.firearms.client.gui;



import java.util.Map;



/** 技能树装配体 → 2D 枪条图标 key（仅未单独出图的枪械做近邻借用）。 */

public final class SkillIconAssets {

    private static final Map<String, String> ASSEMBLY_GUN_ICON = Map.ofEntries(

            Map.entry("chukonu_assembly", "kar98k"),

            Map.entry("crossbow_assembly", "kar98k"));



    private SkillIconAssets() {}



    public static String gunIconKeyForAssembly(String assemblyId) {

        if (assemblyId == null || !assemblyId.endsWith("_assembly")) {

            return assemblyId == null ? "" : assemblyId;

        }

        return ASSEMBLY_GUN_ICON.getOrDefault(

                assemblyId, assemblyId.substring(0, assemblyId.length() - "_assembly".length()));

    }

}


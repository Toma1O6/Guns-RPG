package com.wf.firearms.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.wf.firearms.GunsRpg;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public final class KeyBindings {
    public static final String CATEGORY = "key.categories." + GunsRpg.MOD_ID;

    public static final KeyMapping OPEN_SKILL_TREE = new KeyMapping(
            "key." + GunsRpg.MOD_ID + ".open_skill_tree",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            CATEGORY);

    /** 装填弹药（对标 gunsrpg.key.reload，默认 R） */
    public static final KeyMapping RELOAD = new KeyMapping(
            "key." + GunsRpg.MOD_ID + ".reload",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            CATEGORY);

    /** 解除卡弹（对标 gunsrpg.key.unjam，默认 U） */
    public static final KeyMapping UNJAM = new KeyMapping(
            "key." + GunsRpg.MOD_ID + ".unjam",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_U,
            CATEGORY);

    /** 切换开火模式（默认 B） */
    public static final KeyMapping FIREMODE = new KeyMapping(
            "key." + GunsRpg.MOD_ID + ".firemode",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            CATEGORY);

    /** 切换准星颜色（默认 N） */
    public static final KeyMapping SIGHT_COLOR = new KeyMapping(
            "key." + GunsRpg.MOD_ID + ".sight_color",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            CATEGORY);

    /** 切换准星形状（默认 M） */
    public static final KeyMapping SIGHT_TYPE = new KeyMapping(
            "key." + GunsRpg.MOD_ID + ".sight_type",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            CATEGORY);

    /** 主动技能：紧急空投（god_help_us，默认 G） */
    public static final KeyMapping EMERGENCY_AIRDROP = new KeyMapping(
            "key." + GunsRpg.MOD_ID + ".emergency_airdrop",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            CATEGORY);

    private KeyBindings() {}
}

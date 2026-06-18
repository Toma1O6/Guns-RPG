package com.wf.firearms.gameplay;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;

/** 技能节点解锁后发布，供 KubeJS / 其它模组监听。 */
public class SkillUnlockedEvent extends Event {
    private final ServerPlayer player;
    private final String skillId;

    public SkillUnlockedEvent(ServerPlayer player, String skillId) {
        this.player = player;
        this.skillId = skillId;
    }

    public ServerPlayer getPlayer() {
        return player;
    }

    public String getSkillId() {
        return skillId;
    }
}

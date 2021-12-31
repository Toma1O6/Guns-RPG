package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.api.common.IClickableSkill;
import dev.toma.gunsrpg.api.common.ISkill;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_SkillClickedPacket extends AbstractNetworkPacket<C2S_SkillClickedPacket> {

    private SkillType<?> type;

    public C2S_SkillClickedPacket() {
    }

    public C2S_SkillClickedPacket(SkillType<?> type) {
        this.type = type;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(type.getRegistryName());
    }

    @Override
    public C2S_SkillClickedPacket decode(PacketBuffer buffer) {
        return new C2S_SkillClickedPacket(ModRegistries.SKILLS.getValue(buffer.readResourceLocation()));
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        if (type == null) return;
        // TODO
        /*ISkill skill = SkillUtil.getBestSkillFromOverrides(PlayerData.getSkill(player, type), player);
        if (skill instanceof IClickableSkill && skill.canApply(player)) {
            ((IClickableSkill) skill).clicked(player);
        }*/
    }
}

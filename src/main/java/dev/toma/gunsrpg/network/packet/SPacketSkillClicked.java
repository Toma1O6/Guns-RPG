package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.init.GunsRPGRegistries;
import dev.toma.gunsrpg.common.skills.core.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.interfaces.Clickable;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SPacketSkillClicked extends AbstractNetworkPacket<SPacketSkillClicked> {

    private SkillType<?> type;

    public SPacketSkillClicked() {
    }

    public SPacketSkillClicked(SkillType<?> type) {
        this.type = type;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(type.getRegistryName());
    }

    @Override
    public SPacketSkillClicked decode(PacketBuffer buffer) {
        return new SPacketSkillClicked(GunsRPGRegistries.SKILLS.getValue(buffer.readResourceLocation()));
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        if (type == null) return;
        ISkill skill = SkillUtil.getBestSkillFromOverrides(PlayerDataFactory.getSkill(player, type), player);
        if (skill instanceof Clickable && skill.apply(player)) {
            ((Clickable) skill).clicked(player);
        }
    }
}

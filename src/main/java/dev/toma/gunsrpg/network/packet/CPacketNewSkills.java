package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.init.GunsRPGRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;

public class CPacketNewSkills extends AbstractNetworkPacket<CPacketNewSkills> {

    private final List<SkillType<?>> unlocked;

    public CPacketNewSkills() {
        this(null);
    }

    public CPacketNewSkills(List<SkillType<?>> list) {
        unlocked = list;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeVarInt(unlocked.size());
        for (SkillType<?> type : unlocked) {
            buffer.writeResourceLocation(type.getRegistryName());
        }
    }

    @Override
    public CPacketNewSkills decode(PacketBuffer buffer) {
        List<SkillType<?>> unlocked = new ArrayList<>();
        int n = buffer.readVarInt();
        for (int i = 0; i < n; i++) {
            ResourceLocation location = buffer.readResourceLocation();
            SkillType<?> type = GunsRPGRegistries.SKILLS.getValue(location);
            if (type != null)
                unlocked.add(type);
        }
        return new CPacketNewSkills(unlocked);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        for (SkillType<?> type : unlocked) {
            type.setFresh(true);
        }
    }
}

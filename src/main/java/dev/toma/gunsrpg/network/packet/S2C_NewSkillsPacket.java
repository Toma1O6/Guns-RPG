package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;

public class S2C_NewSkillsPacket extends AbstractNetworkPacket<S2C_NewSkillsPacket> {

    private final List<SkillType<?>> unlocked;

    public S2C_NewSkillsPacket() {
        this(null);
    }

    public S2C_NewSkillsPacket(List<SkillType<?>> list) {
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
    public S2C_NewSkillsPacket decode(PacketBuffer buffer) {
        List<SkillType<?>> unlocked = new ArrayList<>();
        int n = buffer.readVarInt();
        for (int i = 0; i < n; i++) {
            ResourceLocation location = buffer.readResourceLocation();
            SkillType<?> type = ModRegistries.SKILLS.getValue(location);
            if (type != null)
                unlocked.add(type);
        }
        return new S2C_NewSkillsPacket(unlocked);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        for (SkillType<?> type : unlocked) {
            // TODO
            //type.setFresh(true);
        }
    }
}

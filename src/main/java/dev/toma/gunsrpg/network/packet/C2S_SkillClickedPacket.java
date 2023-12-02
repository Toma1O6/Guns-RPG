package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.api.common.skill.IClickableSkill;
import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_SkillClickedPacket extends AbstractNetworkPacket<C2S_SkillClickedPacket> {

    private SkillType<?> type;

    public C2S_SkillClickedPacket() {
    }

    public <S extends ISkill & IClickableSkill, T extends SkillType<S>>  C2S_SkillClickedPacket(T type) {
        this.type = type;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(type.getRegistryName());
    }

    @Override
    public C2S_SkillClickedPacket decode(PacketBuffer buffer) {
        ResourceLocation path = buffer.readResourceLocation();
        return new C2S_SkillClickedPacket(fromRegistry(path));
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        if (type == null || type.isDisabled()) return;
        PlayerData.get(player).ifPresent(data -> {
            ISkillProvider provider = data.getSkillProvider();
            ISkill skill = SkillUtil.getTopHierarchySkill(type, provider);
            IClickableSkill clickable = (IClickableSkill) skill;
            if (clickable.canUse()) {
                clickable.onSkillUsed(player);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private static <S extends ISkill & IClickableSkill, T extends SkillType<S>> T fromRegistry(ResourceLocation location) {
        SkillType<?> type = ModRegistries.SKILLS.getValue(location);
        ISkill skill = type.getDataInstance();
        if (!(skill instanceof IClickableSkill)) {
            throw new IllegalArgumentException("Attempted to send click event from skill which doesn't implement the IClickableSkill interface! [" + location + "]");
        }
        return (T) type;
    }
}

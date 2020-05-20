package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.AbilityData;
import dev.toma.gunsrpg.common.skilltree.Ability;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketAbilityUpdate implements IMessage {

    private String name;
    private PacketInfoType type;

    public SPacketAbilityUpdate() {}

    public SPacketAbilityUpdate(PacketInfoType type, Ability.UnlockableType skill) {
        this.type = type;
        this.name = skill.name;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(type.ordinal());
        ByteBufUtils.writeUTF8String(buf, name);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        type = PacketInfoType.values()[buf.readInt()];
        name = ByteBufUtils.readUTF8String(buf);
    }

    public enum PacketInfoType {
        UNLOCK,
        TOGGLE
    }

    public static class Handler implements IMessageHandler<SPacketAbilityUpdate, IMessage> {

        @Override
        public IMessage onMessage(SPacketAbilityUpdate message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServer().addScheduledTask(() -> {
                PlayerData cap = PlayerDataFactory.get(player);
                AbilityData data = cap.getSkillData().getAbilityData();
                switch (message.type) {
                    case TOGGLE: {
                        Ability.UnlockableType type = Ability.findSkill(message.name);
                        if(type != null && data.unlockedSkills.containsKey(type)) {
                            data.unlockedSkills.get(type).toggle();
                        }
                        break;
                    }
                    case UNLOCK: {
                        Ability.UnlockableType type = Ability.findSkill(message.name);
                        if(type != null && data.lockedSkills.containsKey(type)) {
                            if(data.canPurchase(type)) {
                                data.purchaseSkill(type);
                            }
                        }
                        break;
                    }
                }
                cap.sync();
            });
            return null;
        }
    }
}

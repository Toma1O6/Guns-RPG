package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.skills.core.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.interfaces.Clickable;
import dev.toma.gunsrpg.util.SkillUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketSkillClicked implements IMessage {

    private SkillType<?> type;

    public SPacketSkillClicked() {

    }

    public SPacketSkillClicked(SkillType<?> type) {
        this.type = type;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, type.getRegistryName().toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        ResourceLocation location = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        type = ModRegistry.SKILLS.getValue(location);
    }

    public static class Handler implements IMessageHandler<SPacketSkillClicked, IMessage> {

        @Override
        public IMessage onMessage(SPacketSkillClicked message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServer().addScheduledTask(() -> {
                if(message.type == null) return;
                ISkill skill = SkillUtil.getBestSkillFromOverrides(PlayerDataFactory.getSkill(player, message.type), player);
                if(skill instanceof Clickable && skill.apply(player)) {
                    ((Clickable) skill).clicked(player);
                }
            });
            return null;
        }
    }
}

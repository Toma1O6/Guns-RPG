package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class CPacketNewSkills implements IMessage {

    private List<SkillType<?>> unlocked = new ArrayList<>();

    public CPacketNewSkills() {}

    public CPacketNewSkills(List<SkillType<?>> list) {
        unlocked = list;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(unlocked.size());
        for(SkillType<?> type : unlocked) {
            ByteBufUtils.writeUTF8String(buf, type.getRegistryName().toString());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int i = buf.readInt();
        for(int n = 0; n < i; n++) {
            ResourceLocation location = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
            SkillType<?> type = ModRegistry.SKILLS.getValue(location);
            unlocked.add(type);
        }
    }

    public static class Handler implements IMessageHandler<CPacketNewSkills, IMessage> {

        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(CPacketNewSkills message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(() -> {
                List<SkillType<?>> list = message.unlocked;
                for(SkillType<?> type : list) {
                    if(type != null) {
                        type.isNew = true;
                    }
                }
            });
            return null;
        }
    }
}

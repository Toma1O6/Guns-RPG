package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.init.GunsRPGRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.ModUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SPacketUnlockSkill implements IMessage {

    private boolean hasParent;
    private SkillType<?> clicked;
    private SkillType<?> parent;

    public SPacketUnlockSkill() {
    }

    public SPacketUnlockSkill(SkillType<?> clicked) {
        this(clicked, null);
    }

    public SPacketUnlockSkill(SkillType<?> clicked, SkillType<?> parent) {
        this.clicked = clicked;
        this.parent = parent;
        this.hasParent = parent != null;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(hasParent);
        ByteBufUtils.writeUTF8String(buf, clicked.getRegistryName().toString());
        if (hasParent) ByteBufUtils.writeUTF8String(buf, parent.getRegistryName().toString());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        hasParent = buf.readBoolean();
        clicked = GunsRPGRegistries.SKILLS.getValue(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        if (hasParent) parent = GunsRPGRegistries.SKILLS.getValue(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
    }

    public static class Handler implements IMessageHandler<SPacketUnlockSkill, IMessage> {

        @Override
        public IMessage onMessage(SPacketUnlockSkill message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServer().addScheduledTask(() -> {
                SkillType<?> clicked = message.clicked;
                SkillType<?> parent = message.parent;
                PlayerData data = PlayerDataFactory.get(player);
                if (clicked == null) {
                    logInvalidPacket("clicked skill is null!");
                    return;
                }
                if (parent != null) {
                    if (!ModUtils.contains(clicked, parent.getChilds())) {
                        logInvalidPacket("supplied parent skill is not actual parent!");
                        return;
                    }
                    if (!PlayerDataFactory.hasActiveSkill(player, parent)) {
                        logInvalidPacket("parent skill is not unlocked!");
                        return;
                    }
                } else {
                    // iterate all skills
                    for (SkillType<?> type : GunsRPGRegistries.SKILLS) {
                        // check if clicked skill is child of one skill
                        if (ModUtils.contains(clicked, type.getChilds())) {
                            // type = parent
                            // check parent
                            if (!PlayerDataFactory.hasActiveSkill(player, type)) {
                                logInvalidPacket("parent skill is not unlocked!");
                                return;
                            }
                            break;
                        }
                    }
                }
                if (!clicked.getCriteria().isUnlockAvailable(data, clicked)) {
                    logInvalidPacket("player cannot unlock this skill yet!");
                    return;
                }
                clicked.getCriteria().onActivated(data, clicked);
                data.sync();
            });
            return null;
        }

        private void logInvalidPacket(String message) {
            GunsRPG.log.fatal("Received invalid skill activation packet - {}", message);
        }
    }
}

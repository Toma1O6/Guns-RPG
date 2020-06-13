package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.animation.Animation;
import dev.toma.gunsrpg.client.animation.AnimationManager;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CPacketSendAnimation implements IMessage {

    private int event;

    public CPacketSendAnimation() {

    }

    /**
     * @param event - the ID of animation. Should always use ID's from {@link dev.toma.gunsrpg.client.animation.Animations}!
     */
    public CPacketSendAnimation(int event) {
        this.event = event;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(event);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        event = buf.readInt();
    }

    public static class Handler implements IMessageHandler<CPacketSendAnimation, IMessage> {

        @SideOnly(Side.CLIENT)
        @Override
        public IMessage onMessage(CPacketSendAnimation message, MessageContext ctx) {
            Minecraft mc = Minecraft.getMinecraft();
            mc.addScheduledTask(() -> {
                EntityPlayer player = mc.player;
                ItemStack stack = player.getHeldItemMainhand();
                Animation animation = createAnimationFromID(message.event, player, stack);
                if(animation == null) {
                    GunsRPG.log.error("Couldn't recreate animation for {} with {} for ID {}", player.getName(), stack.getItem(), message.event);
                    return;
                }
                AnimationManager.sendNewAnimation(message.event, animation);
            });
            return null;
        }

        @SideOnly(Side.CLIENT)
        private Animation createAnimationFromID(int ID, EntityPlayer player, ItemStack stack) {
            switch (ID) {
                case Animations.RELOAD: {
                    if(stack.getItem() instanceof GunItem) {
                        return ((GunItem) stack.getItem()).createReloadAnimation(player);
                    }
                }
                case Animations.FIREMODE: {
                    return new Animations.SwitchFiremode(5);
                }
                default: throw new IllegalArgumentException("Unknown animation ID: " + ID);
            }
        }
    }
}

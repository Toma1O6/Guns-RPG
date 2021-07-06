package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.sided.ClientSideManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class CPacketSendAnimation extends AbstractNetworkPacket<CPacketSendAnimation> {

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
    public void encode(PacketBuffer buffer) {
        buffer.writeVarInt(event);
    }

    @Override
    public CPacketSendAnimation decode(PacketBuffer buffer) {
        return new CPacketSendAnimation(buffer.readVarInt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        ItemStack stack = player.getMainHandItem();
        IAnimation animation = createAnimationFromID(event, player, stack);
        if (animation == null) {
            GunsRPG.log.error("Couldn't recreate animation for {} with {} for ID {}", player.getName(), stack.getItem(), event);
            return;
        }
        ClientSideManager.processor().play(event, animation);
    }

    @OnlyIn(Dist.CLIENT)
    private IAnimation createAnimationFromID(int ID, PlayerEntity player, ItemStack stack) {
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

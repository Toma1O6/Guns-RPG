package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.PlayerShootProperties;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.util.properties.PropertyContext;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Consumer;

public class C2S_ShootPacket extends AbstractNetworkPacket<C2S_ShootPacket> {

    private final PropertyContext context;
    private final float x;
    private final float y;

    public C2S_ShootPacket() {
        this(ctx -> {}, 0, 0);
    }

    public C2S_ShootPacket(Consumer<PropertyContext> consumer, float x, float y) {
        this(PropertyContext.create(), x, y);
        consumer.accept(this.context);
    }

    private C2S_ShootPacket(PropertyContext context, float x, float y) {
        this.context = context;
        this.x = x;
        this.y = y;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        context.encode(buffer);
        buffer.writeFloat(x);
        buffer.writeFloat(y);
    }

    @Override
    public C2S_ShootPacket decode(PacketBuffer buffer) {
        PropertyContext context = PropertyContext.create();
        context.decode(buffer);
        float x = buffer.readFloat();
        float y = buffer.readFloat();
        return new C2S_ShootPacket(context, x, y);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        ItemStack stack = player.getMainHandItem();
        Item item = stack.getItem();
        if (item instanceof GunItem) {
            GunItem gun = (GunItem) item;
            gun.shoot(player.level, player, stack, new PlayerShootProperties(player, this.context, this.x, this.y));
        }
    }
}

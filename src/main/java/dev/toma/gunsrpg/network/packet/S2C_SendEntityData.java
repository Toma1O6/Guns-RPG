package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.common.entity.SynchronizableEntity;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

public class S2C_SendEntityData extends AbstractNetworkPacket<S2C_SendEntityData> {

    private final int entity;
    private final CompoundNBT data;

    public S2C_SendEntityData() {
        this(0, null);
    }

    private S2C_SendEntityData(int entity, CompoundNBT data) {
        this.entity = entity;
        this.data = data;
    }

    public static <E extends Entity & SynchronizableEntity> S2C_SendEntityData forEntity(E entity) {
        return new S2C_SendEntityData(entity.getId(), entity.serializeNetworkData());
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(entity);
        buffer.writeNbt(data);
    }

    @Override
    public S2C_SendEntityData decode(PacketBuffer buffer) {
        return new S2C_SendEntityData(buffer.readInt(), buffer.readNbt());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        Minecraft client = Minecraft.getInstance();
        ClientWorld world = client.level;
        Entity entity = world.getEntity(this.entity);
        if (entity instanceof SynchronizableEntity) {
            ((SynchronizableEntity) entity).deserializeNetworkData(this.data);
        }
    }
}

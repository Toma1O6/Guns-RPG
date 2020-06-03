package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.SPacketUpdateSightData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ScopeData {

    public static final ResourceLocation TEXTURES = GunsRPG.makeResource("textures/icons/sight.png");
    private int type;
    private int color;

    public void setNew(int type, int color) {
        this.type = type;
        this.color = color;
    }

    public void updateType() {
        type = type + 1 > 2 ? 0 : type + 1;
        NetworkManager.toServer(new SPacketUpdateSightData(type, color));
    }

    public void updateColor() {
        color = color + 1 > 4 ? 0 : color + 1;
        NetworkManager.toServer(new SPacketUpdateSightData(type, color));
    }

    public int getType() {
        return type;
    }

    public int getColor() {
        return color;
    }

    public double getTexStartX() {
        return (type * 16.0D) / 48.0D;
    }

    public double getTexStartY() {
        return (color * 16.0D) / 80.0D;
    }

    public double getTexEndX() {
        return ((type + 1) * 16.0D) / 48.0D;
    }

    public double getTexEndY() {
        return ((color + 1) * 16.0D) / 80.0D;
    }

    public NBTTagCompound write() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("type", type);
        nbt.setInteger("color", color);
        return nbt;
    }

    public void read(NBTTagCompound nbt) {
        type = nbt.getInteger("type");
        color = nbt.getInteger("color");
    }
}

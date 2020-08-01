package dev.toma.gunsrpg.world.cap;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.entity.EntityAirdrop;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class WorldDataFactory implements WorldDataCap {

    private final WorldEvent bloodmoonEvent;
    private final WorldEvent airdropEvent;

    public WorldDataFactory() {
        bloodmoonEvent = new WorldEvent("bloodmoon", 7).changeDetected((world, event) -> {
            sendUpdate(world);
            if(event.isActive()) {
                this.aggroAllEntities(world);
            }
        }).post((world, event) -> {
            if(event.isActive() && world.getWorldTime() % 200 == 0) {
                this.aggroAllEntities(world);
            }
        }).condition((world, event) -> world.getWorldTime() % 24000 >= 12500);
        airdropEvent = new WorldEvent("airdrop", 3).changeDetected((world, event) -> {
            if(!event.isActive() || world.provider.getDimension() != 0) return;
            Random random = new Random();
            if(world.playerEntities.isEmpty()) return;
            EntityPlayer player = world.playerEntities.get(random.nextInt(world.playerEntities.size()));
            EntityAirdrop airdrop = new EntityAirdrop(world);
            int x = random.nextInt(20) - random.nextInt(20);
            int z = random.nextInt(20) - random.nextInt(20);
            airdrop.setPosition(player.posX + x, player.posY + 75, player.posZ + z);
            ((EntityPlayerMP) player).connection.sendPacket(new SPacketSoundEffect(ModRegistry.GRPGSounds.PLANE_FLY_BY, SoundCategory.MASTER, airdrop.posX, player.posY, airdrop.posZ, 8.0F, 1.0F));
            world.spawnEntity(airdrop);
        });
    }

    public static WorldDataCap get(World world) {
        return world.getCapability(WorldCapProvider.CAP, null);
    }

    public static boolean isBloodMoon(World world) {
        WorldDataCap cap = get(world);
        return cap != null && cap.isBloodmoon();
    }

    @Override
    public boolean isBloodmoon() {
        return bloodmoonEvent.isActive();
    }

    @Override
    public void tick(World world) {
        long day = world.getWorldTime() / 24000L;
        WorldEvent.EVENT_LIST.forEach(worldEvent -> worldEvent.update(world, day));
    }

    private void sendUpdate(World world) {
        for(EntityPlayer player : world.playerEntities) {
            if(!bloodmoonEvent.isActive()) {
                player.sendMessage(new TextComponentString(TextFormatting.GREEN + "Bloodmoon falls"));
                ((EntityPlayerMP) player).connection.sendPacket(new SPacketSoundEffect(ModRegistry.GRPGSounds.RELAXED_2, SoundCategory.NEUTRAL, player.posX, player.posY, player.posZ, 1.0F, 1.0F));
            } else if(bloodmoonEvent.isActive() && player instanceof EntityPlayerMP) {
                ((EntityPlayerMP) player).connection.sendPacket(new SPacketSoundEffect(SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.NEUTRAL, player.posX, player.posY, player.posZ, 1.0F, 1.0F));
            }
        }
    }

    private static EntityPlayer findNearestPlayer(Entity entity, World world) {
        double lastDist = Double.MAX_VALUE;
        EntityPlayer p = null;
        for(int i = 0; i < world.playerEntities.size(); i++) {
            EntityPlayer player = world.playerEntities.get(i);
            double d = entity.getDistanceSq(player);
            if(d < lastDist) {
                lastDist = d;
                p = player;
            }
        }
        return p;
    }

    private void aggroAllEntities(World world) {
        for(Entity entity : world.loadedEntityList) {
            if(entity instanceof IMob || entity instanceof EntityWolf) {
                EntityPlayer player = findNearestPlayer(entity, world);
                if(player == null) continue;
                ((EntityLivingBase) entity).setRevengeTarget(player);
            }
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("bloodmoonEvent", bloodmoonEvent.write());
        nbt.setTag("airdropEvent", airdropEvent.write());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        bloodmoonEvent.read(nbt.hasKey("bloodmoonEvent") ? nbt.getCompoundTag("bloodmoonEvent") : new NBTTagCompound());
        airdropEvent.read(nbt.hasKey("airdropEvent") ? nbt.getCompoundTag("airdropEvent") : new NBTTagCompound());
    }

    public static class WorldEvent {

        protected static final List<WorldEvent> EVENT_LIST = new ArrayList<>();
        protected final String name;
        protected final int day;
        protected boolean isActive;
        protected boolean prevTickState;
        protected BiConsumer<World, WorldEvent> onTrigger = (w, we) -> {};
        protected BiConsumer<World, WorldEvent> postUpdate = (w, we) -> {};
        protected BiPredicate<World, WorldEvent> additionalCondition = (w, we) -> true;

        public WorldEvent(String name, int day) {
            this.name = name;
            this.day = day;
            if(!EVENT_LIST.contains(this)) {
                EVENT_LIST.add(this);
            }
        }

        public WorldEvent changeDetected(BiConsumer<World, WorldEvent> action) {
            this.onTrigger = action;
            return this;
        }

        public WorldEvent post(BiConsumer<World, WorldEvent> action) {
            this.postUpdate = action;
            return this;
        }

        public WorldEvent condition(BiPredicate<World, WorldEvent> condition) {
            this.additionalCondition = condition;
            return this;
        }

        public void update(World world, long day) {
            isActive = day > 0 && day % this.day == 0 && additionalCondition.test(world, this);
            if(prevTickState != isActive) {
                onTrigger.accept(world, this);
            }
            prevTickState = isActive;
            postUpdate.accept(world, this);
        }

        public boolean isActive() {
            return isActive;
        }

        public NBTTagCompound write() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setBoolean("active", isActive);
            nbt.setBoolean("prev", prevTickState);
            return nbt;
        }

        public void read(NBTTagCompound nbt) {
            isActive = nbt.getBoolean("active");
            prevTickState = nbt.getBoolean("prev");
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == this) {
                return true;
            } else {
                if(obj instanceof WorldEvent) {
                    String name = ((WorldEvent) obj).name;
                    return name.equals(this.name);
                }
            }
            return false;
        }
    }
}

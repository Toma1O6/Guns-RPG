package dev.toma.gunsrpg.world.cap;

import dev.toma.gunsrpg.common.entity.AirdropEntity;
import dev.toma.gunsrpg.common.init.GRPGSounds;
import dev.toma.gunsrpg.config.GRPGConfig;
import net.minecraft.entity.IAngerable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

public class WorldDataFactory implements WorldDataCap {

    private final World world;
    private final WorldEvent bloodmoonEvent;
    private final WorldEvent airdropEvent;

    public WorldDataFactory() {
        this(null);
    }

    public WorldDataFactory(World world) {
        this.world = world;
        bloodmoonEvent = new WorldEvent("bloodmoon", GRPGConfig.worldConfig.bloodmoonCycle.get()).changeDetected((w, event) -> {
            sendUpdate(w);
            if(event.isActive()) {
                this.aggroAllEntities(w);
            }
        }).post((w, event) -> {
            if(event.isActive() && w.getDayTime() % 200 == 0) {
                this.aggroAllEntities(w);
            }
        }).condition((w, event) -> w.getDayTime() >= 12500);
        airdropEvent = new WorldEvent("airdrop", GRPGConfig.worldConfig.airdropFrequency.get()).changeDetected((w, event) -> {
            if(!event.isActive() || w.dimensionType().effectsLocation().equals(DimensionType.OVERWORLD_EFFECTS)) return; // quite ugly workaround, didn't figure out anything better
            Random random = new Random();
            if(w.players().isEmpty()) return;
            PlayerEntity player = w.players().get(random.nextInt(w.players().size()));
            AirdropEntity airdrop = new AirdropEntity(w);
            int x = random.nextInt(20) - random.nextInt(20);
            int z = random.nextInt(20) - random.nextInt(20);
            BlockPos.Mutable pos = new BlockPos.Mutable((int) player.getX(x), 255, (int) player.getZ(z));
            while (w.isEmptyBlock(pos.below()) && pos.getY() > 1) {
                pos.setY(pos.getY() - 1);
            }
            airdrop.setPos(pos.getX() + 0.5, pos.getY() + 75, pos.getZ() + 0.5);
            w.playSound(null, player.getX(), player.getY(), player.getZ(), GRPGSounds.PLANE_FLY_BY, SoundCategory.MASTER, 10.0F, 1.0F);
            w.addFreshEntity(airdrop);
        });
    }

    public static WorldDataCap get(World world) {
        return world.getCapability(WorldCapProvider.CAP, null).orElse(null);
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
    public WorldEvent getBloodmoonEvent() {
        return bloodmoonEvent;
    }

    @Override
    public void tick(World world) {
        long day = world.getGameTime() / 24000L;
        WorldEvent.EVENT_LIST.forEach(worldEvent -> worldEvent.update(world, day));
    }

    private void sendUpdate(World world) {
        for(PlayerEntity player : world.players()) {
            if(!bloodmoonEvent.isActive()) {
                player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Bloodmoon falls"), Util.NIL_UUID);
                ((ServerPlayerEntity) player).connection.send(new SPlaySoundEffectPacket(GRPGSounds.RELAXED_2, SoundCategory.NEUTRAL, player.getX(), player.getY(), player.getZ(), 1.0F, 1.0F));
            } else if(bloodmoonEvent.isActive()) {
                ((ServerPlayerEntity) player).connection.send(new SPlaySoundEffectPacket(SoundEvents.END_PORTAL_SPAWN, SoundCategory.NEUTRAL, player.getX(), player.getY(), player.getZ(), 1.0F, 1.0F));
            }
        }
    }

    private void aggroAllEntities(World world) {
        double range = GRPGConfig.worldConfig.bloodMoonMobAgroRange.get();
        for (PlayerEntity player : world.players()) {
            List<LivingEntity> entities = world.getLoadedEntitiesOfClass(LivingEntity.class, new AxisAlignedBB(-range, -range, -range, range, range, range));
            for (LivingEntity living : entities) {
                if (living instanceof MonsterEntity) {
                    MonsterEntity monster = (MonsterEntity) living;
                    if (monster.getTarget() == null)
                        monster.setTarget(player);
                } else if (living instanceof IAngerable) {
                    IAngerable angerable = (IAngerable) living;
                    if (angerable.getTarget() == null)
                        angerable.setTarget(player);
                }
            }
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("bloodmoonEvent", bloodmoonEvent.write());
        nbt.put("airdropEvent", airdropEvent.write());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        bloodmoonEvent.read(nbt.contains("bloodmoonEvent") ? nbt.getCompound("bloodmoonEvent") : new CompoundNBT());
        airdropEvent.read(nbt.contains("airdropEvent") ? nbt.getCompound("airdropEvent") : new CompoundNBT());
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

        public boolean isDisabled() {
            return day < 0;
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
            if(isDisabled()) return;
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

        public CompoundNBT write() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putBoolean("active", isActive);
            nbt.putBoolean("prev", prevTickState);
            return nbt;
        }

        public void read(CompoundNBT nbt) {
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

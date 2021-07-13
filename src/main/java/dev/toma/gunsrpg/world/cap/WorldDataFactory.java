package dev.toma.gunsrpg.world.cap;

import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.world.cap.events.AirdropEventHandler;
import dev.toma.gunsrpg.world.cap.events.BloodmoonEventHandler;
import dev.toma.gunsrpg.world.cap.events.IWorldEventSpec;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;

public class WorldDataFactory implements WorldDataCap {

    private final World world;
    private final Set<IWorldEventSpec> events = new HashSet<>();
    private final BooleanSupplier isBloodmoonSupplier;

    public WorldDataFactory() {
        this(null);
    }

    public WorldDataFactory(World world) {
        this.world = world;
        IWorldEventSpec bloodmoonEvent = IWorldEventSpec.of("bloodmoon", () -> GRPGConfig.worldConfig.bloodmoonCycle.get(), new BloodmoonEventHandler());
        addEvent(bloodmoonEvent);
        isBloodmoonSupplier = bloodmoonEvent::isEventActive;
        addEvent(IWorldEventSpec.of("airdrop", () -> GRPGConfig.worldConfig.airdropFrequency.get(), new AirdropEventHandler(world)));
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
        return isBloodmoonSupplier.getAsBoolean();
    }

    @Override
    public void tick(World world) {
        long day = world.getGameTime() / 24000L;
        events.forEach(spec -> spec.tick(world, day));
    }

    private void addEvent(IWorldEventSpec spec) {
        events.add(spec);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        for (IWorldEventSpec eventSpec : events) {
            nbt.put(eventSpec.getEventName(), eventSpec.serializeNBT());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        for (IWorldEventSpec spec : events) {
            ModUtils.loadNBT(spec.getEventName(), spec, nbt);
        }
    }
}

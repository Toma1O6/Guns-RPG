package dev.toma.gunsrpg.config.world;

import dev.toma.gunsrpg.world.cap.WorldData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.function.Function;

public enum SleepRestriction {

    ALWAYS("Always", world -> PlayerEntity.SleepResult.NOT_SAFE),
    BLOODMOON("Only in bloodmoon", world -> WorldData.isBloodMoon(world) ? PlayerEntity.SleepResult.NOT_SAFE : null),
    NEVER("Never", world -> null);

    final String displayName;
    final Function<World, PlayerEntity.SleepResult> sleepResultFunction;

    SleepRestriction(String displayName, Function<World, PlayerEntity.SleepResult> function) {
        this.displayName = displayName;
        this.sleepResultFunction = function;
    }

    @Nullable
    public PlayerEntity.SleepResult getResult(World world) {
        return sleepResultFunction.apply(world);
    }
}

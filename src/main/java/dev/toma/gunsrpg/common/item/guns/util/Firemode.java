package dev.toma.gunsrpg.common.item.guns.util;

import com.google.common.collect.ImmutableMap;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.util.IFlags;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.object.LazyLoader;
import dev.toma.gunsrpg.util.object.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public enum Firemode {

    SINGLE(
            "Single",
            new LazyLoader<>(IInputEventHandler.Single::new),
            InputEventListenerType.ON_INPUT
    ),

    BURST(
            "Burst",
            new LazyLoader<>(IInputEventHandler.Burst::new),
            InputEventListenerType.ON_INPUT,
            InputEventListenerType.ON_BURST_TICK
    ),

    FULL_AUTO(
            "Full Auto",
            new LazyLoader<>(IInputEventHandler.FullAuto::new),
            InputEventListenerType.ON_TICK
    ),

    SINGLE_BARREL(
            "Single Barrel",
            new LazyLoader<>(IInputEventHandler.Single::new),
            InputEventListenerType.ON_INPUT
    ),

    DOUBLE(
            "Double",
            new LazyLoader<>(IInputEventHandler.Double::new),
            InputEventListenerType.ON_INPUT
    ),

    HOMING(
            "Homing",
            new LazyLoader<>(IInputEventHandler.Single::new),
            InputEventListenerType.ON_INPUT
    ),

    BARRAGE(
            "Barrage",
            new LazyLoader<>(IInputEventHandler.Barrage::new),
            InputEventListenerType.ON_TICK
    );

    private final String name;
    private final LazyLoader<IInputEventHandler> handler;
    private final int eventFlags;

    Firemode(String name, LazyLoader<IInputEventHandler> handler, InputEventListenerType... eventFlags) {
        this.name = name;
        this.handler = handler;
        this.eventFlags = IFlags.combine(InputEventListenerType::getFlag, eventFlags);
    }

    public static Firemode singleAndFullAuto(PlayerEntity player, Firemode firemode) {
        return switchBetween(player, firemode, SINGLE, FULL_AUTO);
    }

    public static Firemode switchBetween(PlayerEntity player, Firemode actual, Firemode f1, Firemode f2) {
        return actual == f1 ? f2 : f1;
    }

    public void triggerEvent(InputEventListenerType event, PlayerEntity player, ItemStack stack, IPlayerData data) {
        if (this.isListeningFor(event)) {
            IInputEventHandler handler = this.getHandler();
            handler.invokeEvent(event, player, stack, data);
        }
    }

    private boolean isListeningFor(InputEventListenerType event) {
        int id = event.getFlag();
        return (eventFlags & id) == id;
    }

    public IInputEventHandler getHandler() {
        return handler.get();
    }

    public static Firemode get(int id) {
        Firemode[] modes = values();
        int i = ModUtils.clamp(id, 0, modes.length - 1);
        return modes[i];
    }

    public String getName() {
        return name;
    }

    public static class ConditionalSelector implements BiFunction<PlayerEntity, Firemode, Firemode> {

        private final Map<Firemode, List<Pair<BiPredicate<PlayerEntity, IPlayerData>, Firemode>>> transitionMap;

        private ConditionalSelector(Map<Firemode, List<Pair<BiPredicate<PlayerEntity, IPlayerData>, Firemode>>> map) {
            this.transitionMap = ImmutableMap.copyOf(map);
        }

        public static Builder builder() {
            return new Builder();
        }

        @Override
        public Firemode apply(PlayerEntity player, Firemode firemode) {
            IPlayerData provider = PlayerData.getUnsafe(player);
            List<Pair<BiPredicate<PlayerEntity, IPlayerData>, Firemode>> links = transitionMap.get(firemode);
            for (Pair<BiPredicate<PlayerEntity, IPlayerData>, Firemode> pair : links) {
                BiPredicate<PlayerEntity, IPlayerData> predicate = pair.getLeft();
                if (predicate.test(player, provider)) {
                    return pair.getRight();
                }
            }
            return firemode;
        }

        public static class Builder {

            private final Map<Firemode, List<Pair<BiPredicate<PlayerEntity, IPlayerData>, Firemode>>> transitionMap = new IdentityHashMap<>();

            private Builder() {}

            public Builder addTransition(Firemode actual, BiPredicate<PlayerEntity, IPlayerData> predicate, Firemode next) {
                this.transitionMap.computeIfAbsent(actual, k -> new ArrayList<>()).add(Pair.of(predicate, next));
                return this;
            }

            public ConditionalSelector build() {
                return new ConditionalSelector(transitionMap);
            }
        }
    }
}

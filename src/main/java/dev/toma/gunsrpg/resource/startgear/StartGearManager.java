package dev.toma.gunsrpg.resource.startgear;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.resource.SingleJsonFileReloadListener;
import dev.toma.gunsrpg.resource.adapter.GearAdapter;
import dev.toma.gunsrpg.resource.adapter.ItemStackAdapter;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;

import java.util.Arrays;

public class StartGearManager extends SingleJsonFileReloadListener {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(IGear.class, new GearAdapter())
            .registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
            .create();

    private IGear gear;

    public StartGearManager() {
        super(GunsRPG.makeResource("starting_gear.json"), GSON);
    }

    @Override
    protected void apply(JsonElement element, IResourceManager resourceManager, IProfiler profiler) {
        try {
            gear = GSON.fromJson(element, IGear.class);
        } catch (JsonParseException jpe) {
            GunsRPG.log.error("Exception loading start gear data file " + jpe);
        }
    }

    public void giveStartingGear(PlayerEntity player) {
        if (gear == null) {
            GunsRPG.log.error("Couldn't give player ({}) his starting gear as gear data is null.", player.getDisplayName());
            return;
        }
        Arrays.stream(gear.getGearItems(player.level)).map(ItemStack::copy).forEach(item -> ModUtils.addItem(player, item));
    }
}

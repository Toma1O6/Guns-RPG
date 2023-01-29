package dev.toma.gunsrpg.common.debuffs;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModRegistries;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public final class DebuffDataManager extends JsonReloadListener {

    private static final Gson GSON = new Gson();

    public DebuffDataManager() {
        super(GSON, "debuff");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, IResourceManager manager, IProfiler profiler) {
        for (DebuffType<?> type : ModRegistries.DEBUFFS) {
            if (type instanceof DynamicDebuff<?>) {
                loadDebuffData(data, type);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <S> void loadDebuffData(Map<ResourceLocation, JsonElement> data, DebuffType<?> debuffType) {
        DynamicDebuff<S> dynDebuff = (DynamicDebuff<S>) debuffType;
        ResourceLocation identifier = debuffType.getRegistryName();
        JsonElement element = data.get(identifier);
        if (element == null) {
            GunsRPG.log.fatal("Unable to load debuff data for {} debuff, no data found", identifier);
            throw new IllegalStateException("Missing debuff data");
        }
        Codec<S> dataCodec = dynDebuff.getDataCodec();
        DataResult<S> dataResult = dataCodec.parse(JsonOps.INSTANCE, element);
        S dataSource = dataResult.resultOrPartial(GunsRPG.log::error).orElseThrow(() -> new IllegalStateException("Invalid data received for " + identifier + " debuff"));
        dynDebuff.onDataLoaded(dataSource);
    }
}

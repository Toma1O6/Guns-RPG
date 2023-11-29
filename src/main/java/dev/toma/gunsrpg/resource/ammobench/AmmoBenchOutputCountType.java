package dev.toma.gunsrpg.resource.ammobench;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unchecked")
public final class AmmoBenchOutputCountType<C extends AmmoBenchOutputCount> {

    private static final Map<ResourceLocation, AmmoBenchOutputCountType<?>> REGISTRY = new HashMap<>();

    public static final AmmoBenchOutputCountType<SetRangeOutputCount> SET = register(GunsRPG.makeResource("set"), new SetRangeOutputCount.Serializer());
    public static final AmmoBenchOutputCountType<AddRangeOutputCount> ADD = register(GunsRPG.makeResource("add"), new AddRangeOutputCount.Serializer());
    public static final AmmoBenchOutputCountType<MulRangeOutputCount> MUL = register(GunsRPG.makeResource("multiply"), new MulRangeOutputCount.Serializer());
    public static final AmmoBenchOutputCountType<WeightedRangeOutputCount> WEIGHTED = register(GunsRPG.makeResource("weighted_select"), new WeightedRangeOutputCount.Serializer());

    private final ResourceLocation identifier;
    private final AmmoBenchOutputCountSerializer<C> serializer;

    private AmmoBenchOutputCountType(ResourceLocation identifier, AmmoBenchOutputCountSerializer<C> serializer) {
        this.identifier = identifier;
        this.serializer = serializer;
    }

    public static <C extends AmmoBenchOutputCount> AmmoBenchOutputCountType<C> register(ResourceLocation identifier, AmmoBenchOutputCountSerializer<C> serializer) {
        AmmoBenchOutputCountType<C> type = new AmmoBenchOutputCountType<>(identifier, serializer);
        REGISTRY.put(identifier, type);
        return type;
    }

    public static <C extends AmmoBenchOutputCount> C parseJson(JsonElement element) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(element);
        ResourceLocation type = new ResourceLocation(JSONUtils.getAsString(object, "type"));
        AmmoBenchOutputCountType<C> countType = (AmmoBenchOutputCountType<C>) byKey(type)
                .orElseThrow(() -> new JsonSyntaxException("Unknown function: " + type));
        AmmoBenchOutputCountSerializer<C> serializer = countType.serializer;
        return serializer.parseJson(object);
    }

    public static <C extends AmmoBenchOutputCount> void toNetwork(C modifier, PacketBuffer buffer) {
        AmmoBenchOutputCountType<C> type = (AmmoBenchOutputCountType<C>) modifier.getType();
        buffer.writeResourceLocation(type.identifier);
        type.serializer.toNetwork(modifier, buffer);
    }


    public static <C extends AmmoBenchOutputCount> C fromNetwork(PacketBuffer buffer) {
        ResourceLocation identifier = buffer.readResourceLocation();
        AmmoBenchOutputCountType<C> type = (AmmoBenchOutputCountType<C>) byKey(identifier)
                .orElseThrow(() -> new IllegalStateException("Unable to synchronize unknown modifier - " + identifier));
        return type.serializer.fromNetwork(buffer);
    }

    public static <C extends AmmoBenchOutputCount> CompoundNBT toNbt(C modifier) {
        AmmoBenchOutputCountType<C> type = (AmmoBenchOutputCountType<C>) modifier.getType();
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("type", type.identifier.toString());
        nbt.put("data", type.serializer.toNbt(modifier));
        return nbt;
    }

    public static <C extends AmmoBenchOutputCount> C fromNbt(CompoundNBT nbt) {
        ResourceLocation identifier = new ResourceLocation(nbt.getString("type"));
        AmmoBenchOutputCountType<C> type = (AmmoBenchOutputCountType<C>) byKey(identifier)
                .orElseThrow(() -> new IllegalStateException("Unable to serialize unknown modifier - " + identifier));
        return type.serializer.fromNbt(nbt.getCompound("data"));
    }

    public static <C extends AmmoBenchOutputCount> Optional<AmmoBenchOutputCountType<C>> byKey(ResourceLocation key) {
        return Optional.ofNullable((AmmoBenchOutputCountType<C>) REGISTRY.get(key));
    }

    public interface AmmoBenchOutputCountSerializer<C extends AmmoBenchOutputCount> {

        C parseJson(JsonElement element) throws JsonParseException;

        void toNetwork(C c, PacketBuffer buffer);

        C fromNetwork(PacketBuffer buffer);

        CompoundNBT toNbt(C c);

        C fromNbt(CompoundNBT nbt);
    }
}

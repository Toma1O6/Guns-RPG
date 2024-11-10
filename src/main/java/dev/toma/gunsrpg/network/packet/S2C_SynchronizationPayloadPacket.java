package dev.toma.gunsrpg.network.packet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.skill.*;
import dev.toma.gunsrpg.client.screen.skill.SkillTreeScreen;
import dev.toma.gunsrpg.common.debuffs.DynamicDebuff;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.common.perk.PerkRegistry;
import dev.toma.gunsrpg.common.skills.core.*;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.resource.perks.PerkConfiguration;
import dev.toma.gunsrpg.resource.skill.SkillPropertyLoader;
import dev.toma.gunsrpg.util.Lifecycle;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class S2C_SynchronizationPayloadPacket extends AbstractNetworkPacket<S2C_SynchronizationPayloadPacket> {

    private static final Gson GSON = new GsonBuilder().create();
    // skills
    private final List<DataContext> data;
    // perks
    private final List<Perk> perks;
    // perk cfg
    private final PerkConfiguration configuration;
    // dynamic debuffs
    private final Map<ResourceLocation, Object> debuffData;

    public S2C_SynchronizationPayloadPacket() {
        this(null, null, null, null);
    }

    public static <T> S2C_SynchronizationPayloadPacket makePayloadPacket() {
        Lifecycle lifecycle = GunsRPG.getModLifecycle();
        List<DataContext> data = ModRegistries.SKILLS.getValues().stream().filter(S2C_SynchronizationPayloadPacket::filterAndLogInvalid).map(DataContext::new).collect(Collectors.toList());
        List<Perk> perks = new ArrayList<>(PerkRegistry.getRegistry().getPerks());
        PerkConfiguration configuration = lifecycle.getPerkManager().configLoader.getConfiguration();
        Map<ResourceLocation, Object> debuffData = lifecycle.getDebuffDataManager().getDebuffData();
        return new S2C_SynchronizationPayloadPacket(data, perks, configuration, debuffData);
    }

    private S2C_SynchronizationPayloadPacket(List<DataContext> data, List<Perk> perks, PerkConfiguration configuration, Map<ResourceLocation, Object> debuffData) {
        this.data = data;
        this.perks = perks;
        this.configuration = configuration;
        this.debuffData = debuffData;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        // skill props
        buffer.writeInt(data.size());
        for (DataContext context : data) {
            context.encode(buffer);
        }
        // perks
        buffer.writeInt(perks.size());
        perks.forEach(perk -> perk.encode(buffer));
        // perk cfg
        configuration.encode(buffer);
        // debuff data
        buffer.writeInt(debuffData.size());
        debuffData.forEach((location, data) -> encodeDebuffData(buffer, location, data));
    }

    @Override
    public S2C_SynchronizationPayloadPacket decode(PacketBuffer buffer) {
        // skill props
        int l = buffer.readInt();
        List<DataContext> list = new ArrayList<>();
        for (int i = 0; i < l; i++) {
            list.add(new DataContext(buffer));
        }
        // perks
        List<Perk> perks = new ArrayList<>();
        int perkCount = buffer.readInt();
        for (int i = 0; i < perkCount; i++) {
            perks.add(Perk.decode(buffer));
        }
        PerkConfiguration configuration = PerkConfiguration.decode(buffer);
        // debuffs
        Map<ResourceLocation, Object> map = decodeDebuffData(buffer);
        return new S2C_SynchronizationPayloadPacket(list, perks, configuration, map);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        // Skills
        data.forEach(this::assign);
        SkillTreeScreen.Cache.invalidate();

        // perks
        PerkRegistry registry = PerkRegistry.getRegistry();
        registry.dropRegistry();
        perks.forEach(perk -> registry.register(perk.getPerkId(), perk));

        // configuration
        GunsRPG.getModLifecycle().getPerkManager().configLoader.setConfiguration(configuration);

        // debuff
        debuffData.forEach(this::handleDebuffData);
    }

    @SuppressWarnings("unchecked")
    private <S extends ISkill> void assign(DataContext context) {
        SkillType<S> owner = (SkillType<S>) context.owner;
        SkillPropertyLoader.ILoadResult<S> result = (SkillPropertyLoader.ILoadResult<S>) context.result;
        owner.onDataAssign(result);
    }

    @SuppressWarnings("unchecked")
    private <D> void encodeDebuffData(PacketBuffer buffer, ResourceLocation location, D data) {
        buffer.writeResourceLocation(location);

        DynamicDebuff<D> type = (DynamicDebuff<D>) ModRegistries.DEBUFFS.getValue(location);
        Codec<D> codec = type.getDataCodec();
        DataResult<INBT> encodeResult = codec.encodeStart(NBTDynamicOps.INSTANCE, data);
        CompoundNBT nbt = encodeResult.result()
                .map(t -> (CompoundNBT) t)
                .orElse(new CompoundNBT());
        buffer.writeNbt(nbt);
    }

    private Map<ResourceLocation, Object> decodeDebuffData(PacketBuffer buffer) {
        Map<ResourceLocation, Object> map = new HashMap<>();
        int count = buffer.readInt();
        for (int i = 0; i < count; i++) {
            ResourceLocation location = buffer.readResourceLocation();
            CompoundNBT nbt = buffer.readNbt();
            Object data = readDebuffData(location, nbt);
            map.put(location, data);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private <D> D readDebuffData(ResourceLocation location, CompoundNBT nbt) {
        DynamicDebuff<D> debuff = (DynamicDebuff<D>) ModRegistries.DEBUFFS.getValue(location);
        Codec<D> codec = debuff.getDataCodec();
        DataResult<D> result = codec.parse(NBTDynamicOps.INSTANCE, nbt);
        return result.result().orElseThrow(IllegalStateException::new);
    }

    @SuppressWarnings("unchecked")
    private <D> void handleDebuffData(ResourceLocation location, D data) {
        DynamicDebuff<D> debuff = (DynamicDebuff<D>) ModRegistries.DEBUFFS.getValue(location);
        debuff.onDataLoaded(data);
    }

    private static boolean filterAndLogInvalid(SkillType<?> type) {
        ISkillHierarchy<?> hierarchy = type.getHierarchy();
        if (hierarchy == null) {
            GunsRPG.log.fatal("Skipping sync of {} skill, no data were loaded!", type.getRegistryName());
        }
        return hierarchy != null;
    }

    private static class DataContext {

        private SkillType<?> owner;
        private SkillPropertyLoader.ILoadResult<?> result;

        DataContext(SkillType<?> owner) {
            this.owner = owner;
        }

        DataContext(PacketBuffer buffer) {
            this.owner = fromResource(buffer.readResourceLocation());
            boolean disabled = buffer.readBoolean();
            ISkillHierarchy<?> hierarchy = decodeHierarchy(buffer);
            ISkillProperties properties = decodeProperties(buffer);
            this.result = new SkillPropertyLoader.Result<>(hierarchy, properties, disabled);
        }

        public void encode(PacketBuffer to) {
            ISkillHierarchy<?> hierarchy = owner.getHierarchy();
            ISkillProperties properties = owner.getProperties();
            to.writeResourceLocation(owner.getRegistryName());
            to.writeBoolean(owner.isDisabled());
            encodeHierarchy(hierarchy, to);
            encodeProperties(properties, to);
        }

        private ISkillHierarchy<?> decodeHierarchy(PacketBuffer buffer) {
            SkillCategory category = buffer.readEnum(SkillCategory.class);
            byte flags = buffer.readByte();
            SkillType<?> parent = null;
            SkillType<?> override = null;
            SkillType<?>[] children = null;
            SkillType<?>[] extensions = null;
            if ((flags & 0b0001) != 0) {
                parent = fromResource(buffer.readResourceLocation());
            }
            if ((flags & 0b0010) != 0) {
                override = fromResource(buffer.readResourceLocation());
            }
            if ((flags & 0b0100) != 0) {
                int l = buffer.readVarInt();
                children = new SkillType[l];
                for (int i = 0; i < l; i++) {
                    children[i] = fromResource(buffer.readResourceLocation());
                }
            }
            if ((flags & 0b1000) != 0) {
                int l = buffer.readVarInt();
                extensions = new SkillType[l];
                for (int i = 0; i < l; i++) {
                    extensions[i] = fromResource(buffer.readResourceLocation());
                }
            }
            return new SkillHierarchy<>(category, parent, override, children, extensions);
        }

        private ISkillProperties decodeProperties(PacketBuffer buffer) {
            int level = buffer.readVarInt();
            int price = buffer.readVarInt();
            ResourceLocation id = buffer.readResourceLocation();
            String string = buffer.readUtf();
            JsonElement element = new JsonParser().parse(string);
            ITransactionValidatorFactory<?, ?> factory = TransactionValidatorRegistry.getValidatorFactory(id);
            ITransactionValidator validator = TransactionValidatorRegistry.getTransactionValidator(factory, element);
            return new SkillProperties(level, price, validator);
        }

        private void encodeHierarchy(ISkillHierarchy<?> hierarchy, PacketBuffer buffer) {
            // mandatory
            SkillCategory category = hierarchy.getCategory();
            // optional
            SkillType<?> parent = hierarchy.getParent();
            SkillType<?> override = hierarchy.getOverride();
            SkillType<?>[] children = hierarchy.getChildren();
            SkillType<?>[] extension = hierarchy.getExtensions();

            byte flags = 0b0000;
            flags |= parent != null     ? 0b0001 : 0;
            flags |= override != null   ? 0b0010 : 0;
            flags |= children != null   ? 0b0100 : 0;
            flags |= extension != null  ? 0b1000 : 0;

            buffer.writeEnum(category);
            buffer.writeByte(flags);
            if (parent != null) {
                buffer.writeResourceLocation(parent.getRegistryName());
            }
            if (override != null) {
                buffer.writeResourceLocation(override.getRegistryName());
            }
            if (children != null) {
                buffer.writeVarInt(children.length);
                for (SkillType<?> child : children) {
                    buffer.writeResourceLocation(child.getRegistryName());
                }
            }
            if (extension != null) {
                buffer.writeVarInt(extension.length);
                for (SkillType<?> ext : extension) {
                    buffer.writeResourceLocation(ext.getRegistryName());
                }
            }
        }

        private void encodeProperties(ISkillProperties properties, PacketBuffer buffer) {
            ITransactionValidator validator = properties.getTransactionValidator();
            buffer.writeVarInt(properties.getRequiredLevel());
            buffer.writeVarInt(properties.getPrice());
            buffer.writeResourceLocation(validator.getId());
            String jsonString = GSON.toJson(validator.getData());
            buffer.writeUtf(jsonString);
        }

        private SkillType<?> fromResource(ResourceLocation location) {
            return ModRegistries.SKILLS.getValue(location);
        }
    }
}

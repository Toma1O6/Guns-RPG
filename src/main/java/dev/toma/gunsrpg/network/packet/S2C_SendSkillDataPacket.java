package dev.toma.gunsrpg.network.packet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidatorFactory;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.*;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.resource.skill.SkillPropertyLoader;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class S2C_SendSkillDataPacket extends AbstractNetworkPacket<S2C_SendSkillDataPacket> {

    private static final Gson GSON = new GsonBuilder().create();
    private final List<DataContext> data;

    public S2C_SendSkillDataPacket() {
        this.data = new ArrayList<>();
    }

    public S2C_SendSkillDataPacket(Collection<SkillType<?>> data) {
        this.data = data.stream().map(DataContext::new).collect(Collectors.toList());
    }

    private S2C_SendSkillDataPacket(List<DataContext> data) {
        this.data = data;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeInt(data.size());
        for (DataContext context : data) {
            context.encode(buffer);
        }
    }

    @Override
    public S2C_SendSkillDataPacket decode(PacketBuffer buffer) {
        int l = buffer.readInt();
        List<DataContext> list = new ArrayList<>();
        for (int i = 0; i < l; i++) {
            list.add(new DataContext(buffer));
        }
        return new S2C_SendSkillDataPacket(list);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        data.forEach(this::assign);
    }

    @SuppressWarnings("unchecked")
    private <S extends ISkill> void assign(DataContext context) {
        SkillType<S> owner = (SkillType<S>) context.owner;
        SkillPropertyLoader.ILoadResult<S> result = (SkillPropertyLoader.ILoadResult<S>) context.result;
        owner.onDataAssign(result);
    }

    private static class DataContext {

        private SkillType<?> owner;
        private SkillPropertyLoader.ILoadResult<?> result;

        DataContext(SkillType<?> owner) {
            this.owner = owner;
        }

        DataContext(PacketBuffer buffer) {
            this.owner = fromResource(buffer.readResourceLocation());
            ISkillHierarchy<?> hierarchy = decodeHierarchy(buffer);
            ISkillProperties properties = decodeProperties(buffer);
            this.result = new SkillPropertyLoader.Result<>(hierarchy, properties);
        }

        public void encode(PacketBuffer to) {
            ISkillHierarchy<?> hierarchy = owner.getHierarchy();
            ISkillProperties properties = owner.getProperties();
            to.writeResourceLocation(owner.getRegistryName());
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

package dev.toma.gunsrpg.common.quests.quest.area;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.common.quests.adapters.MobSpawnerAdapter;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SetEntityControllerProcessor implements IMobSpawnProcessor {

    private final MobSpawnProcessorType<SetEntityControllerProcessor> type;
    private final EntityType<? extends LivingEntity> controllerType;
    private final List<IMobSpawnProcessor> processorList;

    private SetEntityControllerProcessor(MobSpawnProcessorType<SetEntityControllerProcessor> type, EntityType<? extends LivingEntity> controllerType, List<IMobSpawnProcessor> processorList) {
        this.type = type;
        this.controllerType = controllerType;
        this.processorList = processorList;
    }

    @Override
    public MobSpawnProcessorType<SetEntityControllerProcessor> getType() {
        return type;
    }

    @Override
    public void processMobSpawn(LivingEntity entity, IMobTargettingContext targettingContext) {
        World world = entity.level;
        LivingEntity controller = controllerType.create(world);
        controller.setPosAndOldPos(entity.getX(), entity.getY(), entity.getZ());
        world.addFreshEntity(controller);
        processorList.forEach(processor -> processor.processMobSpawn(controller, targettingContext));
        controller.startRiding(entity);
        targettingContext.processMobSpawn(controller);
    }

    public static final class Serializer implements IMobSpawnProcessorSerializer<SetEntityControllerProcessor> {

        @Override
        public SetEntityControllerProcessor deserialize(MobSpawnProcessorType<SetEntityControllerProcessor> type, JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            ResourceLocation id = new ResourceLocation(JSONUtils.getAsString(object, "controller"));
            EntityType<? extends LivingEntity> entity = MobSpawnerAdapter.tryParseAsLivingEntity(id);
            List<IMobSpawnProcessor> processorList = Collections.emptyList();
            if (object.has("processors")) {
                processorList = JsonHelper.deserializeAsList("processors", object, MobSpawnerAdapter::resolveSpawnProcessor);
            }
            return new SetEntityControllerProcessor(type, entity, processorList);
        }

        @Override
        public void toNbt(SetEntityControllerProcessor processor, CompoundNBT nbt) {
            nbt.putString("controller", processor.controllerType.getRegistryName().toString());
            ListNBT list = new ListNBT();
            processor.processorList.stream().map(this::serializeProcessor).forEach(list::add);
            nbt.put("processors", list);
        }

        @SuppressWarnings("unchecked")
        @Override
        public SetEntityControllerProcessor fromNbt(MobSpawnProcessorType<SetEntityControllerProcessor> type, CompoundNBT nbt) {
            ResourceLocation entityId = new ResourceLocation(nbt.getString("controller"));
            EntityType<?> entityType = ForgeRegistries.ENTITIES.getValue(entityId);
            ListNBT listNBT = nbt.getList("processors", Constants.NBT.TAG_COMPOUND);
            List<IMobSpawnProcessor> list = listNBT.stream().<IMobSpawnProcessor>map(inbt -> MobSpawnProcessorType.fromNbt((CompoundNBT) inbt)).collect(Collectors.toList());
            return new SetEntityControllerProcessor(type, (EntityType<? extends LivingEntity>) entityType, list);
        }

        @SuppressWarnings("unchecked")
        private <P extends IMobSpawnProcessor> CompoundNBT serializeProcessor(P processor) {
            MobSpawnProcessorType<P> type = (MobSpawnProcessorType<P>) processor.getType();
            return type.toNbt(processor);
        }
    }
}

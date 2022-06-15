package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.trigger.ITriggerHandler;
import dev.toma.gunsrpg.common.quests.trigger.Trigger;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class UniqueMobKillsConditionProvider extends AbstractQuestConditionProvider<UniqueMobKillsConditionProvider.ConditionTracker> {

    public UniqueMobKillsConditionProvider(QuestConditionProviderType<? extends UniqueMobKillsConditionProvider> type) {
        super(type);
    }

    public static UniqueMobKillsConditionProvider fromNbt(QuestConditionProviderType<UniqueMobKillsConditionProvider> type, CompoundNBT nbt) {
        return new UniqueMobKillsConditionProvider(type);
    }

    @Override
    public ConditionTracker makeConditionInstance() {
        return new ConditionTracker(this);
    }

    final class ConditionTracker implements IQuestCondition, ITriggerHandler {

        private final Set<EntityType<?>> killedMobs = new HashSet<>();
        private final ITextComponent[] descriptors;

        public ConditionTracker(UniqueMobKillsConditionProvider provider) {
            this.descriptors = expandWithShortLocalizations(new TranslationTextComponent(provider.getLocalizationString()));
        }

        @Override
        public ITextComponent getDescriptor(boolean shortDesc) {
            return descriptors[shortDesc ? 1 : 0];
        }

        @Override
        public boolean isValid(PlayerEntity player, IPropertyReader reader) {
            Entity entity = reader.getProperty(QuestProperties.ENTITY);
            if (entity instanceof MonsterEntity) {
                EntityType<?> type = entity.getType();
                return !killedMobs.contains(type);
            }
            return false;
        }

        @Override
        public void handleTriggerSuccess(Trigger trigger, IPropertyReader reader) {
            Entity entity = reader.getProperty(QuestProperties.ENTITY);
            EntityType<?> type = entity.getType();
            killedMobs.add(type);
        }

        @Override
        public IQuestConditionProvider<?> getProviderType() {
            return UniqueMobKillsConditionProvider.this;
        }

        @Override
        public void saveData(CompoundNBT nbt) {
            ListNBT list = new ListNBT();
            killedMobs.stream().map(type -> StringNBT.valueOf(type.getRegistryName().toString())).forEach(list::add);
            nbt.put("killedMobs", list);
        }

        @Override
        public void loadData(CompoundNBT nbt) {
            killedMobs.clear();
            ListNBT list = nbt.getList("killedMobs", Constants.NBT.TAG_STRING);
            list.stream().map(inbt -> {
                ResourceLocation location = new ResourceLocation(inbt.getAsString());
                return ForgeRegistries.ENTITIES.getValue(location);
            }).forEach(killedMobs::add);
        }
    }
}

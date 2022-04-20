package dev.toma.gunsrpg.common.quests.reward;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class RewardProviderType {

    private static final Map<ResourceLocation, RewardProviderType> MAP = new HashMap<>();

    private final ResourceLocation id;
    private final IQuestRewardResolver resolver;

    public RewardProviderType(ResourceLocation id, IQuestRewardResolver resolver) {
        this.id = id;
        this.resolver = resolver;

        MAP.put(id, this);
    }

    RewardProviderType(String id, IQuestRewardResolver resolver) {
        this(GunsRPG.makeResource(id), resolver);
    }

    public IQuestRewardResolver getResolver() {
        return resolver;
    }

    public static RewardProviderType getById(ResourceLocation id) {
        return MAP.get(id);
    }

    static {
        new RewardProviderType("item", new IQuestRewardResolver.SingleItem());
        new RewardProviderType("item_list", new IQuestRewardResolver.ItemList());
        new RewardProviderType("item_group", new IQuestRewardResolver.ItemGroup());
    }
}

package dev.toma.gunsrpg.integration.questing.condition.instance;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.api.common.data.IDebuffs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.integration.questing.condition.provider.DebuffConditionProvider;
import dev.toma.questing.common.component.condition.instance.Condition;
import dev.toma.questing.common.component.trigger.Events;
import dev.toma.questing.common.component.trigger.ResponseType;
import dev.toma.questing.common.component.trigger.event.DeathEvent;
import dev.toma.questing.common.quest.ConditionRegisterHandler;
import dev.toma.questing.common.quest.instance.Quest;
import dev.toma.questing.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class DebuffCondition implements Condition {

    public static final Codec<DebuffCondition> CODEC = DebuffConditionProvider.CODEC
            .xmap(DebuffCondition::new, t -> t.provider).fieldOf("provider").codec();
    private final DebuffConditionProvider provider;

    public DebuffCondition(DebuffConditionProvider provider) {
        this.provider = provider;
    }

    @Override
    public void registerTriggerResponders(ConditionRegisterHandler conditionRegisterHandler) {
        conditionRegisterHandler.register(Events.DEATH_EVENT, this::handleResponse);
        conditionRegisterHandler.register(Events.DAMAGE_EVENT, this::handleResponse);
    }

    @Override
    public DebuffConditionProvider getProvider() {
        return provider;
    }

    private ResponseType handleResponse(DeathEvent event, World level, Quest quest) {
        DamageSource source = event.getSource();
        Entity origin = source.getEntity();
        if (Utils.checkIfEntityIsPartyMember(origin, quest.getParty())) {
            PlayerEntity player = (PlayerEntity) origin;
            return PlayerData.get(player).map(data -> {
                IDebuffs debuffs = data.getDebuffControl();
                boolean hasDebuff = debuffs.hasDebuff(this.provider.getDebuff());
                return hasDebuff != this.provider.mustBeActive() ? this.provider.getDefaultFailureResponse() : ResponseType.OK;
            }).orElse(ResponseType.SKIP);
        }
        return ResponseType.SKIP;
    }
}

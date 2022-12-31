package dev.toma.gunsrpg.integration.questing.condition.instance;

import com.mojang.serialization.Codec;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.integration.questing.condition.provider.HeadshotConditionProvider;
import dev.toma.gunsrpg.util.properties.Properties;
import dev.toma.questing.common.component.condition.ConditionRegisterHandler;
import dev.toma.questing.common.component.condition.instance.Condition;
import dev.toma.questing.common.component.trigger.Events;
import dev.toma.questing.common.component.trigger.ResponseType;
import dev.toma.questing.common.component.trigger.event.DeathEvent;
import dev.toma.questing.common.quest.Quest;
import dev.toma.questing.utils.Utils;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;

public class HeadshotCondition implements Condition {

    public static final Codec<HeadshotCondition> CODEC = HeadshotConditionProvider.CODEC
            .xmap(HeadshotCondition::new, t -> t.provider).fieldOf("provider").codec();
    private final HeadshotConditionProvider provider;

    public HeadshotCondition(HeadshotConditionProvider provider) {
        this.provider = provider;
    }

    @Override
    public void registerTriggerResponders(ConditionRegisterHandler conditionRegisterHandler) {
        conditionRegisterHandler.register(Events.DAMAGE_EVENT, this::getResponse);
        conditionRegisterHandler.register(Events.DEATH_EVENT, this::getResponse);
    }

    @Override
    public HeadshotConditionProvider getProvider() {
        return provider;
    }

    private ResponseType getResponse(DeathEvent event, Quest quest) {
        DamageSource source = event.getSource();
        Entity origin = source.getEntity();
        Entity projectile = source.getDirectEntity();
        if (Utils.checkIfEntityIsPartyMember(origin, quest.getParty())) {
            if (!(projectile instanceof AbstractProjectile)) {
                return this.provider.getDefaultFailureResponse();
            }
            AbstractProjectile abstractProjectile = (AbstractProjectile) projectile;
            boolean isHeadshot = abstractProjectile.getProperty(Properties.IS_HEADSHOT);
            return isHeadshot != this.provider.mustBeHeadshot() ? this.provider.getDefaultFailureResponse() : ResponseType.OK;
        }
        return ResponseType.SKIP;
    }
}

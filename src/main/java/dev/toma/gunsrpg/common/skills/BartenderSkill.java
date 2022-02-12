package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.common.attribute.IValueFormatter;
import dev.toma.gunsrpg.common.skills.core.DescriptionContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.util.text.ITextComponent;

public class BartenderSkill extends BasicSkill implements IDescriptionProvider {

    private final DescriptionContainer container;
    private final int level;

    public BartenderSkill(SkillType<? extends BartenderSkill> type, int level) {
        super(type);
        this.level = level;
        this.container = new DescriptionContainer(type);
        TieredReward.REWARDS[level - 1].forDescription(container);
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        return SkillUtil.Localizations.generateAndMerge(desiredLineCount, this.getType(), container.getLines());
    }

    public int getRewardAmount() {
        return level == 5 ? 2 : 1;
    }

    public static class TieredReward {

        private static final TieredReward[] REWARDS = new TieredReward[5];

        static {
            TieredReward r1 = new TieredReward().setAmmoMultiplier(0.1F).save(1);
            TieredReward r2 = baseOff(r1).setAmmoMultiplier(0.2F).setShownRewards(4).save(2);
            TieredReward r3 = baseOff(r2).setAmmoMultiplier(0.3F).setMedReward(1).setOrbReward(1).save(3);
            TieredReward r4 = baseOff(r3).setAmmoMultiplier(0.4F).setShownRewards(5).setPerkBookReward(1).setExplosiveAmmo(0.5F).save(4);
            baseOff(r4).setAmmoMultiplier(0.5F).setAirdropFlare(1).setOrbReward(2).save(5);
        }

        private float ammoMultiplier;
        private float explosiveAmmo;
        private int shownRewards;
        private int medReward;
        private int orbReward;
        private int perkBookReward;
        private int airdropFlare;

        public static TieredReward baseOff(TieredReward other) {
            TieredReward reward = new TieredReward();
            reward.setAmmoMultiplier(other.ammoMultiplier);
            reward.setExplosiveAmmo(other.explosiveAmmo);
            reward.setShownRewards(other.shownRewards);
            reward.setMedReward(other.medReward);
            reward.setOrbReward(other.orbReward);
            reward.setPerkBookReward(other.perkBookReward);
            reward.setAirdropFlare(other.airdropFlare);
            return reward;
        }

        public TieredReward setAmmoMultiplier(float ammoMultiplier) {
            this.ammoMultiplier = ammoMultiplier;
            return this;
        }

        public TieredReward setExplosiveAmmo(float explosiveAmmo) {
            this.explosiveAmmo = explosiveAmmo;
            return this;
        }

        public TieredReward setShownRewards(int shownRewards) {
            this.shownRewards = shownRewards;
            return this;
        }

        public TieredReward setMedReward(int medReward) {
            this.medReward = medReward;
            return this;
        }

        public TieredReward setOrbReward(int orbReward) {
            this.orbReward = orbReward;
            return this;
        }

        public TieredReward setPerkBookReward(int perkBookReward) {
            this.perkBookReward = perkBookReward;
            return this;
        }

        public TieredReward setAirdropFlare(int airdropFlare) {
            this.airdropFlare = airdropFlare;
            return this;
        }

        public TieredReward save(int level) {
            REWARDS[level - 1] = this;
            return this;
        }

        public void forDescription(DescriptionContainer container) {
            container.addProperty("ammo", IValueFormatter.PERCENT.formatAttributeValue(ammoMultiplier));
            if (explosiveAmmo > 0) {
                container.addProperty("explosives", IValueFormatter.PERCENT.formatAttributeValue(explosiveAmmo));
            }
            if (shownRewards > 0) {
                container.addProperty("visibleRewards", shownRewards);
            }
            if (medReward > 0) {
                container.addProperty("meds", medReward);
            }
            if (orbReward > 0) {
                container.addProperty("orbs", orbReward);
            }
            if (perkBookReward > 0) {
                container.addProperty("perks", perkBookReward);
            }
            if (airdropFlare > 0) {
                container.addProperty("flare", airdropFlare);
            }
        }
    }
}

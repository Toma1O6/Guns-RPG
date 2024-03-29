package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.attribute.*;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.attribute.AttributeModifier;
import dev.toma.gunsrpg.common.attribute.AttributeOps;
import dev.toma.gunsrpg.common.attribute.AttributeTarget;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.skills.core.DescriptionContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

import java.util.UUID;
import java.util.function.Consumer;

public class BartenderSkill extends SimpleSkill implements IDescriptionProvider {

    private final DescriptionContainer container;
    private final IAttributeTarget[] targets;
    private final int rewardCount;

    public BartenderSkill(SkillType<? extends BartenderSkill> type, Consumer<TieredReward.Builder> consumer) {
        super(type);
        this.container = new DescriptionContainer(type);
        TieredReward.Builder builder = new TieredReward.Builder(container);
        consumer.accept(builder);
        TieredReward reward = builder.build();
        this.targets = reward.toAttributes();
        this.rewardCount = reward.rewardCount;
    }

    public int getRewardCount() {
        return rewardCount;
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        return SkillUtil.Localizations.generateAndMerge(desiredLineCount, this.getType(), container.getLines());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onPurchase(PlayerEntity player) {
        PlayerData.get(player).ifPresent(data -> {
            ISkillProvider skillProvider = data.getSkillProvider();
            BartenderSkill skill = ModUtils.firstNonnull(SkillUtil.getTopHierarchySkill((SkillType<BartenderSkill>) this.getType(), skillProvider), this);
            IAttributeProvider provider = data.getAttributes();
            for (IAttributeTarget target : skill.targets) {
                applyTarget(target, provider);
            }
        });
    }

    @Override
    public void onDeactivate(PlayerEntity player) {
        PlayerData.get(player).ifPresent(data -> {
            IAttributeProvider provider = data.getAttributes();
            for (IAttributeTarget target : targets) {
                IAttributeId attributeId = target.getTargetAttribute();
                IAttributeModifier modifier = target.getModifier();
                provider.getAttribute(attributeId).removeModifier(modifier);
            }
        });
    }

    private void applyTarget(IAttributeTarget target, IAttributeProvider provider) {
        IAttributeModifier modifier = target.getModifier();
        IAttributeId attributeId = target.getTargetAttribute();
        IAttribute attribute = provider.getAttribute(attributeId);
        attribute.addModifier(modifier);
    }

    public static class TieredReward {

        private static final UUID AMMO = UUID.fromString("7FDA7B68-DF6D-4CB1-BD1D-E2285D9FEE81");
        private static final UUID EXPLOSIVE = UUID.fromString("5E8266EC-2E1F-4158-885B-C786847EAF22");
        private static final UUID COUNT = UUID.fromString("B66CF62D-5BA4-47CD-BCC0-9E0939F1337B");
        private static final UUID MEDS = UUID.fromString("C98B145A-C8B8-4930-AA6C-5C016C9EDAF7");
        private static final UUID ORBS = UUID.fromString("A8F1394A-6E77-4F46-9DC1-23438B60203E");
        private static final UUID BOOKS = UUID.fromString("28ABF8DA-049F-4B98-BAEE-7F68F3DA7E5B");
        private static final UUID FLARES = UUID.fromString("DE956C1C-4E5A-45DB-87B6-C84F31D38D05");

        private final float ammoMultiplier;
        private final float explosiveAmmo;
        private final int shownRewards;
        private final int medReward;
        private final int orbReward;
        private final int perkBookReward;
        private final int airdropFlare;
        private final int rewardCount;

        private TieredReward(Builder builder) {
            ammoMultiplier = builder.ammoMultiplier;
            explosiveAmmo = builder.explosiveAmmo;
            shownRewards = builder.shownRewards;
            medReward = builder.medReward;
            orbReward = builder.orbReward;
            perkBookReward = builder.perkBookReward;
            airdropFlare = builder.airdropFlare;
            rewardCount = builder.rewardCount;
        }

        public IAttributeTarget[] toAttributes() {
            return new IAttributeTarget[] {
                    AttributeTarget.create(new AttributeModifier(AMMO, AttributeOps.MULB, ammoMultiplier), Attribs.QUEST_AMMO),
                    AttributeTarget.create(new AttributeModifier(EXPLOSIVE, AttributeOps.MULB, explosiveAmmo), Attribs.QUEST_EXPLOSIVES),
                    AttributeTarget.create(new AttributeModifier(COUNT, AttributeOps.SUM, shownRewards), Attribs.QUEST_VISIBLE_REWARD),
                    AttributeTarget.create(new AttributeModifier(MEDS, AttributeOps.SUM, medReward), Attribs.QUEST_MEDS),
                    AttributeTarget.create(new AttributeModifier(ORBS, AttributeOps.SUM, orbReward), Attribs.QUEST_ORBS),
                    AttributeTarget.create(new AttributeModifier(BOOKS, AttributeOps.SUM, perkBookReward), Attribs.QUEST_PERKBOOK),
                    AttributeTarget.create(new AttributeModifier(FLARES, AttributeOps.SUM, airdropFlare), Attribs.QUEST_FLARE)
            };
        }

        public static void bartender1(Builder builder) {
            builder.ammoMultiplier(0.10F);
        }

        public static void bartender2(Builder builder) {
            builder.defaults(0.10F, 0.0F, 0, 0, 0, 0, 0)
                    .ammoMultiplier(0.20F)
                    .showCount(1);
        }

        public static void bartender3(Builder builder) {
            builder.defaults(0.20F, 0.0F, 1, 0, 0, 0, 0)
                    .ammoMultiplier(0.30F)
                    .meds(1)
                    .orbs(1);
        }

        public static void bartender4(Builder builder) {
            builder.defaults(0.30F, 0.0F, 1, 1, 1, 0, 0)
                    .ammoMultiplier(0.40F)
                    .showCount(2)
                    .perkBook(1)
                    .explosives(0.5F);
        }

        public static void bartender5(Builder builder) {
            builder.defaults(0.40F, 0.5F, 2, 1, 1, 1, 0)
                    .ammoMultiplier(0.50F)
                    .flare(1)
                    .orbs(2)
                    .rewardCount(2);
        }

        public static class Builder {

            private final DescriptionContainer ref;
            private float ammoMultiplier;
            private float explosiveAmmo;
            private int shownRewards;
            private int medReward;
            private int orbReward;
            private int perkBookReward;
            private int airdropFlare;
            private int rewardCount;

            private Builder(DescriptionContainer container) {
                this.ref = container;
                this.rewardCount = 1;
            }

            public Builder ammoMultiplier(float ammoMultiplier) {
                this.ref.addProperty("ammo", IValueFormatter.PERCENT.formatAttributeValue(ammoMultiplier - this.ammoMultiplier));
                this.ammoMultiplier = ammoMultiplier;
                return this;
            }

            public Builder explosives(float explosives) {
                this.ref.addProperty("explosives", IValueFormatter.PERCENT.formatAttributeValue(explosives - this.explosiveAmmo));
                this.explosiveAmmo = explosives;
                return this;
            }

            public Builder showCount(int shownRewards) {
                this.ref.addProperty("rewards", shownRewards + (int) Attribs.QUEST_VISIBLE_REWARD.getBaseValue());
                this.shownRewards = shownRewards;
                return this;
            }

            public Builder meds(int meds) {
                this.ref.addProperty("meds", meds - this.medReward);
                this.medReward = meds;
                return this;
            }

            public Builder orbs(int orbs) {
                this.ref.addProperty("orbs", orbs - this.orbReward);
                this.orbReward = orbs;
                return this;
            }

            public Builder perkBook(int books) {
                this.ref.addProperty("perkBook", books - this.perkBookReward);
                this.perkBookReward = books;
                return this;
            }

            public Builder flare(int flares) {
                this.ref.addProperty("flares", flares - this.airdropFlare);
                this.airdropFlare = flares;
                return this;
            }

            public Builder rewardCount(int rewardCount) {
                this.rewardCount = rewardCount;
                return this;
            }

            public Builder defaults(float ammoMultiplier, float explosiveAmmo, int shownRewards, int medReward, int orbReward, int perkBookReward, int airdropFlare) {
                this.ammoMultiplier = ammoMultiplier;
                this.explosiveAmmo = explosiveAmmo;
                this.shownRewards = shownRewards;
                this.medReward = medReward;
                this.orbReward = orbReward;
                this.perkBookReward = perkBookReward;
                this.airdropFlare = airdropFlare;
                return this;
            }

            private TieredReward build() {
                return new TieredReward(this);
            }
        }
    }
}

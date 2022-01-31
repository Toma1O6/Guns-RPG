package dev.toma.gunsrpg.resource.gunner;

import com.google.common.base.Preconditions;
import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.common.entity.ZombieGunnerEntity;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.Difficulty;

public final class GunnerLoadoutInstance {

    // general
    private final int weight;
    // weapon
    private final GunItem weapon;
    private final IDifficultyProperty<IAmmoMaterial> ammo;
    private final int magCapacity;
    private final IDifficultyProperty<Integer> reloadTime;
    private final IDifficultyProperty<Integer> firerate;
    // ai
    private final IDifficultyProperty<Float> damageMultiplier;
    private final float baseInaccuracy;
    private final float accuracyBonus;
    private final int burstSize;
    private final IDifficultyProperty<Integer> burstDelay;
    // cosmetic
    private final int capColor;

    private GunnerLoadoutInstance(Builder builder) {
        weight = builder.weight;
        weapon = builder.weapon;
        ammo = builder.ammo;
        magCapacity = builder.magCapacity;
        reloadTime = builder.reloadTime;
        firerate = builder.firerate;
        damageMultiplier = builder.damageMultiplier;
        baseInaccuracy = builder.baseInaccuracy;
        accuracyBonus = builder.accuracyBonus;
        burstSize = builder.burstSize;
        burstDelay = builder.burstDelay;
        capColor = builder.capColor;
    }

    public void applyGear(ZombieGunnerEntity entity, Difficulty difficulty) {
        ItemStack stack = new ItemStack(weapon);
        IAmmoMaterial material = ammo.getProperty(difficulty);
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("ammo", Integer.MAX_VALUE);
        nbt.putString("material", material.getMaterialID().toString());
        stack.setTag(nbt);
        entity.setItemSlot(EquipmentSlotType.MAINHAND, stack);
        entity.setDamageMultiplier(damageMultiplier.getProperty(difficulty));
        entity.setInaccuracy(this.getCalculatedInaccuracy(difficulty));
        entity.setFirerate(firerate.getProperty(difficulty));
        entity.setMagCapacity(magCapacity);
        entity.setReloadTime(reloadTime.getProperty(difficulty));
        entity.setBurstSize(burstSize);
        entity.setBurstDelay(burstDelay.getProperty(difficulty));
        entity.setCapColor(capColor);
    }

    public int getWeight() {
        return weight;
    }

    public float getCalculatedInaccuracy(Difficulty difficulty) {
        return baseInaccuracy + (difficulty.getId() * accuracyBonus);
    }

    public static class Builder {

        // general
        private int weight;
        // weapon
        private GunItem weapon;
        private IDifficultyProperty<IAmmoMaterial> ammo;
        private int magCapacity;
        private IDifficultyProperty<Integer> reloadTime;
        private IDifficultyProperty<Integer> firerate;
        // ai
        private IDifficultyProperty<Float> damageMultiplier;
        private float baseInaccuracy;
        private float accuracyBonus;
        private int burstSize;
        private IDifficultyProperty<Integer> burstDelay = diff -> 0;
        // cosmetic
        private int capColor = -1;

        public void setupGlobal(GunnerGlobalProperties properties) {
            this.damageMultiplier = new SimpleDifficultyProperty<>(properties.getDamageMultiplier());
            this.baseInaccuracy = properties.getInaccuracy();
            this.accuracyBonus = properties.getAccuracyBonus();
        }

        public void weight(int weight) {
            this.weight = weight;
        }

        public void weapon(GunItem weapon) {
            this.weapon = weapon;
        }

        public void ammo(IDifficultyProperty<IAmmoMaterial> ammo) {
            this.ammo = ammo;
        }

        public void magCapacity(int magCapacity) {
            this.magCapacity = magCapacity;
        }

        public void reloadTime(IDifficultyProperty<Integer> reloadTime) {
            this.reloadTime = reloadTime;
        }

        public void firerate(IDifficultyProperty<Integer> firerate) {
            this.firerate = firerate;
        }

        public void damageMultiplier(IDifficultyProperty<Float> damageMultiplier) {
            this.damageMultiplier = damageMultiplier;
        }

        public void inaccuracy(float inaccuracy) {
            this.baseInaccuracy = inaccuracy;
        }

        public void accuracyBonus(float accuracyBonus) {
            this.accuracyBonus = accuracyBonus;
        }

        public void burstSize(int burstSize) {
            this.burstSize = burstSize;
        }

        public void burstDelay(IDifficultyProperty<Integer> burstDelay) {
            this.burstDelay = burstDelay;
        }

        public void cap(int capColor) {
            this.capColor = capColor;
        }

        public GunnerLoadoutInstance buildLoadout() {
            if (burstSize == 0) burstSize = magCapacity;
            Preconditions.checkState(weight > 0, "Weight must be bigger than 0");
            Preconditions.checkNotNull(weapon, "Weapon cannot be null");
            Preconditions.checkNotNull(ammo, "Ammo cannot be null");
            Preconditions.checkState(magCapacity > 0, "Magazine capacity must be bigger than 0");
            Preconditions.checkNotNull(reloadTime, "Reload time cannot be null");
            Preconditions.checkNotNull(firerate, "Firerate cannot be null");
            Preconditions.checkNotNull(damageMultiplier, "Damage multiplier cannot be null");
            Preconditions.checkState(baseInaccuracy >= 0, "Inaccuracy cannot be smaller than 0");
            Preconditions.checkState(accuracyBonus >= 0, "Accuracy bonus cannot be smaller than 0");
            Preconditions.checkState((baseInaccuracy - (3 * accuracyBonus)) >= 0, "(Inaccuracy-3*bonus) cannot be smaller than 0");
            Preconditions.checkState(burstSize >= 0, "Burst size cannot be smaller than 0");
            Preconditions.checkNotNull(burstDelay, "Burst delay cannot be null");
            return new GunnerLoadoutInstance(this);
        }
    }
}

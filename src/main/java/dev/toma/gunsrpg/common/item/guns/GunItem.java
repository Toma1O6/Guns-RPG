package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.client.animation.AnimationProcessor;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.client.animation.IHandRenderer;
import dev.toma.gunsrpg.client.animation.impl.AimingAnimation;
import dev.toma.gunsrpg.client.animation.impl.RecoilAnimation;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.BulletEntity;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.item.BaseItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.reload.IReloadManager;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagerMagazine;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.config.gun.IWeaponConfig;
import dev.toma.gunsrpg.sided.ClientSideManager;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class GunItem extends BaseItem implements IHandRenderer {

    protected static Random random = new Random();
    protected final GunType gunType;
    protected final Map<AmmoMaterial, Integer> materialDamageBonusMap;

    public GunItem(String name, GunType type, Properties properties) {
        super(name, properties.tab(ModTabs.ITEM_TAB).stacksTo(1));
        this.gunType = type;
        this.fillAmmoMaterialData(materialDamageBonusMap = new HashMap<>());
    }

    public final SoundEvent getWeaponShootSound(LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            return getShootSound((PlayerEntity) entity);
        }
        return getEntityShootSound(entity);
    }

    public abstract SkillType<?> getRequiredSkill();

    public abstract IWeaponConfig getWeaponConfig();

    public abstract void fillAmmoMaterialData(Map<AmmoMaterial, Integer> data);

    @OnlyIn(Dist.CLIENT)
    public abstract IAnimation createReloadAnimation(PlayerEntity player);

    protected SoundEvent getShootSound(PlayerEntity entity) {
        return SoundEvents.LEVER_CLICK;
    }

    protected SoundEvent getEntityShootSound(LivingEntity entity) {
        return SoundEvents.LEVER_CLICK;
    }

    public SoundEvent getReloadSound(PlayerEntity player) {
        return SoundEvents.LEVER_CLICK;
    }

    public final Firemode getFiremode(ItemStack stack) {
        createNBT(stack);
        return Firemode.get(stack.getTag().getInt("firemode"));
    }

    public boolean switchFiremode(ItemStack stack, PlayerEntity player) {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public AimingAnimation createAimAnimation() {
        return new AimingAnimation(-0.54F, 0.06F, 0.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public void onShoot(PlayerEntity player, ItemStack stack) {
        AnimationProcessor processor = ClientSideManager.instance().processor();
        processor.stop(Animations.RELOAD);
        processor.play(Animations.RECOIL, RecoilAnimation.newInstance(5));
    }

    public IReloadManager getReloadManager() {
        return ReloadManagerMagazine.MANAGER;
    }

    public boolean isSilenced(PlayerEntity player) {
        return false;
    }

    public int getFirerate(PlayerEntity player) {
        return 0;
    }

    public int getMaxAmmo(PlayerEntity player) {
        return 0;
    }

    public int getReloadTime(PlayerEntity player) {
        return 50;
    }

    public float getVerticalRecoil(PlayerEntity player) {
        return this.getWeaponConfig().getVerticalRecoil();
    }

    public float getHorizontalRecoil(PlayerEntity player) {
        boolean f = random.nextBoolean();
        float v = this.getWeaponConfig().getHorizontalRecoil();
        return f ? v : -v;
    }

    public void onHitEntity(BulletEntity bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {

    }

    public void onKillEntity(BulletEntity bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {

    }

    public void shootBullet(World world, LivingEntity entity, ItemStack stack) {
        BulletEntity bullet = new BulletEntity(ModEntities.BULLET.get(), world, entity, this, stack);
        boolean aim = entity instanceof PlayerEntity && PlayerData.getUnsafe((PlayerEntity) entity).getAimInfo().isAiming();
        float pitch = entity.xRot + (aim ? 0.0F : (random.nextFloat() - random.nextFloat()) * 2);
        float yaw = entity.yRot + (aim ? 0.0F : (random.nextFloat() - random.nextFloat()) * 2);
        bullet.fire(pitch, yaw, getWeaponConfig().getVelocity());
        world.addFreshEntity(bullet);
    }

    public final void shoot(World world, LivingEntity entity, ItemStack stack) {
        shoot(world, entity, stack, getWeaponShootSound(entity));
    }

    public final void shoot(World world, LivingEntity entity, ItemStack stack, SoundEvent event) {
        Item item = stack.getItem();
        CooldownTracker tracker = null;
        if (entity instanceof PlayerEntity) {
            tracker = ((PlayerEntity) entity).getCooldowns();
            if (tracker.isOnCooldown(item)) {
                return;
            }
        }
        this.shootBullet(world, entity, stack);
        this.setAmmoCount(stack, this.getAmmo(stack) - 1);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), event, SoundCategory.MASTER, 15.0F, 1.0F);
        if (tracker != null) {
            tracker.addCooldown(item, this.getFirerate((PlayerEntity) entity));
        }
    }

    public final int getDamageBonus(ItemStack stack) {
        AmmoMaterial material = this.getMaterialFromNBT(stack);
        Integer v = material != null ? materialDamageBonusMap.get(material) : 0;
        return v == null ? 0 : v;
    }

    public AmmoType getAmmoType() {
        return gunType.getAmmoType();
    }

    public GunType getGunType() {
        return gunType;
    }

    public void createNBT(ItemStack stack) {
        if (stack.hasTag()) {
            return;
        }
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("ammo", 0);
        nbt.putInt("firemode", 0);
        stack.setTag(nbt);
    }

    public AmmoMaterial getMaterialFromNBT(ItemStack stack) {
        this.createNBT(stack);
        CompoundNBT nbt = stack.getTag();
        int id = Math.max(0, Math.min(AmmoMaterial.values().length, nbt.getInt("material")));
        return nbt.contains("material") ? AmmoMaterial.values()[id] : null;
    }

    public int getAmmo(ItemStack stack) {
        this.createNBT(stack);
        return stack.getTag().getInt("ammo");
    }

    public void setAmmoCount(ItemStack stack, int count) {
        this.createNBT(stack);
        CompoundNBT nbt = stack.getTag();
        nbt.putInt("ammo", Math.max(0, count));
    }

    public boolean hasAmmo(ItemStack stack) {
        return this.getAmmo(stack) > 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flags) {
        AmmoMaterial material = getMaterialFromNBT(stack);
        list.add(new StringTextComponent("Ammo type: " + (material != null ? material.getColor() + material.name().toUpperCase() : "???")));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return false;
    }
}

package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.client.animation.BulletEjectAnimation;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.BulletEntity;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.item.BaseItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterialManager;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.GunType;
import dev.toma.gunsrpg.common.item.guns.util.MaterialContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.util.object.RGB2TextFormatting;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.api.AnimationList;
import lib.toma.animations.api.IAnimationEntry;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Random;
import java.util.Set;

public abstract class GunItem extends BaseItem implements IAnimationEntry {

    protected static Random random = new Random();
    protected final GunType gunType;
    private final MaterialContainer container = new MaterialContainer();

    public GunItem(String name, GunType type, Properties properties) {
        super(name, properties.tab(ModTabs.ITEM_TAB).stacksTo(1));
        this.gunType = type;
        this.fillAmmoMaterialData(container);
    }

    public final SoundEvent getWeaponShootSound(LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            return getShootSound((PlayerEntity) entity);
        }
        return getEntityShootSound(entity);
    }

    public abstract SkillType<?> getRequiredSkill();

    public abstract IWeaponConfig getWeaponConfig();

    public abstract void fillAmmoMaterialData(MaterialContainer container);

    @OnlyIn(Dist.CLIENT)
    public abstract ResourceLocation getReloadAnimation(PlayerEntity player);

    protected SoundEvent getShootSound(PlayerEntity entity) {
        return SoundEvents.LEVER_CLICK;
    }

    protected SoundEvent getEntityShootSound(LivingEntity entity) {
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
    public ResourceLocation getAimAnimationPath(ItemStack stack, PlayerEntity player) {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public ResourceLocation getBulletEjectAnimationPath() {
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public void onShoot(PlayerEntity player, ItemStack stack) {
        ResourceLocation bulletEjectPath = getBulletEjectAnimationPath();
        BulletEjectAnimation animation = AnimationUtils.createAnimation(bulletEjectPath, BulletEjectAnimation::new);
        AnimationList.enqueue(ModAnimations.BULLET_EJECTION, animation);
    }

    @Override
    public IRenderConfig right() {
        return IRenderConfig.empty();
    }

    @Override
    public IRenderConfig left() {
        return IRenderConfig.empty();
    }

    @Override
    public boolean disableVanillaAnimations() {
        return true;
    }

    public IReloadManager getReloadManager(PlayerEntity player) {
        return ReloadManagers.fullMagLoading();
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
        IAmmoMaterial material = this.getMaterialFromNBT(stack);
        return material != null ? container.getAdditionalDamage(material) : 0;
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

    public IAmmoMaterial getMaterialFromNBT(ItemStack stack) {
        this.createNBT(stack);
        CompoundNBT nbt = stack.getTag();
        AmmoMaterialManager materialManager = AmmoMaterialManager.get();
        return materialManager.parse(nbt);
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
        IAmmoMaterial material = getMaterialFromNBT(stack);
        TextFormatting formatting = material != null ? RGB2TextFormatting.getClosestFormat(material.getTextColor()) : TextFormatting.GRAY;
        list.add(new StringTextComponent("Ammo material: " + formatting + (material != null ? material.getDisplayName().getString() : "???")));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return false;
    }

    public final Set<IAmmoMaterial> getCompatibleMaterials() {
        return container.getCompatible();
    }

    public MaterialContainer getContainer() {
        return container;
    }
}

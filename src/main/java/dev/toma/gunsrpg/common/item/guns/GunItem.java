package dev.toma.gunsrpg.common.item.guns;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.IWeaponConfig;
import dev.toma.gunsrpg.client.animation.BulletEjectAnimation;
import dev.toma.gunsrpg.client.animation.ModAnimations;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.entity.projectile.*;
import dev.toma.gunsrpg.common.item.BaseItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterialManager;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.reload.ReloadManagers;
import dev.toma.gunsrpg.common.item.guns.util.Firemode;
import dev.toma.gunsrpg.common.item.guns.util.MaterialContainer;
import dev.toma.gunsrpg.common.item.guns.util.WeaponCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.object.RGB2TextFormatting;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.api.AnimationList;
import lib.toma.animations.api.IAnimationEntry;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityType;
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

public abstract class GunItem extends BaseItem implements IAnimationEntry, IProjectileEjector {

    protected static Random random = new Random();
    protected final WeaponCategory weaponCategory;
    private final MaterialContainer container = new MaterialContainer();

    public GunItem(String name, WeaponCategory category, Properties properties) {
        super(name, properties.tab(ModTabs.ITEM_TAB).stacksTo(1));
        this.weaponCategory = category;
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

    public double getNoiseMultiplier(IAttributeProvider provider) {
        return provider.getAttributeValue(Attribs.WEAPON_NOISE);
    }

    public double getHeadshotMultiplier(IAttributeProvider provider) {
        return provider.getAttributeValue(Attribs.HEADSHOT_DAMAGE);
    }

    public float getVerticalRecoil(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.RECOIL_CONTROL).floatValue();
    }

    public float getHorizontalRecoil(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.RECOIL_CONTROL).floatValue();
    }

    public int getMaxAmmo(IAttributeProvider provider) {
        return 1;
    }

    public int getReloadTime(IAttributeProvider provider) {
        return provider.getAttribute(Attribs.RELOAD_SPEED).intValue();
    }

    public int getFirerate(IAttributeProvider provider) {
        return 1;
    }

    public IPenetrationConfig createPenetrationConfig() {
        return IPenetrationConfig.none();
    }

    public IProjectileConfig createProjectileConfig(LivingEntity source) {
        return new StandartProjectileConfig(source, this);
    }

    @Override
    public Projectile createProjectile(EntityType<? extends Projectile> type, World level, LivingEntity source) {
        IProjectileConfig config = this.createProjectileConfig(source);
        Projectile projectile = new Projectile(type, level, config);
        float inaccuracy = source instanceof PlayerEntity ? PlayerData.getValueSafe((PlayerEntity) source, data -> data.getAimInfo().isAiming() ? 0.0F : 0.3F, getMobInaccuracy(level)) : getMobInaccuracy(level);
        projectile.fire(source.xRot, source.yRot, inaccuracy);
        return projectile;
    }

    @Override
    public boolean disableVanillaAnimations() {
        return true;
    }

    public IReloadManager getReloadManager(PlayerEntity player) {
        return ReloadManagers.fullMagLoading();
    }

    public void onHitEntity(Projectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {

    }

    public void onKillEntity(Projectile bullet, LivingEntity victim, ItemStack stack, LivingEntity shooter) {

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
        Projectile projectile = this.createProjectile(null, world, entity);
        world.addFreshEntity(projectile);
        this.setAmmoCount(stack, this.getAmmo(stack) - 1);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), event, SoundCategory.MASTER, 15.0F, 1.0F);
        if (tracker != null) {
            tracker.addCooldown(item, this.getFirerate(PlayerData.getUnsafe((PlayerEntity) entity).getAttributes()));
        }
    }

    public final int getDamageBonus(ItemStack stack) {
        IAmmoMaterial material = this.getMaterialFromNBT(stack);
        return material != null ? container.getAdditionalDamage(material) : 0;
    }

    public AmmoType getAmmoType() {
        return weaponCategory.getAmmoType();
    }

    public WeaponCategory getWeaponCategory() {
        return weaponCategory;
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

    protected float getMobInaccuracy(World level) {
        return 0.5F;
    }

    protected boolean isSilenced(PlayerEntity player) {
        return PlayerData.getValueSafe(player, data -> getNoiseMultiplier(data.getAttributes()) <= 0.2F, false);
    }
}

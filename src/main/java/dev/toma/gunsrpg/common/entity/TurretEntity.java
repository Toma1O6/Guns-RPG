package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IAmmoProvider;
import dev.toma.gunsrpg.common.container.TurretContainer;
import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import dev.toma.gunsrpg.common.entity.projectile.Bullet;
import dev.toma.gunsrpg.common.entity.projectile.IReaction;
import dev.toma.gunsrpg.common.entity.projectile.Rocket;
import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.ammo.IReactiveMaterial;
import dev.toma.gunsrpg.common.item.guns.util.IEntityTrackingGun;
import dev.toma.gunsrpg.network.NetworkManager;
import dev.toma.gunsrpg.network.packet.S2C_SendEntityData;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.locate.ILocatorPredicate;
import dev.toma.gunsrpg.util.locate.ammo.ItemLocator;
import dev.toma.gunsrpg.util.properties.Properties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public final class TurretEntity extends Entity implements SynchronizableEntity {

    public static final int INVENTORY_SIZE = 8;
    public static final ITextComponent SCREEN_TITLE = new TranslationTextComponent("screen.gunsrpg.turret");
    private static final Vector3d GRAVITY_VEC = new Vector3d(0.0, -0.3, 0.0);
    private final TurretType turretType;
    private final Inventory inventory = new Inventory(INVENTORY_SIZE);
    private UUID owner;
    private final Set<UUID> whitelist = new HashSet<>();
    private final Map<UUID, String> nameCache = new HashMap<>();
    private TargettingMode targettingMode;
    private int targetSearchDelay;
    private int attackDelay;
    private LivingEntity target;

    public TurretEntity(EntityType<? extends TurretEntity> type, TurretType turretType, World world) {
        super(type, world);
        this.turretType = turretType;
        this.targettingMode = TargettingMode.NONE;
    }

    public static TurretEntity smg(EntityType<? extends TurretEntity> type, World world) {
        return new TurretEntity(type, Turrets.SMG, world);
    }

    public static TurretEntity ar(EntityType<? extends TurretEntity> type, World world) {
        return new TurretEntity(type, Turrets.AR, world);
    }

    public static TurretEntity rocket(EntityType<? extends TurretEntity> type, World world) {
        return new TurretEntity(type, Turrets.ROCKET, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (!targettingMode.isDisabled()) {
            if (target == null) {
                if (--targetSearchDelay <= 0) {
                    refreshTarget();
                }
            } else if (validateTargetExistsOrRefresh()) {
                rotateTowardsTarget(turretType.rotationSpeed);
                if (--attackDelay <= 0 && isFacingTarget()) {
                    attackDelay = turretType.attackDelay;
                    ItemStack ammo = findAmmo();
                    if (!ammo.isEmpty()) {
                        IAmmoProvider provider = (IAmmoProvider) ammo.getItem();
                        if (!level.isClientSide) {
                            AbstractProjectile projectile = turretType.shootHandler.createProjectile(this, ammo, provider);
                            level.addFreshEntity(projectile);
                            ammo.shrink(1);
                            level.playSound(null, getX(), getY(), getZ(), turretType.shootSound, SoundCategory.NEUTRAL, turretType.shootSoundVolume, 1.0F);
                        }
                        if (turretType.adjustTargetAfterShooting) {
                            refreshTarget();
                        }
                    }
                }
            }
        }
        if (!level.isClientSide && isInLava()) {
            remove();
        }
        this.setDeltaMovement(GRAVITY_VEC);
        move(MoverType.SELF, this.getDeltaMovement());
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return turretType.dropStack.copy();
    }

    @Override
    public ActionResultType interact(PlayerEntity player, Hand hand) {
        if (this.isAuthorized(player) && !level.isClientSide) {
            int entityId = getId();
            NetworkHooks.openGui(
                    (ServerPlayerEntity) player,
                    new SimpleNamedContainerProvider((containerId, playerInventory, user) ->
                            new TurretContainer(ModContainers.TURRET_CONTAINER.get(), containerId, playerInventory, entityId, inventory), SCREEN_TITLE),
                    buffer -> buffer.writeInt(entityId)
            );
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        Entity entity = source.getDirectEntity();
        if (entity == null || source.isExplosion()) {
            return false;
        }
        if (entity instanceof PlayerEntity && !level.isClientSide) {
            PlayerEntity player = (PlayerEntity) entity;
            if (this.isAuthorized(player)) {
                ItemStack stack = this.turretType.dropStack.copy();
                ItemEntity itemEntity = new ItemEntity(level, getX(), getY(), getZ(), stack);
                level.addFreshEntity(itemEntity);
                InventoryHelper.dropContents(level, blockPosition(), inventory);
                remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        nbt.putInt("targetDelay", targetSearchDelay);
        nbt.putInt("attackDelay", attackDelay);
        saveSharedNbtData(nbt);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        targetSearchDelay = nbt.getInt("targetDelay");
        attackDelay = nbt.getInt("attackDelay");
        loadSharedNbtData(nbt);
    }

    @Override
    public CompoundNBT serializeNetworkData() {
        CompoundNBT nbt = new CompoundNBT();
        if (target != null) {
            nbt.putInt("target", target.getId());
        }
        saveSharedNbtData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNetworkData(CompoundNBT tag) {
        if (tag.contains("target")) {
            Entity entity = level.getEntity(tag.getInt("target"));
            if (entity instanceof LivingEntity) {
                this.target = (LivingEntity) entity;
            }
        }
        loadSharedNbtData(tag);
    }

    private void saveSharedNbtData(CompoundNBT tag) {
        tag.putInt("targetMode", targettingMode.ordinal());
        ListNBT inventoryTag = new ListNBT();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                CompoundNBT slotTag = new CompoundNBT();
                slotTag.putInt("slot", i);
                slotTag.put("item", stack.serializeNBT());
                inventoryTag.add(slotTag);
            }
        }
        tag.put("inventory", inventoryTag);
        ListNBT names = new ListNBT();
        for (Map.Entry<UUID, String> entry : nameCache.entrySet()) {
            CompoundNBT entryTag = new CompoundNBT();
            entryTag.putUUID("uuid", entry.getKey());
            entryTag.putString("name", entry.getValue());
            names.add(entryTag);
        }
        tag.put("whitelist", names);
    }

    private void loadSharedNbtData(CompoundNBT tag) {
        targettingMode = ModUtils.getEnumByIdSafely(tag.getInt("targetMode"), TargettingMode.class);
        inventory.clearContent();
        whitelist.clear();
        nameCache.clear();
        ListNBT inventoryTag = tag.getList("inventory", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < inventoryTag.size(); i++) {
            CompoundNBT slotTag = inventoryTag.getCompound(i);
            int slot = slotTag.getInt("slot");
            ItemStack stack = ItemStack.of(slotTag.getCompound("item"));
            inventory.setItem(slot, stack);
        }
        ListNBT whitelist = tag.getList("whitelist", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < whitelist.size(); i++) {
            CompoundNBT whitelistEntry = whitelist.getCompound(i);
            UUID uuid = whitelistEntry.getUUID("uuid");
            String name = whitelistEntry.getString("name");
            nameCache.put(uuid, name);
            this.whitelist.add(uuid);
        }
    }

    @Nullable
    public LivingEntity getTarget() {
        return target;
    }

    public boolean isAuthorized(PlayerEntity player) {
        return this.owner == null || player.getUUID().equals(this.owner);
    }

    public void setOwner(UUID owner) {
        if (owner != null) {
            this.owner = owner;
            this.addToWhitelist(owner);
        }
    }

    public void addToWhitelist(UUID uuid) {
        PlayerEntity player = level.getPlayerByUUID(uuid);
        if (player != null) {
            this.whitelist.add(uuid);
            this.nameCache.put(uuid, player.getDisplayName().getString());
            synchronizeToClients();
        }
    }

    public void removeFromWhitelist(UUID uuid) {
        this.whitelist.remove(uuid);
        this.nameCache.remove(uuid);
        synchronizeToClients();
    }

    public Map<UUID, String> getNameCache() {
        return nameCache;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Predicate<ItemStack> getAmmoFilter() {
        return this.turretType.ammoFilter;
    }

    public TargettingMode getTargettingMode() {
        return targettingMode;
    }

    public void setTargettingMode(TargettingMode targettingMode) {
        if (this.isValidTargettingMode(targettingMode)) {
            this.targettingMode = targettingMode;
            this.synchronizeToClients();
        }
    }

    public boolean isValidTargettingMode(TargettingMode targettingMode) {
        return this.turretType.availableTargettingModes.contains(targettingMode);
    }

    public Set<TargettingMode> getAvailableTargettingModes() {
        return this.turretType.availableTargettingModes;
    }

    private ItemStack findAmmo() {
        return ItemLocator.findFirst(inventory, turretType.ammoFilter);
    }

    private void refreshTarget() {
        targetSearchDelay = 20;
        attackDelay = Math.max(turretType.attackDelay, 20);
        AxisAlignedBB aabb = this.getBoundingBox().inflate(turretType.xzSearchRange, turretType.ySearchRange, turretType.xzSearchRange);
        List<LivingEntity> availableEntities = level.getEntitiesOfClass(LivingEntity.class, aabb, entity -> entity.canSee(this) && targettingMode.isValidTarget(this, entity));
        this.target = availableEntities.isEmpty() ? null : turretType.targetSelector.getTarget(availableEntities, this);
        synchronizeToClients();
    }

    private boolean validateTargetExistsOrRefresh() {
        if (target == null || !target.isAlive() || level.getEntity(target.getId()) == null || !target.canSee(this) || !turretType.targetValidator.canConsiderAsTarget(this, target)) {
            refreshTarget();
        }
        return target != null;
    }

    private void synchronizeToClients() {
        if (level.isClientSide)
            return;
        NetworkManager.sendToAllTracking(this, S2C_SendEntityData.forEntity(this));
    }

    private boolean isFacingTarget() {
        if (target == null)
            return false;
        Vector3d viewVec = this.getViewVector(1.0F).normalize();
        Vector3d posDiffVec = new Vector3d(target.getX() - this.getX(), target.getY() - this.getEyeY(), target.getZ() - this.getZ());
        double length = posDiffVec.length();
        posDiffVec = posDiffVec.normalize();
        double dot = viewVec.dot(posDiffVec);
        return dot > 1.0D - 0.25D / length && target.canSee(this);
    }

    private void rotateTowardsTarget(float rotationSpeed) {
        Vector3d pos = this.position();
        Vector3d targetPos = target.position();
        float xDiff = getXRotationDifference(pos.x, pos.y, pos.z, targetPos.x, targetPos.y, targetPos.z);
        float yDiff = getYRotationDifference(pos.x, pos.z, targetPos.x, targetPos.z);
        this.xRotO = this.xRot;
        this.yRotO = this.yRot;
        this.xRot = rotateTowards(this.xRot, xDiff, rotationSpeed);
        this.yRot = rotateTowards(this.yRot, yDiff, rotationSpeed);
    }

    private static float rotateTowards(float rotation, float rotationDifference, float rotationSpeed) {
        float f = MathHelper.degreesDifference(rotation, rotationDifference);
        float f1 = MathHelper.clamp(f, -rotationSpeed, rotationSpeed);
        return rotation + f1;
    }

    private static float getXRotationDifference(double x1, double y1, double z1, double targetX, double targetY, double targetZ) {
        double x = targetX - x1;
        double y = targetY - y1;
        double z = targetZ - z1;
        double xzDist = Math.sqrt(x * x + z * z);
        return (float) (-(MathHelper.atan2(y, xzDist) * (180.0 / Math.PI)));
    }

    private static float getYRotationDifference(double x1, double z1, double targetX, double targetZ) {
        double x = targetX - x1;
        double z = targetZ - z1;
        return (float) (MathHelper.atan2(z, x) * (180.0 / Math.PI)) - 90.0F;
    }

    public static final class TurretType {

        private final ItemStack dropStack;
        private final double xzSearchRange;
        private final double ySearchRange;
        private final TargetValidator targetValidator;
        private final TargetSelector targetSelector;
        private final Set<TargettingMode> availableTargettingModes;
        private final float rotationSpeed;
        private final int attackDelay;
        private final ILocatorPredicate<ItemStack> ammoFilter;
        private final ShootHandler shootHandler;
        private final SoundEvent shootSound;
        private final float shootSoundVolume;
        private final boolean adjustTargetAfterShooting;

        private TurretType(Builder builder) {
            this.dropStack = builder.dropStack;
            this.xzSearchRange = builder.xzSearchRange;
            this.ySearchRange = builder.ySearchRange;
            this.targetValidator = builder.targetValidator;
            this.targetSelector = builder.targetSelector;
            this.availableTargettingModes = builder.availableModes;
            this.rotationSpeed = builder.rotationSpeed;
            this.attackDelay = builder.attackDelay;
            this.ammoFilter = builder.ammoFilter;
            this.shootHandler = builder.shootHandler;
            this.shootSound = builder.shootSound;
            this.shootSoundVolume = builder.shootSoundVolume;
            this.adjustTargetAfterShooting = builder.adjustTargetAfterShooting;
        }

        @FunctionalInterface
        public interface ShootHandler {
            AbstractProjectile createProjectile(TurretEntity turret, ItemStack stack, IAmmoProvider provider);
        }

        @FunctionalInterface
        public interface TargetValidator {

            boolean canConsiderAsTarget(TurretEntity turret, LivingEntity entity);

            static TargetValidator and(TargetValidator v1, TargetValidator v2) {
                return (turret, entity) -> v1.canConsiderAsTarget(turret, entity) && v2.canConsiderAsTarget(turret, entity);
            }
        }

        @FunctionalInterface
        public interface TargetSelector {
            LivingEntity getTarget(List<LivingEntity> list, TurretEntity turret);
        }

        public static final class Builder {

            private ItemStack dropStack = ItemStack.EMPTY;
            private double xzSearchRange = 16.0;
            private double ySearchRange = 4.0;
            private TargetValidator targetValidator = (turret, target) -> target != null;
            private TargetSelector targetSelector = (list, level) -> ModUtils.getRandomListElement(list, level.random);
            private final Set<TargettingMode> availableModes = EnumSet.of(TargettingMode.NONE);
            private float rotationSpeed = 10.0F;
            private int attackDelay = 5;
            private ILocatorPredicate<ItemStack> ammoFilter;
            private ShootHandler shootHandler;
            private SoundEvent shootSound;
            private float shootSoundVolume = 10.0F;
            private boolean adjustTargetAfterShooting = true;

            private Builder() {}

            public static Builder create() {
                return new Builder();
            }

            public Builder dropAs(ItemStack stack) {
                this.dropStack = stack;
                return this;
            }

            public Builder targetRange(double xz, double y) {
                this.xzSearchRange = xz;
                this.ySearchRange = y;
                return this;
            }

            public Builder targetValidator(TargetValidator validator) {
                this.targetValidator = validator;
                return this;
            }

            public Builder targetSelector(TargetSelector selector) {
                this.targetSelector = selector;
                return this;
            }

            public Builder addTargettingModes(Set<TargettingMode> modes) {
                this.availableModes.addAll(modes);
                return this;
            }

            public Builder rotationSpeed(float rotationSpeed) {
                this.rotationSpeed = rotationSpeed;
                return this;
            }

            public Builder attackDelay(int delay) {
                this.attackDelay = delay;
                return this;
            }

            public Builder requiredAmmo(AmmoType type) {
                this.ammoFilter = stack -> {
                    if (stack.getItem() instanceof IAmmoProvider) {
                        IAmmoProvider provider = (IAmmoProvider) stack.getItem();
                        return provider.getAmmoType().equals(type);
                    }
                    return false;
                };
                return this;
            }

            public Builder shootHandler(ShootHandler handler) {
                this.shootHandler = handler;
                return this;
            }

            public Builder sounds(SoundEvent shootSound, float volume) {
                this.shootSound = shootSound;
                this.shootSoundVolume = volume;
                return this;
            }

            public Builder shouldStayLockedOnTarget() {
                this.adjustTargetAfterShooting = false;
                return this;
            }

            public TurretType build() {
                return new TurretType(this);
            }
        }
    }

    public static final class Turrets {

        public static final TurretType SMG = TurretType.Builder.create()
                .dropAs(new ItemStack(ModItems.SMG_TURRET))
                .targetRange(32, 8)
                .targetSelector(Turrets::getClosest)
                .addTargettingModes(TargettingMode.ROCKET_TARGETS)
                .rotationSpeed(15.0F)
                .attackDelay(2)
                .requiredAmmo(AmmoType.AMMO_45ACP)
                .shootHandler(Turrets::shootSmgBullet)
                .sounds(ModSounds.GUN_VECTOR, 8.0F)
                .shouldStayLockedOnTarget()
                .build();

        public static final TurretType AR = TurretType.Builder.create()
                .dropAs(new ItemStack(ModItems.AR_TURRET))
                .targetRange(64, 16)
                .targetSelector(Turrets::getClosest)
                .addTargettingModes(TargettingMode.ROCKET_TARGETS)
                .rotationSpeed(10.0F)
                .attackDelay(4)
                .requiredAmmo(AmmoType.AMMO_556MM)
                .shootHandler(Turrets::shootArBullet)
                .sounds(ModSounds.GUN_M416, 12.0F)
                .shouldStayLockedOnTarget()
                .build();

        public static final TurretType ROCKET = TurretType.Builder.create()
                .dropAs(new ItemStack(ModItems.ROCKET_TURRET))
                .targetRange(96, 64)
                .targetValidator((turret, target) -> getDistanceSqr(target, turret) > 16)
                .addTargettingModes(TargettingMode.ROCKET_TARGETS)
                .rotationSpeed(5.0F)
                .attackDelay(80)
                .requiredAmmo(AmmoType.ROCKET)
                .shootHandler(Turrets::shootRocket)
                .sounds(ModSounds.RL_SHOT, 15.0F)
                .build();

        private static AbstractProjectile shootBullet(TurretEntity entity, ItemStack ammo, IAmmoProvider provider, float baseDamage, float dmgMultiplier, float velocity, int delay, float inaccuracy) {
            IAmmoMaterial material = provider.getMaterial();
            float actualDamage = baseDamage + material.defaultLevelRequirement() * dmgMultiplier;
            Bullet bullet = new Bullet(ModEntities.BULLET.get(), entity.level);
            bullet.setPos(entity.getX(), entity.getEyeY(), entity.getZ());
            bullet.setup(actualDamage, velocity, delay);
            bullet.fire(entity.xRot, entity.yRot, inaccuracy);
            bullet.setProperty(Properties.TRACER, material.getTracerColor() != null ? material.getTracerColor() : 0xCCCC00);
            return bullet;
        }

        private static AbstractProjectile shootSmgBullet(TurretEntity entity, ItemStack ammo, IAmmoProvider provider) {
            return shootBullet(entity, ammo, provider, 2.0F, 1.25F, 15.0F, 5, 1.0F);
        }

        private static AbstractProjectile shootArBullet(TurretEntity entity, ItemStack ammo, IAmmoProvider provider) {
            return shootBullet(entity, ammo, provider, 4.0F, 2.25F, 20.0F, 8, 0.50F);
        }

        private static AbstractProjectile shootRocket(TurretEntity entity, ItemStack ammo, IAmmoProvider provider) {
            Rocket rocket = new Rocket(ModEntities.ROCKET.get(), entity.level);
            rocket.setPos(entity.getX(), entity.getEyeY(), entity.getZ());
            rocket.setup(0.0F, 4.0F, 0);
            rocket.fire(entity.xRot, entity.yRot, 0.0F);
            rocket.setProperty(Properties.FUELED, true);

            LivingEntity target = entity.getTarget();
            IAmmoMaterial material = provider.getMaterial();
            if (target != null) {
                rocket.setProperty(Properties.ENTITY_ID, target.getId());
                rocket.setProperty(Properties.GUIDENANCE, IEntityTrackingGun.GuidenanceProperties.TURRET);
            }
            if (material instanceof IReactiveMaterial) {
                IReactiveMaterial reactiveMaterial = (IReactiveMaterial) material;
                IReaction reaction = reactiveMaterial.getReaction();
                rocket.setProperty(Properties.REACTION, reaction);
                reaction.writeInitialData(rocket, material, null);
            }
            return rocket;
        }

        private static LivingEntity getClosest(List<LivingEntity> list, TurretEntity turret) {
            return list.stream().min((entity1, entity2) -> {
                double d1 = getDistanceSqr(entity1, turret);
                double d2 = getDistanceSqr(entity2, turret);
                return Double.compare(d1, d2);
            }).orElseThrow(IllegalStateException::new);
        }

        private static double getDistanceSqr(LivingEntity entity, TurretEntity turret) {
            double x = entity.getX() - turret.getX();
            double y = entity.getY() - turret.getY();
            double z = entity.getZ() - turret.getZ();
            return x * x + y * y + z * z;
        }
    }

    public enum TargettingMode {

        NONE((turret, entity) -> false),
        ALL(TargettingMode::isNotWhitelisted),
        MONSTER((turret, entity) -> entity instanceof MonsterEntity),
        GROUND_ONLY((turret, entity) -> entity.isOnGround() && isNotWhitelisted(turret, entity)),
        AIR_ONLY((turret, entity) -> !entity.isOnGround() && entity.getY() > turret.getY() + 3.0 && isNotWhitelisted(turret, entity)),
        PLAYER((turret, entity) -> entity.getType() == EntityType.PLAYER && isNotWhitelisted(turret, entity));

        public static final Set<TargettingMode> GUN_TARGETS = EnumSet.of(ALL, MONSTER, PLAYER);
        public static final Set<TargettingMode> ROCKET_TARGETS = EnumSet.allOf(TargettingMode.class);

        private final TurretType.TargetValidator validator;

        TargettingMode(TurretType.TargetValidator validator) {
            this.validator = validator;
        }

        public boolean isDisabled() {
            return this == NONE;
        }

        public boolean isValidTarget(TurretEntity turret, LivingEntity entity) {
            TurretType.TargetValidator val = TurretType.TargetValidator.and(this.validator, turret.turretType.targetValidator);
            return val.canConsiderAsTarget(turret, entity);
        }

        public static boolean isNotWhitelisted(TurretEntity turret, LivingEntity entity) {
            if (entity.getType() == EntityType.PLAYER) {
                PlayerEntity player = (PlayerEntity) entity;
                UUID uuid = player.getUUID();
                return !turret.whitelist.contains(uuid);
            }
            return true;
        }
    }
}

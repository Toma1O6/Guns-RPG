package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.ai.RangedAttackNoSightGoal;
import dev.toma.gunsrpg.common.entity.projectile.*;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import dev.toma.gunsrpg.util.properties.Properties;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.*;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Random;

public class ExplosiveSkeletonEntity extends MonsterEntity implements IRangedAttackMob, IEntityAdditionalSpawnData, IAlwaysAggroable {

    private final RangedAttackNoSightGoal aiArrowAttack = new RangedAttackNoSightGoal(this, 1.0D, 25, 18.0F);
    private final MeleeAttackGoal aiAttackOnCollide = new MeleeAttackGoal(this, 1.2D, false) {
        @Override
        public void stop() {
            super.stop();
            ExplosiveSkeletonEntity.this.setAggressive(false);
        }

        @Override
        public void start() {
            super.start();
            ExplosiveSkeletonEntity.this.setAggressive(true);
        }
    };
    private LoadoutType loadoutType = LoadoutType.SELECTOR.getRandom();
    private boolean forcedAggro;

    public ExplosiveSkeletonEntity(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public void setForcedAggro(boolean forcedAggro) {
        this.forcedAggro = forcedAggro;
    }

    @Override
    public boolean isAggroForced() {
        return forcedAggro;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.SKELETON_STEP, 0.15F, 1.0F);
    }

    @Override
    public void die(DamageSource cause) {
        if (!this.level.isClientSide) {
            level.explode(this, getX(), getY(), getZ(), 3.0F, Explosion.Mode.NONE);
        }
        super.die(cause);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeEnum(loadoutType);
        buffer.writeBoolean(forcedAggro);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        loadoutType = additionalData.readEnum(LoadoutType.class);
        forcedAggro = additionalData.readBoolean();
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public CreatureAttribute getMobType() {
        return CreatureAttribute.UNDEAD;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, WolfEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    public void aiStep() {
        if (this.level.isDay() && !this.level.isClientSide) {
            float f = this.getBrightness();
            BlockPos blockpos = this.getVehicle() instanceof BoatEntity ? (new BlockPos(this.getX(), (double) Math.round(this.getY()), this.getZ())).above() : new BlockPos(this.getX(), (double) Math.round(this.getY()), this.getZ());
            if (f > 0.5F && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.level.canSeeSky(blockpos)) {
                boolean flag = true;
                ItemStack itemstack = this.getItemBySlot(EquipmentSlotType.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isDamageableItem()) {
                        itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                        if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                            this.broadcastBreakEvent(EquipmentSlotType.HEAD);
                            this.setItemSlot(EquipmentSlotType.HEAD, ItemStack.EMPTY);
                        }
                    }
                    flag = false;
                }
                if (flag) {
                    this.setSecondsOnFire(8);
                }
            }
        }
        super.aiStep();
    }

    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld server, DifficultyInstance difficulty, SpawnReason reason, ILivingEntityData livingData, CompoundNBT nbt) {
        super.finalizeSpawn(server, difficulty, reason, livingData, nbt);
        populateDefaultEquipmentSlots(difficulty);
        populateDefaultEquipmentEnchantments(difficulty);
        setCombatTask();
        this.setCanPickUpLoot(this.random.nextFloat() < 0.55F * difficulty.getSpecialMultiplier());
        this.handDropChances[EquipmentSlotType.MAINHAND.getIndex()] = 0.0F;
        this.handDropChances[EquipmentSlotType.OFFHAND.getIndex()] = 0.0F;
        this.armorDropChances[EquipmentSlotType.HEAD.getIndex()] = 0.0F;
        return livingData;
    }

    public void setCombatTask() {
        if (this.level != null && !this.level.isClientSide) {
            this.goalSelector.removeGoal(this.aiAttackOnCollide);
            this.goalSelector.removeGoal(this.aiArrowAttack);
            ItemStack itemstack = this.getMainHandItem();
            if (itemstack.getItem() == ModItems.GRENADE_LAUNCHER) {
                int i = 60;
                if (this.level.getDifficulty() != Difficulty.HARD) {
                    i = 120;
                }
                this.aiArrowAttack.setAttackCooldown(i);
                this.goalSelector.addGoal(4, this.aiArrowAttack);
            } else {
                this.goalSelector.addGoal(4, this.aiAttackOnCollide);
            }
        }
    }

    @Override
    public void rideTick() {
        super.rideTick();
        if (this.getVehicle() instanceof CreatureEntity) {
            CreatureEntity entitycreature = (CreatureEntity) this.getVehicle();
            this.yBodyRot = entitycreature.yBodyRot;
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(difficulty);
        this.setItemSlot(EquipmentSlotType.MAINHAND, new ItemStack(ModItems.GRENADE_LAUNCHER));
        ItemStack head = new ItemStack(Items.LEATHER_HELMET);
        int color = loadoutType.getColor();
        CompoundNBT data = new CompoundNBT();
        CompoundNBT display = new CompoundNBT();
        display.putInt("color", color);
        data.put("display", display);
        head.setTag(data);
        setItemSlot(EquipmentSlotType.HEAD, head);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        Grenade grenade = new Grenade(ModEntities.GRENADE_SHELL.get(), level, this);
        IReaction reaction = this.loadoutType.getReaction();
        reaction.writeInitialData(grenade, AmmoMaterials.GRENADE, this);
        double x = target.getX() - this.getX();
        double z = target.getZ() - this.getZ();
        float dist = MathHelper.sqrt(x * x + z * z);
        grenade.setup(1.0f, 1.5f, 0);
        grenade.fire(xRot - dist * 0.1f, yRot, 2.5F);
        grenade.setProperty(Properties.REACTION, reaction);
        this.playSound(ModSounds.GL_SHOT1, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(grenade);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("loadoutType", this.loadoutType.ordinal());
        nbt.putBoolean("forcedAggro", this.forcedAggro);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        this.loadoutType = LoadoutType.values()[nbt.getInt("loadoutType")];
        this.forcedAggro = nbt.getBoolean("forcedAggro");
        this.setCombatTask();
    }

    @Override
    public void setItemSlot(EquipmentSlotType slotIn, ItemStack stack) {
        super.setItemSlot(slotIn, stack);
        if (!this.level.isClientSide && slotIn == EquipmentSlotType.MAINHAND) {
            this.setCombatTask();
        }
    }

    @Override
    public float getEyeHeight(Pose p_213307_1_) {
        return 1.74F;
    }

    @Override
    public double getMyRidingOffset() {
        return -0.6D;
    }

    private enum LoadoutType {

        TEAR_GAS(12, 0x51CC72, new EffectSpreadReaction(0x51CC72, () -> new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 100, 1), () -> new EffectInstance(Effects.BLINDNESS, 100, 0))),
        STANDARD(35, 0xBDCC92, MultipartReaction.multi(BreakBlockReaction.INSTANCE, new ExplosiveReaction(2.0f, Explosion.Mode.DESTROY))),
        IMPACT(8, 0xCC4C1E, MultipartReaction.multi(BreakBlockReaction.INSTANCE, new ExplosiveReaction(2.0f, Explosion.Mode.DESTROY), new PropertyTriggerReaction<>(Properties.IMPACT, true))),
        SHOCK(10, 0x3DC2CC, MultipartReaction.multi(BreakBlockReaction.INSTANCE, new ShockReaction(7, 3), new ExplosiveReaction(2.0f, Explosion.Mode.DESTROY)));

        private static final WeightedRandom<LoadoutType> SELECTOR = new WeightedRandom<>(LoadoutType::getWeight, LoadoutType.values());
        private final int weight;
        private final int color;
        private final IReaction reaction;

        LoadoutType(int weight, int color, IReaction reaction) {
            this.weight = weight;
            this.color = color;
            this.reaction = reaction;
        }

        public int getWeight() {
            return weight;
        }

        public int getColor() {
            return color;
        }

        public IReaction getReaction() {
            return reaction;
        }
    }

    private static class BreakBlockReaction implements IReaction {

        public static final BreakBlockReaction INSTANCE = new BreakBlockReaction();

        @Override
        public void react(AbstractProjectile projectile, Vector3d impact, World world) {
            BlockPos pos = new BlockPos(impact.x, impact.y, impact.z);
            destroyBlock(world, pos);
            for (Direction direction : Direction.values()) {
                destroyBlock(world, pos.relative(direction));
            }
        }

        private void destroyBlock(World world, BlockPos pos) {
            BlockState state = world.getBlockState(pos);
            float speed = state.getDestroySpeed(world, pos);
            if (speed < 0) {
                return;
            }
            world.destroyBlock(pos, false);
        }
    }

    private static class ShockReaction implements IReaction {

        private final int range;
        private final int count;

        public ShockReaction(int range, int count) {
            this.range = 1 + range;
            this.count = count;
        }

        @Override
        public void react(AbstractProjectile projectile, Vector3d impact, World world) {
            if (world.isClientSide) return;
            Random random = world.random;
            for (int i = 0; i < count; i++) {
                int x = (int) (impact.x + random.nextInt(range) - random.nextInt(range));
                int z = (int) (impact.z + random.nextInt(range) - random.nextInt(range));
                int y = world.getHeight(Heightmap.Type.WORLD_SURFACE, x, z);
                LightningBoltEntity lightning = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, world);
                lightning.setPos(x + 0.5, y, z + 0.5);
                lightning.setDamage(4.0f);
                world.addFreshEntity(lightning);
            }
        }
    }
}

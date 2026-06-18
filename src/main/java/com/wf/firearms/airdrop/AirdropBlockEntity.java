package com.wf.firearms.airdrop;

import com.wf.firearms.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AirdropBlockEntity extends BaseContainerBlockEntity {
    public static final int SLOT_COUNT = 27;
    /** 落地后冒烟时长（约 5 分钟）。 */
    private static final long SMOKE_DURATION_TICKS = 6000L;

    private final Map<UUID, NonNullList<ItemStack>> playerLoot = new HashMap<>();
    private long smokeUntilGameTime = -1L;

    public AirdropBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.AIRDROP.get(), pos, state);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.gunsrpg.airdrop");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        if (inv.player instanceof ServerPlayer server) {
            return new AirdropMenu(id, inv, this, server);
        }
        return new AirdropMenu(id, inv, this);
    }

    /** 每名玩家独立战利品；首次打开时生成并持久化。 */
    public NonNullList<ItemStack> getOrCreatePlayerLoot(ServerPlayer player) {
        return playerLoot.computeIfAbsent(
                player.getUUID(),
                uuid -> {
                    NonNullList<ItemStack> slots =
                            NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
                    if (level != null) {
                        long day = level.getDayTime() / 24000L;
                        AirdropLoot.fillList(slots, player.getRandom(), day);
                    }
                    setChanged();
                    return slots;
                });
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        return playerLoot.values().stream().allMatch(this::listEmpty);
    }

    private boolean listEmpty(NonNullList<ItemStack> list) {
        for (ItemStack stack : list) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {}

    @Override
    public boolean stillValid(Player player) {
        if (level == null || level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(
                        worldPosition.getX() + 0.5,
                        worldPosition.getY() + 0.5,
                        worldPosition.getZ() + 0.5)
                <= 64.0;
    }

    @Override
    public void clearContent() {
        playerLoot.clear();
    }

    public void beginSmokePhase() {
        if (level == null || level.isClientSide) {
            return;
        }
        smokeUntilGameTime = level.getGameTime() + SMOKE_DURATION_TICKS;
        setChanged();
        if (level instanceof ServerLevel server) {
            server.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            ClientboundBlockEntityDataPacket packet = ClientboundBlockEntityDataPacket.create(this);
            for (ServerPlayer player : server.players()) {
                if (player.distanceToSqr(
                                worldPosition.getX() + 0.5,
                                worldPosition.getY() + 0.5,
                                worldPosition.getZ() + 0.5)
                        < 64 * 64) {
                    player.connection.send(packet);
                }
            }
        }
    }

    public static void clientTickStatic(
            Level level, BlockPos pos, BlockState state, AirdropBlockEntity blockEntity) {
        blockEntity.clientTick();
    }

    public void clientTick() {
        if (level == null || !level.isClientSide) {
            return;
        }
        long gameTime = level.getGameTime();
        if (smokeUntilGameTime < 0) {
            smokeUntilGameTime = gameTime + SMOKE_DURATION_TICKS;
        }
        if (gameTime > smokeUntilGameTime) {
            return;
        }
        Vec3 center = Vec3.atCenterOf(worldPosition);
        float spreadX = (level.random.nextFloat() - level.random.nextFloat()) / 32.0F;
        float spreadZ = (level.random.nextFloat() - level.random.nextFloat()) / 32.0F;
        level.addParticle(
                ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                center.x,
                center.y + 0.6,
                center.z,
                spreadX,
                0.2,
                spreadZ);
        level.addParticle(
                ParticleTypes.SMOKE,
                center.x,
                center.y + 0.4,
                center.z,
                spreadX * 0.5F,
                0.05,
                spreadZ * 0.5F);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ListTag list = new ListTag();
        for (Map.Entry<UUID, NonNullList<ItemStack>> entry : playerLoot.entrySet()) {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID("Player", entry.getKey());
            ContainerHelper.saveAllItems(playerTag, entry.getValue());
            list.add(playerTag);
        }
        tag.put("PlayerLoot", list);
        tag.putLong("SmokeUntil", smokeUntilGameTime);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        playerLoot.clear();
        if (tag.contains("PlayerLoot", Tag.TAG_LIST)) {
            ListTag list = tag.getList("PlayerLoot", Tag.TAG_COMPOUND);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag playerTag = list.getCompound(i);
                UUID uuid = playerTag.getUUID("Player");
                NonNullList<ItemStack> slots =
                        NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
                ContainerHelper.loadAllItems(playerTag, slots);
                playerLoot.put(uuid, slots);
            }
        } else if (tag.contains("Items", Tag.TAG_LIST)) {
            // 旧存档：共享物品转给首个打开者时再生成；此处丢弃避免多人抢同一套
            setChanged();
        }
        smokeUntilGameTime = tag.contains("SmokeUntil") ? tag.getLong("SmokeUntil") : -1L;
    }
}

package com.wf.firearms.airdrop;

import com.wf.firearms.registry.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AirdropMenu extends ChestMenu {
    private final AirdropBlockEntity blockEntity;

    public AirdropMenu(int id, Inventory inv, AirdropBlockEntity blockEntity, ServerPlayer opener) {
        super(
                ModMenuTypes.AIRDROP.get(),
                id,
                inv,
                new PlayerAirdropContainer(blockEntity, blockEntity.getOrCreatePlayerLoot(opener)),
                3);
        this.blockEntity = blockEntity;
    }

    /** 客户端占位容器；物品由服务端同步。 */
    public AirdropMenu(int id, Inventory inv, AirdropBlockEntity blockEntity) {
        super(ModMenuTypes.AIRDROP.get(), id, inv, new SimpleContainer(AirdropBlockEntity.SLOT_COUNT), 3);
        this.blockEntity = blockEntity;
    }

    public static AirdropMenu fromNetwork(int windowId, Inventory inv, FriendlyByteBuf data) {
        BlockPos pos = data.readBlockPos();
        BlockEntity be = inv.player.level().getBlockEntity(pos);
        if (be instanceof AirdropBlockEntity airdrop) {
            if (inv.player instanceof ServerPlayer server) {
                return new AirdropMenu(windowId, inv, airdrop, server);
            }
            return new AirdropMenu(windowId, inv, airdrop);
        }
        return null;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity.stillValid(player);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if (!blockEntity.isRemoved() && player instanceof ServerPlayer) {
            blockEntity.setChanged();
        }
    }
}

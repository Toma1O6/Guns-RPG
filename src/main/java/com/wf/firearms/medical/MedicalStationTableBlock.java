package com.wf.firearms.medical;

import com.wf.firearms.data.PlayerFirearmsData;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class MedicalStationTableBlock extends BaseEntityBlock {
    public MedicalStationTableBlock(Properties props) {
        super(props);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MedicalStationTableBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public InteractionResult use(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hit) {
        if (!level.isClientSide) {
            if (!PlayerFirearmsData.isUnlocked(player, "medical_station")) {
                player.displayClientMessage(
                        Component.translatable("message.gunsrpg.need_skill.medical_station")
                                .withStyle(ChatFormatting.RED),
                        true);
                return InteractionResult.FAIL;
            }
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MedicalStationTableBlockEntity table) {
                NetworkHooks.openScreen((ServerPlayer) player, table, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}

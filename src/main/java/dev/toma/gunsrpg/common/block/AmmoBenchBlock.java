package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.container.AmmoBenchContainer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.tileentity.AmmoBenchTileEntity;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class AmmoBenchBlock extends BaseHorizontalBlock {

    private static final ITextComponent TITLE = new TranslationTextComponent("screen.gunsrpg.ammo_bench");

    public AmmoBenchBlock(String name) {
        super(name, Properties.of(Material.METAL).strength(2.2F, 18.0F).harvestTool(ToolType.PICKAXE).harvestLevel(1).requiresCorrectToolForDrops().noOcclusion());
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult traceResult) {
        if (!world.isClientSide) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof AmmoBenchTileEntity) {
                AmmoBenchTileEntity ammoBenchTile = (AmmoBenchTileEntity) tileEntity;
                if (!PlayerData.hasActiveSkill(player, Skills.GUN_PARTS_SMITH)) {
                    player.displayClientMessage(SkillUtil.getMissingSkillText(Skills.GUN_PARTS_SMITH), true);
                    return ActionResultType.FAIL;
                } else {
                    NetworkHooks.openGui((ServerPlayerEntity) player, new SimpleNamedContainerProvider((windowId, playerInventory, interactingPlayer) -> new AmmoBenchContainer(windowId, playerInventory, ammoBenchTile), TITLE), pos);
                    return ActionResultType.SUCCESS;
                }
            }
        }
        return ActionResultType.CONSUME;
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState oldState, boolean flag) {
        if (!state.is(oldState.getBlock())) {
            TileEntity tile = world.getBlockEntity(pos);
            if (tile instanceof AmmoBenchTileEntity) {
                AmmoBenchTileEntity tileEntity = (AmmoBenchTileEntity) tile;
                InventoryHelper.dropContents(world, pos, tileEntity);
                world.updateNeighbourForOutputSignal(pos, this);
            }
        }
        super.onRemove(state, world, pos, oldState, flag);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AmmoBenchTileEntity();
    }
}

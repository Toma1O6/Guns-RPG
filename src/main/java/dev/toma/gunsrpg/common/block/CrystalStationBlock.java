package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.container.CrystalStationContainer;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class CrystalStationBlock extends BaseBlock {

    private static final ITextComponent TITLE = new TranslationTextComponent("screen.crystal_station");

    public CrystalStationBlock(String name) {
        super(name, Properties.of(Material.STONE).strength(3.6F).harvestTool(ToolType.PICKAXE).harvestLevel(0).requiresCorrectToolForDrops().noOcclusion());
    }

    @Nullable
    @Override
    public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedContainerProvider((windowId, inventory, player) -> new CrystalStationContainer(windowId, inventory), TITLE);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (!world.isClientSide) {
            NetworkHooks.openGui((ServerPlayerEntity) player, this.getMenuProvider(state, world, pos));
            return ActionResultType.CONSUME;
        } else return ActionResultType.SUCCESS;
    }
}

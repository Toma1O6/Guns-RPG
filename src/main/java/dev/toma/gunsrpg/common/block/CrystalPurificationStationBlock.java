package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.container.CrystalPurificationStationContainer;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.tileentity.CrystalPurificationStationTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class CrystalPurificationStationBlock extends BaseBlock {

    private static final ITextComponent TITLE = new TranslationTextComponent("screen.crystal_purification_station");

    public CrystalPurificationStationBlock(String name) {
        super(name, Properties.of(Material.STONE).strength(3.6F).harvestTool(ToolType.PICKAXE).harvestLevel(0).requiresCorrectToolForDrops().noOcclusion());
    }

    @Nullable
    @Override
    public INamedContainerProvider getMenuProvider(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedContainerProvider((windowId, inventory, player) -> new CrystalPurificationStationContainer(windowId, inventory, (CrystalPurificationStationTileEntity) world.getBlockEntity(pos)), TITLE);
    }

    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult result) {
        if (!world.isClientSide) {
            ISkillProvider provider = PlayerData.getUnsafe(player).getSkillProvider();
            if (!provider.hasSkill(Skills.CRYSTAL_PURIFICATION_STATION)) {
                ((ServerPlayerEntity) player).sendMessage(new StringTextComponent("You must have 'crystal purification station' skill in order to interact with this block"), ChatType.GAME_INFO, Util.NIL_UUID);
                return ActionResultType.CONSUME;
            }
            NetworkHooks.openGui((ServerPlayerEntity) player, this.getMenuProvider(state, world, pos), pos);
            return ActionResultType.CONSUME;
        } else return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CrystalPurificationStationTileEntity();
    }
}

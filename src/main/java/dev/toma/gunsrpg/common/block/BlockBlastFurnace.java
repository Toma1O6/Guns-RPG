package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.tileentity.TileEntityBlastFurnace;
import dev.toma.gunsrpg.util.GuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockBlastFurnace extends GRPGBlock {

    private static PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private static PropertyBool BURN = PropertyBool.create("burning");

    public BlockBlastFurnace(String name) {
        super(name, Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(BURN, false));
    }

    public static void updateBurnState(BlockPos pos, World world, boolean lit) {
        IBlockState currentState = world.getBlockState(pos);
        if(currentState.getBlock() != ModRegistry.GRPGBlocks.BLAST_FURNACE) return;
        world.setBlockState(pos, currentState.withProperty(BURN, lit));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if(stateIn.getValue(BURN)) {
            EnumFacing facing = stateIn.getValue(FACING);
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5, pos.getY() + 1.1, pos.getZ() + 0.5, 0.0, 0.0, 0.0);
            double rng = (rand.nextDouble() - rand.nextDouble()) / 3;
            if(rand.nextDouble() <= 0.2) {
                worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }
            switch (facing) {
                case NORTH: {
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5 + rng, pos.getY() + 0.25, pos.getZ() - 0.05, 0, 0, 0);
                    break;
                }
                case EAST: {
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 1.05, pos.getY() + 0.25, pos.getZ() + 0.5 + rng, 0, 0, 0);
                    break;
                }
                case SOUTH: {
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5 + rng, pos.getY() + 0.25, pos.getZ() + 1.05, 0, 0, 0);
                    break;
                }
                case WEST: {
                    worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() - 0.05, pos.getY() + 0.25, pos.getZ() + 0.5 + rng, 0, 0, 0);
                    break;
                }
            }
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(BURN) ? 12 : 0;
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityBlastFurnace) worldIn.getTileEntity(pos));
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityBlastFurnace();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            playerIn.openGui(GunsRPG.modInstance, GuiHandler.BLAST_FURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        meta |= state.getValue(FACING).getHorizontalIndex();
        meta |= state.getValue(BURN) ? 4 : 0;
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(BURN, (meta & 4) > 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, BURN);
    }
}

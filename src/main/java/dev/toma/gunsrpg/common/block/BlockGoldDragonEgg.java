package dev.toma.gunsrpg.common.block;

import dev.toma.gunsrpg.common.entity.EntityGoldDragon;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockGoldDragonEgg extends GRPGBlock {

    private static final PropertyBool IGNITED = PropertyBool.create("ignited");

    public BlockGoldDragonEgg(String name) {
        super(name, Material.DRAGON_EGG);
        setDefaultState(blockState.getBaseState().withProperty(IGNITED, false));
        setTickRandomly(true);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(playerIn.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL) {
            if(!state.getValue(IGNITED)) {
                worldIn.setBlockState(pos, state.withProperty(IGNITED, true), 3);
                playerIn.getHeldItem(hand).damageItem(1, playerIn);
                return true;
            }
        }
        return false;
    }

    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if(stateIn.getValue(IGNITED)) {
            worldIn.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + rand.nextDouble(), pos.getY() + 0.5, pos.getZ() + rand.nextDouble(), 0.0, 0.05, 0.0);
        }
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        if (state.getValue(IGNITED)) {
            if(!worldIn.isRemote) {
                boolean canSpawn = false;
                if(worldIn.canSeeSky(pos)) {
                    EntityGoldDragon goldDragon = new EntityGoldDragon(worldIn);
                    goldDragon.setPosition(pos.getX(), pos.getY() + 30, pos.getZ());
                    goldDragon.phaseManager.setPhase(PhaseList.HOLDING_PATTERN);
                    worldIn.spawnEntity(goldDragon);
                    canSpawn = true;
                }
                worldIn.destroyBlock(pos, !canSpawn);
            }
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(IGNITED) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(IGNITED, meta > 0);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, IGNITED);
    }

    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}

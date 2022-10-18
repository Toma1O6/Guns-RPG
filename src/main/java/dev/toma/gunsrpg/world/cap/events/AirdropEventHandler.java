package dev.toma.gunsrpg.world.cap.events;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IWorldEventHandler;
import dev.toma.gunsrpg.common.entity.AirdropEntity;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

import java.util.List;
import java.util.Random;

public class AirdropEventHandler implements IWorldEventHandler {

    final boolean isValidDimension;
    final Random random;

    public AirdropEventHandler(World world) {
        isValidDimension = world != null && world.dimensionType() == DimensionType.DEFAULT_OVERWORLD;
        random = new Random();
    }

    @Override
    public void eventStarted(World world) {
        if (!isValidDimension) return;
        List<? extends PlayerEntity> playerList = world.players();
        addAirdrop(world, playerList);
    }

    @Override
    public void eventFinished(World world) {
    }

    @Override
    public boolean canTriggerEvent(World world) {
        return true;
    }

    private void addAirdrop(World world, List<? extends PlayerEntity> playerList) {
        PlayerEntity player = ModUtils.getRandomListElement(playerList, random);
        GunsRPG.log.debug(WorldEventSpec.MARKER, "Preparing airdrop spawn event");
        if (player == null) return;
        int maxDist = 35;
        int x = (int) (player.getX() + randomInRange(maxDist));
        int z = (int) (player.getZ() + randomInRange(maxDist));
        int y = world.getHeight(Heightmap.Type.WORLD_SURFACE, x, z) + 100;
        BlockPos pos = new BlockPos(player.getX() + x, y, player.getZ() + z);
        if (!world.isEmptyBlock(pos)) {
            GunsRPG.log.error("Airdrop spawning failed due to position being obstructed at {}", pos);
            return;
        }
        AirdropEntity entity = new AirdropEntity(world);
        entity.setPos(pos.getX(), pos.getY(), pos.getZ());
        world.playSound(null, pos.getX(), player.getY(), pos.getZ(), ModSounds.PLANE_FLY_BY, SoundCategory.MASTER, 15.0F, 1.0F);
        world.addFreshEntity(entity);
        float anotherAirdropChance = 0.005F;
        if (random.nextFloat() < anotherAirdropChance) {
            addAirdrop(world, playerList);
        }
        GunsRPG.log.debug(WorldEventSpec.MARKER, "Created new airdrop at {} for player {}", pos, player.getDisplayName().getString());
    }

    private int randomInRange(int range) {
        return random.nextInt(range) - random.nextInt(range);
    }
}

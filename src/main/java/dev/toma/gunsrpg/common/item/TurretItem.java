package dev.toma.gunsrpg.common.item;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.client.render.item.TurretItemRenderer;
import dev.toma.gunsrpg.common.entity.TurretEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class TurretItem extends BaseItem {

    private final Supplier<EntityType<TurretEntity>> turretType;

    public TurretItem(String name, Supplier<EntityType<TurretEntity>> turretType) {
        super(name, new Properties().stacksTo(1).tab(ModTabs.ITEM_TAB).setISTER(() -> TurretItemRenderer::new));
        this.turretType = turretType;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        PlayerEntity player = context.getPlayer();
        if (!world.isClientSide) {
            TurretEntity entity = this.turretType.get().create(world);
            entity.setPos(pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5);
            entity.setOwner(player.getUUID());
            world.addFreshEntity(entity);
        }
        ItemStack stack = context.getItemInHand();
        if (!player.abilities.instabuild) {
            stack.shrink(1);
        }
        return ActionResultType.SUCCESS;
    }

    public EntityType<TurretEntity> getEntityType() {
        return turretType.get();
    }
}

package dev.toma.gunsrpg.mixin;

import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;

@Mixin(AbstractBlock.class)
public abstract class AbstractBlockMixin extends net.minecraftforge.registries.ForgeRegistryEntry<Block> {

    @Shadow public abstract ResourceLocation getLootTable();

    @Inject(method = "getDrops", at = @At("HEAD"), cancellable = true)
    public void gunsrpg_modifyBlockDrops(BlockState state, LootContext.Builder ctxBuilder, CallbackInfoReturnable<List<ItemStack>> ci) {
        ResourceLocation location = this.getLootTable();
        if (location == LootTables.EMPTY) {
            ci.setReturnValue(Collections.emptyList());
        } else {
            LootContext lootContext = ctxBuilder.withParameter(LootParameters.BLOCK_STATE, state).create(LootParameterSets.BLOCK);
            ServerWorld serverWorld = lootContext.getLevel();
            LootTable lootTable = serverWorld.getServer().getLootTables().get(location);
            List<ItemStack> adjustedDrops = SkillUtil.applyOreDropChanges(lootContext, serverWorld, state, lootTable);
            ci.setReturnValue(adjustedDrops);
        }
    }
}

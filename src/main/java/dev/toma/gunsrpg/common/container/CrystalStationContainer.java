package dev.toma.gunsrpg.common.container;

import com.mojang.datafixers.util.Pair;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPerkProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModContainers;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.item.perk.Crystal;
import dev.toma.gunsrpg.common.item.perk.CrystalItem;
import dev.toma.gunsrpg.common.item.perk.PerkItem;
import dev.toma.gunsrpg.common.item.perk.PerkVariant;
import dev.toma.gunsrpg.config.ModConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CrystalStationContainer extends AbstractContainer {

    public static final ResourceLocation EMPTY_CRYSTAL_SLOT = GunsRPG.makeResource("item/empty_crystal");
    public static final ResourceLocation EMPTY_TRANSMUTATION_SLOT = GunsRPG.makeResource("item/empty_orb_of_transmutation");
    public static final ResourceLocation EMPTY_PURITY_SLOT = GunsRPG.makeResource("item/empty_orb_of_purity");

    private final Inventory temporalInventory;

    public CrystalStationContainer(int windowId, PlayerInventory inventory) {
        super(ModContainers.CRYSTAL_STATION.get(), windowId);
        IPlayerData data = PlayerData.getUnsafe(inventory.player);
        ISkillProvider provider = data.getSkillProvider();
        int rows = provider.hasSkill(Skills.CRYSTALIZED) ? 2 : 1;
        temporalInventory = new Inventory(rows * 6);
        loadInventory(data);
        IntReferenceHolder referenceHolder = IntReferenceHolder.standalone();
        addDataSlot(referenceHolder);
        BooleanSupplier supplier = () -> data.getPerkProvider().getPoints() > 0;
        for (int y = 0; y < rows; y++) {
            for (PerkVariant variant : PerkVariant.values()) {
                int x = variant.ordinal();
                addSlot(new PerkSlot(temporalInventory, x + y * 6, 20 + x * 24, 16 + y * 24, variant, supplier));
            }
        }
        addPlayerInventory(inventory, 74);
        referenceHolder.set(1);
        addSlotListener(new ContainerListener(data, temporalInventory.getContainerSize()));
        referenceHolder.set(0);
    }

    public CrystalStationContainer(int windowId, PlayerInventory inventory, PacketBuffer buffer) {
        this(windowId, inventory);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    private void loadInventory(IPlayerData data) {
        Map<PerkVariant, CrystalItem> map = mapCrystalVariants();
        IPerkProvider provider = data.getPerkProvider();
        for (int i = 0; i < temporalInventory.getContainerSize(); i++) {
            Crystal crystal = provider.getCrystal(i);
            if (crystal == null) continue;
            PerkVariant variant = PerkVariant.byId(i);
            CrystalItem item = map.get(variant);
            temporalInventory.setItem(i, crystal.asItem(item));
        }
    }

    public static Map<PerkVariant, CrystalItem> mapCrystalVariants() {
        return ForgeRegistries.ITEMS.getValues().stream()
                .filter(it -> it instanceof CrystalItem)
                .map(it -> (CrystalItem) it)
                .collect(Collectors.toMap(PerkItem::getVariant, Function.identity()));
    }

    public class PerkSlot extends Slot {

        private final PerkVariant variant;
        private final BooleanSupplier sufficientFundsSupplier;

        public PerkSlot(IInventory inventory, int index, int x, int y, PerkVariant variant, BooleanSupplier sufficientFundsSupplier) {
            super(inventory, index, x, y);
            this.variant = variant;
            this.sufficientFundsSupplier = sufficientFundsSupplier;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return sufficientFundsSupplier.getAsBoolean() && stack.getItem() instanceof CrystalItem && ((CrystalItem) stack.getItem()).getVariant() == variant;
        }

        @Override
        public boolean mayPickup(PlayerEntity entity) {
            return true;
        }

        @Override
        public void setChanged() {
            CrystalStationContainer.this.broadcastChanges();
            super.setChanged();
        }

        @OnlyIn(Dist.CLIENT)
        @Nullable
        @Override
        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
            return Pair.of(PlayerContainer.BLOCK_ATLAS, EMPTY_CRYSTAL_SLOT);
        }
    }

    private static class ContainerListener implements IContainerListener {

        private final IPlayerData data;
        private boolean acceptingEvents;
        private final int slotCount;

        public ContainerListener(IPlayerData data, int slotCount) {
            this.data = data;
            this.slotCount = slotCount;
        }

        @Override
        public void refreshContainer(Container container, NonNullList<ItemStack> items) {
        }

        @Override
        public void slotChanged(Container container, int slot, ItemStack itemStack) {
            if (!acceptingEvents) return;
            if (slot < slotCount) {
                IPerkProvider provider = data.getPerkProvider();
                if (itemStack.isEmpty()) {
                    provider.setCrystal(slot, null);
                } else {
                    CompoundNBT nbt = itemStack.getTag();
                    Crystal crystal;
                    if (nbt == null) {
                        crystal = new Crystal(0, Collections.emptyList());
                    } else {
                        CompoundNBT crystalNbt = nbt.getCompound("crystal");
                        crystal = Crystal.fromNbt(crystalNbt);
                    }
                    provider.setCrystal(slot, crystal);
                    provider.setCooldown(ModConfig.worldConfig.crystalStationUseCooldown.get());
                    provider.awardPoints(-1);
                }
            }
        }

        @Override
        public void setContainerData(Container container, int id, int data) {
            if (id == 0) {
                acceptingEvents = data == 0;
            }
        }
    }
}

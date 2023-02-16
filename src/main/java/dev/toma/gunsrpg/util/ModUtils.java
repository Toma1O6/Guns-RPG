package dev.toma.gunsrpg.util;

import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.tileentity.InventoryTileEntity;
import dev.toma.gunsrpg.util.function.ISplitter;
import dev.toma.gunsrpg.util.locate.ammo.ItemLocator;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

import java.lang.reflect.Array;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModUtils {

    public static final ISplitter<SkillCategory, SkillType<?>> SKILLS_BY_CATEGORY = ModUtils::splitSkillsIntoCategories;
    public static final DecimalFormatSymbols DOT_DECIMAL_SEPARATOR = new DecimalFormatSymbols();

    public static void addItem(PlayerEntity player, ItemStack stack) {
        if (player.level.isClientSide) return;
        boolean flag = player.inventory.add(stack);
        if (flag && stack.isEmpty()) {
            stack.setCount(1);
            ItemEntity itementity1 = player.drop(stack, false);
            if (itementity1 != null) {
                itementity1.makeFakeItem();
            }
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.inventoryMenu.broadcastChanges();
        } else {
            ItemEntity itementity = player.drop(stack, false);
            if (itementity != null) {
                itementity.setNoPickUpDelay();
                itementity.setOwner(player.getUUID());
            }
        }
    }

    public static <T> int indexOf(T[] array, T value) {
        for (int i = 0; i < array.length; i++) {
            T t = array[i];
            if (t.equals(value)) {
                return i;
            }
        }
        return -1;
    }

    public static <T> void shift(T element, T[] array) {
        int length = array.length;
        if (length > 1) {
            System.arraycopy(array, 0, array, 1, length - 2 + 1);
        }
        array[0] = element;
    }

    public static void inverse(Object[] array) {
        int length = array.length;
        int half = length / 2;
        for (int i = 0; i < half; i++) {
            int opposite = length - 1 - i;
            Object temp = array[i];
            array[i] = array[opposite];
            array[opposite] = temp;
        }
    }

    public static boolean isWithinPoints(int x, int y, int ax, int ay, int bx, int by) {
        return x >= ax && x <= bx && y >= ay && y <= by;
    }

    public static boolean equals(double value1, double value2, double precision) {
        double diff = Math.abs(value1 - value2);
        return diff <= precision;
    }

    @SafeVarargs
    public static <T> T firstNonnull(T... values) {
        for (T t : values) {
            if (t != null)
                return t;
        }
        return null;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static boolean isNullOrEmpty(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    public static String convertToLocalization(ResourceLocation location) {
        return location.toString().replaceAll(":", ".");
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] trimArray(T[] in, Class<T> cls) {
        return Arrays.stream(in).filter(Objects::nonNull).toArray(size -> (T[]) Array.newInstance(cls, size));
    }

    public static <T> T init(T t, Consumer<T> initializer) {
        initializer.accept(t);
        return t;
    }

    public static <NBT extends INBT> void loadDeserializable(String name, INBTSerializable<NBT> serializable, INBTDeserializer<NBT> deserializer, Supplier<NBT> fallback, CompoundNBT nbt) {
        NBT inbt = deserializer.deserialize(nbt, name);
        serializable.deserializeNBT(inbt != null ? inbt : fallback.get());
    }

    public static void loadNBT(String name, INBTSerializable<CompoundNBT> serializable, CompoundNBT source) {
        loadDeserializable(name, serializable, CompoundNBT::getCompound, CompoundNBT::new, source);
    }

    public static <T> T getRandomListElement(List<T> list, Random random) {
        if (list.isEmpty())
            return null;
        return list.get(random.nextInt(list.size()));
    }

    public static void dropInventoryItems(World world, BlockPos pos) {
        TileEntity entity = world.getBlockEntity(pos);
        if (entity instanceof InventoryTileEntity) {
            dropInventoryItems(((InventoryTileEntity) entity).getInventory(), world, pos);
        }
    }

    public static void dropInventoryItems(LazyOptional<? extends IItemHandler> optional, World world, BlockPos pos) {
        optional.ifPresent(handler -> {
            for (int i = 0; i < handler.getSlots(); i++) {
                InventoryHelper.dropItemStack(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, handler.getStackInSlot(i));
            }
        });
    }

    public static <K, V> void noDupInsert(Map<K, V> map, V value, Function<V, K> keyExtractor) {
        noDupInsert(map, keyExtractor.apply(value), value);
    }

    public static <K, V> void noDupInsert(Map<K, V> map, K key, V value) {
        if (map.put(key, value) != null)
            throw new IllegalStateException("Duplicate key: " + key);
    }

    public static <A> boolean contains(A obj, A[] array) {
        for (A a : array) {
            if (a == obj) {
                return true;
            }
        }
        return false;
    }

    public static int clamp(int n, int min, int max) {
        return n < min ? min : Math.min(n, max);
    }

    public static <K, V> V getNonnullFromMap(Map<K, V> map, K key, V def) {
        V v = map.get(key);
        return v != null ? v : Objects.requireNonNull(def);
    }

    public static BlockRayTraceResult raytraceBlocksIgnoreGlass(Vector3d start, Vector3d end, IBlockReader reader) {
        RayTraceContext context = new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, null);
        return IBlockReader.traverseBlocks(context, (ctx, pos) -> {
            BlockState blockstate = reader.getBlockState(pos);
            FluidState fluidstate = reader.getFluidState(pos);
            Vector3d vector3d = ctx.getFrom();
            Vector3d vector3d1 = ctx.getTo();
            VoxelShape voxelshape = ctx.getBlockShape(blockstate, reader, pos);
            BlockRayTraceResult blockraytraceresult = reader.clipWithInteractionOverride(vector3d, vector3d1, pos, voxelshape, blockstate);
            VoxelShape voxelshape1 = ctx.getFluidShape(fluidstate, reader, pos);
            BlockRayTraceResult blockraytraceresult1 = voxelshape1.clip(vector3d, vector3d1, pos);
            double d0 = blockraytraceresult == null ? Double.MAX_VALUE : ctx.getFrom().distanceToSqr(blockraytraceresult.getLocation());
            double d1 = blockraytraceresult1 == null ? Double.MAX_VALUE : ctx.getFrom().distanceToSqr(blockraytraceresult1.getLocation());
            return d0 <= d1 ? blockraytraceresult : blockraytraceresult1;
        }, (ctx) -> {
            Vector3d vector3d = ctx.getFrom().subtract(ctx.getTo());
            return BlockRayTraceResult.miss(ctx.getTo(), Direction.getNearest(vector3d.x, vector3d.y, vector3d.z), new BlockPos(ctx.getTo()));
        });
    }

    public static Direction getFacing(PlayerEntity player) {
        float reach = getReachDistance(player);
        Vector3d vec1 = player.getEyePosition(1.0F);
        Vector3d vec2 = player.getLookAngle();
        Vector3d vec3 = vec1.add(vec2.x * reach, vec2.y * reach, vec2.z * reach);
        BlockRayTraceResult result = player.level.clip(new RayTraceContext(vec1, vec3, RayTraceContext.BlockMode.VISUAL, RayTraceContext.FluidMode.NONE, player));
        return result != null && result.getDirection() != null ? result.getDirection() : Direction.NORTH;
    }

    public static float getReachDistance(PlayerEntity player) {
        float attrib = (float) player.getAttributeValue(ForgeMod.REACH_DISTANCE.get());
        return player.isCreative() ? attrib : attrib - 0.5F;
    }

    public static int string2colorRgb(String hexCode) {
        return Integer.decode("0x" + hexCode);
    }

    public static <E extends Enum<E>> E getEnumByIdSafely(int id, Class<E> type) {
        E[] values = type.getEnumConstants();
        return values[id % values.length];
    }

    public static void encodeInventoryContents(IInventory inventory, PacketBuffer buffer) {
        List<SerializedSlot> slots = new ArrayList<>();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                SerializedSlot serializedSlot = new SerializedSlot(i, stack);
                slots.add(serializedSlot);
            }
        }
        buffer.writeInt(slots.size());
        slots.forEach(slot -> slot.encode(buffer));
    }

    public static SerializedSlot[] decodeInventoryContents(PacketBuffer buffer) {
        int count = buffer.readInt();
        SerializedSlot[] slots = new SerializedSlot[count];
        for (int i = 0; i < count; i++) {
            SerializedSlot serializedSlot = SerializedSlot.decode(buffer);
            slots[i] = serializedSlot;
        }
        return slots;
    }

    @SuppressWarnings("unchecked")
    public static <I> void saveInventoryFromContext(ItemLocator.InventoryContext context) {
        Object object = context.getInventory();
        ItemStack parent = context.getParent();
        if (parent.getItem() instanceof ItemLocator.SaveInventoryProvider) {
            ItemLocator.SaveInventoryProvider<I> provider = (ItemLocator.SaveInventoryProvider<I>) parent.getItem();
            I inventory = (I) object;
            provider.insertEditedItem(inventory, context.getCurrentSlotIndex(), context.getCurrectStack());
            provider.saveInventory(inventory, context.getParent());
        }
    }

    private static Map<SkillCategory, List<SkillType<?>>> splitSkillsIntoCategories(Iterable<SkillType<?>> iterable) {
        Map<SkillCategory, List<SkillType<?>>> map = new EnumMap<>(SkillCategory.class);
        for (SkillType<?> type : iterable) {
            SkillCategory category = type.getHierarchy().getCategory();
            map.computeIfAbsent(category, cat -> new ArrayList<>()).add(type);
        }
        return map;
    }

    public static final class SerializedSlot {

        private final int slotIndex;
        private final ItemStack stack;

        public SerializedSlot(int slotIndex, ItemStack stack) {
            this.slotIndex = slotIndex;
            this.stack = stack;
        }

        public int getSlotIndex() {
            return slotIndex;
        }

        public ItemStack getItemStack() {
            return stack;
        }

        void encode(PacketBuffer buffer) {
            buffer.writeInt(slotIndex);
            buffer.writeItem(stack);
        }

        static SerializedSlot decode(PacketBuffer buffer) {
            return new SerializedSlot(buffer.readInt(), buffer.readItem());
        }

        public static void apply(IInventory inventory, SerializedSlot[] slots) {
            for (SerializedSlot serializedSlot : slots) {
                inventory.setItem(serializedSlot.getSlotIndex(), serializedSlot.getItemStack());
            }
        }
    }

    public interface INBTDeserializer<NBT extends INBT> {
        NBT deserialize(CompoundNBT nbt, String key);
    }

    static {
        DOT_DECIMAL_SEPARATOR.setDecimalSeparator('.');
    }
}

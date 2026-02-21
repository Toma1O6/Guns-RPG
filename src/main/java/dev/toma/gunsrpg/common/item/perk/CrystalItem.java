package dev.toma.gunsrpg.common.item.perk;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.common.perk.PerkType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class CrystalItem extends PerkItem implements IPerkHolder {

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");
    private static final ITextComponent TEXT_ATTRIBUTES = new TranslationTextComponent("crystal.stat.attributes").withStyle(TextFormatting.UNDERLINE, TextFormatting.DARK_GRAY);
    private static final ITextComponent NO_ATTRIBUTES = new TranslationTextComponent("crystal.stat.no_attributes").withStyle(TextFormatting.ITALIC, TextFormatting.RED);

    public CrystalItem(String name, PerkVariant variant) {
        super(name, variant, new Properties().tab(ModTabs.ITEM_TAB).stacksTo(1));
    }

    public static Crystal getCrystal(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if (nbt == null) return null;
        CompoundNBT crystalNbt = nbt.getCompound("crystal");
        return Crystal.fromNbt(crystalNbt);
    }

    public static void addCrystal(ItemStack stack, Crystal crystal) {
        CompoundNBT nbt = stack.getTag();
        if (nbt == null) {
            nbt = new CompoundNBT();
        }
        nbt.put("crystal", crystal.toNbt());
        stack.setTag(nbt);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        Crystal crystal = getCrystal(stack);
        if (crystal == null) return;
        ITextComponent levelValue = new StringTextComponent(String.valueOf(crystal.getLevel())).withStyle(TextFormatting.AQUA);
        ITextComponent levelComponent = new TranslationTextComponent("crystal.stat.level", levelValue).withStyle(TextFormatting.DARK_GRAY);
        list.add(levelComponent);

        List<CrystalAttribute> attributes = crystal.listAttributes();
        if (!attributes.isEmpty()) {
            list.add(TEXT_ATTRIBUTES);
            for (CrystalAttribute attribute : attributes) {
                Perk perk = attribute.getPerk();
                ITextComponent name = perk.getDisplayName().plainCopy().withStyle(TextFormatting.GRAY);
                TextFormatting formatting = attribute.getType() == PerkType.BUFF ? TextFormatting.GREEN : TextFormatting.RED;
                String pct = DECIMAL_FORMAT.format(attribute.getValue() * 100F);
                String prefix = attribute.getValue() >= 0 ? "+" : "";
                ITextComponent pctLabel = new StringTextComponent(prefix + pct + "%").withStyle(formatting);
                list.add(new TranslationTextComponent("crystal.stat.attribute", name, pctLabel).withStyle(TextFormatting.DARK_GRAY));
            }
        } else {
            list.add(NO_ATTRIBUTES);
        }
    }

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMAT.setDecimalFormatSymbols(symbols);
    }
}

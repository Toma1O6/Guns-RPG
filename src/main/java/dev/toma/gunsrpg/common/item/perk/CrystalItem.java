package dev.toma.gunsrpg.common.item.perk;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.common.perk.PerkType;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.SkillUtil;
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

    public CrystalItem(String name, PerkVariant variant) {
        super(name, variant, new Properties().tab(ModTabs.ITEM_TAB).stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> list, ITooltipFlag flag) {
        CompoundNBT nbt = stack.getTag();
        if (nbt == null) return;
        CompoundNBT crystalNbt = nbt.getCompound("crystal");
        Crystal crystal = Crystal.fromNbt(crystalNbt);
        list.add(new StringTextComponent("Level: " + TextFormatting.AQUA + crystal.getLevel()));
        list.add(new StringTextComponent("Attributes"));
        for (CrystalAttribute attribute : crystal.listAttributes()) {
            Perk perk = attribute.getPerk();
            ITextComponent name = SkillUtil.Localizations.makeReadable(perk.getPerkId());
            TextFormatting formatting = attribute.getType() == PerkType.BUFF ? TextFormatting.GREEN : TextFormatting.RED;
            String pct = DECIMAL_FORMAT.format(attribute.getValue() * 100F);
            list.add(new StringTextComponent(formatting + name.getString() + ": " + pct + "%"));
        }
    }

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DECIMAL_FORMAT.setDecimalFormatSymbols(symbols);
    }
}

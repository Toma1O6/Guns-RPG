package dev.toma.gunsrpg.common.item.guns.ammo;

import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import dev.toma.gunsrpg.api.common.IAmmoProvider;
import dev.toma.gunsrpg.common.item.BaseItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class AmmoItem extends BaseItem implements IAmmoProvider {

    private final AmmoType ammoType;
    private final IAmmoMaterial material;
    private final IMaterialData data;

    public AmmoItem(String name, AmmoType ammoType, IAmmoMaterial material) {
        super(name, new Properties().tab(ModTabs.ITEM_TAB));
        this.material = material;
        this.ammoType = ammoType;
        this.data = ammoType.container != null ? ammoType.container.getMaterialData(material) : MaterialData.EMPTY;
    }

    @Override
    public AmmoType getAmmoType() {
        return ammoType;
    }

    @Override
    public IAmmoMaterial getMaterial() {
        return material;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> text, ITooltipFlag flags) {
        ITextComponent recoil = formatNumber("Recoil", data.getAddedRecoil());
        ITextComponent durability = formatNumber("Durability", data.getAddedDurability(), true);
        ITextComponent jam = formatNumber("Jamming", data.getAddedJamChance());
        if (recoil != null || durability != null || jam != null) {
            text.add(new StringTextComponent("Attributes:"));
            if (recoil != null)
                text.add(recoil);
            if (durability != null)
                text.add(durability);
            if (jam != null)
                text.add(jam);
        }
    }

    private ITextComponent formatNumber(String key, float value) {
        return formatNumber(key, value, false);
    }

    private ITextComponent formatNumber(String key, float value, boolean invertColors) {
        if (value == 0.0)
            return null;
        int pct = Math.round(value * 100);
        TextFormatting good = invertColors ? TextFormatting.RED : TextFormatting.GREEN;
        TextFormatting bad = invertColors ? TextFormatting.GREEN : TextFormatting.RED;
        TextFormatting formatting = value < 0 ? good : bad;
        String raw = String.format("%s%s: %d%%", formatting, key, pct);
        return new StringTextComponent(raw);
    }
}

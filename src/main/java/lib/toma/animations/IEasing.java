package lib.toma.animations;

import net.minecraft.util.text.ITextComponent;

/**
 *
 *
 * @author Toma
 * @since 1.2.15
 */
public interface IEasing {

    byte getEasingId();

    void setEasingId(byte id);

    float ease(float in);

    String getEasingName();

    ITextComponent getDisplayText();
}

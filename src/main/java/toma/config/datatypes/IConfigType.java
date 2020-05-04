package toma.config.datatypes;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import toma.config.object.IConfigSerializable;
import toma.config.ui.ModConfigScreen;
import toma.config.ui.widget.ConfigWidget;

public interface IConfigType<T> extends IConfigSerializable {

    String displayName();

    T value();

    void set(T value);

    void onLoad();

    boolean hasComment();

    String getComment();

    @SideOnly(Side.CLIENT)
    ConfigWidget<T> create(int x, int y, int w, int h, ModConfigScreen screen);
}

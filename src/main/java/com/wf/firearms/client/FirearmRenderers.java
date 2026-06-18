package com.wf.firearms.client;

import com.wf.firearms.client.render.AbstractWeaponRenderer;
import com.wf.firearms.client.render.weapon.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@OnlyIn(Dist.CLIENT)
public final class FirearmRenderers {
    private static final Map<String, AbstractWeaponRenderer> BY_KEY = new ConcurrentHashMap<>();

    private FirearmRenderers() {}

    public static void init() {
        register("m1911", new M1911Renderer());
        register("r45", new R45Renderer());
        register("desert_eagle", new DesertEagleRenderer());
        register("thompson", new ThompsonRenderer());
        register("vector", new VectorRenderer());
        register("akm", new AkmRenderer());
        register("vss", new VssRenderer());
        register("kar98k", new Kar98kRenderer());
        register("winchester", new WinchesterRenderer());
        register("awm", new AwmRenderer());
        register("s686", new S686Renderer());
        register("s1897", new S686Renderer());
        register("s12k", new S686Renderer());
        register("minigun", new MinigunRenderer());
    }

    private static void register(String key, AbstractWeaponRenderer renderer) {
        BY_KEY.put(key, renderer);
    }

    public static AbstractWeaponRenderer getRenderer(String weaponKey) {
        if (BY_KEY.isEmpty()) {
            init();
        }
        AbstractWeaponRenderer renderer = BY_KEY.get(weaponKey);
        if (renderer != null) {
            return renderer;
        }
        return BY_KEY.get("m1911");
    }
}

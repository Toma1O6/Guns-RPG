package com.wf.firearms.config.settings;

import com.wf.firearms.GunsRpg;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Config(id = "airdrop", group = GunsRpg.MOD_ID)
public final class AirdropSettings {
    @Configurable
    @Configurable.Comment("Days between natural airdrops")
    public int intervalDays = 7;

    @Configurable
    public int testIntervalDays = 1;

    @Configurable
    public int spawnRadius = 128;

    @Configurable
    public boolean testMode = false;

    @Configurable
    public float gunDropChance = 0.25f;

    @Configurable
    public int grenadeCountMin = 4;

    @Configurable
    public int grenadeCountMax = 16;

    @Configurable
    public float grenadeDropChanceMin = 0.06f;

    @Configurable
    public float grenadeDropChanceMax = 0.42f;

    @Configurable
    public Map<String, Float> tier4Guns = defaultTier4();

    @Configurable
    public List<String> tier23Guns = defaultTier23();

    private static Map<String, Float> defaultTier4() {
        Map<String, Float> map = new LinkedHashMap<>();
        map.put("r45", 0.07f);
        map.put("p90", 0.06f);
        map.put("s686", 0.06f);
        map.put("mk14ebr", 0.06f);
        map.put("awm", 0.04f);
        map.put("m249", 0.06f);
        return map;
    }

    private static List<String> defaultTier23() {
        return new ArrayList<>(List.of(
                "glock",
                "m1911",
                "ump45",
                "vector",
                "uzi",
                "akm",
                "hk416",
                "type_81",
                "fn_fal",
                "sks",
                "spr15",
                "desert_eagle",
                "kar98k",
                "winchester",
                "aug",
                "s1897",
                "s12k"));
    }
}

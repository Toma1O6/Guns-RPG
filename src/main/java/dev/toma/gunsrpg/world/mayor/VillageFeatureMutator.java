package dev.toma.gunsrpg.world.mayor;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;

import java.util.function.Function;

public final class VillageFeatureMutator {

    private static final String[] VARIANTS = { "desert", "plains", "savanna", "snowy", "taiga" };

    public static void mutateVanillaVillages() {
        for (String village : VARIANTS) {
            addMayorStructure("village/" + village);
        }
    }

    public static void addMayorStructure(String villageDir) {
        ResourceLocation key = new ResourceLocation(villageDir + "/houses");
        JigsawPattern pattern = WorldGenRegistries.TEMPLATE_POOL.get(key);
        pattern.templates.clear();
        Pair<Function<JigsawPattern.PlacementBehaviour, ? extends JigsawPiece>, Integer> pair = Pair.of(JigsawPiece.legacy(villageDir + "/houses/mayor_home"), 100);
        JigsawPiece piece = pair.getFirst().apply(JigsawPattern.PlacementBehaviour.RIGID);
        pattern.rawTemplates = ImmutableList.<Pair<JigsawPiece, Integer>>builder()
                .addAll(pattern.rawTemplates)
                .add(Pair.of(piece, pair.getSecond()))
                .build();
        pattern.rawTemplates.forEach(rawTemplate -> {
            JigsawPiece jigsawPiece = rawTemplate.getFirst();
            for (int i = 0; i < rawTemplate.getSecond(); i++) {
                pattern.templates.add(jigsawPiece);
            }
        });
    }
}

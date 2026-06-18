package com.wf.firearms.client;

import com.wf.firearms.combat.AmmoCaliber;
import com.wf.firearms.combat.AmmoMaterial;
import com.wf.firearms.combat.AmmoStatsRegistry;
import com.wf.firearms.combat.FirearmCombat;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.combat.FirearmStackState;
import com.wf.firearms.data.LevelingStrategy;
import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.item.FirearmItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public final class FirearmHudOverlay {
    public static final IGuiOverlay OVERLAY = FirearmHudOverlay::render;

    private static final LevelingStrategy LEVELING = LevelingStrategy.load();
    private static final int COLOR_PLAYER = 0xFF55AAFF;
    private static final int COLOR_PLAYER_2 = 0xFF2266CC;
    private static final int COLOR_WEAPON = 0xFFFFAA44;
    private static final int COLOR_WEAPON_2 = 0xFFCC6622;

    private FirearmHudOverlay() {}

    private static void render(ForgeGui gui, GuiGraphics g, float partialTick, int width, int height) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) {
            return;
        }
        ItemStack stack = FirearmCombat.getHeldGun(mc.player);
        if (stack.isEmpty() || !(stack.getItem() instanceof FirearmItem firearm)) {
            return;
        }

        FirearmSpec spec = firearm.getSpec();
        FirearmStackState.ensureInitialized(stack, spec);

        int ammo = FirearmStackState.getAmmo(stack);
        int magSize = spec.magazineSize();
        boolean jammed = FirearmStackState.isJammed(stack);
        FirearmStackState.Action action = FirearmStackState.getAction(stack);
        long actionStart = FirearmStackState.getActionStart(stack);
        long actionEnd = FirearmStackState.getActionEnd(stack);

        int cx = width / 2;
        int cy = height / 2;
        boolean aiming = FirearmStackState.isAiming(stack);
        drawCrosshair(g, cx, cy, aiming);

        if (ammo > 0 && FirearmStackState.hasLoadedAmmoType(stack)) {
            AmmoMaterial mat = FirearmStackState.getLoadedMaterial(stack);
            AmmoCaliber loadedCal = FirearmStackState.getLoadedCaliber(stack);
            if (loadedCal == AmmoCaliber.UNKNOWN) {
                loadedCal = AmmoCaliber.forWeapon(spec);
            }
            Component matLine =
                    Component.translatable(
                            "gunsrpg.gun.hud_material",
                            Component.translatable("gunsrpg.ammo.material." + mat.getId()),
                            AmmoStatsRegistry.damagePercent(mat, loadedCal));
            FirearmHudDraw.drawMaterialLineBottomRight(g, mc.font, width, height, matLine);
        }

        FirearmHudDraw.drawAmmoPanelBottomRight(g, mc.font, width, height, ammo, magSize, ammo == 0 && !jammed);

        if (jammed) {
            Component jam = Component.translatable("gunsrpg.gun.jammed");
            g.drawString(mc.font, jam, cx - mc.font.width(jam) / 2, cy + 12, 0xFFFF5555, true);
        }

        if (action == FirearmStackState.Action.RELOAD || action == FirearmStackState.Action.UNJAM) {
            int barW = 64;
            int x0 = cx - barW / 2;
            int y0 = cy + 28;
            FirearmHudDraw.drawSolid(g, x0 - 1, y0 - 1, x0 + barW + 1, y0 + 7, 0xAA000000);
            float p = actionProgress(mc, actionStart, actionEnd);
            int fillColor = action == FirearmStackState.Action.RELOAD ? 0xFF55AAFF : 0xFFFFAA55;
            FirearmHudDraw.drawSolid(g, x0, y0, x0 + (int) (barW * p), y0 + 5, fillColor);
        }

        if (spec.supportsFireModeSwitch()) {
            var mode = FirearmStackState.getFireMode(stack, spec);
            Component modeText =
                    Component.translatable(
                            "gunsrpg.gun.firemode",
                            Component.translatable("gunsrpg.gun.firemode." + mode.name().toLowerCase()));
            g.drawString(mc.font, modeText, 8, height - 72, 0xFFCCCCCC, false);
        }

        renderProgressionBars(g, mc, width, height, mc.player, firearm.getWeaponKey());
        renderWorldDifficulty(g, mc, width, height);
    }

    private static void renderProgressionBars(
            GuiGraphics g, Minecraft mc, int width, int height, net.minecraft.world.entity.player.Player player, String weaponKey) {
        int barX = width / 2 - (FirearmHudDraw.BAR_WIDTH + 24);
        int y = height - FirearmHudDraw.barBlockHeight() * 2 - 50;

        int playerLevel = PlayerFirearmsData.getPlayerLevel(player);
        int playerKills = PlayerFirearmsData.getTotalKills(player);
        float playerProgress = killProgress(LEVELING.playerLevel(), playerKills, playerLevel);
        FirearmHudDraw.drawKillProgressBar(
                g, mc.font, barX, y, playerLevel, playerProgress, COLOR_PLAYER, COLOR_PLAYER_2);

        int weaponLevel = PlayerFirearmsData.getWeaponLevel(player, weaponKey);
        int weaponKills = PlayerFirearmsData.getWeaponKills(player, weaponKey);
        float weaponProgress = killProgress(LEVELING.weaponLevel(), weaponKills, weaponLevel);
        FirearmHudDraw.drawKillProgressBar(
                g,
                mc.font,
                barX,
                y + FirearmHudDraw.barBlockHeight(),
                weaponLevel,
                weaponProgress,
                COLOR_WEAPON,
                COLOR_WEAPON_2);
    }

    private static float killProgress(LevelingStrategy.LevelCurve curve, int kills, int level) {
        int next = level + 1;
        if (level >= curve.maxLevel()) {
            return 1f;
        }
        int prevReq = level <= 0 ? 0 : curve.killsRequiredForLevel(level);
        int nextReq = curve.killsRequiredForLevel(next);
        if (nextReq <= prevReq) {
            return 1f;
        }
        return (float) (kills - prevReq) / (nextReq - prevReq);
    }

    private static void renderWorldDifficulty(GuiGraphics g, Minecraft mc, int width, int height) {
        if (mc.level == null) {
            return;
        }
        float diff = mc.level.getCurrentDifficultyAt(mc.player.blockPosition()).getEffectiveDifficulty();
        FirearmHudDraw.drawDifficultyBadge(g, mc.font, 6, height - 36, diff);
    }

    private static float actionProgress(Minecraft mc, long start, long end) {
        if (mc.level == null || end <= start) {
            return 0f;
        }
        long now = mc.level.getGameTime();
        return Math.min(1f, Math.max(0f, (float) (now - start) / (end - start)));
    }

    private static void drawCrosshair(GuiGraphics g, int cx, int cy, boolean aiming) {
        int color = FirearmCrosshairStyle.colorArgb();
        int size = aiming ? 6 : 4;
        String type = FirearmCrosshairStyle.typeKey();
        switch (type) {
            case "cross" -> {
                g.fill(cx - size, cy - 1, cx + size + 1, cy, color);
                g.fill(cx - 1, cy - size, cx, cy + size + 1, color);
            }
            case "circle" -> {
                for (int i = 0; i < 8; i++) {
                    double a = i * Math.PI / 4;
                    int px = cx + (int) (Math.cos(a) * size);
                    int py = cy + (int) (Math.sin(a) * size);
                    g.fill(px, py, px + 1, py + 1, color);
                }
            }
            case "bracket" -> {
                g.fill(cx - size - 2, cy - size, cx - size, cy + size, color);
                g.fill(cx + size + 1, cy - size, cx + size + 3, cy + size, color);
            }
            default -> g.fill(cx - 1, cy - 1, cx + 1, cy + 1, color);
        }
    }
}

package dev.toma.gunsrpg.client;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.debuffs.Debuff;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.object.ShootingManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = GunsRPG.MODID)
public class ClientEventHandler {

    private static final ResourceLocation ICON_BACKGROUND = GunsRPG.makeResource("textures/icons/background.png");

    @SubscribeEvent
    public static void renderOverlay(RenderGameOverlayEvent.Post event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer player = mc.player;
            FontRenderer renderer = mc.fontRenderer;
            long day = player.world.getWorldTime() / 24000L;
            boolean b = day % 7 == 0 && day > 0;
            long l = b ? 1 : 8 - day % 7;
            ScaledResolution resolution = event.getResolution();
            mc.fontRenderer.drawStringWithShadow(l + "", resolution.getScaledWidth() - 10, 6, b ? 0xff0000 : l > 1 && l < 4 ? 0xffff00 : 0xffffff);
            PlayerData data = PlayerDataFactory.get(player);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            if(data != null) {
                DebuffData debuffData = data.getDebuffData();
                int offset = 0;
                for(Debuff debuff : debuffData.getDebuffs()) {
                    if(!debuff.isActive()) continue;
                    int yStart = event.getResolution().getScaledHeight() + GRPGConfig.client.debuffs.y - 50;
                    ModUtils.renderTexture(GRPGConfig.client.debuffs.x, yStart + offset * 18, 50, yStart + (1 + offset) * 18, ICON_BACKGROUND);
                    ModUtils.renderTexture(GRPGConfig.client.debuffs.x + 2, yStart + 1 + offset * 18, 18, yStart + 1 + offset * 18 + 16, debuff.getIconTexture());
                    renderer.drawStringWithShadow(debuff.getLevel() + "%", 20, yStart + 5 + offset * 18, 0xFFFFFF);
                    ++offset;
                }
            }
        }
    }

    @SubscribeEvent
    public static void mouseInputEvent(InputEvent.MouseInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;
        GameSettings settings = mc.gameSettings;
        if(player != null) {
            GunItem item = ShootingManager.getGunFrom(player);
            if(item != null && settings.keyBindAttack.isPressed()) {
                ShootingManager.shootSingle(player, player.getHeldItemMainhand());
            }
        }
    }
}

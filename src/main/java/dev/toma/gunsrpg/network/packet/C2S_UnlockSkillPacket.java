package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.ISkills;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_UnlockSkillPacket extends AbstractNetworkPacket<C2S_UnlockSkillPacket> {

    private boolean hasParent;
    private SkillType<?> clicked;
    private SkillType<?> parent;

    public C2S_UnlockSkillPacket() {
    }

    public C2S_UnlockSkillPacket(SkillType<?> clicked) {
        this(clicked, null);
    }

    public C2S_UnlockSkillPacket(SkillType<?> clicked, SkillType<?> parent) {
        this.clicked = clicked;
        this.parent = parent;
        this.hasParent = parent != null;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeBoolean(hasParent);
        buffer.writeResourceLocation(clicked.getRegistryName());
        if (hasParent)
            buffer.writeResourceLocation(parent.getRegistryName());
    }

    @Override
    public C2S_UnlockSkillPacket decode(PacketBuffer buffer) {
        boolean hasParent = buffer.readBoolean();
        SkillType<?> clicked = ModRegistries.SKILLS.getValue(buffer.readResourceLocation());
        SkillType<?> parent = hasParent ? ModRegistries.SKILLS.getValue(buffer.readResourceLocation()) : null;
        return new C2S_UnlockSkillPacket(clicked, parent);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        PlayerData.get(player).ifPresent(data -> {
            ISkills skills = data.getSkills();
            if (clicked == null) {
                logInvalidPacket("Clicked skill is null");
                return;
            }
            if (parent != null) {
                if (!ModUtils.contains(clicked, parent.getChilds())) {
                    logInvalidPacket("Supplied parent skill is not actual parent!");
                    return;
                }
                if (!skills.hasSkill(parent)) {
                    logInvalidPacket("Parent skill is not unlocked!");
                    return;
                }
            } else {

                // validates that parent skill is unlocked
                for (SkillType<?> type : ModRegistries.SKILLS) {
                    if (ModUtils.contains(clicked, type.getChilds())) {
                        if (!skills.hasSkill(type)) {
                            logInvalidPacket("Parent skill is not unlocked!");
                            return;
                        }
                        break;
                    }
                }
            }
            if (!clicked.getCriteria().canUnlock(data, clicked)) {
                logInvalidPacket("Player cannot unlock this skill yet!");
                return;
            }
            clicked.getCriteria().onUnlocked(data.getGenericData(), clicked);
            data.sync(DataFlags.SKILLS | DataFlags.DATA);
        });
    }

    private void logInvalidPacket(String message) {
        GunsRPG.log.fatal("Received invalid skill activation packet - {}", message);
    }
}

package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.init.GunsRPGRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SPacketUnlockSkill extends AbstractNetworkPacket<SPacketUnlockSkill> {

    private boolean hasParent;
    private SkillType<?> clicked;
    private SkillType<?> parent;

    public SPacketUnlockSkill() {
    }

    public SPacketUnlockSkill(SkillType<?> clicked) {
        this(clicked, null);
    }

    public SPacketUnlockSkill(SkillType<?> clicked, SkillType<?> parent) {
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
    public SPacketUnlockSkill decode(PacketBuffer buffer) {
        boolean hasParent = buffer.readBoolean();
        SkillType<?> clicked = GunsRPGRegistries.SKILLS.getValue(buffer.readResourceLocation());
        SkillType<?> parent = hasParent ? GunsRPGRegistries.SKILLS.getValue(buffer.readResourceLocation()) : null;
        return new SPacketUnlockSkill(clicked, parent);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        PlayerDataFactory.get(player).ifPresent(data -> {
            PlayerSkills skills = data.getSkills();
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
                for (SkillType<?> type : GunsRPGRegistries.SKILLS) {
                    if (ModUtils.contains(clicked, type.getChilds())) {
                        if (!skills.hasSkill(type)) {
                            logInvalidPacket("Parent skill is not unlocked!");
                            return;
                        }
                        break;
                    }
                }
            }
            if (!clicked.getCriteria().isUnlockAvailable(data, clicked)) {
                logInvalidPacket("Player cannot unlock this skill yet!");
                return;
            }
            clicked.getCriteria().onActivated(data, clicked);
            data.sync();
        });
    }

    private void logInvalidPacket(String message) {
        GunsRPG.log.fatal("Received invalid skill activation packet - {}", message);
    }
}

package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.DataFlags;
import dev.toma.gunsrpg.api.common.data.ISkillProvider;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_UnlockSkillPacket extends AbstractNetworkPacket<C2S_UnlockSkillPacket> {

    private SkillType<?> toUnlock;

    public C2S_UnlockSkillPacket() {
    }

    public C2S_UnlockSkillPacket(SkillType<?> toUnlock) {
        this.toUnlock = toUnlock;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(toUnlock.getRegistryName());
    }

    @Override
    public C2S_UnlockSkillPacket decode(PacketBuffer buffer) {
        SkillType<?> toUnlock = ModRegistries.SKILLS.getValue(buffer.readResourceLocation());
        return new C2S_UnlockSkillPacket(toUnlock);
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        PlayerData.get(player).ifPresent(data -> {
            ISkillProvider provider = data.getSkillProvider();
            if (toUnlock == null) {
                logInvalidPacket("Clicked skill is null");
                return;
            }
            if (provider.hasSkill(toUnlock)) {
                logInvalidPacket("Skill is already unlocked");
                return;
            }
            ISkillHierarchy<?> hierarchy = toUnlock.getHierarchy();
            SkillType<?> parent = hierarchy.getParent();
            if (parent != null) {
                if (!provider.hasSkill(parent)) {
                    logInvalidPacket("Parent skill is not unlocked!");
                    return;
                }
            }
            ISkillProperties properties = toUnlock.getProperties();
            ITransactionValidator validator = properties.getTransactionValidator();
            if (!validator.canUnlock(data, toUnlock)) {
                logInvalidPacket("Player cannot unlock this skill yet!");
                return;
            }
            validator.onUnlocked(data.getProgressData(), toUnlock);
            data.sync(DataFlags.SKILLS | DataFlags.DATA | DataFlags.WEAPON_POOL);
        });
    }

    private void logInvalidPacket(String message) {
        GunsRPG.log.fatal("Received invalid skill unlock packet - {}", message);
    }
}

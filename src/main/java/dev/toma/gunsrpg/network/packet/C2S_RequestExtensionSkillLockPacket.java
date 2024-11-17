package dev.toma.gunsrpg.network.packet;

import dev.toma.gunsrpg.api.common.data.*;
import dev.toma.gunsrpg.api.common.skill.ISkillHierarchy;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.network.AbstractNetworkPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class C2S_RequestExtensionSkillLockPacket extends AbstractNetworkPacket<C2S_RequestExtensionSkillLockPacket> {

    private final SkillType<?> head;

    public C2S_RequestExtensionSkillLockPacket() {
        this(null);
    }

    public C2S_RequestExtensionSkillLockPacket(SkillType<?> head) {
        this.head = head;
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeRegistryId(head);
    }

    @Override
    public C2S_RequestExtensionSkillLockPacket decode(PacketBuffer buffer) {
        return new C2S_RequestExtensionSkillLockPacket(buffer.readRegistryId());
    }

    @Override
    protected void handlePacket(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        PlayerData.get(player).ifPresent(data -> {
            ISkillProvider skillProvider = data.getSkillProvider();
            IPerkProvider provider = data.getPerkProvider();
            if (provider.getPoints() < GunKillData.SKILL_RESET_PRICE) {
                return;
            }
            ISkillHierarchy<?> hierarchy = head.getHierarchy();
            SkillType<?>[] children = hierarchy.getExtensions();
            if (children == null || children.length == 0) {
                return;
            }
            boolean lockedAny = false;
            for (SkillType<?> type : children) {
                ISkillProperties childProps = type.getProperties();
                if (skillProvider.hasSkill(type)) {
                    lockedAny = true;
                    skillProvider.lock(type);
                    ITransactionValidator validator = childProps.getTransactionValidator();
                    IPointProvider pointProvider = validator.getData(data);
                    pointProvider.addPoints(childProps.getPrice());
                }
            }
            if (lockedAny) {
                provider.addPoints(-GunKillData.SKILL_RESET_PRICE);
            }
            data.sync(DataFlags.WILDCARD);
        });
    }
}

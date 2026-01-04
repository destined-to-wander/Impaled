package ladysnake.impaled.mixin.homing;

import ladysnake.impaled.common.entity.IPlayerTargeting;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static ladysnake.impaled.common.Impaled.targetPacket;

@Mixin({ClientPlayerEntity.class})
public class ClientPlayerEntityMixin implements IPlayerTargeting {
    @Unique
    @Nullable LivingEntity lastTarget;
    @Unique
    int targetDecayTime;

    public ClientPlayerEntityMixin() {
    }

    public LivingEntity getLastTarget() {
        return this.lastTarget;
    }

    public void setLastTarget(LivingEntity target) {
        this.lastTarget = target;
        this.targetDecayTime = 60;
        if (target != null) {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeInt(target.getId());
            ClientPlayNetworking.send(targetPacket, buf);
        }

    }

    @Inject(
            method = {"tick"},
            at = {@At("TAIL")}
    )
    private void decayTarget(CallbackInfo ci) {
        if (this.targetDecayTime > 0) {
            --this.targetDecayTime;
            if (this.targetDecayTime == 0) {
                this.setLastTarget((LivingEntity)null);
            }
        }

    }
}

package ladysnake.impaled.mixin.homing;

import ladysnake.impaled.common.entity.IPlayerTargeting;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ServerPlayerEntity.class})
public abstract class ServerPlayerEntityMixin extends LivingEntity implements IPlayerTargeting {
    @Unique
    @Nullable LivingEntity lastTarget;
    @Unique
    int targetDecayTime;

    protected ServerPlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    public LivingEntity getLastTarget() {
        return this.lastTarget;
    }

    public void setLastTarget(LivingEntity target) {
        this.lastTarget = target;
        this.targetDecayTime = 60;
    }

    @Inject(
            method = {"tick"},
            at = {@At("TAIL")}
    )
    private void decayTarget(CallbackInfo ci) {
        if (this.targetDecayTime > 0) {
            --this.targetDecayTime;
            if (this.targetDecayTime == 0) {
                this.setLastTarget(null);
            }
        }

    }
}

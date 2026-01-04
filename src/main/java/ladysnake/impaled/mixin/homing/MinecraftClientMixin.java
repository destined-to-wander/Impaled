package ladysnake.impaled.mixin.homing;

import ladysnake.impaled.common.entity.IPlayerTargeting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin({MinecraftClient.class})
public abstract class MinecraftClientMixin {
    @Shadow
    public @Nullable ClientWorld world;
    @Shadow
    public @Nullable ClientPlayerEntity player;

    public MinecraftClientMixin() {
    }

    @Shadow
    public abstract @Nullable Entity getCameraEntity();

    @Inject(
            method = {"tick"},
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/tutorial/TutorialManager;tick(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/util/hit/HitResult;)V"
            )}
    )
    public void setLastTarget(CallbackInfo ci) {
        Entity camera = this.getCameraEntity();
        if (this.player != null) {
            if (camera != null) {
                if (this.world != null) {
                    double distanceCap = 16384.0F;
                    Vec3d cameraPos = camera.getCameraPosVec(1.0F);
                    Vec3d cameraRot = camera.getRotationVec(1.0F);
                    Vec3d cameraTarget = cameraPos.add(cameraRot.multiply(distanceCap));
                    Box box = camera.getBoundingBox().stretch(cameraTarget).expand(1.0F, 1.0F, 1.0F);
                    Predicate<Entity> predicate = this::isValidTarget;
                    EntityHitResult entityHitResult = raycast(this.player, box, predicate);
                    if (entityHitResult != null) {
                        Entity entityTarget = entityHitResult.getEntity();
                        if (entityTarget instanceof LivingEntity) {
                            LivingEntity living = (LivingEntity)entityTarget;
                            ClientPlayerEntity var14 = this.player;
                            if (var14 instanceof IPlayerTargeting) {
                                IPlayerTargeting targeting = (IPlayerTargeting)var14;
                                targeting.setLastTarget(living);
                            }
                        }
                    }

                }
            }
        }
    }

    public boolean isValidTarget(@Nullable Entity target) {
        if (target instanceof LivingEntity living) {
            if (this.player == null) {
                return false;
            } else if (target == this.player) {
                return false;
            } else if (!this.player.canSee(target)) {
                return false;
            } else if (living.isDead()) {
                return false;
            } else if (target.isRemoved()) {
                return false;
            } else if (target.isTeammate(this.player)) {
                return false;
            } else if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(target)) {
                return false;
            } else {
                if (target instanceof TameableEntity) {
                    TameableEntity tamed = (TameableEntity)target;
                    if (tamed.isOwner(this.player)) {
                        return false;
                    }
                }

                return target.canHit();
            }
        } else {
            return false;
        }
    }

    private static EntityHitResult raycast(Entity player, Box box, Predicate<Entity> predicate) {
        Entity target = null;
        double targetDistance = 0.01;
        Vec3d rotationVec = player.getRotationVector();

        for(Entity possibleTarget : player.getWorld().getEntitiesByClass(Entity.class, box, predicate)) {
            if (possibleTarget.getRootVehicle() != player.getRootVehicle()) {
                Vec3d playerDistance = possibleTarget.getPos().add((double)0.0F, (double)(possibleTarget.getHeight() / 2.0F), (double)0.0F).subtract(player.getEyePos());
                double distance = (double)1.0F - playerDistance.normalize().dotProduct(rotationVec);
                if (!(distance > targetDistance)) {
                    target = possibleTarget;
                    targetDistance = distance;
                }
            }
        }

        return target == null ? null : new EntityHitResult(target, target.getPos());
    }
}

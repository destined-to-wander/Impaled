package ladysnake.impaled.common.entity;

import net.minecraft.entity.LivingEntity;

public interface IPlayerTargeting {
    LivingEntity getLastTarget();

    void setLastTarget(LivingEntity var1);
}

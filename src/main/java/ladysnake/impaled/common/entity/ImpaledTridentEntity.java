package ladysnake.impaled.common.entity;

import ladysnake.impaled.common.compat.ImpaledCompat;
import ladysnake.impaled.mixin.TridentEntityAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ImpaledTridentEntity extends TridentEntity {
    public ImpaledTridentEntity(EntityType<? extends ImpaledTridentEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setTridentAttributes(ItemStack stack) {
        this.setTridentStack(stack.copy());

        if (FabricLoader.getInstance().isModLoaded("enchancement") && this.getOwner() instanceof LivingEntity livingEntity)
            ImpaledCompat.tryApplyLeech(livingEntity, stack, this);

        this.dataTracker.set(TridentEntityAccessor.impaled$getLoyalty(), (byte) EnchantmentHelper.getLoyalty(stack));
        this.dataTracker.set(TridentEntityAccessor.impaled$getEnchanted(), stack.hasGlint());
    }

    public void setTridentStack(ItemStack tridentStack) {
        ((TridentEntityAccessor) this).impaled$setTridentStack(tridentStack);
    }

    protected void setDealtDamage() {
        ((TridentEntityAccessor) this).impaled$setDealtDamage(true);
    }

    protected boolean hasDealtDamage() {
        return ((TridentEntityAccessor) this).impaled$hasDealtDamage();
    }
}

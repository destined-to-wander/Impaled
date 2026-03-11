package ladysnake.impaled.common.compat;

import moriyashiine.enchancement.common.component.entity.LeechComponent;
import moriyashiine.enchancement.common.component.entity.WarpComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;

public class EnchancementCompat {
    public static void addTridentComponents(LivingEntity livingEntity, ItemStack stack, PersistentProjectileEntity trident){
        LeechComponent.maybeSet(livingEntity, stack, trident);
        WarpComponent.maybeSet(livingEntity, stack, trident);
    }
}

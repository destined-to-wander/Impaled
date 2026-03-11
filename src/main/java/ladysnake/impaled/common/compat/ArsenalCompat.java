package ladysnake.impaled.common.compat;

import dev.doctor4t.arsenal.cca.ArsenalComponents;
import dev.doctor4t.arsenal.cca.BackWeaponComponent;
import dev.doctor4t.arsenal.util.ProjectileSlotHolder;
import dev.doctor4t.arsenal.util.WeaponSlotHolder;
import ladysnake.impaled.common.item.ImpaledTridentItem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ArsenalCompat {
    public static void decrementImpaledTridentStack(PlayerEntity player, ItemStack stack, Entity entity) {
        if (!FabricLoader.getInstance().isModLoaded("arsenal")) return;
        BackWeaponComponent backWeaponComponent = ArsenalComponents.BACK_WEAPON_COMPONENT.get(player);
        if (backWeaponComponent.getBackWeapon().getItem() instanceof ImpaledTridentItem)
            backWeaponComponent.getBackWeapon().decrement(1);

        // idk what this does
        if (player.getInventory() instanceof WeaponSlotHolder holder && entity instanceof ProjectileSlotHolder slotHolder) {
            int index = holder.arsenal$getSlotHolding(stack);
            if (index != -1) slotHolder.arsenal$setOwnedSlot(index);
        }
    }

    public static boolean backslotActive(PlayerEntity player){
        return BackWeaponComponent.isHoldingBackWeapon(player);
    }

    public static boolean tryInsertIntoBackslot(PlayerEntity player, ItemStack stack){
        if (!BackWeaponComponent.getBackWeapon(player).isEmpty()) return false;
        return BackWeaponComponent.setBackWeapon(player, stack);
    }

}


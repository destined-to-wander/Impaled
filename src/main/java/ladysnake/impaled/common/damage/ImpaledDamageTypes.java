package ladysnake.impaled.common.damage;

import net.minecraft.entity.damage.DamageEffects;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public interface ImpaledDamageTypes {
    RegistryKey<DamageType> HELLFORK_HEAT = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("hellfork_heat"));

    static void bootstrap(Registerable<DamageType> damageTypeRegisterable) {
        damageTypeRegisterable.register(HELLFORK_HEAT, new DamageType("hellforkHeat", 0.1F, DamageEffects.BURNING));
    }
}

package ladysnake.impaled.common.damage;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registry;

public class ImpaledDamageSources {
    private final Registry<DamageType> registry;
    private final DamageSource hellforkHeat;

    public ImpaledDamageSources(Registry<DamageType> registry) {
        this.registry = registry;
        this.hellforkHeat = new DamageSource(this.registry.entryOf(ImpaledDamageTypes.HELLFORK_HEAT));
    }

    public DamageSource hellforkHeat() {
        return this.hellforkHeat;
    }
}

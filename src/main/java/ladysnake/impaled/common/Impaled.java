package ladysnake.impaled.common;

import ladysnake.impaled.common.damage.ImpaledDamageSources;
import ladysnake.impaled.common.entity.IPlayerTargeting;
import ladysnake.impaled.common.init.ImpaledEntityTypes;
import ladysnake.impaled.common.init.ImpaledItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class Impaled implements ModInitializer {
    public static final String MODID = "impaled";

    public static final Identifier targetPacket = new Identifier(MODID, ".target");

    private static final Identifier BASTION_TREASURE_CHEST_LOOT_TABLE_ID = new Identifier("minecraft", "chests/bastion_treasure");

    @Override
    public void onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(targetPacket, (minecraftServer, serverPlayer, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
            int id = packetByteBuf.readInt();
            minecraftServer.execute(() -> {
                if (serverPlayer instanceof IPlayerTargeting targeting) {
                    Entity temp = serverPlayer.getWorld().getEntityById(id);
                    if (temp instanceof LivingEntity living) {
                        targeting.setLastTarget(living);
                    }
                }

            });
        });

        ImpaledEntityTypes.init();
        ImpaledItems.init();

        // add loot to dungeons, mineshafts, jungle temples, and stronghold libraries chests loot tables
        UniformLootNumberProvider lootTableRange = UniformLootNumberProvider.create(1, 1);
        LootCondition chanceLootCondition = RandomChanceLootCondition.builder(60).build();
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, supplier, setter) -> {
            if (BASTION_TREASURE_CHEST_LOOT_TABLE_ID.equals(id)) {
                LootPool lootPool = LootPool.builder()
                        .rolls(lootTableRange)
                        .conditionally(chanceLootCondition)
                        .with(ItemEntry.builder(ImpaledItems.ANCIENT_TRIDENT).build()).build();

                supplier.pool(lootPool);
            }
        });
    }
}

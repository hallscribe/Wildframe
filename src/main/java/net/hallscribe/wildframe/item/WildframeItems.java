package net.hallscribe.wildframe.item;

import net.hallscribe.wildframe.Wildframe;
import net.hallscribe.wildframe.entity.WildframeEntities;
import net.hallscribe.wildframe.item.custom.WildframeArmorMaterials;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.equipment.ArmorType;
import java.util.function.Function;

public class WildframeItems {

        public static final Item FUR = registerItem("fur", Item::new);

        public static final Item MOOHORN_SPAWN_EGG = registerItem("moohorn_spawn_egg",
                properties -> new SpawnEggItem(properties.spawnEgg(WildframeEntities.MOOHORN)));

        public static final Item FUR_HELMET = registerItem("fur_helmet",
                properties -> new Item(properties.humanoidArmor(WildframeArmorMaterials.FUR_ARMOR_MATERIAL, ArmorType.HELMET)));
        public static final Item FUR_CHESTPLATE = registerItem("fur_chestplate",
            properties -> new Item(properties.humanoidArmor(WildframeArmorMaterials.FUR_ARMOR_MATERIAL, ArmorType.CHESTPLATE)));
        public static final Item FUR_LEGGINGS = registerItem("fur_leggings",
            properties -> new Item(properties.humanoidArmor(WildframeArmorMaterials.FUR_ARMOR_MATERIAL, ArmorType.LEGGINGS)));
        public static final Item FUR_BOOTS = registerItem("fur_boots",
            properties -> new Item(properties.humanoidArmor(WildframeArmorMaterials.FUR_ARMOR_MATERIAL, ArmorType.BOOTS)));

        private static Item registerItem(String name, Function<Item.Properties, Item> function) {
            return Registry.register(BuiltInRegistries.ITEM, Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, name),
                    function.apply(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, name)))));
        }

        public static void registerWildframeItems() {
            Wildframe.LOGGER.info("Registering Wildframe Items for " + Wildframe.MOD_ID);
        }
}

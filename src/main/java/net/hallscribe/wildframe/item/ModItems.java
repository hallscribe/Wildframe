package net.hallscribe.wildframe.item;

import net.hallscribe.wildframe.Wildframe;
import net.hallscribe.wildframe.item.custom.SavannaCrawlerSpawnEggItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;

public class ModItems {

    public static SavannaCrawlerSpawnEggItem SAVANNA_CRAWLER_SPAWN_EGG;

    public static void registerModItems() {
        SAVANNA_CRAWLER_SPAWN_EGG = Registry.register(
            BuiltInRegistries.ITEM,
            Wildframe.id("savanna_crawler_spawn_egg"),
            new SavannaCrawlerSpawnEggItem()
        );
        Wildframe.LOGGER.info("Registering mod items for " + Wildframe.MOD_ID);
    }
}

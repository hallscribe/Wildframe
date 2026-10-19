package net.hallscribe.wildframe.tags;

import net.hallscribe.wildframe.Wildframe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class WildframeTags {
    public static class Items {
        public static final TagKey<Item> FUR_REPAIR = createTag("fur_repair");

        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, name));
        }
    }
}

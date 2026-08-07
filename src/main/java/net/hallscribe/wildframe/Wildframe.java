package net.hallscribe.wildframe;

import net.fabricmc.api.ModInitializer;
import net.hallscribe.wildframe.entity.ModEntities;
import net.hallscribe.wildframe.item.ModItems;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Wildframe implements ModInitializer {
	public static final String MOD_ID = "wildframe";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		try {
		    ModEntities.register();
		    ModItems.registerModItems();
		} catch (Throwable t) {
		    // Likely running in a datagen environment where registries are not available yet.
		    LOGGER.warn("Skipping runtime registration during datagen or early bootstrap: {}", t.toString());
		}
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}

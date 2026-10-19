package net.hallscribe.wildframe;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.hallscribe.wildframe.datagen.*;

public class WildframeDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();

		pack.addProvider(WildframeModelProvider::new);
		pack.addProvider(WildframeSoundsProvider::new);
		pack.addProvider(WildframeEntityTypeTagProvider::new);
		pack.addProvider(WildframeEntityLootTableProvider::new);
		pack.addProvider(WildframeRecipeProvider::new);
	}
}

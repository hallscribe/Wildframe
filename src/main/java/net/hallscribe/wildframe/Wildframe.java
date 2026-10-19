package net.hallscribe.wildframe;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.hallscribe.wildframe.creativemodtab.WildframeCreativeModeTabs;
import net.hallscribe.wildframe.entity.WildframeEntities;
import net.hallscribe.wildframe.entity.custom.Moohorn;
import net.hallscribe.wildframe.item.WildframeItems;
import net.hallscribe.wildframe.item.custom.FurFreezeProtection;
import net.hallscribe.wildframe.particle.WildframeParticles;
import net.hallscribe.wildframe.sounds.WildframeSoundEvents;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Wildframe implements ModInitializer {

	public static final String MOD_ID = "wildframe";

	public static final Logger LOGGER =
			LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		WildframeEntities.registerWildframeEntities();
		WildframeItems.registerWildframeItems();
		WildframeCreativeModeTabs.registerModCreativeModeTabs();
		WildframeSoundEvents.registerWildframeSoundEvents();
		WildframeParticles.registerWildframeParticles();

		FurFreezeProtection.register();

		FabricDefaultAttributeRegistry.register(WildframeEntities.MOOHORN, Moohorn.createMoohornAttributes());

		SpawnPlacements.register(
				WildframeEntities.MOOHORN,
				SpawnPlacementTypes.ON_GROUND,
				Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				Moohorn::checkMoohornSpawnRules
		);

	}
}
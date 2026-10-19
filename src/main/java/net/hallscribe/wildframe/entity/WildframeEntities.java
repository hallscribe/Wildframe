package net.hallscribe.wildframe.entity;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.hallscribe.wildframe.Wildframe;
import net.hallscribe.wildframe.entity.custom.Moohorn;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.phys.Vec3;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.illager.AbstractIllager;

public class WildframeEntities {
    public static final ResourceKey<EntityType<?>> MOOHORN_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "moohorn"));
    public static final ResourceKey<EntityType<?>> MOOHORN_BABY_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "moohorn_baby"));
    public static final ResourceKey<EntityType<?>> MOOHORN_SADDLE_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "moohorn_saddle"));

    public static final EntityType<Moohorn> MOOHORN = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "moohorn"),
            EntityType.Builder.of(Moohorn::new, MobCategory.CREATURE).sized(1.65F, 2.2F)
                    .passengerAttachments(new Vec3(0.0, 1.95, -0.1))
                    .build(MOOHORN_KEY));
    public static final EntityType<Moohorn> MOOHORN_BABY = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "moohorn_baby"),
            EntityType.Builder.of(Moohorn::new, MobCategory.CREATURE).sized(1.65F, 2.2F).build(MOOHORN_BABY_KEY));
    public static final EntityType<Moohorn> MOOHORN_SADDLE = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "moohorn_saddle"),
            EntityType.Builder.of(Moohorn::new, MobCategory.CREATURE).sized(1.65F, 2.2F).build(MOOHORN_SADDLE_KEY));

    public static void registerWildframeEntities() {
        Wildframe.LOGGER.info("Registering Wildframe Entities for " + Wildframe.MOD_ID);

        ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
            if (entity instanceof AbstractIllager illager) {
                illager.targetSelector.addGoal(3,
                        new NearestAttackableTargetGoal<>(illager, Moohorn.class, true));
            }
        });

        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(Biomes.SNOWY_PLAINS),
                MobCategory.CREATURE,
                WildframeEntities.MOOHORN,
                1,
                2,
                4
        );
    }
}

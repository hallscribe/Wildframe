package net.hallscribe.wildframe.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.hallscribe.wildframe.Wildframe;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class WildframeParticles {
    public static final SimpleParticleType RUN_SMOKE = register("run_smoke");

    private static SimpleParticleType register(String name) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE,
                Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, name),
                FabricParticleTypes.simple());
    }

    public static void registerWildframeParticles() {
        Wildframe.LOGGER.info("Registering Wildframe Particles for " + Wildframe.MOD_ID);
    }
}

package net.hallscribe.wildframe.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class RunSmokeParticle extends SingleQuadParticle {
    private final SpriteSet sprites;

    protected RunSmokeParticle(final ClientLevel level, final double x, final double y, final double z,
                               final double xa, final double ya, final double za,
                               final SpriteSet sprites, final RandomSource random) {
        super(level, x, y, z, sprites.get(random));
        this.sprites = sprites;
        this.lifetime = 20 + this.random.nextInt(10);
        this.quadSize *= 1.5F;
        this.gravity = 0.0F;
        this.friction = 0.96F;
        this.xd = xa;
        this.yd = ya;
        this.zd = za;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        this.alpha = 1.0F - (float) this.age / (float) this.lifetime;
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(final SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(final SimpleParticleType options, final ClientLevel level,
                                       final double x, final double y, final double z,
                                       final double xAux, final double yAux, final double zAux,
                                       final RandomSource random) {
            return new RunSmokeParticle(level, x, y, z, xAux, yAux, zAux, this.sprites, random);
        }
    }
}
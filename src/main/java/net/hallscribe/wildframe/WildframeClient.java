package net.hallscribe.wildframe;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.hallscribe.wildframe.entity.WildframeEntities;
import net.hallscribe.wildframe.entity.client.FurArmorRenderer;
import net.hallscribe.wildframe.entity.client.model.armor.fur_armor.FurBootsModel;
import net.hallscribe.wildframe.entity.client.model.armor.fur_armor.FurChestplateModel;
import net.hallscribe.wildframe.entity.client.model.armor.fur_armor.FurHelmetModel;
import net.hallscribe.wildframe.entity.client.model.armor.fur_armor.FurLeggingsModel;
import net.hallscribe.wildframe.entity.client.model.moohorn.AdultMoohornModel;
import net.hallscribe.wildframe.entity.client.model.moohorn.BabyMoohornModel;
import net.hallscribe.wildframe.entity.client.MoohornRenderer;
import net.hallscribe.wildframe.entity.client.WildframeModelLayerLocation;
import net.hallscribe.wildframe.entity.client.model.moohorn.MoohornSaddleModel;
import net.hallscribe.wildframe.item.WildframeItems;
import net.hallscribe.wildframe.particle.RunSmokeParticle;
import net.hallscribe.wildframe.particle.WildframeParticles;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry;

public class WildframeClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        ModelLayerRegistry.registerModelLayer(WildframeModelLayerLocation.MOOHORN, AdultMoohornModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(WildframeModelLayerLocation.MOOHORN_BABY, BabyMoohornModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(WildframeModelLayerLocation.MOOHORN_SADDLE, MoohornSaddleModel::createSaddleLayer);

        EntityRenderers.register(WildframeEntities.MOOHORN, MoohornRenderer::new);

        ModelLayerRegistry.registerModelLayer(WildframeModelLayerLocation.FUR_HELMET, FurHelmetModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(WildframeModelLayerLocation.FUR_CHESTPLATE, FurChestplateModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(WildframeModelLayerLocation.FUR_LEGGINGS, FurLeggingsModel::createBodyLayer);
        ModelLayerRegistry.registerModelLayer(WildframeModelLayerLocation.FUR_BOOTS, FurBootsModel::createBodyLayer);

        ArmorRenderer.register(FurArmorRenderer::new,
                WildframeItems.FUR_HELMET, WildframeItems.FUR_CHESTPLATE,
                WildframeItems.FUR_LEGGINGS, WildframeItems.FUR_BOOTS);

        ParticleProviderRegistry.getInstance().register(WildframeParticles.RUN_SMOKE, RunSmokeParticle.Provider::new);
    }
}

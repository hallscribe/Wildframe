package net.hallscribe.wildframe.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.hallscribe.wildframe.Wildframe;
import net.hallscribe.wildframe.entities.ModEntities;
import net.hallscribe.wildframe.entities.animal.chameleon.ChameleonEntity;
import net.hallscribe.wildframe.entities.animal.chameleon.ChameleonEntityRenderer;
import net.hallscribe.wildframe.entities.animal.chameleon.ChameleonModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class WildframeClient implements ClientModInitializer {
    public static final ModelLayerLocation CHAMELEON_MODEL_LAYER = new ModelLayerLocation(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "chameleon"), "main");

    @Override
    public void onInitializeClient() {
        // Register model layer and renderer
        ModelLayerRegistry.registerModelLayer(CHAMELEON_MODEL_LAYER, ChameleonModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.CHAMELEON, (ctx) -> new ChameleonEntityRenderer(ctx));
    }
}

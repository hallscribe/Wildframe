package net.hallscribe.wildframe.entities.animal.chameleon;

import net.hallscribe.wildframe.Wildframe;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class ChameleonEntityRenderer extends MobRenderer<ChameleonEntity, ChameleonModel> {
    public ChameleonEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new ChameleonModel(ctx.bakeLayer(net.hallscribe.wildframe.client.WildframeClient.CHAMELEON_MODEL_LAYER)), 0.3F);
    }

    @Override
    public Identifier getTextureLocation(ChameleonEntity entity) {
        return Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "textures/entity/animal/chameleon/chameleon.png");
    }
}

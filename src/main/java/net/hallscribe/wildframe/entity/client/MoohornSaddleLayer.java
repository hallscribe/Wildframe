package net.hallscribe.wildframe.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hallscribe.wildframe.Wildframe;
import net.hallscribe.wildframe.entity.client.model.moohorn.MoohornModel;
import net.hallscribe.wildframe.entity.client.model.moohorn.MoohornSaddleModel;
import net.hallscribe.wildframe.entity.client.state.MoohornRenderState;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.SubmitNodeCollector;

public class MoohornSaddleLayer extends RenderLayer<MoohornRenderState, MoohornModel> {
    private static final Identifier SADDLE_LOCATION = Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "textures/entity/moohorn/moohorn_saddle.png");
    private final MoohornSaddleModel saddleModel;

    public MoohornSaddleLayer(final EntityRendererProvider.Context context, final RenderLayerParent<MoohornRenderState, MoohornModel> renderer) {
        super(renderer);
        this.saddleModel = new MoohornSaddleModel(context.bakeLayer(WildframeModelLayerLocation.MOOHORN_SADDLE));
    }

    @Override
    public void submit(final PoseStack poseStack, final SubmitNodeCollector bufferSource, final int packedLight, final MoohornRenderState state, final float yRot, final float xRot) {
        if (state.saddle != null && !state.saddle.isEmpty()) {
            RenderLayer.coloredCutoutModelCopyLayerRender(this.saddleModel, SADDLE_LOCATION, poseStack, bufferSource, packedLight, state, 16777215, 0);
        }
    }
}


package net.hallscribe.wildframe.entity.client;

import net.hallscribe.wildframe.Wildframe;
import net.hallscribe.wildframe.entity.client.model.moohorn.AdultMoohornModel;
import net.hallscribe.wildframe.entity.client.model.moohorn.BabyMoohornModel;
import net.hallscribe.wildframe.entity.client.model.moohorn.MoohornModel;
import net.hallscribe.wildframe.entity.client.state.MoohornRenderState;
import net.hallscribe.wildframe.entity.custom.Moohorn;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;

public class MoohornRenderer extends AgeableMobRenderer<Moohorn, MoohornRenderState, MoohornModel> {
    private static final Identifier MOOHORN_LOCATION = Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "textures/entity/moohorn/moohorn.png");
    private static final Identifier MOOHORN_BABY_LOCATION = Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "textures/entity/moohorn/moohorn_baby.png");

    public MoohornRenderer(final EntityRendererProvider.Context context) {
        super(context, new AdultMoohornModel(context.bakeLayer(WildframeModelLayerLocation.MOOHORN)), new BabyMoohornModel(context.bakeLayer(WildframeModelLayerLocation.MOOHORN_BABY)), 0.7F);
        this.addLayer(new MoohornSaddleLayer(context, this));
    }

    @Override
    public Identifier getTextureLocation(final MoohornRenderState state) {
        return state.isBaby ? MOOHORN_BABY_LOCATION : MOOHORN_LOCATION;
    }

    @Override
    public MoohornRenderState createRenderState() {
        return new MoohornRenderState();
    }

    @Override
    public void extractRenderState(final Moohorn entity, final MoohornRenderState state, final float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.saddle = entity.getItemBySlot(EquipmentSlot.SADDLE).copy();
        state.attackAnimationState.copyFrom(entity.attackAnimationState);
        state.attackAnimationTick = entity.getAttackAnimationRemainingTicks();
        state.running = entity.isRunning();
        state.sprinting = entity.isRideSprinting();
        state.ridden = entity.isVehicle();
        state.airSeek = entity.getAirSeek(partialTicks);
        state.airSeekBob = Mth.sin((entity.tickCount + partialTicks) * 0.20F);
    }
}

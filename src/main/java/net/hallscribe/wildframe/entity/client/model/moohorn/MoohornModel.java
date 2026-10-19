package net.hallscribe.wildframe.entity.client.model.moohorn;

import net.hallscribe.wildframe.entity.client.state.MoohornRenderState;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.jspecify.annotations.Nullable;

public abstract class MoohornModel extends EntityModel<MoohornRenderState> {
    protected float runTimeMultiplier = 2.0F;

    protected final ModelPart head;
    protected final ModelPart body;
    protected final ModelPart frontLeftLeg;
    protected final ModelPart frontRightLeg;
    protected final ModelPart backLeftLeg;
    protected final ModelPart backRightLeg;

    protected final KeyframeAnimation walkingAnimation;
    protected final KeyframeAnimation runAnimation;
    @Nullable
    protected final KeyframeAnimation attackAnimation;

    protected MoohornModel(final ModelPart root, final AnimationDefinition walkingAnimation, final AnimationDefinition runAnimation,
                          @Nullable final AnimationDefinition attackAnimation) {
        super(root);
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.frontLeftLeg = root.getChild("front_left_leg");
        this.frontRightLeg = root.getChild("front_right_leg");
        this.backLeftLeg = root.getChild("back_left_leg");
        this.backRightLeg = root.getChild("back_right_leg");

        this.walkingAnimation = walkingAnimation.bake(root);
        this.runAnimation = runAnimation.bake(root);
        this.attackAnimation = attackAnimation == null ? null : attackAnimation.bake(root);
    }

    @Override
    public void setupAnim(final MoohornRenderState state) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotations(state.yRot, state.xRot);

        if (state.running) {
            this.runAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, this.runTimeMultiplier, 9.0F);
        } else if (state.sprinting) {
            this.walkingAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 5.0F, 12.5F);
        } else {
            this.walkingAnimation.applyWalk(state.walkAnimationPos, state.walkAnimationSpeed, 10.0F, 12.5F);
        }
        float lift = state.airSeek;
        if (lift > 0.0F) {
            float up = -8.5F * Mth.DEG_TO_RAD;
            float bob = state.airSeekBob * 2.0F * Mth.DEG_TO_RAD;
            this.head.xRot = Mth.lerp(lift, this.head.xRot, up + bob * lift);
        }

        if (this.attackAnimation != null && state.attackAnimationTick > 0) {
            this.attackAnimation.apply(state.attackAnimationState, state.ageInTicks, 1.5F);
        }
    }

    protected void applyHeadRotations(final float headYaw, final float headPitch) {
        final float clampedYaw = Mth.clamp(headYaw, -30.0F, 30.0F);
        final float clampedPitch = Mth.clamp(headPitch, -25.0F, 45.0F);

        this.head.yRot = clampedYaw * ((float) Math.PI / 180F);
        this.head.xRot = clampedPitch * ((float) Math.PI / 180F);
    }
}

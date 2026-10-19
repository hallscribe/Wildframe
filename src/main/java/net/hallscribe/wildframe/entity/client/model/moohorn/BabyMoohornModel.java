package net.hallscribe.wildframe.entity.client.model.moohorn;

import net.hallscribe.wildframe.entity.client.animation.BabyMoohornAnimation;
import net.hallscribe.wildframe.entity.client.state.MoohornRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class BabyMoohornModel extends MoohornModel {
    public BabyMoohornModel(final ModelPart root) {
        super(root, BabyMoohornAnimation.MOOHORN_BABY_WALK, BabyMoohornAnimation.MOOHORN_BABY_RUN, null);
        this.runTimeMultiplier = 1.5F;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 53).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(32, 53).addBox(5.0F, -6.0F, -5.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(40, 53).addBox(-7.0F, -6.0F, -5.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(32, 60).addBox(4.0F, -3.0F, -5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(38, 60).addBox(-5.0F, -3.0F, -5.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(50, 50).addBox(-4.5F, -5.0F, -8.5F, 9.0F, 6.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(54, 14).addBox(-3.0F, 4.0F, -8.0F, 6.0F, 3.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 8.0F, -7.5F));

        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 26).addBox(-7.0F, -10.0F, -11.0F, 14.0F, 15.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-7.5F, -11.0F, -11.5F, 15.0F, 14.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(50, 26).addBox(-7.0F, -9.0F, 0.0F, 14.0F, 14.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 10.0F, 3.5F));

        partdefinition.addOrReplaceChild("front_left_leg", CubeListBuilder.create().texOffs(72, 0).addBox(-2.6F, -2.0F, -2.65F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, 15.0F, -4.75F));

        partdefinition.addOrReplaceChild("front_right_leg", CubeListBuilder.create().texOffs(32, 65).addBox(-2.4F, -2.0F, -2.65F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5F, 15.0F, -4.75F));

        partdefinition.addOrReplaceChild("back_left_leg", CubeListBuilder.create().texOffs(52, 65).addBox(-2.6F, -2.0F, -2.6F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, 15.0F, 11.0F));

        partdefinition.addOrReplaceChild("back_right_leg", CubeListBuilder.create().texOffs(0, 69).addBox(-2.4F, -2.0F, -2.6F, 5.0F, 11.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5F, 15.0F, 11.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(final MoohornRenderState state) {
        super.setupAnim(state);
    }
}

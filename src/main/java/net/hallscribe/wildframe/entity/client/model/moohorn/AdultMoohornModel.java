package net.hallscribe.wildframe.entity.client.model.moohorn;

import net.hallscribe.wildframe.entity.client.animation.AdultMoohornAnimation;
import net.hallscribe.wildframe.entity.client.state.MoohornRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class AdultMoohornModel extends MoohornModel {
    public AdultMoohornModel(final ModelPart root) {
        super(root, AdultMoohornAnimation.MOOHORN_WALK, AdultMoohornAnimation.MOOHORN_RUN, AdultMoohornAnimation.MOOHORN_ATTACK);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 89).addBox(-5.0F, -6.0F, -11.0F, 10.0F, 12.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(34, 114).addBox(6.0F, -12.0F, -7.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(34, 128).addBox(-9.0F, -12.0F, -7.0F, 3.0F, 11.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(82, 39).addBox(5.0F, -4.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(90, 39).addBox(-6.0F, -4.0F, -7.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(78, 87).addBox(-5.4F, -7.0F, -11.6F, 11.0F, 8.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(0, 114).addBox(-4.0F, 6.0F, -11.0F, 8.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.0F, -13.5F));

        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(78, 46).addBox(-11.0F, -19.0F, -10.0F, 22.0F, 24.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-11.5F, -19.5F, -10.5F, 23.0F, 28.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(82, 0).addBox(-10.0F, -16.0F, 7.0F, 20.0F, 21.0F, 18.0F, new CubeDeformation(0.0F))
                .texOffs(0, 46).addBox(-10.5F, -16.5F, 7.5F, 21.0F, 25.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, -3.5F));

        partdefinition.addOrReplaceChild("front_left_leg", CubeListBuilder.create().texOffs(46, 89).addBox(-4.0F, -2.0F, -3.75F, 8.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 12.0F, -6.75F));

        partdefinition.addOrReplaceChild("front_right_leg", CubeListBuilder.create().texOffs(46, 113).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 12.0F, -6.5F));

        partdefinition.addOrReplaceChild("back_left_leg", CubeListBuilder.create().texOffs(78, 113).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 12.0F, 15.5F));

        partdefinition.addOrReplaceChild("back_right_leg", CubeListBuilder.create().texOffs(110, 113).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 14.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 12.0F, 15.5F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(final MoohornRenderState state) {
        super.setupAnim(state);
    }
}

package net.hallscribe.wildframe.entity.client.model.moohorn;

import net.hallscribe.wildframe.entity.client.animation.AdultMoohornAnimation;
import net.hallscribe.wildframe.entity.client.state.MoohornRenderState;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class MoohornSaddleModel extends MoohornModel {
    private final ModelPart reins;

    public MoohornSaddleModel(final ModelPart root) {
        super(root, AdultMoohornAnimation.MOOHORN_WALK, AdultMoohornAnimation.MOOHORN_RUN, AdultMoohornAnimation.MOOHORN_ATTACK);
        this.reins = this.head.getChild("reins");
    }

    public static LayerDefinition createSaddleLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeDeformation inflate = new CubeDeformation(0.05F);

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 3.0F, -13.5F));
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 7.0F, -3.5F));
        root.addOrReplaceChild("front_left_leg", CubeListBuilder.create(), PartPose.offset(5.0F, 12.0F, -6.75F));
        root.addOrReplaceChild("front_right_leg", CubeListBuilder.create(), PartPose.offset(-5.0F, 12.0F, -6.5F));
        root.addOrReplaceChild("back_left_leg", CubeListBuilder.create(), PartPose.offset(5.0F, 12.0F, 15.5F));
        root.addOrReplaceChild("back_right_leg", CubeListBuilder.create(), PartPose.offset(-5.0F, 12.0F, 15.5F));

        head.addOrReplaceChild("bridle", CubeListBuilder.create()
                .texOffs(78, 43).addBox(-5.0F, -6.0F, -11.0F, 10.0F, 12.0F, 13.0F, inflate)
                .texOffs(0, 83).addBox(-4.0F, 6.0F, -11.0F, 8.0F, 4.0F, 9.0F, inflate), PartPose.ZERO);

        body.addOrReplaceChild("saddle", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-11.5F, -19.5F, -10.5F, 23.0F, 25.0F, 18.0F, inflate)
                .texOffs(84, 29).addBox(-7.5F, -29.5F, 4.5F, 15.0F, 10.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(34, 83).addBox(-6.5F, -33.5F, 4.5F, 13.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(0, 43).addBox(-10.5F, -16.5F, 7.5F, 21.0F, 22.0F, 18.0F, inflate), PartPose.ZERO);

        head.addOrReplaceChild("reins", CubeListBuilder.create()
                        .texOffs(86, 73).addBox(9.0F, -42.0F, -17.5F, 0.0F, 13.0F, 7.0F, new CubeDeformation(0.0F))
                        .texOffs(-7, 97).addBox(-9.0F, -42.0F, -17.5F, 18.0F, 0.0F, 7.0F, new CubeDeformation(0.0F))
                        .texOffs(87, 5).addBox(-9.0F, -42.0F, -17.5F, 0.0F, 13.0F, 7.0F, new CubeDeformation(0.0F))
                        .texOffs(34, 90).addBox(5.5F, -31.0F, -21.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                        .texOffs(66, 83).addBox(-9.5F, -31.0F, -21.0F, 4.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 21.0F, 13.5F));

        return LayerDefinition.create(mesh, 128, 128);
    }

    @Override
    public void setupAnim(final MoohornRenderState state) {
        super.setupAnim(state);
        this.reins.visible = state.ridden;
    }
}

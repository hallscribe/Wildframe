package net.hallscribe.wildframe.entity.client.model.armor.fur_armor;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class FurLeggingsModel {
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = FurArmorSkeleton.create();
        PartDefinition root = mesh.getRoot();

        root.getChild("body").addOrReplaceChild("pelvis", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(-5.5F, -9.5F, -2.5F, 9.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 19).addBox(-5.5F, -7.5F, -2.5F, 9.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offset(1.0F, 19.5F, 0.0F));

        root.getChild("right_leg").addOrReplaceChild("right_legging", CubeListBuilder.create()
                        .texOffs(16, 7).addBox(-4.0F, -12.5F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.14F)),
                PartPose.offset(1.9F, 12.0F, 0.0F));

        root.getChild("left_leg").addOrReplaceChild("left_legging", CubeListBuilder.create()
                        .texOffs(0, 7).addBox(0.0F, -12.5F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.145F)),
                PartPose.offset(-1.9F, 12.0F, 0.0F));

        return LayerDefinition.create(mesh, 32, 32);
    }
}

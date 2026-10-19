package net.hallscribe.wildframe.entity.client.model.armor.fur_armor;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class FurBootsModel {
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = FurArmorSkeleton.create();
        PartDefinition root = mesh.getRoot();

        root.getChild("right_leg").addOrReplaceChild("right_foot", CubeListBuilder.create()
                        .texOffs(12, 7).addBox(-4.5F, -5.5F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 19).addBox(-4.5F, -3.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 14).addBox(-4.0F, -4.43F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.14F)),
                PartPose.offset(1.9F, 12.0F, 0.0F));

        root.getChild("left_leg").addOrReplaceChild("left_foot", CubeListBuilder.create()
                        .texOffs(0, 10).addBox(0.0F, -4.43F, -2.0F, 4.0F, 5.0F, 4.0F, new CubeDeformation(0.145F))
                        .texOffs(0, 0).addBox(-0.5F, -5.5F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.005F))
                        .texOffs(12, 26).addBox(-0.5F, -3.5F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.005F)),
                PartPose.offset(-1.9F, 12.0F, 0.0F));

        return LayerDefinition.create(mesh, 32, 32);
    }
}

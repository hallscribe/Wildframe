package net.hallscribe.wildframe.entity.client.model.armor.fur_armor;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class FurHelmetModel {
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = FurArmorSkeleton.create();
        PartDefinition head = mesh.getRoot().getChild("head");

        head.addOrReplaceChild("helmet", CubeListBuilder.create()
                        .texOffs(0, 18).addBox(-2.0F, -11.25F, 3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(8, 18).addBox(-8.0F, -11.25F, 3.0F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 16).addBox(-8.0F, -11.25F, 6.0F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(24, 19).addBox(-2.0F, -17.25F, 2.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 0).addBox(-8.5F, -18.75F, 2.75F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.14F))
                        .texOffs(28, 19).addBox(-8.0F, -17.25F, 2.0F, 1.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 16).addBox(-7.0F, -17.25F, 2.0F, 5.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 23).addBox(-9.0F, -18.25F, 2.2F, 9.0F, 9.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offset(4.5F, 10.75F, -6.75F));

        return LayerDefinition.create(mesh, 32, 32);
    }
}

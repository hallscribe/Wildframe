package net.hallscribe.wildframe.entity.client.model.armor.fur_armor;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class FurChestplateModel {
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = FurArmorSkeleton.create();
        PartDefinition root = mesh.getRoot();

        root.getChild("body").addOrReplaceChild("chestplate", CubeListBuilder.create()
                        .texOffs(0, 0).addBox(1.0F, -13.0F, -1.0F, 8.0F, 13.0F, 4.0F, new CubeDeformation(0.15F))
                        .texOffs(24, 14).addBox(1.5F, -12.5F, -1.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.005F))
                        .texOffs(28, 17).addBox(0.5F, -13.25F, -1.25F, 9.0F, 3.0F, 0.0F, new CubeDeformation(0.005F))
                        .texOffs(16, 34).addBox(7.5F, -13.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.005F))
                        .texOffs(0, 36).addBox(1.5F, -13.5F, 1.5F, 7.0F, 1.0F, 1.0F, new CubeDeformation(0.005F))
                        .texOffs(29, 22).addBox(0.5F, -13.25F, -1.25F, 9.0F, 0.0F, 4.0F, new CubeDeformation(0.005F))
                        .texOffs(24, 34).addBox(1.5F, -13.5F, -1.5F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.005F)),
                PartPose.offset(-5.0F, 12.25F, -1.0F));

        root.getChild("right_arm").addOrReplaceChild("fur_right_sleeve", CubeListBuilder.create()
                        .texOffs(0, 17).addBox(-8.0F, -24.75F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.14F))
                        .texOffs(24, 0).addBox(-8.5F, -16.6F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                        .texOffs(32, 32).addBox(-8.5F, -14.6F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)),
                PartPose.offset(5.0F, 22.0F, 0.0F));

        root.getChild("left_arm").addOrReplaceChild("fur_left_sleeve", CubeListBuilder.create()
                        .texOffs(24, 7).addBox(3.5F, -16.6F, -2.5F, 5.0F, 2.0F, 5.0F, new CubeDeformation(0.0F))
                        .texOffs(32, 26).addBox(3.5F, -14.6F, -2.5F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 17).addBox(4.0F, -24.75F, -2.0F, 4.0F, 10.0F, 4.0F, new CubeDeformation(0.14F)),
                PartPose.offset(-5.0F, 22.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }
}

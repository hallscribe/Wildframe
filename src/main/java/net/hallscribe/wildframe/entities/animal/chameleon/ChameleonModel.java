package net.hallscribe.wildframe.entities.animal.chameleon;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

public class ChameleonModel extends EntityModel<ChameleonEntity> {
    private final ModelPart entity;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart mouth;
    private final ModelPart casque;
    private final ModelPart tail;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart leftHindLeg;
    private final ModelPart rightHindLeg;

    public ChameleonModel(ModelPart root) {
        super(root);
        this.entity = root.getChild("Entity");
        this.body = this.entity.getChild("Body");
        this.head = this.body.getChild("Head");
        this.mouth = this.head.getChild("Mouth");
        this.casque = this.head.getChild("Casque");
        this.tail = this.body.getChild("Tail");
        this.leftFrontLeg = this.body.getChild("L-Front-Leg");
        this.rightFrontLeg = this.body.getChild("R-Front-Leg");
        this.leftHindLeg = this.body.getChild("L-Hind-Leg");
        this.rightHindLeg = this.body.getChild("R-Hind-Leg");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        PartDefinition entity = root.addOrReplaceChild("Entity",
            CubeListBuilder.create(),
            PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body = entity.addOrReplaceChild("Body",
            CubeListBuilder.create()
                .texOffs(0, 0).addBox(-3.0F, -2.0F, -5.0F, 6.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 29).addBox(0.1F, -4.0F, -5.0F, 0.0F, 2.0F, 8.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -3.0F, 0.0F));

        PartDefinition head = body.addOrReplaceChild("Head",
            CubeListBuilder.create()
                .texOffs(20, 15).addBox(-2.0F, -2.0F, -5.0F, 4.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 39).addBox(2.0F, -2.0F, -4.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(8, 39).addBox(-3.0F, -2.0F, -4.0F, 1.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -2.0F, -5.0F));

        PartDefinition mouth = head.addOrReplaceChild("Mouth",
            CubeListBuilder.create()
                .texOffs(37, 38).addBox(-2.0F, 0.0F, -5.0F, 4.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 1.0F, 0.0F));

        PartDefinition casque = head.addOrReplaceChild("Casque",
            CubeListBuilder.create(),
            PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition casque_r1 = casque.addOrReplaceChild("Casque_r1",
            CubeListBuilder.create()
                .texOffs(20, 25).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 5.0F, 6.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("Tail",
            CubeListBuilder.create()
                .texOffs(0, 15).addBox(-1.5F, -5.0F, -1.0F, 3.0F, 7.0F, 7.0F, new CubeDeformation(0.0F)),
            PartPose.offset(0.0F, -1.0F, 4.0F));

        PartDefinition leftFrontLeg = body.addOrReplaceChild("L-Front-Leg",
            CubeListBuilder.create()
                .texOffs(16, 36).addBox(-1.0F, 0.0F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(29, 37).addBox(-2.0F, 3.0F, -3.9F, 4.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)),
            PartPose.offset(3.0F, 0.0F, -4.0F));

        PartDefinition rightFrontLeg = body.addOrReplaceChild("R-Front-Leg",
            CubeListBuilder.create()
                .texOffs(36, 25).addBox(-2.0F, 0.0F, -2.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(38, 14).addBox(-2.0F, 3.0F, -3.9F, 4.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)),
            PartPose.offset(-3.0F, 0.0F, -4.0F));

        PartDefinition leftHindLeg = body.addOrReplaceChild("L-Hind-Leg",
            CubeListBuilder.create()
                .texOffs(32, 0).addBox(-1.0F, 0.0F, -1.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(36, 31).addBox(1.0F, 4.0F, -2.0F, 3.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)),
            PartPose.offset(3.0F, -1.0F, 4.0F));

        PartDefinition rightHindLeg = body.addOrReplaceChild("R-Hind-Leg",
            CubeListBuilder.create()
                .texOffs(32, 7).addBox(-2.0F, 0.0F, -1.0F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(38, 18).addBox(-4.0F, 4.0F, -2.0F, 3.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)),
            PartPose.offset(-3.0F, -1.0F, 4.0F));

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void setupAnim(ChameleonEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.entity.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}

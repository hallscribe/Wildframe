package net.hallscribe.wildframe.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.hallscribe.wildframe.Wildframe;
import net.hallscribe.wildframe.entity.client.model.armor.fur_armor.FurArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class FurArmorRenderer implements ArmorRenderer {
    private final ArmorModelSet<FurArmorModel> models;
    private final ArmorModelSet<Identifier> textures = new ArmorModelSet<>(
            tex("fur_helmet"), tex("fur_chestplate"), tex("fur_leggings"), tex("fur_boots"));

    private static Identifier tex(String name) {
        return Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "textures/entity/armor/" + name + ".png");
    }

    public FurArmorRenderer(EntityRendererProvider.Context context) {
        this.models = new ArmorModelSet<>(
                new FurArmorModel(context.bakeLayer(WildframeModelLayerLocation.FUR_HELMET)),
                new FurArmorModel(context.bakeLayer(WildframeModelLayerLocation.FUR_CHESTPLATE)),
                new FurArmorModel(context.bakeLayer(WildframeModelLayerLocation.FUR_LEGGINGS)),
                new FurArmorModel(context.bakeLayer(WildframeModelLayerLocation.FUR_BOOTS)));
    }

    @Override
    public void render(PoseStack poseStack, SubmitNodeCollector collector, ItemStack stack,
                       HumanoidRenderState state, EquipmentSlot slot, int light,
                       HumanoidModel<HumanoidRenderState> contextModel) {
        FurArmorModel model = models.get(slot);

        ArmorRenderer.submitTransformCopyingModel(
                contextModel, state, model, state, false,
                collector.order(1), poseStack,
                RenderTypes.armorCutoutNoCull(textures.get(slot)),
                light, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);

        if (stack.hasFoil()) {
            ArmorRenderer.submitTransformCopyingModel(
                    contextModel, state, model, state, false,
                    collector.order(2), poseStack,
                    RenderTypes.armorEntityGlint(),
                    light, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
        }
    }
}

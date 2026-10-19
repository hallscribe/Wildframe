package net.hallscribe.wildframe.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.hallscribe.wildframe.entity.WildframeEntities;
import net.hallscribe.wildframe.item.WildframeItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.ModelTemplates;

public class WildframeModelProvider extends FabricModelProvider {
    public WildframeModelProvider(FabricPackOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockModelGenerators) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerators) {
        itemModelGenerators.generateFlatItem(WildframeItems.FUR, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(WildframeItems.MOOHORN_SPAWN_EGG, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(WildframeItems.FUR_HELMET, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(WildframeItems.FUR_CHESTPLATE, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(WildframeItems.FUR_LEGGINGS, ModelTemplates.FLAT_ITEM);
        itemModelGenerators.generateFlatItem(WildframeItems.FUR_BOOTS, ModelTemplates.FLAT_ITEM);
    }


}

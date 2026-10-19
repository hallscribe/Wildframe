package net.hallscribe.wildframe.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.hallscribe.wildframe.entity.WildframeEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class WildframeEntityTypeTagProvider extends FabricTagsProvider.EntityTypeTagsProvider {
    public WildframeEntityTypeTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.builder(EntityTypeTags.CAN_EQUIP_SADDLE).add(WildframeEntities.MOOHORN_KEY);
    }
}


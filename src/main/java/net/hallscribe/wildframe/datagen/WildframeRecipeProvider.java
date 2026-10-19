package net.hallscribe.wildframe.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.hallscribe.wildframe.Wildframe;
import net.hallscribe.wildframe.item.WildframeItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;

public class WildframeRecipeProvider extends FabricRecipeProvider {
    public WildframeRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RecipeProvider(registries, output) {
            @Override
            public void buildRecipes() {

            shaped(RecipeCategory.MISC, WildframeItems.FUR_HELMET, 1)
                    .pattern("FFF")
                    .pattern("F F")
                    .define('F', WildframeItems.FUR)
                    .group("Fur Helmet")
                    .unlockedBy(getHasName(WildframeItems.FUR), has(WildframeItems.FUR))
                    .save(output, "fur_helmet_from_fur");

                shaped(RecipeCategory.MISC, WildframeItems.FUR_CHESTPLATE, 1)
                        .pattern("F F")
                        .pattern("FFF")
                        .pattern("FFF")
                        .define('F', WildframeItems.FUR)
                        .group("Fur Chestplate")
                        .unlockedBy(getHasName(WildframeItems.FUR), has(WildframeItems.FUR))
                        .save(output, "fur_chestplate_from_fur");

                shaped(RecipeCategory.MISC, WildframeItems.FUR_LEGGINGS, 1)
                        .pattern("FFF")
                        .pattern("F F")
                        .pattern("F F")
                        .define('F', WildframeItems.FUR)
                        .group("Fur Leggings")
                        .unlockedBy(getHasName(WildframeItems.FUR), has(WildframeItems.FUR))
                        .save(output, "fur_leggings_from_fur");

                shaped(RecipeCategory.MISC, WildframeItems.FUR_BOOTS, 1)
                        .pattern("F F")
                        .pattern("F F")
                        .define('F', WildframeItems.FUR)
                        .group("Fur Boots")
                        .unlockedBy(getHasName(WildframeItems.FUR), has(WildframeItems.FUR))
                        .save(output, "fur_boots_from_fur");
            }
        };
    }

    @Override
    public String getName() {
        return "Wildframe Recipes";
    }
}

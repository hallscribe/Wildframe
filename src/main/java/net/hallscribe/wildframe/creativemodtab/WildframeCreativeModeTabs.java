package net.hallscribe.wildframe.creativemodtab;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.hallscribe.wildframe.Wildframe;
import net.hallscribe.wildframe.item.WildframeItems;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class WildframeCreativeModeTabs {
    public static final CreativeModeTab WILDFRAME_TAB = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
            Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "wildframe"),
            FabricCreativeModeTab.builder()
                    .icon(() -> new ItemStack(WildframeItems.MOOHORN_SPAWN_EGG))
                    .title(Component.translatable("creativemodetab.wildframe.wildframe"))
                    .displayItems((parameters, output) -> {
                        output.accept(WildframeItems.MOOHORN_SPAWN_EGG);
                        output.accept(WildframeItems.FUR);
                        output.accept(WildframeItems.FUR_HELMET);
                        output.accept(WildframeItems.FUR_CHESTPLATE);
                        output.accept(WildframeItems.FUR_LEGGINGS);
                        output.accept(WildframeItems.FUR_BOOTS);
                    })

                    .build());

    public static void registerModCreativeModeTabs() {
        Wildframe.LOGGER.info("Registering Creative Mode Tabs for " + Wildframe.MOD_ID);
    }
}

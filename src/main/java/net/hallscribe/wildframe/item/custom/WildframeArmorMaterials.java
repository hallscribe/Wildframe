package net.hallscribe.wildframe.item.custom;

import net.hallscribe.wildframe.Wildframe;
import net.hallscribe.wildframe.tags.WildframeTags;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.EquipmentAsset;

public class WildframeArmorMaterials {
    public static final ResourceKey<? extends Registry<EquipmentAsset>> REGISTRY_KEY =
            ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));
    public static final ResourceKey<EquipmentAsset> FUR_KEY =
            ResourceKey.create(REGISTRY_KEY, Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "fur"));

    public static final ArmorMaterial FUR_ARMOR_MATERIAL = new ArmorMaterial(
            15,
            ArmorMaterials.makeDefense(1, 3, 3, 2, 6),
            15,
            SoundEvents.ARMOR_EQUIP_LEATHER,
            0,
            0.2F,
            WildframeTags.Items.FUR_REPAIR,
            FUR_KEY);
}

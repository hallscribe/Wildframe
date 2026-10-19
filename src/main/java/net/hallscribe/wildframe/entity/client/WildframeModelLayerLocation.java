package net.hallscribe.wildframe.entity.client;

import net.hallscribe.wildframe.Wildframe;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;

public class WildframeModelLayerLocation {
    public static final ModelLayerLocation MOOHORN =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "moohorn"), "main");
    public static final ModelLayerLocation MOOHORN_BABY =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "moohorn_baby"), "main");
    public static final ModelLayerLocation MOOHORN_SADDLE =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "moohorn_saddle"), "main");

    public static final ModelLayerLocation FUR_HELMET =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "fur_helmet"), "main");
    public static final ModelLayerLocation FUR_CHESTPLATE =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "fur_chestplate"), "main");
    public static final ModelLayerLocation FUR_LEGGINGS =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "fur_leggings"), "main");
    public static final ModelLayerLocation FUR_BOOTS =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "fur_boots"), "main");

}

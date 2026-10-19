package net.hallscribe.wildframe.mixin;

import net.hallscribe.wildframe.item.WildframeItems;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvatarRendererMixin {

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
            at = @At("TAIL")
    )
    private void wildframe$hideOverlayUnderFur(Avatar entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        if (state.headEquipment.is(WildframeItems.FUR_HELMET)) {
            state.showHat = false;
        }

        if (state.chestEquipment.is(WildframeItems.FUR_CHESTPLATE)) {
            state.showJacket = false;
            state.showLeftSleeve = false;
            state.showRightSleeve = false;
        }

        if (state.legsEquipment.is(WildframeItems.FUR_LEGGINGS)) {
            state.showLeftPants = false;
            state.showRightPants = false;
        }
    }
}

package net.hallscribe.wildframe.entity.client.state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.item.ItemStack;

public class MoohornRenderState extends LivingEntityRenderState {
    public final AnimationState attackAnimationState = new AnimationState();
    public int attackAnimationTick = 0;
    public boolean running;
    public boolean sprinting;
    public boolean ridden;
    public ItemStack saddle = ItemStack.EMPTY;
    public float airSeek;
    public float airSeekBob;
}

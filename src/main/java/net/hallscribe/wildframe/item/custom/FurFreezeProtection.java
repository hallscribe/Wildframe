package net.hallscribe.wildframe.item.custom;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.hallscribe.wildframe.item.WildframeItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class FurFreezeProtection {
    private FurFreezeProtection() {}

    public static int get(LivingEntity e) {
        int p = 0;
        if (e.getItemBySlot(EquipmentSlot.HEAD).is(WildframeItems.FUR_HELMET))      p += 15;
        if (e.getItemBySlot(EquipmentSlot.CHEST).is(WildframeItems.FUR_CHESTPLATE)) p += 35;
        if (e.getItemBySlot(EquipmentSlot.LEGS).is(WildframeItems.FUR_LEGGINGS))    p += 25;
        if (e.getItemBySlot(EquipmentSlot.FEET).is(WildframeItems.FUR_BOOTS))       p += 25;
        return p;
    }

    public static int countPieces(LivingEntity e){ int c=0;
        if(e.getItemBySlot(EquipmentSlot.HEAD).is(WildframeItems.FUR_HELMET)) c++;
        if(e.getItemBySlot(EquipmentSlot.CHEST).is(WildframeItems.FUR_CHESTPLATE)) c++;
        if(e.getItemBySlot(EquipmentSlot.LEGS).is(WildframeItems.FUR_LEGGINGS)) c++;
        if(e.getItemBySlot(EquipmentSlot.FEET).is(WildframeItems.FUR_BOOTS)) c++;
        return c;
    }
    public static boolean isFullSet(LivingEntity e){ return countPieces(e)>=2; }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                if (!player.isInPowderSnow) continue;

                int protection = get(player);
                if (protection <= 0) continue;

                int pieces = countPieces(player);

                int damageChance = Math.max(1, 4 - pieces);
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                        ItemStack item = player.getItemBySlot(slot);
                        if (item.is(WildframeItems.FUR_HELMET) || item.is(WildframeItems.FUR_CHESTPLATE) ||
                            item.is(WildframeItems.FUR_LEGGINGS) || item.is(WildframeItems.FUR_BOOTS)) {
                            if (player.getRandom().nextInt(100) < damageChance) {
                                item.hurtAndBreak(1, player, slot);
                            }
                        }
                    }
                }

                if (isFullSet(player)) {
                    if (player.getTicksFrozen() > 0) {
                        player.setTicksFrozen(Math.max(0, player.getTicksFrozen() - 5));
                    }
                    continue;
                }

                int maxFrozenTicks = 140 - (protection * 140 / 100);
                if (maxFrozenTicks < 0) maxFrozenTicks = 0;

                if (player.getTicksFrozen() > maxFrozenTicks) {
                    int excess = player.getTicksFrozen() - maxFrozenTicks;
                    int reduction = Math.min(excess / 2 + 1, 5);
                    player.setTicksFrozen(Math.max(maxFrozenTicks, player.getTicksFrozen() - reduction));
                }
            }
        });

        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (source.typeHolder().is(DamageTypes.FREEZE) && isFullSet(entity)) {
                return false;
            }
            return true;
        });
    }
}

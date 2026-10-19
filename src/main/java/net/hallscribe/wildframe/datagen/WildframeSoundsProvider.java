package net.hallscribe.wildframe.datagen;

import net.fabricmc.fabric.api.client.datagen.v1.builder.SoundTypeBuilder;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricSoundsProvider;
import net.hallscribe.wildframe.Wildframe;
import net.hallscribe.wildframe.sounds.WildframeSoundEvents;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.concurrent.CompletableFuture;

public class WildframeSoundsProvider extends FabricSoundsProvider {
    public WildframeSoundsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registryLookup, SoundExporter exporter) {
        exporter.add(WildframeSoundEvents.MOOHORN_AMBIENT, SoundTypeBuilder.of(WildframeSoundEvents.MOOHORN_AMBIENT).subtitle("sounds.entity.moohorn.wildframe.ambient")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/ambient1")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/ambient2")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/ambient3")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/ambient4")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/ambient5")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/ambient6")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/ambient7"))));
        exporter.add(WildframeSoundEvents.MOOHORN_HURT, SoundTypeBuilder.of(WildframeSoundEvents.MOOHORN_HURT).subtitle("sounds.entity.moohorn.wildframe.hurt")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/hurt1")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/hurt2")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/hurt3"))));
        exporter.add(WildframeSoundEvents.MOOHORN_DEATH, SoundTypeBuilder.of(WildframeSoundEvents.MOOHORN_DEATH).subtitle("sounds.entity.moohorn.wildframe.death")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/death"))));
        exporter.add(WildframeSoundEvents.MOOHORN_ATTACK, SoundTypeBuilder.of(WildframeSoundEvents.MOOHORN_ATTACK).subtitle("sounds.entity.moohorn.wildframe.attack")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/attack"))));
        exporter.add(WildframeSoundEvents.MOOHORN_EAT, SoundTypeBuilder.of(WildframeSoundEvents.MOOHORN_EAT).subtitle("sounds.entity.moohorn.wildframe.eat")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/eat1")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/eat2")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/eat3")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/eat4")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/eat5"))));
        exporter.add(WildframeSoundEvents.MOOHORN_ANGRY, SoundTypeBuilder.of(WildframeSoundEvents.MOOHORN_ANGRY).subtitle("sounds.entity.moohorn.wildframe.angry")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/angry1")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/angry2")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/angry3"))));
        exporter.add(WildframeSoundEvents.MOOHORN_BREATHE, SoundTypeBuilder.of(WildframeSoundEvents.MOOHORN_BREATHE).subtitle("sounds.entity.moohorn.wildframe.breathe")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/breathe1")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/breathe2")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/breathe3")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/breathe4")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/breathe5")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/breathe6")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/breathe7"))));
        exporter.add(WildframeSoundEvents.MOOHORN_SOFT, SoundTypeBuilder.of(WildframeSoundEvents.MOOHORN_SOFT).subtitle("sounds.entity.moohorn.wildframe.soft")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/soft1")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/soft2")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/soft3")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/soft4")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/soft5")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/soft6"))));
        exporter.add(WildframeSoundEvents.MOOHORN_WOOD, SoundTypeBuilder.of(WildframeSoundEvents.MOOHORN_WOOD).subtitle("sounds.entity.moohorn.wildframe.wood")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/wood1")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/wood2")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/wood3")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/wood4")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/wood5")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/wood6"))));
        exporter.add(WildframeSoundEvents.MOOHORN_BABY_AMBIENT, SoundTypeBuilder.of(WildframeSoundEvents.MOOHORN_BABY_AMBIENT).subtitle("sounds.entity.moohorn.wildframe.ambient")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/baby_moohorn/ambient1")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/baby_moohorn/ambient2")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/baby_moohorn/ambient3")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/baby_moohorn/ambient4")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/baby_moohorn/ambient5"))));
        exporter.add(WildframeSoundEvents.MOOHORN_BABY_HURT, SoundTypeBuilder.of(WildframeSoundEvents.MOOHORN_BABY_HURT).subtitle("sounds.entity.moohorn.wildframe.hurt")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/baby_moohorn/hurt1")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/baby_moohorn/hurt2")))
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/baby_moohorn/hurt3"))));
        exporter.add(WildframeSoundEvents.MOOHORN_BABY_DEATH, SoundTypeBuilder.of(WildframeSoundEvents.MOOHORN_BABY_DEATH).subtitle("sounds.entity.moohorn.wildframe.death")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/baby_moohorn/death"))));
        exporter.add(WildframeSoundEvents.MOOHORN_BABY_ANGRY, SoundTypeBuilder.of(WildframeSoundEvents.MOOHORN_BABY_ANGRY).subtitle("sounds.entity.moohorn.wildframe.angry")
                .sound(SoundTypeBuilder.RegistrationBuilder.ofFile(Identifier.fromNamespaceAndPath(Wildframe.MOD_ID, "entity/moohorn/baby_moohorn/angry"))));

    }

    @Override
    public String getName() {
        return "Wildframe Sounds";
    }
}

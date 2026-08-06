package net.hallscribe.wildframe.entities.animal.chameleon;

import net.hallscribe.wildframe.entities.ModEntities;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.SpawnGroupData;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class ChameleonEntity extends Animal {
    public ChameleonEntity(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevelAccessor serverLevelAccessor, AgeableMob ageableMob) {
        return null;
    }

    public static boolean checkChameleonSpawnRules(EntityType<? extends Animal> type, net.minecraft.world.level.LevelAccessor level, net.minecraft.world.entity.MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).isSolid();
    }
}

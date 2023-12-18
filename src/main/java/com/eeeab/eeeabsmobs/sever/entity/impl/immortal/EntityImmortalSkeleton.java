package com.eeeab.eeeabsmobs.sever.entity.impl.immortal;

import com.eeeab.eeeabsmobs.sever.config.EEConfigHandler;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.init.ItemInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class EntityImmortalSkeleton extends AbstractImmortalSkeleton implements IEntity {
    public EntityImmortalSkeleton(EntityType<? extends EntityImmortalSkeleton> type, Level level) {
        super(type, level);
        this.xpReward = 5;
        this.dropAfterDeathAnim = true;
        this.active = true;
    }

    @Override
    protected EEConfigHandler.AttributeConfig getAttributeConfig() {
        return EEConfigHandler.COMMON.MOB.IMMORTAL.SKELETON.combatConfig;
    }


    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        super.registerGoals();
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyIn) {
        if (randomSource.nextInt(10) <= difficultyIn.getDifficulty().getId()) {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        } else {
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemInit.IMMORTAL_AXE.get()));
        }
    }

    @Nullable
    @Override
    //在初始生成时调用
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        RandomSource randomsource = worldIn.getRandom();
        this.populateDefaultEquipmentSlots(randomsource, difficultyIn);
        this.populateDefaultEquipmentEnchantments(randomsource, difficultyIn);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.88f;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).
                add(Attributes.MOVEMENT_SPEED, 0.3D).
                add(Attributes.FOLLOW_RANGE, 16.0D).
                add(Attributes.ATTACK_DAMAGE, 3.0D).
                add(Attributes.ATTACK_KNOCKBACK, 0.1D).
                add(Attributes.KNOCKBACK_RESISTANCE, 0.1D);
    }

}

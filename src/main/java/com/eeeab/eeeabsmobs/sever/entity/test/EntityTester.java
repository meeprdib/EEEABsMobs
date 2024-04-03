package com.eeeab.eeeabsmobs.sever.entity.test;

import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.eeeabsmobs.sever.entity.IEntity;
import com.eeeab.eeeabsmobs.sever.entity.XpReward;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.EMLookAtGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationActivateGoal;
import com.eeeab.eeeabsmobs.sever.entity.ai.goal.animation.AnimationDeactivateGoal;
import com.eeeab.eeeabsmobs.sever.util.EMTUtils;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

//伤害测试单位 仅用于测试
public class EntityTester extends EEEABMobLibrary implements IEntity {
    public static final Animation YES = Animation.create(5);
    public static final Animation NO = Animation.create(5);
    public static final Animation[] ANIMATIONS = {YES, NO};
    private static final EntityDataAccessor<Boolean> ACTIVE = SynchedEntityData.defineId(EntityTester.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<Component>> DAMAGE = SynchedEntityData.defineId(EntityTester.class, EntityDataSerializers.OPTIONAL_COMPONENT);

    public EntityTester(EntityType<? extends EEEABMobLibrary> type, Level level) {
        super(type, level);
        active = true;
    }

    @Override
    protected XpReward getEntityReward() {
        return XpReward.XP_REWARD_NONE;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(7, new EMLookAtGoal(this, Player.class, 6.0F, true));
        goalSelector.addGoal(8, new EMLookAtGoal(this, Mob.class, 8.0F));
    }

    @Override
    protected void registerCustomGoals() {
        goalSelector.addGoal(0, new AnimationActivateGoal<>(this, YES) {
            @Override
            public void start() {
                super.start();
                this.entity.playSound(SoundEvents.VILLAGER_YES);
            }
        });
        goalSelector.addGoal(0, new AnimationDeactivateGoal<>(this, NO) {
            @Override
            public void start() {
                super.start();
                this.entity.playSound(SoundEvents.VILLAGER_NO);
            }
        });
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes().
                add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FLYING_SPEED, 0.0D);
    }

    @Override
    public void tick() {
        if (!this.isActive()) this.setDeltaMovement(0, isOnGround() ? 0 : getDeltaMovement().y, 0);
        super.tick();
        AnimationHandler.INSTANCE.updateAnimations(this);
    }

    @Override
    public Animation getDeathAnimation() {
        return null;
    }

    @Override
    public Animation getHurtAnimation() {
        return null;
    }

    @Override
    public boolean hurt(DamageSource source, float damage) {
        if (this.level.isClientSide) {
            return false;
        } else if (!(source == DamageSource.OUT_OF_WORLD || source == DamageSource.GENERIC)) {
            this.setDamage(damage);
            damage = 0;
        }
        return super.hurt(source, damage);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_AMBIENT;
    }

    @Override
    public void playAmbientSound() {
        if (isActive()) super.playAmbientSound();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.VILLAGER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    @Override
    public boolean shouldShowName() {
        return true;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            this.setDamage(0.0);
            this.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP);
            player.displayClientMessage(Component.keybind("reset success").setStyle(EMTUtils.STYLE_GREEN), true);
            return InteractionResult.SUCCESS;
        }
        if (getAnimation() == NO_ANIMATION) {
            setActive(!isActive());
            this.playAnimation(isActive() ? NO : YES);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public Animation[] getAnimations() {
        return ANIMATIONS;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("isActive", isActive());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setActive(compound.getBoolean("isActive"));
        active = isActive();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ACTIVE, true);
        this.entityData.define(DAMAGE, Optional.of(Component.keybind("0.0")));
    }

    public void setActive(boolean isActive) {
        this.entityData.set(ACTIVE, isActive);
    }

    public boolean isActive() {
        return this.entityData.get(ACTIVE);
    }

    public Component getDamage() {
        return this.entityData.get(DAMAGE).orElse(Component.keybind("0.0"));
    }

    public void setDamage(double damage) {
        this.entityData.set(DAMAGE, Optional.of(Component.keybind(String.format("%.1f", damage))));
    }
}

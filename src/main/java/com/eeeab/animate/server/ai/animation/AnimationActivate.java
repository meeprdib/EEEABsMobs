package com.eeeab.animate.server.ai.animation;

import com.eeeab.eeeabsmobs.sever.entity.EEEABMobLibrary;
import com.eeeab.animate.server.ai.AnimationSimpleAI;
import com.eeeab.animate.server.animation.EMAnimatedEntity;
import com.eeeab.animate.server.animation.Animation;

import java.util.function.Supplier;

public class AnimationActivate<T extends EEEABMobLibrary & EMAnimatedEntity> extends AnimationSimpleAI<T> {
    public AnimationActivate(T entity, Supplier<Animation> animationSupplier) {
        super(entity, animationSupplier);
    }

    @Override
    public void stop() {
        super.stop();
        entity.active = true;
    }
}

package com.eeeab.eeeabsmobs.sever.potion;

import net.minecraft.world.effect.MobEffectCategory;

//眩晕
public class EffectVertigo extends EMEffect {
    public EffectVertigo() {
        super(MobEffectCategory.HARMFUL, -13382297, false);
    }

    @Override
    public boolean isBeneficial() {
        return false;
    }
}

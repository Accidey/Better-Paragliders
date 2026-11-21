package com.xulai.betterparagliders.potion;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.event.entity.living.EffectParticleModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.xulai.betterparagliders.BetterParaglidersMod;

public final class PotionEffects {

    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, BetterParaglidersMod.MOD_ID);

    public static final DeferredHolder<MobEffect, HiddenEffect> STAMINA_EFFICIENCY =
            EFFECTS.register("stamina_efficiency", HiddenEffect::new);

    public static final DeferredHolder<MobEffect, HiddenEffect> SPRINT_EFFICIENCY =
            EFFECTS.register("sprint_efficiency", HiddenEffect::new);

    private PotionEffects() {
    }

    public static void hideInvisibleEffectParticles(EffectParticleModificationEvent event) {
        Holder<MobEffect> effect = event.getEffect().getEffect();
        if (effect.is(STAMINA_EFFICIENCY) || effect.is(SPRINT_EFFICIENCY)) {
            event.setVisible(false);
        }
    }
}

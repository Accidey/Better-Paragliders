package com.xulai.betterparagliders.potion;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.neoforge.event.entity.living.EffectParticleModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import com.xulai.betterparagliders.BetterParaglidersMod;

public final class PotionEffects {

    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, BetterParaglidersMod.MOD_ID);
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(Registries.POTION, BetterParaglidersMod.MOD_ID);

    public static final DeferredHolder<MobEffect, SimpleEffect> STAMINA_EFFICIENCY =
            EFFECTS.register("stamina_efficiency", () -> new SimpleEffect(MobEffectCategory.BENEFICIAL, 0x9BBF4A));

    public static final DeferredHolder<MobEffect, SimpleEffect> SPRINT_EFFICIENCY =
            EFFECTS.register("sprint_efficiency", () -> new SimpleEffect(MobEffectCategory.BENEFICIAL, 0x9BBF4A));

    public static final DeferredHolder<MobEffect, SimpleEffect> ARCHERY_POWER =
            EFFECTS.register("archery_power", () -> new SimpleEffect(MobEffectCategory.BENEFICIAL, 0xC87F32));

    public static final DeferredHolder<MobEffect, SimpleEffect> ARCHERY_EFFICIENCY =
            EFFECTS.register("archery_efficiency", () -> new SimpleEffect(MobEffectCategory.BENEFICIAL, 0x9BBF4A));

    public static final DeferredHolder<Potion, Potion> ARCHERY = POTIONS.register("archery",
            () -> new Potion(
                    new MobEffectInstance(ARCHERY_POWER, 600, 0),
                    new MobEffectInstance(ARCHERY_EFFICIENCY, 600, 0)));

    public static final DeferredHolder<Potion, Potion> LONG_ARCHERY = POTIONS.register("long_archery",
            () -> new Potion(
                    new MobEffectInstance(ARCHERY_POWER, 600, 0),
                    new MobEffectInstance(ARCHERY_EFFICIENCY, 600, 1)));

    public static final DeferredHolder<Potion, Potion> STRONG_ARCHERY = POTIONS.register("strong_archery",
            () -> new Potion(
                    new MobEffectInstance(ARCHERY_POWER, 900, 1),
                    new MobEffectInstance(ARCHERY_EFFICIENCY, 900, 2)));

    private PotionEffects() {
    }

    public static void hideInvisibleEffectParticles(EffectParticleModificationEvent event) {
        Holder<MobEffect> effect = event.getEffect().getEffect();
        if (effect.is(STAMINA_EFFICIENCY) || effect.is(SPRINT_EFFICIENCY) || effect.is(ARCHERY_EFFICIENCY)) {
            event.setVisible(false);
        }
    }
}

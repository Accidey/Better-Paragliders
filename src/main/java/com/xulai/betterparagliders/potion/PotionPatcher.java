package com.xulai.betterparagliders.potion;

import java.util.List;

import com.xulai.betterparagliders.config.BetterParaglidersConfig;
import com.xulai.betterparagliders.mixins.minecraft.PotionAccessor;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

public final class PotionPatcher {

    private static boolean patched;

    private PotionPatcher() {
    }

    public static void apply() {
        patch(Potions.STRENGTH, MobEffects.DAMAGE_BOOST, 0, PotionEffects.STAMINA_EFFICIENCY, 0, BetterParaglidersConfig.strengthDurationSeconds());
        patch(Potions.LONG_STRENGTH, MobEffects.DAMAGE_BOOST, 0, PotionEffects.STAMINA_EFFICIENCY, 1, BetterParaglidersConfig.longStrengthDurationSeconds());
        patch(Potions.STRONG_STRENGTH, MobEffects.DAMAGE_BOOST, 1, PotionEffects.STAMINA_EFFICIENCY, 2, BetterParaglidersConfig.strongStrengthDurationSeconds());
        patch(Potions.SWIFTNESS, MobEffects.MOVEMENT_SPEED, 0, PotionEffects.SPRINT_EFFICIENCY, 0, BetterParaglidersConfig.swiftnessDurationSeconds());
        patch(Potions.LONG_SWIFTNESS, MobEffects.MOVEMENT_SPEED, 0, PotionEffects.SPRINT_EFFICIENCY, 1, BetterParaglidersConfig.longSwiftnessDurationSeconds());
        patch(Potions.STRONG_SWIFTNESS, MobEffects.MOVEMENT_SPEED, 1, PotionEffects.SPRINT_EFFICIENCY, 2, BetterParaglidersConfig.strongSwiftnessDurationSeconds());
        patched = true;
    }

    public static boolean isPatched() {
        return patched;
    }

    private static void patch(Holder<Potion> potion, Holder<MobEffect> visibleEffect, int visibleAmplifier,
            Holder<MobEffect> hiddenEffect, int hiddenAmplifier, int seconds) {
        int duration = Math.max(1, seconds * 20);
        ((PotionAccessor) (Object) potion.value()).betterparagliders$setEffects(List.of(
                new MobEffectInstance(visibleEffect, duration, visibleAmplifier, false, true, true),
                new MobEffectInstance(hiddenEffect, duration, hiddenAmplifier, false, false, false)
        ));
    }
}

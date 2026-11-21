package com.xulai.betterparagliders.potion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.xulai.betterparagliders.config.BetterParaglidersConfig;
import com.xulai.betterparagliders.mixins.minecraft.PotionAccessor;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.Potions;

public final class PotionPatcher {

    private static final Map<Holder<Potion>, List<MobEffectInstance>> ORIGINALS = new HashMap<>();
    private static boolean patched;

    private PotionPatcher() {
    }

    public static void apply() {
        patchOrRestore(Potions.STRENGTH, () -> effects(MobEffects.DAMAGE_BOOST, 0, PotionEffects.STAMINA_EFFICIENCY, 0, BetterParaglidersConfig.strengthDurationSeconds()));
        patchOrRestore(Potions.LONG_STRENGTH, () -> effects(MobEffects.DAMAGE_BOOST, 0, PotionEffects.STAMINA_EFFICIENCY, 1, BetterParaglidersConfig.longStrengthDurationSeconds()));
        patchOrRestore(Potions.STRONG_STRENGTH, () -> effects(MobEffects.DAMAGE_BOOST, 1, PotionEffects.STAMINA_EFFICIENCY, 2, BetterParaglidersConfig.strongStrengthDurationSeconds()));
        patchOrRestore(Potions.SWIFTNESS, () -> effects(MobEffects.MOVEMENT_SPEED, 0, PotionEffects.SPRINT_EFFICIENCY, 0, BetterParaglidersConfig.swiftnessDurationSeconds()));
        patchOrRestore(Potions.LONG_SWIFTNESS, () -> effects(MobEffects.MOVEMENT_SPEED, 0, PotionEffects.SPRINT_EFFICIENCY, 1, BetterParaglidersConfig.longSwiftnessDurationSeconds()));
        patchOrRestore(Potions.STRONG_SWIFTNESS, () -> effects(MobEffects.MOVEMENT_SPEED, 1, PotionEffects.SPRINT_EFFICIENCY, 2, BetterParaglidersConfig.strongSwiftnessDurationSeconds()));
        patched = true;
    }

    public static boolean isPatched() {
        return patched;
    }

    private static void patchOrRestore(Holder<Potion> potion, Supplier<List<MobEffectInstance>> modifiedEffects) {
        List<MobEffectInstance> original = ORIGINALS.putIfAbsent(potion, List.copyOf(potion.value().getEffects()));
        if (!BetterParaglidersConfig.potionEffectsEnabled()) {
            if (original != null) {
                ((PotionAccessor) (Object) potion.value()).betterparagliders$setEffects(original);
            }
            return;
        }
        ((PotionAccessor) (Object) potion.value()).betterparagliders$setEffects(modifiedEffects.get());
    }

    private static List<MobEffectInstance> effects(Holder<MobEffect> visibleEffect, int visibleAmplifier,
            Holder<MobEffect> hiddenEffect, int hiddenAmplifier, int seconds) {
        int duration = Math.max(1, seconds * 20);
        return List.of(
                new MobEffectInstance(visibleEffect, duration, visibleAmplifier, false, true, true),
                new MobEffectInstance(hiddenEffect, duration, hiddenAmplifier, false, false, false));
    }
}

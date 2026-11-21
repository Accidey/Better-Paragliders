package com.xulai.betterparagliders.mixins.minecraft;

import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PotionBrewing.Builder.class)
public final class PotionBrewingBuilderMixin {

    @Inject(method = "addMix", at = @At("HEAD"), cancellable = true)
    private void betterparagliders$blockVanillaPotionMixes(Holder<Potion> input, Item reagent, Holder<Potion> result, CallbackInfo ci) {
        if (input.is(Potions.AWKWARD) && reagent == Items.BLAZE_POWDER && result.is(Potions.STRENGTH)
                || input.is(Potions.STRENGTH) && reagent == Items.REDSTONE && result.is(Potions.LONG_STRENGTH)
                || input.is(Potions.STRENGTH) && reagent == Items.GLOWSTONE_DUST && result.is(Potions.STRONG_STRENGTH)
                || input.is(Potions.AWKWARD) && reagent == Items.SUGAR && result.is(Potions.SWIFTNESS)
                || input.is(Potions.SWIFTNESS) && reagent == Items.REDSTONE && result.is(Potions.LONG_SWIFTNESS)
                || input.is(Potions.SWIFTNESS) && reagent == Items.GLOWSTONE_DUST && result.is(Potions.STRONG_SWIFTNESS)) {
            ci.cancel();
        }
    }
}

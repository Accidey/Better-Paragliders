package com.xulai.betterparagliders.potion;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

import com.xulai.betterparagliders.config.BetterParaglidersConfig;
import com.xulai.betterparagliders.potion.PotionEffects;

public final class BrewingRecipes {

    private BrewingRecipes() {
    }

    public static void onRegisterBrewingRecipes(RegisterBrewingRecipesEvent event) {
        if (!BetterParaglidersConfig.potionEffectsEnabled()) return;
        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(Potions.AWKWARD, Items.DANDELION, Potions.STRENGTH);
        builder.addMix(Potions.STRENGTH, Items.DANDELION, Potions.LONG_STRENGTH);
        builder.addMix(Potions.LONG_STRENGTH, Items.TORCHFLOWER, Potions.STRONG_STRENGTH);
        builder.addMix(Potions.AWKWARD, Items.OXEYE_DAISY, Potions.SWIFTNESS);
        builder.addMix(Potions.SWIFTNESS, Items.POPPY, Potions.LONG_SWIFTNESS);
        builder.addMix(Potions.LONG_SWIFTNESS, Items.CORNFLOWER, Potions.STRONG_SWIFTNESS);
        builder.addMix(Potions.AWKWARD, Items.POPPY, PotionEffects.ARCHERY);
        builder.addMix(PotionEffects.ARCHERY, Items.OXEYE_DAISY, PotionEffects.LONG_ARCHERY);
        builder.addMix(PotionEffects.LONG_ARCHERY, Items.PITCHER_PLANT, PotionEffects.STRONG_ARCHERY);
    }
}


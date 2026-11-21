package com.xulai.betterparagliders.mixins.minecraft;

import java.util.List;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Potion.class)
public interface PotionAccessor {

    @Mutable
    @Accessor("effects")
    void betterparagliders$setEffects(List<MobEffectInstance> effects);
}

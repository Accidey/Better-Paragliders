package com.xulai.betterparagliders.mixins.minecraft;

import com.xulai.betterparagliders.config.BetterParaglidersConfig;
import com.xulai.betterparagliders.potion.PotionEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("TAIL"))
    private void betterparagliders$applyArcheryDamage(EntityType<? extends AbstractArrow> type, LivingEntity owner,
            Level level, ItemStack pickupItemStack, ItemStack firedFromWeapon, CallbackInfo ci) {
        if ((Object) this instanceof ThrownTrident || !(owner instanceof Player player)) return;

        MobEffectInstance instance = player.getEffect(PotionEffects.ARCHERY_POWER);
        if (instance == null) return;
        double bonus = BetterParaglidersConfig.archeryDamageBonus(instance.getAmplifier());
        if (bonus <= 0) return;

        AbstractArrow self = (AbstractArrow) (Object) this;
        self.setBaseDamage(self.getBaseDamage() * (1.0D + bonus));
    }
}

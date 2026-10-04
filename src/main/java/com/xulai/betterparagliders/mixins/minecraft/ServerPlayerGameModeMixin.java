package com.xulai.betterparagliders.mixins.minecraft;

import com.xulai.betterparagliders.config.BetterParaglidersConfig;
import com.xulai.betterparagliders.utils.CalculateStaminaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {

    @Shadow
    @Final
    protected ServerPlayer player;

    @Inject(method = "incrementDestroyProgress", at = @At("HEAD"))
    private void betterparagliders$drainMiningStamina(BlockState state, BlockPos pos, int startTick,
                                                     CallbackInfoReturnable<Float> cir) {
        if (!BetterParaglidersConfig.enableMiningStamina()) return;
        if (player.isCreative() || player.isSpectator()) return;

        ServerLevel level = player.serverLevel();
        double drain = CalculateStaminaUtils.getBlockStaminaDrain(state, level, pos, player);
        if (drain <= 0.0D) return;

        if (CalculateStaminaUtils.drainMiningStamina(player, drain)) return;
        if (BetterParaglidersConfig.blockMiningWithoutStamina()) cir.setReturnValue(0.0F);
    }
}

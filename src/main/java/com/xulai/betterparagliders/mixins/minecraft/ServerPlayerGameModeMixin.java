package com.xulai.betterparagliders.mixins.minecraft;

import com.xulai.betterparagliders.capabilities.MiningDrainTracker;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin implements MiningDrainTracker {

    @Shadow
    @Final
    protected ServerPlayer player;

    @Unique
    private BlockPos betterparagliders$drainedPos;

    @Unique
    private int betterparagliders$drainedStartTick;

    @Unique
    private double betterparagliders$drainedAmount;

    @Inject(method = "incrementDestroyProgress", at = @At("HEAD"))
    private void betterparagliders$drainMiningStamina(BlockState state, BlockPos pos, int startTick,
                                                     CallbackInfoReturnable<Float> cir) {
        if (!BetterParaglidersConfig.enableMiningStamina()) return;
        if (player.isCreative() || player.isSpectator()) return;

        ServerLevel level = player.serverLevel();
        double total = CalculateStaminaUtils.getBlockStaminaCost(state, level, pos, player);
        if (total <= 0.0D) return;

        if (!betterparagliders$isSameDig(pos, startTick)) {
            this.betterparagliders$drainedStartTick = startTick;
            this.betterparagliders$drainedAmount = 0.0D;
        }
        markDrained(pos);

        double remaining = total - this.betterparagliders$drainedAmount;
        double drain = remaining <= 0.001D ? 0.0D
                : Math.min(CalculateStaminaUtils.getBlockStaminaDrain(state, level, pos, player), remaining);

        if (!CalculateStaminaUtils.drainMiningStamina(player, drain)) {
            if (BetterParaglidersConfig.blockMiningWithoutStamina()) cir.setReturnValue(0.0F);
            return;
        }
        this.betterparagliders$drainedAmount += drain;
    }

    @Unique
    private boolean betterparagliders$isSameDig(BlockPos pos, int startTick) {
        return this.betterparagliders$drainedPos != null
                && this.betterparagliders$drainedPos.equals(pos)
                && this.betterparagliders$drainedStartTick == startTick;
    }

    @Override
    public void markDrained(BlockPos pos) {
        this.betterparagliders$drainedPos = pos;
    }

    @Override
    public boolean isDrained(BlockPos pos) {
        BlockPos drained = this.betterparagliders$drainedPos;
        return drained != null && drained.equals(pos);
    }
}

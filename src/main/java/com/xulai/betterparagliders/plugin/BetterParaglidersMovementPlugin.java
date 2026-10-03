package com.xulai.betterparagliders.plugin;

import com.xulai.betterparagliders.BetterParaglidersMod;
import com.xulai.betterparagliders.mixins.accessor.LivingEntityJumpAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import tictim.paraglider.ParagliderUtils;
import tictim.paraglider.api.movement.MovementPlugin;
import tictim.paraglider.api.movement.PlayerStateCondition;
import tictim.paraglider.api.plugin.ParagliderPlugin;

@ParagliderPlugin
public class BetterParaglidersMovementPlugin implements MovementPlugin {

    public static final ResourceLocation DROWNING = ResourceLocation.fromNamespaceAndPath(BetterParaglidersMod.MOD_ID, "drowning");
    public static final ResourceLocation FLOATING = ResourceLocation.fromNamespaceAndPath(BetterParaglidersMod.MOD_ID, "floating");

    public static final double FLOATING_STAMINA_DELTA = 10.0;

    @Override
    public void registerNewStates(@NotNull PlayerStateRegister register) {
        register.register(DROWNING, 0);
        register.register(FLOATING, FLOATING_STAMINA_DELTA);
    }

    @Override
    public void registerStateConnections(@NotNull PlayerStateConnectionRegister register) {
        PlayerStateCondition inWater = c -> c.player().isInWater();
        PlayerStateCondition depleted = c -> c.stamina().isDepleted();
        PlayerStateCondition holdingJump = c -> isJumping(c.player());
        PlayerStateCondition canBreathe = c -> ParagliderUtils.canBreatheUnderwater(c.player());

        register.connect(tictim.paraglider.api.movement.ParagliderPlayerStates.IDLE, DROWNING,
                c -> inWater.test(c) && depleted.test(c), 100);
        register.connect(tictim.paraglider.api.movement.ParagliderPlayerStates.SWIMMING, DROWNING,
                c -> inWater.test(c) && depleted.test(c), 100);
        register.connect(tictim.paraglider.api.movement.ParagliderPlayerStates.UNDERWATER, DROWNING,
                c -> inWater.test(c) && depleted.test(c), 100);
        register.connect(tictim.paraglider.api.movement.ParagliderPlayerStates.BREATHING_UNDERWATER, DROWNING,
                c -> inWater.test(c) && depleted.test(c), 100);

        register.connect(tictim.paraglider.api.movement.ParagliderPlayerStates.IDLE, FLOATING,
                c -> inWater.test(c) && !depleted.test(c) && holdingJump.test(c), 90);
        register.connect(tictim.paraglider.api.movement.ParagliderPlayerStates.SWIMMING, FLOATING,
                c -> inWater.test(c) && !depleted.test(c) && holdingJump.test(c), 90);
        register.connect(tictim.paraglider.api.movement.ParagliderPlayerStates.UNDERWATER, FLOATING,
                c -> inWater.test(c) && !depleted.test(c) && holdingJump.test(c), 90);
        register.connect(tictim.paraglider.api.movement.ParagliderPlayerStates.BREATHING_UNDERWATER, FLOATING,
                c -> inWater.test(c) && !depleted.test(c) && holdingJump.test(c), 90);

        register.connect(tictim.paraglider.api.movement.ParagliderPlayerStates.IDLE,
                tictim.paraglider.api.movement.ParagliderPlayerStates.UNDERWATER,
                c -> inWater.test(c) && !depleted.test(c) && canBreathe.test(c), 6);
    }

    private static boolean isJumping(Player player) {
        return ((LivingEntityJumpAccessor) player).betterparagliders$isJumping();
    }
}
package com.xulai.betterparagliders.mixins.paragliders.stamina;

import com.xulai.betterparagliders.capabilities.StaminaOverride;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import tictim.paraglider.impl.stamina.BotWStamina;

@Mixin(BotWStamina.class)
public abstract class BotWStaminaMixin implements StaminaOverride {

    @Unique
    private int betterparagliders$totalActionStaminaCost;

    @Override
    public int getTotalActionStaminaCost() {
        return this.betterparagliders$totalActionStaminaCost;
    }

    @Override
    public void setTotalActionStaminaCost(int totalActionStaminaCost) {
        this.betterparagliders$totalActionStaminaCost = totalActionStaminaCost;
    }
}

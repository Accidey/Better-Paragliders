package com.xulai.betterparagliders.client;

import com.xulai.betterparagliders.BetterParaglidersMod;
import com.xulai.betterparagliders.network.ModNet;
import net.bettercombat.api.client.BetterCombatClientEvents;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = BetterParaglidersMod.MOD_ID, value = Dist.CLIENT)
public final class BetterParaglidersClientEvents {

    private BetterParaglidersClientEvents() {}

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        BetterCombatClientEvents.ATTACK_HIT.register((player, attackHand, targets, cursorTarget) ->
                reportSwing(attackHand.itemStack()));
    }

    @SubscribeEvent
    public static void onEmptyLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (!(event.getEntity() instanceof LocalPlayer player)) return;
        reportSwing(player.getMainHandItem());
    }

    public static void reportSwing(ItemStack stack) {
        if (stack.isEmpty()) return;
        ModNet.sendSwingAction(BuiltInRegistries.ITEM.getKey(stack.getItem()));
    }
}

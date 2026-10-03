package com.xulai.betterparagliders.attributes;

import com.xulai.betterparagliders.BetterParaglidersMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class BetterParaglidersAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(Registries.ATTRIBUTE, BetterParaglidersMod.MOD_ID);

    public static final DeferredHolder<Attribute, Attribute> SPRINTING_STAMINA_REDUCTION = register("sprinting_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> SWIMMING_STAMINA_REDUCTION = register("swimming_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> IDLE_STAMINA_REGEN = register("idle_stamina_regen");
    public static final DeferredHolder<Attribute, Attribute> SUBMERGED_STAMINA_REGEN = register("submerged_stamina_regen");
    public static final DeferredHolder<Attribute, Attribute> WATER_BREATHING_STAMINA_REGEN = register("water_breathing_stamina_regen");

    public static final DeferredHolder<Attribute, Attribute> FAST_RUNNING_STAMINA_REDUCTION = register("fast_running_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> FAST_SWIMMING_STAMINA_REDUCTION = register("fast_swimming_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> CLING_TO_CLIFF_STAMINA_REDUCTION = register("cling_to_cliff_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> HORIZONTAL_WALL_RUN_STAMINA_REDUCTION = register("horizontal_wall_run_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> DODGE_STAMINA_REDUCTION = register("dodge_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> ROLL_STAMINA_REDUCTION = register("roll_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> BREAKFALL_STAMINA_REDUCTION = register("breakfall_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> VAULT_STAMINA_REDUCTION = register("vault_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> CLIMB_UP_STAMINA_REDUCTION = register("climb_up_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> VERTICAL_WALL_RUN_STAMINA_REDUCTION = register("vertical_wall_run_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> CAT_LEAP_STAMINA_REDUCTION = register("cat_leap_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> CHARGE_JUMP_STAMINA_REDUCTION = register("charge_jump_stamina_reduction");

    public static final DeferredHolder<Attribute, Attribute> BASE_MELEE_STAMINA_REDUCTION = register("base_melee_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> ONE_HANDED_STAMINA_REDUCTION = register("one_handed_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> TWO_HANDED_STAMINA_REDUCTION = register("two_handed_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> RANGE_STAMINA_REDUCTION = register("range_stamina_reduction");
    public static final DeferredHolder<Attribute, Attribute> BLOCK_STAMINA_REDUCTION = register("block_stamina_reduction");

    private BetterParaglidersAttributes() {}

    private static DeferredHolder<Attribute, Attribute> register(String name) {
        return ATTRIBUTES.register(name, () -> new RangedAttribute(
                        "attribute.name." + BetterParaglidersMod.MOD_ID + "." + name, 0.0, 0.0, 100.0)
                .setSyncable(true));
    }

    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }
}

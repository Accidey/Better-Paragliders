package com.xulai.betterparagliders.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.xulai.betterparagliders.BetterParaglidersMod;
import com.xulai.betterparagliders.config.BetterParaglidersConfig;
import com.xulai.betterparagliders.config.BetterParaglidersConfig.ItemCategory;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@EventBusSubscriber(modid = BetterParaglidersMod.MOD_ID)
public final class BetterParaglidersCommand {

    private static final List<String> CATEGORY_NAMES = List.of("melee", "ranged", "shield");

    private static final SuggestionProvider<CommandSourceStack> CATEGORY_SUGGESTIONS =
            (context, builder) -> SharedSuggestionProvider.suggest(CATEGORY_NAMES, builder);

    private BetterParaglidersCommand() {}

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(build("betterparagliders"));
        dispatcher.register(build("bp"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> build(String name) {
        return Commands.literal(name)
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("additem")
                        .then(Commands.argument("category", StringArgumentType.word())
                                .suggests(CATEGORY_SUGGESTIONS)
                                .executes(context -> addItem(context, null))
                                .then(Commands.argument("cost", DoubleArgumentType.doubleArg(0.0, 10000.0))
                                        .executes(context -> addItem(context,
                                                DoubleArgumentType.getDouble(context, "cost"))))))
                .then(Commands.literal("removeitem")
                        .then(Commands.argument("category", StringArgumentType.word())
                                .suggests(CATEGORY_SUGGESTIONS)
                                .executes(BetterParaglidersCommand::removeItem)));
    }

    private static int addItem(CommandContext<CommandSourceStack> context, Double cost) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) {
            context.getSource().sendFailure(Component.translatable("command.betterparagliders.no_item"));
            return 0;
        }

        ItemCategory category = parseCategory(context);
        if (category == null) return 0;

        String itemId = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        double value = cost != null ? cost : BetterParaglidersConfig.defaultCostOf(category);

        Map<ItemCategory, List<String>> updated = new EnumMap<>(ItemCategory.class);
        for (ItemCategory each : ItemCategory.values()) {
            List<String> entries = BetterParaglidersConfig.rawOverrides(each);
            entries.removeIf(entry -> itemId.equals(itemIdOf(entry)));
            updated.put(each, entries);
        }
        updated.get(category).add(itemId + "=" + formatCost(value));

        BetterParaglidersConfig.applyOverrides(updated);

        context.getSource().sendSuccess(() -> Component.translatable("command.betterparagliders.added",
                itemId, categoryName(category), formatCost(value)), true);
        return 1;
    }

    private static int removeItem(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) {
            context.getSource().sendFailure(Component.translatable("command.betterparagliders.no_item"));
            return 0;
        }

        ItemCategory category = parseCategory(context);
        if (category == null) return 0;

        String itemId = BuiltInRegistries.ITEM.getKey(stack.getItem()).toString();
        boolean removed = false;

        Map<ItemCategory, List<String>> updated = new EnumMap<>(ItemCategory.class);
        for (ItemCategory each : ItemCategory.values()) {
            List<String> entries = BetterParaglidersConfig.rawOverrides(each);
            if (entries.removeIf(entry -> itemId.equals(itemIdOf(entry)))) removed = true;
            updated.put(each, entries);
        }

        if (!removed) {
            context.getSource().sendFailure(Component.translatable("command.betterparagliders.not_configured", itemId));
            return 0;
        }

        BetterParaglidersConfig.applyOverrides(updated);

        context.getSource().sendSuccess(() -> Component.translatable("command.betterparagliders.removed", itemId), true);
        return 1;
    }

    private static ItemCategory parseCategory(CommandContext<CommandSourceStack> context) {
        String raw = StringArgumentType.getString(context, "category");
        ItemCategory category = ItemCategory.byName(raw);
        if (category == null) {
            context.getSource().sendFailure(Component.translatable("command.betterparagliders.unknown_category", raw));
        }
        return category;
    }

    private static String itemIdOf(String entry) {
        int separator = entry.indexOf('=');
        return separator < 0 ? entry : entry.substring(0, separator).trim();
    }

    private static String formatCost(double cost) {
        return cost == Math.floor(cost) && Double.isFinite(cost)
                ? Long.toString((long) cost)
                : Double.toString(cost);
    }

    private static Component categoryName(ItemCategory category) {
        return Component.translatable("betterparagliders.category." + category.name().toLowerCase(Locale.ROOT));
    }
}

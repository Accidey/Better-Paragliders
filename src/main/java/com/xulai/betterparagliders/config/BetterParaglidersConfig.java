package com.xulai.betterparagliders.config;

import com.xulai.betterparagliders.BetterParaglidersMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BetterParaglidersConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue MELEE_STAMINA_COST = BUILDER
            .comment(" 近战武器每次攻击消耗的耐力，默认 200",
                    " 200 = 主模组疾跑 1 秒的消耗量（疾跑 -10/tick × 20 tick = 200/秒）",
                    " 主手物品既不是远程武器也不是盾牌时，攻击按这个数值扣耐力",
                    " 想让某个物品用别的数值，把它加到下面的 melee_item_overrides 里",
                    " 只要当前耐力够付这次消耗，攻击就一定打得出来；付不起时才按 block_attack_without_stamina 处理",
                    " Stamina consumed by each melee attack. Default 200",
                    " 200 = exactly one second of the main mod's sprint drain (sprint -10/tick x 20 ticks = 200/s)",
                    " Used when the main hand item is neither a ranged weapon nor a shield",
                    " To give a single item its own value, add it to melee_item_overrides below",
                    " An attack always goes through as long as the player can afford it; see block_attack_without_stamina for the rest")
            .defineInRange("melee_stamina_cost", 200.0, 0.0, 10000.0);

    public static final ModConfigSpec.DoubleValue RANGED_STAMINA_COST = BUILDER
            .comment("",
                    " 远程武器每次射击消耗的耐力，默认 400",
                    " 400 = 主模组疾跑 2 秒的消耗量（200/秒 × 2）",
                    " 弓、弩以及任何 ProjectileWeaponItem 都按这个数值扣耐力",
                    " 想让某个物品用别的数值，把它加到下面的 ranged_item_overrides 里",
                    " 只要当前耐力够付这次消耗，这一箭就一定射得出去；付不起时按 block_item_use_without_stamina 处理",
                    " Stamina consumed by each ranged shot. Default 400",
                    " 400 = exactly two seconds of the main mod's sprint drain (200/s x 2)",
                    " Used by bows, crossbows and every ProjectileWeaponItem",
                    " To give a single item its own value, add it to ranged_item_overrides below",
                    " A shot always goes through as long as the player can afford it; see block_item_use_without_stamina for the rest")
            .defineInRange("ranged_stamina_cost", 400.0, 0.0, 10000.0);

    public static final ModConfigSpec.DoubleValue SHIELD_STAMINA_COST = BUILDER
            .comment("",
                    " 盾牌成功格挡一次攻击消耗的耐力，默认 200",
                    " 200 = 主模组疾跑 1 秒的消耗量（200/秒）",
                    " 只有真正被判定为格挡成功的那一次才扣耐力，没挡住的伤害不扣",
                    " 想让某个物品用别的数值，把它加到下面的 shield_item_overrides 里",
                    " 只要当前耐力够付这次消耗，格挡就一定成功；付不起时格挡照常生效但不扣耐力，而下一次举盾会被中断",
                    " Stamina consumed by one successful shield block. Default 200",
                    " 200 = exactly one second of the main mod's sprint drain (200/s)",
                    " Only blocks that actually succeed consume stamina; unblocked damage costs nothing",
                    " To give a single item its own value, add it to shield_item_overrides below",
                    " A block always succeeds as long as the player can afford it; when they cannot, the block still works but costs nothing and the next shield raise gets interrupted")
            .defineInRange("shield_stamina_cost", 200.0, 0.0, 10000.0);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> MELEE_ITEM_OVERRIDES = BUILDER
            .comment("",
                    " 近战物品的单独耐力消耗表，格式为 物品ID=消耗",
                    " 这里的数值会直接覆盖 melee_stamina_cost，不是在它基础上叠加",
                    " 示例：minecraft:trident=250 表示三叉戟每次攻击消耗 250 点耐力",
                    " 游戏内可用指令直接写入主手物品：/betterparagliders additem melee [消耗]",
                    " Per-item stamina cost for melee items, format: item_id=cost",
                    " The value here overrides melee_stamina_cost instead of adding to it",
                    " Example: minecraft:trident=250 makes the trident cost 250 stamina per attack",
                    " In game you can write the held item with: /betterparagliders additem melee [cost]")
            .defineListAllowEmpty("melee_item_overrides", List.of(), () -> "minecraft:trident=250",
                    BetterParaglidersConfig::validateItemOverride);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> RANGED_ITEM_OVERRIDES = BUILDER
            .comment("",
                    " 远程物品的单独耐力消耗表，格式为 物品ID=消耗",
                    " 这里的数值会直接覆盖 ranged_stamina_cost，不是在它基础上叠加",
                    " 写进这张表的物品会被当作远程武器对待，即使它本身不是弓或弩",
                    " 示例：minecraft:crossbow=500 表示弩每次射击消耗 500 点耐力",
                    " 游戏内可用指令直接写入主手物品：/betterparagliders additem ranged [消耗]",
                    " Per-item stamina cost for ranged items, format: item_id=cost",
                    " The value here overrides ranged_stamina_cost instead of adding to it",
                    " Items listed here are treated as ranged weapons even if they are not bows or crossbows",
                    " Example: minecraft:crossbow=500 makes the crossbow cost 500 stamina per shot",
                    " In game you can write the held item with: /betterparagliders additem ranged [cost]")
            .defineListAllowEmpty("ranged_item_overrides", List.of(), () -> "minecraft:crossbow=500",
                    BetterParaglidersConfig::validateItemOverride);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> SHIELD_ITEM_OVERRIDES = BUILDER
            .comment("",
                    " 盾牌的单独耐力消耗表，格式为 物品ID=消耗",
                    " 这里的数值会直接覆盖 shield_stamina_cost，不是在它基础上叠加",
                    " 写进这张表的物品会被当作盾牌对待，即使它本身不是原版盾牌",
                    " 示例：minecraft:shield=300 表示原版盾牌每次格挡消耗 300 点耐力",
                    " 游戏内可用指令直接写入主手物品：/betterparagliders additem shield [消耗]",
                    " Per-item stamina cost for shields, format: item_id=cost",
                    " The value here overrides shield_stamina_cost instead of adding to it",
                    " Items listed here are treated as shields even if they are not the vanilla shield",
                    " Example: minecraft:shield=300 makes the vanilla shield cost 300 stamina per block",
                    " In game you can write the held item with: /betterparagliders additem shield [cost]")
            .defineListAllowEmpty("shield_item_overrides", List.of(), () -> "minecraft:shield=300",
                    BetterParaglidersConfig::validateItemOverride);

    public static final ModConfigSpec.IntValue ACTION_RECOVERY_DELAY = BUILDER
            .comment("",
                    " 完成一次动作后要等多久才开始重新恢复耐力，单位 tick（20 tick = 1 秒），默认 40",
                    " 这段等待期内耐力完全不回复，等待期结束后才按当前玩家状态的速度恢复",
                    " 设为 0 表示动作扣完耐力后立刻恢复",
                    " How long stamina regeneration stays paused after an action, in ticks (20 ticks = 1 second). Default 40",
                    " Stamina does not regenerate at all during this wait; regeneration resumes at the rate of the current player state afterwards",
                    " Set to 0 to resume regeneration immediately after an action")
            .defineInRange("action_recovery_delay", 40, 0, 200);

    public static final ModConfigSpec.BooleanValue BLOCK_ATTACK_WITHOUT_STAMINA = BUILDER
            .comment("",
                    " 当前耐力不足以支付这次近战消耗时，是否取消这次攻击",
                    " 耐力只要够付这次消耗就一定能打出来，扣完立刻进入上面的恢复等待期",
                    " 关闭后耐力不足也会照常命中，只是不扣耐力",
                    " Whether a melee attack is cancelled when the player cannot pay its stamina cost",
                    " An attack always goes through as long as the player can afford it, then the recovery wait above kicks in",
                    " Disable this to let out-of-stamina attacks land without consuming anything")
            .define("block_attack_without_stamina", true);

    public static final ModConfigSpec.BooleanValue BLOCK_ITEM_USE_WITHOUT_STAMINA = BUILDER
            .comment("",
                    " 当前耐力不足以支付消耗时，是否强制中断需要耐力的物品使用（拉弓、弩蓄力、举盾）",
                    " 弩已经装填好的时候不受影响，只有正在蓄力才会被打断",
                    " Whether using a stamina consuming item (drawing a bow, charging a crossbow, raising a shield) is interrupted when the player cannot pay its cost",
                    " An already loaded crossbow is unaffected; only an in-progress charge gets interrupted")
            .define("block_item_use_without_stamina", true);

    public static final ModConfigSpec.BooleanValue BLOCK_SWING_WITHOUT_STAMINA = BUILDER
            .comment("",
                    " 当前耐力不足以支付近战消耗时，是否在客户端提前取消 Better Combat 的挥砍（仅影响表现）",
                    " 关闭后需要依赖服务端拒绝攻击，手感会有一点点延迟",
                    " Whether Better Combat swing animations are cancelled client side while the player cannot pay the melee cost",
                    " Disable this to rely on the server rejecting the attack instead; it feels slightly delayed")
            .define("block_swing_without_stamina", true);

    public static final ModConfigSpec.DoubleValue DROWNING_SINK_SPEED = BUILDER
            .comment("",
                    " 体力耗尽溺水时每 tick 施加的向下速度，默认 0.1（约 2 格/秒）",
                    " 玩家会匀速下沉，直到触底或溺水死亡；按住空格也无法上浮",
                    " Downward speed applied each tick while drowning. Default 0.1 (about 2 blocks/second)",
                    " The player sinks steadily until reaching the bottom or drowning; holding space cannot lift them")
            .defineInRange("drowning_sink_speed", 0.1, 0.01, 5.0);

    public static final ModConfigSpec.BooleanValue ENABLE_MINING_STAMINA = BUILDER
            .comment("",
                    " 是否启用挖掘/砍伐/耕地消耗耐力",
                    " 关闭后，挖掘、砍伐、锄地都不会消耗耐力",
                    " Whether mining, chopping and tilling consume stamina",
                    " When disabled, digging, chopping and tilling cost no stamina")
            .define("enable_mining_stamina", true);

    public static final ModConfigSpec.DoubleValue MINING_STAMINA_PER_LOG = BUILDER
            .comment("",
                    " 挖掘/砍伐的耐力消耗基准，默认 53.3333",
                    " 具体算法：基准 × (方块硬度 ÷ 2)，硬度以原木(硬度2)为基准等比例缩放",
                    " 硬度为 0 的方块（草丛、花、火把等）不消耗耐力",
                    " 不使用正确工具挖掘时（如空手挖石头），消耗翻倍",
                    " 默认值 53.3333 使满耐力 1000 恰好能挖 18.75 块原木，或 25 块石头，与疾跑 1 秒 200 的消耗量级一致",
                    " Base stamina cost for mining/chopping. Default 53.3333",
                    " Formula: base x (block hardness / 2), scaled proportionally from logs (hardness 2)",
                    " Blocks with hardness 0 (grass, flowers, torches, etc.) cost nothing",
                    " Mining with the wrong tool (e.g. bare-handed stone) doubles the cost",
                    " Default 53.3333 makes a full 1000 stamina wheel last for 18.75 logs, or 25 stone blocks, matching the sprint drain scale")
            .defineInRange("mining_stamina_per_log", 53.333333333333336, 0.0, 10000.0);

    public static final ModConfigSpec.DoubleValue TILLING_STAMINA_COST = BUILDER
            .comment("",
                    " 每耕一块地消耗的耐力，默认 40",
                    " 所有工具改造动作都按这个数值扣除，包括锄地耕地、斧头去皮、铲子铲路等",
                    " 40 = 满耐力 1000 恰好能耕 25 块地，与挖掘消耗量级一致",
                    " 耐力不足 40 时无法耕地",
                    " Stamina consumed per tilled farmland. Default 40",
                    " Applies to every tool modification, such as hoeing, axe stripping and shovel pathing",
                    " 40 = a full 1000 stamina wheel tills exactly 25 blocks, matching the mining scale",
                    " Tilling is blocked when stamina is below 40")
            .defineInRange("tilling_stamina_cost", 40.0, 0.0, 10000.0);

    public static final ModConfigSpec.BooleanValue BLOCK_MINING_WITHOUT_STAMINA = BUILDER
            .comment("",
                    " 耐力不足以支付挖掘/砍伐消耗时，是否取消方块破坏",
                    " 关闭后，耐力不足时仍可挖掘，只是不扣耐力",
                    " Whether block breaking is cancelled when the player cannot pay the mining cost",
                    " Disable this to let mining continue without consuming stamina when out of stamina")
            .define("block_mining_without_stamina", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public enum ItemCategory {
        MELEE, RANGED, SHIELD;

        public static ItemCategory byName(String name) {
            for (ItemCategory category : values()) {
                if (category.name().equalsIgnoreCase(name)) return category;
            }
            return null;
        }
    }

    private static volatile Map<Item, Double> meleeOverrides = Map.of();
    private static volatile Map<Item, Double> rangedOverrides = Map.of();
    private static volatile Map<Item, Double> shieldOverrides = Map.of();
    private static volatile double meleeCost = 200.0;
    private static volatile double rangedCost = 400.0;
    private static volatile double shieldCost = 200.0;
    private static volatile boolean blockAttackWithoutStamina = true;
    private static volatile boolean blockItemUseWithoutStamina = true;
    private static volatile boolean blockSwingWithoutStamina = true;
    private static volatile int actionRecoveryDelay = 40;
    private static volatile double drowningSinkSpeed = 0.1;
    private static volatile boolean enableMiningStamina = true;
    private static volatile double miningStaminaPerLog = 53.333333333333336;
    private static volatile double tillingStaminaCost = 40.0;
    private static volatile boolean blockMiningWithoutStamina = true;

    private BetterParaglidersConfig() {}

    private static boolean validateItemOverride(Object value) {
        if (!(value instanceof String entry)) return false;
        int separator = entry.indexOf('=');
        if (separator <= 0 || separator == entry.length() - 1) return false;
        if (ResourceLocation.tryParse(entry.substring(0, separator).trim()) == null) return false;
        try {
            return Double.parseDouble(entry.substring(separator + 1).trim()) >= 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void onConfigEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;
        if (event instanceof ModConfigEvent.Unloading) return;
        reload();
    }

    public static void reload() {
        meleeCost = value(MELEE_STAMINA_COST);
        rangedCost = value(RANGED_STAMINA_COST);
        shieldCost = value(SHIELD_STAMINA_COST);
        actionRecoveryDelay = value(ACTION_RECOVERY_DELAY);
        blockAttackWithoutStamina = value(BLOCK_ATTACK_WITHOUT_STAMINA);
        blockItemUseWithoutStamina = value(BLOCK_ITEM_USE_WITHOUT_STAMINA);
        blockSwingWithoutStamina = value(BLOCK_SWING_WITHOUT_STAMINA);
        drowningSinkSpeed = value(DROWNING_SINK_SPEED);
        enableMiningStamina = value(ENABLE_MINING_STAMINA);
        miningStaminaPerLog = value(MINING_STAMINA_PER_LOG);
        tillingStaminaCost = value(TILLING_STAMINA_COST);
        blockMiningWithoutStamina = value(BLOCK_MINING_WITHOUT_STAMINA);

        meleeOverrides = parseOverrides(value(MELEE_ITEM_OVERRIDES), "melee_item_overrides");
        rangedOverrides = parseOverrides(value(RANGED_ITEM_OVERRIDES), "ranged_item_overrides");
        shieldOverrides = parseOverrides(value(SHIELD_ITEM_OVERRIDES), "shield_item_overrides");

        BetterParaglidersMod.LOGGER.info("Better Paragliders config loaded: melee={} ranged={} shield={} recoveryDelay={} overrides={}/{}/{} mining={} perLog={} till={} blockMining={}",
                meleeCost, rangedCost, shieldCost, actionRecoveryDelay,
                meleeOverrides.size(), rangedOverrides.size(), shieldOverrides.size(),
                enableMiningStamina, miningStaminaPerLog, tillingStaminaCost, blockMiningWithoutStamina);
    }

    private static Map<Item, Double> parseOverrides(List<? extends String> entries, String option) {
        Map<Item, Double> parsed = new HashMap<>();
        for (String entry : entries) {
            int separator = entry.indexOf('=');
            if (separator <= 0 || separator == entry.length() - 1) {
                BetterParaglidersMod.LOGGER.warn("Ignoring malformed entry '{}' in {}", entry, option);
                continue;
            }
            ResourceLocation id = ResourceLocation.tryParse(entry.substring(0, separator).trim());
            if (id == null) {
                BetterParaglidersMod.LOGGER.warn("Ignoring malformed item id in entry '{}' of {}", entry, option);
                continue;
            }
            Item item = BuiltInRegistries.ITEM.get(id);
            if (item == null) {
                BetterParaglidersMod.LOGGER.warn("Ignoring unknown item '{}' in {}", id, option);
                continue;
            }
            try {
                parsed.put(item, Double.parseDouble(entry.substring(separator + 1).trim()));
            } catch (NumberFormatException e) {
                BetterParaglidersMod.LOGGER.warn("Ignoring unparseable cost in entry '{}' of {}", entry, option);
            }
        }
        return Map.copyOf(parsed);
    }

    public static boolean isOverridden(ItemCategory category, Item item) {
        return overridesOf(category).containsKey(item);
    }

    public static double costOf(ItemCategory category, Item item) {
        Double override = overridesOf(category).get(item);
        if (override != null) return override;
        return switch (category) {
            case MELEE -> meleeCost;
            case RANGED -> rangedCost;
            case SHIELD -> shieldCost;
        };
    }

    public static double defaultCostOf(ItemCategory category) {
        return switch (category) {
            case MELEE -> meleeCost;
            case RANGED -> rangedCost;
            case SHIELD -> shieldCost;
        };
    }

    private static Map<Item, Double> overridesOf(ItemCategory category) {
        return switch (category) {
            case MELEE -> meleeOverrides;
            case RANGED -> rangedOverrides;
            case SHIELD -> shieldOverrides;
        };
    }

    public static List<String> rawOverrides(ItemCategory category) {
        List<? extends String> raw = switch (category) {
            case MELEE -> value(MELEE_ITEM_OVERRIDES);
            case RANGED -> value(RANGED_ITEM_OVERRIDES);
            case SHIELD -> value(SHIELD_ITEM_OVERRIDES);
        };
        return new ArrayList<>(raw);
    }

    private static <T> T value(ModConfigSpec.ConfigValue<T> configValue) {
        return SPEC.isLoaded() ? configValue.get() : configValue.getDefault();
    }

    public static void applyOverrides(Map<ItemCategory, List<String>> entries) {
        MELEE_ITEM_OVERRIDES.set(entries.getOrDefault(ItemCategory.MELEE, List.of()));
        RANGED_ITEM_OVERRIDES.set(entries.getOrDefault(ItemCategory.RANGED, List.of()));
        SHIELD_ITEM_OVERRIDES.set(entries.getOrDefault(ItemCategory.SHIELD, List.of()));
        SPEC.save();
        reload();
    }

    public static int actionRecoveryDelay() {
        return actionRecoveryDelay;
    }

    public static boolean blockAttackWithoutStamina() {
        return blockAttackWithoutStamina;
    }

    public static boolean blockItemUseWithoutStamina() {
        return blockItemUseWithoutStamina;
    }

    public static boolean blockSwingWithoutStamina() {
        return blockSwingWithoutStamina;
    }

    public static double drowningSinkSpeed() {
        return drowningSinkSpeed;
    }

    public static boolean enableMiningStamina() {
        return enableMiningStamina;
    }

    public static double miningStaminaPerLog() {
        return miningStaminaPerLog;
    }

    public static double tillingStaminaCost() {
        return tillingStaminaCost;
    }

    public static boolean blockMiningWithoutStamina() {
        return blockMiningWithoutStamina;
    }
}

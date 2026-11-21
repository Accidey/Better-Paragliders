package com.xulai.betterparagliders.config;

import com.xulai.betterparagliders.BetterParaglidersMod;
import com.xulai.betterparagliders.potion.PotionPatcher;
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

    public static final ModConfigSpec.DoubleValue MELEE_STAMINA_COST;
    public static final ModConfigSpec.DoubleValue RANGED_STAMINA_COST;
    public static final ModConfigSpec.DoubleValue SHIELD_STAMINA_COST;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> MELEE_ITEM_OVERRIDES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> RANGED_ITEM_OVERRIDES;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> SHIELD_ITEM_OVERRIDES;
    public static final ModConfigSpec.IntValue ACTION_RECOVERY_DELAY;
    public static final ModConfigSpec.BooleanValue BLOCK_ATTACK_WITHOUT_STAMINA;
    public static final ModConfigSpec.BooleanValue BLOCK_ITEM_USE_WITHOUT_STAMINA;
    public static final ModConfigSpec.BooleanValue BLOCK_SWING_WITHOUT_STAMINA;
    public static final ModConfigSpec.DoubleValue DROWNING_SINK_SPEED;
    public static final ModConfigSpec.BooleanValue ENABLE_MINING_STAMINA;
    public static final ModConfigSpec.DoubleValue MINING_STAMINA_PER_LOG;
    public static final ModConfigSpec.DoubleValue TILLING_STAMINA_COST;
    public static final ModConfigSpec.BooleanValue BLOCK_MINING_WITHOUT_STAMINA;
    public static final ModConfigSpec.IntValue DEATH_HEART_CONTAINER_LOSS;
    public static final ModConfigSpec.BooleanValue SEND_DEATH_PENALTY_MESSAGE;
    public static final ModConfigSpec.BooleanValue ENABLE_POTION_EFFECTS;
    public static final ModConfigSpec.DoubleValue STRENGTH_STAMINA_DISCOUNT;
    public static final ModConfigSpec.DoubleValue LONG_STRENGTH_STAMINA_DISCOUNT;
    public static final ModConfigSpec.DoubleValue STRONG_STRENGTH_STAMINA_DISCOUNT;
    public static final ModConfigSpec.DoubleValue SWIFTNESS_SPRINT_STAMINA_DISCOUNT;
    public static final ModConfigSpec.DoubleValue LONG_SWIFTNESS_SPRINT_STAMINA_DISCOUNT;
    public static final ModConfigSpec.DoubleValue STRONG_SWIFTNESS_SPRINT_STAMINA_DISCOUNT;
    public static final ModConfigSpec.IntValue STRENGTH_DURATION_SECONDS;
    public static final ModConfigSpec.IntValue LONG_STRENGTH_DURATION_SECONDS;
    public static final ModConfigSpec.IntValue STRONG_STRENGTH_DURATION_SECONDS;
    public static final ModConfigSpec.IntValue SWIFTNESS_DURATION_SECONDS;
    public static final ModConfigSpec.IntValue LONG_SWIFTNESS_DURATION_SECONDS;
    public static final ModConfigSpec.IntValue STRONG_SWIFTNESS_DURATION_SECONDS;
    public static final ModConfigSpec.DoubleValue STAMINA_REGEN_EXHAUSTION_PER_WHEEL;

    static {
        MELEE_STAMINA_COST = BUILDER
                .comment("近战武器每次攻击消耗的耐力（默认 200）",
                        "Melee stamina cost per attack (default 200)",
                        "200 = 主模组疾跑 1 秒的消耗量（疾跑 -10/tick × 20 tick = 200/秒）。",
                        "200 = exactly one second of the main mod's sprint drain (sprint -10/tick x 20 ticks = 200/s).",
                        "主手物品既不是远程武器也不是盾牌时，攻击按这个数值扣耐力。",
                        "Used when the main hand item is neither a ranged weapon nor a shield.",
                        "想让某个物品用别的数值，把它加到下面的 melee_item_overrides 里。",
                        "To give a single item its own value, add it to melee_item_overrides below.",
                        "只要当前耐力够付这次消耗，攻击就一定打得出来；付不起时才按 block_attack_without_stamina 处理。",
                        "An attack always goes through as long as the player can afford it; see block_attack_without_stamina for the rest.",
                        "Default: 200.0 / 默认：200.0",
                        "Range: 0.0 ~ 10000.0")
                .defineInRange("melee_stamina_cost", 200.0, 0.0, 10000.0);

        BUILDER.comment(" ");

        RANGED_STAMINA_COST = BUILDER
                .comment("远程武器每次射击消耗的耐力（默认 400）",
                        "Ranged stamina cost per shot (default 400)",
                        "400 = 主模组疾跑 2 秒的消耗量（200/秒 × 2）。",
                        "400 = exactly two seconds of the main mod's sprint drain (200/s x 2).",
                        "弓、弩以及任何 ProjectileWeaponItem 都按这个数值扣耐力。",
                        "Used by bows, crossbows and every ProjectileWeaponItem.",
                        "投掷三叉戟同样按这个数值扣耐力，蓄力不足 10 tick 的快速点击不算投掷。",
                        "Throwing a trident costs the same amount; a quick tap shorter than 10 ticks does not count as a throw.",
                        "想让某个物品用别的数值，把它加到下面的 ranged_item_overrides 里。",
                        "To give a single item its own value, add it to ranged_item_overrides below.",
                        "只要当前耐力够付这次消耗，这一箭就一定射得出去；付不起时按 block_item_use_without_stamina 处理。",
                        "A shot always goes through as long as the player can afford it; see block_item_use_without_stamina for the rest.",
                        "Default: 400.0 / 默认：400.0",
                        "Range: 0.0 ~ 10000.0")
                .defineInRange("ranged_stamina_cost", 400.0, 0.0, 10000.0);

        BUILDER.comment(" ");

        SHIELD_STAMINA_COST = BUILDER
                .comment("盾牌成功格挡一次攻击消耗的耐力（默认 200）",
                        "Shield stamina cost per successful block (default 200)",
                        "200 = 主模组疾跑 1 秒的消耗量（200/秒）。",
                        "200 = exactly one second of the main mod's sprint drain (200/s).",
                        "只有真正被判定为格挡成功的那一次才扣耐力，没挡住的伤害不扣。",
                        "Only blocks that actually succeed consume stamina; unblocked damage costs nothing.",
                        "想让某个物品用别的数值，把它加到下面的 shield_item_overrides 里。",
                        "To give a single item its own value, add it to shield_item_overrides below.",
                        "只要当前耐力够付这次消耗，格挡就一定成功；付不起时格挡照常生效但不扣耐力，而下一次举盾会被中断。",
                        "A block always succeeds as long as the player can afford it; when they cannot, the block still works but costs nothing and the next shield raise gets interrupted.",
                        "Default: 200.0 / 默认：200.0",
                        "Range: 0.0 ~ 10000.0")
                .defineInRange("shield_stamina_cost", 200.0, 0.0, 10000.0);

        BUILDER.comment(" ");

        MELEE_ITEM_OVERRIDES = BUILDER
                .comment("近战物品的单独耐力消耗表",
                        "Per-item melee stamina cost overrides",
                        "格式为 物品ID=消耗，这里的数值直接覆盖 melee_stamina_cost，不是在它基础上叠加。",
                        "Format: item_id=cost. The value overrides melee_stamina_cost instead of adding to it.",
                        "示例：minecraft:trident=250 表示三叉戟每次攻击消耗 250 点耐力。",
                        "Example: minecraft:trident=250 makes the trident cost 250 stamina per attack.",
                        "游戏内可用指令直接写入主手物品：/betterparagliders additem melee [消耗]。",
                        "In game you can write the held item with: /betterparagliders additem melee [cost].",
                        "Default: [] / 默认：[]")
                .defineListAllowEmpty("melee_item_overrides", List.of(), () -> "minecraft:trident=250",
                        BetterParaglidersConfig::validateItemOverride);

        BUILDER.comment(" ");

        RANGED_ITEM_OVERRIDES = BUILDER
                .comment("远程物品的单独耐力消耗表",
                        "Per-item ranged stamina cost overrides",
                        "格式为 物品ID=消耗，这里的数值直接覆盖 ranged_stamina_cost，不是在它基础上叠加。",
                        "Format: item_id=cost. The value overrides ranged_stamina_cost instead of adding to it.",
                        "写进这张表的物品会被当作远程武器对待，即使它本身不是弓或弩。",
                        "Items listed here are treated as ranged weapons even if they are not bows or crossbows.",
                        "投掷三叉戟的消耗也读这张表，可以把 minecraft:trident 加进来单独设定投掷消耗。",
                        "Trident throws read this list too, so minecraft:trident can be added here to set its throw cost separately.",
                        "示例：minecraft:crossbow=500 表示弩每次射击消耗 500 点耐力。",
                        "Example: minecraft:crossbow=500 makes the crossbow cost 500 stamina per shot.",
                        "游戏内可用指令直接写入主手物品：/betterparagliders additem ranged [消耗]。",
                        "In game you can write the held item with: /betterparagliders additem ranged [cost].",
                        "Default: [] / 默认：[]")
                .defineListAllowEmpty("ranged_item_overrides", List.of(), () -> "minecraft:crossbow=500",
                        BetterParaglidersConfig::validateItemOverride);

        BUILDER.comment(" ");

        SHIELD_ITEM_OVERRIDES = BUILDER
                .comment("盾牌的单独耐力消耗表",
                        "Per-item shield stamina cost overrides",
                        "格式为 物品ID=消耗，这里的数值直接覆盖 shield_stamina_cost，不是在它基础上叠加。",
                        "Format: item_id=cost. The value overrides shield_stamina_cost instead of adding to it.",
                        "写进这张表的物品会被当作盾牌对待，即使它本身不是原版盾牌。",
                        "Items listed here are treated as shields even if they are not the vanilla shield.",
                        "示例：minecraft:shield=300 表示原版盾牌每次格挡消耗 300 点耐力。",
                        "Example: minecraft:shield=300 makes the vanilla shield cost 300 stamina per block.",
                        "游戏内可用指令直接写入主手物品：/betterparagliders additem shield [消耗]。",
                        "In game you can write the held item with: /betterparagliders additem shield [cost].",
                        "Default: [] / 默认：[]")
                .defineListAllowEmpty("shield_item_overrides", List.of(), () -> "minecraft:shield=300",
                        BetterParaglidersConfig::validateItemOverride);

        BUILDER.comment(" ");

        ACTION_RECOVERY_DELAY = BUILDER
                .comment("完成一次动作后要等多久才开始重新恢复耐力（tick）",
                        "Stamina regeneration pause after an action (ticks)",
                        "单位 tick（20 tick = 1 秒），默认 40。这段等待期内耐力完全不回复，等待期结束后才按当前玩家状态的速度恢复。",
                        "In ticks (20 ticks = 1 second). Default 40. Stamina does not regenerate at all during this wait; regeneration resumes at the rate of the current player state afterwards.",
                        "设为 0 表示动作扣完耐力后立刻恢复。",
                        "Set to 0 to resume regeneration immediately after an action.",
                        "Default: 40 / 默认：40",
                        "Range: 0 ~ 200")
                .defineInRange("action_recovery_delay", 40, 0, 200);

        BUILDER.comment(" ");

        BLOCK_ATTACK_WITHOUT_STAMINA = BUILDER
                .comment("当前耐力不足以支付近战消耗时是否取消攻击",
                        "Whether to cancel melee attacks when stamina is insufficient",
                        "耐力只要够付这次消耗就一定能打出来，扣完立刻进入上面的恢复等待期。",
                        "An attack always goes through as long as the player can afford it, then the recovery wait above kicks in.",
                        "关闭后耐力不足也会照常命中，只是不扣耐力。",
                        "Disable this to let out-of-stamina attacks land without consuming anything.",
                        "Default: true / 默认：true")
                .define("block_attack_without_stamina", true);

        BUILDER.comment(" ");

        BLOCK_ITEM_USE_WITHOUT_STAMINA = BUILDER
                .comment("耐力不足时是否强制中断需要耐力的物品使用",
                        "Whether to interrupt stamina-consuming item use when out of stamina",
                        "包括拉弓、弩蓄力、举盾、投掷三叉戟。弩已经装填好的时候不受影响，只有正在蓄力才会被打断。",
                        "Covers drawing a bow, charging a crossbow, raising a shield and throwing a trident. An already loaded crossbow is unaffected; only an in-progress charge gets interrupted.",
                        "Default: true / 默认：true")
                .define("block_item_use_without_stamina", true);

        BUILDER.comment(" ");

        BLOCK_SWING_WITHOUT_STAMINA = BUILDER
                .comment("耐力不足时是否在客户端提前取消 Better Combat 的挥砍",
                        "Whether to cancel Better Combat swing animations client side when out of stamina",
                        "仅影响表现。关闭后需要依赖服务端拒绝攻击，手感会有一点点延迟。",
                        "Affects visuals only. Disable this to rely on the server rejecting the attack instead; it feels slightly delayed.",
                        "Default: true / 默认：true")
                .define("block_swing_without_stamina", true);

        BUILDER.comment(" ");

        DROWNING_SINK_SPEED = BUILDER
                .comment("体力耗尽溺水时每 tick 施加的向下速度",
                        "Downward speed applied each tick while drowning",
                        "默认 0.1（约 2 格/秒）。玩家会匀速下沉，直到触底或溺水死亡；按住空格也无法上浮。",
                        "Default 0.1 (about 2 blocks/second). The player sinks steadily until reaching the bottom or drowning; holding space cannot lift them.",
                        "Default: 0.1 / 默认：0.1",
                        "Range: 0.01 ~ 5.0")
                .defineInRange("drowning_sink_speed", 0.1, 0.01, 5.0);

        BUILDER.comment(" ");

        ENABLE_MINING_STAMINA = BUILDER
                .comment("是否启用挖掘/砍伐/耕地消耗耐力",
                        "Whether mining, chopping and tilling consume stamina",
                        "关闭后，挖掘、砍伐、锄地都不会消耗耐力。",
                        "When disabled, digging, chopping and tilling cost no stamina.",
                        "Default: true / 默认：true")
                .define("enable_mining_stamina", true);

        BUILDER.comment(" ");

        MINING_STAMINA_PER_LOG = BUILDER
                .comment("挖掘/砍伐的耐力消耗基准",
                        "Base stamina cost for mining and chopping",
                        "具体算法：基准 × (方块硬度 ÷ 2)，硬度以原木(硬度2)为基准等比例缩放。",
                        "Formula: base x (block hardness / 2), scaled proportionally from logs (hardness 2).",
                        "硬度为 0 的方块（草丛、花、火把等）不消耗耐力。",
                        "Blocks with hardness 0 (grass, flowers, torches, etc.) cost nothing.",
                        "不使用正确工具挖掘时（如空手挖石头），消耗翻倍。",
                        "Mining with the wrong tool (e.g. bare-handed stone) doubles the cost.",
                        "默认值 53.3333 使满耐力 1000 恰好能挖 18.75 块原木，或 25 块石头，与疾跑 1 秒 200 的消耗量级一致。",
                        "Default 53.3333 makes a full 1000 stamina wheel last for 18.75 logs, or 25 stone blocks, matching the sprint drain scale.",
                        "Default: 53.3333 / 默认：53.3333",
                        "Range: 0.0 ~ 10000.0")
                .defineInRange("mining_stamina_per_log", 53.333333333333336, 0.0, 10000.0);

        BUILDER.comment(" ");

        TILLING_STAMINA_COST = BUILDER
                .comment("每耕一块地消耗的耐力",
                        "Stamina consumed per tilled farmland",
                        "所有工具改造动作都按这个数值扣除，包括锄地耕地、斧头去皮、铲子铲路等。",
                        "Applies to every tool modification, such as hoeing, axe stripping and shovel pathing.",
                        "40 = 满耐力 1000 恰好能耕 25 块地，与挖掘消耗量级一致。",
                        "40 = a full 1000 stamina wheel tills exactly 25 blocks, matching the mining scale.",
                        "耐力不足 40 时无法耕地。",
                        "Tilling is blocked when stamina is below 40.",
                        "Default: 40.0 / 默认：40.0",
                        "Range: 0.0 ~ 10000.0")
                .defineInRange("tilling_stamina_cost", 40.0, 0.0, 10000.0);

        BUILDER.comment(" ");

        BLOCK_MINING_WITHOUT_STAMINA = BUILDER
                .comment("耐力不足以支付挖掘/砍伐消耗时是否取消方块破坏",
                        "Whether block breaking is cancelled when stamina is insufficient",
                        "关闭后，耐力不足时仍可挖掘，只是不扣耐力。",
                        "Disable this to let mining continue without consuming stamina when out of stamina.",
                        "Default: true / 默认：true")
                .define("block_mining_without_stamina", true);

        BUILDER.comment(" ");

        DEATH_HEART_CONTAINER_LOSS = BUILDER
                .comment("每次死亡扣除的心之容器数量",
                        "Heart containers lost per death",
                        "玩家每次死亡都会失去这个数量的心之容器，每 1 个心之容器 = 1 颗额外红心 = 2 点最大生命值。",
                        "Each death removes this many heart containers; 1 heart container = 1 extra heart = 2 max health.",
                        "扣减只作用于 Paraglider 心之容器提供的额外生命上限，扣到 0 个容器（初始 20 点生命）为止，不会低于初始值。",
                        "Only the extra max health granted by Paraglider heart containers is affected; the penalty stops at 0 containers (the base 20 health) and never goes below it.",
                        "死亡时心之容器已为 0 则不再扣减。",
                        "Deaths with 0 heart containers lose nothing.",
                        "免疫死亡（如不死图腾）不触发；创造/旁观模式不触发。",
                        "Death prevention (e.g. totem of undying) and creative/spectator mode do not trigger the penalty.",
                        "Default: 1 / 默认：1",
                        "Range: 1 ~ 20")
                .defineInRange("death_heart_container_loss", 1, 1, 20);

        BUILDER.comment(" ");

        SEND_DEATH_PENALTY_MESSAGE = BUILDER
                .comment("死亡扣除心之容器后是否向玩家发送聊天提示",
                        "Whether to send a chat message after the death penalty",
                        "开启后玩家重生时会收到一条消息，说明失去了多少个心之容器。",
                        "When enabled, the player receives a message after respawning explaining how many heart containers were lost.",
                        "Default: true / 默认：true")
                .define("send_death_penalty_message", true);

        BUILDER.comment(" ");

        BUILDER.comment("药水效果替换与酿造配方", "Potion effect replacement and brewing recipes").push("potionEffects");

        ENABLE_POTION_EFFECTS = BUILDER
                .comment("药水效果与酿造配方改动的总开关",
                        "Master switch for potion effect and brewing recipe changes",
                        "关闭后三档力量/迅捷药水恢复原版效果与时长，酿造配方恢复原版（烈焰粉与糖链）。重新开启后药水效果立即重新补丁，酿造配方需重进世界生效。",
                        "When off, the strength/swiftness potions return to vanilla effects and durations, and brewing returns to the vanilla chains (blaze powder / sugar). Turning it on re-patches the potions instantly; brewing recipes apply after re-entering the world.",
                        "Default: true / 默认：true")
                .define("enabled", true);

        BUILDER.comment(" ");

        STRENGTH_STAMINA_DISCOUNT = BUILDER
                .comment("力量药水附带的耐力效率效果对近战与格挡消耗的减免比例",
                        "Melee and block stamina discount granted by the stamina efficiency effect of strength potions",
                        "喝下力量药水后获得的耐力效率等级 0 对应这个数值，等级更高时往上收敛，不会更低。",
                        "Stamina efficiency level 0 from strength potions maps to this value; higher levels never discount less.",
                        "只作用于近战与格挡，不影响远程、挖掘与耕地。",
                        "Applies to melee and blocking only, not to ranged attacks, mining or tilling.",
                        "Default: 0.10 / 默认：0.10",
                        "Range: 0.0 ~ 1.0")
                .defineInRange("strengthStaminaDiscount", 0.10, 0.0, 1.0);

        BUILDER.comment(" ");

        LONG_STRENGTH_STAMINA_DISCOUNT = BUILDER
                .comment("长效力量药水附带的耐力效率效果对近战与格挡消耗的减免比例",
                        "Melee and block stamina discount granted by the stamina efficiency effect of long strength potions",
                        "长效力量药水给的是耐力效率等级 1，对应这个数值。",
                        "Long strength potions grant stamina efficiency level 1, which maps to this value.",
                        "Default: 0.20 / 默认：0.20",
                        "Range: 0.0 ~ 1.0")
                .defineInRange("longStrengthStaminaDiscount", 0.20, 0.0, 1.0);

        BUILDER.comment(" ");

        STRONG_STRENGTH_STAMINA_DISCOUNT = BUILDER
                .comment("强效力量药水附带的耐力效率效果对近战与格挡消耗的减免比例",
                        "Melee and block stamina discount granted by the stamina efficiency effect of strong strength potions",
                        "强效力量药水给的是耐力效率等级 2，等级 2 及以上都对应这个数值。",
                        "Strong strength potions grant stamina efficiency level 2; level 2 and above map to this value.",
                        "Default: 0.30 / 默认：0.30",
                        "Range: 0.0 ~ 1.0")
                .defineInRange("strongStrengthStaminaDiscount", 0.30, 0.0, 1.0);

        BUILDER.comment(" ");

        SWIFTNESS_SPRINT_STAMINA_DISCOUNT = BUILDER
                .comment("迅捷药水附带的冲刺效率效果对冲刺与游泳消耗的减免比例",
                        "Sprint and swim stamina discount granted by the sprint efficiency effect of swiftness potions",
                        "喝下迅捷药水后获得的冲刺效率等级 0 对应这个数值，等级更高时往上收敛，不会更低。",
                        "Sprint efficiency level 0 from swiftness potions maps to this value; higher levels never discount less.",
                        "只作用于奔跑与游泳的耐力增量，不影响滑翔、攀爬等其它移动状态。",
                        "Applies to the running and swimming stamina drain only, not to gliding, climbing or other movement states.",
                        "Default: 0.10 / 默认：0.10",
                        "Range: 0.0 ~ 1.0")
                .defineInRange("swiftnessSprintStaminaDiscount", 0.10, 0.0, 1.0);

        BUILDER.comment(" ");

        LONG_SWIFTNESS_SPRINT_STAMINA_DISCOUNT = BUILDER
                .comment("长效迅捷药水附带的冲刺效率效果对冲刺与游泳消耗的减免比例",
                        "Sprint and swim stamina discount granted by the sprint efficiency effect of long swiftness potions",
                        "长效迅捷药水给的是冲刺效率等级 1，对应这个数值。",
                        "Long swiftness potions grant sprint efficiency level 1, which maps to this value.",
                        "Default: 0.20 / 默认：0.20",
                        "Range: 0.0 ~ 1.0")
                .defineInRange("longSwiftnessSprintStaminaDiscount", 0.20, 0.0, 1.0);

        BUILDER.comment(" ");

        STRONG_SWIFTNESS_SPRINT_STAMINA_DISCOUNT = BUILDER
                .comment("强效迅捷药水附带的冲刺效率效果对冲刺与游泳消耗的减免比例",
                        "Sprint and swim stamina discount granted by the sprint efficiency effect of strong swiftness potions",
                        "强效迅捷药水给的是冲刺效率等级 2，等级 2 及以上都对应这个数值。",
                        "Strong swiftness potions grant sprint efficiency level 2; level 2 and above map to this value.",
                        "Default: 0.30 / 默认：0.30",
                        "Range: 0.0 ~ 1.0")
                .defineInRange("strongSwiftnessSprintStaminaDiscount", 0.30, 0.0, 1.0);

        BUILDER.comment(" ");

        STRENGTH_DURATION_SECONDS = BUILDER
                .comment("力量药水效果时长（秒）",
                        "Strength potion effect duration in seconds",
                        "同时决定力量效果与耐力效率效果的持续时长。修改后立即作用于所有已有药水，无需重新酿造。",
                        "Applies to both the strength effect and its stamina efficiency effect. Affects already brewed potions immediately.",
                        "Default: 180 / 默认：180",
                        "Range: 1 ~ 7200")
                .defineInRange("strengthDurationSeconds", 180, 1, 7200);

        BUILDER.comment(" ");

        LONG_STRENGTH_DURATION_SECONDS = BUILDER
                .comment("长效力量药水效果时长（秒）",
                        "Long strength potion effect duration in seconds",
                        "同时决定力量效果与耐力效率效果的持续时长。修改后立即作用于所有已有药水，无需重新酿造。",
                        "Applies to both the strength effect and its stamina efficiency effect. Affects already brewed potions immediately.",
                        "Default: 480 / 默认：480",
                        "Range: 1 ~ 7200")
                .defineInRange("longStrengthDurationSeconds", 480, 1, 7200);

        BUILDER.comment(" ");

        STRONG_STRENGTH_DURATION_SECONDS = BUILDER
                .comment("强效力量药水效果时长（秒）",
                        "Strong strength potion effect duration in seconds",
                        "同时决定力量效果与耐力效率效果的持续时长。原版只有 90 秒，这里默认放大到 15 分钟。修改后立即作用于所有已有药水，无需重新酿造。",
                        "Applies to both the strength effect and its stamina efficiency effect. Vanilla is 90 seconds; the default extends it to 15 minutes. Affects already brewed potions immediately.",
                        "Default: 900 / 默认：900",
                        "Range: 1 ~ 7200")
                .defineInRange("strongStrengthDurationSeconds", 900, 1, 7200);

        BUILDER.comment(" ");

        SWIFTNESS_DURATION_SECONDS = BUILDER
                .comment("迅捷药水效果时长（秒）",
                        "Swiftness potion effect duration in seconds",
                        "同时决定迅捷效果与冲刺效率效果的持续时长。修改后立即作用于所有已有药水，无需重新酿造。",
                        "Applies to both the swiftness effect and its sprint efficiency effect. Affects already brewed potions immediately.",
                        "Default: 180 / 默认：180",
                        "Range: 1 ~ 7200")
                .defineInRange("swiftnessDurationSeconds", 180, 1, 7200);

        BUILDER.comment(" ");

        LONG_SWIFTNESS_DURATION_SECONDS = BUILDER
                .comment("长效迅捷药水效果时长（秒）",
                        "Long swiftness potion effect duration in seconds",
                        "同时决定迅捷效果与冲刺效率效果的持续时长。修改后立即作用于所有已有药水，无需重新酿造。",
                        "Applies to both the swiftness effect and its sprint efficiency effect. Affects already brewed potions immediately.",
                        "Default: 480 / 默认：480",
                        "Range: 1 ~ 7200")
                .defineInRange("longSwiftnessDurationSeconds", 480, 1, 7200);

        BUILDER.comment(" ");

        STRONG_SWIFTNESS_DURATION_SECONDS = BUILDER
                .comment("强效迅捷药水效果时长（秒）",
                        "Strong swiftness potion effect duration in seconds",
                        "同时决定迅捷效果与冲刺效率效果的持续时长。原版只有 90 秒，这里默认放大到 15 分钟。修改后立即作用于所有已有药水，无需重新酿造。",
                        "Applies to both the swiftness effect and its sprint efficiency effect. Vanilla is 90 seconds; the default extends it to 15 minutes. Affects already brewed potions immediately.",
                        "Default: 900 / 默认：900",
                        "Range: 1 ~ 7200")
                .defineInRange("strongSwiftnessDurationSeconds", 900, 1, 7200);

        BUILDER.pop();

        BUILDER.comment(" ");

        STAMINA_REGEN_EXHAUSTION_PER_WHEEL = BUILDER
                .comment("每恢复一整轮耐力增加的疲惫值",
                        "Exhaustion added per full stamina wheel recovered",
                        "耐力每恢复 1000 点（一整轮）就给玩家增加这个数值的疲惫值，疲惫值按原版规则折算成饱和度与饥饿值消耗：4 点疲惫 = 1 点饥饿（半格图标）。",
                        "Every 1000 stamina recovered (one full wheel) adds this much exhaustion to the player. Vanilla rules convert exhaustion into saturation and hunger loss: 4 exhaustion = 1 food point (half an icon).",
                        "默认 8 表示每回满一轮掉一整格饥饿图标；从空轮到三整轮全满约消耗 1.5 格。恢复实际发生时才计费，满轮溢出的部分不计。",
                        "The default 8 drains one full hunger icon per wheel; refilling from empty to three full wheels costs about 1.5 icons. Only actual recovery is charged; overflow beyond the wheel is free.",
                        "0 = 关闭该机制。创造与旁观模式不受影响。",
                        "0 disables the mechanic. Creative and spectator modes are unaffected.",
                        "Default: 8.0 / 默认：8.0",
                        "Range: 0.0 ~ 80.0")
                .defineInRange("staminaRegenExhaustionPerWheel", 8.0, 0.0, 80.0);
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

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
    private static volatile int deathHeartContainerLoss = 1;
    private static volatile boolean sendDeathPenaltyMessage = true;
    private static volatile double strengthStaminaDiscount = 0.10;
    private static volatile double longStrengthStaminaDiscount = 0.20;
    private static volatile double strongStrengthStaminaDiscount = 0.30;
    private static volatile double swiftnessSprintStaminaDiscount = 0.10;
    private static volatile double longSwiftnessSprintStaminaDiscount = 0.20;
    private static volatile double strongSwiftnessSprintStaminaDiscount = 0.30;
    private static volatile int strengthDurationSeconds = 180;
    private static volatile int longStrengthDurationSeconds = 480;
    private static volatile int strongStrengthDurationSeconds = 900;
    private static volatile int swiftnessDurationSeconds = 180;
    private static volatile int longSwiftnessDurationSeconds = 480;
    private static volatile int strongSwiftnessDurationSeconds = 900;
    private static volatile boolean potionEffectsEnabled = true;
    private static volatile double staminaRegenExhaustionPerWheel = 8.0;

    private BetterParaglidersConfig() {}

    public enum ItemCategory {
        MELEE, RANGED, SHIELD;

        public static ItemCategory byName(String name) {
            for (ItemCategory category : values()) {
                if (category.name().equalsIgnoreCase(name)) return category;
            }
            return null;
        }
    }

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
        if (PotionPatcher.isPatched()) {
            PotionPatcher.apply();
            BetterParaglidersMod.LOGGER.info("Better Paragliders potion effects reapplied from config");
        }
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
        deathHeartContainerLoss = value(DEATH_HEART_CONTAINER_LOSS);
        sendDeathPenaltyMessage = value(SEND_DEATH_PENALTY_MESSAGE);
        strengthStaminaDiscount = value(STRENGTH_STAMINA_DISCOUNT);
        longStrengthStaminaDiscount = value(LONG_STRENGTH_STAMINA_DISCOUNT);
        strongStrengthStaminaDiscount = value(STRONG_STRENGTH_STAMINA_DISCOUNT);
        swiftnessSprintStaminaDiscount = value(SWIFTNESS_SPRINT_STAMINA_DISCOUNT);
        longSwiftnessSprintStaminaDiscount = value(LONG_SWIFTNESS_SPRINT_STAMINA_DISCOUNT);
        strongSwiftnessSprintStaminaDiscount = value(STRONG_SWIFTNESS_SPRINT_STAMINA_DISCOUNT);
        strengthDurationSeconds = value(STRENGTH_DURATION_SECONDS);
        longStrengthDurationSeconds = value(LONG_STRENGTH_DURATION_SECONDS);
        strongStrengthDurationSeconds = value(STRONG_STRENGTH_DURATION_SECONDS);
        swiftnessDurationSeconds = value(SWIFTNESS_DURATION_SECONDS);
        longSwiftnessDurationSeconds = value(LONG_SWIFTNESS_DURATION_SECONDS);
        strongSwiftnessDurationSeconds = value(STRONG_SWIFTNESS_DURATION_SECONDS);
        potionEffectsEnabled = value(ENABLE_POTION_EFFECTS);
        staminaRegenExhaustionPerWheel = value(STAMINA_REGEN_EXHAUSTION_PER_WHEEL);

        meleeOverrides = parseOverrides(value(MELEE_ITEM_OVERRIDES), "melee_item_overrides");
        rangedOverrides = parseOverrides(value(RANGED_ITEM_OVERRIDES), "ranged_item_overrides");
        shieldOverrides = parseOverrides(value(SHIELD_ITEM_OVERRIDES), "shield_item_overrides");

        BetterParaglidersMod.LOGGER.info("Better Paragliders config loaded: melee={} ranged={} shield={} recoveryDelay={} overrides={}/{}/{} mining={} perLog={} till={} blockMining={} deathLoss={} sendMsg={}",
                meleeCost, rangedCost, shieldCost, actionRecoveryDelay,
                meleeOverrides.size(), rangedOverrides.size(), shieldOverrides.size(),
                enableMiningStamina, miningStaminaPerLog, tillingStaminaCost, blockMiningWithoutStamina,
                deathHeartContainerLoss, sendDeathPenaltyMessage);
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

    public static int deathHeartContainerLoss() {
        return deathHeartContainerLoss;
    }

    public static boolean sendDeathPenaltyMessage() {
        return sendDeathPenaltyMessage;
    }

    public static double staminaEfficiencyDiscount(int amplifier) {
        if (amplifier <= 0) return strengthStaminaDiscount;
        if (amplifier == 1) return longStrengthStaminaDiscount;
        return strongStrengthStaminaDiscount;
    }

    public static double sprintEfficiencyDiscount(int amplifier) {
        if (amplifier <= 0) return swiftnessSprintStaminaDiscount;
        if (amplifier == 1) return longSwiftnessSprintStaminaDiscount;
        return strongSwiftnessSprintStaminaDiscount;
    }

    public static int strengthDurationSeconds() {
        return strengthDurationSeconds;
    }

    public static int longStrengthDurationSeconds() {
        return longStrengthDurationSeconds;
    }

    public static int strongStrengthDurationSeconds() {
        return strongStrengthDurationSeconds;
    }

    public static int swiftnessDurationSeconds() {
        return swiftnessDurationSeconds;
    }

    public static int longSwiftnessDurationSeconds() {
        return longSwiftnessDurationSeconds;
    }

    public static int strongSwiftnessDurationSeconds() {
        return strongSwiftnessDurationSeconds;
    }

    public static boolean potionEffectsEnabled() {
        return potionEffectsEnabled;
    }

    public static double staminaRegenExhaustionPerWheel() {
        return staminaRegenExhaustionPerWheel;
    }
}

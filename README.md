# Better Paragliders (1.21.1 NeoForge)

Paraglider 的附属模组：把 Paraglider 的耐力系统接到武器攻击、远程射击与盾牌格挡上。

- 主模组：Paraglider `21.1.5+`（必需依赖）
- 战斗模组：Better Combat `2.4.0+`（必需依赖）
- Java 包名：`com.xulai.betterparagliders`

## 功能

| 行为 | 默认耐力消耗 | 换算 |
| --- | --- | --- |
| 近战攻击 | 200 | = 疾跑 1 秒 |
| 远程射击（弓 / 弩 / 任意 `ProjectileWeaponItem`） | 400 | = 疾跑 2 秒 |
| 盾牌成功格挡 | 200 | = 疾跑 1 秒 |

基准是主模组 Paraglider 的疾跑消耗：`running` 状态 `staminaDelta = -10`，而 `PlayerMovement.updateStamina()`
每 tick 结算一次，所以**疾跑 1 秒 = 10 × 20 = 200 点耐力**。1 格耐力 = 1000 点。

- 每次动作**一次性**扣除对应耐力，不按武器伤害计算。
- **空挥也扣**：只要挥砍动作真的打出来（哪怕一个目标都没碰到），就近战消耗扣耐力；远程只要把箭射出去就扣，不看有没有命中。
- **耐力只要够付这次消耗，动作就一定做得出来**；做完立刻进入一段恢复等待期，等待期内耐力完全不回复，等待期结束才按当前状态速度恢复。
- 可以给单个物品单独指定数值，写在配置文件的 `*_item_overrides` 表里，**覆盖**该类别的默认值。
- 耐力不够付时，默认取消近战攻击、中断拉弓 / 弩蓄力 / 举盾（可各自关闭）。盾牌格挡照常生效但不扣耐力。
- 保留了 22 个耐力属性（疾跑 / 游泳 / 水下 / 各类移动状态减免，以及近战 / 单手 / 双手 / 远程 / 格挡减免），可在属性修饰符里自由配置。

### 死亡惩罚（心之容器）

为了防止玩家通过无限死亡重生来磨死 BOSS，玩家每次死亡都会失去 1 个心之容器
（可通过配置调整），每 1 个心之容器 = 1 颗额外红心 = 2 点最大生命值。

- 惩罚只作用于 Paraglider 心之容器提供的**额外**生命上限，扣到 0 个容器
  （初始 20 点生命 / 10 颗心）为止，不会低于初始值。
- 心之容器为 0 时死亡不再扣减。
- 免疫死亡（如不死图腾）不触发；创造 / 旁观模式不触发。
- 玩家重生时会收到一条聊天提示（可配置关闭）。
- 恢复方式：重新获得并食用心之容器物品，或由管理员使用 Paraglider 自带命令
  `paraglider set heart <玩家> <数量>` 补回。

### 对主模组“耐力耗尽”机制的修改

Paraglider 原本的 `isDepleted` 不是“耐力为 0”，而是“耐力耗尽后必须回到 `min(耐力上限, 3 格)` 才解除”。
在这段恢复期里主模组会禁止冲刺 / 游泳 / 鞘翅、附加饥饿效果并禁用飞行，玩家因此一个需要耐力的动作都做不了。

本模组把这条门槛放宽为**耐力一回到正值就立刻解除 depleted**（mixin `ServerPlayerMovement.updateStamina`），
于是“恢复期”不再阻塞任何动作，玩家只要攒够某次动作的消耗就能立刻使用它。
代价是主模组原有的“耐力耗尽惩罚”窗口缩短到只剩耐力恰好为 0 的那一 tick。

## 配置文件

`config/betterparagliders-server.toml`（服务端配置，中英双语注释）：

```toml
melee_stamina_cost = 200.0
ranged_stamina_cost = 400.0
shield_stamina_cost = 200.0

melee_item_overrides = []
ranged_item_overrides = []
shield_item_overrides = []

action_recovery_delay = 40

block_attack_without_stamina = true
block_item_use_without_stamina = true
block_swing_without_stamina = true

death_heart_container_loss = 1
send_death_penalty_message = true
```

`action_recovery_delay` 单位为 tick（20 tick = 1 秒），设为 0 表示动作扣完耐力后立刻恢复；默认 40 = 动作后停 2 秒不回复。

`death_heart_container_loss` 为每次死亡扣除的心之容器数量（默认 1，范围 1~20），
`send_death_penalty_message` 控制是否发送扣减提示（默认开启）。

物品覆盖表格式为 `物品ID=消耗`，例如：

```toml
melee_item_overrides = ["minecraft:trident=250"]
```

写进 `ranged_item_overrides` 的物品会被当作远程武器，写进 `shield_item_overrides` 的物品会被当作盾牌（即使它本身不是弓 / 弩 / 原版盾牌）。

## 命令

均需权限等级 2（OP）。

```
/betterparagliders additem <melee|ranged|shield> [消耗]
/betterparagliders removeitem <melee|ranged|shield>
```

`additem` 把主手物品写入对应类别的覆盖表并立即落盘重载；省略 `消耗` 时使用该类别的默认值。
`removeitem` 把主手物品从所有覆盖表中移除。

`/bp` 是 `/betterparagliders` 的简写。

## 构建

```
gradlew build
```

产物在 `build/libs/betterparagliders-1.0.0-1.21.1-NeoForge.jar`。

`libs/` 下放着编译期依赖（Paraglider 与 Better Combat 的 1.21.1 发行 jar），它们只在编译时使用，不会打进产物。
`libs/dev/` 下是本地开发运行时才需要的附属前置（cloth-config / player-animation-lib / architectury），同样不会打进产物。

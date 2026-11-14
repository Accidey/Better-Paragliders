# Better Paragliders

**The stamina wheel now pays for everything you do.**

An add-on for Paraglider that carries stamina out of movement and into combat, gathering and water. Every swing, shot, block, pickaxe hit and second of swimming is drawn from the same wheel, priced against the base mod's own sprint drain — one second of sprinting costs 200, a full default wheel holds 1000 — so every number below can be read at a glance.

## ⚔️ Combat

- **Melee — 200 per swing** (one second of sprinting). Charged when the swing happens, not when damage lands: a swing that hits nothing still costs.
- **Ranged — 400 per shot** (two seconds of sprinting). Bows, crossbows and any projectile weapon; a charged trident throw counts, a quick tap does not.
- **Shield — 200 per successful block.** Only blocks that actually stop damage are charged.
- Costs are flat per action. Weapon damage, enchantments and the number of targets change nothing.
- If you can pay, the action always succeeds; regeneration then pauses briefly (two seconds by default).
- If you cannot pay, the action is refused: the swing never starts, a drawn bow or raised shield drops, a charging crossbow is interrupted (a loaded one is not). A block you cannot afford still protects you, free of charge, but the next raise is interrupted. Each refusal can be switched off separately.

## ⛏️ Gathering

- Mining, chopping and tilling draw stamina, priced by block hardness with logs as the reference.
- The cost is drained while you dig rather than taken in a single hit.
- Blocks with no hardness — grass, flowers, torches — are free. Mining without the correct tool costs double.
- Hoeing, stripping and path-making use a flat cost per block.
- Chain-mining is charged per block broken.
- Out of stamina, the block refuses to break.

## 🌊 Water

- Running dry in water is a hazard rather than a pause: steering and buoyancy are lost, you sink steadily (about two blocks per second by default), air burns three times faster, and once it is gone the screen darkens as health drains. Holding jump will not lift you.
- Stamina can be recovered by treading: float in place with jump held while any stamina remains, and it returns at half the idle rate.

## 💀 Death Penalty

- Each death takes one heart container (configurable). One container is one extra heart, two points of maximum health.
- Only the extra health granted by Paraglider's vessels is affected. The penalty stops at zero containers and never touches the base ten hearts; dying with none costs nothing.
- Totem-style death prevention, creative mode and spectator mode do not trigger it. A chat notice reports the loss.
- Containers are earned back the usual way.

## 🎛️ Tuning

Server-side settings:

- Base costs for the three combat categories, plus the post-action regeneration pause.
- Per-item overrides for each category. An item listed as ranged or shield is treated as one even if it is neither, and operators can register the held item with a single in-game command.
- Switches for refusing attacks, item use and mining on an empty wheel; drowning sink speed; gathering costs; containers lost per death and its notice.
- Twenty-two player attributes covering movement drains, idle regeneration and the new combat costs, for packs that want finer control.
- The wheel displays the cost of the action just paid.

## ⌨️ Commands

Both commands require permission level 2 and act on the item in your main hand. Changes are written and applied immediately, with no restart.

- `/betterparagliders additem <melee|ranged|shield> [cost]` — adds the held item to that category's cost list. Omitting the cost uses the category default.
- `/betterparagliders removeitem <melee|ranged|shield>` — removes the held item from every cost list.
- `/bp` is a shorthand for `/betterparagliders`.

## 🗂️ Configuration

- One file only: `config/betterparagliders-server.toml`, the same path for single-player and dedicated servers. Every option carries a bilingual comment.
- Saving the file applies the change immediately, with no game or server restart.
- It holds the three base costs, the per-item lists, the recovery pause, the refusal switches, the drowning sink speed, the gathering costs and the death penalty settings.
- The commands above edit the same file, so manual edits and in-game edits do not conflict.

## 📝 Notes

- Creative and spectator players pay nothing.
- Since the wheel now funds everything, the base mod's depleted lock clears as soon as any stamina returns instead of waiting for a threshold. One point is enough to act again, which shortens the original post-depletion penalty window.

## 📦 Requirements

Paraglider and Better Combat, both required.

## 🐛 Feedback

Bug reports and suggestions are welcome in the issue tracker. Please include the situation that triggers the problem and the related log lines.

---

# Better Paragliders 中文介绍

**耐力圈从此为你的每个动作付费。**

这是 Paraglider 的附属模组，把耐力从移动延伸到战斗、采集与水下。每一次挥砍、射击、格挡、挖掘和游泳都从同一个耐力圈中扣除，数值以主模组的疾跑消耗为基准——疾跑 1 秒消耗 200 点，默认满圈为 1000 点——因此下面每个数字都可以一眼换算。

## ⚔️ 战斗

- **近战：每次挥砍 200 点**（等于疾跑 1 秒）。挥砍动作做出来就扣费，不看是否命中，空挥同样消耗。
- **远程：每次射击 400 点**（等于疾跑 2 秒）。弓、弩与任何投射武器；蓄力完成的三叉戟投掷计入，快速点击不计。
- **盾牌：每次成功格挡 200 点**。只有真正挡下伤害的那一次扣费。
- 每次动作固定扣费，与武器伤害、附魔、命中目标数量无关。
- 耐力够付，动作就一定能做出来，随后短暂停止恢复（默认 2 秒）。
- 耐力不够付，动作会被拒绝：挥砍不会起手，拉开的弓与举起的盾会放下，蓄力中的弩被打断（已装填的不受影响）。付不起的格挡仍然会保护你，但不扣耐力，下一次举盾会被打断。以上拒绝均可单独关闭。

## ⛏️ 采集

- 挖掘、砍伐与耕地消耗耐力，价格按方块硬度缩放，以原木为基准。
- 费用在挖掘过程中逐步扣除，而不是在破坏瞬间一次性收取。
- 无硬度方块（草丛、花、火把）免费；不使用正确工具挖掘时，消耗翻倍。
- 锄地、去皮、铲路等改造动作按每块固定价格扣费。
- 连锁挖掘按破坏的方块逐块计费。
- 耐力耗尽时，方块无法被破坏。

## 🌊 水下

- 在水里耗尽耐力不再是小事：你会失去转向与浮力，匀速下沉（默认约每秒 2 格），氧气消耗变为三倍；氧气见底后，画面随生命值下降逐渐变暗，按住跳跃也无法上浮。
- 恢复方式是踩水：在仍有耐力时按住跳跃原地漂浮，耐力以静止恢复速度的一半回升。

## 💀 死亡惩罚

- 每次死亡失去 1 个心之容器（可配置）。1 个心之容器 = 1 颗额外红心 = 2 点最大生命值。
- 只扣除 Paraglider 心之容器提供的额外生命上限，扣到 0 个为止，不会动用基础的 10 颗心；容器为 0 时死亡不再扣减。
- 不死图腾一类的免死效果、创造模式与旁观模式均不触发；损失会以聊天消息告知。
- 容器可按原有方式重新获取。

## 🎛️ 配置

全部为服务端配置：

- 三类战斗动作的基础消耗，以及动作后的恢复等待时间。
- 每个类别都支持按物品单独定价。被列入远程或盾牌的物品，即使本身不是弓或盾牌也按该类处理；管理员可用一条游戏内指令登记主手物品。
- 耐力不足时是否拒绝攻击、物品使用与挖掘的开关；溺水下沉速度；采集消耗；每次死亡损失的心之容器数量与提示。
- 22 个玩家属性，覆盖移动消耗、静止恢复与新增的战斗消耗，便于整合包精细调整。
- 耐力圈会显示刚刚支付的消耗。

## ⌨️ 指令

两条指令均需权限等级 2（OP），作用于主手物品，执行后立即写入并生效，无需重启。

- `/betterparagliders additem <melee|ranged|shield> [消耗]` —— 把主手物品写入对应类别的消耗表；省略消耗时使用该类别的默认值。
- `/betterparagliders removeitem <melee|ranged|shield>` —— 把主手物品从所有消耗表中移除。
- `/bp` 是 `/betterparagliders` 的简写。

## 🗂️ 配置文件

- 只有一个配置文件：`config/betterparagliders-server.toml`，单人存档与专用服务器路径相同，每一项都带中英双语注释。
- 保存文件立即生效，无需重启游戏或服务器。
- 内容包含三类基础消耗、按物品覆盖表、恢复等待时间、拒绝开关、溺水下沉速度、采集消耗与死亡惩罚。
- 上面的指令修改的是同一个文件，手动编辑与游戏内指令不会互相冲突。

## 📝 说明

- 创造模式与旁观模式不产生任何消耗。
- 由于耐力圈现在为一切动作付费，主模组的「耗尽」锁定改为在耐力回到正值时立即解除，而不再等待阈值；1 点耐力即可再次行动，原有的耗尽惩罚窗口相应缩短。

## 📦 前置

需要 Paraglider 与 Better Combat。

## 🐛 反馈

问题与建议请提交至问题追踪器。提交时请附上触发问题的场景与相关日志。

# gunsrpg-1201 移植待办



## 已完成（近期）



- [x] 减少类属性天赋表述/效果/50% 上限

- [x] 升级 +2 技能点 / +3 属性点

- [x] 空投间隔天赋全服叠加（定期空投保持简化版）

- [x] 生存 I–V 分色图标、肌肉猛男 2–10

- [x] **武器扩展** — `WeaponExtensionService` 接入弹匣/换弹/散布/卡弹/消音伤害/爆头

- [x] **缺失枪械** — UMP45、HK416、AUG、SKS、Mk14（弩/诸葛连弩暂缓）

- [x] **属性天赋** — bow/silent/loud/noise/melee_cooldown（like_a_cat 为禁用主动技能）

- [x] **采矿技能** — mother_lode、grave_digger、heavy_pickaxe、sharp_axe、acrobatics

- [x] **Guns RPG 医疗品** — 绷带/石膏/止血钳/疫苗等 + debuff 治愈映射

- [x] **手雷** — 普通/大型/碰炸三型 + 榴弹兵技能加成

- [x] **god_help_us** — 紧急个人空投 + 技能树「使用技能」按钮

- [x] **血月** — 每 15 日夜间、64 格僵尸仇恨、刷怪替换、血月傀儡/火箭天使、`/gunsrpg bloodmoon start`

- [x] **MMT 弹药（枪械台）** — T3–T0 全材质 × 六口径；马格南配方/伤害+25%；造弹大师 I–V；弹壳无序合成；弹头染色；口径后缀解析修复
- [ ] **MMT/弹药进游戏验收** — JEI 配方、tooltip 伤害梯队、造弹产量、铜弹、口径行（见根目录 `TODO.md` 火器工业条目）

- [x] 酒保/任务/市长/水晶站/铁匠等已从技能树排除



## 进行中 / 后续



- [x] **烹饪台** — 方块/菜单/配方类型/JEI + 特色食物 + 熔炉中间品

- [x] **维修站** — GUI/菜单已接

- [x] **医疗站（Medstation）** — 方块/GUI/配方/止血剂延缓出血/按已损失生命%回血/Buff叠级

- [ ] **弩 / 诸葛连弩** — 暂缓

- [x] **血月禁睡 + 破门** — `BloodmoonSleepHandler`、`bloodmoon_door_opening` 标签 + `OpenDoorWithoutClosingGoal`

- [ ] **血月客户端** — 氛围粒子/循环音、血月 HUD（红月遮罩+红雾已做，见 `docs/gunsrpg_bloodmoon.md`）



## 枪械后端：TaCZ 替换 CGM（推荐架构）

> 玩法留在 **gunsrpg**，枪模/射击用 **TaCZ**。见 `docs/gunsrpg_tacz_architecture.md`

- [x] 研究：`docs/tacz_akm_research.md`
- [x] CGM 栈备份/恢复：`tools/backup-gun-stack.ps1` · `restore-gun-stack.ps1`
- [x] 整合包装 TaCZ（T0）
- [x] `tacz_backend.json` + wf_key→gunId + 解锁发枪 + 击杀涨级（T1）
- [x] KubeJS 伤害接 TaCZ + 材料弹桥接（T2）
- [x] TaCZ 武器扩展 / 爆头天赋 / 磨损 / CGM 附魔桥接（`TaczPerkBridge`）
- [ ] 自定义 gunpack 数值完全贴合 `FirearmRegistry`（可选 P2）

## 空投（后续调）



- [ ] 战利品表细化、锁销、信号弹动画 — **当前简化版可用，暂不扩展**



## 明确不做 / 用 FTB 替代



- ~~酒保 Bartender、任务、市长~~ → FTB Quests

- ~~宝藏猎人、陷阱、伐木工~~（已禁用）

- ~~水晶站 / 铁匠 / 锤子 / 烈焰粉 / 矿物学家~~

- ~~铁伙伴、为我复仇、战争机器~~ 等主动/召唤类（除 god_help_us）


# Guns RPG（1.20.1 Forge 移植版）

[English](README.md) | **简体中文**

[Guns RPG](https://github.com/Toma1O6/Guns-RPG) 的 **Minecraft 1.20.1 Forge** 移植分支（原版为 Toma1O6 的 **1.16.5** 模组）。  
在保留技能树与生存 RPG 框架的基础上，实现了自研枪械系统、可选 TaCZ 联调、持枪怪物与血月等整合包向内容。

| 项 | 值 |
|---|---|
| **Mod ID** | `gunsrpg` |
| **版本** | `1.20.1-0.1.0-port` |
| **Minecraft** | 1.20.1 |
| **Forge** | 47.x（在 47.4.x 上测试） |
| **上游仓库** | [Toma1O6/Guns-RPG](https://github.com/Toma1O6/Guns-RPG)（`1.16.5` 分支） |

> 当前为**早期移植版本**：核心玩法可玩，部分天赋、怪物持枪渲染与边缘平衡仍在完善。欢迎向上游提交 PR。

---

## 功能概览

### 成长与技能树

- **技能树界面** — 默认 **O** 键打开（`SkillTreeScreen`）：技能、武器扩展、天赋
- 从 `config/gunsrpg/port_from_gunsrpg/` 加载技能数据（300+ 节点、50+ 天赋）
- 中英文技能名与说明：`assets/gunsrpg/lang/*_skills.json`
- **账号等级**与**武器等级**：枪械击杀推进（`leveling_strategy.json`）
- **新手礼包**：首次进世界发放（`config/gunsrpg/starter_kit.json`）
- 主动技能（如 **G** 键紧急空投，需解锁对应天赋）

### 枪械与制造

- **自研 `FirearmItem` 枪械系统**：BEWLR 模型、装填、卡弹、开火模式、配件、耐久
- **枪械台**（`gunsrpg:gunsmith_table`）：零件、多级弹药、骨粉、装配配方
- **维修台**、**医疗台**、**烹饪台**
- 手雷、榴弹发射器、火箭筒
- 可选 **TaCZ** 射击后端（`config/gunsrpg/tacz_backend.json`），安装 TaCZ 后可映射部分武器
- **JEI** 配方展示（可选）

### 生存系统

- **Debuff**：出血、骨折、中毒、感染（`debuff_config.json`）；右上角 HUD；技能/天赋提供抗性
- **空投**补给（可配置战利品表）
- **血月**世界事件及专属怪物、客户端特效
- **枪声引怪**：附近怪物对枪声做出反应（可配置）

### 敌对生物

- **僵尸枪手**（Zombie Gunner）— 持枪射击 AI
- **掷弹骷髅**（Explosive Skeleton）— 投掷爆炸物
- **血月傀儡**、**火箭天使** — 血月 Boss
- 自然刷怪替换与难度缩放：`mob_spawn.json`、`gunner_loadout.json`、`weapon_levels.json`（可选 L2 Hostility 联动）

### 兼容模组

| 模组 | 是否必需 | 说明 |
|------|----------|------|
| **Configuration** | 必需 | 原版硬依赖，配置与同步框架 |
| **TaCZ** | 可选 | 备用射击后端与武器映射 |
| **Sophisticated Backpacks** | 可选 | 装填时从背包取弹 |
| **JEI** | 可选 | 配方查看 |

---

## 环境要求

- **Minecraft 1.20.1** + **Forge 47+**
- **[Configuration](https://www.curseforge.com/minecraft/mc-mods/configuration)**（必装）
- 构建需 **JDK 17**

---

## 构建与部署

### 独立实例

```powershell
git clone https://github.com/Saitoseason/Guns-RPG.git
cd Guns-RPG
git checkout 1.20.1

.\gradlew.bat build
```

产物：`build/libs/gunsrpg-1.20.1-0.1.0-port.jar`  
与 **Configuration** 一并放入 `mods/`。

### 极限挑战 · 无暇赴死整合包内开发

```powershell
cd gunsrpg-1201
.\gradlew.bat build
.\tools\deploy.ps1          # 复制最新 jar 到整合包 mods/
.\tools\enable-mod.ps1      # 启用 .disabled 的 jar，或自动构建部署
.\tools\disable-mod.ps1     # 临时禁用（不删除）
```

游戏运行时配置目录：`<实例>/config/gunsrpg/`。  
整合包已附带完整技能数据：`config/gunsrpg/port_from_gunsrpg/`。

---

## 配置文件

均在 **`config/gunsrpg/`** 下：

| 文件 / 目录 | 用途 |
|-------------|------|
| `port_from_gunsrpg/` | 技能节点、天赋、升级策略、武器映射 |
| `starter_kit.json` | 新手礼包 |
| `debuff_config.json` | Debuff 数值 |
| `mob_spawn.json` | 持枪怪刷怪与缩放 |
| `gunner_loadout.json` | 枪手 / 掷弹手装备 |
| `weapon_levels.json` | 怪物武器等级表 |
| `tacz_backend.json` | 是否启用 TaCZ 及映射表 |
| `airdrop.json` | 空投规则与战利品 |
| `world.json` | 血月、枪声引怪 |
| `ammo_materials.json` | 弹药材料分级 |
| `weapon_caliber_overrides.json` | 口径覆盖 |

游戏内重载（OP）：`/gunsrpg reload`

---

## 按键绑定

| 按键 | 功能 |
|------|------|
| **O** | 技能树 |
| **R** | 装填 |
| **U** | 解除卡弹 |
| **B** | 切换开火模式 |
| **N** / **M** | 准星颜色 / 准星形状 |
| **G** | 紧急空投（需天赋） |

可在 **选项 → 控制 → Guns RPG** 中改键。

---

## 命令

均以 `/gunsrpg` 为前缀（需 OP 的已标注）。

| 命令 | 说明 |
|------|------|
| `/gunsrpg status` | 技能库加载状态 |
| `/gunsrpg reload` | 重载配置（OP 2） |
| `/gunsrpg bootstrap` | 重新发放新手礼包（OP 2） |
| `/gunsrpg points <n>` | 增加技能点（OP 2） |
| `/gunsrpg level <n>` | 设置火器账号等级（OP 2） |
| `/gunsrpg unlock <技能id>` | 解锁技能（OP 2） |
| `/gunsrpg give gun <weapon_key>` | 给予枪械（OP 2） |
| `/gunsrpg give spawn_egg <mob>` | 刷怪蛋：`zombie_gunner`、`explosive_skeleton`、`bloodmoon_golem`、`rocket_angel` |
| `/gunsrpg airdrop spawn` | 强制空投（OP 2，主世界） |
| `/gunsrpg bloodmoon start\|stop` | 强制血月（OP 2） |
| `/gunsrpg debuff status\|clear\|apply ...` | Debuff 调试（OP 2） |

---

## 游戏内快速测试

```mcfunction
/gunsrpg give spawn_egg zombie_gunner
/gunsrpg give spawn_egg explosive_skeleton
/gunsrpg give gun m1911
/gunsrpg bloodmoon start
```

每次替换 jar 后请**完全重启游戏**，不支持热替换。

---

## 目录结构

```
gunsrpg-1201/
├── src/main/java/com/wf/firearms/   # 模组源码
├── src/main/resources/assets/gunsrpg/
├── tools/                           # 部署、图标导入、数据脚本
├── reference/                       # 只读参考资源（如 TaCZ AK）
└── build/libs/                      # 构建产物
```

技能树网格布局移植自上游 `Tree.java` / `SkillTrees.java`，对应本仓库 `client/gui/layout/GunsRpgTree.java`、`GunsRpgSkillTrees.java`。

---

## 参与贡献与上游 PR

1. Fork [Toma1O6/Guns-RPG](https://github.com/Toma1O6/Guns-RPG)
2. 在分支 **`1.20.1`** 上开发
3. 向 `Toma1O6/Guns-RPG:1.20.1` 提交 PR，附构建步骤与测试说明

提交信息建议清晰可读（Conventional Commits 风格即可）。  
原作者 Toma1O6 欢迎社区移植与新版本 PR。

---

## 致谢

- **Guns RPG（1.16.5）** — [Toma1O6](https://github.com/Toma1O6/Guns-RPG)
- **1.20.1 移植** — Saitoseason / 极限挑战 · 无暇赴死 开发组
- **Configuration** — Toma1O6
- 技能树 UI 算法与设计 — 原版 Guns RPG 代码库
- 可选 **TaCZ** 联调 — [Timeless and Classics Zero](https://www.curseforge.com/minecraft/mc-mods/timeless-and-classics-zero)

## 许可

遵循上游 Guns RPG 项目约定，详见 `gradle.properties`（`mod_license`）及原仓库说明。

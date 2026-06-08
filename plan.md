# Plan — AI-Assisted Honor of Kings Information Management System

> 作者：TODO(HUMAN)
> 日期：2026年6月
> 课程：Java OOP Coursework 2026

---

## 1. Project Goal（项目目标）

本项目是一个 **基于控制台的王者荣耀信息管理系统**。系统用于管理游戏中的玩家、英雄、装备、战队和比赛记录，支持查询、统计、排行、数据管理等操作。

**用户角色**：
- **Admin（管理员）**：可以增删改所有数据
- **Player（普通玩家）**：可以查看公开信息和自己的数据，编辑有限的个人信息

**运行方式**：命令行菜单驱动，用户在控制台中输入数字选择功能。

---

## 2. Requirement Analysis（需求分析）

### 2.1 Player Lookup（玩家查询）
- 按 ID 或名字搜索玩家
- 展示：ID、名字、战队、等级、胜率、拥有的英雄及装备

### 2.2 Team Overview（战队概览）
- 按 ID 或名字搜索战队
- 展示：战队名、成员列表、平均等级、总比赛数、胜率、最强队员

### 2.3 Hero Details（英雄详情）
- 按名字搜索英雄
- 展示：名字、类型、基础属性、可用装备、拥有该英雄的玩家

### 2.4 Equipment Statistics（装备统计）
- 按使用次数或胜率贡献对装备排序
- 展示排名公式说明

### 2.5 Match History（比赛记录）
- 查看玩家或战队的最近 N 场比赛
- 展示：对手、日期、结果、英雄选择

### 2.6 Leaderboard（排行榜）
- 按胜率、等级、场次或自定义分数排序
- 展示前 X 名，说明平局处理方式

### 2.7 Data Management（数据管理）
- Admin：添加、删除、修改玩家/英雄/装备/战队/比赛
- Player：查看自己的信息、编辑有限的个人信息

### 2.8 Authentication（身份认证）
- 登录/登出系统
- Admin 账号和 Player 账号分别登录，权限不同

---

## 3. Java Concepts Used（使用的 Java 概念）

| 概念 | 使用位置 | 说明 |
|------|---------|------|
| **继承 (Inheritance)** | Person → Player, Person → Admin | 抽象父类 Person 包含 id, name, password 公共属性 |
| **接口 (Interface)** | Identifiable, Reportable | 统一标识和报告行为，体现多态 |
| **封装 (Encapsulation)** | 所有模型类 | 字段 private，通过 getter/setter 访问 |
| **多态 (Polymorphism)** | `Person.displayInfo()`, `Identifiable` | 同一方法在不同子类有不同行为 |
| **集合 (Collections)** | GameDataManager | 用 HashMap 存储数据（id→对象），ArrayList 存储列表 |
| **异常处理 (Exception Handling)** | 各 Service 类 | 处理重复ID、找不到记录、无效输入、文件错误 |
| **文件 I/O (File I/O)** | FileStorageService | CSV 文件保存和加载数据 |
| **枚举 (Enum)** | HeroType, EquipmentType, MatchResult | 英雄类型、装备类型、比赛结果 |

---

## 4. Class Design（类设计）

### 4.1 继承结构

```
Person (abstract)
├── id, name, passwordHash
├── abstract displayInfo()
├── checkPassword()
│
├── Player extends Person
│   ├── level, rankScore, totalMatches, wins
│   ├── heroIdList (List<Integer>)
│   ├── teamId (int, -1 = 无战队)
│   ├── getWinRate()
│   └── incrementMatch(boolean isWin)
│
└── Admin extends Person
    ├── role (String)
    └── lastLoginTime
```

### 4.2 独立实体类

**Hero（英雄）**
```
id, name, heroType (HeroType枚举), difficulty(1-10)
recommendedEquipmentIds (List<Integer>)
description (简介)
```

**Equipment（装备）**
```
id, name, equipType (EquipmentType枚举), price
attributeMap (Map<String, Integer> 属性键值对)
usageCount (被推荐次数，用于排名)
```

**Team（战队）**
```
id, name, createdDate
playerIds (List<Integer>)
matchRecordIds (List<Integer>)
```

**MatchRecord（比赛记录）**
```
id, teamAId, teamBId, result (MatchResult枚举)
winningTeamId, duration(秒), matchTime
```
> 比赛记录创建后不可修改，保证数据真实性。

### 4.3 关键设计决策：用 ID 引用代替对象引用

所有实体之间通过 **ID + HashMap 查找** 来关联，而不是直接持有对象引用。例如：
- Player 存储 `heroIdList`（英雄ID列表），而不是 `List<Hero>` 对象
- Team 存储 `playerIds`，而不是 `List<Player>` 对象

**好处**：
- 避免循环引用（Player → Team → Player）
- 数据只有一份，修改时不会出现不一致
- 文件序列化更简单

### 4.4 接口设计

**Identifiable（可标识）**
```java
public interface Identifiable {
    int getId();
    String getName();
}
```
实现类：Person(及子类), Hero, Equipment, Team

**Reportable（可报告）**
```java
public interface Reportable {
    String generateReport();
}
```
实现类：Player, Team, MatchRecord

### 4.5 枚举设计

| 枚举 | 值 | 用途 |
|------|-----|------|
| `HeroType` | TANK, WARRIOR, ASSASSIN, MAGE, MARKSMAN, SUPPORT | 英雄分类 |
| `EquipmentType` | ATTACK, DEFENSE, MOVEMENT, MAGIC, JUNGLE, SUPPORT | 装备分类 |
| `MatchResult` | TEAM_A_WIN, TEAM_B_WIN, DRAW | 比赛结果 |

### 4.6 Service 层设计

| Service | 职责 | 核心方法 |
|---------|------|---------|
| `GameDataManager` | 数据中心（单例），管理所有数据 | 增删改查各实体，ID自增管理 |
| `AuthenticationService` | 登录认证 | `login(id, password)` |
| `SearchService` | 搜索功能 | `searchPlayer()`, `searchTeam()`, `searchHero()` |
| `RankingService` | 排行榜计算 | `getWinRateRanking()`, `getEquipmentRanking()` |
| `AdminDataService` | 管理员数据操作 | `addHero()`, `deletePlayer()`, `updateTeam()` |
| `FileStorageService` | 文件读写 | `saveAll()`, `loadAll()` |

### 4.7 Util 层设计

| Util | 职责 |
|------|------|
| `InputHelper` | 控制台输入读取 + 校验 |
| `DataInitializer` | 创建初始硬编码数据集 |

---

## 5. UML Draft（UML 草图）

```
┌──────────────────────────────────────────────────────────┐
│                     <<interface>>                        │
│                     Identifiable                         │
│  + getId(): int                                          │
│  + getName(): String                                     │
└──────────────────────────────────────────────────────────┘
                          △
        ┌─────────────────┼──────────────┬────────────────┐
        │                 │              │                │
        │            ┌────┴────┐   ┌─────┴──────┐   ┌────┴─────┐
        │            │  Hero   │   │ Equipment  │   │   Team   │
        │            └─────────┘   └────────────┘   └──────────┘

┌───────┴──────────┐
│  Person (abstract)│
│  - id: int       │
│  - name: String  │
│  - passwordHash  │
│  + displayInfo() │ ◄── abstract
└──────┬───────────┘
       │
  ┌────┴──────────────┐
  │                   │
┌─┴──────┐       ┌────┴─────┐
│ Player │       │  Admin   │
│ -level │       │ -role    │
│ -wins  │       └──────────┘
│ -teamId│
│ -heroId│
│  List  │
└───┬────┘
    │ "has-many" via heroIdList
    │
    ▽
  Hero ◄──── "recommends" (ID list) ──── Equipment
    │
    │ "belongs to" via teamId
    ▽
  Team ◄──── "participates in" via matchRecordIds ──── MatchRecord

        ┌──────────────────────┐
        │  GameDataManager     │ (单例)
        │  -playerMap: HashMap │
        │  -heroMap: HashMap   │
        │  -teamMap: HashMap   │
        │  -equipmentMap: ...  │
        │  -matchMap: ...      │
        └──────┬───────────────┘
               │ 被调用
    ┌──────────┼──────────┬────────────┐
    │          │          │            │
┌───┴───┐ ┌───┴───┐ ┌───┴───┐ ┌──────┴───────┐
│Search │ │Ranking│ │Auth   │ │AdminData     │
│Service│ │Service│ │Service│ │Service       │
└───────┘ └───────┘ └───────┘ └──────┬───────┘
                                     │ 调用
                            ┌────────┴───────┐
                            │ FileStorage    │
                            │ Service        │
                            └────────────────┘
```

---

## 6. Data Design（数据设计）

### 6.1 初始数据集

| 数据类型 | 最小数量 | 实际计划 |
|---------|---------|---------|
| 战队 (Team) | 3 | 3 支 |
| 玩家 (Player) | 10 | 12 名（每队 4-5 人 + 散人） |
| 英雄 (Hero) | 15 | 15 名 |
| 装备 (Equipment) | 20 | 20 件 |
| 比赛记录 (MatchRecord) | 10 | 10 条 |

### 6.2 数据构建顺序
```
1. Equipment (20件) ── 无依赖
2. Hero (15名)     ── 依赖 Equipment ID
3. Team (3支)       ── 无依赖
4. Player (12名)   ── 依赖 Hero ID, Team ID
5. 填充 Team.playerIds ── 依赖 Player ID
6. MatchRecord (10条)  ── 依赖 Team ID
```

### 6.3 文件存储格式（CSV）

**players.csv**
```
id,name,passwordHash,level,rankScore,totalMatches,wins,teamId,heroIds
1,张三,48690,15,1500,100,60,1,1;2;3;4
```
多值字段（如 heroIds）用 `;` 分隔。

---

## 7. AI Usage Plan（AI 使用计划）

### 7.1 三位 Agent 角色

| Agent | 职责 | 用于 |
|-------|------|------|
| **Architect Agent** | 类设计、UML、模块规划 | Stage 1-2 |
| **Implementation Agent** | 逐步实现类和方法 | Stage 3-5 |
| **Testing/Reviewer Agent** | 代码审查、Bug发现、测试用例 | Stage 6-7 |

### 7.2 AI 辅助范围

AI 可以帮助：
- 设计类结构和关系
- 生成小段代码示例
- 调试和修复 Bug
- 审查代码质量
- 生成测试用例
- 改善文档

AI 不能替代：
- 我对代码的理解（每条代码我都要能解释）
- Git 提交和版本管理
- 最终测试验证
- 反思和文档撰写

---

## 8. Prompt Strategy（提示词策略）

### 8.1 写好提示词的原则
- **具体**：写清楚要生成什么，给出上下文和约束
- **分步**：一次只问一个问题，不要问整个项目
- **检查**：AI 生成的代码必须编译运行通过

### 8.2 提示词质量对比

| 差 | 好 |
|----|----|
| "帮我写项目" | "帮我设计一个包含继承和接口的类结构，显示类之间的关系" |
| "修一下Bug" | "PlayerLookup 方法在搜索不存在的ID时崩溃了，请找出原因并修复" |
| "这个好吗？" | "检查这个类是否遵循封装原则，有没有潜在的 NullPointer 风险？" |

### 8.3 检查清单
每次收到 AI 代码后，我必须：
1. 读懂每一行代码
2. 编译成功
3. 运行测试
4. 确认符合需求
5. 记录到 prompts.md

---

## 9. Development Timeline（开发时间线）

| Stage | 内容 | 预计工作量 | 提交前缀 |
|-------|------|-----------|---------|
| Stage 0 | 项目初始化，目录结构，Git 初始化 | 1次 | [Human] |
| Stage 1 | 需求分析，plan.md 定稿 | 1次 | [Human] |
| Stage 2 | 类设计确认，UML 草图 | 1-2次 | [AI-Architect] |
| Stage 3 | 实现 Model 类（枚举、接口、模型类） | 2-3次 | [AI-Implementation] |
| Stage 4 | 实现 Service 层 + Main 菜单 | 3-4次 | [AI-Implementation] |
| Stage 5 | 身份认证 + 权限控制 | 1-2次 | [AI-Implementation] |
| Stage 6 | 文件 I/O + 排行榜 | 2次 | [AI-Implementation] |
| Stage 7 | 测试、Bug修复、代码审查 | 2-3次 | [AI-Review]/[Fix] |
| Stage 8 | 文档完善、反思、最终提交 | 1-2次 | [Docs]/[Human] |

> 预计总提交数：≥ 12 次，覆盖所有 AI 和 Human 提交前缀。

---

## 10. Testing Plan（测试计划）

| 测试编号 | 功能 | 测试内容 |
|---------|------|---------|
| T01 | 登录 | Admin 用正确密码登录，检查权限 |
| T02 | 登录 | Player 用正确密码登录，检查权限 |
| T03 | 登录 | 错误密码应拒绝并提示 |
| T04 | 玩家查询 | 按名字搜索存在的玩家 |
| T05 | 玩家查询 | 搜索不存在的名字应提示 |
| T06 | 战队概览 | 按名字查看战队，验证成员列表 |
| T07 | 英雄详情 | 按名字查看英雄，验证装备列表 |
| T08 | 装备统计 | 按使用次数排名，验证排序正确 |
| T09 | 比赛记录 | 查看某玩家的最近5场比赛 |
| T10 | 排行榜 | 按胜率排名，验证平局处理 |
| T11 | 数据管理 | Admin 添加新英雄，验证数据持久 |
| T12 | 数据管理 | Admin 删除玩家，验证关联数据清理 |
| T13 | 文件I/O | 重启程序后数据仍在 |
| T14 | 异常处理 | 输入非法字符时系统不崩溃 |

---

## 11. Risk Analysis（风险分析）

| 风险 | 影响 | 应对措施 |
|------|------|---------|
| AI 生成的代码有 Bug | 系统不稳定 | 每段代码都手动测试 |
| 类设计太复杂 | 超出初学者能力 | 保持简洁，按检查点确认 |
| 数据不一致（如删除战队后玩家 teamId 遗留） | 数据显示错误 | 在 AdminDataService 中统一处理级联更新 |
| 文件 I/O 路径问题 | 无法读取数据 | 使用相对路径，Windows/Linux 兼容 |
| AI 生成代码不可读 | 无法解释 | 要求 AI 添加中文注释，每行都读懂 |
| 时间不够 | 功能不完备 | 按优先级：核心查询→认证→管理→文件I/O→额外功能 |
| Git 历史不清晰 | 扣分 | 每次提交只做一件事，遵守提交前缀规范 |

---

## 12. Final Reflection Placeholder（最终反思占位）

> 此部分将在项目全部完成后填写，对应 `ai/reflection.md` 的 10 个问题。
> 当前状态：TODO(HUMAN)

---

## 附录：包结构

```
HonorOfKingsIMS/
├── src/
│   ├── Main.java                     # 程序入口 + 菜单路由
│   ├── model/
│   │   ├── Person.java               # 抽象父类
│   │   ├── Player.java               # 玩家
│   │   ├── Admin.java                # 管理员
│   │   ├── Hero.java                 # 英雄
│   │   ├── Equipment.java            # 装备
│   │   ├── Team.java                 # 战队
│   │   ├── MatchRecord.java          # 比赛记录
│   │   ├── enums/
│   │   │   ├── HeroType.java
│   │   │   ├── EquipmentType.java
│   │   │   └── MatchResult.java
│   │   └── interfaces/
│   │       ├── Identifiable.java
│   │       └── Reportable.java
│   ├── service/
│   │   ├── GameDataManager.java
│   │   ├── AuthenticationService.java
│   │   ├── SearchService.java
│   │   ├── RankingService.java
│   │   ├── AdminDataService.java
│   │   └── FileStorageService.java
│   └── util/
│       ├── InputHelper.java
│       └── DataInitializer.java
├── docs/
│   ├── design.md
│   └── test-cases.md
├── ai/
│   ├── prompts.md
│   ├── agent-log.md
│   └── reflection.md
├── plan.md
├── README.md
└── git-history.txt
```

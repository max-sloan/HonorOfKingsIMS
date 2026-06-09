# Design Document — Honor of Kings IMS

> 本文档记录所有类的详细设计，包括属性类型、方法签名和类间关系。
> 这是写 Java 代码的蓝图。

---

## 1. 枚举 (Enums)

### 1.1 HeroType

```java
public enum HeroType {
    TANK("坦克"),
    WARRIOR("战士"),
    ASSASSIN("刺客"),
    MAGE("法师"),
    MARKSMAN("射手"),
    SUPPORT("辅助");

    private final String displayName;
    // 构造器 + getDisplayName()
}
```

### 1.2 EquipmentType

```java
public enum EquipmentType {
    ATTACK("攻击装"),
    DEFENSE("防御装"),
    MOVEMENT("移动装"),
    MAGIC("法术装"),
    JUNGLE("打野装"),
    SUPPORT("辅助装");

    private final String displayName;
    // 构造器 + getDisplayName()
}
```

### 1.3 MatchResult

```java
public enum MatchResult {
    TEAM_A_WIN("队伍A获胜"),
    TEAM_B_WIN("队伍B获胜"),
    DRAW("平局");

    private final String displayName;
    // 构造器 + getDisplayName()
}
```

---

## 2. 接口 (Interfaces)

### 2.1 Identifiable

```java
public interface Identifiable {
    int getId();
    String getName();
}
```

| 实现类 | 理由 |
|--------|------|
| Person (及子类) | 玩家和管理员有 ID 和名字 |
| Hero | 英雄有 ID 和名字 |
| Equipment | 装备有 ID 和名字 |
| Team | 战队有 ID 和名字 |

### 2.2 Reportable

```java
public interface Reportable {
    String generateReport();
}
```

| 实现类 | generateReport() 返回内容 |
|--------|---------------------------|
| Player | "玩家[张三] 等级15 胜率60.0%" |
| Team | "战队[AG超玩会] 共5人 胜率70.0%" |
| MatchRecord | "2024-03-15: AG超玩会 vs QGhappy → AG超玩会获胜" |

---

## 3. 模型类 (Model Classes)

### 3.1 Person（抽象父类）

```
包: src/model/Person.java
类型: public abstract class Person implements Identifiable
```

| 属性 | 类型 | 访问 | 说明 |
|------|------|------|------|
| id | int | private | 唯一编号 |
| name | String | private | 姓名/昵称 |
| passwordHash | int | private | 密码哈希值（用 String.hashCode()） |

| 方法 | 返回 | 说明 |
|------|------|------|
| Person(id, name, password) | 构造器 | 初始化三个属性 |
| getId() | int | 实现 Identifiable |
| getName() | String | 实现 Identifiable |
| setName(name) | void | 修改名字 |
| checkPassword(raw) | boolean | 比较密码是否正确 |
| setPassword(raw) | void | 修改密码 |
| displayInfo() | abstract void | 子类各自实现 |
| toString() | String | 返回 "id + name" |

---

### 3.2 Player（玩家，继承 Person）

```
包: src/model/Player.java
类型: public class Player extends Person implements Reportable
```

| 新增属性 | 类型 | 默认值 | 说明 |
|----------|------|--------|------|
| level | int | 1 | 玩家等级 (1-30) |
| rankScore | int | 1000 | 排位积分 |
| totalMatches | int | 0 | 总比赛场次 |
| wins | int | 0 | 胜场数 |
| teamId | int | -1 | 所属战队ID，-1 表示无战队 |
| heroIdList | List\<Integer\> | new ArrayList<>() | 拥有的英雄ID列表 |

| 新增方法 | 返回 | 说明 |
|----------|------|------|
| getWinRate() | double | 胜率 = wins / totalMatches，0场时返回0 |
| addHero(heroId) | void | 添加英雄 |
| removeHero(heroId) | void | 移除英雄 |
| incrementMatch(isWin) | void | 比赛后 totalMatches++，赢了则 wins++ |
| getLevel() | int | 获取等级 |
| setLevel(level) | void | 设置等级 |
| getTeamId() | int | 获取战队ID |
| setTeamId(teamId) | void | 设置战队ID |
| getHeroIdList() | List\<Integer\> | 获取英雄ID列表 |
| displayInfo() | void | 打印玩家信息（覆盖Person的抽象方法） |
| generateReport() | String | 实现 Reportable：返回 "[名字] Lv[等级] 胜率[%]" |

---

### 3.3 Admin（管理员，继承 Person）

```
包: src/model/Admin.java
类型: public class Admin extends Person
```

| 新增属性 | 类型 | 说明 |
|----------|------|------|
| role | String | 管理角色，如 "超级管理员" |

| 方法 | 返回 | 说明 |
|------|------|------|
| Admin(id, name, password, role) | 构造器 | 初始化 |
| getRole() | String | 获取角色 |
| displayInfo() | void | 覆盖：打印管理员信息 |

---

### 3.4 Hero（英雄）

```
包: src/model/Hero.java
类型: public class Hero implements Identifiable
```

| 属性 | 类型 | 说明 |
|------|------|------|
| id | int | 唯一编号 |
| name | String | 英雄名 |
| heroType | HeroType | 英雄定位 |
| difficulty | int | 难度 (1-10) |
| recommendedEquipIds | List\<Integer\> | 推荐装备ID列表 |
| description | String | 简介 |

| 方法 | 返回 | 说明 |
|------|------|------|
| Hero(id, name, heroType, difficulty, description) | 构造器 | 初始化 |
| getId() | int | 实现 Identifiable |
| getName() | String | 实现 Identifiable |
| getHeroType() | HeroType | 获取类型 |
| getDifficulty() | int | 获取难度 |
| getDescription() | String | 获取简介 |
| addEquipment(equipId) | void | 添加推荐装备 |
| getRecommendedEquipIds() | List\<Integer\> | 获取装备列表 |
| displayInfo() | void | 打印英雄详细信息 |

---

### 3.5 Equipment（装备）

```
包: src/model/Equipment.java
类型: public class Equipment implements Identifiable
```

| 属性 | 类型 | 说明 |
|------|------|------|
| id | int | 唯一编号 |
| name | String | 装备名 |
| equipType | EquipmentType | 装备类型 |
| price | int | 价格（金币） |
| attributeMap | Map\<String, Integer\> | 属性加成，如 {"攻击力": 80, "攻速": 20} |
| usageCount | int | 被推荐次数（用于排名统计） |

| 方法 | 返回 | 说明 |
|------|------|------|
| Equipment(id, name, equipType, price) | 构造器 | 初始化 |
| getId() | int | 实现 Identifiable |
| getName() | String | 实现 Identifiable |
| getEquipType() | EquipmentType | 获取装备类型 |
| getPrice() | int | 获取价格 |
| addAttribute(key, value) | void | 添加一条属性 |
| getAttributeMap() | Map\<String, Integer\> | 获取所有属性 |
| getUsageCount() | int | 获取使用次数 |
| incrementUsage() | void | 使用次数 +1 |
| displayInfo() | void | 打印装备详情 |

---

### 3.6 Team（战队）

```
包: src/model/Team.java
类型: public class Team implements Identifiable, Reportable
```

| 属性 | 类型 | 说明 |
|------|------|------|
| id | int | 唯一编号 |
| name | String | 战队名 |
| createdDate | String | 创建日期（格式 "2024-01-01"） |
| playerIds | List\<Integer\> | 成员ID列表 |
| matchRecordIds | List\<Integer\> | 参与的比赛ID列表 |

| 方法 | 返回 | 说明 |
|------|------|------|
| Team(id, name, createdDate) | 构造器 | 初始化 |
| getId() | int | 实现 Identifiable |
| getName() | String | 实现 Identifiable |
| addPlayer(playerId) | void | 添加成员 |
| removePlayer(playerId) | void | 移除成员 |
| addMatchRecord(matchId) | void | 添加比赛记录 |
| getPlayerIds() | List\<Integer\> | 获取成员列表 |
| getMatchRecordIds() | List\<Integer\> | 获取比赛列表 |
| displayInfo() | void | 打印战队概要 |
| generateReport() | String | 实现 Reportable：返回战队基本信息 |

---

### 3.7 MatchRecord（比赛记录）

```
包: src/model/MatchRecord.java
类型: public class MatchRecord implements Reportable
```

| 属性 | 类型 | 说明 |
|------|------|------|
| id | int | 唯一编号 |
| teamAId | int | 队伍A的ID |
| teamBId | int | 队伍B的ID |
| result | MatchResult | 比赛结果 |
| winningTeamId | int | 获胜队伍ID（平局时为-1） |
| duration | int | 比赛时长（秒） |
| matchTime | String | 比赛时间（格式 "2024-03-15 14:30"） |

**注意**：MatchRecord 不实现 Identifiable，因为它不需要按名字搜索。

| 方法 | 返回 | 说明 |
|------|------|------|
| MatchRecord(...) | 构造器 | 初始化全部字段 |
| getId() | int | 获取编号 |
| getTeamAId() | int | 获取队伍A |
| getTeamBId() | int | 获取队伍B |
| displayInfo() | void | 打印比赛详情 |
| generateReport() | String | 实现 Reportable：返回比赛概要 |

---

## 4. Service 层设计

### 4.1 GameDataManager（数据中心，单例模式）

```
包: src/service/GameDataManager.java
```

**什么是单例模式？**
> 单例（Singleton）是一种设计模式，确保一个类在整个程序中只有一个实例。
> 实现方法：构造器设为 private，对外只通过 `getInstance()` 方法获取唯一实例。
> 第一次调用 getInstance() 时创建对象，之后每次调用都返回同一个对象。

**什么是 HashMap？**
> HashMap 是一种存储"键值对"的集合。格式：`Map<键的类型, 值的类型>`
> - `put(key, value)` — 存入数据
> - `get(key)` — 通过键取出值
> - `containsKey(key)` — 检查是否存在某个键
> - 和 ArrayList 的区别：ArrayList 通过 index（位置编号）查找，HashMap 通过 key（自定义键）查找
> - 例如：`heroMap.get(5)` 直接拿到 ID=5 的英雄，不需要遍历整个列表

| 属性 | 类型 | 说明 |
|------|------|------|
| instance | GameDataManager (static) | 单例实例 |
| playerMap | Map\<Integer, Player\> | ID → Player |
| adminMap | Map\<Integer, Admin\> | ID → Admin |
| heroMap | Map\<Integer, Hero\> | ID → Hero |
| equipmentMap | Map\<Integer, Equipment\> | ID → Equipment |
| teamMap | Map\<Integer, Team\> | ID → Team |
| matchMap | Map\<Integer, MatchRecord\> | ID → MatchRecord |
| nextPlayerId | int | Player 自增ID |
| nextHeroId | int | Hero 自增ID |

| 核心方法 | 说明 |
|----------|------|
| getInstance() | 获取单例 |
| addPlayer(Player) | 添加玩家 |
| findPlayerById(int) | 按ID查找，返回 Player 或 null |
| findPlayersByName(String) | 按名字模糊查找 |
| removePlayer(int) | 删除玩家 |
| (Hero/Equipment/Team/MatchRecord 同理) | |

### 4.2 AuthenticationService

| 方法 | 返回 | 说明 |
|------|------|------|
| login(id, password) | Object | 返回 Player 或 Admin |
| getCurrentUser() | Object | 当前登录用户 |

### 4.3 SearchService

| 方法 | 返回 | 说明 |
|------|------|------|
| searchPlayer(keyword) | List\<Player\> | 按名字搜索 |
| searchTeam(keyword) | List\<Team\> | 按名字搜索 |
| searchHero(keyword) | List\<Hero\> | 按名字搜索 |
| getPlayerDetail(player) | String | 生成完整报告 |

### 4.4 RankingService

| 方法 | 返回 | 说明 |
|------|------|------|
| getWinRateRanking(topN) | List\<Player\> | 按胜率排名 |
| getLevelRanking(topN) | List\<Player\> | 按等级排名 |
| getEquipmentUsageRanking() | List\<Equipment\> | 装备按使用次数排名 |

### 4.5 AdminDataService

| 方法 | 说明 |
|------|------|
| addHero(...) | 添加英雄 |
| deleteHero(id) | 删除英雄（级联清理） |
| addEquipment(...) | 添加装备 |
| deleteEquipment(id) | 删除装备（级联清理） |
| (Player/Team/MatchRecord 同理) | |

### 4.6 FileStorageService

| 方法 | 说明 |
|------|------|
| saveAll(GameDataManager) | 保存全部数据到 CSV |
| loadAll(GameDataManager) | 从 CSV 加载全部数据 |

---

## 5. 数据构建顺序

```
步骤1: 创建 20 件 Equipment（无外部依赖）
步骤2: 创建 15 个 Hero（引用 Equipment ID）
步骤3: 创建 3 个 Team（无外部依赖）
步骤4: 创建 12 个 Player（引用 Hero ID + Team ID）
步骤5: 为 Team 补充 playerIds（此时 Player ID 已确定）
步骤6: 创建 10 条 MatchRecord（引用 Team ID）
```

---

## 6. 菜单结构（Main.java）

```
═══════════════════════════════════════════
  王者荣耀信息管理系统
═══════════════════════════════════════════
1. 登录
0. 退出系统
请选择:
```

登录后，根据角色显示不同菜单：

**Admin 菜单:**
```
═══════════ Admin 菜单 ═══════════
1.  玩家查询
2.  战队概览
3.  英雄详情
4.  装备统计
5.  比赛记录
6.  排行榜
7.  数据管理（增删改）
8.  保存数据到文件
0.  登出
```

**Player 菜单:**
```
═══════════ Player 菜单 ═══════════
1.  查看我的信息
2.  战队概览
3.  英雄详情
4.  装备统计
5.  比赛记录
6.  排行榜
7.  查看我的英雄
8.  编辑个人信息
0.  登出
```

---

## 7. 技术决策总结

| 决策 | 选择 | 理由 |
|------|------|------|
| 数据关联方式 | ID 引用 + HashMap 查找 | 避免循环引用，序列化简单 |
| 文件格式 | CSV（逗号分隔，;分隔多值） | 初学者友好，可读性强 |
| 密码存储 | String.hashCode() | 简单，课程作业足够 |
| GameDataManager | 单例模式 | 全局唯一数据源 |
| 异常处理 | try-catch + 自定义消息 | 练习异常处理概念 |
| 日期格式 | String ("yyyy-MM-dd") | 简单，避免 Java 日期API复杂度 |
| 菜单实现 | while(true) + switch-case | 初学者容易理解 |

---

## 8. UML Class Diagram（类图）

```
┌──────────────────────────────────────────────────────────────┐
│                    <<interface>>                             │
│                    Identifiable                              │
│  + getId(): int                                              │
│  + getName(): String                                         │
└──────────────────────────────────────────────────────────────┘
          △                    △              △            △
          │                    │              │            │
┌─────────┴──────┐   ┌────────┴───┐  ┌───────┴──────┐  ┌──┴─────────┐
│  Person        │   │   Hero     │  │  Equipment   │  │   Team     │
│  (abstract)    │   └────────────┘  └──────────────┘  └────────────┘
│  - id: int     │
│  - name: String│
│  - password    │
│  +displayInfo()│
└───────┬────────┘
        │
   ┌────┴───────────┐
   │                │
┌──┴──────┐  ┌──────┴─────┐
│ Player  │  │   Admin    │
│ -level  │  │  -role     │
│ -wins   │  └────────────┘
│ -teamId │
│ -heroId │
│  List   │
└───┬─────┘
    │ has-many (ID引用)
    ▽
  Hero ─── recommends (ID) ───> Equipment

  Team ─── contains (ID) ───> Player
  Team ─── participates (ID) ───> MatchRecord
```

### 关系说明

| 关系 | 类型 | 说明 |
|------|------|------|
| Person → Player/Admin | 继承 (extends) | "是一个" 关系 |
| Player → Hero | 关联 (ID引用) | "拥有" 关系，一对多 |
| Hero → Equipment | 关联 (ID引用) | "推荐" 关系，一对多 |
| Team → Player | 聚合 (ID引用) | "包含" 关系 |
| Identifiable ← Person/Hero/Equipment/Team | 实现 (implements) | "提供" 关系 |
| Reportable ← Player/Team/MatchRecord | 实现 (implements) | "提供" 关系 |

---

## 9. Data Flow Diagram（数据流图）

```
┌──────────┐     ┌─────────────────────────────┐
│  用户输入  │────>│  Main.java (菜单路由)         │
└──────────┘     └──────────┬──────────────────┘
                            │
              ┌─────────────┼─────────────┐
              ▽             ▽             ▽
        ┌──────────┐ ┌──────────┐ ┌────────────┐
        │  Search  │ │ Ranking  │ │  AdminData │
        │  Service │ │ Service  │ │  Service   │
        └────┬─────┘ └────┬─────┘ └─────┬──────┘
             │            │             │
             └────────────┼─────────────┘
                          ▽
              ┌───────────────────────┐
              │   GameDataManager     │
              │   (单例，HashMap存储)  │
              │  playerMap<Integer,..>│
              │  heroMap<Integer,..>  │
              │  teamMap<Integer,..>  │
              │  equipmentMap<Int,..> │
              │  matchMap<Integer,..> │
              └───────────┬───────────┘
                          │
                          ▽
              ┌───────────────────────┐
              │   FileStorageService  │
              │   CSV 读写            │
              │   data/*.csv          │
              └───────────────────────┘
```

---

## 10. Sequence Diagram（时序图）—— 玩家查询流程

```
用户输入 "梦泪"
     │
     ▼
Main.doPlayerLookup()
     │
     ├─ InputHelper.readNonEmptyString("关键词: ")
     │       │
     │       └─> 返回 "梦泪"
     │
     ├─ searchService.searchPlayers("梦泪")
     │       │
     │       └─> GameDataManager.findPlayersByName("梦泪")
     │               │
     │               ├─ 遍历 playerMap.values()
     │               ├─ 每个 Player.getName() 做 contains("梦泪")
     │               │
     │               └─> 返回 List<Player> [梦泪(ID=1)]
     │
     ├─ 打印: "找到 1 人: 梦泪 Lv.28 胜率62.5%"
     │
     ├─ InputHelper.readIntOrDefault("ID看详情: ", 0)
     │       │
     │       └─> 返回 1
     │
     └─ searchService.getPlayerFullReport(梦泪)
             │
             ├─ 打印: 名字、等级、胜率、战队
             ├─ 遍历 heroIdList [1,2,3]
             │     ├─ dm.findHeroById(1) → 李白 [刺客]
             │     │     └─ 遍历 recommendedEquipIds [1,2,18]
             │     │           ├─ dm.findEquipmentById(1) → 破军
             │     │           ├─ dm.findEquipmentById(2) → 无尽战刃
             │     │           └─ dm.findEquipmentById(18) → 冷静之靴
             │     ├─ dm.findHeroById(2) → 韩信 [刺客]
             │     └─ dm.findHeroById(3) → 孙悟空 [刺客]
             │
             └─> 返回完整报告字符串
```

### 时序图关键点

| 步骤 | 说明 |
|------|------|
| 1. 用户输入 | InputHelper 读取并校验非空 |
| 2. 模糊搜索 | 遍历 HashMap 的 values()，contains() 匹配 |
| 3. ID 查找 | HashMap.get(id) - O(1) 时间复杂度 |
| 4. 级联查询 | 通过 heroIdList → 查 Hero → 通过 recommendedEquipIds → 查 Equipment |
| 5. 报告生成 | SearchService 组装所有信息为字符串 |

---

## 11. Class Responsibility Summary

| Class | Layer | Responsibility | Key Java Concept |
|-------|-------|---------------|-----------------|
| Person | Model | Shared fields for all system users | Abstract class, Encapsulation |
| Player | Model | Game player with stats and hero collection | Inheritance, ID references |
| Admin | Model | Administrator with data management permissions | instanceof, Inheritance |
| Hero | Model | Playable character with type and equipment | Interface implementation |
| Equipment | Model | Item with attribute bonuses (HashMap) | HashMap, Encapsulation |
| Team | Model | Team containing players and match history | Multiple interfaces, Aggregation |
| MatchRecord | Model | Immutable match result record | Immutability, No-setters design |
| GameDataManager | Service | Central data store (Singleton) | Singleton, HashMap, Cascade |
| AuthenticationService | Service | Login/logout with role checking | Polymorphism, instanceof |
| SearchService | Service | Fuzzy search and full report generation | Collections traversal |
| RankingService | Service | Leaderboard sorting with tie-break | Comparator, Lambda |
| AdminDataService | Service | Admin CRUD with cascade updates | Exception handling, Cascade logic |
| FileStorageService | Service | CSV save/load persistence | File I/O, try-with-resources |
| InputHelper | Util | Console input with validation | Static methods, try-catch |
| DataInitializer | Util | Hardcoded dataset creation | Dependency order, Data seeding |
| Main | Entry | Menu routing and user interaction | while-switch loop, Control flow |

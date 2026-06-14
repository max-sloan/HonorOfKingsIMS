# Design Document — Honor of Kings IMS

## 1. Enums

### 1.1 HeroType
TANK, WARRIOR, ASSASSIN, MAGE, MARKSMAN, SUPPORT

### 1.2 EquipmentType
ATTACK, DEFENSE, MOVEMENT, MAGIC, JUNGLE, SUPPORT

### 1.3 MatchResult
TEAM_A_WIN, TEAM_B_WIN, DRAW

---

## 2. Interfaces

### 2.1 Identifiable
`int getId(); String getName();`
Implemented by: Person (and subclasses), Hero, Equipment, Team

### 2.2 Reportable
`String generateReport();`
Implemented by: Player, Team, MatchRecord

---

## 3. Model Classes

### 3.1 Person (abstract)
- Fields: id (int), name (String), password (String)
- Methods: getId(), getName(), setName(), checkPassword(), setPassword(), getPassword(), abstract displayInfo()

### 3.2 Player (extends Person, implements Reportable)
- New fields: level, rankScore, totalMatches, wins, winRate, teamId, teamName, heroIdList (List<Integer>)
- Key methods: getWinRate(), updateWinRate(), incrementMatch(), addHero(), removeHero(), generateReport()

### 3.3 Admin (extends Person)
- New fields: role (String)

### 3.4 Hero (implements Identifiable)
- Fields: id, name, heroType (HeroType), difficulty (1-10), description, recommendedEquipIds (List<Integer>)

### 3.5 Equipment (implements Identifiable)
- Fields: id, name, equipType (EquipmentType), price, attributeMap (Map<String,Integer>), usageCount
- key methods: addAttribute(), incrementUsage()

### 3.6 Team (implements Identifiable, Reportable)
- Fields: id, name, createdDate, playerIds (List<Integer>), matchRecordIds (List<Integer>)

### 3.7 MatchRecord (implements Reportable)
- Fields: id, teamAId, teamBId, result (MatchResult), winningTeamId, duration, matchTime
- Immutable: no setters after creation

---

## 4. Service Layer

### 4.1 GameDataManager (Singleton)
Central data store with 6 HashMaps: playerMap, adminMap, heroMap, equipmentMap, teamMap, matchMap.
All entities linked via ID references + HashMap lookup.

### 4.2 AuthenticationService
login(id, password) returns "Player" or "Admin". Uses instanceof for role checking.

### 4.3 SearchService
Fuzzy search by name, ID lookup, team player lists, full report generation.

### 4.4 RankingService
Leaderboard sorting with Comparator lambdas. Tie-break on secondary metrics.

### 4.5 AdminDataService
CRUD operations with cascade cleanup on delete. Auto-updates player stats on match creation.

### 4.6 FileStorageService
CSV save/load for all 6 data types. dataExists() pre-check before loading.

---

## 5. Data Build Order

```
Step 1: Create 20 Equipment (no dependencies)
Step 2: Create 15 Heroes (reference Equipment IDs)
Step 3: Create 3 Teams (no dependencies)
Step 4: Create 12 Players (reference Hero IDs + Team IDs)
Step 5: Populate Team.playerIds (Player IDs now known)
Step 6: Create 10 MatchRecords (reference Team IDs)
```

---

## 6. Menu Structure

Main Menu: Login | Save | Load | Exit

Admin Menu: Player Lookup | Team Overview | Hero Details | Equipment Stats | Match History | Leaderboard | Data Management | Save Data

Player Menu: My Info | Team Overview | Hero Details | Equipment Stats | Match History | Leaderboard | My Heroes | Edit Profile

---

## 7. Technical Decisions

| Decision | Choice | Reason |
|----------|--------|--------|
| Entity linking | ID references + HashMap lookup | Avoid circular references, simple serialization |
| File format | CSV (comma-delimited, semicolons for multi-value) | Beginner-friendly, human-readable |
| Password storage | Plain text String | Simple, transparent for coursework |
| GameDataManager | Singleton pattern | Single global data source |
| Exception handling | try-catch with custom messages | Practice exception handling concepts |
| Date format | String ("yyyy-MM-dd") | Simple, avoids Java date API complexity |
| Menu implementation | while(true) + switch-case | Easy for beginners to understand |

---

## 8. CSV File Format Specification

> This section defines the CSV storage format used by FileStorageService.
> All CSV files are stored in the `data/` folder under the project root.

### 8.1 Delimiter Rules

CSV (Comma-Separated Values) uses plain text to store tabular data. Since some fields contain
multiple values (e.g., a hero recommends several equipment items), this project uses a
three-level delimiter system:

| Level | Separator | Example | Purpose |
|-------|-----------|---------|---------|
| 1 — Columns | `,` (comma) | `1,Breaker,ATTACK,2950` | Separate fields in one row |
| 2 — Lists | `;` (semicolon) | `1;2;18` | Separate multiple values in one field (e.g., heroIds list) |
| 3 — Key-Value | `:` (colon) | `Attack:180` | Separate attribute key and value in equipment attributes |

First line of every CSV file is a **header row** (column names), skipped when loading.

### 8.2 File Definitions

#### 8.2.1 equipment.csv — 20 records

| Column | Type | Example | Description |
|--------|------|---------|-------------|
| id | int | `1` | Unique equipment ID |
| name | String | `Breaker` | Equipment name |
| equipType | EquipmentType enum | `ATTACK` | One of: ATTACK, DEFENSE, MOVEMENT, MAGIC, JUNGLE, SUPPORT |
| price | int | `2950` | Gold price |
| attributes | String | `Attack:180;Crit Rate:25` | Key-value pairs joined by `;` and `:` |
| usageCount | int | `4` | How many heroes recommend this equipment |

Example row:
```
1,Breaker,ATTACK,2950,Attack:180,4
```

#### 8.2.2 heroes.csv — 15 records

| Column | Type | Example | Description |
|--------|------|---------|-------------|
| id | int | `1` | Unique hero ID |
| name | String | `Li Bai` | Hero name |
| heroType | HeroType enum | `ASSASSIN` | One of: TANK, WARRIOR, ASSASSIN, MAGE, MARKSMAN, SUPPORT |
| difficulty | int | `8` | Difficulty rating 1–10 |
| description | String | `The sword immortal...` | Hero lore text (must not contain commas) |
| equipIds | String | `1;2;18` | Recommended equipment IDs, separated by `;` |

Example row:
```
1,Li Bai,ASSASSIN,8,The sword immortal comes and goes like the wind,1;2;18
```

#### 8.2.3 teams.csv — 3 records

| Column | Type | Example | Description |
|--------|------|---------|-------------|
| id | int | `1` | Unique team ID |
| name | String | `AG` | Team name |
| createdDate | String | `2023-06-01` | Date string (yyyy-MM-dd) |
| playerIds | String | `1;2;3;4` | Member player IDs, separated by `;` |
| matchIds | String | `1;2;3;4;5;6;10` | Match record IDs this team participated in, separated by `;` |

Example row:
```
1,AG,2023-06-01,1;2;3;4,1;2;3;4;5;6;10
```

#### 8.2.4 players.csv — 12 records

| Column | Type | Example | Description |
|--------|------|---------|-------------|
| id | int | `1` | Unique player ID |
| name | String | `MengLei` | Player name |
| password | String | `123` | Login password (plain text) |
| level | int | `28` | Player level 1–30 |
| rankScore | int | `2500` | Rank score |
| totalMatches | int | `8` | Total matches played |
| wins | int | `5` | Matches won |
| winRate | double | `62.5` | Calculated win rate (wins/totalMatches × 100) |
| teamId | int | `1` | Team ID (-1 = no team) |
| teamName | String | `AG` | Cached team name (for display without lookup) |
| heroIds | String | `1;2;3` | Owned hero IDs, separated by `;` |

Example row:
```
1,MengLei,123,28,2500,8,5,62.5,1,AG,1;2;3
```

#### 8.2.5 matches.csv — 10 records

| Column | Type | Example | Description |
|--------|------|---------|-------------|
| id | int | `1` | Unique match ID |
| teamAId | int | `1` | Team A ID |
| teamBId | int | `2` | Team B ID |
| result | MatchResult enum | `TEAM_A_WIN` | One of: TEAM_A_WIN, TEAM_B_WIN, DRAW |
| winningTeamId | int | `1` | Winning team ID (-1 for DRAW) |
| duration | int | `1200` | Match duration in seconds |
| matchTime | String | `2024-01-15 14:30` | Match timestamp (yyyy-MM-dd HH:mm) |

Example row:
```
1,1,2,TEAM_A_WIN,1,1200,2024-01-15 14:30
```

#### 8.2.6 admins.csv — 1 record

| Column | Type | Example | Description |
|--------|------|---------|-------------|
| id | int | `999` | Unique admin ID |
| name | String | `admin` | Admin name |
| password | String | `admin` | Login password (plain text) |
| role | String | `Super Admin` | Admin role label |

Example row:
```
999,admin,admin,Super Admin
```

### 8.3 Load Order (Dependency Chain)

CSV files must be loaded in a specific order because later entities reference earlier ones by ID:

```
Step 1: equipment.csv     ← No dependencies
Step 2: heroes.csv        ← References equipment IDs (recommendedEquipIds)
Step 3: teams.csv         ← No dependencies (but loaded before players for teamId reference)
Step 4: players.csv       ← References team IDs, hero IDs
Step 5: matches.csv       ← References team IDs (teamAId, teamBId)
Step 6: admins.csv        ← No dependencies, loaded last
```

**Why this order matters**: When `loadHeroes()` runs, equipment must already exist in `equipmentMap`
so that hero equipment ID references are valid. When `loadPlayers()` runs, teams must already exist
for `teamId` assignment. Loading out of order would result in null references or orphaned data.

### 8.4 Java Features Used for File I/O

| Feature | Where | Purpose |
|---------|-------|---------|
| `try-with-resources` | All load/save methods | Automatically closes BufferedReader/PrintWriter, no manual `.close()` needed |
| `BufferedReader` | Load methods | Reads CSV lines efficiently line-by-line |
| `PrintWriter` + `FileWriter` | Save methods | Writes formatted data to CSV files |
| `String.split(",")` | Load methods | Parses each CSV row into column values |
| `String.join(";", ...)` | Save methods | Joins list values into semicolon-separated strings |
| `EnumType.valueOf(name)` | Load methods | Converts saved enum name string back to enum constant |

---

## 9. UML Class Diagram

```
                    <<interface>>
                    Identifiable
                    +getId():int
                    +getName():String
                         △
         ┌───────────────┼───────────────┐
         │               │               │
    ┌────┴────┐   ┌──────┴──────┐   ┌───┴──────┐
    │  Hero   │   │  Equipment  │   │   Team   │
    └─────────┘   └─────────────┘   └──────────┘

┌───────────────┐
│ Person (abs)  │
│ -id: int      │
│ -name: String │
│ -password     │
│ +displayInfo()│
└───────┬───────┘
        │
   ┌────┴────────┐
   │             │
┌──┴──────┐ ┌───┴─────┐
│ Player  │ │  Admin  │
│ -level  │ │  -role  │
│ -wins   │ └─────────┘
│ -teamId │
│ -heroId │
│  List   │
└────┬────┘
     │ has-many (ID ref)
     ▽
   Hero ── recommends (ID) ──> Equipment
   Team ── contains (ID) ──> Player
   Team ── participates (ID) ──> MatchRecord
```

### Relationships

| Relation | Type | Description |
|----------|------|-------------|
| Person -> Player/Admin | Inheritance (extends) | "is-a" relationship |
| Player -> Hero | Association (ID ref) | "owns" relationship, one-to-many |
| Hero -> Equipment | Association (ID ref) | "recommends" relationship |
| Team -> Player | Aggregation (ID ref) | "contains" relationship |
| Identifiable <- Person/Hero/Equipment/Team | Implementation (implements) | "provides" relationship |
| Reportable <- Player/Team/MatchRecord | Implementation (implements) | "provides" relationship |

---

## 10. Data Flow Diagram

```
┌──────────┐     ┌─────────────────────────────┐
│   User    │────>│  Main.java (Menu Router)     │
│  Input    │     └──────────┬──────────────────┘
└──────────┘                │
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
              │   (Singleton, HashMap)│
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
              │   CSV read/write      │
              │   data/*.csv          │
              └───────────────────────┘
```

---

## 11. Sequence Diagram — Player Lookup Flow

```
User enters "MengLei"
     │
     ▼
Main.doPlayerLookup()
     │
     ├─ InputHelper.readNonEmptyString("Name keyword: ")
     │       └─> returns "MengLei"
     │
     ├─ searchService.searchPlayers("MengLei")
     │       └─> GameDataManager.findPlayersByName("MengLei")
     │               ├─ Iterate playerMap.values()
     │               ├─ Each Player.getName().contains("MengLei")
     │               └─> returns List<Player> [MengLei(ID=1)]
     │
     ├─ Print: "Found 1 player(s): MengLei Lv.28 WR:62.5%"
     │
     ├─ InputHelper.readIntOrDefault("Enter ID (0=skip): ", 0)
     │       └─> returns 1
     │
     └─ searchService.getPlayerFullReport(MengLei)
             ├─ Print: Name, Level, Win Rate, Team
             ├─ Iterate heroIdList [1,2,3]
             │     ├─ dm.findHeroById(1) -> Li Bai [Assassin]
             │     │     └─ Iterate equipIds [1,2,18]
             │     │           ├─ dm.findEquipmentById(1) -> Breaker
             │     │           ├─ dm.findEquipmentById(2) -> Infinity Edge
             │     │           └─ dm.findEquipmentById(18) -> Calm Boots
             │     ├─ dm.findHeroById(2) -> Han Xin [Assassin]
             │     └─ dm.findHeroById(3) -> Sun Wukong [Assassin]
             │
             └─> Returns full report string
```

### Key Points

| Step | Description |
|------|-------------|
| 1. User input | InputHelper reads and validates non-empty |
| 2. Fuzzy search | Iterate HashMap values(), contains() matching |
| 3. ID lookup | HashMap.get(id) - O(1) time complexity |
| 4. Cascade query | heroIdList -> find Hero -> equipIds -> find Equipment |
| 5. Report generation | SearchService assembles all info into a string |

---

## 12. Class Responsibility Summary

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

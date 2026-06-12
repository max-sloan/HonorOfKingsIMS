# AI Prompts Record

## Prompt 01 — Project Reset and Structure Init

* **Time**:Jun 8th,2026
* **Tool/Model**: Cherry + deepseekv4
* **Agent Role**: Project Manager Agent
* **Related Commit**:1da742f [AI-Implementation] implement model classes, service layer, and Main menu

### My Prompt
Because of my fault, I make my project become miss and I don't know how to continue my project. So I decide to use the Agent to help me to manage my project. With the AI help, I build the main body of my project and I sent the first commit.

### AI Response Summary

AI confirmed folder path C:\Users\wangye\Desktop\HonorOfKingsIMS, then: deleted old content, recreated directory structure (src/model, src/service, src/util, docs, ai), created placeholder files, initialized Git. AI built the main structor of my project.

### My Decision

Accepted the AI's suggestions. Confirmed path and agreed to delete and rebuild the main body of my project and sent the first commit.

### My Manual Change

None.



## Prompt 02 — Architecture Design and plan.md Draft

* **Time**:Jun 8th,2026
* **Tool/Model**: Cherry + DeepseekV4 (Architect Agent)
* **Agent Role**: Architect Agent
* **Related Commit**:1da742f [AI-Implementation] implement model classes, service layer, and Main menu
                     a616f59 [AI-Implementation] complete file I/O: save and load data via CSV

### My Prompt

"You are a Java OOP architect. Please provide an architecture analysis report for the Honor of Kings IMS project. Include class relationships, interface design, enum design, data flow, architecture recommendations, and beginner tips."

### AI Response Summary

Architect Agent provided a complete analysis: Person->Player/Admin inheritance, ID reference design, Identifiable + Reportable interfaces, 3 enums, 4-layer architecture (UI -> Service -> GameDataManager -> FileStorage), 6 service classes, CSV format, beginner pitfalls list, data build order.

### My Decision

Accepted (Option A). First time seeing HashMap concept. AI explained HashMap as key-value pairs with O(1) lookup. Previously only knew ArrayList. Understanding why HashMap is faster than List traversal for data center lookups.

### My Manual Change

None.


## Prompt 03 — Class Design Finalization (Checkpoint 2)

* **Time**:Jun 9th,2026
* **Tool/Model**: Cherry + Claude (Project Manager Agent)
* **Agent Role**: Project Manager Agent
* **Related Commit**:aae9a6 [AI-Architect] add sequence diagram for player lookup flow

### My Prompt

(Auto-generated: PM Agent generated design.md and presented Checkpoint 2)

### AI Response Summary

PM Agent generated complete design.md: 3 enums, 2 interfaces, 7 model classes, 6 service classes, HashMap and Singleton explanations, menu structure design for Admin/Player dual roles.

### My Decision

Accepted (Option A). Approved all class design, proceed to code.

### My Manual Change

None.



## Prompt 04 — Checkpoint 3: Player class modifications

* **Time**:Jun 8th,2026
* **Tool/Model**: Cherry + Claude
* **Agent Role**: Project Manager Agent

### My Prompt

"I choose B. Add personal win rate field and team name field to Player class."

### AI Response Summary

Added winRate (double) and teamName (String) fields to Player.java. Added updateWinRate() method, updated incrementMatch() to auto-recalculate, updated displayInfo() to show team name.

### My Decision

Accepted modifications, then confirmed to proceed to Service layer.

### My Manual Change

None.


## Prompt 05 — Checkpoint 4: Dataset modification

* **Time**:Jun 8th,2026
* **Tool/Model**: Cherry + Claude
* **Agent Role**: Project Manager Agent
* **Related Commit**:f2435d7 [Human] fix menu alignment and add player win rate simulation

### My Prompt

"I choose B. Change team names to AG, Wolves, eStar, and change players to famous KPL players from these teams."

### AI Response Summary

Updated team names to AG, Wolves, eStar and all 12 players to real KPL players (MengLei/YiNuo/ChangSheng/Cat, Fly/YaoDao/XiaoPang/XiangYu, HuaHai/QingRong/TanRan/ZiYang).

### My Decision

Accepted, then confirmed Service layer.

### My Manual Change
I need more suitable Service layer so that the users can use this project more comfortable.

---



## Prompt 06 — Code Review and Bug Fixes

* **Time**:Jun 9th,2026
* **Tool/Model**: Cherry + Claude (Testing/Reviewer Agent)
* **Agent Role**: Testing/Reviewer Agent
* **Related Commit**:2ed3a22 [AI-Review] bug fixes from code review + final documentation

### My Prompt

"Review all 21 Java files. Find bugs, logic issues, OOP design problems, collection issues, exception handling gaps, file I/O issues."

### AI Response Summary

Found 20 issues (3 critical, 6 logic, 2 file I/O, etc.). Key findings: hero descriptions with Chinese commas crash CSV parsing, match history unsorted, orphaned user after data load, wrong javadoc comments.

### My Decision

Accepted review, fixed critical bugs (commas->spaces, match sorting, auto-logout on load, comment fixes). Minor issues recorded as known limitations.

### My Manual Change

Fixed comma issues in hero descriptions, sorted matches by time, added auth.logout() before data load.


## Prompt 07 — Project Review and Data Consistency Fix

* **Time**: Jun 12th, 2026
* **Tool/Model**: Cherry + DeepSeek (Project Manager Agent)
* **Agent Role**: Project Manager Agent
* **Related Commit**: TODO(HUMAN)

### My Prompt

"I hope you can check whether the contents of my HonorOfKingsIMS folder meet the requirements. If there are any non-compliant parts, stop first and ask me before making changes."

### AI Response Summary

PM Agent conducted a comprehensive project review, reading all 21 Java source files, 5 CSV data files (now 6), and 8 documentation files. The review covered: directory structure, 7 core entities, 8 Java concepts (inheritance, interface, encapsulation, polymorphism, collections, exception handling, file I/O, enums), and 8 system features. Found 4 issues:

1. **Critical**: CSV data files contained Chinese names (equipment, heroes, players) but Java source code (DataInitializer.java) used English names — language inconsistency after project translation.
2. **Minor**: README listed 3 known but unfixed bugs (plaintext password, equipment usage count on hero delete, teamName cache on team rename).
3. **Medium**: Admin data was not persisted to CSV — only players, heroes, equipment, teams, and matches were saved. Program restart would lose Admin data (only default admin recreated).
4. **Minor**: `ai/reflection.md` filename uses non-standard spelling (missing one 'l').

### My Decision

- Problem 1: Fix — regenerate all CSV files with English data matching DataInitializer.java
- Problem 2: Skip for now — keep as documented known limitations
- Problem 3: Fix — add Admin CSV save/load to FileStorageService and create admins.csv
- Problem 4: Keep as-is (matches project requirements)

### My Manual Change

Wrote the prompt to trigger PM Agent to review the project. Confirmed all three fix decisions.

---

### AI Fix Actions

1. Regenerated 5 CSV data files (`equipment.csv`, `heroes.csv`, `players.csv`, `teams.csv`, `matches.csv`) — all names now in English matching DataInitializer.java. Equipment usage counts recalculated based on `addEquipmentToHero()` calls.
2. Added `saveAdmins()` and `loadAdmins()` methods to `FileStorageService.java`, integrated into `saveAll()` and `loadAll()`.
3. Created `data/admins.csv` with default admin record (ID=999, admin/admin, Super Admin).

### Files Modified
- `data/equipment.csv` — rewritten (English names, recalculated usageCount)
- `data/heroes.csv` — rewritten (English names and descriptions)
- `data/players.csv` — rewritten (English player names)
- `data/teams.csv` — unchanged (already English)
- `data/matches.csv` — unchanged (already English)
- `data/admins.csv` — new file
- `src/service/FileStorageService.java` — added `saveAdmins()`, `loadAdmins()`, updated `saveAll()` and `loadAll()`



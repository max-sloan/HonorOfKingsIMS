# AI Prompts Record

## Prompt 01 — Project Reset and Structure Init

* **Time**:Jun 8th,2026
* **Tool/Model**: Cherry + deepseekv4
* **Agent Role**: Project Manager Agent
* **Related Commit**:1da742f [AI-Implementation] implement model classes, service layer, and Main menu

### My Prompt
Due to errors I made earlier, the project encountered anomalies. I believe it would be better to start over: clear the original files and create a new project architecture that meets all requirements.

### AI Response Summary

AI confirmed folder path C:\Users\wangye\Desktop\HonorOfKingsIMS, then: deleted old content, recreated directory structure (src/model, src/service, src/util, docs, ai), created placeholder files, initialized Git. AI built the main structor of my project.

### My Decision

Accepted the AI's suggestions. Confirmed path and agreed to delete and rebuild the main body of my project and sent the first commit.

### My Manual Change

I manually modified the project name to make it more compliant with the requirements and optimized the design process.



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

I have remove HashMap from this project.


## Prompt 03 — Class Design Finalization (Checkpoint 2)

* **Time**:Jun 9th,2026
* **Tool/Model**: Cherry + Claude (Project Manager Agent)
* **Agent Role**: Project Manager Agent
* **Related Commit**:aae9a6 [AI-Architect] add sequence diagram for player lookup flow

### My Prompt
Based on the documents I just told you about, design the classes required for this project. The design must include at least 3 enums, 2 interfaces, 7 model classes, 6 service classes, explanations for the use of HashMap and the Singleton pattern, and a menu structure design for the dual roles of Admin and Player.
Do not directly write the generated content into the project. Instead, generate and print it on the screen for me to review. Only after I have checked and confirmed there are no errors, and with my explicit consent, may you write the generated content into the files.

### AI Response Summary

PM Agent generated complete design.md: 3 enums, 2 interfaces, 7 model classes, 6 service classes, HashMap and Singleton explanations, menu structure design for Admin/Player dual roles.

### My Decision

I have decided to adopt your design for the model classes, but improvements are still needed in other parts.

### My Manual Change

I have input your design for the model classes, but I have not adopted the designs for other components. Instead, I have redesigned all other files from scratch.



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
* **Related Commit**: bd26426 [AI-Review] fix English text inconsistencies after translation

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
* **Related Commit**: fb20049 [AI-PM] fix CSV data consistency and add Admin persistence



## Prompt 08 — README How-to-Run Fix

* **Time**: Jun 14th, 2026
* **Tool/Model**: Cherry + DeepSeek (Project Manager Agent)
* **Agent Role**: Project Manager Agent
* **Related Commit**: 90f24bb (HEAD -> master) [Human] fix README missing compile/run commands and unclosed code block

### My Prompt

"I noticed the README has a problem, specifically the 'How to Run' section. It seems part of it is missing. After detecting, report the result to me, then I will decide whether to fix it."

### AI Response Summary

PM Agent read README.md and found two issues in section 2 "How to Run":

1. **Missing compile and run commands**: The code block only contained `cd Desktop/HonorOfKingsIMS`, the `javac` compile command and `java` run command were completely lost.
2. **Unclosed code block**: The ` ```powershell ` fence was never closed with ` ``` `, causing all subsequent sections (3–8) to be swallowed by the Markdown renderer as part of the code block. Sections about login accounts, features, Java concepts, AI usage, testing, and known limitations were invisible to readers.

### My Decision

Fix — add the missing `javac` and `java` commands, close the code block, and restore all section visibility.

### My Manual Change

None. The AI detected the issue and I confirmed the fix.

---



## Prompt 09 — CSV Documentation Enhancement

* **Time**: Jun 14th, 2026
* **Tool/Model**: Cherry + DeepSeek (Project Manager Agent)
* **Agent Role**: Project Manager Agent
* **Related Commit**: TODO(HUMAN)

### My Prompt

"I notice the CSV documentation in the project is not clearly recorded. I need you to check this area, tell me how to improve it, and only make changes I approve."

### AI Response Summary

PM Agent searched all Markdown files and Java files for CSV/file I/O references. Found 5 issues:

1. `docs/design.md` §4.6: said "5 data types" — wrong, now 6 (Admin added)
2. `docs/test-cases.md` T14: said "5 CSV files correct" — wrong, now 6
3. `docs/design.md`: no dedicated CSV format specification chapter existed — the entire CSV storage design was documented in just 4 scattered one-liners
4. `plan.md` §6: only one line "Storage: CSV files in data/ folder"
5. `README.md`: only mentioned "File I/O (CSV)" in two words

Proposed 3 changes: fix 5→6 errors (A, B), add a dedicated CSV chapter to design.md with 4 sub-sections (C).

### My Decision

- Fix the two "5→6" data errors
- Add a new independent §8 CSV File Format Specification chapter in `docs/design.md`

### My Manual Change

Wrote the prompt for AI to check CSV documentation and accepted the 3 proposed fixes.

---

### AI Fix Actions

1. Fixed `docs/design.md` §4.6: `5 data types` → `6 data types`
2. Fixed `docs/test-cases.md` T14: `5 CSV files` → `6 CSV files`
3. Inserted new **§8 CSV File Format Specification** in `docs/design.md` with 4 sub-sections:
   - **8.1 Delimiter Rules**: three-level separator system (comma, semicolon, colon) with explanation
   - **8.2 File Definitions**: 6 subsections (8.2.1–8.2.6), each with a column table and example row for equipment, heroes, teams, players, matches, admins
   - **8.3 Load Order**: 6-step dependency chain with explanation of why order matters
   - **8.4 Java Features Used**: table of 6 Java file I/O features (try-with-resources, BufferedReader, PrintWriter, String.split, String.join, Enum.valueOf)
4. Renumbered subsequent chapters §8→§9, §9→§10, §10→§11, §11→§12

### Files Modified
- `docs/design.md` — fixed 5→6, added §8 CSV chapter, renumbered §9~12
- `docs/test-cases.md` — fixed 5→6 in T14
- `ai/prompts.md` — Prompt 09 added (this entry)
- `ai/agent-log.md` — CSV documentation session entry added
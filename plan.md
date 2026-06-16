# Plan — AI-Assisted Honor of Kings Information Management System

> Author: wangye(25722029)
> Date: June 2026
> Course: Java Coursework 2026

---

## 1. Project Goal

A console-based Honor of Kings information management system for managing players, heroes, equipment, teams, and match records.

**User Roles:**
- Admin: full CRUD access to all data
- Player: view public info and own data, edit limited personal info

---

## 2. Requirement Analysis

### 2.1 Player Lookup
Search by ID or name. Display: ID, name, team, level, win rate, owned heroes and equipment.

### 2.2 Team Overview
Search by ID or name. Display: team name, members, average level, total matches, win rate, top player.

### 2.3 Hero Details
Search by name. Display: name, type, recommended equipment, owners.

### 2.4 Equipment Statistics
Rank equipment by usage count. Display ranking formula in documentation.

### 2.5 Match History
Retrieve last N matches for a player or team. Display: opponent, date, result.

### 2.6 Leaderboard
Rank players by win rate, level, or matches. Explain tie-break handling.

### 2.7 Data Management
Admin: add, delete, modify all data. Player: view own info, edit limited fields.

### 2.8 Authentication
Login/logout with two roles: Admin and Player.

### 2.9 Recommendation Engine
Recommend heroes based on player preference, hero win rate, team composition. Recommend equipment based on hero type match, usage count, player history.

---

## 3. Java Concepts Used

| Concept | Location | Description |
|---------|----------|-------------|
| Inheritance | Person -> Player, Person -> Admin | Abstract parent with shared fields |
| Interface | Identifiable, Reportable | Unified identity and reporting behavior |
| Encapsulation | All model classes | Private fields with getter/setter |
| Polymorphism | Person.displayInfo(), Identifiable | Same method, different subclass behavior |
| Collections | GameDataManager | HashMap for data storage, ArrayList for lists |
| Exception Handling | Service classes | Invalid input, duplicate IDs, file errors |
| File I/O | FileStorageService | CSV save and load |
| Enums | HeroType, EquipmentType, MatchResult | Fixed constant sets |
| Algorithm | RecommendationService | Weighted scoring formula for hero/equipment recommendation |

---

## 4. Class Design

### Inheritance Tree
```
Person (abstract) -> Player, Admin
```
Independent entities: Hero, Equipment, Team, MatchRecord

### Key Design Decision: ID References
All entities linked via ID + HashMap lookup instead of direct object references. Benefits: no circular references, simple serialization.

### Interfaces
- Identifiable: getId(), getName() — Person, Hero, Equipment, Team
- Reportable: generateReport() — Player, Team, MatchRecord

### Service Layer
- GameDataManager (Singleton) — central data store
- AuthenticationService — login/logout
- SearchService — search and query
- RankingService — leaderboard calculations
- AdminDataService — admin CRUD
- FileStorageService — CSV persistence
- RecommendationService — Hero/equipment recommendations via weighted scoring

---

## 5. UML Draft

```
                    <<interface>>
                    Identifiable
                         △
         ┌───────────────┼───────────────┐
    ┌────┴────┐   ┌──────┴──────┐   ┌───┴──────┐
    │  Hero   │   │  Equipment  │   │   Team   │
    └─────────┘   └─────────────┘   └──────────┘

┌───────────────┐
│ Person (abs)  │
└───────┬───────┘
   ┌────┴────────┐
┌──┴──────┐ ┌───┴─────┐
│ Player  │ │  Admin  │
└─────────┘ └─────────┘
```

---

## 6. Data Design

| Type | Count |
|------|-------|
| Teams | 3 |
| Players | 12 |
| Heroes | 15 |
| Equipment | 20 |
| Match Records | 10 |

Build order: Equipment -> Heroes -> Teams -> Players -> Matches

Storage: CSV files in data/ folder

---

## 7. AI Usage Plan

Three agent roles:
- Architect Agent: class design, UML, module planning
- Implementation Agent: incremental code implementation
- Testing/Reviewer Agent: code review, bug finding, test cases

---

## 8. Prompt Strategy

- Be specific: give context and constraints
- Go step by step: one question at a time
- Verify: AI code must compile and run

---

## 9. Development Timeline

| Stage | Content | Commits |
|-------|---------|---------|
| Stage 0 | Project init, Git setup | [Human] |
| Stage 1-2 | Requirements, design | [Human], [AI-Architect] |
| Stage 3-5 | Model, Service, Main | [AI-Implementation] |
| Stage 6 | File I/O | [AI-Implementation] |
| Stage 7 | Testing, review, docs | [AI-Review], [Human] |

---

## 10. Testing Plan

14 test cases covering: login (3), player lookup (2), team overview, hero details, equipment stats, match history, leaderboard (2), data management (2), file I/O.

---

## 11. Risk Analysis

| Risk | Mitigation |
|------|------------|
| AI-generated bugs | Manual testing for every code block |
| Overly complex design | Keep simple, checkpoint confirmation |
| Data inconsistency | Cascade updates in AdminDataService |
| File I/O path issues | Use relative paths, data/ folder |

---

## 12. Final Reflection Placeholder

> Filled in ai/reflection.md after project completion.

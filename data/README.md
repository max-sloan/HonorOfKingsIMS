# CSV Data Files — Format Documentation

> This folder contains all persistent data for the Honor of Kings IMS project.
> FileStorageService reads from and writes to these files.

---

## Delimiter Rules

| Level | Separator | Example | Purpose |
|-------|-----------|---------|---------|
| 1 — Columns | `,` (comma) | `1,Breaker,ATTACK,2950` | Separate fields in one row |
| 2 — Lists | `;` (semicolon) | `1;2;18` | Separate multiple values in one field |
| 3 — Key-Value | `:` (colon) | `Attack:180` | Separate attribute key and value |

First line of every CSV file is a **header row**, skipped when loading.

---

## File Index

| File | Entity | Records | Dependencies |
|------|--------|---------|--------------|
| `equipment.csv` | Equipment | 20 | None |
| `heroes.csv` | Hero | 15 | equipment.csv |
| `teams.csv` | Team | 3 | None |
| `players.csv` | Player | 12 | teams.csv, heroes.csv |
| `matches.csv` | MatchRecord | 10 | teams.csv |
| `admins.csv` | Admin | 1 | None |

---

## Load Order

```
Step 1: equipment.csv
Step 2: heroes.csv       (references equipment IDs)
Step 3: teams.csv
Step 4: players.csv      (references team IDs, hero IDs)
Step 5: matches.csv      (references team IDs)
Step 6: admins.csv
```

Loading out of order causes null references.

---

## File Definitions

### equipment.csv

| Column | Type | Example | Description |
|--------|------|---------|-------------|
| id | int | `1` | Unique equipment ID |
| name | String | `Breaker` | Equipment name |
| equipType | enum | `ATTACK` | ATTACK / DEFENSE / MOVEMENT / MAGIC / JUNGLE / SUPPORT |
| price | int | `2950` | Gold price |
| attributes | String | `Attack:180;Crit Rate:25` | Key-value pairs joined by `;` and `:` |
| usageCount | int | `4` | Number of heroes recommending this equipment |

Example row:
```
1,Breaker,ATTACK,2950,Attack:180,4
```

### heroes.csv

| Column | Type | Example | Description |
|--------|------|---------|-------------|
| id | int | `1` | Unique hero ID |
| name | String | `Li Bai` | Hero name |
| heroType | enum | `ASSASSIN` | TANK / WARRIOR / ASSASSIN / MAGE / MARKSMAN / SUPPORT |
| difficulty | int | `8` | Difficulty rating 1–10 |
| description | String | `The sword immortal...` | Hero lore (must not contain commas) |
| equipIds | String | `1;2;18` | Recommended equipment IDs |

Example row:
```
1,Li Bai,ASSASSIN,8,The sword immortal comes and goes like the wind,1;2;18
```

### teams.csv

| Column | Type | Example | Description |
|--------|------|---------|-------------|
| id | int | `1` | Unique team ID |
| name | String | `AG` | Team name |
| createdDate | String | `2023-06-01` | Date (yyyy-MM-dd) |
| playerIds | String | `1;2;3;4` | Member player IDs |
| matchIds | String | `1;2;3;4;5;6;10` | Match record IDs |

Example row:
```
1,AG,2023-06-01,1;2;3;4,1;2;3;4;5;6;10
```

### players.csv

| Column | Type | Example | Description |
|--------|------|---------|-------------|
| id | int | `1` | Unique player ID |
| name | String | `MengLei` | Player name |
| password | String | `123` | Login password (plain text) |
| level | int | `28` | Player level 1–30 |
| rankScore | int | `2500` | Rank score |
| totalMatches | int | `8` | Total matches played |
| wins | int | `5` | Matches won |
| winRate | double | `62.5` | wins / totalMatches × 100 |
| teamId | int | `1` | Team ID (-1 = no team) |
| teamName | String | `AG` | Cached team name |
| heroIds | String | `1;2;3` | Owned hero IDs |

Example row:
```
1,MengLei,123,28,2500,8,5,62.5,1,AG,1;2;3
```

### matches.csv

| Column | Type | Example | Description |
|--------|------|---------|-------------|
| id | int | `1` | Unique match ID |
| teamAId | int | `1` | Team A ID |
| teamBId | int | `2` | Team B ID |
| result | enum | `TEAM_A_WIN` | TEAM_A_WIN / TEAM_B_WIN / DRAW |
| winningTeamId | int | `1` | Winning team ID (-1 for DRAW) |
| duration | int | `1200` | Duration in seconds |
| matchTime | String | `2024-01-15 14:30` | Timestamp (yyyy-MM-dd HH:mm) |

Example row:
```
1,1,2,TEAM_A_WIN,1,1200,2024-01-15 14:30
```

### admins.csv

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

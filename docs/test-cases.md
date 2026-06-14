# Test Cases — Honor of Kings IMS

> Manual test documentation, 14 test cases.

---

## T01: Admin Login (correct password)

| Item | Content |
|------|---------|
| **Function** | Authentication — Admin login |
| **Input** | ID=999, password=admin |
| **Expected** | Login success, Admin menu displayed |
| **Actual** | Login success, "Admin Menu" shown |
| **Result** | Pass |

---

## T02: Admin Login (wrong password)

| Item | Content |
|------|---------|
| **Function** | Authentication — wrong password |
| **Input** | ID=999, password=wrong |
| **Expected** | "Wrong ID or password" message, return to main menu |
| **Actual** | "[Failed] Wrong ID or password!" |
| **Result** | Pass |

---

## T03: Player Login

| Item | Content |
|------|---------|
| **Function** | Authentication — Player login |
| **Input** | ID=1, password=123 |
| **Expected** | Login success, Player menu (MengLei) |
| **Actual** | Login success, "Player Menu | Player: MengLei" |
| **Result** | Pass |

---

## T04: Player Lookup (fuzzy search)

| Item | Content |
|------|---------|
| **Function** | Player lookup |
| **Input** | Search keyword "Meng" |
| **Expected** | Find MengLei |
| **Actual** | Found 1: "MengLei Lv.28 WR:62.5%" |
| **Result** | Pass |

---

## T05: Player Lookup (not found)

| Item | Content |
|------|---------|
| **Function** | Player lookup |
| **Input** | Search keyword "xyz_not_exist" |
| **Expected** | "[Not Found]" message |
| **Actual** | "[Not Found]" |
| **Result** | Pass |

---

## T06: Team Overview

| Item | Content |
|------|---------|
| **Function** | Team overview — view AG |
| **Input** | By ID, ID=1 |
| **Expected** | AG details, 4 members, avg level, win rate |
| **Actual** | Correct: 4 members, avg level, team win rate |
| **Result** | Pass |

---

## T07: Hero Details

| Item | Content |
|------|---------|
| **Function** | Hero details — Li Bai |
| **Input** | Search "Li Bai", ID=1 |
| **Expected** | Assassin type, 3 recommended equipment, owners |
| **Actual** | Correct: type, equipment, 3 owners |
| **Result** | Pass |

---

## T08: Equipment Stats Ranking

| Item | Content |
|------|---------|
| **Function** | Equipment stats — by recommendation count |
| **Input** | Select "Equipment Stats" |
| **Expected** | Equipment sorted by usage count descending |
| **Actual** | Correctly sorted, Breaker ranks #1 |
| **Result** | Pass |

---

## T09: Match History

| Item | Content |
|------|---------|
| **Function** | Match history — by team |
| **Input** | By team, ID=1 (AG) |
| **Expected** | All 7 AG matches sorted by time |
| **Actual** | Correctly displayed and sorted |
| **Result** | Pass |

---

## T10: Leaderboard (Win Rate)

| Item | Content |
|------|---------|
| **Function** | Leaderboard — Win Rate Top 5 |
| **Input** | Win Rate, show 5 |
| **Expected** | Fly(70%), YiNuo(66.7%), MengLei/HuaHai(62.5%) |
| **Actual** | Correct order, tie-break by matches |
| **Result** | Pass |

---

## T11: Leaderboard (Level)

| Item | Content |
|------|---------|
| **Function** | Leaderboard — Level Top 5 |
| **Input** | Level, show 5 |
| **Expected** | Fly(28), MengLei(28), Fly ranked first (higher score) |
| **Actual** | Correct ordering |
| **Result** | Pass |

---

## T12: Admin Add Player

| Item | Content |
|------|---------|
| **Function** | Data management — add player |
| **Input** | Add "Test" level 10 score 1500 |
| **Expected** | Created with new ID |
| **Actual** | "[Success] ID=13" |
| **Result** | Pass |

---

## T13: Admin Delete Player

| Item | Content |
|------|---------|
| **Function** | Data management — delete player |
| **Input** | Delete ID=13, confirm yes |
| **Expected** | Cascade cleanup of team reference |
| **Actual** | "[Success] Deleted", cleanup correct |
| **Result** | Pass |

---

## T14: File Save and Load

| Item | Content |
|------|---------|
| **Function** | File I/O — save then reload |
| **Input** | Save -> Restart -> Load |
| **Expected** | Data consistent, password="123" |
| **Actual** | 6 CSV files correct, data intact after load |
| **Result** | Pass |

---

## T15: README How-to-Run Section Review

| Item | Content |
|------|---------|
| **Function** | Documentation — README.md compile/run instructions |
| **Input** | Open README.md, check section 2 "How to Run" |
| **Expected** | Complete javac and java commands visible, code block properly closed, sections 3-8 visible |
| **Actual** | `javac` and `java` commands were missing, code block was unclosed, sections 3-8 invisible. After fix: all commands present, sections 1-8 all visible |
| **Result** | Pass (after fix) |

---

## Summary

| Statistic | Count |
|-----------|-------|
| Total tests | 15 |
| Passed | 15 |
| Failed | 0 |

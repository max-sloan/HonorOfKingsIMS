# AI-Assisted Honor of Kings Information Management System

## 1. Project Overview

Java console application managing players, heroes, equipment, teams, and match records for Honor of Kings.

## 2. How to Run

```powershell
cd Desktop/HonorOfKingsIMS
javac -encoding UTF-8 -d out src/model/enums/*.java src/model/interfaces/*.java src/model/*.java src/util/*.java src/service/*.java src/Main.java
java -cp out -Dfile.encoding=UTF-8 Main
```

## 3. Default Login Accounts

| Role | ID | Password |
|------|-----|----------|
| Admin | 999 | admin |
| All Players | 1-12 | 123 |

## 4. Implemented Features

Player Lookup, Team Overview, Hero Details, Equipment Stats, Match History, Leaderboard, Data Management (CRUD), Authentication (Admin/Player), File I/O (CSV).

## 5. Java Concepts Used

Inheritance, Interface, Encapsulation, Polymorphism, Collections (ArrayList, HashMap), Exception Handling, File I/O, Enums.

## 6. AI Usage Summary

Architect Agent (class design), Implementation Agent (code generation), Testing Agent (code review). See `ai/` folder for details.

## 7. Testing Summary

15 manual test cases, all passed. See `docs/test-cases.md`.

## 8. Known Limitations

- Plaintext password storage
- Hero deletion does not decrement equipment usage count
- Team rename does not auto-update cached teamName in Player

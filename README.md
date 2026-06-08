# AI-Assisted Honor of Kings Information Management System

## 1. Project Overview

Java 控制台应用，管理王者荣耀的玩家、英雄、装备、战队和比赛记录。

## 2. How to Run

```powershell
cd HonorOfKingsIMS
javac -encoding UTF-8 -d out src/model/enums/*.java src/model/interfaces/*.java src/model/*.java src/util/*.java src/service/*.java src/Main.java
java -cp out -Dfile.encoding=UTF-8 Main
```

## 3. Default Login Accounts

| 角色 | ID | 密码 |
|------|-----|------|
| Admin | 999 | admin |
| 所有玩家 | 1-12 | 123 |

## 4. Implemented Features

Player Lookup, Team Overview, Hero Details, Equipment Stats, Match History, Leaderboard, Data Management (CRUD), Authentication (Admin/Player), File I/O (CSV).

## 5. Java Concepts Used

Inheritance, Interface, Encapsulation, Polymorphism, Collections (ArrayList, HashMap), Exception Handling, File I/O, Enums.

## 6. AI Usage Summary

Architect Agent (类设计), Implementation Agent (代码生成), Testing Agent (代码审查). 详见 `ai/` 文件夹。

## 7. Testing Summary

14 个测试用例全部通过。详见 `docs/test-cases.md`。

## 8. Known Limitations

- 密码明文存储
- 英雄删除不回退装备计数
- 战队改名后 teamName 缓存不自动更新

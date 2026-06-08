# AI Prompts Record

> 记录本项目中使用AI工具的所有重要提示词及响应。

---

## Prompt 01 — 项目重置与结构初始化

* **Time**: TODO(HUMAN)
* **Tool/Model**: Cherry + Claude
* **Agent Role**: Project Manager Agent（项目经理）
* **Related Commit**: `acc907d`

### My Prompt

```
RESET AND OVERWRITE EXISTING PROJECT.
I want you to restart my Java coursework project from zero and overwrite the existing project folder.
...
[完整的重置指令，包括删除旧项目、重建目录结构、Git初始化等]
```

### AI Response Summary

AI 首先确认了项目文件夹路径 `C:\Users\wangye\Desktop\HonorOfKingsIMS`，等待用户确认后执行了以下操作：
1. 由于文件夹被进程锁定，先清空了旧项目内容
2. 重新创建了完整的目录结构（src/model, src/service, src/util, docs, ai）
3. 创建了所有占位文件（Main.java, design.md, test-cases.md, prompts.md, agent-log.md, reflection.md, plan.md, README.md, git-history.txt）
4. 初始化 Git 仓库
5. 完成第一次提交：`[Human] reset project and create clean initial structure`

### My Decision

接受。确认路径正确，同意删除旧项目并重建。

### My Manual Change

无。

---

## Prompt 02 — 架构设计与 plan.md 起草

* **Time**: TODO(HUMAN)
* **Tool/Model**: Cherry + Claude（Architect Agent）
* **Agent Role**: Architect Agent（架构师）
* **Related Commit**: TODO(HUMAN)

### My Prompt

```
你是一位 Java OOP 架构师。请为以下王者荣耀信息管理系统项目提供架构分析报告。
[包含完整的项目需求、必选类、必用概念、数据集要求、功能模块等]
请提供：类关系分析、接口设计建议、枚举设计、数据流设计、架构建议、初学者注意事项。
```

### AI Response Summary

Architect Agent 提供了一份完整的架构分析报告，包括：
1. 类关系分析：Person→Player/Admin 两层继承，实体类通过 ID 引用关联
2. 接口设计：建议 Identifiable + Reportable 两个接口
3. 枚举设计：HeroType, EquipmentType, MatchResult
4. 数据流：四层架构 UI → Service → GameDataManager → FileStorageService
5. 架构建议：单例 GameDataManager，ID引用代替对象引用
6. 初学者注意事项：推荐单例模式，避免循环引用，数据构建顺序等

### My Decision

接受（选项A）。同意 plan.md 的全部设计，包括：ID引用设计、Identifiable + Reportable 接口、3个枚举、6个Service类、CSV存储格式、包结构设计。

特别说明：在选择架构设计时，我第一次看到 HashMap 这个概念。AI 解释了 HashMap 是一种"键值对"集合，可以通过 ID 快速查找对象（O(1) 时间复杂度）。我以前只会用 ArrayList，现在理解了为什么用 HashMap 做数据中心查找比用 List 遍历快得多。后续实现阶段我会进一步学习 HashMap 的具体用法。

### My Manual Change

无。

---

## Prompt 03 — 类设计定型（Checkpoint 2）

* **Time**: TODO(HUMAN)
* **Tool/Model**: Cherry + Claude（Project Manager Agent）
* **Agent Role**: Project Manager Agent（项目经理）
* **Related Commit**: TODO(HUMAN)

### My Prompt

```
（系统自动：Architect Agent 已给出完整架构分析后，PM Agent 生成了 design.md 并呈现 Checkpoint 2）
要求确认：接口选择、包结构、MatchRecord 不实现 Identifiable、日期用 String、约20个文件
```

### AI Response Summary

PM Agent 生成了完整的 design.md，包含：
- 3个枚举的完整定义（HeroType, EquipmentType, MatchResult）
- 2个接口的完整方法签名（Identifiable, Reportable）
- 7个模型类的详细属性表和方法签名表
- 6个 Service 类的职责和核心方法
- HashMap 和单例模式的初学者解释（写入 design.md）
- 菜单结构设计（Admin/Player 双角色）

### My Decision

接受（选项A）。同意全部类设计，开始写代码。

### My Manual Change

无。

---

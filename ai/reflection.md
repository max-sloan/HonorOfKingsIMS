# Final Reflection — 最终反思

> 项目：AI-Assisted Information Management System for Honor of Kings
> 作者：TODO(HUMAN)
> 日期：TODO(HUMAN)

---

## 1. Which AI tools or models did you use?

Cherry Studio + Claude，Project Manager / Architect / Implementation / Testing 四个 Agent 角色。

---

## 2. Which prompt was the most useful? Why?

Architect Agent 的类结构设计。它提出 **ID引用 + HashMap查找** 模式，避免了我自己设计时会犯的循环引用错误。

---

## 3. Which AI-generated suggestion was wrong, incomplete, or misleading?

**密码哈希（String.hashCode()）**。AI 用了 hashCode 存密码，但我是初学者还没学哈希，CSV 里存着 48690 而不是 123，完全看不懂。我主动要求改回明文 String。

---

## 4. How did you check whether AI-generated code was correct?

1. 编译检查语法
2. 在 VS Code 实际运行测试
3. 读代码确保能解释每一行

---

## 5. What bugs did you fix yourself instead of asking AI to fix?

1. **菜单对齐**：方框字符 ╔═╗ 和中文不对齐 → 改成 `=` 分隔线
2. **胜率全是 0%**：在 DataInitializer 加了 simulatePlayerStats()
3. **密码改明文**：从 hashCode 改回 String

---

## 6. What Java concept did you understand better after using AI?

**HashMap**。以前只会 ArrayList。通过这个项目理解了 `Map<键, 值>`：
- `put(key, value)` 存入
- `get(key)` 取出
- HashMap 通过键查找比 ArrayList 遍历快

---

## 7. What Java concept are you still unsure about?

1. Lambda 表达式 `(a, b) -> ...`
2. 单例模式的 static/instance 细节
3. try-with-resources `try (BufferedReader br = ...)` 的原理

---

## 8. Did AI make the project easier, harder, or both?

**两者都有。**
- 容易：类设计、代码生成、Bug 发现
- 困难：AI 会使用我没学过的概念（hashCode、Lambda），需要额外学习和要求简化

---

## 9. Which parts of the final project were mainly written by you?

- 数据集内容（英雄名、装备名、战队名、选手名）
- 菜单对齐和胜率 Bug 修复
- 密码改为明文的决策

---

## 10. Which parts were mainly generated or assisted by AI?

- 类结构设计（Architect Agent）
- Service 层代码框架
- CSV 读写逻辑
- 排行榜排序算法
- 代码审查（Testing Agent 发现 20 个问题）

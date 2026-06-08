package model;

import model.interfaces.Identifiable;

/**
 * Person —— 抽象父类
 *
 * 抽象类（abstract class）不能直接 new 对象，只能被子类继承。
 * 它定义 Player 和 Admin 共有的属性和方法。
 * 这体现了 Java 的"继承"和"封装"。
 *
 * "封装"（Encapsulation）的意思是：属性用 private 保护，
 * 外部只能通过 getter/setter 方法访问，这样可以控制数据的读写。
 */
public abstract class Person implements Identifiable {
    private int id;              // 唯一编号
    private String name;         // 姓名/昵称
    private int passwordHash;    // 密码哈希（用 String.hashCode() 生成）

    // 构造器：创建对象时初始化三个基本属性
    public Person(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.passwordHash = password.hashCode();  // hashCode() 把字符串转为数字
    }

    // === getter/setter ===
    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 检查密码是否正确
     * @param rawPassword 用户输入的明文密码
     * @return true = 密码正确, false = 密码错误
     */
    public boolean checkPassword(String rawPassword) {
        return this.passwordHash == rawPassword.hashCode();
    }

    /**
     * 修改密码
     */
    public void setPassword(String newPassword) {
        this.passwordHash = newPassword.hashCode();
    }

    /**
     * 抽象方法 —— 没有方法体，由子类各自实现
     * 子类必须实现这个方法，否则编译会报错。
     * 这是"多态"的核心：同一条 displayInfo() 调用，
     * 在 Player 和 Admin 身上产生不同的输出。
     */
    public abstract void displayInfo();

    @Override
    public String toString() {
        return id + " - " + name;
    }
}

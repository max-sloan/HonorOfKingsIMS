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
    private String password;     // 密码（明文存储）

    // 构造器：创建对象时初始化三个基本属性
    public Person(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
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
     * 用 .equals() 比较字符串，不能用 ==
     */
    public boolean checkPassword(String rawPassword) {
        return this.password.equals(rawPassword);
    }

    /**
     * 修改密码
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * 获取密码（供保存到文件使用）
     */
    public String getPassword() {
        return password;
    }

    /**
     * 抽象方法
     */
    public abstract void displayInfo();

    @Override
    public String toString() {
        return id + " - " + name;
    }
}

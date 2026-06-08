package model;

/**
 * Admin —— 管理员类，继承 Person
 *
 * Admin 也继承 Person，但比 Player 简单得多。
 * 它存在的意义不是属性多，而是作为"权限检查"的分水岭——
 * 当系统判断当前用户是 Admin 还是 Player 时，
 * 用的是 instanceof 关键字。
 */
public class Admin extends Person {
    private String role;  // 管理角色，如 "超级管理员"

    public Admin(int id, String name, String password, String role) {
        super(id, name, password);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public void displayInfo() {
        System.out.println("===== 管理员信息 =====");
        System.out.println("ID: " + getId());
        System.out.println("名字: " + getName());
        System.out.println("角色: " + role);
    }

    @Override
    public String toString() {
        return super.toString() + " [" + role + "]";
    }
}

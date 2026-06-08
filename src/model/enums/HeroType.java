package model.enums;

/**
 * 英雄类型枚举
 * 枚举（Enum）是一种特殊的类，用于定义一组固定的常量。
 * 例如：英雄的类型只有这6种，不会变。
 */
public enum HeroType {
    TANK("坦克"),
    WARRIOR("战士"),
    ASSASSIN("刺客"),
    MAGE("法师"),
    MARKSMAN("射手"),
    SUPPORT("辅助");

    private final String displayName;  // 中文显示名

    // 枚举的构造器默认是 private 的
    HeroType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

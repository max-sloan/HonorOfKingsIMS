package model.enums;

/**
 * 装备类型枚举
 */
public enum EquipmentType {
    ATTACK("攻击装"),
    DEFENSE("防御装"),
    MOVEMENT("移动装"),
    MAGIC("法术装"),
    JUNGLE("打野装"),
    SUPPORT("辅助装");

    private final String displayName;

    EquipmentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

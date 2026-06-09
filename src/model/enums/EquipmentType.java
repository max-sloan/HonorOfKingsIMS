package model.enums;

/**
 * Equipment type enum
 */
public enum EquipmentType {
    ATTACK("Attack"),
    DEFENSE("Defense"),
    MOVEMENT("Movement"),
    MAGIC("Magic"),
    JUNGLE("Jungle"),
    SUPPORT("Support");

    private final String displayName;

    EquipmentType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

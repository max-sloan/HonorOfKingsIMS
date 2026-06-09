package model.enums;

/**
 * Hero type enum
 */
public enum HeroType {
    TANK("Tank"),
    WARRIOR("Warrior"),
    ASSASSIN("Assassin"),
    MAGE("Mage"),
    MARKSMAN("Marksman"),
    SUPPORT("Support");

    private final String displayName;

    HeroType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

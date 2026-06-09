package model.enums;

/**
 * Match result enum
 */
public enum MatchResult {
    TEAM_A_WIN("Team A Wins"),
    TEAM_B_WIN("Team B Wins"),
    DRAW("Draw");

    private final String displayName;

    MatchResult(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

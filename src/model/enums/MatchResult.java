package model.enums;

/**
 * 比赛结果枚举
 */
public enum MatchResult {
    TEAM_A_WIN("队伍A获胜"),
    TEAM_B_WIN("队伍B获胜"),
    DRAW("平局");

    private final String displayName;

    MatchResult(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

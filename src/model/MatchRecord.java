package model;

import model.enums.MatchResult;
import model.interfaces.Reportable;

/**
 * MatchRecord —— 比赛记录类
 *
 * 注意：MatchRecord 不实现 Identifiable，因为它不需要按名字搜索。
 * 这体现了"接口"的自由性——不需要的接口可以不实现。
 *
 * 比赛记录一旦创建就不可修改（没有 setter），保证数据真实性。
 */
public class MatchRecord implements Reportable {
    private int id;
    private int teamAId;            // 队伍A的ID
    private int teamBId;            // 队伍B的ID
    private MatchResult result;     // 比赛结果（枚举）
    private int winningTeamId;      // 获胜队伍ID（平局时为 -1）
    private int duration;           // 比赛时长（秒）
    private String matchTime;       // 比赛时间，如 "2024-03-15 14:30"

    public MatchRecord(int id, int teamAId, int teamBId,
                       MatchResult result, int winningTeamId,
                       int duration, String matchTime) {
        this.id = id;
        this.teamAId = teamAId;
        this.teamBId = teamBId;
        this.result = result;
        this.winningTeamId = winningTeamId;
        this.duration = duration;
        this.matchTime = matchTime;
    }

    // === getter（只有 getter，没有 setter——不可修改） ===
    public int getId() { return id; }
    public int getTeamAId() { return teamAId; }
    public int getTeamBId() { return teamBId; }
    public MatchResult getResult() { return result; }
    public int getWinningTeamId() { return winningTeamId; }
    public int getDuration() { return duration; }
    public String getMatchTime() { return matchTime; }

    /**
     * 打印比赛详情
     */
    public void displayInfo() {
        System.out.println("===== 比赛记录 =====");
        System.out.println("ID: " + id);
        System.out.println("时间: " + matchTime);
        System.out.println("队伍A(ID:" + teamAId + ") vs 队伍B(ID:" + teamBId + ")");
        System.out.println("结果: " + result.getDisplayName());
        if (winningTeamId != -1) {
            System.out.println("胜方: 队伍" + winningTeamId);
        }
        System.out.println("时长: " + (duration / 60) + "分" + (duration % 60) + "秒");
    }

    @Override
    public String generateReport() {
        return matchTime + ": 队伍" + teamAId + " vs 队伍" + teamBId
                + " → " + result.getDisplayName();
    }

    @Override
    public String toString() {
        return "Match#" + id + " [" + matchTime + "]";
    }
}

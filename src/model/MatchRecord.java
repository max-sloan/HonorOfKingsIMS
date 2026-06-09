package model;

import model.enums.MatchResult;
import model.interfaces.Reportable;

/**
 * MatchRecord class
 *
 * Does NOT implement Identifiable - match records don't need name-based search.
 * Immutable: no setters, data cannot be modified after creation.
 */
public class MatchRecord implements Reportable {
    private int id;
    private int teamAId;
    private int teamBId;
    private MatchResult result;
    private int winningTeamId;
    private int duration;
    private String matchTime;

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

    public int getId() { return id; }
    public int getTeamAId() { return teamAId; }
    public int getTeamBId() { return teamBId; }
    public MatchResult getResult() { return result; }
    public int getWinningTeamId() { return winningTeamId; }
    public int getDuration() { return duration; }
    public String getMatchTime() { return matchTime; }

    public void displayInfo() {
        System.out.println("===== Match Record =====");
        System.out.println("ID: " + id);
        System.out.println("Time: " + matchTime);
        System.out.println("TeamA(ID:" + teamAId + ") vs TeamB(ID:" + teamBId + ")");
        System.out.println("Result: " + result.getDisplayName());
        if (winningTeamId != -1) {
            System.out.println("Winner: Team " + winningTeamId);
        }
        System.out.println("Duration: " + (duration / 60) + "m" + (duration % 60) + "s");
    }

    @Override
    public String generateReport() {
        return matchTime + ": Team" + teamAId + " vs Team" + teamBId
                + " -> " + result.getDisplayName();
    }

    @Override
    public String toString() {
        return "Match#" + id + " [" + matchTime + "]";
    }
}

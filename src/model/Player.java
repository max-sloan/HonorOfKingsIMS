package model;

import model.interfaces.Reportable;
import java.util.ArrayList;
import java.util.List;

public class Player extends Person implements Reportable {
    private int level;
    private int rankScore;
    private int totalMatches;
    private int wins;
    private double winRate;
    private int teamId;
    private String teamName;
    private List<Integer> heroIdList;

    public Player(int id, String name, String password, int level, int rankScore) {
        super(id, name, password);
        this.level = level;
        this.rankScore = rankScore;
        this.totalMatches = 0;
        this.wins = 0;
        this.winRate = 0.0;
        this.teamId = -1;
        this.teamName = "No Team";
        this.heroIdList = new ArrayList<>();
    }

    public double getWinRate() {
        return winRate;
    }

    public void updateWinRate() {
        if (totalMatches == 0) {
            this.winRate = 0.0;
        } else {
            this.winRate = (double) wins / totalMatches * 100;
        }
    }

    public void incrementMatch(boolean isWin) {
        this.totalMatches++;
        if (isWin) {
            this.wins++;
        }
        updateWinRate();
    }

    public void addHero(int heroId) {
        if (!heroIdList.contains(heroId)) {
            heroIdList.add(heroId);
        }
    }

    public void removeHero(int heroId) {
        heroIdList.remove(Integer.valueOf(heroId));
    }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getRankScore() { return rankScore; }
    public void setRankScore(int rankScore) { this.rankScore = rankScore; }

    public int getTotalMatches() { return totalMatches; }
    public void setTotalMatches(int totalMatches) { this.totalMatches = totalMatches; }
    public int getWins() { return wins; }
    public void setWins(int wins) { this.wins = wins; }

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public List<Integer> getHeroIdList() {
        return heroIdList;
    }

    @Override
    public String getRole() {
        return "Player";
    }

    @Override
    public void displayInfo() {
        System.out.println("===== Player Info =====");
        System.out.println("ID: " + getId());
        System.out.println("Name: " + getName());
        System.out.println("Level: " + level);
        System.out.println("Rank Score: " + rankScore);
        System.out.println("Win Rate: " + String.format("%.1f%%", getWinRate()));
        System.out.println("Matches: " + totalMatches + " | Wins: " + wins);
        System.out.println("Heroes Owned: " + heroIdList.size());
        System.out.println("Team: " + teamName);
    }

    @Override
    public String generateReport() {
        return getName() + " Lv." + level + " WR:" + String.format("%.1f%%", getWinRate());
    }

    @Override
    public String toString() {
        return super.toString() + " [Lv." + level + "]";
    }
}

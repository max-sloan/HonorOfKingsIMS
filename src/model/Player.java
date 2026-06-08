package model;

import model.interfaces.Reportable;
import java.util.ArrayList;
import java.util.List;

/**
 * Player —— 玩家类，继承 Person
 *
 * "继承"（Inheritance）用 extends 关键字。
 * Player 自动拥有 Person 的 id, name, passwordHash 属性和所有方法。
 * 同时实现了 Reportable 接口。
 *
 * Player 通过 "teamId" 和 "heroIdList" 与其他类关联，
 * 而不是直接持有 Team 或 Hero 对象——这叫"ID引用"。
 * 好处：避免循环引用，数据查找通过 GameDataManager 完成。
 */
public class Player extends Person implements Reportable {
    private int level;                  // 等级 (1-30)
    private int rankScore;              // 排位积分
    private int totalMatches;           // 总比赛场次
    private int wins;                   // 胜场数
    private double winRate;             // 个人胜率（百分比，0.0~100.0）
    private int teamId;                 // 所属战队ID（-1表示无战队）
    private String teamName;            // 所属战队名（用于显示）
    private List<Integer> heroIdList;   // 拥有的英雄ID列表

    public Player(int id, String name, String password, int level, int rankScore) {
        super(id, name, password);      // super() 调用父类 Person 的构造器
        this.level = level;
        this.rankScore = rankScore;
        this.totalMatches = 0;
        this.wins = 0;
        this.winRate = 0.0;
        this.teamId = -1;               // 默认无战队
        this.teamName = "无战队";
        this.heroIdList = new ArrayList<>();
    }

    /**
     * 获取个人胜率（已计算好的值）
     * @return 胜率百分比
     */
    public double getWinRate() {
        return winRate;
    }

    /**
     * 重新计算胜率，在比赛数据更新后调用
     */
    public void updateWinRate() {
        if (totalMatches == 0) {
            this.winRate = 0.0;
        } else {
            this.winRate = (double) wins / totalMatches * 100;
        }
    }

    /**
     * 比赛后更新统计（自动重算胜率）
     * @param isWin 是否赢了
     */
    public void incrementMatch(boolean isWin) {
        this.totalMatches++;
        if (isWin) {
            this.wins++;
        }
        updateWinRate();  // 自动重新计算胜率
    }

    // === 英雄管理 ===
    public void addHero(int heroId) {
        if (!heroIdList.contains(heroId)) {
            heroIdList.add(heroId);
        }
    }

    public void removeHero(int heroId) {
        heroIdList.remove(Integer.valueOf(heroId));
    }

    // === getter/setter ===
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getRankScore() { return rankScore; }
    public void setRankScore(int rankScore) { this.rankScore = rankScore; }

    public int getTotalMatches() { return totalMatches; }
    public int getWins() { return wins; }

    public int getTeamId() { return teamId; }
    public void setTeamId(int teamId) { this.teamId = teamId; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public List<Integer> getHeroIdList() {
        return heroIdList;
    }

    // === 覆盖父类方法 ===
    @Override
    public void displayInfo() {
        System.out.println("===== 玩家信息 =====");
        System.out.println("ID: " + getId());
        System.out.println("名字: " + getName());
        System.out.println("等级: " + level);
        System.out.println("排位分: " + rankScore);
        System.out.println("胜率: " + String.format("%.1f%%", getWinRate()));
        System.out.println("总场次: " + totalMatches + " | 胜场: " + wins);
        System.out.println("拥有英雄数: " + heroIdList.size());
        System.out.println("战队: " + teamName);
    }

    @Override
    public String generateReport() {
        return getName() + " Lv." + level + " 胜率" + String.format("%.1f%%", getWinRate());
    }

    @Override
    public String toString() {
        return super.toString() + " [Lv." + level + "]";
    }
}

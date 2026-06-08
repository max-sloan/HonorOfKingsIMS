package model;

import model.interfaces.Identifiable;
import model.interfaces.Reportable;
import java.util.ArrayList;
import java.util.List;

/**
 * Team —— 战队类
 *
 * 同时实现 Identifiable 和 Reportable 两个接口。
 * 一个类可以实现多个接口（但不能继承多个父类）。
 *
 * Team 通过 playerIds 和 matchRecordIds 列表来关联玩家和比赛，
 * 存的是 ID，不是对象——统一的 ID 引用模式。
 */
public class Team implements Identifiable, Reportable {
    private int id;
    private String name;
    private String createdDate;               // 创建日期，如 "2024-01-01"
    private List<Integer> playerIds;          // 成员玩家ID列表
    private List<Integer> matchRecordIds;     // 参与的比赛ID列表

    public Team(int id, String name, String createdDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.playerIds = new ArrayList<>();
        this.matchRecordIds = new ArrayList<>();
    }

    // === 成员管理 ===
    public void addPlayer(int playerId) {
        if (!playerIds.contains(playerId)) {
            playerIds.add(playerId);
        }
    }

    public void removePlayer(int playerId) {
        playerIds.remove(Integer.valueOf(playerId));
    }

    public void addMatchRecord(int matchId) {
        if (!matchRecordIds.contains(matchId)) {
            matchRecordIds.add(matchId);
        }
    }

    // === getter ===
    @Override
    public int getId() { return id; }
    @Override
    public String getName() { return name; }
    public String getCreatedDate() { return createdDate; }
    public List<Integer> getPlayerIds() { return playerIds; }
    public List<Integer> getMatchRecordIds() { return matchRecordIds; }

    /**
     * 打印战队概要
     */
    public void displayInfo() {
        System.out.println("===== 战队概要 =====");
        System.out.println("名称: " + name);
        System.out.println("创建日期: " + createdDate);
        System.out.println("成员数: " + playerIds.size());
        System.out.println("比赛数: " + matchRecordIds.size());
    }

    @Override
    public String generateReport() {
        return "战队[" + name + "] 成员" + playerIds.size() + "人 比赛" + matchRecordIds.size() + "场";
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}

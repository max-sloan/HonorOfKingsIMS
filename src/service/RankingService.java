package service;

import model.*;
import java.util.*;

/**
 * RankingService —— 排名/排行榜服务
 *
 * 用 Java 的 Collections.sort() 和 Comparator 来排序。
 *
 * Comparator 是什么？—— 一个"比较器"，告诉 sort() 怎么比大小。
 * 例如按胜率从高到低：(a, b) -> Double.compare(b.getWinRate(), a.getWinRate())
 * 上面的 lambda 表达式意思是：比较 b 和 a，b 大就排在前面（降序）。
 */
public class RankingService {
    private GameDataManager dm;

    public RankingService() {
        this.dm = GameDataManager.getInstance();
    }

    /**
     * 按胜率排名（降序）
     * 平局处理：胜率相同时，按总场次降序
     */
    public List<Player> getWinRateRanking(int topN) {
        List<Player> players = new ArrayList<>(dm.getAllPlayers());
        players.sort((a, b) -> {
            int cmp = Double.compare(b.getWinRate(), a.getWinRate());  // 胜率降序
            if (cmp == 0) {
                cmp = Integer.compare(b.getTotalMatches(), a.getTotalMatches()); // 平局看场次
            }
            return cmp;
        });
        // 只返回前 topN 名
        return players.subList(0, Math.min(topN, players.size()));
    }

    /**
     * 按等级排名（降序）
     * 平局处理：等级相同时，按排位分降序
     */
    public List<Player> getLevelRanking(int topN) {
        List<Player> players = new ArrayList<>(dm.getAllPlayers());
        players.sort((a, b) -> {
            int cmp = Integer.compare(b.getLevel(), a.getLevel());
            if (cmp == 0) {
                cmp = Integer.compare(b.getRankScore(), a.getRankScore());
            }
            return cmp;
        });
        return players.subList(0, Math.min(topN, players.size()));
    }

    /**
     * 按总场次排名（降序）
     */
    public List<Player> getMatchCountRanking(int topN) {
        List<Player> players = new ArrayList<>(dm.getAllPlayers());
        players.sort((a, b) -> Integer.compare(b.getTotalMatches(), a.getTotalMatches()));
        return players.subList(0, Math.min(topN, players.size()));
    }

    /**
     * 装备按使用次数排名（降序）
     */
    public List<Equipment> getEquipmentUsageRanking() {
        List<Equipment> equips = new ArrayList<>(dm.getAllEquipments());
        equips.sort((a, b) -> Integer.compare(b.getUsageCount(), a.getUsageCount()));
        return equips;
    }

    /**
     * 战队按胜率排名
     * 胜率 = 该队所有比赛中的胜场 / 总场
     */
    public List<Team> getTeamWinRateRanking() {
        List<Team> teams = new ArrayList<>(dm.getAllTeams());
        teams.sort((a, b) -> {
            double rateA = calculateTeamWinRate(a);
            double rateB = calculateTeamWinRate(b);
            return Double.compare(rateB, rateA);  // 降序
        });
        return teams;
    }

    /**
     * 计算一支战队的胜率
     */
    public double calculateTeamWinRate(Team team) {
        List<Integer> matchIds = team.getMatchRecordIds();
        if (matchIds.isEmpty()) return 0.0;

        int wins = 0;
        for (int matchId : matchIds) {
            MatchRecord m = dm.findMatchById(matchId);
            if (m != null && m.getWinningTeamId() == team.getId()) {
                wins++;
            }
        }
        return (double) wins / matchIds.size() * 100;
    }

    /**
     * 找出战队的"最强队员"（胜率最高者）
     */
    public Player getTopPlayerInTeam(int teamId) {
        Team team = dm.findTeamById(teamId);
        if (team == null || team.getPlayerIds().isEmpty()) return null;

        Player best = null;
        double bestRate = -1;
        for (int playerId : team.getPlayerIds()) {
            Player p = dm.findPlayerById(playerId);
            if (p != null && p.getWinRate() > bestRate) {
                bestRate = p.getWinRate();
                best = p;
            }
        }
        return best;
    }

    /**
     * 计算战队的平均等级
     */
    public double calculateTeamAvgLevel(int teamId) {
        Team team = dm.findTeamById(teamId);
        if (team == null || team.getPlayerIds().isEmpty()) return 0;

        int sum = 0;
        for (int playerId : team.getPlayerIds()) {
            Player p = dm.findPlayerById(playerId);
            if (p != null) sum += p.getLevel();
        }
        return (double) sum / team.getPlayerIds().size();
    }
}

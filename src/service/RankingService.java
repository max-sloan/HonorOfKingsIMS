package service;

import model.*;
import java.util.*;

/**
 * RankingService - leaderboard and ranking calculations
 *
 * Uses Collections.sort() with Comparator lambdas.
 * Comparator tells sort() how to compare two objects.
 * e.g. (a, b) -> Double.compare(b.rate, a.rate) means descending by rate.
 */
public class RankingService {
    private GameDataManager dm;

    public RankingService() {
        this.dm = GameDataManager.getInstance();
    }

    /** Rank by win rate (descending). Tie-break: total matches */
    public List<Player> getWinRateRanking(int topN) {
        List<Player> players = new ArrayList<>(dm.getAllPlayers());
        players.sort((a, b) -> {
            int cmp = Double.compare(b.getWinRate(), a.getWinRate());
            if (cmp == 0) {
                cmp = Integer.compare(b.getTotalMatches(), a.getTotalMatches());
            }
            return cmp;
        });
        return players.subList(0, Math.min(topN, players.size()));
    }

    /** Rank by level (descending). Tie-break: rank score */
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

    /** Rank by total matches (descending) */
    public List<Player> getMatchCountRanking(int topN) {
        List<Player> players = new ArrayList<>(dm.getAllPlayers());
        players.sort((a, b) -> Integer.compare(b.getTotalMatches(), a.getTotalMatches()));
        return players.subList(0, Math.min(topN, players.size()));
    }

    /** Rank equipment by usage count (descending) */
    public List<Equipment> getEquipmentUsageRanking() {
        List<Equipment> equips = new ArrayList<>(dm.getAllEquipments());
        equips.sort((a, b) -> Integer.compare(b.getUsageCount(), a.getUsageCount()));
        return equips;
    }

    /** Rank teams by win rate */
    public List<Team> getTeamWinRateRanking() {
        List<Team> teams = new ArrayList<>(dm.getAllTeams());
        teams.sort((a, b) -> {
            double rateA = calculateTeamWinRate(a);
            double rateB = calculateTeamWinRate(b);
            return Double.compare(rateB, rateA);
        });
        return teams;
    }

    /** Calculate a team's win rate */
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

    /** Find best player in a team (highest win rate) */
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

    /** Calculate average level of a team */
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

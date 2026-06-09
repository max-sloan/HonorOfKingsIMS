package service;

import model.*;
import model.enums.*;
import java.util.*;

/**
 * AdminDataService - admin-only data CRUD operations
 *
 * Handles add, delete, update for all data types with cascade cleanup.
 */
public class AdminDataService {
    private GameDataManager dm;

    public AdminDataService() {
        this.dm = GameDataManager.getInstance();
    }

    // ============ Add operations ============

    public Player addPlayer(String name, String password, int level, int rankScore) {
        int id = dm.getNextPlayerId();
        Player p = new Player(id, name, password, level, rankScore);
        dm.addPlayer(p);
        return p;
    }

    public Hero addHero(String name, HeroType heroType, int difficulty, String description) {
        int id = dm.getNextHeroId();
        Hero h = new Hero(id, name, heroType, difficulty, description);
        dm.addHero(h);
        return h;
    }

    public Equipment addEquipment(String name, EquipmentType equipType, int price) {
        int id = dm.getNextEquipmentId();
        Equipment e = new Equipment(id, name, equipType, price);
        dm.addEquipment(e);
        return e;
    }

    public Team addTeam(String name, String createdDate) {
        int id = dm.getNextTeamId();
        Team t = new Team(id, name, createdDate);
        dm.addTeam(t);
        return t;
    }

    public MatchRecord addMatchRecord(int teamAId, int teamBId, MatchResult result,
                                       int winningTeamId, int duration, String matchTime) {
        int id = dm.getNextMatchId();
        MatchRecord m = new MatchRecord(id, teamAId, teamBId, result, winningTeamId, duration, matchTime);
        dm.addMatchRecord(m);

        // Add to both teams' match lists
        Team teamA = dm.findTeamById(teamAId);
        Team teamB = dm.findTeamById(teamBId);
        if (teamA != null) teamA.addMatchRecord(id);
        if (teamB != null) teamB.addMatchRecord(id);

        // Update player stats: all players get +1 match, winners get +1 win
        updatePlayerStats(dm, teamA, result == MatchResult.TEAM_A_WIN);
        updatePlayerStats(dm, teamB, result == MatchResult.TEAM_B_WIN);

        return m;
    }

    /** Update match stats for all players on a team */
    private void updatePlayerStats(GameDataManager dm, Team team, boolean isWinner) {
        if (team == null) return;
        for (int playerId : team.getPlayerIds()) {
            Player p = dm.findPlayerById(playerId);
            if (p != null) {
                p.incrementMatch(isWinner);
            }
        }
    }

    // ============ Delete operations (with cascade) ============

    public boolean deletePlayer(int id) {
        if (!dm.playerExists(id)) return false;
        dm.removePlayer(id);
        return true;
    }

    public boolean deleteHero(int id) {
        if (!dm.heroExists(id)) return false;
        dm.removeHero(id);
        return true;
    }

    public boolean deleteEquipment(int id) {
        if (!dm.equipmentExists(id)) return false;
        dm.removeEquipment(id);
        return true;
    }

    public boolean deleteTeam(int id) {
        if (!dm.teamExists(id)) return false;
        dm.removeTeam(id);
        return true;
    }

    public boolean deleteMatchRecord(int id) {
        if (dm.findMatchById(id) == null) return false;
        dm.removeMatchRecord(id);
        return true;
    }

    // ============ Update operations ============

    public boolean updatePlayerName(int id, String newName) {
        Player p = dm.findPlayerById(id);
        if (p == null) return false;
        p.setName(newName);
        return true;
    }

    public boolean updatePlayerPassword(int id, String newPassword) {
        Player p = dm.findPlayerById(id);
        if (p == null) return false;
        p.setPassword(newPassword);
        return true;
    }

    /** Assign a player to a team */
    public boolean assignPlayerToTeam(int playerId, int teamId) {
        Player p = dm.findPlayerById(playerId);
        Team t = dm.findTeamById(teamId);
        if (p == null || t == null) return false;

        if (p.getTeamId() != -1) {
            Team oldTeam = dm.findTeamById(p.getTeamId());
            if (oldTeam != null) oldTeam.removePlayer(playerId);
        }

        p.setTeamId(teamId);
        p.setTeamName(t.getName());
        t.addPlayer(playerId);
        return true;
    }

    /** Add recommended equipment to a hero */
    public boolean addEquipmentToHero(int heroId, int equipId) {
        Hero hero = dm.findHeroById(heroId);
        Equipment equip = dm.findEquipmentById(equipId);
        if (hero == null || equip == null) return false;

        hero.addEquipment(equipId);
        equip.incrementUsage();
        return true;
    }

    /** Give a hero to a player */
    public boolean addHeroToPlayer(int playerId, int heroId) {
        Player p = dm.findPlayerById(playerId);
        if (p == null || !dm.heroExists(heroId)) return false;
        p.addHero(heroId);
        return true;
    }
}

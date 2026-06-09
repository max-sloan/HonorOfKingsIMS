package service;

import model.*;
import java.util.*;

/**
 * GameDataManager - Central data store (Singleton pattern)
 *
 * The data brain of the system. All data lives in HashMaps here.
 *
 * Singleton: constructor is private, only accessible via getInstance().
 * Ensures only one data manager exists in the entire program.
 *
 * HashMap: Map<Integer, Player> means key=ID, value=Player object.
 * - put(id, player) to store
 * - get(id) to retrieve (O(1) fast lookup)
 * - containsKey(id) to check existence
 * - values() to get all values
 */
public class GameDataManager {
    private static GameDataManager instance;

    private Map<Integer, Player>      playerMap;
    private Map<Integer, Admin>       adminMap;
    private Map<Integer, Hero>        heroMap;
    private Map<Integer, Equipment>   equipmentMap;
    private Map<Integer, Team>        teamMap;
    private Map<Integer, MatchRecord> matchMap;

    private int nextPlayerId;
    private int nextHeroId;
    private int nextEquipmentId;
    private int nextTeamId;
    private int nextMatchId;

    private GameDataManager() {
        playerMap    = new HashMap<>();
        adminMap     = new HashMap<>();
        heroMap      = new HashMap<>();
        equipmentMap = new HashMap<>();
        teamMap      = new HashMap<>();
        matchMap     = new HashMap<>();

        nextPlayerId    = 1;
        nextHeroId      = 1;
        nextEquipmentId = 1;
        nextTeamId      = 1;
        nextMatchId     = 1;
    }

    public static GameDataManager getInstance() {
        if (instance == null) {
            instance = new GameDataManager();
        }
        return instance;
    }

    /** Clear all data (for reloading from file) */
    public void reset() {
        playerMap.clear();
        adminMap.clear();
        heroMap.clear();
        equipmentMap.clear();
        teamMap.clear();
        matchMap.clear();
        nextPlayerId    = 1;
        nextHeroId      = 1;
        nextEquipmentId = 1;
        nextTeamId      = 1;
        nextMatchId     = 1;
    }

    // ============ Player operations ============

    public void addPlayer(Player player) {
        playerMap.put(player.getId(), player);
        if (player.getId() >= nextPlayerId) {
            nextPlayerId = player.getId() + 1;
        }
    }

    public Player findPlayerById(int id) {
        return playerMap.get(id);
    }

    public List<Player> findPlayersByName(String keyword) {
        List<Player> result = new ArrayList<>();
        for (Player p : playerMap.values()) {
            if (p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(p);
            }
        }
        return result;
    }

    public void removePlayer(int id) {
        Player p = playerMap.remove(id);
        if (p != null && p.getTeamId() != -1) {
            Team team = findTeamById(p.getTeamId());
            if (team != null) {
                team.removePlayer(id);
            }
        }
    }

    public Collection<Player> getAllPlayers() {
        return playerMap.values();
    }

    public int getNextPlayerId() { return nextPlayerId++; }

    // ============ Admin operations ============

    public void addAdmin(Admin admin) {
        adminMap.put(admin.getId(), admin);
    }

    public Admin findAdminById(int id) {
        return adminMap.get(id);
    }

    public Collection<Admin> getAllAdmins() {
        return adminMap.values();
    }

    // ============ Hero operations ============

    public void addHero(Hero hero) {
        heroMap.put(hero.getId(), hero);
        if (hero.getId() >= nextHeroId) {
            nextHeroId = hero.getId() + 1;
        }
    }

    public Hero findHeroById(int id) {
        return heroMap.get(id);
    }

    public List<Hero> findHeroesByName(String keyword) {
        List<Hero> result = new ArrayList<>();
        for (Hero h : heroMap.values()) {
            if (h.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(h);
            }
        }
        return result;
    }

    public void removeHero(int id) {
        Hero hero = heroMap.remove(id);
        if (hero != null) {
            for (Player p : playerMap.values()) {
                p.removeHero(id);
            }
        }
    }

    public Collection<Hero> getAllHeroes() {
        return heroMap.values();
    }

    public int getNextHeroId() { return nextHeroId++; }

    // ============ Equipment operations ============

    public void addEquipment(Equipment equip) {
        equipmentMap.put(equip.getId(), equip);
        if (equip.getId() >= nextEquipmentId) {
            nextEquipmentId = equip.getId() + 1;
        }
    }

    public Equipment findEquipmentById(int id) {
        return equipmentMap.get(id);
    }

    public List<Equipment> findEquipmentByName(String keyword) {
        List<Equipment> result = new ArrayList<>();
        for (Equipment e : equipmentMap.values()) {
            if (e.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(e);
            }
        }
        return result;
    }

    public void removeEquipment(int id) {
        Equipment equip = equipmentMap.remove(id);
        if (equip != null) {
            for (Hero h : heroMap.values()) {
                h.getRecommendedEquipIds().remove(Integer.valueOf(id));
            }
        }
    }

    public Collection<Equipment> getAllEquipments() {
        return equipmentMap.values();
    }

    public int getNextEquipmentId() { return nextEquipmentId++; }

    // ============ Team operations ============

    public void addTeam(Team team) {
        teamMap.put(team.getId(), team);
        if (team.getId() >= nextTeamId) {
            nextTeamId = team.getId() + 1;
        }
    }

    public Team findTeamById(int id) {
        return teamMap.get(id);
    }

    public List<Team> findTeamsByName(String keyword) {
        List<Team> result = new ArrayList<>();
        for (Team t : teamMap.values()) {
            if (t.getName().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(t);
            }
        }
        return result;
    }

    public void removeTeam(int id) {
        Team team = teamMap.remove(id);
        if (team != null) {
            for (int playerId : team.getPlayerIds()) {
                Player p = findPlayerById(playerId);
                if (p != null) {
                    p.setTeamId(-1);
                    p.setTeamName("No Team");
                }
            }
        }
    }

    public Collection<Team> getAllTeams() {
        return teamMap.values();
    }

    public int getNextTeamId() { return nextTeamId++; }

    // ============ MatchRecord operations ============

    public void addMatchRecord(MatchRecord match) {
        matchMap.put(match.getId(), match);
        if (match.getId() >= nextMatchId) {
            nextMatchId = match.getId() + 1;
        }
    }

    public MatchRecord findMatchById(int id) {
        return matchMap.get(id);
    }

    public void removeMatchRecord(int id) {
        MatchRecord match = matchMap.remove(id);
        if (match != null) {
            Team teamA = findTeamById(match.getTeamAId());
            Team teamB = findTeamById(match.getTeamBId());
            if (teamA != null) teamA.getMatchRecordIds().remove(Integer.valueOf(id));
            if (teamB != null) teamB.getMatchRecordIds().remove(Integer.valueOf(id));
        }
    }

    public Collection<MatchRecord> getAllMatches() {
        return matchMap.values();
    }

    public int getNextMatchId() { return nextMatchId++; }

    // ============ Utility ============

    public boolean playerExists(int id) {
        return playerMap.containsKey(id);
    }

    public boolean heroExists(int id) {
        return heroMap.containsKey(id);
    }

    public boolean equipmentExists(int id) {
        return equipmentMap.containsKey(id);
    }

    public boolean teamExists(int id) {
        return teamMap.containsKey(id);
    }
}

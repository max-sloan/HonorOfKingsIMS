package service;

import model.*;
import java.util.*;

/**
 * GameDataManager —— 数据中心（单例模式）
 *
 * 这是整个系统的数据大脑，所有数据都存在这里的 HashMap 中。
 *
 * "单例模式"（Singleton）：
 * - 构造器是 private 的，外部不能 new
 * - 通过 getInstance() 获取唯一实例
 * - 保证整个程序只有一个数据管理器
 *
 * "HashMap"：
 * - Map<Integer, Player> 意思是：键=整数ID，值=Player对象
 * - put(id, player) 存入
 * - get(id) 取出（O(1) 快速查找）
 * - containsKey(id) 检查是否存在
 * - values() 获取所有值（返回 Collection）
 */
public class GameDataManager {
    // === 单例 ===
    private static GameDataManager instance;

    // === 数据存储（6个 HashMap） ===
    private Map<Integer, Player>      playerMap;
    private Map<Integer, Admin>       adminMap;
    private Map<Integer, Hero>        heroMap;
    private Map<Integer, Equipment>   equipmentMap;
    private Map<Integer, Team>        teamMap;
    private Map<Integer, MatchRecord> matchMap;

    // === 自增ID ===
    private int nextPlayerId;
    private int nextHeroId;
    private int nextEquipmentId;
    private int nextTeamId;
    private int nextMatchId;

    // 构造器设为 private，外部不能 new
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

    /**
     * 获取单例实例
     */
    public static GameDataManager getInstance() {
        if (instance == null) {
            instance = new GameDataManager();
        }
        return instance;
    }

    // ============================================
    //  Player 操作
    // ============================================

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
            // 从战队中移除该玩家
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

    // ============================================
    //  Admin 操作
    // ============================================

    public void addAdmin(Admin admin) {
        adminMap.put(admin.getId(), admin);
    }

    public Admin findAdminById(int id) {
        return adminMap.get(id);
    }

    public Collection<Admin> getAllAdmins() {
        return adminMap.values();
    }

    // ============================================
    //  Hero 操作
    // ============================================

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
            // 级联清理：从所有玩家的英雄列表中移除
            for (Player p : playerMap.values()) {
                p.removeHero(id);
            }
            // 从所有装备中减掉使用计数（这里简化处理）
        }
    }

    public Collection<Hero> getAllHeroes() {
        return heroMap.values();
    }

    public int getNextHeroId() { return nextHeroId++; }

    // ============================================
    //  Equipment 操作
    // ============================================

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
            // 级联清理：从所有英雄的推荐列表移除
            for (Hero h : heroMap.values()) {
                h.getRecommendedEquipIds().remove(Integer.valueOf(id));
            }
        }
    }

    public Collection<Equipment> getAllEquipments() {
        return equipmentMap.values();
    }

    public int getNextEquipmentId() { return nextEquipmentId++; }

    // ============================================
    //  Team 操作
    // ============================================

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
            // 级联清理：所有队员的 teamId 重置为 -1
            for (int playerId : team.getPlayerIds()) {
                Player p = findPlayerById(playerId);
                if (p != null) {
                    p.setTeamId(-1);
                    p.setTeamName("无战队");
                }
            }
        }
    }

    public Collection<Team> getAllTeams() {
        return teamMap.values();
    }

    public int getNextTeamId() { return nextTeamId++; }

    // ============================================
    //  MatchRecord 操作
    // ============================================

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
            // 级联清理：从两支队伍的记录列表移除
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

    // ============================================
    //  通用方法
    // ============================================

    /**
     * 检查玩家是否存在
     */
    public boolean playerExists(int id) {
        return playerMap.containsKey(id);
    }

    /**
     * 检查英雄是否存在
     */
    public boolean heroExists(int id) {
        return heroMap.containsKey(id);
    }

    /**
     * 检查装备是否存在
     */
    public boolean equipmentExists(int id) {
        return equipmentMap.containsKey(id);
    }

    /**
     * 检查战队是否存在
     */
    public boolean teamExists(int id) {
        return teamMap.containsKey(id);
    }
}

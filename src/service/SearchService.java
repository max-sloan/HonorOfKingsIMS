package service;

import model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SearchService —— 搜索服务
 *
 * 封装所有的查询逻辑。
 * 每个搜索方法都返回一个列表（可能为空），调用方再决定如何显示。
 */
public class SearchService {
    private GameDataManager dm;

    public SearchService() {
        this.dm = GameDataManager.getInstance();
    }

    /**
     * 按名字搜索玩家（模糊匹配）
     */
    public List<Player> searchPlayers(String keyword) {
        return dm.findPlayersByName(keyword);
    }

    /**
     * 按ID搜索玩家（精确匹配）
     */
    public Player searchPlayerById(int id) {
        return dm.findPlayerById(id);
    }

    /**
     * 按名字搜索战队
     */
    public List<Team> searchTeams(String keyword) {
        return dm.findTeamsByName(keyword);
    }

    /**
     * 按ID搜索战队
     */
    public Team searchTeamById(int id) {
        return dm.findTeamById(id);
    }

    /**
     * 按名字搜索英雄
     */
    public List<Hero> searchHeroes(String keyword) {
        return dm.findHeroesByName(keyword);
    }

    /**
     * 按ID搜索英雄
     */
    public Hero searchHeroById(int id) {
        return dm.findHeroById(id);
    }

    /**
     * 按名字搜索装备
     */
    public List<Equipment> searchEquipment(String keyword) {
        return dm.findEquipmentByName(keyword);
    }

    /**
     * 获取拥有某个英雄的所有玩家
     * 遍历所有玩家，检查 heroIdList 中是否包含该英雄ID
     */
    public List<Player> getPlayersOwningHero(int heroId) {
        List<Player> result = new ArrayList<>();
        for (Player p : dm.getAllPlayers()) {
            if (p.getHeroIdList().contains(heroId)) {
                result.add(p);
            }
        }
        return result;
    }

    /**
     * 获取某个战队的所有比赛记录
     */
    public List<MatchRecord> getTeamMatches(int teamId) {
        List<MatchRecord> result = new ArrayList<>();
        for (MatchRecord m : dm.getAllMatches()) {
            if (m.getTeamAId() == teamId || m.getTeamBId() == teamId) {
                result.add(m);
            }
        }
        return result;
    }

    /**
     * 获取某个战队的所有成员
     */
    public List<Player> getTeamPlayers(int teamId) {
        List<Player> result = new ArrayList<>();
        Team team = dm.findTeamById(teamId);
        if (team != null) {
            for (int playerId : team.getPlayerIds()) {
                Player p = dm.findPlayerById(playerId);
                if (p != null) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    /**
     * 生成玩家完整报告（包含本类信息和拥有的英雄+装备）
     */
    public String getPlayerFullReport(Player player) {
        StringBuilder sb = new StringBuilder();
        sb.append("══════ 玩家完整报告 ══════\n");
        sb.append("名字: ").append(player.getName()).append("\n");
        sb.append("等级: ").append(player.getLevel()).append("\n");
        sb.append("胜率: ").append(String.format("%.1f%%", player.getWinRate())).append("\n");
        sb.append("战队: ").append(player.getTeamName()).append("\n");
        sb.append("\n--- 拥有的英雄 ---\n");
        for (int heroId : player.getHeroIdList()) {
            Hero hero = dm.findHeroById(heroId);
            if (hero != null) {
                sb.append("  ").append(hero.getName())
                  .append(" [").append(hero.getHeroType().getDisplayName()).append("]\n");
                // 显示该英雄的推荐装备
                for (int equipId : hero.getRecommendedEquipIds()) {
                    Equipment equip = dm.findEquipmentById(equipId);
                    if (equip != null) {
                        sb.append("    └─ ").append(equip.getName()).append("\n");
                    }
                }
            }
        }
        return sb.toString();
    }
}

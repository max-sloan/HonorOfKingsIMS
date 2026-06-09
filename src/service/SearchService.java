package service;

import model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SearchService - handles all search/query logic
 */
public class SearchService {
    private GameDataManager dm;

    public SearchService() {
        this.dm = GameDataManager.getInstance();
    }

    public List<Player> searchPlayers(String keyword) {
        return dm.findPlayersByName(keyword);
    }

    public Player searchPlayerById(int id) {
        return dm.findPlayerById(id);
    }

    public List<Team> searchTeams(String keyword) {
        return dm.findTeamsByName(keyword);
    }

    public Team searchTeamById(int id) {
        return dm.findTeamById(id);
    }

    public List<Hero> searchHeroes(String keyword) {
        return dm.findHeroesByName(keyword);
    }

    public Hero searchHeroById(int id) {
        return dm.findHeroById(id);
    }

    public List<Equipment> searchEquipment(String keyword) {
        return dm.findEquipmentByName(keyword);
    }

    /** Find all players who own a specific hero */
    public List<Player> getPlayersOwningHero(int heroId) {
        List<Player> result = new ArrayList<>();
        for (Player p : dm.getAllPlayers()) {
            if (p.getHeroIdList().contains(heroId)) {
                result.add(p);
            }
        }
        return result;
    }

    /** Get all matches involving a team */
    public List<MatchRecord> getTeamMatches(int teamId) {
        List<MatchRecord> result = new ArrayList<>();
        for (MatchRecord m : dm.getAllMatches()) {
            if (m.getTeamAId() == teamId || m.getTeamBId() == teamId) {
                result.add(m);
            }
        }
        return result;
    }

    /** Get all members of a team */
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

    /** Generate a full report for a player (including heroes and equipment) */
    public String getPlayerFullReport(Player player) {
        StringBuilder sb = new StringBuilder();
        sb.append("========== Player Full Report ==========\n");
        sb.append("Name: ").append(player.getName()).append("\n");
        sb.append("Level: ").append(player.getLevel()).append("\n");
        sb.append("Win Rate: ").append(String.format("%.1f%%", player.getWinRate())).append("\n");
        sb.append("Team: ").append(player.getTeamName()).append("\n");
        sb.append("\n--- Owned Heroes ---\n");
        for (int heroId : player.getHeroIdList()) {
            Hero hero = dm.findHeroById(heroId);
            if (hero != null) {
                sb.append("  ").append(hero.getName())
                  .append(" [").append(hero.getHeroType().getDisplayName()).append("]\n");
                for (int equipId : hero.getRecommendedEquipIds()) {
                    Equipment equip = dm.findEquipmentById(equipId);
                    if (equip != null) {
                        sb.append("    + ").append(equip.getName()).append("\n");
                    }
                }
            }
        }
        return sb.toString();
    }
}

package service;

import model.*;
import model.enums.*;
import java.util.*;

/**
 * RecommendationService — recommends heroes and equipment based on
 * player preference, hero win rate, team composition, and equipment usage.
 *
 * Scoring formulas are defined in docs/recommendation-formula.md.
 * All sub-scores range from 0 to 100. Final score = weighted sum.
 */
public class RecommendationService {
    private GameDataManager dm;

    public RecommendationService() {
        this.dm = GameDataManager.getInstance();
    }

    // ==================== HERO RECOMMENDATION ====================

    /**
     * Recommend top N heroes for a player, considering their team composition.
     */
    public List<Hero> recommendHeroesForPlayer(Player player, Team team, int topN) {
        List<Hero> allHeroes = new ArrayList<>(dm.getAllHeroes());
        Map<Integer, Double> scores = new HashMap<>();

        for (Hero hero : allHeroes) {
            double score = calculateHeroScore(player, hero, team);
            scores.put(hero.getId(), score);
        }

        // Sort by score descending
        allHeroes.sort((a, b) -> Double.compare(scores.get(b.getId()), scores.get(a.getId())));

        // Return top N
        int count = Math.min(topN, allHeroes.size());
        return allHeroes.subList(0, count);
    }

    /**
     * Calculate the full hero recommendation score for a specific player+hero+team.
     */
    public double calculateHeroScore(Player player, Hero hero, Team team) {
        double score = 0.0;
        score += 0.30 * calcTypePreference(player, hero);
        score += 0.25 * calcHeroWinRateScore(hero);
        score += 0.20 * calcTeamNeedScore(team, hero);
        score += 0.15 * calcPlayerHistoryScore(player, hero);
        score += 0.10 * calcEquipmentCompatibilityScore(hero);
        return score;
    }

    // ---- Hero sub-scores ----

    private double calcTypePreference(Player player, Hero hero) {
        // Count owned heroes by type
        Map<HeroType, Integer> typeCount = new HashMap<>();
        for (int hid : player.getHeroIdList()) {
            Hero h = dm.findHeroById(hid);
            if (h != null) {
                HeroType ht = h.getHeroType();
                typeCount.put(ht, typeCount.getOrDefault(ht, 0) + 1);
            }
        }

        if (typeCount.isEmpty()) return 50; // No heroes owned → neutral

        // Find the player's most-used type
        HeroType favoriteType = null;
        int maxCount = 0;
        for (Map.Entry<HeroType, Integer> entry : typeCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                favoriteType = entry.getKey();
            }
        }

        if (hero.getHeroType() == favoriteType) return 100;
        if (typeCount.containsKey(hero.getHeroType())) return 60;
        return 20;
    }

    private double calcHeroWinRateScore(Hero hero) {
        return Math.min(hero.getHeroWinRate(), 100);
    }

    private double calcTeamNeedScore(Team team, Hero hero) {
        if (team == null) return 50; // No team → neutral score

        // Count how many heroes of this type the team already has
        int count = 0;
        for (int pid : team.getPlayerIds()) {
            Player p = dm.findPlayerById(pid);
            if (p != null) {
                for (int hid : p.getHeroIdList()) {
                    Hero h = dm.findHeroById(hid);
                    if (h != null && h.getHeroType() == hero.getHeroType()) {
                        count++;
                    }
                }
            }
        }

        if (count == 0) return 100; // Team lacks this type entirely
        if (count == 1) return 50;
        return 10;
    }

    private double calcPlayerHistoryScore(Player player, Hero hero) {
        if (player.getHeroIdList().contains(hero.getId())) return 0;
        return 100;
    }

    private double calcEquipmentCompatibilityScore(Hero hero) {
        return Math.min(hero.getRecommendedEquipIds().size() * 20, 100);
    }

    // ==================== EQUIPMENT RECOMMENDATION ====================

    /**
     * Recommend top N equipment items for a specific hero and player.
     */
    public List<Equipment> recommendEquipmentForHero(Hero hero, Player player, int topN) {
        List<Equipment> allEquip = new ArrayList<>(dm.getAllEquipments());
        Map<Integer, Double> scores = new HashMap<>();

        for (Equipment equip : allEquip) {
            double score = calculateEquipmentScore(player, hero, equip);
            scores.put(equip.getId(), score);
        }

        allEquip.sort((a, b) -> Double.compare(scores.get(b.getId()), scores.get(a.getId())));

        int count = Math.min(topN, allEquip.size());
        return allEquip.subList(0, count);
    }

    /**
     * Calculate the full equipment recommendation score.
     */
    public double calculateEquipmentScore(Player player, Hero hero, Equipment equipment) {
        double score = 0.0;
        score += 0.35 * calcHeroTypeMatchScore(hero, equipment);
        score += 0.30 * calcUsageCountScore(equipment);
        score += 0.20 * calcWinRateSupportScore(equipment);
        score += 0.15 * calcPlayerPreferenceScore(player, equipment);
        return score;
    }

    // ---- Equipment sub-scores ----

    private double calcHeroTypeMatchScore(Hero hero, Equipment equip) {
        if (isEquipmentCompatible(hero.getHeroType(), equip.getEquipType())) return 100;
        return 15;
    }

    /**
     * Check if an equipment type is suitable for a hero type.
     * Based on game logic:
     * - ATTACK  → Assassin, Marksman, Warrior
     * - DEFENSE → Tank, Warrior
     * - MAGIC   → Mage
     * - MOVEMENT → All types
     * - JUNGLE  → Assassin, Warrior
     * - SUPPORT → Support, Tank
     */
    private boolean isEquipmentCompatible(HeroType heroType, EquipmentType equipType) {
        switch (equipType) {
            case ATTACK:
                return heroType == HeroType.ASSASSIN
                    || heroType == HeroType.MARKSMAN
                    || heroType == HeroType.WARRIOR;
            case DEFENSE:
                return heroType == HeroType.TANK
                    || heroType == HeroType.WARRIOR;
            case MAGIC:
                return heroType == HeroType.MAGE;
            case MOVEMENT:
                return true; // All heroes benefit from movement
            case JUNGLE:
                return heroType == HeroType.ASSASSIN
                    || heroType == HeroType.WARRIOR;
            case SUPPORT:
                return heroType == HeroType.SUPPORT
                    || heroType == HeroType.TANK;
            default:
                return false;
        }
    }

    private double calcUsageCountScore(Equipment equip) {
        return Math.min(equip.getUsageCount() * 10, 100);
    }

    private double calcWinRateSupportScore(Equipment equip) {
        // Average win rate of heroes that recommend this equipment
        double totalRate = 0;
        int count = 0;
        for (Hero h : dm.getAllHeroes()) {
            if (h.getRecommendedEquipIds().contains(equip.getId())) {
                totalRate += h.getHeroWinRate();
                count++;
            }
        }
        if (count == 0) return 0;
        return totalRate / count;
    }

    private double calcPlayerPreferenceScore(Player player, Equipment equip) {
        int totalHeroes = player.getHeroIdList().size();
        if (totalHeroes == 0) return 50; // Neutral

        int matchCount = 0;
        for (int hid : player.getHeroIdList()) {
            Hero h = dm.findHeroById(hid);
            if (h != null && h.getRecommendedEquipIds().contains(equip.getId())) {
                matchCount++;
            }
        }

        return (double) matchCount / totalHeroes * 100;
    }
}

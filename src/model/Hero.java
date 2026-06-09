package model;

import model.enums.HeroType;
import model.interfaces.Identifiable;
import java.util.ArrayList;
import java.util.List;

/**
 * Hero class
 *
 * Does NOT extend Person - heroes are not people.
 * Instead implements Identifiable directly, showing how interfaces
 * can unify unrelated classes.
 */
public class Hero implements Identifiable {
    private int id;
    private String name;
    private HeroType heroType;
    private int difficulty;
    private String description;
    private List<Integer> recommendedEquipIds;

    public Hero(int id, String name, HeroType heroType, int difficulty, String description) {
        this.id = id;
        this.name = name;
        this.heroType = heroType;
        this.difficulty = difficulty;
        this.description = description;
        this.recommendedEquipIds = new ArrayList<>();
    }

    public void addEquipment(int equipId) {
        if (!recommendedEquipIds.contains(equipId)) {
            recommendedEquipIds.add(equipId);
        }
    }

    @Override
    public int getId() { return id; }
    @Override
    public String getName() { return name; }
    public HeroType getHeroType() { return heroType; }
    public int getDifficulty() { return difficulty; }
    public String getDescription() { return description; }
    public List<Integer> getRecommendedEquipIds() { return recommendedEquipIds; }

    public void displayInfo() {
        System.out.println("===== Hero Details =====");
        System.out.println("Name: " + name);
        System.out.println("Type: " + heroType.getDisplayName());
        System.out.println("Difficulty: " + difficulty + "/10");
        System.out.println("Description: " + description);
        System.out.println("Recommended Equipment: " + recommendedEquipIds.size());
    }

    @Override
    public String toString() {
        return id + " - " + name + " [" + heroType.getDisplayName() + "]";
    }
}

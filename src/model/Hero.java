package model;

import model.enums.HeroType;
import model.interfaces.Identifiable;
import java.util.ArrayList;
import java.util.List;

/**
 * Hero —— 英雄类
 *
 * 不继承 Person（英雄不是人），而是直接实现 Identifiable 接口。
 * 这展示了"接口"的另一种用法：不相关的类可以通过接口统一处理。
 */
public class Hero implements Identifiable {
    private int id;
    private String name;
    private HeroType heroType;              // 英雄定位（使用枚举）
    private int difficulty;                 // 难度 (1-10)
    private String description;             // 简介

    // 推荐装备ID列表——存的是装备的ID，不是装备对象
    private List<Integer> recommendedEquipIds;

    public Hero(int id, String name, HeroType heroType, int difficulty, String description) {
        this.id = id;
        this.name = name;
        this.heroType = heroType;
        this.difficulty = difficulty;
        this.description = description;
        this.recommendedEquipIds = new ArrayList<>();
    }

    // === 装备管理 ===
    public void addEquipment(int equipId) {
        if (!recommendedEquipIds.contains(equipId)) {
            recommendedEquipIds.add(equipId);
        }
    }

    // === getter ===
    @Override
    public int getId() { return id; }
    @Override
    public String getName() { return name; }
    public HeroType getHeroType() { return heroType; }
    public int getDifficulty() { return difficulty; }
    public String getDescription() { return description; }
    public List<Integer> getRecommendedEquipIds() { return recommendedEquipIds; }

    /**
     * 打印英雄详细信息
     */
    public void displayInfo() {
        System.out.println("===== 英雄详情 =====");
        System.out.println("名字: " + name);
        System.out.println("类型: " + heroType.getDisplayName());
        System.out.println("难度: " + difficulty + "/10");
        System.out.println("简介: " + description);
        System.out.println("推荐装备数: " + recommendedEquipIds.size());
    }

    @Override
    public String toString() {
        return id + " - " + name + " [" + heroType.getDisplayName() + "]";
    }
}

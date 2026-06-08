package model;

import model.enums.EquipmentType;
import model.interfaces.Identifiable;
import java.util.HashMap;
import java.util.Map;

/**
 * Equipment —— 装备类
 *
 * 使用 HashMap<String, Integer> 来存属性加成。
 * 例如：{"攻击力": 80, "攻速": 20, "暴击率": 15}
 *
 * HashMap 讲解：
 * - 格式：Map<键的类型, 值的类型>
 * - put(key, value) → 存入一对数据
 * - get(key) → 取出对应的值
 * - 和 ArrayList 的区别：ArrayList 用数字下标找元素，HashMap 用自定义的 key 找元素
 */
public class Equipment implements Identifiable {
    private int id;
    private String name;
    private EquipmentType equipType;
    private int price;

    // 属性加成表：属性名 → 属性值
    // 例如: "攻击力" → 80, "冷却缩减" → 10
    private Map<String, Integer> attributeMap;

    private int usageCount;  // 被推荐次数（排行榜用）

    public Equipment(int id, String name, EquipmentType equipType, int price) {
        this.id = id;
        this.name = name;
        this.equipType = equipType;
        this.price = price;
        this.attributeMap = new HashMap<>();  // 创建空的 HashMap
        this.usageCount = 0;
    }

    /**
     * 添加一条属性加成
     * @param key   属性名，如 "攻击力"
     * @param value 属性值，如 80
     */
    public void addAttribute(String key, int value) {
        attributeMap.put(key, value);  // HashMap 的 put 方法
    }

    /**
     * 使用次数 +1（每当有英雄推荐此装备时调用）
     */
    public void incrementUsage() {
        this.usageCount++;
    }

    // === getter ===
    @Override
    public int getId() { return id; }
    @Override
    public String getName() { return name; }
    public EquipmentType getEquipType() { return equipType; }
    public int getPrice() { return price; }
    public Map<String, Integer> getAttributeMap() { return attributeMap; }
    public int getUsageCount() { return usageCount; }

    /**
     * 打印装备详情
     */
    public void displayInfo() {
        System.out.println("===== 装备详情 =====");
        System.out.println("名字: " + name);
        System.out.println("类型: " + equipType.getDisplayName());
        System.out.println("价格: " + price + " 金币");
        System.out.println("属性加成:");
        // 遍历 HashMap 的每一对键值
        for (Map.Entry<String, Integer> entry : attributeMap.entrySet()) {
            System.out.println("  " + entry.getKey() + ": +" + entry.getValue());
        }
        System.out.println("被推荐次数: " + usageCount);
    }

    @Override
    public String toString() {
        return id + " - " + name + " [" + equipType.getDisplayName() + "]";
    }
}

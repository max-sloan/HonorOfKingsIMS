package model;

import model.enums.EquipmentType;
import model.interfaces.Identifiable;
import java.util.HashMap;
import java.util.Map;

/**
 * Equipment class
 *
 * Uses HashMap<String, Integer> to store attribute bonuses.
 * e.g. {"Attack": 80, "Attack Speed": 20, "Crit Rate": 15}
 *
 * HashMap:
 * - Format: Map<KeyType, ValueType>
 * - put(key, value) - store a pair
 * - get(key) - retrieve by key
 * - Unlike ArrayList which uses numeric index, HashMap uses custom keys
 */
public class Equipment implements Identifiable {
    private int id;
    private String name;
    private EquipmentType equipType;
    private int price;
    private Map<String, Integer> attributeMap;
    private int usageCount;

    public Equipment(int id, String name, EquipmentType equipType, int price) {
        this.id = id;
        this.name = name;
        this.equipType = equipType;
        this.price = price;
        this.attributeMap = new HashMap<>();
        this.usageCount = 0;
    }

    public void addAttribute(String key, int value) {
        attributeMap.put(key, value);
    }

    /** Increment when a hero recommends this equipment */
    public void incrementUsage() {
        this.usageCount++;
    }

    @Override
    public int getId() { return id; }
    @Override
    public String getName() { return name; }
    public EquipmentType getEquipType() { return equipType; }
    public int getPrice() { return price; }
    public Map<String, Integer> getAttributeMap() { return attributeMap; }
    public int getUsageCount() { return usageCount; }

    public void displayInfo() {
        System.out.println("===== Equipment Details =====");
        System.out.println("Name: " + name);
        System.out.println("Type: " + equipType.getDisplayName());
        System.out.println("Price: " + price + " gold");
        System.out.println("Attributes:");
        for (Map.Entry<String, Integer> entry : attributeMap.entrySet()) {
            System.out.println("  " + entry.getKey() + ": +" + entry.getValue());
        }
        System.out.println("Times Recommended: " + usageCount);
    }

    @Override
    public String toString() {
        return id + " - " + name + " [" + equipType.getDisplayName() + "]";
    }
}

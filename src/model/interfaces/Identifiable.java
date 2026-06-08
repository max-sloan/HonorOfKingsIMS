package model.interfaces;

/**
 * Identifiable 接口 —— 可标识
 * 接口（Interface）定义了一组方法签名，不包含实现。
 * 任何实现了这个接口的类，都必须提供 getId() 和 getName() 方法。
 *
 * 这体现了 Java 的"多态"——我们可以用 Identifiable 类型来统一处理
 * Player、Hero、Equipment、Team 等不同对象。
 */
public interface Identifiable {
    int getId();
    String getName();
}

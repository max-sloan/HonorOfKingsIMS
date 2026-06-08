package model.interfaces;

/**
 * Reportable 接口 —— 可报告
 * 实现此接口的类需要提供 generateReport() 方法，
 * 返回一段描述自身的文本。
 */
public interface Reportable {
    String generateReport();
}

package model.interfaces;

/**
 * Reportable interface
 * Implementing classes must provide generateReport() to return
 * a self-describing text string.
 */
public interface Reportable {
    String generateReport();
}

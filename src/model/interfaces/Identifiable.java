package model.interfaces;

/**
 * Identifiable interface
 * Any class implementing this must provide getId() and getName().
 * Enables polymorphism - we can handle Player, Hero, Equipment, Team
 * uniformly through this interface.
 */
public interface Identifiable {
    int getId();
    String getName();
}

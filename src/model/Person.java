package model;

import model.interfaces.Identifiable;

/**
 * Person - abstract parent class
 *
 * An abstract class cannot be instantiated directly; it can only be inherited.
 * It defines attributes and methods shared by Player and Admin.
 * Demonstrates inheritance and encapsulation.
 *
 * Encapsulation means fields are private and accessed through getters/setters,
 * controlling data read/write.
 */
public abstract class Person implements Identifiable {
    private int id;
    private String name;
    private String password;

    public Person(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Check if password is correct (use .equals() not ==)
     */
    public boolean checkPassword(String rawPassword) {
        return this.password.equals(rawPassword);
    }

    /**
     * Change password
     */
    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    /**
     * Get password (for saving to file)
     */
    public String getPassword() {
        return password;
    }

    /**
     * Abstract method - subclasses must implement this
     */
    public abstract void displayInfo();

    @Override
    public String toString() {
        return id + " - " + name;
    }
}

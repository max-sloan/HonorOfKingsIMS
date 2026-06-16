package model;

import model.interfaces.Identifiable;

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

    public boolean checkPassword(String rawPassword) {
        return this.password.equals(rawPassword);
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Get system role — subclasses implement this to return "Player" or "Admin".
     * This enables polymorphism: no need to use instanceof to check identity.
     */
    public abstract String getRole();

    public abstract void displayInfo();

    @Override
    public String toString() {
        return id + " - " + name;
    }
}

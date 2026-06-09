package model;

/**
 * Admin class, extends Person
 *
 * Admin exists primarily as a permission checkpoint -
 * the system uses instanceof to distinguish Admin from Player.
 */
public class Admin extends Person {
    private String role;

    public Admin(int id, String name, String password, String role) {
        super(id, name, password);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public void displayInfo() {
        System.out.println("===== Admin Info =====");
        System.out.println("ID: " + getId());
        System.out.println("Name: " + getName());
        System.out.println("Role: " + role);
    }

    @Override
    public String toString() {
        return super.toString() + " [" + role + "]";
    }
}

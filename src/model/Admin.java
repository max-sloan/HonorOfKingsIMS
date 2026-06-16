package model;

public class Admin extends Person {
    private String adminTitle;

    public Admin(int id, String name, String password, String adminTitle) {
        super(id, name, password);
        this.adminTitle = adminTitle;
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    public String getAdminTitle() {
        return adminTitle;
    }

    @Override
    public void displayInfo() {
        System.out.println("===== Admin Info =====");
        System.out.println("ID: " + getId());
        System.out.println("Name: " + getName());
        System.out.println("Role: " + adminTitle);
    }

    @Override
    public String toString() {
        return super.toString() + " [" + adminTitle + "]";
    }
}

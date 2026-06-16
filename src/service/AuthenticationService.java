package service;

import model.Admin;
import model.Person;
import model.Player;

/**
 * AuthenticationService - handles login/logout
 *
 * Uses Person type for currentUser (polymorphism).
 * Role is determined by Person.getRole() instead of instanceof.
 * This is better OOP: the object itself knows its identity.
 */
public class AuthenticationService {
    private GameDataManager dataManager;
    private Person currentUser;

    public AuthenticationService() {
        this.dataManager = GameDataManager.getInstance();
        this.currentUser = null;
    }

    /**
     * Attempt login
     * @return "Player" or "Admin" on success, null on failure
     */
    public String login(int id, String password) {
        Player player = dataManager.findPlayerById(id);
        if (player != null && player.checkPassword(password)) {
            currentUser = player;
            return currentUser.getRole();
        }

        Admin admin = dataManager.findAdminById(id);
        if (admin != null && admin.checkPassword(password)) {
            currentUser = admin;
            return currentUser.getRole();
        }

        return null;
    }

    /** Log out */
    public void logout() {
        currentUser = null;
    }

    public Person getCurrentUser() {
        return currentUser;
    }

    public boolean isAdmin() {
        return currentUser != null && "Admin".equals(currentUser.getRole());
    }

    public boolean isPlayer() {
        return currentUser != null && "Player".equals(currentUser.getRole());
    }

    public String getCurrentUserInfo() {
        if (currentUser == null) return "Not logged in";
        return currentUser.getRole() + ": " + currentUser.getName();
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}

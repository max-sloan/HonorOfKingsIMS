package service;

import model.Admin;
import model.Player;

/**
 * AuthenticationService - handles login/logout
 *
 * Uses Object type for currentUser (could be Player or Admin).
 * instanceof is used to check the actual type at runtime.
 */
public class AuthenticationService {
    private GameDataManager dataManager;
    private Object currentUser;

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
            return "Player";
        }

        Admin admin = dataManager.findAdminById(id);
        if (admin != null && admin.checkPassword(password)) {
            currentUser = admin;
            return "Admin";
        }

        return null;
    }

    /** Log out */
    public void logout() {
        currentUser = null;
    }

    public Object getCurrentUser() {
        return currentUser;
    }

    public boolean isAdmin() {
        return currentUser instanceof Admin;
    }

    public boolean isPlayer() {
        return currentUser instanceof Player;
    }

    public String getCurrentUserInfo() {
        if (currentUser instanceof Player) {
            Player p = (Player) currentUser;
            return "Player: " + p.getName();
        } else if (currentUser instanceof Admin) {
            Admin a = (Admin) currentUser;
            return "Admin: " + a.getName();
        }
        return "Not logged in";
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}

package service;

import model.Admin;
import model.Player;

/**
 * AuthenticationService —— 身份认证服务
 *
 * 负责登录验证。
 * 用 Object 类型返回当前用户（可能是 Player 或 Admin），
 * 这体现了"多态"——父类引用指向子类对象。
 */
public class AuthenticationService {
    private GameDataManager dataManager;
    private Object currentUser;   // 当前登录用户（Player 或 Admin）

    public AuthenticationService() {
        this.dataManager = GameDataManager.getInstance();
        this.currentUser = null;
    }

    /**
     * 尝试登录
     * @param id 用户ID
     * @param password 明文密码
     * @return 登录成功返回 "Player" 或 "Admin"，失败返回 null
     */
    public String login(int id, String password) {
        // 先在 Player 中找
        Player player = dataManager.findPlayerById(id);
        if (player != null && player.checkPassword(password)) {
            currentUser = player;
            return "Player";
        }

        // 再在 Admin 中找
        Admin admin = dataManager.findAdminById(id);
        if (admin != null && admin.checkPassword(password)) {
            currentUser = admin;
            return "Admin";
        }

        return null;  // 登录失败
    }

    /**
     * 登出
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * 获取当前登录用户
     * @return Player 或 Admin 对象，未登录返回 null
     */
    public Object getCurrentUser() {
        return currentUser;
    }

    /**
     * 判断当前用户是不是 Admin
     * instanceof 是 Java 的"类型检查"关键字：
     * - obj instanceof Admin  →  检查 obj 是不是 Admin 类型
     */
    public boolean isAdmin() {
        return currentUser instanceof Admin;
    }

    /**
     * 判断当前用户是不是 Player
     */
    public boolean isPlayer() {
        return currentUser instanceof Player;
    }

    /**
     * 获取当前用户信息（用于显示）
     */
    public String getCurrentUserInfo() {
        if (currentUser instanceof Player) {
            Player p = (Player) currentUser;
            return "玩家: " + p.getName();
        } else if (currentUser instanceof Admin) {
            Admin a = (Admin) currentUser;
            return "管理员: " + a.getName();
        }
        return "未登录";
    }

    /**
     * 检查是否已登录
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}

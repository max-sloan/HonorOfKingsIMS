import model.*;
import service.*;
import util.*;
import java.util.*;

/**
 * Main —— 程序入口
 *
 * 包含主菜单（登录/退出）、Admin 菜单（8个功能）、Player 菜单（8个功能）。
 * 用 while(true) + switch-case 实现菜单循环——这是控制台应用最常用的模式。
 */
public class Main {

    // 全局服务实例
    private static GameDataManager dm;
    private static AuthenticationService auth;
    private static SearchService search;
    private static RankingService ranking;
    private static AdminDataService adminService;
    private static FileStorageService fileService;

    public static void main(String[] args) {
        // 初始化服务
        dm = GameDataManager.getInstance();
        auth = new AuthenticationService();
        search = new SearchService();
        ranking = new RankingService();
        adminService = new AdminDataService();
        fileService = new FileStorageService();

        // 初始化数据集
        System.out.println("正在初始化数据...");
        DataInitializer.initAll(dm, adminService);

        // 创建默认管理员账号: ID=999, 密码=admin
        Admin defaultAdmin = new Admin(999, "admin", "admin", "超级管理员");
        dm.addAdmin(defaultAdmin);
        System.out.println("[提示] 默认管理员: ID=999, 密码=admin");

        System.out.println("\n欢迎使用王者荣耀信息管理系统！");

        // 主循环
        while (true) {
            String line = repeatStr("=", 40);
            System.out.println("\n" + line);
            System.out.println("  王者荣耀信息管理系统");
            System.out.println(line);
            System.out.println("  1. 登录");
            System.out.println("  2. 保存数据到文件");
            System.out.println("  3. 从文件加载数据");
            System.out.println("  0. 退出系统");
            System.out.println(line);

            int choice = InputHelper.readIntInRange("请选择: ", 0, 3);
            switch (choice) {
                case 1: doLogin(); break;
                case 2: doSaveData(); break;
                case 3: doLoadData(); break;
                case 0:
                    System.out.println("感谢使用，再见！");
                    return;
            }
        }
    }

    // ============ 登录 ============

    private static void doLogin() {
        System.out.println("\n===== 用户登录 =====");
        int id = InputHelper.readInt("请输入用户ID: ");
        String password = InputHelper.readNonEmptyString("请输入密码: ");

        String role = auth.login(id, password);
        if (role == null) {
            System.out.println("[失败] ID或密码错误！");
            return;
        }
        System.out.println("[成功] 登录成功！身份: " + role);

        if (auth.isAdmin()) {
            adminMenuLoop();
        } else {
            playerMenuLoop();
        }
    }

    // ============ Admin 菜单 ============

    private static void adminMenuLoop() {
        while (true) {
            String line = repeatStr("=", 40);
            System.out.println("\n" + line);
            System.out.println("  Admin 菜单  |  " + auth.getCurrentUserInfo());
            System.out.println(line);
            System.out.println("  1. 玩家查询        2. 战队概览");
            System.out.println("  3. 英雄详情        4. 装备统计");
            System.out.println("  5. 比赛记录        6. 排行榜");
            System.out.println("  7. 数据管理        8. 保存数据");
            System.out.println("  0. 登出");
            System.out.println(line);

            int choice = InputHelper.readIntInRange("请选择: ", 0, 8);
            switch (choice) {
                case 1: doPlayerLookup(); break;
                case 2: doTeamOverview(); break;
                case 3: doHeroDetails(); break;
                case 4: doEquipmentStats(); break;
                case 5: doMatchHistory(); break;
                case 6: doLeaderboard(); break;
                case 7: doDataManagement(); break;
                case 8: doSaveData(); break;
                case 0: auth.logout(); System.out.println("[已登出]"); return;
            }
        }
    }

    // ============ Player 菜单 ============

    private static void playerMenuLoop() {
        while (true) {
            String line = repeatStr("=", 40);
            System.out.println("\n" + line);
            System.out.println("  Player 菜单  |  " + auth.getCurrentUserInfo());
            System.out.println(line);
            System.out.println("  1. 查看我的信息    2. 战队概览");
            System.out.println("  3. 英雄详情        4. 装备统计");
            System.out.println("  5. 比赛记录        6. 排行榜");
            System.out.println("  7. 查看我的英雄    8. 编辑个人信息");
            System.out.println("  0. 登出");
            System.out.println(line);

            int choice = InputHelper.readIntInRange("请选择: ", 0, 8);
            switch (choice) {
                case 1: doViewMyInfo(); break;
                case 2: doTeamOverview(); break;
                case 3: doHeroDetails(); break;
                case 4: doEquipmentStats(); break;
                case 5: doMatchHistory(); break;
                case 6: doLeaderboard(); break;
                case 7: doViewMyHeroes(); break;
                case 8: doEditMyInfo(); break;
                case 0: auth.logout(); System.out.println("[已登出]"); return;
            }
        }
    }

    // ============ 1. 玩家查询 ============

    private static void doPlayerLookup() {
        System.out.println("\n===== 玩家查询 =====");
        System.out.println("1. 按ID  2. 按名字");
        int way = InputHelper.readIntInRange("请选择: ", 1, 2);

        if (way == 1) {
            int id = InputHelper.readInt("玩家ID: ");
            Player p = search.searchPlayerById(id);
            if (p != null) {
                System.out.println(search.getPlayerFullReport(p));
            } else {
                System.out.println("[未找到]");
            }
        } else {
            String kw = InputHelper.readNonEmptyString("名字关键词: ");
            List<Player> list = search.searchPlayers(kw);
            if (list.isEmpty()) {
                System.out.println("[未找到]");
            } else {
                System.out.println("找到 " + list.size() + " 人:");
                for (Player p : list) {
                    System.out.println("  " + p.generateReport());
                }
                int id = InputHelper.readIntOrDefault("输入ID看详情(0=跳过): ", 0);
                if (id > 0) {
                    Player p = search.searchPlayerById(id);
                    if (p != null) System.out.println(search.getPlayerFullReport(p));
                }
            }
        }
        InputHelper.waitForEnter();
    }

    // ============ 2. 战队概览 ============

    private static void doTeamOverview() {
        System.out.println("\n===== 战队概览 =====");
        System.out.println("1. 按ID  2. 按名字  3. 全部战队");
        int way = InputHelper.readIntInRange("请选择: ", 1, 3);

        Team team = null;
        if (way == 1) {
            team = search.searchTeamById(InputHelper.readInt("战队ID: "));
        } else if (way == 2) {
            String kw = InputHelper.readNonEmptyString("战队名: ");
            List<Team> list = search.searchTeams(kw);
            if (list.isEmpty()) { System.out.println("[未找到]"); return; }
            for (Team t : list) System.out.println("  " + t.generateReport());
            int id = InputHelper.readInt("输入ID看详情: ");
            team = search.searchTeamById(id);
        } else {
            for (Team t : dm.getAllTeams()) System.out.println("  " + t.generateReport());
            int id = InputHelper.readInt("输入ID看详情(0=跳过): ");
            if (id > 0) team = search.searchTeamById(id);
        }

        if (team != null) {
            System.out.println("\n══════ " + team.getName() + " ══════");
            System.out.println("创建: " + team.getCreatedDate());
            System.out.println("平均等级: " + String.format("%.1f", ranking.calculateTeamAvgLevel(team.getId())));
            System.out.println("胜率: " + String.format("%.1f%%", ranking.calculateTeamWinRate(team)));

            List<Player> members = search.getTeamPlayers(team.getId());
            System.out.println("\n成员 (" + members.size() + "人):");
            for (Player p : members) System.out.println("  " + p.generateReport());

            Player top = ranking.getTopPlayerInTeam(team.getId());
            if (top != null) System.out.println("\n最强: " + top.getName());
        }
        InputHelper.waitForEnter();
    }

    // ============ 3. 英雄详情 ============

    private static void doHeroDetails() {
        System.out.println("\n===== 英雄详情 =====");
        String kw = InputHelper.readNonEmptyString("英雄名关键词: ");
        List<Hero> list = search.searchHeroes(kw);
        if (list.isEmpty()) { System.out.println("[未找到]"); return; }
        for (Hero h : list) System.out.println("  " + h.toString());

        int id = InputHelper.readIntOrDefault("输入ID看详情(0=跳过): ", 0);
        if (id == 0) return;
        Hero hero = search.searchHeroById(id);
        if (hero == null) { System.out.println("[未找到]"); return; }

        hero.displayInfo();
        System.out.println("\n推荐装备:");
        for (int eid : hero.getRecommendedEquipIds()) {
            Equipment e = dm.findEquipmentById(eid);
            if (e != null) System.out.println("  " + e.getName());
        }
        List<Player> owners = search.getPlayersOwningHero(id);
        System.out.println("\n拥有者 (" + owners.size() + "人):");
        for (Player p : owners) System.out.println("  " + p.getName() + " [" + p.getTeamName() + "]");
        InputHelper.waitForEnter();
    }

    // ============ 4. 装备统计 ============

    private static void doEquipmentStats() {
        System.out.println("\n===== 装备统计（按被推荐次数排名）=====");
        List<Equipment> list = ranking.getEquipmentUsageRanking();
        System.out.println("\n排名  装备名              类型      次数");
        System.out.println("────  ──────────────────  ────────  ────");
        int rank = 1;
        for (Equipment e : list) {
            if (e.getUsageCount() > 0) {
                System.out.printf("%-4d  %-20s %-8s %d\n",
                    rank++, e.getName(), e.getEquipType().getDisplayName(), e.getUsageCount());
            }
        }
        InputHelper.waitForEnter();
    }

    // ============ 5. 比赛记录 ============

    private static void doMatchHistory() {
        System.out.println("\n===== 比赛记录 =====");
        System.out.println("1. 按战队  2. 全部");
        int way = InputHelper.readIntInRange("请选择: ", 1, 2);

        List<MatchRecord> matches;
        if (way == 1) {
            matches = search.getTeamMatches(InputHelper.readInt("战队ID: "));
        } else {
            matches = new ArrayList<>(dm.getAllMatches());
        }

        // 按时间排序（最近的在前）
        matches.sort((a, b) -> b.getMatchTime().compareTo(a.getMatchTime()));

        int n = InputHelper.readIntOrDefault("显示最近几条(默认全部): ", matches.size());
        int cnt = Math.min(n, matches.size());
        System.out.println("\n最近 " + cnt + " 场:");
        for (int i = matches.size() - cnt; i < matches.size(); i++) {
            MatchRecord m = matches.get(i);
            Team a = dm.findTeamById(m.getTeamAId());
            Team b = dm.findTeamById(m.getTeamBId());
            System.out.printf("  %s | %s vs %s | %s\n",
                m.getMatchTime(),
                a != null ? a.getName() : "?",
                b != null ? b.getName() : "?",
                m.getResult().getDisplayName());
        }
        InputHelper.waitForEnter();
    }

    // ============ 6. 排行榜 ============

    private static void doLeaderboard() {
        System.out.println("\n===== 排行榜 =====");
        System.out.println("1. 胜率  2. 等级  3. 场次");
        int way = InputHelper.readIntInRange("请选择: ", 1, 3);
        int topN = InputHelper.readIntOrDefault("显示前几名(默认5): ", 5);

        List<Player> players;
        String title;
        if (way == 1) { players = ranking.getWinRateRanking(topN); title = "胜率榜"; }
        else if (way == 2) { players = ranking.getLevelRanking(topN); title = "等级榜"; }
        else { players = ranking.getMatchCountRanking(topN); title = "场次榜"; }

        System.out.println("\n══════ " + title + " Top" + topN + " ══════");
        System.out.println("排名  玩家         等级  胜率       场次  战队");
        int rank = 1;
        for (Player p : players) {
            System.out.printf("%-4d  %-10s  %-4d  %-8s  %-4d  %s\n",
                rank++, p.getName(), p.getLevel(),
                String.format("%.1f%%", p.getWinRate()),
                p.getTotalMatches(), p.getTeamName());
        }
        System.out.println("\n平局处理: 主要指标相同时，按次要指标排序。");
        InputHelper.waitForEnter();
    }

    // ============ 7. 数据管理（Admin） ============

    private static void doDataManagement() {
        while (true) {
            System.out.println("\n===== 数据管理 =====");
            System.out.println("1. 添加玩家  2. 删除玩家  3. 全部玩家");
            System.out.println("4. 加入战队  5. 添加英雄  0. 返回");
            int c = InputHelper.readIntInRange("请选择: ", 0, 5);
            switch (c) {
                case 1:
                    String name = InputHelper.readNonEmptyString("玩家名: ");
                    String pwd = InputHelper.readNonEmptyString("密码: ");
                    int lv = InputHelper.readIntInRange("等级(1-30): ", 1, 30);
                    int sc = InputHelper.readInt("排位分: ");
                    Player np = adminService.addPlayer(name, pwd, lv, sc);
                    System.out.println("[成功] ID=" + np.getId());
                    break;
                case 2:
                    int did = InputHelper.readInt("删除玩家ID: ");
                    Player dp = dm.findPlayerById(did);
                    if (dp == null) { System.out.println("[失败] 不存在"); break; }
                    if (InputHelper.readLine("确认删除 " + dp.getName() + "? (yes): ").equalsIgnoreCase("yes")) {
                        adminService.deletePlayer(did);
                        System.out.println("[成功] 已删除");
                    }
                    break;
                case 3:
                    for (Player p : dm.getAllPlayers()) System.out.println(p.generateReport());
                    break;
                case 4:
                    if (adminService.assignPlayerToTeam(
                        InputHelper.readInt("玩家ID: "), InputHelper.readInt("战队ID: ")))
                        System.out.println("[成功]");
                    else System.out.println("[失败]");
                    break;
                case 5:
                    if (adminService.addHeroToPlayer(
                        InputHelper.readInt("玩家ID: "), InputHelper.readInt("英雄ID: ")))
                        System.out.println("[成功]");
                    else System.out.println("[失败]");
                    break;
                case 0: return;
            }
            if (c != 0) InputHelper.waitForEnter();
        }
    }

    // ============ 保存 / 加载 ============

    private static void doSaveData() {
        System.out.println("\n===== 保存数据 =====");
        try {
            fileService.saveAll(dm);
        } catch (Exception e) {
            System.out.println("[错误] " + e.getMessage());
        }
        InputHelper.waitForEnter();
    }

    private static void doLoadData() {
        System.out.println("\n===== 加载数据 =====");
        System.out.println("[警告] 加载会覆盖当前内存中的数据！");
        String confirm = InputHelper.readLine("输入 yes 确认: ");
        if (!"yes".equalsIgnoreCase(confirm)) {
            System.out.println("[取消]");
            return;
        }
        try {
            // 先登出防止旧用户引用失效
            auth.logout();
            // 清空旧数据
            dm.reset();
            fileService.loadAll(dm);
            // 确保管理员账号存在
            if (dm.findAdminById(999) == null) {
                dm.addAdmin(new Admin(999, "admin", "admin", "超级管理员"));
            }
        } catch (Exception e) {
            System.out.println("[错误] 加载失败，请检查 data/ 文件夹是否存在");
            System.out.println("[错误] 加载失败: " + e.getMessage());
        }
        InputHelper.waitForEnter();
    }

    // ============ Player 专用 ============

    private static void doViewMyInfo() {
        if (auth.isPlayer()) ((Player) auth.getCurrentUser()).displayInfo();
        InputHelper.waitForEnter();
    }

    private static void doViewMyHeroes() {
        if (!auth.isPlayer()) return;
        Player me = (Player) auth.getCurrentUser();
        System.out.println("\n===== 我的英雄 =====");
        if (me.getHeroIdList().isEmpty()) {
            System.out.println("还没有英雄");
        } else {
            for (int hid : me.getHeroIdList()) {
                Hero h = dm.findHeroById(hid);
                if (h != null) System.out.println("  " + h.toString());
            }
        }
        InputHelper.waitForEnter();
    }

    private static void doEditMyInfo() {
        if (!auth.isPlayer()) return;
        Player me = (Player) auth.getCurrentUser();
        System.out.println("\n===== 编辑信息 =====");
        System.out.println("1. 改名(" + me.getName() + ")  2. 改密码  0. 返回");
        int c = InputHelper.readIntInRange("请选择: ", 0, 2);
        if (c == 1) {
            me.setName(InputHelper.readNonEmptyString("新名字: "));
            System.out.println("[成功]");
        } else if (c == 2) {
            me.setPassword(InputHelper.readNonEmptyString("新密码: "));
            System.out.println("[成功]");
        }
        InputHelper.waitForEnter();
    }

    // ============ 工具 ============

    private static String repeatStr(String s, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) sb.append(s);
        return sb.toString();
    }
}

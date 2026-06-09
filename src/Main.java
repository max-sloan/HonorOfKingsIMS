import model.*;
import service.*;
import util.*;
import java.util.*;

/**
 * Main - program entry point
 *
 * Contains main menu, Admin menu (8 functions), Player menu (8 functions).
 * Uses while(true) + switch-case for menu loops.
 */
public class Main {

    private static GameDataManager dm;
    private static AuthenticationService auth;
    private static SearchService search;
    private static RankingService ranking;
    private static AdminDataService adminService;
    private static FileStorageService fileService;

    public static void main(String[] args) {
        dm = GameDataManager.getInstance();
        auth = new AuthenticationService();
        search = new SearchService();
        ranking = new RankingService();
        adminService = new AdminDataService();
        fileService = new FileStorageService();

        System.out.println("Initializing data...");
        DataInitializer.initAll(dm, adminService);

        Admin defaultAdmin = new Admin(999, "admin", "admin", "Super Admin");
        dm.addAdmin(defaultAdmin);
        System.out.println("[Tip] Default admin: ID=999, password=admin");

        System.out.println("\nWelcome to Honor of Kings IMS!");

        while (true) {
            String line = repeatStr("=", 40);
            System.out.println("\n" + line);
            System.out.println("  Honor of Kings IMS");
            System.out.println(line);
            System.out.println("  1. Login");
            System.out.println("  2. Save Data to File");
            System.out.println("  3. Load Data from File");
            System.out.println("  0. Exit");
            System.out.println(line);

            int choice = InputHelper.readIntInRange("Select: ", 0, 3);
            switch (choice) {
                case 1: doLogin(); break;
                case 2: doSaveData(); break;
                case 3: doLoadData(); break;
                case 0:
                    System.out.println("Goodbye!");
                    return;
            }
        }
    }

    // ============ Login ============

    private static void doLogin() {
        System.out.println("\n===== Login =====");
        int id = InputHelper.readInt("User ID: ");
        String password = InputHelper.readNonEmptyString("Password: ");

        String role = auth.login(id, password);
        if (role == null) {
            System.out.println("[Failed] Wrong ID or password!");
            return;
        }
        System.out.println("[Success] Logged in as: " + role);

        if (auth.isAdmin()) {
            adminMenuLoop();
        } else {
            playerMenuLoop();
        }
    }

    // ============ Admin Menu ============

    private static void adminMenuLoop() {
        while (true) {
            String line = repeatStr("=", 40);
            System.out.println("\n" + line);
            System.out.println("  Admin Menu  |  " + auth.getCurrentUserInfo());
            System.out.println(line);
            System.out.println("  1. Player Lookup    2. Team Overview");
            System.out.println("  3. Hero Details      4. Equipment Stats");
            System.out.println("  5. Match History      6. Leaderboard");
            System.out.println("  7. Data Management    8. Save Data");
            System.out.println("  0. Logout");
            System.out.println(line);

            int choice = InputHelper.readIntInRange("Select: ", 0, 8);
            switch (choice) {
                case 1: doPlayerLookup(); break;
                case 2: doTeamOverview(); break;
                case 3: doHeroDetails(); break;
                case 4: doEquipmentStats(); break;
                case 5: doMatchHistory(); break;
                case 6: doLeaderboard(); break;
                case 7: doDataManagement(); break;
                case 8: doSaveData(); break;
                case 0: auth.logout(); System.out.println("[Logged out]"); return;
            }
        }
    }

    // ============ Player Menu ============

    private static void playerMenuLoop() {
        while (true) {
            String line = repeatStr("=", 40);
            System.out.println("\n" + line);
            System.out.println("  Player Menu  |  " + auth.getCurrentUserInfo());
            System.out.println(line);
            System.out.println("  1. My Info          2. Team Overview");
            System.out.println("  3. Hero Details      4. Equipment Stats");
            System.out.println("  5. Match History      6. Leaderboard");
            System.out.println("  7. My Heroes         8. Edit Profile");
            System.out.println("  0. Logout");
            System.out.println(line);

            int choice = InputHelper.readIntInRange("Select: ", 0, 8);
            switch (choice) {
                case 1: doViewMyInfo(); break;
                case 2: doTeamOverview(); break;
                case 3: doHeroDetails(); break;
                case 4: doEquipmentStats(); break;
                case 5: doMatchHistory(); break;
                case 6: doLeaderboard(); break;
                case 7: doViewMyHeroes(); break;
                case 8: doEditMyInfo(); break;
                case 0: auth.logout(); System.out.println("[Logged out]"); return;
            }
        }
    }

    // ============ 1. Player Lookup ============

    private static void doPlayerLookup() {
        System.out.println("\n===== Player Lookup =====");
        System.out.println("1. By ID  2. By Name");
        int way = InputHelper.readIntInRange("Select: ", 1, 2);

        if (way == 1) {
            int id = InputHelper.readInt("Player ID: ");
            Player p = search.searchPlayerById(id);
            if (p != null) {
                System.out.println(search.getPlayerFullReport(p));
            } else {
                System.out.println("[Not Found]");
            }
        } else {
            String kw = InputHelper.readNonEmptyString("Name keyword: ");
            List<Player> list = search.searchPlayers(kw);
            if (list.isEmpty()) {
                System.out.println("[Not Found]");
            } else {
                System.out.println("Found " + list.size() + " player(s):");
                for (Player p : list) {
                    System.out.println("  " + p.generateReport());
                }
                int id = InputHelper.readIntOrDefault("Enter ID for details (0=skip): ", 0);
                if (id > 0) {
                    Player p = search.searchPlayerById(id);
                    if (p != null) System.out.println(search.getPlayerFullReport(p));
                }
            }
        }
        InputHelper.waitForEnter();
    }

    // ============ 2. Team Overview ============

    private static void doTeamOverview() {
        System.out.println("\n===== Team Overview =====");
        System.out.println("1. By ID  2. By Name  3. All Teams");
        int way = InputHelper.readIntInRange("Select: ", 1, 3);

        Team team = null;
        if (way == 1) {
            team = search.searchTeamById(InputHelper.readInt("Team ID: "));
        } else if (way == 2) {
            String kw = InputHelper.readNonEmptyString("Team name: ");
            List<Team> list = search.searchTeams(kw);
            if (list.isEmpty()) { System.out.println("[Not Found]"); return; }
            for (Team t : list) System.out.println("  " + t.generateReport());
            int id = InputHelper.readInt("Enter ID for details: ");
            team = search.searchTeamById(id);
        } else {
            for (Team t : dm.getAllTeams()) System.out.println("  " + t.generateReport());
            int id = InputHelper.readInt("Enter ID for details (0=skip): ");
            if (id > 0) team = search.searchTeamById(id);
        }

        if (team != null) {
            System.out.println("\n========== " + team.getName() + " ==========");
            System.out.println("Created: " + team.getCreatedDate());
            System.out.println("Avg Level: " + String.format("%.1f", ranking.calculateTeamAvgLevel(team.getId())));
            System.out.println("Win Rate: " + String.format("%.1f%%", ranking.calculateTeamWinRate(team)));

            List<Player> members = search.getTeamPlayers(team.getId());
            System.out.println("\nMembers (" + members.size() + "):");
            for (Player p : members) System.out.println("  " + p.generateReport());

            Player top = ranking.getTopPlayerInTeam(team.getId());
            if (top != null) System.out.println("\nTop Player: " + top.getName());
        }
        InputHelper.waitForEnter();
    }

    // ============ 3. Hero Details ============

    private static void doHeroDetails() {
        System.out.println("\n===== Hero Details =====");
        String kw = InputHelper.readNonEmptyString("Hero name keyword: ");
        List<Hero> list = search.searchHeroes(kw);
        if (list.isEmpty()) { System.out.println("[Not Found]"); return; }
        for (Hero h : list) System.out.println("  " + h.toString());

        int id = InputHelper.readIntOrDefault("Enter ID for details (0=skip): ", 0);
        if (id == 0) return;
        Hero hero = search.searchHeroById(id);
        if (hero == null) { System.out.println("[Not Found]"); return; }

        hero.displayInfo();
        System.out.println("\nRecommended Equipment:");
        for (int eid : hero.getRecommendedEquipIds()) {
            Equipment e = dm.findEquipmentById(eid);
            if (e != null) System.out.println("  " + e.getName());
        }
        List<Player> owners = search.getPlayersOwningHero(id);
        System.out.println("\nOwners (" + owners.size() + "):");
        for (Player p : owners) System.out.println("  " + p.getName() + " [" + p.getTeamName() + "]");
        InputHelper.waitForEnter();
    }

    // ============ 4. Equipment Stats ============

    private static void doEquipmentStats() {
        System.out.println("\n===== Equipment Stats (by recommendation count) =====");
        List<Equipment> list = ranking.getEquipmentUsageRanking();
        System.out.println("\nRank  Name                 Type      Count");
        System.out.println("----  -------------------  --------  -----");
        int rank = 1;
        for (Equipment e : list) {
            if (e.getUsageCount() > 0) {
                System.out.printf("%-4d  %-20s %-8s %d\n",
                    rank++, e.getName(), e.getEquipType().getDisplayName(), e.getUsageCount());
            }
        }
        InputHelper.waitForEnter();
    }

    // ============ 5. Match History ============

    private static void doMatchHistory() {
        System.out.println("\n===== Match History =====");
        System.out.println("1. By Team  2. All");
        int way = InputHelper.readIntInRange("Select: ", 1, 2);

        List<MatchRecord> matches;
        if (way == 1) {
            matches = search.getTeamMatches(InputHelper.readInt("Team ID: "));
        } else {
            matches = new ArrayList<>(dm.getAllMatches());
        }

        // Sort by time (most recent first)
        matches.sort((a, b) -> b.getMatchTime().compareTo(a.getMatchTime()));

        int n = InputHelper.readIntOrDefault("Show last N (default=all): ", matches.size());
        int cnt = Math.min(n, matches.size());
        System.out.println("\nLast " + cnt + " matches:");
        for (int i = 0; i < cnt; i++) {
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

    // ============ 6. Leaderboard ============

    private static void doLeaderboard() {
        System.out.println("\n===== Leaderboard =====");
        System.out.println("1. Win Rate  2. Level  3. Matches");
        int way = InputHelper.readIntInRange("Select: ", 1, 3);
        int topN = InputHelper.readIntOrDefault("Show top N (default=5): ", 5);

        List<Player> players;
        String title;
        if (way == 1) { players = ranking.getWinRateRanking(topN); title = "Win Rate"; }
        else if (way == 2) { players = ranking.getLevelRanking(topN); title = "Level"; }
        else { players = ranking.getMatchCountRanking(topN); title = "Matches"; }

        System.out.println("\n========== " + title + " Top " + topN + " ==========");
        System.out.println("Rank  Player        Level  Win Rate   Matches  Team");
        int rank = 1;
        for (Player p : players) {
            System.out.printf("%-4d  %-10s  %-5d  %-8s  %-7d  %s\n",
                rank++, p.getName(), p.getLevel(),
                String.format("%.1f%%", p.getWinRate()),
                p.getTotalMatches(), p.getTeamName());
        }
        System.out.println("\nTie-break: secondary metric when primary is equal.");
        InputHelper.waitForEnter();
    }

    // ============ 7. Data Management (Admin) ============

    private static void doDataManagement() {
        while (true) {
            System.out.println("\n===== Data Management =====");
            System.out.println("1. Add Player  2. Delete Player  3. List All");
            System.out.println("4. Join Team   5. Give Hero      0. Back");
            int c = InputHelper.readIntInRange("Select: ", 0, 5);
            switch (c) {
                case 1:
                    String name = InputHelper.readNonEmptyString("Name: ");
                    String pwd = InputHelper.readNonEmptyString("Password: ");
                    int lv = InputHelper.readIntInRange("Level(1-30): ", 1, 30);
                    int sc = InputHelper.readInt("Rank Score: ");
                    Player np = adminService.addPlayer(name, pwd, lv, sc);
                    System.out.println("[Success] ID=" + np.getId());
                    break;
                case 2:
                    int did = InputHelper.readInt("Delete player ID: ");
                    Player dp = dm.findPlayerById(did);
                    if (dp == null) { System.out.println("[Failed] Not found"); break; }
                    if (InputHelper.readLine("Confirm delete " + dp.getName() + "? (yes): ").equalsIgnoreCase("yes")) {
                        adminService.deletePlayer(did);
                        System.out.println("[Success] Deleted");
                    }
                    break;
                case 3:
                    for (Player p : dm.getAllPlayers()) System.out.println(p.generateReport());
                    break;
                case 4:
                    if (adminService.assignPlayerToTeam(
                        InputHelper.readInt("Player ID: "), InputHelper.readInt("Team ID: ")))
                        System.out.println("[Success]");
                    else System.out.println("[Failed]");
                    break;
                case 5:
                    if (adminService.addHeroToPlayer(
                        InputHelper.readInt("Player ID: "), InputHelper.readInt("Hero ID: ")))
                        System.out.println("[Success]");
                    else System.out.println("[Failed]");
                    break;
                case 0: return;
            }
            if (c != 0) InputHelper.waitForEnter();
        }
    }

    // ============ Save / Load ============

    private static void doSaveData() {
        System.out.println("\n===== Save Data =====");
        try {
            fileService.saveAll(dm);
        } catch (Exception e) {
            System.out.println("[Error] " + e.getMessage());
        }
        InputHelper.waitForEnter();
    }

    private static void doLoadData() {
        System.out.println("\n===== Load Data =====");
        if (!fileService.dataExists()) {
            System.out.println("[Info] No saved data found in data/. Save first.");
            InputHelper.waitForEnter();
            return;
        }
        System.out.println("[Warning] This will overwrite current data in memory!");
        String confirm = InputHelper.readLine("Type yes to confirm: ");
        if (!"yes".equalsIgnoreCase(confirm)) {
            System.out.println("[Cancelled]");
            return;
        }
        try {
            auth.logout();
            dm.reset();
            fileService.loadAll(dm);
            if (dm.findAdminById(999) == null) {
                dm.addAdmin(new Admin(999, "admin", "admin", "Super Admin"));
            }
        } catch (Exception e) {
            System.out.println("[Error] Load failed. Check the data/ folder.");
        }
        InputHelper.waitForEnter();
    }

    // ============ Player-specific ============

    private static void doViewMyInfo() {
        if (auth.isPlayer()) ((Player) auth.getCurrentUser()).displayInfo();
        InputHelper.waitForEnter();
    }

    private static void doViewMyHeroes() {
        if (!auth.isPlayer()) return;
        Player me = (Player) auth.getCurrentUser();
        System.out.println("\n===== My Heroes =====");
        if (me.getHeroIdList().isEmpty()) {
            System.out.println("No heroes yet.");
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
        System.out.println("\n===== Edit Profile =====");
        System.out.println("1. Change Name (" + me.getName() + ")  2. Change Password  0. Back");
        int c = InputHelper.readIntInRange("Select: ", 0, 2);
        if (c == 1) {
            me.setName(InputHelper.readNonEmptyString("New name: "));
            System.out.println("[Success]");
        } else if (c == 2) {
            me.setPassword(InputHelper.readNonEmptyString("New password: "));
            System.out.println("[Success]");
        }
        InputHelper.waitForEnter();
    }

    // ============ Utility ============

    private static String repeatStr(String s, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) sb.append(s);
        return sb.toString();
    }
}

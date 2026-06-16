import model.*;
import model.enums.*;
import service.*;
import util.*;
import java.util.*;

/**
 * Main - program entry point
 *
 * Contains main menu, Admin menu (9 functions), Player menu (9 functions).
 * Uses while(true) + switch-case for menu loops.
 */
public class Main {

    private static GameDataManager dm;
    private static AuthenticationService auth;
    private static SearchService search;
    private static RankingService ranking;
    private static AdminDataService adminService;
    private static FileStorageService fileService;
    private static RecommendationService recService;

    public static void main(String[] args) {
        dm = GameDataManager.getInstance();
        auth = new AuthenticationService();
        search = new SearchService();
        ranking = new RankingService();
        adminService = new AdminDataService();
        fileService = new FileStorageService();
        recService = new RecommendationService();

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
            System.out.println("  9. Recommendation     0. Logout");
            System.out.println(line);

            int choice = InputHelper.readIntInRange("Select: ", 0, 9);
            switch (choice) {
                case 1: doPlayerLookup(); break;
                case 2: doTeamOverview(); break;
                case 3: doHeroDetails(); break;
                case 4: doEquipmentStats(); break;
                case 5: doMatchHistory(); break;
                case 6: doLeaderboard(); break;
                case 7: doDataManagement(); break;
                case 8: doSaveData(); break;
                case 9: doRecommendation(); break;
                case 0: auth.logout(); System.out.println("[Logged Out]"); return;
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
            System.out.println("  9. Recommendation     0. Logout");
            System.out.println(line);

            int choice = InputHelper.readIntInRange("Select: ", 0, 9);
            switch (choice) {
                case 1: doViewMyInfo(); break;
                case 2: doTeamOverview(); break;
                case 3: doHeroDetails(); break;
                case 4: doEquipmentStats(); break;
                case 5: doMatchHistory(); break;
                case 6: doLeaderboard(); break;
                case 7: doViewMyHeroes(); break;
                case 8: doEditMyInfo(); break;
                case 9: doRecommendation(); break;
                case 0: auth.logout(); System.out.println("[Logged Out]"); return;
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
            String line = repeatStr("=", 50);
            System.out.println("\n" + line);
            System.out.println("  Data Management (Admin Only)");
            System.out.println(line);
            System.out.println("  -- Player --");
            System.out.println("   1. Add Player          2. Delete Player       3. List All Players");
            System.out.println("   4. Edit Player         5. Join Team           6. Give Hero to Player");
            System.out.println("  -- Hero --");
            System.out.println("   7. Add Hero            8. Delete Hero         9. List All Heroes");
            System.out.println("  -- Equipment --");
            System.out.println("  10. Add Equipment      11. Delete Equipment   12. List All Equipment");
            System.out.println("  13. Assign Equipment to Hero");
            System.out.println("  -- Team & Match --");
            System.out.println("  14. Add Team           15. Delete Team        16. List All Teams");
            System.out.println("  17. Add Match Record");
            System.out.println("   0. Back");
            System.out.println(line);

            int c = InputHelper.readIntInRange("Select: ", 0, 17);
            switch (c) {
                case 1:  doAddPlayer(); break;
                case 2:  doDeletePlayer(); break;
                case 3:  doListPlayers(); break;
                case 4:  doEditPlayer(); break;
                case 5:  doJoinTeam(); break;
                case 6:  doGiveHero(); break;
                case 7:  doAddHero(); break;
                case 8:  doDeleteHero(); break;
                case 9:  doListHeroes(); break;
                case 10: doAddEquipment(); break;
                case 11: doDeleteEquipment(); break;
                case 12: doListEquipment(); break;
                case 13: doAssignEquipToHero(); break;
                case 14: doAddTeam(); break;
                case 15: doDeleteTeam(); break;
                case 16: doListTeams(); break;
                case 17: doAddMatchRecord(); break;
                case 0: return;
            }
            if (c != 0) InputHelper.waitForEnter();
        }
    }

    // ---- Player operations ----

    private static void doAddPlayer() {
        System.out.println("\n--- Add New Player ---");
        System.out.println("[Tip] Enter the new player's basic information below.");
        String name = InputHelper.readNonEmptyString("  Player Name: ");
        String pwd = InputHelper.readNonEmptyString("  Password: ");
        int lv = InputHelper.readIntInRange("  Level (1~30): ", 1, 30);
        int sc = InputHelper.readInt("  Rank Score: ");
        Player np = adminService.addPlayer(name, pwd, lv, sc);
        System.out.println("[Success] Player created! ID=" + np.getId() + ", Name=" + np.getName());
    }

    private static void doDeletePlayer() {
        System.out.println("\n--- Delete Player ---");
        System.out.println("[Tip] Enter the ID of the player you want to delete.");
        int did = InputHelper.readInt("  Player ID: ");
        Player dp = dm.findPlayerById(did);
        if (dp == null) {
            System.out.println("[Failed] No player found with ID=" + did);
            return;
        }
        System.out.println("  Found: " + dp.getName() + " (Team: " + dp.getTeamName() + ")");
        String confirm = InputHelper.readLine("  Type 'yes' to confirm deletion: ");
        if ("yes".equalsIgnoreCase(confirm)) {
            adminService.deletePlayer(did);
            System.out.println("[Success] Player '" + dp.getName() + "' deleted.");
        } else {
            System.out.println("[Cancelled] Deletion aborted.");
        }
    }

    private static void doListPlayers() {
        System.out.println("\n--- All Players ---");
        Collection<Player> players = dm.getAllPlayers();
        if (players.isEmpty()) {
            System.out.println("  (No players in the system)");
            return;
        }
        System.out.println("  ID   Name           Lv   Win Rate   Team");
        System.out.println("  ---- -------------- ---- --------- -------");
        for (Player p : players) {
            System.out.printf("  %-4d %-14s %-4d %-8s  %s\n",
                p.getId(), p.getName(), p.getLevel(),
                String.format("%.1f%%", p.getWinRate()), p.getTeamName());
        }
    }

    private static void doEditPlayer() {
        System.out.println("\n--- Edit Player ---");
        System.out.println("[Tip] You can change a player's name or password.");
        int pid = InputHelper.readInt("  Player ID: ");
        Player p = dm.findPlayerById(pid);
        if (p == null) {
            System.out.println("[Failed] No player found with ID=" + pid);
            return;
        }
        System.out.println("  Editing: " + p.getName() + " (Level " + p.getLevel() + ")");
        System.out.println("  1. Change Name (current: " + p.getName() + ")");
        System.out.println("  2. Change Password");
        System.out.println("  0. Cancel");
        int choice = InputHelper.readIntInRange("  Select: ", 0, 2);
        if (choice == 1) {
            String newName = InputHelper.readNonEmptyString("  New Name: ");
            adminService.updatePlayerName(pid, newName);
            System.out.println("[Success] Name changed to '" + newName + "'.");
        } else if (choice == 2) {
            String newPwd = InputHelper.readNonEmptyString("  New Password: ");
            adminService.updatePlayerPassword(pid, newPwd);
            System.out.println("[Success] Password updated.");
        }
    }

    private static void doJoinTeam() {
        System.out.println("\n--- Assign Player to Team ---");
        System.out.println("[Tip] Assign a player to an existing team. The player will leave their current team.");
        int pid = InputHelper.readInt("  Player ID: ");
        Player p = dm.findPlayerById(pid);
        if (p == null) {
            System.out.println("[Failed] No player found with ID=" + pid);
            return;
        }
        System.out.println("  Player: " + p.getName() + " | Current Team: " + p.getTeamName());
        System.out.println("  Available Teams:");
        for (Team t : dm.getAllTeams()) {
            System.out.println("    ID=" + t.getId() + "  " + t.getName() + " (" + t.getPlayerIds().size() + " members)");
        }
        int tid = InputHelper.readInt("  Target Team ID: ");
        if (adminService.assignPlayerToTeam(pid, tid)) {
            Team t = dm.findTeamById(tid);
            System.out.println("[Success] " + p.getName() + " is now a member of " + t.getName() + ".");
        } else {
            System.out.println("[Failed] Invalid player or team ID.");
        }
    }

    private static void doGiveHero() {
        System.out.println("\n--- Give Hero to Player ---");
        System.out.println("[Tip] Add a hero to a player's hero collection.");
        int pid = InputHelper.readInt("  Player ID: ");
        Player p = dm.findPlayerById(pid);
        if (p == null) {
            System.out.println("[Failed] No player found with ID=" + pid);
            return;
        }
        System.out.println("  Player: " + p.getName() + " | Owns " + p.getHeroIdList().size() + " heroes");
        System.out.println("[Tip] Available heroes:");
        for (Hero h : dm.getAllHeroes()) {
            boolean owned = p.getHeroIdList().contains(h.getId());
            System.out.printf("    ID=%-2d %-14s [%s] %s\n",
                h.getId(), h.getName(), h.getHeroType().getDisplayName(),
                owned ? "(already owned)" : "");
        }
        int hid = InputHelper.readInt("  Hero ID to give: ");
        if (adminService.addHeroToPlayer(pid, hid)) {
            Hero h = dm.findHeroById(hid);
            System.out.println("[Success] " + h.getName() + " given to " + p.getName() + ".");
        } else {
            System.out.println("[Failed] Invalid player or hero ID.");
        }
    }

    // ---- Hero operations ----

    private static void doAddHero() {
        System.out.println("\n--- Add New Hero ---");
        System.out.println("[Tip] Enter the new hero's information.");
        String name = InputHelper.readNonEmptyString("  Hero Name: ");
        System.out.println("  Hero Types: TANK | WARRIOR | ASSASSIN | MAGE | MARKSMAN | SUPPORT");
        String typeStr = InputHelper.readNonEmptyString("  Hero Type: ");
        HeroType heroType;
        try {
            heroType = HeroType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("[Failed] Invalid hero type! Use one of the listed types.");
            return;
        }
        int diff = InputHelper.readIntInRange("  Difficulty (1~10): ", 1, 10);
        String desc = InputHelper.readNonEmptyString("  Description: ");
        Hero h = adminService.addHero(name, heroType, diff, desc);
        System.out.println("[Success] Hero created! ID=" + h.getId() + ", Name=" + h.getName());
    }

    private static void doDeleteHero() {
        System.out.println("\n--- Delete Hero ---");
        System.out.println("[Tip] Enter the ID of the hero you want to delete.");
        System.out.println("  Current heroes:");
        for (Hero h : dm.getAllHeroes()) {
            System.out.printf("    ID=%-2d %-14s [%s]\n", h.getId(), h.getName(), h.getHeroType().getDisplayName());
        }
        int hid = InputHelper.readInt("  Hero ID: ");
        Hero h = dm.findHeroById(hid);
        if (h == null) {
            System.out.println("[Failed] No hero found with ID=" + hid);
            return;
        }
        String confirm = InputHelper.readLine("  Type 'yes' to confirm deletion of '" + h.getName() + "': ");
        if ("yes".equalsIgnoreCase(confirm)) {
            adminService.deleteHero(hid);
            System.out.println("[Success] Hero '" + h.getName() + "' deleted.");
        } else {
            System.out.println("[Cancelled] Deletion aborted.");
        }
    }

    private static void doListHeroes() {
        System.out.println("\n--- All Heroes ---");
        Collection<Hero> heroes = dm.getAllHeroes();
        if (heroes.isEmpty()) {
            System.out.println("  (No heroes in the system)");
            return;
        }
        System.out.println("  ID   Name             Type        Diff   Win Rate   Equipment");
        System.out.println("  ---- ---------------- ---------- -----  --------- ---------");
        for (Hero h : heroes) {
            System.out.printf("  %-4d %-16s %-10s %-5d %-6.1f%%   %d items\n",
                h.getId(), h.getName(), h.getHeroType().getDisplayName(),
                h.getDifficulty(), h.getHeroWinRate(), h.getRecommendedEquipIds().size());
        }
    }

    // ---- Equipment operations ----

    private static void doAddEquipment() {
        System.out.println("\n--- Add New Equipment ---");
        System.out.println("[Tip] Enter the new equipment's information.");
        String name = InputHelper.readNonEmptyString("  Equipment Name: ");
        System.out.println("  Equipment Types: ATTACK | DEFENSE | MOVEMENT | MAGIC | JUNGLE | SUPPORT");
        String typeStr = InputHelper.readNonEmptyString("  Equipment Type: ");
        EquipmentType equipType;
        try {
            equipType = EquipmentType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("[Failed] Invalid equipment type! Use one of the listed types.");
            return;
        }
        int price = InputHelper.readInt("  Price (gold): ");
        Equipment e = adminService.addEquipment(name, equipType, price);
        System.out.println("[Success] Equipment created! ID=" + e.getId() + ", Name=" + e.getName());
    }

    private static void doDeleteEquipment() {
        System.out.println("\n--- Delete Equipment ---");
        System.out.println("[Tip] Enter the ID of the equipment you want to delete.");
        System.out.println("  Current equipment:");
        for (Equipment e : dm.getAllEquipments()) {
            System.out.printf("    ID=%-2d %-18s [%s] Price=%d\n",
                e.getId(), e.getName(), e.getEquipType().getDisplayName(), e.getPrice());
        }
        int eid = InputHelper.readInt("  Equipment ID: ");
        Equipment e = dm.findEquipmentById(eid);
        if (e == null) {
            System.out.println("[Failed] No equipment found with ID=" + eid);
            return;
        }
        String confirm = InputHelper.readLine("  Type 'yes' to confirm deletion of '" + e.getName() + "': ");
        if ("yes".equalsIgnoreCase(confirm)) {
            adminService.deleteEquipment(eid);
            System.out.println("[Success] Equipment '" + e.getName() + "' deleted.");
        } else {
            System.out.println("[Cancelled] Deletion aborted.");
        }
    }

    private static void doListEquipment() {
        System.out.println("\n--- All Equipment ---");
        Collection<Equipment> equips = dm.getAllEquipments();
        if (equips.isEmpty()) {
            System.out.println("  (No equipment in the system)");
            return;
        }
        System.out.println("  ID   Name                 Type        Price    Usage");
        System.out.println("  ---- -------------------- ---------- -------  -----");
        for (Equipment e : equips) {
            System.out.printf("  %-4d %-20s %-10s %-7d %d\n",
                e.getId(), e.getName(), e.getEquipType().getDisplayName(),
                e.getPrice(), e.getUsageCount());
        }
    }

    private static void doAssignEquipToHero() {
        System.out.println("\n--- Assign Equipment to Hero ---");
        System.out.println("[Tip] Link an equipment item to a hero as a recommendation.");
        System.out.println("  Heroes:");
        for (Hero h : dm.getAllHeroes()) {
            System.out.printf("    ID=%-2d %-14s [%s]\n", h.getId(), h.getName(), h.getHeroType().getDisplayName());
        }
        int hid = InputHelper.readInt("  Hero ID: ");
        Hero hero = dm.findHeroById(hid);
        if (hero == null) {
            System.out.println("[Failed] No hero found with ID=" + hid);
            return;
        }
        System.out.println("  Selected: " + hero.getName());
        System.out.println("  Equipment available:");
        for (Equipment e : dm.getAllEquipments()) {
            boolean assigned = hero.getRecommendedEquipIds().contains(e.getId());
            System.out.printf("    ID=%-2d %-18s [%s] %s\n",
                e.getId(), e.getName(), e.getEquipType().getDisplayName(),
                assigned ? "(already assigned)" : "");
        }
        int eid = InputHelper.readInt("  Equipment ID: ");
        if (adminService.addEquipmentToHero(hid, eid)) {
            Equipment e = dm.findEquipmentById(eid);
            System.out.println("[Success] '" + e.getName() + "' assigned to " + hero.getName() + ".");
        } else {
            System.out.println("[Failed] Invalid hero or equipment ID.");
        }
    }

    // ---- Team operations ----

    private static void doAddTeam() {
        System.out.println("\n--- Add New Team ---");
        System.out.println("[Tip] Enter the new team's information.");
        String name = InputHelper.readNonEmptyString("  Team Name: ");
        String date = InputHelper.readNonEmptyString("  Created Date (yyyy-MM-dd): ");
        Team t = adminService.addTeam(name, date);
        System.out.println("[Success] Team created! ID=" + t.getId() + ", Name=" + t.getName());
    }

    private static void doDeleteTeam() {
        System.out.println("\n--- Delete Team ---");
        System.out.println("[Tip] Deleting a team will remove all members from it.");
        System.out.println("  Current teams:");
        for (Team t : dm.getAllTeams()) {
            System.out.printf("    ID=%-2d %-10s Members=%d  Matches=%d\n",
                t.getId(), t.getName(), t.getPlayerIds().size(), t.getMatchRecordIds().size());
        }
        int tid = InputHelper.readInt("  Team ID: ");
        Team t = dm.findTeamById(tid);
        if (t == null) {
            System.out.println("[Failed] No team found with ID=" + tid);
            return;
        }
        String confirm = InputHelper.readLine("  Type 'yes' to confirm deletion of '" + t.getName() + "': ");
        if ("yes".equalsIgnoreCase(confirm)) {
            adminService.deleteTeam(tid);
            System.out.println("[Success] Team '" + t.getName() + "' deleted. All members are now teamless.");
        } else {
            System.out.println("[Cancelled] Deletion aborted.");
        }
    }

    private static void doListTeams() {
        System.out.println("\n--- All Teams ---");
        Collection<Team> teams = dm.getAllTeams();
        if (teams.isEmpty()) {
            System.out.println("  (No teams in the system)");
            return;
        }
        System.out.println("  ID   Name         Members  Matches  Created");
        System.out.println("  ---- ------------ -------  -------  ----------");
        for (Team t : teams) {
            System.out.printf("  %-4d %-12s %-7d %-7d  %s\n",
                t.getId(), t.getName(), t.getPlayerIds().size(),
                t.getMatchRecordIds().size(), t.getCreatedDate());
        }
    }

    // ---- Match operations ----

    private static void doAddMatchRecord() {
        System.out.println("\n--- Add Match Record ---");
        System.out.println("[Tip] Record a match between two teams. Player stats will auto-update.");

        System.out.println("  Teams:");
        for (Team t : dm.getAllTeams()) {
            System.out.printf("    ID=%-2d %s (%d members)\n", t.getId(), t.getName(), t.getPlayerIds().size());
        }

        int teamAId = InputHelper.readInt("  Team A ID: ");
        if (!dm.teamExists(teamAId)) {
            System.out.println("[Failed] Team A not found.");
            return;
        }
        int teamBId = InputHelper.readInt("  Team B ID: ");
        if (!dm.teamExists(teamBId)) {
            System.out.println("[Failed] Team B not found.");
            return;
        }
        if (teamAId == teamBId) {
            System.out.println("[Failed] A team cannot play against itself.");
            return;
        }

        Team teamA = dm.findTeamById(teamAId);
        Team teamB = dm.findTeamById(teamBId);
        System.out.println("  Match: " + teamA.getName() + " vs " + teamB.getName());

        System.out.println("  Result: 1. " + teamA.getName() + " Wins  2. " + teamB.getName() + " Wins  3. Draw");
        int resultChoice = InputHelper.readIntInRange("  Select: ", 1, 3);

        MatchResult result;
        int winnerId;
        if (resultChoice == 1) {
            result = MatchResult.TEAM_A_WIN;
            winnerId = teamAId;
        } else if (resultChoice == 2) {
            result = MatchResult.TEAM_B_WIN;
            winnerId = teamBId;
        } else {
            result = MatchResult.DRAW;
            winnerId = -1;
        }

        int duration = InputHelper.readInt("  Duration (seconds): ");
        String time = InputHelper.readNonEmptyString("  Match Time (yyyy-MM-dd HH:mm): ");

        MatchRecord m = adminService.addMatchRecord(teamAId, teamBId, result, winnerId, duration, time);
        System.out.println("[Success] Match recorded! ID=" + m.getId());
        System.out.println("[Info] Player match stats and win rates have been auto-updated.");
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

    // ============ Recommendation ============

    private static void doRecommendation() {
        while (true) {
            System.out.println("\n===== Recommendation Engine =====");
            System.out.println("1. Recommend Heroes for Me");
            System.out.println("2. Recommend Equipment for a Hero");
            System.out.println("0. Back");
            int c = InputHelper.readIntInRange("Select: ", 0, 2);

            switch (c) {
                case 1: doRecommendHeroes(); break;
                case 2: doRecommendEquipment(); break;
                case 0: return;
            }
        }
    }

    private static void doRecommendHeroes() {
        if (!auth.isLoggedIn()) {
            System.out.println("[Error] Please login first.");
            return;
        }

        Player player = null;
        Team team = null;

        if (auth.isPlayer()) {
            player = (Player) auth.getCurrentUser();
            team = dm.findTeamById(player.getTeamId());
        } else {
            // Admin: need to pick a player first
            int pid = InputHelper.readInt("Enter Player ID: ");
            player = dm.findPlayerById(pid);
            if (player == null) {
                System.out.println("[Not Found]");
                return;
            }
            team = dm.findTeamById(player.getTeamId());
        }

        int topN = InputHelper.readIntOrDefault("Show top N (default=5): ", 5);
        List<Hero> heroes = recService.recommendHeroesForPlayer(player, team, topN);

        System.out.println("\n========== Hero Recommendations for " + player.getName() + " ==========");
        if (team != null) {
            System.out.println("Team: " + team.getName());
        }
        System.out.println("Rank  Hero              Type        WR      Reason");
        System.out.println("----  ----------------  ----------  ------  ----------------------------------");
        int rank = 1;
        for (Hero h : heroes) {
            String reason = getHeroRecommendReason(player, h, team);
            System.out.printf("%-4d  %-16s  %-10s  %-5.1f%%  %s\n",
                rank++, h.getName(),
                h.getHeroType().getDisplayName(),
                h.getHeroWinRate(),
                reason);
        }
        InputHelper.waitForEnter();
    }

    private static String getHeroRecommendReason(Player player, Hero hero, Team team) {
        List<String> reasons = new ArrayList<>();

        if (player.getHeroIdList().contains(hero.getId())) {
            reasons.add("already owned");
        }

        // Check type match
        int ownedCount = 0;
        for (int hid : player.getHeroIdList()) {
            Hero h = dm.findHeroById(hid);
            if (h != null && h.getHeroType() == hero.getHeroType()) {
                ownedCount++;
            }
        }
        if (ownedCount >= 2) {
            reasons.add("matches your preference");
        }

        // Check team need
        if (team != null) {
            boolean teamHasType = false;
            for (int pid : team.getPlayerIds()) {
                Player tp = dm.findPlayerById(pid);
                if (tp != null) {
                    for (int hid : tp.getHeroIdList()) {
                        Hero th = dm.findHeroById(hid);
                        if (th != null && th.getHeroType() == hero.getHeroType()) {
                            teamHasType = true;
                            break;
                        }
                    }
                }
            }
            if (!teamHasType) {
                reasons.add("team needs this role");
            }
        }

        if (hero.getHeroWinRate() >= 53) {
            reasons.add("high win rate");
        }

        if (reasons.isEmpty()) {
            reasons.add("balanced choice");
        }

        return String.join(", ", reasons);
    }

    private static void doRecommendEquipment() {
        System.out.println("\n===== Equipment Recommendation =====");
        int heroId = InputHelper.readInt("Enter Hero ID: ");
        Hero hero = dm.findHeroById(heroId);
        if (hero == null) {
            System.out.println("[Not Found]");
            return;
        }

        Player player = null;
        if (auth.isPlayer()) {
            player = (Player) auth.getCurrentUser();
        } else {
            int pid = InputHelper.readIntOrDefault("Enter Player ID (0=skip): ", 0);
            if (pid > 0) player = dm.findPlayerById(pid);
        }

        int topN = InputHelper.readIntOrDefault("Show top N (default=5): ", 5);
        List<Equipment> equips = recService.recommendEquipmentForHero(hero, player, topN);

        System.out.println("\n===== Equipment Recommendations for " + hero.getName()
            + " [" + hero.getHeroType().getDisplayName() + "] =====");
        System.out.println("Rank  Equipment           Type        Price   Usage  Reason");
        System.out.println("----  ------------------  ----------  ------  -----  -------------------------");
        int rank = 1;
        for (Equipment e : equips) {
            String reason = getEquipRecommendReason(hero, player, e);
            System.out.printf("%-4d  %-18s  %-10s  %-6d  %-5d  %s\n",
                rank++, e.getName(),
                e.getEquipType().getDisplayName(),
                e.getPrice(),
                e.getUsageCount(),
                reason);
        }
        InputHelper.waitForEnter();
    }

    private static String getEquipRecommendReason(Hero hero, Player player, Equipment equip) {
        List<String> reasons = new ArrayList<>();

        // Check type compatibility
        if (hero.getRecommendedEquipIds().contains(equip.getId())) {
            reasons.add("recommended for this hero");
        }

        if (equip.getUsageCount() >= 3) {
            reasons.add("popular item");
        }

        if (player != null) {
            int ownedThatUse = 0;
            for (int hid : player.getHeroIdList()) {
                Hero h = dm.findHeroById(hid);
                if (h != null && h.getRecommendedEquipIds().contains(equip.getId())) {
                    ownedThatUse++;
                }
            }
            if (ownedThatUse >= 2) {
                reasons.add("used by your heroes");
            }
        }

        if (reasons.isEmpty()) {
            reasons.add("viable option");
        }

        return String.join(", ", reasons);
    }

    // ============ Utility ============

    private static String repeatStr(String s, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) sb.append(s);
        return sb.toString();
    }
}

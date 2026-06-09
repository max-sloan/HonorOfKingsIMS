package util;

import model.*;
import model.enums.*;
import service.*;

/**
 * DataInitializer - creates the initial hardcoded dataset.
 *
 * Build order is strict: Equipment -> Heroes -> Teams -> Players -> Matches.
 * Later data references earlier data's IDs.
 */
public class DataInitializer {

    public static void initAll(GameDataManager dm, AdminDataService adminService) {
        initEquipment(dm);
        initHeroes(dm, adminService);
        initTeams(dm);
        initPlayers(dm, adminService);
        initMatchRecords(dm, adminService);
        simulatePlayerStats(dm);
    }

    /** Step 1: Create 20 equipment items */
    private static void initEquipment(GameDataManager dm) {
        // Attack items
        Equipment e1 = new Equipment(1, "Breaker", EquipmentType.ATTACK, 2950);
        e1.addAttribute("Attack", 180);
        dm.addEquipment(e1);

        Equipment e2 = new Equipment(2, "Infinity Edge", EquipmentType.ATTACK, 2140);
        e2.addAttribute("Attack", 110);
        e2.addAttribute("Crit Rate", 25);
        dm.addEquipment(e2);

        Equipment e3 = new Equipment(3, "Doomsday", EquipmentType.ATTACK, 2160);
        e3.addAttribute("Attack", 60);
        e3.addAttribute("Atk Speed", 30);
        e3.addAttribute("Lifesteal", 10);
        dm.addEquipment(e3);

        Equipment e4 = new Equipment(4, "Grandmaster", EquipmentType.ATTACK, 2100);
        e4.addAttribute("Attack", 80);
        e4.addAttribute("Crit Rate", 20);
        e4.addAttribute("HP", 500);
        dm.addEquipment(e4);

        Equipment e5 = new Equipment(5, "Shadow Axe", EquipmentType.ATTACK, 2090);
        e5.addAttribute("Attack", 85);
        e5.addAttribute("CDR", 15);
        e5.addAttribute("HP", 500);
        dm.addEquipment(e5);

        Equipment e6 = new Equipment(6, "Lightning Dagger", EquipmentType.ATTACK, 1840);
        e6.addAttribute("Atk Speed", 35);
        e6.addAttribute("Crit Rate", 15);
        e6.addAttribute("Move Speed", 8);
        dm.addEquipment(e6);

        // Defense items
        Equipment e7 = new Equipment(7, "Ominous Omen", EquipmentType.DEFENSE, 2180);
        e7.addAttribute("Phys Def", 270);
        e7.addAttribute("HP", 1200);
        dm.addEquipment(e7);

        Equipment e8 = new Equipment(8, "Magic Cloak", EquipmentType.DEFENSE, 2120);
        e8.addAttribute("Magic Def", 300);
        e8.addAttribute("HP", 1000);
        dm.addEquipment(e8);

        Equipment e9 = new Equipment(9, "Thornmail", EquipmentType.DEFENSE, 1910);
        e9.addAttribute("Phys Def", 400);
        e9.addAttribute("Attack", 40);
        dm.addEquipment(e9);

        Equipment e10 = new Equipment(10, "Lava Cloak", EquipmentType.DEFENSE, 1800);
        e10.addAttribute("Phys Def", 240);
        e10.addAttribute("HP", 1000);
        dm.addEquipment(e10);

        Equipment e11 = new Equipment(11, "Frozen Storm", EquipmentType.DEFENSE, 2100);
        e11.addAttribute("Phys Def", 360);
        e11.addAttribute("CDR", 20);
        e11.addAttribute("Mana", 500);
        dm.addEquipment(e11);

        Equipment e12 = new Equipment(12, "Guardian Angel", EquipmentType.DEFENSE, 2080);
        e12.addAttribute("Phys Def", 140);
        e12.addAttribute("Magic Def", 140);
        dm.addEquipment(e12);

        // Magic items
        Equipment e13 = new Equipment(13, "Scholar's Wrath", EquipmentType.MAGIC, 2300);
        e13.addAttribute("Magic Atk", 240);
        dm.addEquipment(e13);

        Equipment e14 = new Equipment(14, "Echo Staff", EquipmentType.MAGIC, 2100);
        e14.addAttribute("Magic Atk", 240);
        e14.addAttribute("Move Speed", 7);
        dm.addEquipment(e14);

        Equipment e15 = new Equipment(15, "Torment Mask", EquipmentType.MAGIC, 2040);
        e15.addAttribute("Magic Atk", 120);
        e15.addAttribute("CDR", 5);
        e15.addAttribute("HP", 500);
        dm.addEquipment(e15);

        Equipment e16 = new Equipment(16, "Hourglass", EquipmentType.MAGIC, 1990);
        e16.addAttribute("Magic Atk", 160);
        e16.addAttribute("CDR", 10);
        dm.addEquipment(e16);

        Equipment e17 = new Equipment(17, "Sage Tome", EquipmentType.MAGIC, 2990);
        e17.addAttribute("Magic Atk", 400);
        e17.addAttribute("HP", 1000);
        dm.addEquipment(e17);

        // Movement items
        Equipment e18 = new Equipment(18, "Calm Boots", EquipmentType.MOVEMENT, 710);
        e18.addAttribute("Move Speed", 60);
        e18.addAttribute("CDR", 15);
        dm.addEquipment(e18);

        Equipment e19 = new Equipment(19, "Resist Boots", EquipmentType.MOVEMENT, 710);
        e19.addAttribute("Move Speed", 60);
        e19.addAttribute("Magic Def", 110);
        dm.addEquipment(e19);

        // Jungle item
        Equipment e20 = new Equipment(20, "Greedy Maw", EquipmentType.JUNGLE, 2160);
        e20.addAttribute("Attack", 100);
        e20.addAttribute("Atk Speed", 15);
        e20.addAttribute("Move Speed", 8);
        dm.addEquipment(e20);

        System.out.println("[Init] Created 20 equipment items");
    }

    /** Step 2: Create 15 heroes, each with 2+ recommended equipment */
    private static void initHeroes(GameDataManager dm, AdminDataService adminService) {
        // Assassins
        Hero h1 = new Hero(1, "Li Bai", HeroType.ASSASSIN, 8, "The sword immortal comes and goes like the wind");
        dm.addHero(h1);
        adminService.addEquipmentToHero(1, 1);
        adminService.addEquipmentToHero(1, 2);
        adminService.addEquipmentToHero(1, 18);

        Hero h2 = new Hero(2, "Han Xin", HeroType.ASSASSIN, 7, "The unmatched strategist king of tower pushing");
        dm.addHero(h2);
        adminService.addEquipmentToHero(2, 1);
        adminService.addEquipmentToHero(2, 4);
        adminService.addEquipmentToHero(2, 20);

        Hero h3 = new Hero(3, "Sun Wukong", HeroType.ASSASSIN, 6, "The Great Sage Equal to Heaven - one strike to the soul");
        dm.addHero(h3);
        adminService.addEquipmentToHero(3, 2);
        adminService.addEquipmentToHero(3, 6);
        adminService.addEquipmentToHero(3, 4);

        // Mages
        Hero h4 = new Hero(4, "Daji", HeroType.MAGE, 2, "Please command me freely master");
        dm.addHero(h4);
        adminService.addEquipmentToHero(4, 13);
        adminService.addEquipmentToHero(4, 14);
        adminService.addEquipmentToHero(4, 18);

        Hero h5 = new Hero(5, "Angela", HeroType.MAGE, 3, "Knowledge is power");
        dm.addHero(h5);
        adminService.addEquipmentToHero(5, 13);
        adminService.addEquipmentToHero(5, 15);
        adminService.addEquipmentToHero(5, 16);

        Hero h6 = new Hero(6, "Diao Chan", HeroType.MAGE, 7, "Flowers bloom and fall across the sky");
        dm.addHero(h6);
        adminService.addEquipmentToHero(6, 14);
        adminService.addEquipmentToHero(6, 17);
        adminService.addEquipmentToHero(6, 11);

        Hero h7 = new Hero(7, "Wang Zhaojun", HeroType.MAGE, 4, "The harsh winter has arrived");
        dm.addHero(h7);
        adminService.addEquipmentToHero(7, 13);
        adminService.addEquipmentToHero(7, 15);

        Hero h8 = new Hero(8, "Zhuge Liang", HeroType.MAGE, 6, "The world is a chessboard calculated in three moves");
        dm.addHero(h8);
        adminService.addEquipmentToHero(8, 14);
        adminService.addEquipmentToHero(8, 16);
        adminService.addEquipmentToHero(8, 18);

        // Marksmen
        Hero h9 = new Hero(9, "Luban No.7", HeroType.MARKSMAN, 3, "Master Luban - IQ 250");
        dm.addHero(h9);
        adminService.addEquipmentToHero(9, 2);
        adminService.addEquipmentToHero(9, 6);
        adminService.addEquipmentToHero(9, 3);

        Hero h10 = new Hero(10, "Hou Yi", HeroType.MARKSMAN, 4, "I shot down Sunday");
        dm.addHero(h10);
        adminService.addEquipmentToHero(10, 2);
        adminService.addEquipmentToHero(10, 3);
        adminService.addEquipmentToHero(10, 6);

        // Warriors
        Hero h11 = new Hero(11, "Arthur", HeroType.WARRIOR, 1, "The face of Honor of Kings");
        dm.addHero(h11);
        adminService.addEquipmentToHero(11, 5);
        adminService.addEquipmentToHero(11, 7);
        adminService.addEquipmentToHero(11, 12);

        Hero h14 = new Hero(14, "Lu Bu", HeroType.WARRIOR, 5, "Where is my Diao Chan");
        dm.addHero(h14);
        adminService.addEquipmentToHero(14, 1);
        adminService.addEquipmentToHero(14, 5);
        adminService.addEquipmentToHero(14, 7);

        // Tanks
        Hero h12 = new Hero(12, "Cheng Yaojin", HeroType.TANK, 3, "One word: Fight!");
        dm.addHero(h12);
        adminService.addEquipmentToHero(12, 10);
        adminService.addEquipmentToHero(12, 7);
        adminService.addEquipmentToHero(12, 8);

        Hero h13 = new Hero(13, "Lian Po", HeroType.TANK, 4, "I am very strong");
        dm.addHero(h13);
        adminService.addEquipmentToHero(13, 7);
        adminService.addEquipmentToHero(13, 9);

        // Assassin
        Hero h15 = new Hero(15, "Lanling Wang", HeroType.ASSASSIN, 6, "The hunter in the dark night");
        dm.addHero(h15);
        adminService.addEquipmentToHero(15, 1);
        adminService.addEquipmentToHero(15, 5);
        adminService.addEquipmentToHero(15, 19);

        System.out.println("[Init] Created 15 heroes");
    }

    /** Step 3: Create 3 teams */
    private static void initTeams(GameDataManager dm) {
        dm.addTeam(new Team(1, "AG", "2023-06-01"));
        dm.addTeam(new Team(2, "Wolves", "2023-06-15"));
        dm.addTeam(new Team(3, "eStar", "2023-07-01"));
        System.out.println("[Init] Created 3 teams");
    }

    /** Step 4: Create 12 players (4 per team) */
    private static void initPlayers(GameDataManager dm, AdminDataService adminService) {
        // AG (teamId=1): MengLei, YiNuo, ChangSheng, Cat
        Player p1 = new Player(1, "MengLei", "123", 28, 2500);
        dm.addPlayer(p1);
        adminService.assignPlayerToTeam(1, 1);
        adminService.addHeroToPlayer(1, 1);
        adminService.addHeroToPlayer(1, 2);
        adminService.addHeroToPlayer(1, 3);

        Player p2 = new Player(2, "YiNuo", "123", 26, 2450);
        dm.addPlayer(p2);
        adminService.assignPlayerToTeam(2, 1);
        adminService.addHeroToPlayer(2, 9);
        adminService.addHeroToPlayer(2, 10);
        adminService.addHeroToPlayer(2, 1);

        Player p3 = new Player(3, "ChangSheng", "123", 24, 2200);
        dm.addPlayer(p3);
        adminService.assignPlayerToTeam(3, 1);
        adminService.addHeroToPlayer(3, 4);
        adminService.addHeroToPlayer(3, 5);
        adminService.addHeroToPlayer(3, 6);

        Player p4 = new Player(4, "Cat", "123", 27, 2350);
        dm.addPlayer(p4);
        adminService.assignPlayerToTeam(4, 1);
        adminService.addHeroToPlayer(4, 8);
        adminService.addHeroToPlayer(4, 7);
        adminService.addHeroToPlayer(4, 13);

        // Wolves (teamId=2): Fly, YaoDao, XiaoPang, XiangYu
        Player p5 = new Player(5, "Fly", "123", 28, 2600);
        dm.addPlayer(p5);
        adminService.assignPlayerToTeam(5, 2);
        adminService.addHeroToPlayer(5, 14);
        adminService.addHeroToPlayer(5, 11);
        adminService.addHeroToPlayer(5, 12);

        Player p6 = new Player(6, "YaoDao", "123", 26, 2400);
        dm.addPlayer(p6);
        adminService.assignPlayerToTeam(6, 2);
        adminService.addHeroToPlayer(6, 9);
        adminService.addHeroToPlayer(6, 10);
        adminService.addHeroToPlayer(6, 3);

        Player p7 = new Player(7, "XiaoPang", "123", 25, 2300);
        dm.addPlayer(p7);
        adminService.assignPlayerToTeam(7, 2);
        adminService.addHeroToPlayer(7, 1);
        adminService.addHeroToPlayer(7, 2);
        adminService.addHeroToPlayer(7, 15);

        Player p8 = new Player(8, "XiangYu", "123", 24, 2150);
        dm.addPlayer(p8);
        adminService.assignPlayerToTeam(8, 2);
        adminService.addHeroToPlayer(8, 4);
        adminService.addHeroToPlayer(8, 5);
        adminService.addHeroToPlayer(8, 14);

        // eStar (teamId=3): HuaHai, QingRong, TanRan, ZiYang
        Player p9 = new Player(9, "HuaHai", "123", 27, 2500);
        dm.addPlayer(p9);
        adminService.assignPlayerToTeam(9, 3);
        adminService.addHeroToPlayer(9, 1);
        adminService.addHeroToPlayer(9, 3);
        adminService.addHeroToPlayer(9, 15);

        Player p10 = new Player(10, "QingRong", "123", 26, 2380);
        dm.addPlayer(p10);
        adminService.assignPlayerToTeam(10, 3);
        adminService.addHeroToPlayer(10, 5);
        adminService.addHeroToPlayer(10, 6);
        adminService.addHeroToPlayer(10, 8);

        Player p11 = new Player(11, "TanRan", "123", 25, 2250);
        dm.addPlayer(p11);
        adminService.assignPlayerToTeam(11, 3);
        adminService.addHeroToPlayer(11, 11);
        adminService.addHeroToPlayer(11, 12);
        adminService.addHeroToPlayer(11, 13);

        Player p12 = new Player(12, "ZiYang", "123", 24, 2100);
        dm.addPlayer(p12);
        adminService.assignPlayerToTeam(12, 3);
        adminService.addHeroToPlayer(12, 7);
        adminService.addHeroToPlayer(12, 9);
        adminService.addHeroToPlayer(12, 2);

        System.out.println("[Init] Created 12 players");
    }

    /** Step 5+6: Create 10 match records */
    private static void initMatchRecords(GameDataManager dm, AdminDataService adminService) {
        // AG vs Wolves
        adminService.addMatchRecord(1, 2, MatchResult.TEAM_A_WIN, 1, 1200, "2024-01-15 14:30");
        adminService.addMatchRecord(1, 2, MatchResult.TEAM_B_WIN, 2, 1350, "2024-01-22 15:00");
        adminService.addMatchRecord(1, 2, MatchResult.TEAM_A_WIN, 1, 1100, "2024-02-05 13:00");

        // AG vs eStar
        adminService.addMatchRecord(1, 3, MatchResult.TEAM_A_WIN, 1, 1250, "2024-02-10 14:00");
        adminService.addMatchRecord(1, 3, MatchResult.TEAM_B_WIN, 3, 1400, "2024-02-18 16:30");
        adminService.addMatchRecord(1, 3, MatchResult.TEAM_A_WIN, 1, 1180, "2024-03-01 14:00");

        // Wolves vs eStar
        adminService.addMatchRecord(2, 3, MatchResult.TEAM_B_WIN, 3, 1300, "2024-03-08 15:30");
        adminService.addMatchRecord(2, 3, MatchResult.TEAM_A_WIN, 2, 1150, "2024-03-15 13:00");
        adminService.addMatchRecord(2, 3, MatchResult.DRAW, -1, 1500, "2024-03-22 16:00");

        // Bonus: AG vs Wolves
        adminService.addMatchRecord(1, 2, MatchResult.TEAM_B_WIN, 2, 1280, "2024-04-01 14:30");

        System.out.println("[Init] Created 10 match records");
    }

    /** Simulate individual player match stats for realistic win rates */
    private static void simulatePlayerStats(GameDataManager dm) {
        // AG: MengLei(8m/5w), YiNuo(6m/4w), ChangSheng(4m/2w), Cat(7m/4w)
        simulateStats(dm, 1, 8, 5);
        simulateStats(dm, 2, 6, 4);
        simulateStats(dm, 3, 4, 2);
        simulateStats(dm, 4, 7, 4);

        // Wolves: Fly(10m/7w), YaoDao(6m/3w), XiaoPang(5m/3w), XiangYu(4m/2w)
        simulateStats(dm, 5, 10, 7);
        simulateStats(dm, 6, 6, 3);
        simulateStats(dm, 7, 5, 3);
        simulateStats(dm, 8, 4, 2);

        // eStar: HuaHai(8m/5w), QingRong(6m/3w), TanRan(5m/3w), ZiYang(4m/2w)
        simulateStats(dm, 9, 8, 5);
        simulateStats(dm, 10, 6, 3);
        simulateStats(dm, 11, 5, 3);
        simulateStats(dm, 12, 4, 2);

        System.out.println("[Init] Simulated player match stats");
    }

    private static void simulateStats(GameDataManager dm, int playerId, int total, int winCount) {
        Player p = dm.findPlayerById(playerId);
        if (p != null) {
            for (int i = 0; i < total; i++) {
                p.incrementMatch(i < winCount);
            }
        }
    }
}

package util;

import model.*;
import model.enums.*;
import service.*;

/**
 * DataInitializer —— 数据初始化器
 *
 * 在程序第一次运行时创建初始硬编码数据集。
 * 数据构建必须严格按顺序：装备 → 英雄 → 战队 → 玩家 → 比赛
 * 因为后面的数据引用前面的数据ID。
 */
public class DataInitializer {

    /**
     * 初始化全部数据
     */
    public static void initAll(GameDataManager dm, AdminDataService adminService) {
        initEquipment(dm);
        initHeroes(dm, adminService);
        initTeams(dm);
        initPlayers(dm, adminService);
        initMatchRecords(dm, adminService);
        simulatePlayerStats(dm);  // 模拟比赛数据，让胜率不再是0
    }

    /**
     * 第1步：创建 20 件装备
     */
    private static void initEquipment(GameDataManager dm) {
        // 攻击装
        Equipment e1 = new Equipment(1, "破军", EquipmentType.ATTACK, 2950);
        e1.addAttribute("攻击力", 180);
        dm.addEquipment(e1);

        Equipment e2 = new Equipment(2, "无尽战刃", EquipmentType.ATTACK, 2140);
        e2.addAttribute("攻击力", 110);
        e2.addAttribute("暴击率", 25);
        dm.addEquipment(e2);

        Equipment e3 = new Equipment(3, "末世", EquipmentType.ATTACK, 2160);
        e3.addAttribute("攻击力", 60);
        e3.addAttribute("攻速", 30);
        e3.addAttribute("吸血", 10);
        dm.addEquipment(e3);

        Equipment e4 = new Equipment(4, "宗师之力", EquipmentType.ATTACK, 2100);
        e4.addAttribute("攻击力", 80);
        e4.addAttribute("暴击率", 20);
        e4.addAttribute("生命值", 500);
        dm.addEquipment(e4);

        Equipment e5 = new Equipment(5, "暗影战斧", EquipmentType.ATTACK, 2090);
        e5.addAttribute("攻击力", 85);
        e5.addAttribute("冷却缩减", 15);
        e5.addAttribute("生命值", 500);
        dm.addEquipment(e5);

        Equipment e6 = new Equipment(6, "闪电匕首", EquipmentType.ATTACK, 1840);
        e6.addAttribute("攻速", 35);
        e6.addAttribute("暴击率", 15);
        e6.addAttribute("移速", 8);
        dm.addEquipment(e6);

        // 防御装
        Equipment e7 = new Equipment(7, "不祥征兆", EquipmentType.DEFENSE, 2180);
        e7.addAttribute("物理防御", 270);
        e7.addAttribute("生命值", 1200);
        dm.addEquipment(e7);

        Equipment e8 = new Equipment(8, "魔女斗篷", EquipmentType.DEFENSE, 2120);
        e8.addAttribute("法术防御", 300);
        e8.addAttribute("生命值", 1000);
        dm.addEquipment(e8);

        Equipment e9 = new Equipment(9, "反伤刺甲", EquipmentType.DEFENSE, 1910);
        e9.addAttribute("物理防御", 400);
        e9.addAttribute("攻击力", 40);
        dm.addEquipment(e9);

        Equipment e10 = new Equipment(10, "红莲斗篷", EquipmentType.DEFENSE, 1800);
        e10.addAttribute("物理防御", 240);
        e10.addAttribute("生命值", 1000);
        dm.addEquipment(e10);

        Equipment e11 = new Equipment(11, "极寒风暴", EquipmentType.DEFENSE, 2100);
        e11.addAttribute("物理防御", 360);
        e11.addAttribute("冷却缩减", 20);
        e11.addAttribute("法力值", 500);
        dm.addEquipment(e11);

        Equipment e12 = new Equipment(12, "贤者的庇护", EquipmentType.DEFENSE, 2080);
        e12.addAttribute("物理防御", 140);
        e12.addAttribute("法术防御", 140);
        dm.addEquipment(e12);

        // 法术装
        Equipment e13 = new Equipment(13, "博学者之怒", EquipmentType.MAGIC, 2300);
        e13.addAttribute("法术攻击", 240);
        dm.addEquipment(e13);

        Equipment e14 = new Equipment(14, "回响之杖", EquipmentType.MAGIC, 2100);
        e14.addAttribute("法术攻击", 240);
        e14.addAttribute("移速", 7);
        dm.addEquipment(e14);

        Equipment e15 = new Equipment(15, "痛苦面具", EquipmentType.MAGIC, 2040);
        e15.addAttribute("法术攻击", 120);
        e15.addAttribute("冷却缩减", 5);
        e15.addAttribute("生命值", 500);
        dm.addEquipment(e15);

        Equipment e16 = new Equipment(16, "辉月", EquipmentType.MAGIC, 1990);
        e16.addAttribute("法术攻击", 160);
        e16.addAttribute("冷却缩减", 10);
        dm.addEquipment(e16);

        Equipment e17 = new Equipment(17, "贤者之书", EquipmentType.MAGIC, 2990);
        e17.addAttribute("法术攻击", 400);
        e17.addAttribute("生命值", 1000);
        dm.addEquipment(e17);

        // 移动装
        Equipment e18 = new Equipment(18, "冷静之靴", EquipmentType.MOVEMENT, 710);
        e18.addAttribute("移速", 60);
        e18.addAttribute("冷却缩减", 15);
        dm.addEquipment(e18);

        Equipment e19 = new Equipment(19, "抵抗之靴", EquipmentType.MOVEMENT, 710);
        e19.addAttribute("移速", 60);
        e19.addAttribute("法术防御", 110);
        dm.addEquipment(e19);

        // 打野装
        Equipment e20 = new Equipment(20, "贪婪之噬", EquipmentType.JUNGLE, 2160);
        e20.addAttribute("攻击力", 100);
        e20.addAttribute("攻速", 15);
        e20.addAttribute("移速", 8);
        dm.addEquipment(e20);

        System.out.println("[初始化] 已创建 20 件装备");
    }

    /**
     * 第2步：创建 15 个英雄，每个至少关联 2 件装备
     */
    private static void initHeroes(GameDataManager dm, AdminDataService adminService) {
        // 战士
        Hero h1 = new Hero(1, "李白", HeroType.ASSASSIN, 8, "剑仙李白，来去如风");
        dm.addHero(h1);
        adminService.addEquipmentToHero(1, 1);  // 破军
        adminService.addEquipmentToHero(1, 2);  // 无尽战刃
        adminService.addEquipmentToHero(1, 18); // 冷静之靴

        Hero h2 = new Hero(2, "韩信", HeroType.ASSASSIN, 7, "国士无双，偷塔之王");
        dm.addHero(h2);
        adminService.addEquipmentToHero(2, 1);  // 破军
        adminService.addEquipmentToHero(2, 4);  // 宗师之力
        adminService.addEquipmentToHero(2, 20); // 贪婪之噬

        Hero h3 = new Hero(3, "孙悟空", HeroType.ASSASSIN, 6, "齐天大圣，一棒入魂");
        dm.addHero(h3);
        adminService.addEquipmentToHero(3, 2);  // 无尽战刃
        adminService.addEquipmentToHero(3, 6);  // 闪电匕首
        adminService.addEquipmentToHero(3, 4);  // 宗师之力

        // 法师
        Hero h4 = new Hero(4, "妲己", HeroType.MAGE, 2, "请尽情吩咐妲己，主人");
        dm.addHero(h4);
        adminService.addEquipmentToHero(4, 13); // 博学者之怒
        adminService.addEquipmentToHero(4, 14); // 回响之杖
        adminService.addEquipmentToHero(4, 18); // 冷静之靴

        Hero h5 = new Hero(5, "安琪拉", HeroType.MAGE, 3, "知识就是力量");
        dm.addHero(h5);
        adminService.addEquipmentToHero(5, 13); // 博学者之怒
        adminService.addEquipmentToHero(5, 15); // 痛苦面具
        adminService.addEquipmentToHero(5, 16); // 辉月

        Hero h6 = new Hero(6, "貂蝉", HeroType.MAGE, 7, "花开花落花满天");
        dm.addHero(h6);
        adminService.addEquipmentToHero(6, 14); // 回响之杖
        adminService.addEquipmentToHero(6, 17); // 贤者之书
        adminService.addEquipmentToHero(6, 11); // 极寒风暴

        Hero h7 = new Hero(7, "王昭君", HeroType.MAGE, 4, "凛冬已至");
        dm.addHero(h7);
        adminService.addEquipmentToHero(7, 13); // 博学者之怒
        adminService.addEquipmentToHero(7, 15); // 痛苦面具

        Hero h8 = new Hero(8, "诸葛亮", HeroType.MAGE, 6, "天下如棋，一步三算");
        dm.addHero(h8);
        adminService.addEquipmentToHero(8, 14); // 回响之杖
        adminService.addEquipmentToHero(8, 16); // 辉月
        adminService.addEquipmentToHero(8, 18); // 冷静之靴

        // 射手
        Hero h9 = new Hero(9, "鲁班七号", HeroType.MARKSMAN, 3, "鲁班大师，智商二百五");
        dm.addHero(h9);
        adminService.addEquipmentToHero(9, 2);  // 无尽战刃
        adminService.addEquipmentToHero(9, 6);  // 闪电匕首
        adminService.addEquipmentToHero(9, 3);  // 末世

        Hero h10 = new Hero(10, "后羿", HeroType.MARKSMAN, 4, "周日被我射熄火了");
        dm.addHero(h10);
        adminService.addEquipmentToHero(10, 2); // 无尽战刃
        adminService.addEquipmentToHero(10, 3); // 末世
        adminService.addEquipmentToHero(10, 6); // 闪电匕首

        // 坦克
        Hero h11 = new Hero(11, "亚瑟", HeroType.WARRIOR, 1, "王者荣耀的代言人");
        dm.addHero(h11);
        adminService.addEquipmentToHero(11, 5); // 暗影战斧
        adminService.addEquipmentToHero(11, 7); // 不祥征兆
        adminService.addEquipmentToHero(11, 12); // 贤者的庇护

        Hero h12 = new Hero(12, "程咬金", HeroType.TANK, 3, "一个字，干！");
        dm.addHero(h12);
        adminService.addEquipmentToHero(12, 10); // 红莲斗篷
        adminService.addEquipmentToHero(12, 7);  // 不祥征兆
        adminService.addEquipmentToHero(12, 8);  // 魔女斗篷

        Hero h13 = new Hero(13, "廉颇", HeroType.TANK, 4, "我可是很强的");
        dm.addHero(h13);
        adminService.addEquipmentToHero(13, 7);  // 不祥征兆
        adminService.addEquipmentToHero(13, 9);  // 反伤刺甲

        Hero h14 = new Hero(14, "吕布", HeroType.WARRIOR, 5, "我的貂蝉在哪里");
        dm.addHero(h14);
        adminService.addEquipmentToHero(14, 1);  // 破军
        adminService.addEquipmentToHero(14, 5);  // 暗影战斧
        adminService.addEquipmentToHero(14, 7);  // 不祥征兆

        // 辅助
        Hero h15 = new Hero(15, "兰陵王", HeroType.ASSASSIN, 6, "暗夜中的猎手");
        dm.addHero(h15);
        adminService.addEquipmentToHero(15, 1);  // 破军
        adminService.addEquipmentToHero(15, 5);  // 暗影战斧
        adminService.addEquipmentToHero(15, 19); // 抵抗之靴

        System.out.println("[初始化] 已创建 15 个英雄");
    }

    /**
     * 第3步：创建 3 支战队
     */
    private static void initTeams(GameDataManager dm) {
        Team t1 = new Team(1, "AG", "2023-06-01");
        dm.addTeam(t1);

        Team t2 = new Team(2, "Wolves", "2023-06-15");
        dm.addTeam(t2);

        Team t3 = new Team(3, "eStar", "2023-07-01");
        dm.addTeam(t3);

        System.out.println("[初始化] 已创建 3 支战队");
    }

    /**
     * 第4步：创建 12 名玩家（每队4人）
     */
    private static void initPlayers(GameDataManager dm, AdminDataService adminService) {
        // === AG（teamId=1）：梦泪、一诺、长生、Cat ===
        Player p1 = new Player(1, "梦泪", "123", 28, 2500);
        dm.addPlayer(p1);
        adminService.assignPlayerToTeam(1, 1);
        adminService.addHeroToPlayer(1, 1);  // 李白
        adminService.addHeroToPlayer(1, 2);  // 韩信
        adminService.addHeroToPlayer(1, 3);  // 孙悟空

        Player p2 = new Player(2, "一诺", "123", 26, 2450);
        dm.addPlayer(p2);
        adminService.assignPlayerToTeam(2, 1);
        adminService.addHeroToPlayer(2, 9);  // 鲁班七号
        adminService.addHeroToPlayer(2, 10); // 后羿
        adminService.addHeroToPlayer(2, 1);  // 李白

        Player p3 = new Player(3, "长生", "123", 24, 2200);
        dm.addPlayer(p3);
        adminService.assignPlayerToTeam(3, 1);
        adminService.addHeroToPlayer(3, 4);  // 妲己
        adminService.addHeroToPlayer(3, 5);  // 安琪拉
        adminService.addHeroToPlayer(3, 6);  // 貂蝉

        Player p4 = new Player(4, "Cat", "123", 27, 2350);
        dm.addPlayer(p4);
        adminService.assignPlayerToTeam(4, 1);
        adminService.addHeroToPlayer(4, 8);  // 诸葛亮
        adminService.addHeroToPlayer(4, 7);  // 王昭君
        adminService.addHeroToPlayer(4, 13); // 廉颇

        // === Wolves（teamId=2）：Fly、妖刀、小胖、向鱼 ===
        Player p5 = new Player(5, "Fly", "123", 28, 2600);
        dm.addPlayer(p5);
        adminService.assignPlayerToTeam(5, 2);
        adminService.addHeroToPlayer(5, 14); // 吕布
        adminService.addHeroToPlayer(5, 11); // 亚瑟
        adminService.addHeroToPlayer(5, 12); // 程咬金

        Player p6 = new Player(6, "妖刀", "123", 26, 2400);
        dm.addPlayer(p6);
        adminService.assignPlayerToTeam(6, 2);
        adminService.addHeroToPlayer(6, 9);  // 鲁班七号
        adminService.addHeroToPlayer(6, 10); // 后羿
        adminService.addHeroToPlayer(6, 3);  // 孙悟空

        Player p7 = new Player(7, "小胖", "123", 25, 2300);
        dm.addPlayer(p7);
        adminService.assignPlayerToTeam(7, 2);
        adminService.addHeroToPlayer(7, 1);  // 李白
        adminService.addHeroToPlayer(7, 2);  // 韩信
        adminService.addHeroToPlayer(7, 15); // 兰陵王

        Player p8 = new Player(8, "向鱼", "123", 24, 2150);
        dm.addPlayer(p8);
        adminService.assignPlayerToTeam(8, 2);
        adminService.addHeroToPlayer(8, 4);  // 妲己
        adminService.addHeroToPlayer(8, 5);  // 安琪拉
        adminService.addHeroToPlayer(8, 14); // 吕布

        // === eStar（teamId=3）：花海、清融、坦然、子阳 ===
        Player p9 = new Player(9, "花海", "123", 27, 2500);
        dm.addPlayer(p9);
        adminService.assignPlayerToTeam(9, 3);
        adminService.addHeroToPlayer(9, 1);  // 李白
        adminService.addHeroToPlayer(9, 3);  // 孙悟空
        adminService.addHeroToPlayer(9, 15); // 兰陵王

        Player p10 = new Player(10, "清融", "123", 26, 2380);
        dm.addPlayer(p10);
        adminService.assignPlayerToTeam(10, 3);
        adminService.addHeroToPlayer(10, 5);  // 安琪拉
        adminService.addHeroToPlayer(10, 6);  // 貂蝉
        adminService.addHeroToPlayer(10, 8);  // 诸葛亮

        Player p11 = new Player(11, "坦然", "123", 25, 2250);
        dm.addPlayer(p11);
        adminService.assignPlayerToTeam(11, 3);
        adminService.addHeroToPlayer(11, 11); // 亚瑟
        adminService.addHeroToPlayer(11, 12); // 程咬金
        adminService.addHeroToPlayer(11, 13); // 廉颇

        Player p12 = new Player(12, "子阳", "123", 24, 2100);
        dm.addPlayer(p12);
        adminService.assignPlayerToTeam(12, 3);
        adminService.addHeroToPlayer(12, 7);  // 王昭君
        adminService.addHeroToPlayer(12, 9);  // 鲁班七号
        adminService.addHeroToPlayer(12, 2);  // 韩信

        System.out.println("[初始化] 已创建 12 名玩家");
    }

    /**
     * 第6步：创建 10 条比赛记录
     */
    private static void initMatchRecords(GameDataManager dm, AdminDataService adminService) {
        // AG vs Wolves 系列
        adminService.addMatchRecord(1, 2, MatchResult.TEAM_A_WIN, 1, 1200, "2024-01-15 14:30");
        adminService.addMatchRecord(1, 2, MatchResult.TEAM_B_WIN, 2, 1350, "2024-01-22 15:00");
        adminService.addMatchRecord(1, 2, MatchResult.TEAM_A_WIN, 1, 1100, "2024-02-05 13:00");

        // AG vs eStar 系列
        adminService.addMatchRecord(1, 3, MatchResult.TEAM_A_WIN, 1, 1250, "2024-02-10 14:00");
        adminService.addMatchRecord(1, 3, MatchResult.TEAM_B_WIN, 3, 1400, "2024-02-18 16:30");
        adminService.addMatchRecord(1, 3, MatchResult.TEAM_A_WIN, 1, 1180, "2024-03-01 14:00");

        // Wolves vs eStar 系列
        adminService.addMatchRecord(2, 3, MatchResult.TEAM_B_WIN, 3, 1300, "2024-03-08 15:30");
        adminService.addMatchRecord(2, 3, MatchResult.TEAM_A_WIN, 2, 1150, "2024-03-15 13:00");
        adminService.addMatchRecord(2, 3, MatchResult.DRAW, -1, 1500, "2024-03-22 16:00");

        // 额外一场 AG vs Wolves
        adminService.addMatchRecord(1, 2, MatchResult.TEAM_B_WIN, 2, 1280, "2024-04-01 14:30");

        System.out.println("[初始化] 已创建 10 条比赛记录");
    }

    /**
     * 模拟每个玩家的比赛数据，让胜率真实有意义。
     * 各队明星选手场次多、胜率高，替补选手场次少。
     */
    private static void simulatePlayerStats(GameDataManager dm) {
        // AG (teamId=1): 梦泪(8场5胜), 一诺(6场4胜), 长生(4场2胜), Cat(7场4胜)
        simulateStats(dm, 1, 8, 5);
        simulateStats(dm, 2, 6, 4);
        simulateStats(dm, 3, 4, 2);
        simulateStats(dm, 4, 7, 4);

        // Wolves (teamId=2): Fly(10场7胜), 妖刀(6场3胜), 小胖(5场3胜), 向鱼(4场2胜)
        simulateStats(dm, 5, 10, 7);
        simulateStats(dm, 6, 6, 3);
        simulateStats(dm, 7, 5, 3);
        simulateStats(dm, 8, 4, 2);

        // eStar (teamId=3): 花海(8场5胜), 清融(6场3胜), 坦然(5场3胜), 子阳(4场2胜)
        simulateStats(dm, 9, 8, 5);
        simulateStats(dm, 10, 6, 3);
        simulateStats(dm, 11, 5, 3);
        simulateStats(dm, 12, 4, 2);

        System.out.println("[初始化] 已模拟玩家比赛胜率数据");
    }

    /**
     * 帮一个玩家模拟 total 场比赛，其中前 winCount 场为胜。
     * incrementMatch(true) 会让 totalMatches 和 wins 各自累加，并自动重算 winRate。
     */
    private static void simulateStats(GameDataManager dm, int playerId, int total, int winCount) {
        Player p = dm.findPlayerById(playerId);
        if (p != null) {
            for (int i = 0; i < total; i++) {
                p.incrementMatch(i < winCount);
            }
        }
    }
}

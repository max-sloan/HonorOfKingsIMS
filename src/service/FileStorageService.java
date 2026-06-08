package service;

import model.*;
import model.enums.*;
import java.io.*;
import java.util.*;

/**
 * FileStorageService —— 文件存储服务
 *
 * CSV 格式（逗号分隔），多值字段用分号分隔。
 * 文件保存在 data/ 文件夹下。
 *
 * 加载顺序很重要：装备→英雄→战队→玩家→比赛
 */
public class FileStorageService {
    private static final String DATA_DIR = "data";

    /**
     * 保存全部数据
     */
    public void saveAll(GameDataManager dm) throws IOException {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdir();

        savePlayers(dm);
        saveHeroes(dm);
        saveEquipment(dm);
        saveTeams(dm);
        saveMatches(dm);

        System.out.println("[成功] 数据已保存到 " + DATA_DIR + "/ 文件夹");
    }

    /**
     * 加载全部数据（会覆盖内存中的现有数据）
     */
    public void loadAll(GameDataManager dm) throws IOException {
        // 按依赖顺序加载
        loadEquipment(dm);
        loadHeroes(dm);
        loadTeams(dm);
        loadPlayers(dm);
        loadMatches(dm);

        System.out.println("[成功] 数据已从 " + DATA_DIR + "/ 文件夹加载");
    }

    // ============================================
    //  保存
    // ============================================

    private void savePlayers(GameDataManager dm) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_DIR + "/players.csv"))) {
            pw.println("id,name,passwordHash,level,rankScore,totalMatches,wins,winRate,teamId,teamName,heroIds");
            for (Player p : dm.getAllPlayers()) {
                String heroIds = String.join(";",
                    p.getHeroIdList().stream().map(String::valueOf).toArray(String[]::new));
                pw.printf(Locale.US, "%d,%s,%d,%d,%d,%d,%d,%.1f,%d,%s,%s\n",
                    p.getId(), p.getName(), p.getPasswordHash(),  // 用 Person 新增的 getPasswordHash()
                    p.getLevel(), p.getRankScore(),
                    p.getTotalMatches(), p.getWins(), p.getWinRate(),
                    p.getTeamId(), p.getTeamName(), heroIds);
            }
        }
    }

    private void saveHeroes(GameDataManager dm) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_DIR + "/heroes.csv"))) {
            pw.println("id,name,heroType,difficulty,description,equipIds");
            for (Hero h : dm.getAllHeroes()) {
                String equipIds = String.join(";",
                    h.getRecommendedEquipIds().stream().map(String::valueOf).toArray(String[]::new));
                pw.printf(Locale.US, "%d,%s,%s,%d,%s,%s\n",
                    h.getId(), h.getName(), h.getHeroType().name(),
                    h.getDifficulty(), h.getDescription(), equipIds);
            }
        }
    }

    private void saveEquipment(GameDataManager dm) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_DIR + "/equipment.csv"))) {
            pw.println("id,name,equipType,price,attributes,usageCount");
            for (Equipment e : dm.getAllEquipments()) {
                StringBuilder attrStr = new StringBuilder();
                for (Map.Entry<String, Integer> entry : e.getAttributeMap().entrySet()) {
                    if (attrStr.length() > 0) attrStr.append(";");
                    attrStr.append(entry.getKey()).append(":").append(entry.getValue());
                }
                pw.printf(Locale.US, "%d,%s,%s,%d,%s,%d\n",
                    e.getId(), e.getName(), e.getEquipType().name(),
                    e.getPrice(), attrStr.toString(), e.getUsageCount());
            }
        }
    }

    private void saveTeams(GameDataManager dm) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_DIR + "/teams.csv"))) {
            pw.println("id,name,createdDate,playerIds,matchIds");
            for (Team t : dm.getAllTeams()) {
                String playerIds = String.join(";",
                    t.getPlayerIds().stream().map(String::valueOf).toArray(String[]::new));
                String matchIds = String.join(";",
                    t.getMatchRecordIds().stream().map(String::valueOf).toArray(String[]::new));
                pw.printf(Locale.US, "%d,%s,%s,%s,%s\n",
                    t.getId(), t.getName(), t.getCreatedDate(), playerIds, matchIds);
            }
        }
    }

    private void saveMatches(GameDataManager dm) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_DIR + "/matches.csv"))) {
            pw.println("id,teamAId,teamBId,result,winningTeamId,duration,matchTime");
            for (MatchRecord m : dm.getAllMatches()) {
                pw.printf(Locale.US, "%d,%d,%d,%s,%d,%d,%s\n",
                    m.getId(), m.getTeamAId(), m.getTeamBId(),
                    m.getResult().name(), m.getWinningTeamId(),
                    m.getDuration(), m.getMatchTime());
            }
        }
    }

    // ============================================
    //  加载
    // ============================================

    private void loadEquipment(GameDataManager dm) throws IOException {
        File file = new File(DATA_DIR + "/equipment.csv");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // 跳过标题
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                EquipmentType equipType = EquipmentType.valueOf(parts[2]);
                int price = Integer.parseInt(parts[3]);
                Equipment e = new Equipment(id, parts[1], equipType, price);

                // 加载属性 "key1:val1;key2:val2"
                if (parts.length > 4 && !parts[4].isEmpty()) {
                    for (String attr : parts[4].split(";")) {
                        String[] kv = attr.split(":");
                        e.addAttribute(kv[0], Integer.parseInt(kv[1]));
                    }
                }
                // 恢复 usageCount
                if (parts.length > 5 && !parts[5].isEmpty()) {
                    for (int i = 0; i < Integer.parseInt(parts[5]); i++) {
                        e.incrementUsage();
                    }
                }
                dm.addEquipment(e);
            }
        }
        System.out.println("  [加载] 装备完成");
    }

    private void loadHeroes(GameDataManager dm) throws IOException {
        File file = new File(DATA_DIR + "/heroes.csv");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                HeroType heroType = HeroType.valueOf(parts[2]);
                int difficulty = Integer.parseInt(parts[3]);
                Hero h = new Hero(id, parts[1], heroType, difficulty, parts[4]);

                // 加载推荐装备
                if (parts.length > 5 && !parts[5].isEmpty()) {
                    for (String equipId : parts[5].split(";")) {
                        h.addEquipment(Integer.parseInt(equipId));
                    }
                }
                dm.addHero(h);
            }
        }
        System.out.println("  [加载] 英雄完成");
    }

    private void loadTeams(GameDataManager dm) throws IOException {
        File file = new File(DATA_DIR + "/teams.csv");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                Team t = new Team(id, parts[1], parts[2]);  // id, name, createdDate

                // 加载玩家ID
                if (parts.length > 3 && !parts[3].isEmpty()) {
                    for (String playerId : parts[3].split(";")) {
                        t.addPlayer(Integer.parseInt(playerId));
                    }
                }
                // 加载比赛ID
                if (parts.length > 4 && !parts[4].isEmpty()) {
                    for (String matchId : parts[4].split(";")) {
                        t.addMatchRecord(Integer.parseInt(matchId));
                    }
                }
                dm.addTeam(t);
            }
        }
        System.out.println("  [加载] 战队完成");
    }

    /**
     * 加载玩家 —— 完整还原所有字段
     *
     * CSV 格式:
     * id,name,passwordHash,level,rankScore,totalMatches,wins,winRate,teamId,teamName,heroIds
     */
    private void loadPlayers(GameDataManager dm) throws IOException {
        File file = new File(DATA_DIR + "/players.csv");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // 跳过标题
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                int passwordHash = Integer.parseInt(parts[2]);
                int level = Integer.parseInt(parts[3]);
                int rankScore = Integer.parseInt(parts[4]);
                int totalMatches = Integer.parseInt(parts[5]);
                int wins = Integer.parseInt(parts[6]);
                int teamId = Integer.parseInt(parts[8]);
                String teamName = parts[9];

                // 创建玩家（密码用占位，马上覆盖哈希）
                Player p = new Player(id, name, "temp", level, rankScore);
                p.setPasswordHash(passwordHash);   // 恢复原始密码哈希
                p.setTotalMatches(totalMatches);   // 恢复比赛数据
                p.setWins(wins);
                p.updateWinRate();                  // 根据 totalMatches+wins 重算胜率
                p.setTeamId(teamId);
                p.setTeamName(teamName);

                // 加载英雄
                if (parts.length > 10 && !parts[10].isEmpty()) {
                    for (String heroId : parts[10].split(";")) {
                        p.addHero(Integer.parseInt(heroId));
                    }
                }

                dm.addPlayer(p);
            }
        }
        System.out.println("  [加载] 玩家完成");
    }

    private void loadMatches(GameDataManager dm) throws IOException {
        File file = new File(DATA_DIR + "/matches.csv");
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                int teamAId = Integer.parseInt(parts[1]);
                int teamBId = Integer.parseInt(parts[2]);
                MatchResult result = MatchResult.valueOf(parts[3]);
                int winningTeamId = Integer.parseInt(parts[4]);
                int duration = Integer.parseInt(parts[5]);
                String matchTime = parts[6];

                MatchRecord m = new MatchRecord(id, teamAId, teamBId,
                    result, winningTeamId, duration, matchTime);
                dm.addMatchRecord(m);
            }
        }
        System.out.println("  [加载] 比赛记录完成");
    }
}

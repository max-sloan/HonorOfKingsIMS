package service;

import model.*;
import model.enums.*;
import java.io.*;
import java.util.*;

/**
 * FileStorageService - save/load data via CSV files
 *
 * CSV (Comma-Separated Values): simple text format.
 * Multi-value fields (like heroIdList) use semicolons as delimiters.
 * Files are stored in the data/ folder.
 *
 * Load order matters: Equipment -> Heroes -> Teams -> Players -> Matches
 */
public class FileStorageService {
    private static final String DATA_DIR = "data";

    public boolean dataExists() {
        File dir = new File(DATA_DIR);
        if (!dir.exists() || !dir.isDirectory()) return false;
        String[] files = dir.list();
        return files != null && files.length > 0;
    }

    public void saveAll(GameDataManager dm) throws IOException {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) dir.mkdir();

        savePlayers(dm);
        saveHeroes(dm);
        saveEquipment(dm);
        saveTeams(dm);
        saveMatches(dm);

        System.out.println("[Success] Data saved to " + DATA_DIR + "/");
    }

    public void loadAll(GameDataManager dm) throws IOException {
        File dir = new File(DATA_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("[Error] data/ folder not found. Save data first.");
            return;
        }
        int loaded = 0;
        if (loadEquipment(dm)) loaded++;
        if (loadHeroes(dm)) loaded++;
        if (loadTeams(dm)) loaded++;
        if (loadPlayers(dm)) loaded++;
        if (loadMatches(dm)) loaded++;

        if (loaded > 0) {
            System.out.println("[Success] Loaded " + loaded + " data types from " + DATA_DIR + "/");
        } else {
            System.out.println("[Error] data/ folder is empty. Save data first.");
        }
    }

    // ============ Save ============

    private void savePlayers(GameDataManager dm) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_DIR + "/players.csv"))) {
            pw.println("id,name,password,level,rankScore,totalMatches,wins,winRate,teamId,teamName,heroIds");
            for (Player p : dm.getAllPlayers()) {
                String heroIds = String.join(";",
                    p.getHeroIdList().stream().map(String::valueOf).toArray(String[]::new));
                pw.printf(Locale.US, "%d,%s,%s,%d,%d,%d,%d,%.1f,%d,%s,%s\n",
                    p.getId(), p.getName(), p.getPassword(),
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

    // ============ Load ============

    private boolean loadEquipment(GameDataManager dm) throws IOException {
        File file = new File(DATA_DIR + "/equipment.csv");
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                EquipmentType equipType = EquipmentType.valueOf(parts[2]);
                int price = Integer.parseInt(parts[3]);
                Equipment e = new Equipment(id, parts[1], equipType, price);
                if (parts.length > 4 && !parts[4].isEmpty()) {
                    for (String attr : parts[4].split(";")) {
                        String[] kv = attr.split(":");
                        e.addAttribute(kv[0], Integer.parseInt(kv[1]));
                    }
                }
                if (parts.length > 5 && !parts[5].isEmpty()) {
                    for (int i = 0; i < Integer.parseInt(parts[5]); i++) {
                        e.incrementUsage();
                    }
                }
                dm.addEquipment(e);
            }
        }
        System.out.println("  [Load] Equipment OK");
        return true;
    }

    private boolean loadHeroes(GameDataManager dm) throws IOException {
        File file = new File(DATA_DIR + "/heroes.csv");
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                HeroType heroType = HeroType.valueOf(parts[2]);
                int difficulty = Integer.parseInt(parts[3]);
                Hero h = new Hero(id, parts[1], heroType, difficulty, parts[4]);
                if (parts.length > 5 && !parts[5].isEmpty()) {
                    for (String equipId : parts[5].split(";")) {
                        h.addEquipment(Integer.parseInt(equipId));
                    }
                }
                dm.addHero(h);
            }
        }
        System.out.println("  [Load] Heroes OK");
        return true;
    }

    private boolean loadTeams(GameDataManager dm) throws IOException {
        File file = new File(DATA_DIR + "/teams.csv");
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                Team t = new Team(id, parts[1], parts[2]);
                if (parts.length > 3 && !parts[3].isEmpty()) {
                    for (String playerId : parts[3].split(";")) {
                        t.addPlayer(Integer.parseInt(playerId));
                    }
                }
                if (parts.length > 4 && !parts[4].isEmpty()) {
                    for (String matchId : parts[4].split(";")) {
                        t.addMatchRecord(Integer.parseInt(matchId));
                    }
                }
                dm.addTeam(t);
            }
        }
        System.out.println("  [Load] Teams OK");
        return true;
    }

    private boolean loadPlayers(GameDataManager dm) throws IOException {
        File file = new File(DATA_DIR + "/players.csv");
        if (!file.exists()) return false;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String password = parts[2];
                int level = Integer.parseInt(parts[3]);
                int rankScore = Integer.parseInt(parts[4]);
                int totalMatches = Integer.parseInt(parts[5]);
                int wins = Integer.parseInt(parts[6]);
                int teamId = Integer.parseInt(parts[8]);
                String teamName = parts[9];

                Player p = new Player(id, name, password, level, rankScore);
                p.setTotalMatches(totalMatches);
                p.setWins(wins);
                p.updateWinRate();
                p.setTeamId(teamId);
                p.setTeamName(teamName);

                if (parts.length > 10 && !parts[10].isEmpty()) {
                    for (String heroId : parts[10].split(";")) {
                        p.addHero(Integer.parseInt(heroId));
                    }
                }
                dm.addPlayer(p);
            }
        }
        System.out.println("  [Load] Players OK");
        return true;
    }

    private boolean loadMatches(GameDataManager dm) throws IOException {
        File file = new File(DATA_DIR + "/matches.csv");
        if (!file.exists()) return false;

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
        System.out.println("  [Load] Matches OK");
        return true;
    }
}

package model;

import model.interfaces.Identifiable;
import model.interfaces.Reportable;
import java.util.ArrayList;
import java.util.List;

/**
 * Team class
 *
 * Implements both Identifiable and Reportable.
 * A class can implement multiple interfaces but can only extend one parent class.
 * Uses ID references to link players and matches.
 */
public class Team implements Identifiable, Reportable {
    private int id;
    private String name;
    private String createdDate;
    private List<Integer> playerIds;
    private List<Integer> matchRecordIds;

    public Team(int id, String name, String createdDate) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
        this.playerIds = new ArrayList<>();
        this.matchRecordIds = new ArrayList<>();
    }

    public void addPlayer(int playerId) {
        if (!playerIds.contains(playerId)) {
            playerIds.add(playerId);
        }
    }

    public void removePlayer(int playerId) {
        playerIds.remove(Integer.valueOf(playerId));
    }

    public void addMatchRecord(int matchId) {
        if (!matchRecordIds.contains(matchId)) {
            matchRecordIds.add(matchId);
        }
    }

    @Override
    public int getId() { return id; }
    @Override
    public String getName() { return name; }
    public String getCreatedDate() { return createdDate; }
    public List<Integer> getPlayerIds() { return playerIds; }
    public List<Integer> getMatchRecordIds() { return matchRecordIds; }

    public void displayInfo() {
        System.out.println("===== Team Overview =====");
        System.out.println("Name: " + name);
        System.out.println("Created: " + createdDate);
        System.out.println("Members: " + playerIds.size());
        System.out.println("Matches: " + matchRecordIds.size());
    }

    @Override
    public String generateReport() {
        return "Team[" + name + "] " + playerIds.size() + " members, " + matchRecordIds.size() + " matches";
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}

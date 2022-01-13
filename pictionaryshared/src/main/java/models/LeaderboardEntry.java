package models;

public class LeaderboardEntry {
    private String username;
    private int score;

    public String getUsername() {
        return this.username;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public LeaderboardEntry(String username, int score) {
        this.username = username;
        this.score = score;
    }

    @Override
    public String toString() {
        return username + " [" + score + "]";
    }

}
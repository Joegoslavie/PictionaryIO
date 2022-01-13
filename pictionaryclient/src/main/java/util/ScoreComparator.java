package util;

import models.LeaderboardEntry;

import java.util.Comparator;

public class ScoreComparator implements Comparator<LeaderboardEntry> {

    @Override
    public int compare(LeaderboardEntry o1, LeaderboardEntry o2) {
        if(o1.getScore() > o2.getScore()) {
            return -1;
        } else if (o1.getScore() < o2.getScore()) {
            return 1;
        } else {
            return 0;
        }
    }
}

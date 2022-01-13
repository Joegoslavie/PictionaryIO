package other;

import java.util.Arrays;

public class Levenshtein {

    public static int minDistance(String string1, String string2) {
        int[][] distanceBetweenPrefixes = new int[string1.length()][string2.length()];
        for (int[] row : distanceBetweenPrefixes) {
            Arrays.fill(row, -1);
        }
        return calculateDistanceBetweenPrefixes(string1, string1.length() - 1, string2, string2.length() - 1, distanceBetweenPrefixes);
    }

    private static int calculateDistanceBetweenPrefixes(String string1, int string1_length, String string2, int string2_length, int[][] distanceBetweenPrefixes) {

        //Als de substring van positie 0 naar 0 is is de string leeg, dus moeten alle letters van string2 toegevoegd worden, dus wordt de lengte van string2 teruggegeven.
        if(string1_length < 0) {
            return string2_length + 1;
        } else if (string2_length < 0) {
            return string1_length + 1;
        }

        //Als de distance nog niet is berekend, dan gaan we berekenen
        if(distanceBetweenPrefixes[string1_length][string2_length] == -1) {
            //Als de letters gelijk zijn hoeft er niets te gebeuren.
            if(string1.charAt(string1_length) == string2.charAt(string2_length)) {
                distanceBetweenPrefixes[string1_length][string2_length] = calculateDistanceBetweenPrefixes(string1, string1_length - 1, string2, string2_length - 1, distanceBetweenPrefixes);
            } else {
                //Als de letters niet gelijk zijn zijn er 3 mogelijke opties:
                //- Letter vervangen
                //- Letter toevoegen
                //- Letter verwijderen

                int substituteLast = calculateDistanceBetweenPrefixes(string1, string1_length - 1, string2, string2_length - 1, distanceBetweenPrefixes);
                int addLast = calculateDistanceBetweenPrefixes(string1, string1_length, string2, string2_length - 1, distanceBetweenPrefixes);
                int deleteLast = calculateDistanceBetweenPrefixes(string1, string1_length - 1, string2, string2_length, distanceBetweenPrefixes);

                //Kijken welke optie het minimum is en die gebruiken
                distanceBetweenPrefixes[string1_length][string2_length] = 1 + Math.min(substituteLast, Math.min(addLast, deleteLast));
            }
        }

        return distanceBetweenPrefixes[string1_length][string2_length];
    }
}

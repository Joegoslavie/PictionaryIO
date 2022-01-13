package other;

import java.util.Random;

public class Utils {

    private static Random rdm = new Random();
    private static String chars = "abcdefghijklmnopqrstuvwxyz";

    public static String generateToken(int length){
        char[] text = new char[length];

        for (int i = 0; i < length; i++)
            text[i] = chars.charAt(rdm.nextInt(chars.length()));

        return new String(text);
    }

    public static int calculateEditDistance(String guessedWord, String wordToGuess) {
        return Levenshtein.minDistance(guessedWord, wordToGuess);
    }
}

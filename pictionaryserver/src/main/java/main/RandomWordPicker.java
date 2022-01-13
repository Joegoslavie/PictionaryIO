package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

// TODO, alter the class so that it can do more than just pick completely random words from a preset list

public class RandomWordPicker {
    private static final String FILE = "words.txt";

    private static ArrayList<String> wordList = new ArrayList<>();
    private Random rng;

    private HashSet<String> previousRoundWords = new HashSet<>();

    public RandomWordPicker() throws IOException {
        rng = new Random();
        fillWordList();
    }

    private void fillWordList() throws IOException {
        if(wordList.isEmpty()) {
            BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(FILE));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                wordList.add(line);
            }
        }
    }

    public String getRandomWord() {
        int randomIndex = rng.nextInt(wordList.size());
        return wordList.get(randomIndex);
    }

    public ArrayList<String> getRandomWords(int amount) {
        ArrayList<String> words = new ArrayList<>();

        for(int i = 0; i < amount; i++) {
            boolean unusedWord = false;
            while (!unusedWord) {
                String word = getRandomWord();
                if(!previousRoundWords.contains(word) && !words.contains(word)) {
                    unusedWord = true;
                    words.add(word);
                }
            }
        }

        previousRoundWords.clear();
        previousRoundWords.addAll(words);

        return words;
    }
}

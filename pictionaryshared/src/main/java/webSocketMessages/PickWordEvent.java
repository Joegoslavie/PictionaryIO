package webSocketMessages;

import java.util.ArrayList;

public class PickWordEvent {
    public ArrayList<String> newWords;
    public String chosenWord;

    public PickWordEvent(){}
    public PickWordEvent(String chosenWord) {
        this.chosenWord = chosenWord;
    }
    public PickWordEvent(ArrayList<String> newWords, String chosenWord){
        this.newWords = newWords;
        this.chosenWord = chosenWord;
    }
}

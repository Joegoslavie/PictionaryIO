package models;

import enums.RoundState;
import enums.ToolType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class Round {
    private String drawerName;
    private RoundState roundState;
    private String wordToDraw;
    //TODO: Add Bitmap

    public String getDrawerName() {
        return this.drawerName;
    }

    public RoundState getRoundState() {
        return this.roundState;
    }

    public String getWordToDraw() {
        return this.wordToDraw;
    }

    public void setWordToDraw(String word) {
        this.wordToDraw = word;
    }

    public void setRoundState(RoundState roundState) {
        this.roundState = roundState;
    }

    public Round(String drawerName) {
        this.drawerName = drawerName;
        this.roundState = RoundState.CHOOSING;
    }

    public void  setToolSize(double size) {

    }

    public void setPencilColor(Color color) {

    }

    public void guessWord(String word) {

    }

    public void setToolType(ToolType toolType) {

    }

    public void setShape(Shape shape) {

    }
}

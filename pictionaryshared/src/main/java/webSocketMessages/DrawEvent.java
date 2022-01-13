package webSocketMessages;

public class DrawEvent {
    int startX;
    int endX;
    int startY;
    int endY;

    //TODO: Add pencil


    public DrawEvent(){}
    public DrawEvent(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endX;
    }
}

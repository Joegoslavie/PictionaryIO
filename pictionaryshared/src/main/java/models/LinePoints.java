package models;

public class LinePoints {
    double x;
    double y;
    double lastX;
    double lastY;

    String color;
    double brushSize;

    public LinePoints(double x, double y, double lastX, double lasyY, String color, double brushSize) {
        this.x = x;
        this.y = y;
        this.lastX = lastX;
        this.lastY = lasyY;
        this.color = color;
        this.brushSize = brushSize;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getLastX() {
        return lastX;
    }

    public double getLastY() {
        return lastY;
    }

    public String getColor() {
        return color;
    }

    public double getBrushSize() {
        return brushSize;
    }
}

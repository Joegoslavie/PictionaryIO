package models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.Line;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LinePointsTest {
    LinePoints linePoints;
    boolean exceptionThrown;

    @BeforeEach
    public void setUp(){
        linePoints = new LinePoints(1, 1, 0, 0, "Red", 2);
        exceptionThrown = false;
    }

    //constructors (and getters/setters)
    @Test
    public void constructorTest(){
        LinePoints linePoints = null;
        try{
            linePoints = new LinePoints(1, 1, 0, 0, "Red", 2);
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
    }

    @Test
    public void gettersTest(){
        double x = -1;
        double y = -1;
        double lastX = -1;
        double lasyY = -1;
        String color = null;
        double brushSize = -1;
        try{
            x = linePoints.getX();
            y = linePoints.getY();
            lastX = linePoints.getLastX();
            lasyY = linePoints.getLastY();
            color = linePoints.getColor();
            brushSize = linePoints.getBrushSize();
        }
        catch(Exception e){
            exceptionThrown = true;
        }
        assertFalse(exceptionThrown);
        assertEquals(x, 1);
        assertEquals(y, 1);
        assertEquals(lastX, 0);
        assertEquals(lasyY, 0);
        assertEquals(color, "Red");
        assertEquals(brushSize, 2);
    }
}
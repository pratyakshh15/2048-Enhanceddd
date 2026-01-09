package enhanced2048;

import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import processing.core.PImage;

public class Cell { 


    public int x, y;
    public int targetX, targetY;
    public int value;
    public static Map<Integer, PImage> tileImages = new HashMap<>();


    public Cell(int x, int y, int value) {
        this.x = this.targetX = x;
        this.y = this.targetY = y;
        this.value = value;
    }


    public static void load_tiles(PApplet app){
        int[] values = {2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096};
        for (int num : values) {
            String path = "enhanced2048/" + num + ".png";
            tileImages.put(num, app.loadImage(path));
        }
    }


    public void setTarget(int targetX, int targetY) {
        this.targetX = targetX;
        this.targetY = targetY;
    }


    public int getValue() {
        return this.value;
    }

    public void setValue(int val) {
        this.value = val;
    }


    public void draw(PApplet app){
        int speed = 30;
        if (x < targetX) 
            x += speed;
        if (x > targetX) 
            x -= speed;
        if (y < targetY) 
            y += speed;
        if (y > targetY)
            y -= speed;

        if (Math.abs(x - targetX) < speed) 
            x = targetX;
        if (Math.abs(y - targetY) < speed) 
            y = targetY;

        
        PImage image = tileImages.get(this.value);
        app.image(image, x, y, App.CELL_SIZE, App.CELL_SIZE);

    }

}
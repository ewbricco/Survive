package main.eastin.Survive.Utils;

import main.eastin.Survive.GameState;

import static org.lwjgl.opengl.GL11.glColor3f;

/**
 * Created by ebricco on 6/26/16.
 */
public class Color {
    private float red;
    private float green;
    private float blue;

    public Color() {
        this(GameState.RAND.nextFloat(), GameState.RAND.nextFloat(), GameState.RAND.nextFloat());
    }

    public Color(float red, float green, float blue) {
        if (red > 1) {
            this.red = 1;
        } else if (red < 0) {
            red = 0;
        } else {
            this.red = red;
        }

        if (green > 1) {
            this.green = 1;
        } else if (green < 0) {
            green = 0;
        } else {
            this.green = green;
        }

        if (blue > 1) {
            this.blue = 1;
        } else if (blue < 0) {
            blue = 0;
        } else {
            this.blue = blue;
        }
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public void incRed(float increase) {
        red += increase;
        if(red > 1){
            red = 1;
        }
    }

    public void incGreen(float increase) {
        green += increase;
        if(green > 1){
            green = 1;
        }
    }

    public void incBlue(float increase) {
        blue += increase;
        if(blue > 1){
            blue = 1;
        }
    }

    public void decRed(float decrease) {
        red -= decrease;
        if(red < 0){
            red = 0;
        }
    }

    public void decGreen(float decrease) {
        green -= decrease;
        if(green < 0){
            green = 0;
        }
    }


    public void decBlue(float decrease) {
        blue -= decrease;
        if(blue < 0){
            blue = 0;
        }
    }

    public static Color invert(Color color){
        color.red = 1 - color.red;
        color.green = 1 - color.green;
        color.blue = 1 - color.blue;
        return color;
    }

    public void setColor() {
        glColor3f(red, green, blue);
    }
}

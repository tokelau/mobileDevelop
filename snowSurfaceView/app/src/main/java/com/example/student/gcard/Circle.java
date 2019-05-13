package com.example.student.gcard;


import android.util.Log;

public class Circle {
    public float x, y;
    public int color;
    public Circle() {
        x = -1;
        y = -1;
        color = -1;
    }
    public Circle(float _x, float _y, int _color) {
        x = _x;
        y = _y;
        color = _color;
    }
    public float setXPos(float xPos) {
//        Log.w("Nasty", Float.toString(xPos));
        if (xPos < 40) {
            return 1050;
//            Log.w("Nasty", Float.toString(this.x));
        } else if (xPos > 1050) {
            return 40;
        }
        return xPos;
    }
    public float setYPos(float yPos) {
        if (yPos < 40) {
            return 1830;
        } else if (yPos > 1830) {
            return 1830;
        }
        return yPos;
    }
    public boolean checkPos(int _color) {
        if (color == _color && x <= 100) {
            if (y >= 600 && y <= 1000) {
                return true;
            }
        }
        return false;
    }
}

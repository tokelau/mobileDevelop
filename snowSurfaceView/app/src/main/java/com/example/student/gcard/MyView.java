package com.example.student.gcard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class MyView extends View {

    float x = 100, y = 100;
    float snowX[], snowY[], velocity[];
    float maxX = 1000, maxY = 1000;

    public MyView(Context context,  AttributeSet attrs) {

        super(context, attrs);

        // 1) создать два массива координат со случайными
        // значениями от 0 до 1
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.w("Nasty", "onDraw");
        super.onDraw(canvas);
        canvas.drawColor(Color.rgb(0,0,50));
        maxX = canvas.getWidth();
        maxY = canvas.getHeight();

        Paint p = new Paint();
        p.setColor(Color.YELLOW);
        p.setColor(Color.rgb(100,100,100));
        p.setColor(Color.parseColor("#FFEECC"));
        canvas.drawCircle(x,y,10,p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.w("Nasty", "onTouchEvent");
        x = event.getX();
        y = event.getY();
        invalidate();
       // при нажатии круг должен смещаться вниз
        // а при выходе за пределы экрана появляться вверху
        return false;
    }
}

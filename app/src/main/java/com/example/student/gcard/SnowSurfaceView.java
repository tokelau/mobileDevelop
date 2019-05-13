package com.example.student.gcard;


import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class SnowSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    DrawThread thread;

    // центр объекта, например, круга
    ArrayList<Circle> circles;
    float center_x1 = (new Random().nextFloat() * 1000), center_x2 = (new Random().nextFloat() * 1000);
    float center_x3 = (new Random().nextFloat() * 1000), center_x4 = (new Random().nextFloat() * 1000);
    float center_x5 = (new Random().nextFloat() * 1000), center_x6 = (new Random().nextFloat() * 1000);
    float center_y1 = (new Random().nextFloat() * 1000), center_y2 = (new Random().nextFloat() * 1000);
    float center_y3 = (new Random().nextFloat() * 1000), center_y4 = (new Random().nextFloat() * 1000);
    float center_y5 = (new Random().nextFloat() * 1000), center_y6 = (new Random().nextFloat() * 1000);
    int rndColor;
    Toast toast;
    public SnowSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
//        center_x = (new Random().nextFloat() * 2000) % this.getHeight();
//        center_y = (new Random().nextFloat() * 2000) % this.getWidth();
        circles = new ArrayList<Circle>();
        circles.add(new Circle(center_x1, center_y1, Color.YELLOW));
        circles.add(new Circle(center_x2, center_y2, Color.CYAN));
        circles.add(new Circle(center_x3, center_y3, Color.RED));
        circles.add(new Circle(center_x4, center_y4, Color.GREEN));
        circles.add(new Circle(center_x5, center_y5, Color.MAGENTA));
        circles.add(new Circle(center_x6, center_y6, Color.WHITE));

        rndColor = circles.get(Math.abs(new Random().nextInt() * 10 % circles.size())).color;
        toast = Toast.makeText(context, "Пора покормить кота! Игра закончилась :)", Toast.LENGTH_SHORT);
//        Log.w("Nasty", Integer.toString(rnd));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float tx = event.getX();
        float ty = event.getY();
        int i = findNearest(tx, ty);
        Circle cur = new Circle();
        if (i != -1) {
            cur = circles.get(i);
        }

//        Log.w("Nasty", Float.toString(cur.x));
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            center_x = tx;
//            center_y = ty;
//        }
//        else
        if (event.getAction() == MotionEvent.ACTION_MOVE)  {
            if (Math.abs(cur.x - tx) < 60 && Math.abs(cur.y - ty) < 60) {
                cur.x = cur.setXPos(tx); 
                cur.y = cur.setYPos(ty);
            }
            if (cur.checkPos(rndColor)) {
                circles.remove(cur);
                Log.w("Nasty", Integer.toString(circles.size()));
                if (circles.size() > 0) {
                    rndColor = circles.get(Math.abs(new Random().nextInt() * 10 % circles.size())).color;
                }
            }
        }
        return true;
    }

    class DrawThread extends  Thread {
        boolean isRunning = true; // флаг для управления потоком
        SurfaceHolder holder;
        Paint p; // объект-кисть
        DrawThread(SurfaceHolder holder) {
            super();
            this.holder = holder;
            p = new Paint();
        }

        @Override
        public void run() {
            while (isRunning) {

                try {
                    sleep(50);
                } catch (InterruptedException e) {}

                Canvas c = holder.lockCanvas();
                // проверить, существует ли канва
                if (c != null) {
//                    Log.w("Nasty", "run");
                    c.drawColor(Color.BLACK);
                    if (circles.size() > 0) {
                        for (int i = 0; i < circles.size(); i++) {
                            p.setColor(circles.get(i).color);
                            c.drawCircle(circles.get(i).x, circles.get(i).y, 20, p);
                        }
                        p.setColor(rndColor);
                        c.drawRect(0, 600, 100, 1000, p);
                    } else {
                        toast.show();
                    }
                    holder.unlockCanvasAndPost(c);
                }
            }
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        thread = new DrawThread(surfaceHolder);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // при изменении поверхности перезапустить поток
        thread.isRunning = false;
        if (surfaceHolder != null) {
            thread = new DrawThread(surfaceHolder);
            thread.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // остановить поток, если поверхности больше нет
        thread.isRunning = false;
    }

    //найти ближайшую окружность
    private int findNearest(float tx, float ty) {
        if (circles.size() == 0) {
            return -1;
        }
        double x = Math.abs(circles.get(0).x - tx);
        double y = Math.abs(circles.get(0).y - ty);
        double minDist = Math.hypot(x, y);
        int min = 0;
        for(int i = 1; i < circles.size(); i++) {
            x = Math.abs(circles.get(i).x - tx);
            y = Math.abs(circles.get(i).y - ty);
            double dist = Math.hypot(x, y);
            if (dist < minDist) {
                minDist = dist;
                min = i;
            }
        }
        return min;
    }
}

package com.example.breakout;

import android.graphics.RectF;

    public class Bat {

        private RectF rect;

        private float length;

        private float x;

        int mScreenX;
        int mScreenY;

        private float paddleSpeed;

        final int STOPPED = 0;
        final int LEFT = 1;
        final int RIGHT = 2;

        private int paddleMoving = STOPPED;

    Bat(int screenX, int screenY){


        mScreenX = screenX;
        mScreenY = screenY;

        length = mScreenX / 8;
        float height = mScreenY / 25;

        x = screenX / 2;

        float y = screenY - 20;

        rect = new RectF(x, y, x + length, y+ height);
        paddleSpeed = 500;
    }

    RectF  getRect(){
        return rect;
    }

    void setMovementState(int state){
        paddleMoving = state;
    }

    void update(long fps){
        if(paddleMoving == LEFT){
            x = x - paddleSpeed / fps;
        }

        if(paddleMoving == RIGHT){
            x = x + paddleSpeed / fps;
        }

        if(rect.left < 0){ x= 0; } if(rect.right > mScreenX){
            x = mScreenX - (rect.right - rect.left);
        }




        rect.left = x;
        rect.right = x + length;
    }

}

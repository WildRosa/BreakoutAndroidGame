package com.example.breakout;

import android.graphics.RectF;

import java.util.Random;


public class Ball {

    private RectF rect;
    private float xVelocity;
    private float yVelocity;
    private float ballWidth = 20;
    private float ballHeight = 20;

  /*  Ball(){
    xVelocity = 200;
    yVelocity = -400;

    rect = new RectF();
    }
*/

    public Ball(int screenX, int screenY){
        // Make the mBall size relative to the screen resolution
        //ballWidth = screenX / 100;
        //ballHeight = ballWidth;

    /*
        Start the ball travelling straight up
        at a quarter of the screen height per second
    */
        yVelocity = screenY / 4;
        xVelocity = yVelocity;

        // Initialize the Rect that represents the mBall
        rect = new RectF();
    }

    RectF getRect(){
        return rect;
    }

    void update(long fps){
        rect.left = rect.left + (xVelocity / fps);
        rect.top = rect.top + (yVelocity / fps);
        rect.right = rect.left + ballWidth;
        rect.bottom = rect.top - ballHeight;
    }

    void reverseYVelocity(){
        yVelocity = -yVelocity;
    }

    void reverseXVelocity(){
        xVelocity = -xVelocity;
    }

    void setRandomXVelocity(){
        Random generator = new Random();
        int answer = generator.nextInt(2);

        if(answer == 0){
            reverseXVelocity();
        }
    }

    void clearObstacleY(float y){
        rect.bottom = y;
        rect.top = y - ballHeight;
    }

    void clearObstacleX(float x){
        rect.left = x;
        rect.right = x + ballWidth;
    }

    void reset(int x, int y){
        rect.left = x / 2;
        rect.top = y - 20;
        rect.right = x / 2 + ballWidth;
        rect.bottom = y - 20 - ballHeight;
    }

}

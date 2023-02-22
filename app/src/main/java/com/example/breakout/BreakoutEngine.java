package com.example.breakout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;

import java.io.IOException;
import java.util.Arrays;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class BreakoutEngine extends SurfaceView implements Runnable{

    private Thread gameThred = null;


    private SurfaceHolder ourHolder;

    private volatile boolean playing;

    private boolean paused = true;

    private Canvas canvas;

    private Paint paint;

    private int screenX;
    private int screenY;

    private long fps;

    private long timeThisFrame;

    private int buildSecondLayer = 0;

    private int level = 1;


    Bat bat;
    Ball ball;

    SoundPool soundPool;
    int batSound = -1;
    int bounce = -1;
    int lose = -1;
    int wall = -1;
    int brickSound = -1;

    int timerCheck= 0;
    Timer timer = new Timer();
    Brick[] bricks = new Brick[100];
    Brick[] bricks2 = new Brick[100];
    Brick[] bricks3 = new Brick[100];
    int[] checkBrick = new int[40];

    int time = 0;
    int numBricks = 0;
    int numBricks2 = 0;
    int attempt = 6;
    int attemptcheck = 0;
    int score = 0;
    int lives = 3;

    public BreakoutEngine(Context context, int x, int y) {
        super(context);

        ourHolder = getHolder();
        paint = new Paint();

        Arrays.fill(checkBrick,2);
        screenX = x;
        screenY = y;

        bat = new Bat(screenX, screenY);
        ball = new Ball(screenX, screenY);

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        try{
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;
            descriptor = assetManager.openFd("batSound.mp3");
            batSound = soundPool.load(descriptor,0);

            descriptor = assetManager.openFd("bounce.mp3");
            bounce = soundPool.load(descriptor,0);

            descriptor = assetManager.openFd("lose.mp3");
            lose = soundPool.load(descriptor,0);

            descriptor = assetManager.openFd("wall.mp3");
            wall = soundPool.load(descriptor,0);

            descriptor = assetManager.openFd("brickSound.mp3");
            brickSound = soundPool.load(descriptor,0);

        }
        catch (IOException e ){
            Log.e("error", "failed to load sound files");
        }

        restart();
    }


    public void pause(){

        playing = false;
        try{
            gameThred.join();
        }catch (InterruptedException e){
            Log.e("Error:", "joining thread");
        }



    }

    public void resume(){

        playing = true;
        gameThred = new Thread(this);
        gameThred.start();



    }



    @Override
    public void run() {

        while(playing){

            long startFrameTime = System.currentTimeMillis();



            if(!paused){
                update();


            }

            draw();
            if(!paused){
                TimerTask t = new TimerTask() {
                    @Override
                    public void run() {

                        time +=1000;
                    }
                };
                //timer.scheduleAtFixedRate(t,1000,1000);
                timer.schedule(t, 1000);

            }



            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if(timeThisFrame >= 1){
                fps = 1000/timeThisFrame;
            }

        }


    }

    private void update(){

        bat.update(fps);
        ball.update(fps);


        if(buildSecondLayer == 0 && level == 1){
            for(int i = 0;  i < numBricks2; i++){
                bricks3[i].setInvisible();
            }
            buildSecondLayer += 1;

        }

        if(level == 1){
            for(int i = 0; i < numBricks; i++){
                if(bricks[i].getVisibility()){
                    if(RectF.intersects(bricks[i].getRect(), ball.getRect())){
                        bricks[i].setInvisible();
                        score += 10;
                        ball.reverseYVelocity();
                        soundPool.play(bounce, 1, 1, 0, 0, 1);

                    }
                }

            }
        }
        if(level == 3){
            for(int i =0; i < numBricks2; i++){
                if((RectF.intersects(bricks2[i].getRect(), ball.getRect()) || RectF.intersects(bricks3[i].getRect(), ball.getRect())) && checkBrick[i] !=0){
                    checkBrick[i] -= 1;
                    if(checkBrick[i] == 0){
                        bricks3[i].setInvisible();
                        bricks2[i].setInvisible();
                        soundPool.play(bounce, 1, 1, 0, 0, 1);
                        //bricks[i].setInvisible();
                        score += 20;
                    }
                    if(checkBrick[i] == 1){
                        bricks2[i].setVisible();
                        bricks3[i].setInvisible();
                        soundPool.play(brickSound, 1, 1, 0, 0, 1);


                    }
                    ball.reverseYVelocity();



                }
            }
        }

        if(level == 2){

            for(int i =0; i < numBricks2; i++){
                if((RectF.intersects(bricks2[i].getRect(), ball.getRect()) || RectF.intersects(bricks3[i].getRect(), ball.getRect())) && checkBrick[i] !=0){
                    checkBrick[i] -= 1;
                    if(checkBrick[i] == 0){
                        bricks3[i].setInvisible();
                        bricks2[i].setInvisible();
                        //bricks[i].setInvisible();
                        soundPool.play(bounce, 1, 1, 0, 0, 1);
                        score += 20;
                    }
                    if(checkBrick[i] == 1){

                        bricks3[i].setInvisible();
                        bricks2[i].setVisible();
                        soundPool.play(brickSound, 1, 1, 0, 0, 1);


                    }
                    ball.reverseYVelocity();



                }
            }




            for(int i = 0; i < numBricks; i++){

                if(bricks[i].getVisibility()){

                    if(RectF.intersects(bricks[i].getRect(), ball.getRect())){
                        bricks[i].setInvisible();
                        ball.reverseYVelocity();
                        score += 10;
                        soundPool.play(bounce, 1, 1, 0, 0, 1);

                    }


                }

            }
        }



if(RectF.intersects(bat.getRect(), ball.getRect())){
    ball.setRandomXVelocity();
    ball.reverseYVelocity();
    ball.clearObstacleY(bat.getRect().top -2);
    attempt -= 1;
    soundPool.play(batSound, 1, 1, 0, 0, 1);
}

if(ball.getRect().bottom > screenY) {
    ball.reverseYVelocity();
    ball.clearObstacleY(screenY - 2);

    lives--;
    soundPool.play(lose, 1, 1, 0, 0, 1);
    if(lives < 3 && lives != 0){
        ball.reset(screenX, screenY);
        bat = new Bat(screenX, screenY);
        paused = true;
       // bat.reset(screenX, screenY);
    }


    if (lives == 0) {
        paused = true;


        Intent intent = new Intent(getContext(), LoseMenu.class);
        intent.putExtra("score", score);
        intent.putExtra("time", time);
        getContext().startActivity(intent);



        //level = 1;
        //restart();

    }
}

    if(ball.getRect().top < 0){
        ball.reverseYVelocity();
        ball.clearObstacleY(20);
        soundPool.play(wall, 1, 1, 0, 0, 1);

    }

    if(ball.getRect().left < 0){
        ball.reverseXVelocity();
        ball.clearObstacleX(2);
        soundPool.play(wall, 1, 1, 0, 0, 1);

    }

    if(ball.getRect().right > screenX - 10){
        ball.reverseXVelocity();
        ball.clearObstacleX(screenX - 32);
        soundPool.play(wall, 1, 1, 0, 0, 1);

    }
    if(attempt == 0){
    //paused = true;
    //restart();
        if(level == 3){

            Intent intent = new Intent(getContext(), LoseMenu.class);
            intent.putExtra("score", score);
            intent.putExtra("time",  time);
            getContext().startActivity(intent);

        }
        level +=1;
        paused = true;
        attempt = 5;
        restart();
    }



    }

    void restart(){
        ball.reset(screenX, screenY);
        bat = new Bat(screenX, screenY);


        int brickWidth = screenX / 8;
        int brickHeight = screenY / 10;

        numBricks = 0;
        numBricks2 = 0;


        setLevel();
        if(level == 1){
            score = 0;
            lives = 3;
        }

        Arrays.fill(checkBrick,2);

        buildSecondLayer = 0;
    }

    private void setLevel(){
        int brickWidth = screenX / 8;
        int brickHeight = screenY / 10;

        numBricks = 0;
        numBricks2 = 0;

        if(level == 1){
            for(int column = 0; column < 8; column++){
                for(int row = 0; row < 5; row++){
                    bricks[numBricks] = new Brick(row, column, brickWidth, brickHeight);
                    numBricks++;
                }
            }

        }
        else if(level == 2){
            for(int column = 0; column < 8; column++){
                for(int row = 0; row < 6; row++){
                    if(row % 2 != 1){

                        bricks2[numBricks2] = new Brick(row, column, brickWidth, brickHeight);
                        bricks3[numBricks2] = new Brick(row, column, brickWidth, brickHeight);
                        numBricks2++;


                    }
                    else{
                        bricks[numBricks] = new Brick(row, column, brickWidth, brickHeight);
                        numBricks++;
                    }

                }
            }
        }
        else if(level == 3){
            for(int column = 0; column < 8; column++) {
                for(int row=0; row < 5; row++) {
                    if((row+column)%2 == 0) {
                        bricks2[numBricks2] = new Brick(row, column, brickWidth, brickHeight);
                        bricks3[numBricks2] = new Brick(row, column, brickWidth, brickHeight);
                        numBricks2++;
                    }
                }
            }
        }
    }


    private void draw(){

        if(ourHolder.getSurface().isValid()){

            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 255, 255, 255));

            canvas.drawColor(Color.argb(241,5,255,255));
            canvas.drawRect(bat.getRect(), paint);

            canvas.drawRect(ball.getRect(), paint);

            paint.setColor(Color.argb(255, 249, 129, 0));

            for(int i = 0; i < numBricks; i++){
                if(bricks[i].getVisibility()){
                    canvas.drawRect(bricks[i].getRect(), paint);
                }
            }
            //paint.setColor(Color.argb(220,52,0,255));
            paint.setColor(Color.rgb(255,122,108));
            for(int i = 0; i < numBricks2; i++){
                if(bricks2[i].getVisibility()){
                    canvas.drawRect(bricks2[i].getRect(), paint);
                    //bricks2[i].setInvisible();
                }
            }

            paint.setColor(Color.rgb(255,91,67));
            for(int i = 0; i < numBricks2; i++){
                if(bricks3[i].getVisibility()){
                    canvas.drawRect(bricks3[i].getRect(), paint);

                }
            }

            paint.setColor(Color.argb(255, 255, 255, 255));

            paint.setTextSize(70);
            canvas.drawText("Score:" + score + "  Lives:" + lives + " Attempt:" + attempt + " Time:" + millisecondsToString(time), 10, 80, paint);


            ourHolder.unlockCanvasAndPost(canvas);

        }


    }
    private String millisecondsToString(int milliseconds)  {
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent){

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                paused = false;

                if(motionEvent.getX() > screenX / 2){
                    bat.setMovementState(bat.RIGHT);
                }
                else{
                    bat.setMovementState(bat.LEFT);
                }

                break;

            case MotionEvent.ACTION_UP:
                bat.setMovementState(bat.STOPPED);
                break;

        }



        return true;
    }


}

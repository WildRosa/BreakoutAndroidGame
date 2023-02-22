package com.example.breakout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Point;
import android.view.Display;

import androidx.appcompat.app.AlertDialog;

public class Game extends Activity {
    BreakoutEngine breakoutEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);


        breakoutEngine = new BreakoutEngine(this, size.x, size.y);

        setContentView(breakoutEngine);
    }

    @Override
    protected void onPause(){
        super.onPause();

        breakoutEngine.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();

        breakoutEngine.resume();
    }

}

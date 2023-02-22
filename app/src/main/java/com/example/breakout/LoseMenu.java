package com.example.breakout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoseMenu extends Activity {

    TextView menu;
    TextView restart;
    DB databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lose_menu);
        databaseHelper = new DB(getApplicationContext());
        db = databaseHelper.getReadableDatabase();



        Bundle arguments = getIntent().getExtras();
        String score = arguments.get("score").toString();
        int Score = Integer.parseInt(score);
        String time = arguments.get("time").toString();
        int Time = Integer.parseInt(time);

        ContentValues newValues = new ContentValues();

        //newValues.put(databaseHelper.COLUMN_SCORES, Score);
        //db.insert(databaseHelper.TABLE, null, newValues);

        userCursor =  db.rawQuery("select * from scoreTable", null);

        userCursor.moveToLast();
        int id = userCursor.getInt(0);


        userCursor.close();
        menu = (TextView) findViewById(R.id.menu);
        restart =(TextView) findViewById(R.id.restart);

        View.OnClickListener oclBtnMenu = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newValues.put(databaseHelper.COLUMN_SCORES, Score);
                newValues.put(databaseHelper.COLUMN_TIME, millisecondsToString(Time));
                db.update(databaseHelper.TABLE,newValues, databaseHelper.COLUMN_ID + "=" + id, null );

                Intent intent = new Intent(LoseMenu.this, MainActivity.class);
                startActivity(intent);
            }
        };

        menu.setOnClickListener(oclBtnMenu);

        View.OnClickListener oclBtnRestart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(LoseMenu.this, Game.class);
                startActivity(intent);
            }
        };

        restart.setOnClickListener(oclBtnRestart);


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
}
package com.example.breakout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.graphics.Point;
import android.view.Display;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView name;
    TextView start;
    TextView score;
    TextView enter;
    TextView backStart;
    TextView backScore;
    TextView sort;
    TextView errorNick;
    EditText enterName;
    EditText enterNick;
    AlertDialog.Builder builder;
    TextView text;

    TextView header;
    DB databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        name = (TextView) findViewById(R.id.name);
        start = (TextView) findViewById(R.id.start);
        score =(TextView) findViewById(R.id.score);
        enter = (TextView) findViewById(R.id.enter);
        backStart = (TextView) findViewById(R.id.backStart);
        backScore = (TextView) findViewById(R.id.backScore);
        sort =  (TextView) findViewById(R.id.sort);
        errorNick = (TextView) findViewById(R.id.erorText);
        enterName = (EditText) findViewById(R.id.namePlayer);
        enterNick = (EditText) findViewById(R.id.nickPlayer);

        header = findViewById(R.id.header);
        text = findViewById(R.id.textView);

        databaseHelper = new DB(getApplicationContext());



        View.OnClickListener oclBtnStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* new AlertDialog.Builder(MainActivity.this)
                        .setTitle("About Us")
                        .setMessage("Blah Blah Blah")
                        .setCancelable(true)
                        .setPositiveButton("Ok", null)
                        .setNegativeButton("NO", null)
                        .show();
*/
                start.setVisibility(View.INVISIBLE);
                score.setVisibility(View.INVISIBLE);
                enter.setVisibility(View.VISIBLE);
                backStart.setVisibility(View.VISIBLE);
                enterName.setVisibility(View.VISIBLE);
                enterNick.setVisibility(View.VISIBLE);

                //Intent intent = new Intent(MainActivity.this, Game.class);
                //startActivity(intent);
            }
        };

        start.setOnClickListener(oclBtnStart);


        View.OnClickListener oclBtnbackStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enterName.getText().clear();
                enterNick.getText().clear();
                name.setVisibility(View.VISIBLE);
                start.setVisibility(View.VISIBLE);
                score.setVisibility(View.VISIBLE);
                enter.setVisibility(View.INVISIBLE);
                backStart.setVisibility(View.INVISIBLE);
                enterName.setVisibility(View.INVISIBLE);
                enterNick.setVisibility(View.INVISIBLE);

            }
        };

        backStart.setOnClickListener(oclBtnbackStart);


        View.OnClickListener oclBtnEnter = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checkNick = true;
                String Name = enterName.getText().toString();
                String Nick = enterNick.getText().toString();

                if(!enterNick.getText().toString().isEmpty() && !enterName.getText().toString().isEmpty()){
                    db = databaseHelper.getReadableDatabase();
                    userCursor =  db.rawQuery("select * from scoreTable", null);
                    while(userCursor.moveToNext()){
                        String login = userCursor.getString(2);
                        if(login.equals(Nick)){
                            errorNick.setVisibility(View.VISIBLE);
                            checkNick = false;
                            break;
                        }
                        else{
                            checkNick = true;
                        }
                    }

                    if(checkNick == true) {

                        ContentValues newValues = new ContentValues();

                        newValues.put(databaseHelper.COLUMN_NAME, Name);
                        newValues.put(databaseHelper.COLUMN_LOGIN, Nick);
                        db.insert(databaseHelper.TABLE, null, newValues);

                        db.close();
                        userCursor.close();
                        errorNick.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(MainActivity.this, Game.class);
                        startActivity(intent);
                    }
                }



            }
        };

        enter.setOnClickListener(oclBtnEnter);



        View.OnClickListener oclBtnScore = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setVisibility(View.INVISIBLE);
                start.setVisibility(View.INVISIBLE);
                score.setVisibility(View.INVISIBLE);
                header.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
                sort.setVisibility(View.VISIBLE);
                backScore.setVisibility(View.VISIBLE);
             /*   ContentValues newValues = new ContentValues();
                newValues.put(databaseHelper.COLUMN_NAME, "Vikdda");
                newValues.put(databaseHelper.COLUMN_SCORES, 23);
                db.insert(databaseHelper.TABLE, null, newValues);*/
                //setContentView(R.layout.activity_db);
                //Intent intent = new Intent(MainActivity.this, DB.class);
               // startActivity(intent);
            }
        };

        score.setOnClickListener(oclBtnScore);



        View.OnClickListener oclBtnbackScore = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                score.setVisibility(View.VISIBLE);
                enter.setVisibility(View.INVISIBLE);
                backScore.setVisibility(View.INVISIBLE);
                sort.setVisibility(View.INVISIBLE);
                header.setVisibility(View.INVISIBLE);
                text.setVisibility(View.INVISIBLE);

            }
        };

        backScore.setOnClickListener(oclBtnbackScore);



        View.OnClickListener oclBtnSort = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start.setVisibility(View.INVISIBLE);
                score.setVisibility(View.INVISIBLE);
                backScore.setVisibility(View.VISIBLE);
                sort.setVisibility(View.VISIBLE);
                header.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
                header.setText("");
                text.setText("");
                db = databaseHelper.getReadableDatabase();
                String orderBy = "scores";
                userCursor =  db.rawQuery("select * from scoreTable", null);
                userCursor = db.query("scoreTable", null, null, null, null, null , orderBy + " DESC");
                while(userCursor.moveToNext()){
                    int id = userCursor.getInt(0);
                    String name = userCursor.getString(1);
                    String login = userCursor.getString(2);
                    int scores = userCursor.getInt(3);
                    String time = userCursor.getString(4);
                    text.append("Name: " + name + " Nickname: " + login + " Score: " + scores + " Time: " + time + "\n");
                }
                userCursor.close();
                db.close();
            }
        };

        sort.setOnClickListener(oclBtnSort);



    }





    @Override
    public void onResume() {
        super.onResume();

        // открываем подключение
        db = databaseHelper.getReadableDatabase();
        //db.delete("scoreTable", null, null);
        //получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("select * from scoreTable", null);

        while(userCursor.moveToNext()){
            int id = userCursor.getInt(0);
            String name = userCursor.getString(1);
            String login = userCursor.getString(2);
            int scores = userCursor.getInt(3);
            String time = userCursor.getString(4);
            text.append("Name: " + name + " Nickname: " + login + " Score: " + scores + " Time: " + time + "\n");
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //db.delete("scoreTable", null, null);
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }

}
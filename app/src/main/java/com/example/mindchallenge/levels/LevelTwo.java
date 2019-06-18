package com.example.mindchallenge.levels;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.mindchallenge.Database;
import com.example.mindchallenge.PreLevelStart;
import com.example.mindchallenge.R;

import java.util.Objects;
import java.util.Random;

public class LevelTwo extends AppCompatActivity implements View.OnClickListener {

    Button[][] buttons;
    int[] rows;
    int[] columns;
    int numRows = 6;
    int numColumns = 5;
    Database database;
    int databaseScore;
    int score;
    int memoryDepth;
    int rowColumnCounter;
    int respectivelyClickedButton;
    static Drawable buttonDrawable;
    boolean assignedDrawable;
    boolean clicksAvailable;

    int lastLevel;
    String from;


    private Handler changeButtonColorHandler = new Handler();

    private Runnable changeButtonColorRunnable = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {
            if (rowColumnCounter >= memoryDepth) {
                stopRepeating();
            } else {
                if (!assignedDrawable) {
                    buttonDrawable = buttons[rows[rowColumnCounter]][columns[rowColumnCounter]].getBackground();
                    assignedDrawable = true;
                }
                buttons[rows[rowColumnCounter]][columns[rowColumnCounter]].setBackground(getDrawable(R.drawable.level_wrong_button));
                buttons[rows[rowColumnCounter]][columns[rowColumnCounter]].setScaleX(0.9f);
                buttons[rows[rowColumnCounter]][columns[rowColumnCounter]].setScaleY(0.9f);
                rowColumnCounter++;
                changeButtonColorHandler.postDelayed(this, 2000);
            }

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void stopRepeating() {
        changeButtonColorHandler.removeCallbacks(changeButtonColorRunnable);
        for (int i = memoryDepth - 1; i >= 0; i--) {
            buttons[rows[i]][columns[i]].setBackground(buttonDrawable);
            buttons[rows[i]][columns[i]].setScaleX(1);
            buttons[rows[i]][columns[i]].setScaleY(1);
        }
        setOnClickListenerToAllButtons();
    }

    private void startRepeating() {
        changeButtonColorRunnable.run();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_two);
        database = new Database(getApplicationContext());
        databaseScore = database.getScoreAtLevel(2);
        from = Objects.requireNonNull(getIntent().getExtras()).getString("from");
        score = 0;
        assignedDrawable = false;
        clicksAvailable = true;
        lastLevel = database.getLastLevelUnlocked();

        assert from != null;
        switch (from) {
            case "main":
                memoryDepth = 4;
                break;
            case "1":
                memoryDepth = 5;
                break;
            case "2":
                memoryDepth = 6;
                break;
            default:
                database.close();
        finish();
                break;
        }
        rows = new int[memoryDepth];
        columns = new int[memoryDepth];
        rowColumnCounter = 0;
        respectivelyClickedButton = 0;

        buttons = new Button[numRows][numColumns];


        for (int i = 0; i < memoryDepth; i++) {
            rows[i] = new Random().nextInt(numRows);
            columns[i] = new Random().nextInt(numColumns);
            for (int j = 0; j < i; j++) {
                while (rows[j] == rows[i] && columns[j] == columns[i]) {
                    rows[i] = new Random().nextInt(numRows);
                    columns[i] = new Random().nextInt(numColumns);
                }
            }
        }
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                String buttonId = "levelTwoButton" + (i + 1) + "-" + (j + 1);
                int resId = getResources().getIdentifier(buttonId, "id", getPackageName());
                buttons[i][j] = findViewById(resId);
            }
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startRepeating();
            }
        }, 1000);

    }

    private void setOnClickListenerToAllButtons() {

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                buttons[i][j].setOnClickListener(this);
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (clicksAvailable) {
            if (v.getId() == buttons[rows[respectivelyClickedButton]][columns[respectivelyClickedButton]].getId()) {


                score += 33 / memoryDepth;
                if (databaseScore < score) {
                    database.updateScore(score, 2);
                }
                buttons[rows[respectivelyClickedButton]][columns[respectivelyClickedButton]].setBackground(getDrawable(R.drawable.level_correct_button));
                buttons[rows[respectivelyClickedButton]][columns[respectivelyClickedButton]].setScaleX(0.9f);
                buttons[rows[respectivelyClickedButton]][columns[respectivelyClickedButton]].setScaleY(0.9f);

                if (respectivelyClickedButton == memoryDepth - 1) {
                    clicksAvailable = false;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            switch (from) {
                                case "1":
                                    score = 66;
                                    if (databaseScore < score) {
                                        database.updateScore(score, 2);
                                    }
                                    Intent intent1 = new Intent(getApplicationContext(), LevelTwo.class);
                                    intent1.putExtra("from", "2");
                                    startActivity(intent1);
                                    database.close();
        finish();
                                    break;
                                case "2":
                                    score = 100;
                                    if (databaseScore < score) {
                                        database.updateScore(score, 2);
                                    }
                                    if (lastLevel < 3) {
                                        database.unlockLevel(3);
                                    }
                                    Intent intent = new Intent(getApplicationContext(), PreLevelStart.class);
                                    intent.putExtra("from", "2");
                                    intent.putExtra("level", 2);
                                    intent.putExtra("status", getResources().getString(R.string.congratulations));
                                    intent.putExtra("score", score);
                                    intent.putExtra("lastLevel", database.getLastLevelUnlocked());
                                    startActivity(intent);
                                    database.close();
        finish();
                                    break;
                                case "main":
                                    score = 33;
                                    if (databaseScore < score) {
                                        database.updateScore(score, 2);
                                    }
                                    Intent intent2 = new Intent(getApplicationContext(), LevelTwo.class);
                                    intent2.putExtra("from", "1");
                                    startActivity(intent2);
                                    database.close();
        finish();
                                default:
                                    database.close();
        finish();
                                    break;
                            }
                        }
                    }, 1000);
                }
                respectivelyClickedButton++;

            } else {
                clicksAvailable = false;
                buttons[rows[respectivelyClickedButton]][columns[respectivelyClickedButton]].setBackground(getDrawable(R.drawable.level_wrong_button));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), PreLevelStart.class);
                        intent.putExtra("from", "2");
                        intent.putExtra("level", 2);
                        intent.putExtra("status", getResources().getString(R.string.wrong_button));
                        intent.putExtra("score", score);
                        intent.putExtra("lastLevel", database.getLastLevelUnlocked());
                        startActivity(intent);
                        database.close();
        finish();
                    }

                }, 2000);
            }

        }
    }
}
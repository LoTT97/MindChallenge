package com.example.mindchallenge.levels;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mindchallenge.Database;
import com.example.mindchallenge.FadeOutScore;
import com.example.mindchallenge.PreLevelStart;
import com.example.mindchallenge.R;

import java.util.Random;

public class LevelOne extends AppCompatActivity implements View.OnClickListener {
    Button[][] buttons;
    int row;
    int column;
    int numRows = 16;
    int numColumns = 9;
    Database database;
    int databaseScore;
    int score;
    int FinishTime;
    int tickingTime;
    int counterForTicking;
    boolean clicked;
    boolean activityPaused;
    boolean finished;
    FadeOutScore fadeButton;
    LinearLayout levelOneLayout;
    CountDownTimer counterTimer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_one);
        database = new Database(getApplicationContext());
        databaseScore = database.getScoreAtLevel(1);
        activityPaused = false;
        clicked = false;
        finished = false;
        counterForTicking = 10;
        score = 0;
        fadeButton = new FadeOutScore(getApplicationContext());
        levelOneLayout = findViewById(R.id.level_one_layout);
        buttons = new Button[numRows][numColumns];
        row = new Random().nextInt(numRows) + 1;
        column = new Random().nextInt(numColumns) + 1;

        int randomNumber = new Random().nextInt(99);
        int differentNumber = randomNumber;
        while (differentNumber == randomNumber) {
            differentNumber = new Random().nextInt(99);
        }


        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                String buttonId = "levelOneButton" + (i + 1) + "-" + (j + 1);
                int resId = getResources().getIdentifier(buttonId, "id", getPackageName());
                buttons[i][j] = findViewById(resId);
                buttons[i][j].setOnClickListener(this);
                if (!(j == (column - 1) && i == (row - 1))) {
                    buttons[i][j].setText(String.valueOf(randomNumber));
                } else {
                    buttons[i][j].setText(String.valueOf(differentNumber));
                }
            }
        }
        FinishTime = 10;
        tickingTime = FinishTime * 10;
        int countDownInterval = 100;
        counterTimer = new CountDownTimer(FinishTime * 1000, countDownInterval) {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onFinish() {
                //finish your activity here
                finished = true;
                if (!clicked) {
                    buttons[row - 1][column - 1].setBackground(getDrawable(R.drawable.level_wrong_button));
                    ScaleAnimation buttonAnim = new ScaleAnimation(1, 1.2f, 1, 1.2f);
                    buttonAnim.setDuration(3200);
                    buttons[row - 1][column - 1].startAnimation(buttonAnim);
                    if (!activityPaused) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(LevelOne.this, PreLevelStart.class);
                                intent.putExtra("from", "1");
                                intent.putExtra("level", 1);
                                intent.putExtra("status", getResources().getString(R.string.not_in_time));
                                intent.putExtra("score", score);
                                intent.putExtra("lastLevel", database.getLastLevelUnlocked());
                                startActivity(intent);
                                finish();
                            }
                        }, 3000);
                    }
                }


            }

            public void onTick(long millisUntilFinished) {
//                called every 1 sec coz countDownInterval = 1000 (1 sec)
                tickingTime--;
                if (counterForTicking >= 10) {

                    if (fadeButton.getParent() != null) {
                        ((ViewGroup) fadeButton.getParent()).removeView(fadeButton);
                    }
                    fadeButton.setText(String.format("Time left: %s", String.valueOf(millisUntilFinished / 1000)));
                    levelOneLayout.addView(fadeButton, 0);
                    counterForTicking = 0;
                }
                counterForTicking++;

            }
        };
        counterTimer.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        activityPaused = true;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        counterTimer.cancel();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (!clicked && !finished) {
            clicked = true;
            score = 0;
            final Intent intent = new Intent(LevelOne.this, PreLevelStart.class);
            intent.putExtra("from", "1");
            intent.putExtra("level", 1);


            String buttonId = "levelOneButton" + row + "-" + column;

            int resId = getResources().getIdentifier(buttonId, "id", getPackageName());
            if (v.getId() == resId) {
                score = 10 * tickingTime / FinishTime;
                intent.putExtra("score", score);
                if (score > 50) {
                    if (!(database.getLastLevelUnlocked() > 1)) {
                        database.unlockLevel(2);
                    }
                    intent.putExtra("status", getResources().getString(R.string.congratulations));
                } else {
                    intent.putExtra("status", getResources().getString(R.string.correct_button_low_score));
                }
                intent.putExtra("lastLevel", database.getLastLevelUnlocked());
                if (databaseScore < score) {
                    database.updateScore(score, 1);
                }

                counterTimer.cancel();

                startActivity(intent);
                finish();
            } else {

                buttons[row - 1][column - 1].setBackground(getDrawable(R.drawable.level_wrong_button));
                ScaleAnimation buttonAnim = new ScaleAnimation(1, 1.2f, 1, 1.2f);
                buttonAnim.setDuration(2200);
                buttons[row - 1][column - 1].startAnimation(buttonAnim);
                if (!activityPaused) {
                    counterTimer.cancel();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            intent.putExtra("score", score);
                            intent.putExtra("status", getResources().getString(R.string.wrong_button));
                            intent.putExtra("lastLevel", database.getLastLevelUnlocked());
                            startActivity(intent);
                            finish();
                        }
                    }, 2000);
                }


            }
        }
    }


}

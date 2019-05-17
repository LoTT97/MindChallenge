package com.example.mindchallenge.levels;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.example.mindchallenge.Database;
import com.example.mindchallenge.PreLevelStart;
import com.example.mindchallenge.R;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class LevelFour extends AppCompatActivity implements View.OnClickListener {

    Database database;
    Button[] buttons;
    TextView statementTextView;
    int correctButton;

    int[] randomResources;
    int statementDifficulty;
    int tapDontTap;
    int round;
    int score;
    boolean firstPart;
    boolean clicked;

    TextView timerTextView = null;
    int secondsUntilGameOver;
    boolean timerFinished;
    CountDownTimer downTimer;

    final static int[] buttonResourceIds = {R.drawable.button_black, R.drawable.button_blue, R.drawable.button_pink, R.drawable.button_purple, R.drawable.button_red, R.drawable.button_yellow, R.drawable.button_green};

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_four);

        database = new Database(getApplicationContext());
        buttons = new Button[3];
        randomResources = new int[3];
        firstPart = Objects.requireNonNull(getIntent().getExtras()).getBoolean("firstPart");
        score = Objects.requireNonNull(getIntent().getExtras()).getInt("score");

        clicked = false;
        timerFinished = false;


        statementTextView = findViewById(R.id.levelFourTextView);
        buttons[0] = findViewById(R.id.levelFourButton1);
        buttons[1] = findViewById(R.id.levelFourButton2);
        buttons[2] = findViewById(R.id.levelFourButton3);

        buttons[0].setOnClickListener(this);
        buttons[1].setOnClickListener(this);
        buttons[2].setOnClickListener(this);

        assignRandomValues(randomResources, buttonResourceIds.length);

        assignColors(randomResources);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBackground(getDrawable(randomResources[i]));
        }

        if (firstPart) {


            statementDifficulty = 1;
            tapDontTap = 0;
            round = Objects.requireNonNull(getIntent().getExtras()).getInt("round");
            if (round == 1) {
                secondsUntilGameOver = 30;
            } else {
                secondsUntilGameOver = getIntent().getExtras().getInt("seconds");
            }
            if (round == 7) {
                firstPart = false;
                round = 1;

            }
            round++;
        } else {
            secondsUntilGameOver = getIntent().getExtras().getInt("seconds");
            statementDifficulty = 2;
            round = Objects.requireNonNull(getIntent().getExtras()).getInt("round");
            round++;
        }


        correctButton = new Random().nextInt(3);
        int randomNotCorrectButtonColor = correctButton;
        while (randomNotCorrectButtonColor == correctButton) {
            randomNotCorrectButtonColor = new Random().nextInt(3);
        }

        if (statementDifficulty == 1) {
            statementTextView.setText(Html.fromHtml(String.format("Tap the <font color=\"%s\">%s</font> button", getColorFromResource(randomResources[randomNotCorrectButtonColor], true), getColorFromResource(randomResources[correctButton], false))));
        } else if (statementDifficulty == 2) {
            tapDontTap = new Random().nextInt(3);
            if (tapDontTap == 0) {
                statementTextView.setText(Html.fromHtml(String.format("Tap the <font color=\"%s\">%s</font> button", getColorFromResource(randomResources[randomNotCorrectButtonColor], true), getColorFromResource(randomResources[correctButton], false))));

            } else if (tapDontTap == 1 || tapDontTap == 2) {
                statementTextView.setText(Html.fromHtml(String.format("Don't tap the <font color=\"%s\">%s</font> button", getColorFromResource(randomResources[randomNotCorrectButtonColor], true), getColorFromResource(randomResources[correctButton], false))));
            }
        } else {
            downTimer.cancel();
            finish();
        }

        //timer
        timerTextView = findViewById(R.id.levelFourTimerTextView);
        startTimer(timerTextView, secondsUntilGameOver);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        downTimer.cancel();
        finish();
    }

    private void startPreLevel(String status, int s, int lastLevel) {
        downTimer.cancel();

        Intent intent = new Intent(getApplicationContext(), PreLevelStart.class);
        intent.putExtra("from", "4");
        intent.putExtra("level", 4);
        intent.putExtra("status", status);
        intent.putExtra("score", s);
        intent.putExtra("lastLevel", lastLevel);
        startActivity(intent);
        finish();
    }

    private void startLevelFour(int r, boolean f, int s) {
        downTimer.cancel();

        Intent intent = new Intent(getApplicationContext(), LevelFour.class);
        intent.putExtra("round", r);
        intent.putExtra("firstPart", f);
        intent.putExtra("score", s);
        intent.putExtra("seconds", secondsUntilGameOver);
        startActivity(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (!clicked) {
            clicked = true;
            if (!timerFinished) {
                if (v.getId() == buttons[correctButton].getId()) {

                    if (tapDontTap == 0) {
                        if (!firstPart && round >= 7) {
                            if (database.getLastLevelUnlocked() < 5) {
                                database.unlockLevel(5);
                            }
                            score = 100;
                            startPreLevel(getResources().getString(R.string.congratulations), score, database.getLastLevelUnlocked());

                        } else {
                            score += 12;
                            startLevelFour(round, firstPart, score);
                        }

                    } else {
                        ScaleAnimation buttonAnim = new ScaleAnimation(1, 1.2f, 1, 1.2f);
                        buttonAnim.setDuration(3200);
                        buttons[correctButton].startAnimation(buttonAnim);
                        new Handler().postDelayed(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void run() {
                                startPreLevel(getResources().getString(R.string.wrong_button), score, database.getLastLevelUnlocked());
                            }
                        }, 2000);

                    }
                } else {

                    if (tapDontTap == 0) {
                        ScaleAnimation buttonAnim = new ScaleAnimation(1, 1.2f, 1, 1.2f);
                        buttonAnim.setDuration(3200);
                        buttons[correctButton].startAnimation(buttonAnim);
                        new Handler().postDelayed(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void run() {
                                startPreLevel(getResources().getString(R.string.wrong_button), score, database.getLastLevelUnlocked());
                            }
                        }, 2000);
                    } else {
                        if (!firstPart && round >= 11) {
                            if (database.getLastLevelUnlocked() < 5) {
                                database.unlockLevel(5);
                            }
                            score = 100;
                            startPreLevel(getResources().getString(R.string.congratulations), score, database.getLastLevelUnlocked());


                        } else {
                            score += 12;
                            startLevelFour(round, firstPart, score);
                        }
                    }
                }

            }
        }
    }

    private String getColorFromResource(int resourceID, boolean asHex) {
        if (resourceID == buttonResourceIds[0]) {
            return asHex ? "#000000" : "BLACK";
        } else if (resourceID == buttonResourceIds[1]) {
            return asHex ? "#0000ff" : "BLUE";
        } else if (resourceID == buttonResourceIds[2]) {
            return asHex ? "#ff00cc" : "PINK";
        } else if (resourceID == buttonResourceIds[3]) {
            return asHex ? "#800080" : "PURPLE";
        } else if (resourceID == buttonResourceIds[4]) {
            return asHex ? "#ff0000" : "RED";
        } else if (resourceID == buttonResourceIds[5]) {
            return asHex ? "#ffff00" : "YELLOW";
        } else if (resourceID == buttonResourceIds[6]) {
            return asHex ? "#00ff00" : "GREEN";
        }
        return null;
    }

    private void assignRandomValues(int[] array, int randomBound) {
        boolean isSame;
        array[0] = new Random().nextInt(randomBound);

        for (int i = 1; i < array.length; i++) {

            isSame = true;
            while (isSame) {
                int j = 0;
                array[i] = new Random().nextInt(randomBound);

                isSame = false;
                while (j < i) {
                    if (array[j] == array[i]) {
                        isSame = true;
                        break;
                    }
                    j++;
                }
            }
        }
    }


    private void assignColors(int[] to) {
        assert to.length <= LevelFour.buttonResourceIds.length;

        for (int i = 0; i < to.length; i++) {
            to[i] = LevelFour.buttonResourceIds[to[i]];
        }
    }

    public void startTimer(final TextView textView, final int seconds) {

        downTimer = new CountDownTimer(1000 * seconds, 1000) {

            public void onTick(long millisUntilFinished) {

                secondsUntilGameOver--;
                if (!clicked) {
                    String v = String.format(Locale.ENGLISH, "%02d", millisUntilFinished / 60000);
                    int va = (int) ((millisUntilFinished % 60000) / 1000);
                    textView.setText(v + ":" + String.format("%02d", va));
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onFinish() {
                timerFinished = true;
                if (!clicked) {
                    ScaleAnimation buttonAnim = new ScaleAnimation(1, 1.2f, 1, 1.2f);
                    buttonAnim.setDuration(3200);
                    buttons[correctButton].startAnimation(buttonAnim);
                    new Handler().postDelayed(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {

                            startPreLevel(getResources().getString(R.string.not_in_time), Objects.requireNonNull(getIntent().getExtras()).getInt("score"), database.getLastLevelUnlocked());
                        }
                    }, 2000);
                }
            }
        };
        downTimer.start();
    }

}

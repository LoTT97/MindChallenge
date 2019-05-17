package com.example.mindchallenge.levels;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableRow;

import com.example.mindchallenge.Database;
import com.example.mindchallenge.PreLevelStart;
import com.example.mindchallenge.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class LevelFive extends AppCompatActivity implements View.OnClickListener {

    Database database;
    List<Button> buttons;
    List<Button> duplicateButtons;
    ProgressBar progressBar;
    int buttonNumber = 1;
    int buttonClickedNumber;
    int buttonNumberDifference;

    int numRows = 6;
    int numColumns = 5;
    int round;
    int score;
    int maxBar;

    boolean progressBarFinished;

    Thread thread;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_two);

        buttonNumberDifference = Objects.requireNonNull(getIntent().getExtras()).getInt("difference");
        round = Objects.requireNonNull(getIntent().getExtras()).getInt("round");
        maxBar = Objects.requireNonNull(getIntent().getExtras()).getInt("maxBar");


        progressBar = findViewById(R.id.levelFiveProgressBar);
        progressBar.setMax(maxBar);
        progressBarFinished = false;
        setProgressValue(0);

        database = new Database(getApplicationContext());
        buttons = new ArrayList<>();
        duplicateButtons = new ArrayList<>();
        score = 0;
        buttonClickedNumber = 1;


        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.height = 214;
        params.width = 175;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                Button b;
                String buttonId = "levelTwoButton" + (i + 1) + "-" + (j + 1);
                int resId = getResources().getIdentifier(buttonId, "id", getPackageName());
                b = findViewById(resId);

                if (j == 4) {
                    b.setVisibility(View.GONE);
                } else {
                    b.setOnClickListener(this);
                    b.setBackground(getDrawable(R.drawable.level_five_button));
                    b.setLayoutParams(params);
                    buttons.add(b);
                    duplicateButtons.add(b);
                }
            }
        }


        int size = buttons.size();
        for (int i = 0; i < size; i++) {
            int randomButton = new Random().nextInt(duplicateButtons.size());
            duplicateButtons.get(randomButton).setText(String.valueOf(buttonNumber));
            duplicateButtons.remove(randomButton);
            buttonNumber = buttonNumber + buttonNumberDifference;
        }
    }

    @Override
    public void onClick(View v) {

        if (!progressBarFinished) {
            for (Button b : buttons) {
                if (b.getText() == String.valueOf(buttonClickedNumber)) {
                    if (v.getId() == b.getId()) {
                        b.setText("");
                        b.setEnabled(false);
                        if (buttonClickedNumber == buttonNumberDifference * 23 + 1) { // (6 rows * 4 columns - 1) * difference + 1 (because we start with 1) , it means it is the last number
                            if (maxBar > 0) {
                                score += ((100 * (maxBar - progressBar.getProgress())) / maxBar) * 3;
                                if (score > 100) {
                                    score = 100;
                                }
                            } else {
                                score = 0;
                            }
                            if (round == 3) {
                                if (maxBar > 50) {
                                    if (database.getLastLevelUnlocked() < 6) {
                                        database.unlockLevel(6);
                                    }
                                }
                                if (database.getScoreAtLevel(5) < score) {
                                    database.updateScore(score, 5);
                                }
                                startPreLevel(getResources().getString(R.string.congratulations), score, database.getLastLevelUnlocked());
                            } else {
                                maxBar += 500;
                                round++;

                                startLevelFive(round, buttonNumberDifference + 1, maxBar);
                            }
                        }
                        buttonClickedNumber = buttonClickedNumber + buttonNumberDifference;
                    } else {
                        startPreLevel(getResources().getString(R.string.wrong_button), score, database.getLastLevelUnlocked());
                    }

                    break;
                }
            }
        }
    }

    private void startLevelFive(int r, int dif, int maxB) {
        thread.interrupt();
        progressBarFinished = true;
        Intent intent = new Intent(getApplicationContext(), LevelFive.class);
        intent.putExtra("round", r);
        intent.putExtra("difference", dif);
        intent.putExtra("maxBar", maxB);
        startActivity(intent);
        finish();

    }

    private void startPreLevel(String stat, int s, int lastLevel) {
        thread.interrupt();
        progressBarFinished = true;
        Intent intent = new Intent(getApplicationContext(), PreLevelStart.class);
        intent.putExtra("from", "5");
        intent.putExtra("level", 5);
        intent.putExtra("status", stat);
        intent.putExtra("score", s);
        intent.putExtra("lastLevel", lastLevel);
        startActivity(intent);
        finish();
    }

    private void setProgressValue(final int progress) {

        if (!progressBarFinished) {
            // set the progress
            progressBar.setProgress(progress);

            // thread is used to change the progress value
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setProgressValue(progress + 1);
                }
            });
            if (progress >= maxBar) {
                thread.interrupt();
                progressBarFinished = true;
                startPreLevel(getResources().getString(R.string.not_in_time), score, database.getLastLevelUnlocked());
            }else{
                thread.start();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        thread.interrupt();
        finish();
    }
}

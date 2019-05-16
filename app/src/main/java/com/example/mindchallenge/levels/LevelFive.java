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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_two);

        progressBar = findViewById(R.id.levelFiveProgressBar);
        progressBar.setMax(3000);
        setProgressValue(0);

        database = new Database(getApplicationContext());
        buttons = new ArrayList<>();
        duplicateButtons = new ArrayList<>();
        score = 0;
        buttonClickedNumber = 1;
        buttonNumberDifference = Objects.requireNonNull(getIntent().getExtras()).getInt("difference");
        round = Objects.requireNonNull(getIntent().getExtras()).getInt("round");


        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.height = 225;
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

        for (Button b : buttons) {
            if (b.getText() == String.valueOf(buttonClickedNumber)) {
                if (v.getId() == b.getId()) {
                    b.setText("");
                    if (buttonClickedNumber == buttonNumberDifference * 29 + 1) {
                        if (round == 3) {
                            startPreLevel(getResources().getString(R.string.congratulations), score, database.getLastLevelUnlocked());
                        } else {
                            round++;
                            startLevelFive(round, buttonNumberDifference + 1);
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

    private void startLevelFive(int r, int dif) {

        Intent intent = new Intent(getApplicationContext(), LevelFive.class);
        intent.putExtra("round", r);
        intent.putExtra("difference", dif);
        startActivity(intent);
        finish();

    }

    private void startPreLevel(String stat, int s, int lastLevel) {

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

        // set the progress
        progressBar.setProgress(progress);
        // thread is used to change the progress value
        Thread thread = new Thread(new Runnable() {
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
        thread.start();
    }

}

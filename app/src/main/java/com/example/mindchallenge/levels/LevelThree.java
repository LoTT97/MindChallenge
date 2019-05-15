package com.example.mindchallenge.levels;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
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

public class LevelThree extends AppCompatActivity implements View.OnClickListener {


    Database database;
    boolean activityPaused;
    TextView equationTextView;

    String equation;
    final static String[] signs = {"+", "-"};
    Button[] buttons;

    int databaseScore, lastLevel;
    int numbers;
    int correctButton;
    int score;
    int upperLimitDifficulty;
    double differenceFromTheResultInPercentage;

    String from;
    boolean clicked;


    TextView timerTextView = null;
    int secondsUntilGameOver;
    boolean timerFinished;
    CountDownTimer downTimer;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_three);

        database = new Database(getApplicationContext());
        databaseScore = database.getScoreAtLevel(3);
        activityPaused = false;
        timerFinished = false;
        equation = "";
        buttons = new Button[4];
        score = 0;
        clicked = false;
        from = Objects.requireNonNull(getIntent().getExtras()).getString("from");

        assert from != null;
        switch (from) {
            case "main":
                secondsUntilGameOver = 60;
                numbers = 2;
                upperLimitDifficulty = 100;
                differenceFromTheResultInPercentage = 30;
                break;
            case "1":
                secondsUntilGameOver = getIntent().getExtras().getInt("seconds");
                numbers = 3;
                upperLimitDifficulty = 200;
                differenceFromTheResultInPercentage = 20;
                break;
            case "2":
                secondsUntilGameOver = getIntent().getExtras().getInt("seconds");
                numbers = 4;
                upperLimitDifficulty = 200;
                differenceFromTheResultInPercentage = 20;
                break;
            case "3":
                secondsUntilGameOver = getIntent().getExtras().getInt("seconds");
                numbers = 5;
                upperLimitDifficulty = 200;
                differenceFromTheResultInPercentage = 10;
                break;
            case "4":
                secondsUntilGameOver = getIntent().getExtras().getInt("seconds");
                numbers = 6;
                upperLimitDifficulty = 500;
                differenceFromTheResultInPercentage = 5;
                break;
        }

        equationTextView = findViewById(R.id.levelFourTextView);

        buttons[0] = findViewById(R.id.levelThreeButton1_1);
        buttons[1] = findViewById(R.id.levelThreeButton1_2);
        buttons[2] = findViewById(R.id.levelThreeButton2_1);
        buttons[3] = findViewById(R.id.levelThreeButton2_2);

        for (int i = 1; i < numbers; i++) {
            equation += new Random().nextInt(upperLimitDifficulty) + " " + signs[getRandomSign()] + " ";
        }

        equation += new Random().nextInt(upperLimitDifficulty);

        if (numbers == 6) {
            equation = "";
            equation += new Random().nextInt(100) + 20 + "*" + new Random().nextInt(15) + signs[getRandomSign()] + new Random().nextInt(upperLimitDifficulty) + 1;
        }

        equationTextView.setText(equation);
        int result = (int) eval(equation);
        correctButton = new Random().nextInt(4);

        buttons[0].setText(String.valueOf((int) eval(result + " " + signs[getRandomSign()] + " " + getRandomDifference(result))));
        buttons[1].setText(String.valueOf((int) eval(result + " " + signs[getRandomSign()] + " " + getRandomDifference(result))));
        buttons[2].setText(String.valueOf((int) eval(result + " " + signs[getRandomSign()] + " " + getRandomDifference(result))));
        buttons[3].setText(String.valueOf((int) eval(result + " " + signs[getRandomSign()] + " " + getRandomDifference(result))));

        buttons[correctButton].setText(String.valueOf(result));

        buttons[0].setOnClickListener(this);
        buttons[1].setOnClickListener(this);
        buttons[2].setOnClickListener(this);
        buttons[3].setOnClickListener(this);


        //timer
        timerTextView = findViewById(R.id.levelThreeTimerTextView);
        startTimer(timerTextView, secondsUntilGameOver);
    }


    @Override
    protected void onPause() {
        super.onPause();
        activityPaused = true;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        if (!clicked && !timerFinished) {
            clicked = true;
            if (v.getId() == buttons[correctButton].getId()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (from) {
                            case "1":
                                score = 40;
                                if (databaseScore < score) {
                                    database.updateScore(score, 3);
                                }
                                startLevelThree("2",secondsUntilGameOver,score);
                                break;
                            case "2":
                                score = 60;
                                if (databaseScore < score) {
                                    database.updateScore(score, 3);
                                }
                                startLevelThree("3",secondsUntilGameOver,score);
                                break;
                            case "3":
                                score = 80;
                                if (databaseScore < score) {
                                    database.updateScore(score, 3);
                                }
                                startLevelThree("4",secondsUntilGameOver,score);
                                break;
                            case "4":
                                score = 100;
                                if (databaseScore < score) {
                                    database.updateScore(score, 3);
                                }
                                if (lastLevel < 4) {
                                    database.unlockLevel(4);
                                }
                                startPreLevel(getResources().getString(R.string.congratulations),score,database.getLastLevelUnlocked());
                                break;
                            case "main":
                                score = 20;
                                if (databaseScore < score) {
                                    database.updateScore(score, 3);
                                }
                                startLevelThree("1",secondsUntilGameOver,score);
                                break;
                        }
                    }
                }, 500);
            } else {
                buttons[correctButton].setBackgroundColor(Color.YELLOW);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startPreLevel(getResources().getString(R.string.wrong_button),Objects.requireNonNull(getIntent().getExtras()).getInt("score"),database.getLastLevelUnlocked());

                    }
                }, 2000);

            }
        }
    }

    public void startTimer(final TextView textView, final int seconds) {

         downTimer = new CountDownTimer(1000 * seconds, 1000) {

            public void onTick(long millisUntilFinished) {

                secondsUntilGameOver --;
                if (!clicked) {
                    String v = String.format(Locale.ENGLISH, "%02d", millisUntilFinished / 60000);
                    int va = (int) ((millisUntilFinished % 60000) / 1000);
                    textView.setText(v + ":" + String.format("%02d", va));
                }
            }

            public void onFinish() {
                timerFinished = true;
                if (!clicked) {
                    buttons[correctButton].setBackgroundColor(Color.GREEN);
                    ScaleAnimation buttonAnim = new ScaleAnimation(1, 1.2f, 1, 1.2f);
                    buttonAnim.setDuration(3200);
                    buttons[correctButton].startAnimation(buttonAnim);

                    new Handler().postDelayed(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {

                            startPreLevel(getResources().getString(R.string.not_in_time),Objects.requireNonNull(getIntent().getExtras()).getInt("score"),database.getLastLevelUnlocked());

                        }
                    }, 2000);
                }
            }
        };
        downTimer.start();
    }

    private void startPreLevel(String status, int s, int lastLevel) {
        downTimer.cancel();
        downTimer = null;

        Intent intent = new Intent(getApplicationContext(), PreLevelStart.class);
        intent.putExtra("from", "3");
        intent.putExtra("level", 3);
        intent.putExtra("status", status);
        intent.putExtra("score", s);
        intent.putExtra("lastLevel", lastLevel);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        downTimer.cancel();
        downTimer = null;
    }

    private void startLevelThree(String from, int seconds, int score) {
        downTimer.cancel();
        downTimer = null;

        Intent intent = new Intent(getApplicationContext(), LevelThree.class);
        intent.putExtra("from", from);
        intent.putExtra("seconds", seconds);
        intent.putExtra("score", score);
        startActivity(intent);
        finish();
    }

    private int getRandomSign() {
        return new Random().nextInt(2);
    }

    @NonNull
    private String getRandomDifference(int result) {
        int a = new Random().nextInt((int) Math.abs((differenceFromTheResultInPercentage / 100) * result)) + 1;
        return String.valueOf(a);
    }


    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }


}

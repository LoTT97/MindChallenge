package com.example.mindchallenge;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mindchallenge.levels.LevelFive;
import com.example.mindchallenge.levels.LevelFour;
import com.example.mindchallenge.levels.LevelOne;
import com.example.mindchallenge.levels.LevelThree;
import com.example.mindchallenge.levels.LevelTwo;

import java.util.Objects;

public class PreLevelStart extends AppCompatActivity {

    Database database;
    String from;
    String status;

    Button continueButton;
    ImageButton homeButton;
    ImageButton replayButton;
    ImageButton nextLevelButton;
    TextView levelTextView;
    TextView descriptionTextView;
    TextView tapToContinueTextView;
    TextView currentScoreTextView;
    LinearLayout linearLayout;

    int level;
    int score;
    boolean clickable;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pre_level_start);
        database = new Database(getApplicationContext());
        from = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getString("from"));
        level = Objects.requireNonNull(getIntent().getExtras()).getInt("level");
        continueButton = findViewById(R.id.preLevelTapToPlayButton);
        clickable = true;

        levelTextView = findViewById(R.id.preLevelTextView);
        tapToContinueTextView = findViewById(R.id.preLevelTapToContinueTextView);
        descriptionTextView = findViewById(R.id.preLevelDescriptionTextView);
        currentScoreTextView = findViewById(R.id.preLevelCurrentScoreTextView);
        homeButton = findViewById(R.id.preLevelHomeButton);
        replayButton = findViewById(R.id.preLevelReplayButton);
        nextLevelButton = findViewById(R.id.preLevelNextLevelButton);
        linearLayout = findViewById(R.id.preLevelButtonsLinearLayout);

        switch(level){
            case 1:
                descriptionTextView.setText(getResources().getString(R.string.level_one_description));
                break;
            case 2:
                descriptionTextView.setText(getResources().getString(R.string.level_two_description));
                break;
            case 3:
                descriptionTextView.setText(getResources().getString(R.string.level_three_description));
                break;
            case 4:
                descriptionTextView.setText(getResources().getString(R.string.level_four_description));
                break;
            case 5:
                descriptionTextView.setText(getResources().getString(R.string.level_five_description));
                break;
            default:
                finish();
                break;
        }
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickable) {
                    clickable = false;
                    Intent intent;
                    switch (level) {
                        case 1:
                            descriptionTextView.setText(getResources().getString(R.string.level_one_description));
                            intent = new Intent(PreLevelStart.this, LevelOne.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 2:
                            descriptionTextView.setText(getResources().getString(R.string.level_two_description));
                            intent = new Intent(PreLevelStart.this, LevelTwo.class);
                            intent.putExtra("from", "main");
                            startActivity(intent);
                            finish();
                            break;
                        case 3:
                            descriptionTextView.setText(getResources().getString(R.string.level_three_description));
                            intent = new Intent(PreLevelStart.this, LevelThree.class);
                            intent.putExtra("from", "main");
                            startActivity(intent);
                            finish();
                            break;
                        case 4:
                            descriptionTextView.setText(getResources().getString(R.string.level_four_description));
                            intent = new Intent(PreLevelStart.this, LevelFour.class);
                            intent.putExtra("round", 1);
                            intent.putExtra("firstPart", true);
                            intent.putExtra("score", 0);
                            startActivity(intent);
                            finish();
                            break;
                        case 5:
                            descriptionTextView.setText(getResources().getString(R.string.level_five_description));
                            intent = new Intent(PreLevelStart.this, LevelFive.class);
                            intent.putExtra("round", 1);
                            intent.putExtra("difference", 2);
                            intent.putExtra("maxBar",3500);
                            startActivity(intent);
                            finish();
                            break;
                        default:
                            finish();
                            break;
                    }
                }
            }
        });


        if (from.equals("main")) {
            levelTextView.setText(String.format(getResources().getString(R.string.level), String.valueOf(level)));
            currentScoreTextView.setVisibility(View.INVISIBLE);
            linearLayout.setVisibility(View.INVISIBLE);
            homeButton.setVisibility(View.INVISIBLE);
            replayButton.setVisibility(View.INVISIBLE);
            nextLevelButton.setVisibility(View.INVISIBLE);
            homeButton.setEnabled(false);
            replayButton.setEnabled(false);
            nextLevelButton.setEnabled(false);


        } else if (from.equals("1")) {
            score = getIntent().getExtras().getInt("score");
            status = Objects.requireNonNull(getIntent().getExtras().getString("status"));
            descriptionTextView.setText(getResources().getString(R.string.score_limit));
            continueButton.setEnabled(false);
            linearLayout.setVisibility(View.VISIBLE);
            currentScoreTextView.setVisibility(View.VISIBLE);
            currentScoreTextView.setText(String.format(getResources().getString(R.string.level_score), String.valueOf(score)));
            tapToContinueTextView.setVisibility(View.INVISIBLE);
            levelTextView.setText(status);
            homeButton.setVisibility(View.VISIBLE);
            replayButton.setVisibility(View.VISIBLE);
            nextLevelButton.setVisibility(View.VISIBLE);
            homeButton.setEnabled(true);
            replayButton.setEnabled(true);
            nextLevelButton.setEnabled(true);
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            replayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PreLevelStart.this, LevelOne.class);
                    startActivity(intent);
                    finish();
                }
            });
            if ((getIntent().getExtras().getInt("lastLevel")) > level) {
                nextLevelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PreLevelStart.this, LevelTwo.class);
                        intent.putExtra("from", "main");
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                nextLevelButton.setEnabled(false);
            }
        } else if (from.equals("2")) {
            score = getIntent().getExtras().getInt("score");
            status = Objects.requireNonNull(getIntent().getExtras().getString("status"));
            descriptionTextView.setText(getResources().getString(R.string.score_limit));
            continueButton.setEnabled(false);
            linearLayout.setVisibility(View.VISIBLE);
            currentScoreTextView.setVisibility(View.VISIBLE);
            currentScoreTextView.setText(String.format(getResources().getString(R.string.level_score), String.valueOf(score)));
            tapToContinueTextView.setVisibility(View.INVISIBLE);
            levelTextView.setText(status);
            homeButton.setVisibility(View.VISIBLE);
            replayButton.setVisibility(View.VISIBLE);
            nextLevelButton.setVisibility(View.VISIBLE);
            homeButton.setEnabled(true);
            replayButton.setEnabled(true);
            nextLevelButton.setEnabled(true);
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            replayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PreLevelStart.this, LevelTwo.class);
                    intent.putExtra("from", "main");
                    startActivity(intent);
                    finish();
                }
            });

            if ((getIntent().getExtras().getInt("lastLevel")) > level) {
                nextLevelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(PreLevelStart.this, LevelThree.class);
                        intent.putExtra("from", "main");
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                nextLevelButton.setEnabled(false);
            }
        } else if (from.equals("3")) {
            score = getIntent().getExtras().getInt("score");
            status = Objects.requireNonNull(getIntent().getExtras().getString("status"));
            descriptionTextView.setText(getResources().getString(R.string.score_limit));
            continueButton.setEnabled(false);
            linearLayout.setVisibility(View.VISIBLE);
            currentScoreTextView.setVisibility(View.VISIBLE);
            currentScoreTextView.setText(String.format(getResources().getString(R.string.level_score), String.valueOf(score)));
            tapToContinueTextView.setVisibility(View.INVISIBLE);
            levelTextView.setText(status);
            homeButton.setVisibility(View.VISIBLE);
            replayButton.setVisibility(View.VISIBLE);
            nextLevelButton.setVisibility(View.VISIBLE);
            homeButton.setEnabled(true);
            replayButton.setEnabled(true);
            nextLevelButton.setEnabled(true);
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            replayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PreLevelStart.this, LevelThree.class);
                    intent.putExtra("from", "main");
                    intent.putExtra("score", 0);
                    startActivity(intent);
                    finish();
                }
            });

            if ((getIntent().getExtras().getInt("lastLevel")) > level) {
                nextLevelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), LevelFour.class);
                        intent.putExtra("round", 1);
                        intent.putExtra("firstPart", true);
                        intent.putExtra("score", 0);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                nextLevelButton.setEnabled(false);
            }
        }
        else if (from.equals("4")) {
            score = getIntent().getExtras().getInt("score");
            status = Objects.requireNonNull(getIntent().getExtras().getString("status"));
            descriptionTextView.setText(getResources().getString(R.string.score_limit));
            continueButton.setEnabled(false);
            linearLayout.setVisibility(View.VISIBLE);
            currentScoreTextView.setVisibility(View.VISIBLE);
            currentScoreTextView.setText(String.format(getResources().getString(R.string.level_score), String.valueOf(score)));
            tapToContinueTextView.setVisibility(View.INVISIBLE);
            levelTextView.setText(status);
            homeButton.setVisibility(View.VISIBLE);
            replayButton.setVisibility(View.VISIBLE);
            nextLevelButton.setVisibility(View.VISIBLE);
            homeButton.setEnabled(true);
            replayButton.setEnabled(true);
            nextLevelButton.setEnabled(true);
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            replayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PreLevelStart.this, LevelFour.class);
                    intent.putExtra("round", 1);
                    intent.putExtra("firstPart", true);
                    intent.putExtra("score", 0);
                    startActivity(intent);
                    finish();
                }
            });

            if ((getIntent().getExtras().getInt("lastLevel")) > level) {
                nextLevelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(PreLevelStart.this, LevelFive.class);
                        intent.putExtra("round", 1);
                        intent.putExtra("difference", 2);
                        intent.putExtra("maxBar", 3500);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                nextLevelButton.setEnabled(false);
            }
        }
        else if (from.equals("5")) {
            score = getIntent().getExtras().getInt("score");
            status = Objects.requireNonNull(getIntent().getExtras().getString("status"));
            descriptionTextView.setText(getResources().getString(R.string.score_limit));
            continueButton.setEnabled(false);
            linearLayout.setVisibility(View.VISIBLE);
            currentScoreTextView.setVisibility(View.VISIBLE);
            currentScoreTextView.setText(String.format(getResources().getString(R.string.level_score), String.valueOf(score)));
            tapToContinueTextView.setVisibility(View.INVISIBLE);
            levelTextView.setText(status);
            homeButton.setVisibility(View.VISIBLE);
            replayButton.setVisibility(View.VISIBLE);
            nextLevelButton.setVisibility(View.VISIBLE);
            homeButton.setEnabled(true);
            replayButton.setEnabled(true);
            nextLevelButton.setEnabled(true);
            homeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            replayButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PreLevelStart.this, LevelFive.class);
                    intent.putExtra("round", 1);
                    intent.putExtra("difference", 2);
                    intent.putExtra("maxBar", 3500);
                    startActivity(intent);
                    finish();
                }
            });

            if ((getIntent().getExtras().getInt("lastLevel")) > level) {
                nextLevelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            } else {
                nextLevelButton.setEnabled(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

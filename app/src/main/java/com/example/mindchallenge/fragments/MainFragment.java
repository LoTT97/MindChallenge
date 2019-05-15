package com.example.mindchallenge.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mindchallenge.Database;
import com.example.mindchallenge.MainActivity;
import com.example.mindchallenge.PreLevelStart;
import com.example.mindchallenge.R;

public class MainFragment extends Fragment {

    Button playButton;
    TextView scoreTextView;
    Database database;
    int lastLevelUnlocked;

    public static final String ARG_POSITION = "position";
    private int position;

    public static MainFragment newInstance(int position){

        MainFragment main = new MainFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION,position);
        main.setArguments(b);

        return main;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.main_fragment, container, false);

        database = new Database(getContext());
//        database.onCreate(database.getWritableDatabase());
        lastLevelUnlocked = database.getLastLevelUnlocked();
        playButton = mainView.findViewById(R.id.playButton);
        scoreTextView = mainView.findViewById(R.id.scoreTextView);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PreLevelStart.class);
                intent.putExtra("level",lastLevelUnlocked);
                intent.putExtra("from","main");
                intent.putExtra("round",4);
                intent.putExtra("level5round",1);
                intent.putExtra("difference",2);
                intent.putExtra("firstPart", true);
                intent.putExtra("score", 0);
                startActivity(intent);
            }
        });

        scoreTextView.setText(String.format("Score: %s", String.valueOf(database.getFullScore())));
        return mainView;
    }
}

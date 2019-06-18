package com.example.mindchallenge.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mindchallenge.Database;
import com.example.mindchallenge.MainActivity;
import com.example.mindchallenge.PreLevelStart;
import com.example.mindchallenge.R;

import java.util.Locale;

public class MainFragment extends Fragment {

    Button playButton;
    Button optionsButton;
    TextView scoreTextView;
    Database database;
    int lastLevelUnlocked;


    //options menu
    Button resetScoreButton;
    Button languageButton;
    boolean optionsClicked;



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
        optionsClicked = false;
        final View mainView = inflater.inflate(R.layout.main_fragment, container, false);
        final RelativeLayout rl = mainView.findViewById(R.id.main_fragment);
        final View optionsView =  inflater.inflate(R.layout.options_layout,rl,false);

        resetScoreButton = optionsView.findViewById(R.id.resetScoreButton);
        languageButton = optionsView.findViewById(R.id.languageButton);

        database = new Database(getContext());
//        database.onCreate(database.getWritableDatabase());
        lastLevelUnlocked = database.getLastLevelUnlocked();
        playButton = mainView.findViewById(R.id.playButton);
        optionsButton = mainView.findViewById(R.id.optionsButton);
        scoreTextView = mainView.findViewById(R.id.scoreTextView);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PreLevelStart.class);
                intent.putExtra("level",lastLevelUnlocked);
                intent.putExtra("from","main");
                intent.putExtra("score", 0);
                startActivity(intent);
            }
        });
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!optionsClicked) {
                    rl.addView(optionsView);
                    hidePlayButton();
                    optionsClicked = true;
                }else{
                    rl.removeView(optionsView);
                    showPlayButton();
                    optionsClicked = false;
                }

            }
        });
        resetScoreButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                database.resetData();
                optionsClicked = false;
                optionsButton.setEnabled(false);
                showPlayButton();
                playButton.setEnabled(false);
                rl.removeView(optionsView);
                Toast.makeText(getActivity(),getResources().getString(R.string.data_got_reset),Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        optionsButton.setEnabled(true);
                        playButton.setEnabled(true);
                        startActivity(new Intent(getContext(),MainActivity.class));
                        getActivity().finish();
                    }
                },1000);
            }
        });
        languageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                PopupMenu popupMenu = new PopupMenu(getActivity(),languageButton);
                popupMenu.getMenuInflater().inflate(R.menu.languages_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitleCondensed().toString()){
                            case "english":
                                setApplicationLanguage("en");
                                break;
                            case "spanish":
                                setApplicationLanguage("es");
                                break;
                            case "french":
                                setApplicationLanguage("fr");
                                break;
                            case "italian":
                                setApplicationLanguage("it");
                                break;
                            case "german":
                                setApplicationLanguage("de");
                                break;
                            case "albanian":
                                setApplicationLanguage("sq");
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        scoreTextView.setText(String.format("Score: %s", String.valueOf(database.getFullScore())));
        return mainView;

    }

    void hidePlayButton(){
        playButton.setEnabled(false);
        playButton.setVisibility(View.INVISIBLE);
    }
    void showPlayButton(){
        playButton.setEnabled(true);
        playButton.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    void setApplicationLanguage(String languageCode){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Configuration conf = getResources().getConfiguration();

        conf.setLocale(new Locale(languageCode.toLowerCase()));

        Toast.makeText(getContext(),getString(R.string.lan_change),Toast.LENGTH_LONG).show();
        getResources().updateConfiguration(conf,dm);
    }
}

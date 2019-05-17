package com.example.mindchallenge;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mindchallenge.levels.LevelFive;
import com.example.mindchallenge.levels.LevelFour;
import com.example.mindchallenge.levels.LevelOne;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class LevelViewerAdapter extends RecyclerView.Adapter<LevelViewerAdapter.LevelViewHolder> {

    //Levels view class
    public static class LevelViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vScore;
        protected View cardView;
        protected LinearLayout linearLayout;
        protected ImageView imageView;

        public LevelViewHolder(View v) {
            super(v);
            vName = v.findViewById(R.id.card_level_name_text);
            vScore = v.findViewById(R.id.card_level_score_text);
            cardView = v.findViewById(R.id.card_view);
            linearLayout = v.findViewById(R.id.innerCardLinearLayout);
            imageView = v.findViewById(R.id.cardImageView);

        }

    }

    private static final String LOG_TAG = "LevelViewerAdapter";

    private LevelItem item;
    private int lastUnclokedLevel;
    private Database mDatabase;
    private Context mContext;
    private LinearLayoutManager llm;

    public LevelViewerAdapter(Context context, LinearLayoutManager linearLayoutManager) {
        super();
        mContext = context;
        mDatabase = new Database(mContext);
        llm = linearLayoutManager;
        lastUnclokedLevel = mDatabase.getLastLevelUnlocked();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull final LevelViewerAdapter.LevelViewHolder holder, final int position) {

        item = getItem(position);
        holder.vName.setText(item.getmLevelName());
        holder.vScore.setText(String.format(mContext.getResources().getString(R.string.level_score), String.valueOf(item.getmLevelScore())));
        if (holder.getLayoutPosition() < lastUnclokedLevel) {
            holder.linearLayout.setBackground(null);
            holder.linearLayout.setAlpha(1);
        }

        switch (holder.getLayoutPosition()) {
            case 0:
                holder.imageView.setImageResource(R.mipmap.preview_level_one);
                holder.imageView.setPadding(16, 0, 0, 0);
                break;
            case 1:
                holder.imageView.setImageResource(R.mipmap.preview_level_two);
                holder.imageView.setPadding(16, 0, 0, 0);
                break;
            case 2:
                holder.imageView.setImageResource(R.mipmap.preview_level_three);
                holder.imageView.setPadding(16, 0, 0, 0);
                break;
            case 3:
                holder.imageView.setImageResource(R.mipmap.preview_level_four);
                holder.imageView.setPadding(16, 0, 0, 0);
                break;
            case 4:
                holder.imageView.setImageResource(R.mipmap.preview_level_two);
                holder.imageView.setPadding(16, 0, 0, 0);
                break;
            default:
                break;
        }
        // define an on click listener to open PlaybackFragment
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent;
                    switch (holder.getLayoutPosition()) {

                        case 0:
                            intent = new Intent(mContext, PreLevelStart.class);
                            intent.putExtra("level", 1);
                            intent.putExtra("from", "main");
                            mContext.startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(mContext, PreLevelStart.class);
                            intent.putExtra("level", 2);
                            intent.putExtra("from", "main");
                            mContext.startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(mContext, PreLevelStart.class);
                            intent.putExtra("level", 3);
                            intent.putExtra("from", "main");
                            mContext.startActivity(intent);
                            break;
                        case 3:
                            intent = new Intent(mContext, PreLevelStart.class);
                            intent.putExtra("level", 4);
                            intent.putExtra("from", "main");
                            mContext.startActivity(intent);
                            break;
                        case 4:
                            intent = new Intent(mContext, PreLevelStart.class);
                            intent.putExtra("level", 5);
                            intent.putExtra("from", "main");
                            mContext.startActivity(intent);
                            break;
                        default:
                            break;
                    }

                } catch (Exception e) {
                    Log.e(LOG_TAG, "exception", e);
                }
            }
        });


    }

    @Override
    public LevelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_view, parent, false);

        mContext = parent.getContext();

        return new LevelViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return mDatabase.getCount();
    }

    public LevelItem getItem(int position) {
        return mDatabase.getItemAt(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}


package com.example.mindchallenge.fragments;

import android.os.Bundle;
import android.os.FileObserver;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mindchallenge.LevelViewerAdapter;
import com.example.mindchallenge.R;

public class LevelViewerFragment extends Fragment {
    private static final String ARG_POSITION =  "position";

    private static final String LOG_TAG =  "FileViewerFragment";

    private int position;

    private LevelViewerAdapter mLevelViewerAdapter;

    public static LevelViewerFragment newInstance(int position)
    {
        LevelViewerFragment f = new LevelViewerFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.all_levels, container, false);


        RecyclerView mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        //newest to oldest order (database stores from oldest to newest)
        llm.setReverseLayout(false);
        llm.setStackFromEnd(false);

        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mLevelViewerAdapter = new LevelViewerAdapter(getActivity(), llm);
        mRecyclerView.setAdapter(mLevelViewerAdapter);

        return v;
    }


}

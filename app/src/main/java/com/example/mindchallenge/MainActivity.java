package com.example.mindchallenge;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.astuetz.PagerSlidingTabStrip;
import com.example.mindchallenge.fragments.LevelViewerFragment;
import com.example.mindchallenge.fragments.MainFragment;
public class MainActivity extends AppCompatActivity{

    PagerSlidingTabStrip tabs;
    ViewPager pager;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       pager = findViewById(R.id.pager);
       pager.setAdapter(new MyAdapter(getSupportFragmentManager()));
       tabs = findViewById(R.id.tabs);
       tabs.setViewPager(pager);


    }

    public class MyAdapter extends FragmentPagerAdapter{

        public String[] titles = {getString(R.string.tab_title_main),getString(R.string.tab_title_levels)};
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch(i){
                case 0:
                    return MainFragment.newInstance(i);
                case 1:
                    return LevelViewerFragment.newInstance(i);
            }
            return null;
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}



package com.example.mindchallenge;

import android.content.Context;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import com.example.mindchallenge.levels.LevelOne;

public class FadeOutScore extends android.support.v7.widget.AppCompatButton {
    public FadeOutScore(Context context) {
        super(context);
    }

    @Override
    protected void onAttachedToWindow() {

        super.onAttachedToWindow();
        ScaleAnimation animation = new ScaleAnimation(0,1,0,1);
        animation.setDuration(1300);
        animation.setFillBefore(true);
        this.startAnimation(animation);
    }
}

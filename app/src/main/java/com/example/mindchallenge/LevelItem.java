package com.example.mindchallenge;

import android.os.Parcel;
import android.os.Parcelable;

public class LevelItem implements Parcelable {

    private String mLevelName;
    private int mLevelScore;


    public static final Parcelable.Creator<LevelItem> CREATOR = new Parcelable.Creator<LevelItem>(){
        public LevelItem createFromParcel(Parcel p){return new LevelItem(p);}
        public LevelItem[] newArray(int size){return new LevelItem[size];}
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mLevelName);
        dest.writeInt(mLevelScore);
    }


    public LevelItem(Parcel p){
        mLevelName = p.readString();
        mLevelScore = p.readInt();
    }

    public String getmLevelName() {
        return mLevelName;
    }

    public void setmLevelName(String mLevelName) {
        this.mLevelName = mLevelName;
    }

    public int getmLevelScore() {
        return mLevelScore;
    }

    public void setmLevelScore(int mLevelScore) {
        this.mLevelScore = mLevelScore;
    }

    public LevelItem(){

    }
}

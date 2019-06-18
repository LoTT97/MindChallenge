package com.example.mindchallenge;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class Database extends SQLiteOpenHelper {

    public Context mContext;
    public static final String DbName = "mind_challenge.db";
    private static final int DbVersion = 1;
    private SQLiteDatabase database;


    public static abstract class DbItem implements BaseColumns {
        public static final String TableName = "Scoreboard";
        public static final String SCORE_COLUMN = "score";
        public static final String LEVEL_COLUMN = "level";
        public static final String LEVEL_UNLOCKED_COLUMN = "level_unlocked";
    }

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + DbItem.TableName + " (" + DbItem._ID + " INTEGER PRIMARY KEY,"
            + DbItem.SCORE_COLUMN + " INTEGER,"
            + DbItem.LEVEL_COLUMN + " INTEGER,"
            + DbItem.LEVEL_UNLOCKED_COLUMN + " TINYINT(1) )";


    public Database(Context context) {
        super(context, DbName, null, DbVersion);
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS '" + DbItem.TableName + "'");
        db.execSQL(SQL_CREATE_ENTRIES);

        for (int i = 1; i <= 100; i++) {
            ContentValues cv = new ContentValues();
            cv.put(DbItem.SCORE_COLUMN, 0);
            cv.put(DbItem.LEVEL_COLUMN, i);
            cv.put(DbItem.LEVEL_UNLOCKED_COLUMN, 0);
            db.insert(DbItem.TableName, null, cv);
        }
        database = db;
        unlockLevel(1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.setVersion(newVersion);
    }

    public void unlockLevel(int level) {
        SQLiteDatabase db = database == null ? getWritableDatabase() : database;
        ContentValues cv = new ContentValues();
        cv.put(DbItem.LEVEL_UNLOCKED_COLUMN, 1);
        db.update(DbItem.TableName, cv, "level = " + level, null);
    }

    public int getScoreAtLevel(int level) {
        SQLiteDatabase db = getReadableDatabase();
        int score = 0;
        String[] tableColumns = new String[]{
                DbItem.SCORE_COLUMN,
                DbItem.LEVEL_COLUMN
        };

        Cursor c = db.query(DbItem.TableName, tableColumns, null, null,
                null, null, null);
        while (c.moveToNext()) {
            if (c.getInt(c.getColumnIndex(DbItem.LEVEL_COLUMN)) == level) {
                score = c.getInt(c.getColumnIndex(DbItem.SCORE_COLUMN));
                break;
            }
        }
        return score;

    }

    public int getLastLevelUnlocked() {
        SQLiteDatabase db = getReadableDatabase();

        String[] tableColumns = new String[]{
                DbItem.LEVEL_COLUMN,
                DbItem.SCORE_COLUMN,
                DbItem.LEVEL_UNLOCKED_COLUMN
        };
        Cursor c = db.query(DbItem.TableName, tableColumns, null, null,
                null, null, null);
        int maxlevel = 0;
        while (c.moveToNext()) {
            if ((c.getInt(c.getColumnIndex(DbItem.LEVEL_UNLOCKED_COLUMN)) == 1)) {
                if (maxlevel < c.getInt(c.getColumnIndex(DbItem.LEVEL_COLUMN))) {
                    maxlevel = c.getInt(c.getColumnIndex(DbItem.LEVEL_COLUMN));
                }
            }
        }
        return maxlevel;
    }

    public void RemoveColumnWithId(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = {String.valueOf(id)};
        db.delete(DbItem.TableName, "_ID=?", whereArgs);
    }


    public void updateScore(int score, int level) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DbItem.SCORE_COLUMN, score);

        db.update(DbItem.TableName, cv, DbItem.LEVEL_COLUMN + " = " + level, null);
    }
    public void resetData(){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DbItem.SCORE_COLUMN, 0);
        cv.put(DbItem.LEVEL_UNLOCKED_COLUMN, 0);

        db.update(DbItem.TableName, cv, null,null);
        unlockLevel(1);
    }
    public int getFullScore() {
        SQLiteDatabase db = getReadableDatabase();
        int score = 0;
        String[] selColumns = new String[]{
                DbItem.SCORE_COLUMN
        };
        Cursor c = db.query(DbItem.TableName, selColumns, null, null, null, null, null);
        while (c.moveToNext()) {
            score += c.getInt(c.getColumnIndex(DbItem.SCORE_COLUMN));
        }
        return score;
    }

    public LevelItem getItemAt(int position){
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {
                DbItem.LEVEL_COLUMN,
                DbItem.SCORE_COLUMN
        };

        Cursor c = db.query(DbItem.TableName,columns,null,null,null,null,null);

        if(c.moveToPosition(position)){
            LevelItem item = new LevelItem();
            item.setmLevelName(String.format(getContext().getResources().getString(R.string.level),String.valueOf(c.getInt(c.getColumnIndex(DbItem.LEVEL_COLUMN)))));
            item.setmLevelScore(c.getInt(c.getColumnIndex(DbItem.SCORE_COLUMN)));
            c.close();
            return item;
        }
        return null;
    }

    public int getCount(){
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {DbItem._ID};
        Cursor c = db.query(DbItem.TableName,columns,null,null,null,null,null);
        int count = c.getCount();
        c.close();
        return count;
    }
}

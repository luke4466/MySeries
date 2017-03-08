package com.lukaszgielec.myseries;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Łukasz Gielec on 06.03.2017.
 */

public class LocalDatabase extends SQLiteOpenHelper {
    private static final String mDatabaseName = "LocalDatabase.db";
    private static final String mTableName = "series";

    private static final String mID = "id";
    private static final String mImdbID = "ImdbID";
    private static final String mIsFollowing = "following";
    private static final String mIsWatched = "watched";


    public LocalDatabase(Context context) {
        super(context, mDatabaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + mTableName + "("
                + mID + " INTEGER PRIMARY KEY,"
                + mIsFollowing + " INTEGER,"
                + mIsWatched + " INTEGER,"
                + mImdbID + " TEXT )";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addFollowingSeries(Series series) {
        ContentValues values = new ContentValues();
        values.put(mImdbID, series.getImdbID());
        values.put(mIsFollowing, 1);

        if (!update(values)) add(values);

        series.setFollowing(true);


    }

    public void removeFollowingSeries(Series series){
        ContentValues values = new ContentValues();
        values.put(mImdbID, series.getImdbID());
        values.put(mIsFollowing, 0);

        if (!update(values)) add(values);

        series.setFollowing(false);


    }

    public boolean isFollowing(Series series) {
        boolean response = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ mTableName+ " WHERE " + mImdbID +" = "+"'"+series.getImdbID()+"'",null);
        if(cursor.moveToFirst())
        {
            cursor.moveToFirst();
            if(cursor.getInt(1) == 1){
                response =  true;
            }
        }
        db.close();
        cursor.close();
        return response;
    }

    public void addWatchedSeries(Series series){
        ContentValues values = new ContentValues();
        values.put(mImdbID, series.getImdbID());
        values.put(mIsWatched, 1);

        if (!update(values)) add(values);

        series.setWatched(true);

    }

    public void removeWatchedSeries(Series series){
        ContentValues values = new ContentValues();
        values.put(mImdbID, series.getImdbID());
        values.put(mIsWatched, 0);

        if (!update(values)) add(values);

        series.setWatched(false);


    }

    public boolean isWatched(Series series) {
        boolean response = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ mTableName+ " WHERE " + mImdbID +" = "+"'"+series.getImdbID()+"'",null);
        if(cursor.moveToFirst())
        {
            cursor.moveToFirst();
            if(cursor.getInt(2) == 1){
                response =  true;
            }
        }
        db.close();
        cursor.close();
        return response;
    }

    private boolean update(ContentValues contentValues){
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.update(mTableName, contentValues, mImdbID + " = ?", new String[]{ contentValues.getAsString(mImdbID) });
        db.close();
        return i == 1;
    }

    private void add(ContentValues contentValues){
        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(mTableName, null, contentValues);
        db.close();
    }
}



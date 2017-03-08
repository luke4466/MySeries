package com.lukaszgielec.myseries;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Łukasz Gielec on 07.03.2017.
 */

public class Database implements DatabaseInterface {
    private Context mContext;
    private LocalDatabase mLocalDatabase;
    private CloudDatabase mCloudDatabase;

    private int mPage = 0;
    private String mQuery;


    public Database(Context context){
        mContext = context;
        mLocalDatabase = new LocalDatabase(mContext);
        mCloudDatabase = new CloudDatabase();

    }

    @Override
    public ArrayList<Series> search(String query, boolean localSearch) throws DatabaseException {
        Log.i("Search now!","Searching: "+query);
        ArrayList<Series> seriesList = new ArrayList<>();

        if (isNewQuery(query)){
            mQuery = query;

            if (localSearch){

            }else {
                seriesList = mCloudDatabase.search(query,1);

                if (seriesList.isEmpty()){
                    throw new DatabaseException("NotFound");
                }else {
                    mPage = 1;
                }

                setLocalAttributes(seriesList);

            }

        }else {
            mPage++;
            seriesList = mCloudDatabase.search(query, mPage);
            if (seriesList.isEmpty()) {
                throw new DatabaseException("NoMoreResults");
            }
            setLocalAttributes(seriesList);

        }

        return seriesList;
    }

    private boolean isNewQuery(String query){
        if(mQuery == null || !mQuery.equals(query)){
            return true;
        }else return false;
    }

    private void setLocalAttributes(List<Series> seriesList){
        for (Series series : seriesList){
            series.setWatched(mLocalDatabase.isWatched(series));
            series.setFollowing(mLocalDatabase.isFollowing(series));
        }

    }

    @Override
    public void addWatchedSeries(Series series) {
        mLocalDatabase.addWatchedSeries(series);
        Log.i("Database","Watched Series added: "+series.getTitle());

    }

    @Override
    public void removeWatchedSeries(Series series) {
        mLocalDatabase.removeWatchedSeries(series);
        Log.i("Database","Watched Series removed: "+series.getTitle());
    }

    @Override
    public void addFollowingSeries(Series series) {
        mLocalDatabase.addFollowingSeries(series);
        Log.i("Database","Following Series added: "+series.getTitle());


    }

    @Override
    public void removeFollowingSeries(Series series) {
        mLocalDatabase.removeFollowingSeries(series);
        Log.i("Database","Following Series removed: "+series.getTitle());

    }


    public void update(ArrayList<Series> seriesList){
        for (Series series : seriesList){
            series.setWatched(mLocalDatabase.isWatched(series));
            series.setFollowing(mLocalDatabase.isFollowing(series));
        }

    }
}

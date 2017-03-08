package com.lukaszgielec.myseries;

import java.util.ArrayList;

/**
 * Created by Łukasz Gielec on 07.03.2017.
 */

public interface DatabaseInterface {

    ArrayList<Series> search(String query, boolean localSearch) throws DatabaseException;

    void addWatchedSeries(Series series);
    void removeWatchedSeries(Series series);

    void addFollowingSeries(Series series);
    void removeFollowingSeries(Series series);

    void update(ArrayList<Series> seriesList);

}

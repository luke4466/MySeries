package com.lukaszgielec.myseries;

import java.io.Serializable;

/**
 * Created by Łukasz Gielec on 09.01.2017.
 */

public class Series implements Serializable {

    private String mTitle;
    private String mYear;
    private String mImdbID;
    private String mPosterURL;
    private String mReleasedDate;
    private String mRuntime;
    private String mGenre;
    private String mDirector;
    private String mWriter;
    private String mActors;
    private String mFullPlot;
    private String mLanguage;
    private String mCountry;
    private String mAwards;
    private String mImdbRating;
    private String mImdbVotes;
    private String mTotalSeasons;

    private boolean isFollowing;
    private boolean isWatched;

    public Series(){

    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getYear() {
        return mYear;
    }

    public void setYear(String mYear) {
        this.mYear = mYear;
    }

    public String getPosterURL() {
        return mPosterURL;
    }

    public void setPosterURL(String mPosterURL) {
        this.mPosterURL = mPosterURL;
    }

    public String getImdbID() {
        return mImdbID;
    }

    public void setImdbID(String mImdbID) {
        this.mImdbID = mImdbID;
    }


    public String getReleasedDate() {
        return mReleasedDate;
    }

    public void setReleasedDate(String mReleased) {
        this.mReleasedDate = mReleased;
    }

    public String getRuntime() {
        return mRuntime;
    }

    public void setRuntime(String mRuntime) {
        this.mRuntime = mRuntime;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String mGenre) {
        this.mGenre = mGenre;
    }

    public String getDirector() {
        return mDirector;
    }

    public void setDirector(String mDirector) {
        this.mDirector = mDirector;
    }

    public String getWriter() {
        return mWriter;
    }

    public void setWriter(String mWriter) {
        this.mWriter = mWriter;
    }

    public String getActors() {
        return mActors;
    }

    public void setActors(String mActors) {
        this.mActors = mActors;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public void setLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }

    public String getFullPlot() {
        return mFullPlot;
    }

    public void setFullPlot(String mFullPlot) {
        this.mFullPlot = mFullPlot;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getAwards() {
        return mAwards;
    }

    public void setAwards(String mAwards) {
        this.mAwards = mAwards;
    }

    public String getImdbRating() {
        return mImdbRating;
    }

    public void setImdbRating(String mImdbRating) {
        this.mImdbRating = mImdbRating;
    }

    public String getImdbVotes() {
        return mImdbVotes;
    }

    public void setImdbVotes(String mImdbVotes) {
        this.mImdbVotes = mImdbVotes;
    }

    public String getTotalSeasons() {
        return mTotalSeasons;
    }

    public void setTotalSeasons(String mTotalSeasons) {
        this.mTotalSeasons = mTotalSeasons;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean isFollowing) {
        this.isFollowing = isFollowing;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }
}

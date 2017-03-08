package com.lukaszgielec.myseries;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailSeries extends AppCompatActivity {


    final public static String SERIES = "SERIES";

    private Series mSeries;

    private ImageView mPoster;
    private TextView mTitle;
    private ImageView mFollowingButton;
    private ImageView mWatchedButton;

    private MyListener mListener;

    private Database mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
// inside your activity (if you did not enable transitions in your theme)
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
// set an enter transition
        getWindow().setEnterTransition(new Fade());
// set an exit transition
        getWindow().setExitTransition(new Fade());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_series);

        mSeries =(Series) getIntent().getSerializableExtra(SERIES);


        initializeView();

        initializeListeners();

        mDatabase = new Database(this);



    }

    private void initializeView(){
        mPoster = (ImageView) findViewById(R.id.poster);
        mFollowingButton = (ImageView) findViewById(R.id.followingButton);
        mWatchedButton = (ImageView) findViewById(R.id.watchedButton);
        mTitle = (TextView) findViewById(R.id.title);

        if(mSeries.getPosterURL().equals("N/A")) Picasso.with(this).load(R.drawable.no_poster).into(mPoster);
        else Picasso.with(this).load(mSeries.getPosterURL()).into(mPoster);

        if (mSeries.isFollowing()) Picasso.with(this).load(R.drawable.ic_favorite_black_48dp).into(mFollowingButton);
        else Picasso.with(this).load(R.drawable.ic_favorite_border_black_48dp).into(mFollowingButton);

        if (mSeries.isWatched()) Picasso.with(this).load(R.drawable.ic_watched_black).into(mWatchedButton);
        else Picasso.with(this).load(R.drawable.ic_not_watched_black).into(mWatchedButton);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mPoster.setTranslationY((float) (1.3*verticalOffset));
            }
        });

    }

    private void initializeListeners(){
        mListener = new MyListener();

        mWatchedButton.setOnClickListener(mListener);
        mFollowingButton.setOnClickListener(mListener);

    }

    private class MyListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.followingButton: {
                    if (mSeries.isFollowing()) {
                        mDatabase.removeFollowingSeries(mSeries);
                    } else {
                        mDatabase.addFollowingSeries(mSeries);
                    }
                    initializeView();
                    break;
                }
                case R.id.watchedButton: {
                    if (mSeries.isWatched()) {
                        mDatabase.removeWatchedSeries(mSeries);
                    } else {
                        mDatabase.addWatchedSeries(mSeries);
                    }
                    initializeView();
                    break;
                }
            }
        }
    }

}

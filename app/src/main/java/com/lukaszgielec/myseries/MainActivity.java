package com.lukaszgielec.myseries;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private BottomBar mBottomBar;
    private MyOnTabSelectListener mTabSelectListener = new MyOnTabSelectListener();

    private FloatingSearchView mFloatingSearchView;

    private ArrayList<FloatingSearchView.OnSearchListener> mListeners;

    private SearchFragment mSearchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchFragment = new SearchFragment();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mFloatingSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        mFloatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                if(mListeners!=null){
                    for(FloatingSearchView.OnSearchListener listener : mListeners){
                        listener.onSearchAction(currentQuery);
                    }
                }
            }
        });
        mFloatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {

            }
        });

        mListeners = new ArrayList<>();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               /* //Log.i("position:",""+positionOffset);

                float offsetY2 = 80 * positionOffset;
                int offsetY = (int) offsetY2;
                //Log.i("offsetY: ",""+offsetY);
                if(mListeners!=null) {
                    for (int i = 0; i < mListeners.size(); i++) {
                        if(i == position) mListeners.get(i).onChangeOffset(-offsetY);
                        if(i == position+1) mListeners.get(i).onChangeOffset(-80 + offsetY);
                    }
                }
                */
            }

            @Override
            public void onPageSelected(int position) {
                mBottomBar.selectTabAtPosition(position);
                mFloatingSearchView.clearQuery();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mBottomBar.setOnTabSelectListener(mTabSelectListener);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    return new SoonFragment();

                case 1:
                    return new SoonFragment();

                case 2:
                    return mSearchFragment;

                default:
                    return new SoonFragment();
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 3:
                    return "SECTION 3";
            }
            return null;
        }
    }

    private class MyOnTabSelectListener implements OnTabSelectListener {


        @Override
        public void onTabSelected(@IdRes int tabId) {

            switch (tabId){
                case R.id.tab_watching:{
                    mViewPager.setCurrentItem(0);
                    Log.i("MainActivity","TabSelected: Favourite");

                    break;
                }

                case R.id.tab_soon:{
                    Log.i("MainActivity","TabSelected: Soon");
                    mViewPager.setCurrentItem(1);
                    break;
                }
                case R.id.tab_search:{
                    Log.i("MainActivity","TabSelected: Search");
                    mViewPager.setCurrentItem(2);
                    break;
                }

                case R.id.tab_watched:{
                    Log.i("MainActivity","TabSelected: Watched");
                    mViewPager.setCurrentItem(3);
                    break;
                }
            }

        }

    }

    public void setListener(FloatingSearchView.OnSearchListener listener){
        if(mListeners!=null) mListeners.add(listener);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSearchFragment.onResume();
    }
}

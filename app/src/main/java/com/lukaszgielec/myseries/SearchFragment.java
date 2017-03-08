package com.lukaszgielec.myseries;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SearchFragment extends Fragment implements FloatingSearchView.OnSearchListener, RecyclerViewClickListener {


    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private SearchRecyclerViewAdapter mAdapter;

    private ArrayList<Series> mShowedSeriesList = new ArrayList<>();
    private boolean isNoResults = false;
    private String mQuery;

    private View mView;

    private Database mDatabase;

    private boolean isLoading = false;



    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setListener(this);
        mDatabase = new Database(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_search, container, false);

            mRecyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
            mLinearLayoutManager = new LinearLayoutManager(mView.getContext());
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mAdapter = new SearchRecyclerViewAdapter();
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setNestedScrollingEnabled(false);
        }

        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }


    @Override
    public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

    }

    @Override
    public void onSearchAction(String currentQuery) {
        Log.i("OnSearchAction", currentQuery);
        if(!isLoading) search(currentQuery);

    }

    private void search(String query) {
//        mTitle = query;
//        mPage = 0;

        if (mQuery == null || !mQuery.equals(query)) {
            isNoResults = false;
            mQuery = query;

            mShowedSeriesList.clear();
            mShowedSeriesList.add(new Loading());

            mAdapter.notifyDataSetChanged();
            new SearchInDB().execute(mQuery);

        }


    }


    private class SearchInDB extends AsyncTask<String, ArrayList<Series>, Exception> {

        @Override
        protected void onPreExecute() {

            mAdapter.setLoading(true);

        }

        @Override
        protected Exception doInBackground(String... params) {

            ArrayList<Series> seriesList = new ArrayList<>();
            try {
                seriesList = mDatabase.search(params[0], false);


            } catch (DatabaseException e) {
                return e;
            }

            publishProgress(seriesList);

            return null;
        }

        @Override
        protected void onProgressUpdate(ArrayList<Series>... seriesList) {

            mShowedSeriesList.remove(mShowedSeriesList.size() - 1);
            mShowedSeriesList.addAll(seriesList[0]);
            mShowedSeriesList.add(new Loading());

            mAdapter.setLoading(false);
            mAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onPostExecute(Exception exception) {

            if (exception == null) {

            } else if (((DatabaseException) exception).getMyMessage().equals("NoMoreResults")) {
                Snackbar.make(mRecyclerView.getRootView(), "No more results", Snackbar.LENGTH_LONG).show();
                isNoResults = true;
                if(!mShowedSeriesList.isEmpty() && mShowedSeriesList.get(mShowedSeriesList.size()-1) instanceof Loading) {
                    mShowedSeriesList.remove(mShowedSeriesList.size() - 1);
                }
                mAdapter.notifyDataSetChanged();

            } else if (((DatabaseException) exception).getMyMessage().equals("NotFound")) {
                Snackbar.make(mRecyclerView.getRootView(), "No results", Snackbar.LENGTH_LONG).show();
                isNoResults = true;
                if(!mShowedSeriesList.isEmpty() && mShowedSeriesList.get(mShowedSeriesList.size()-1) instanceof Loading) {
                    mShowedSeriesList.remove(mShowedSeriesList.size() - 1);
                }
                mAdapter.notifyDataSetChanged();

            }

        }

    }

    class SearchRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int VIEW_TYPE_ITEM = 0;
        private final int VIEW_TYPE_LOADING = 1;

        public SearchRecyclerViewAdapter() {

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (!isLoading && !recyclerView.canScrollVertically(1)) {
                        if (!isNoResults) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    new SearchInDB().execute(mQuery);
                                }
                            });
                        }

                    }
                }
            });
        }


        public void setLoading(boolean loading) {
            isLoading = loading;
        }


        @Override
        public int getItemViewType(int position) {
            if (mShowedSeriesList.get(position) instanceof Loading) return VIEW_TYPE_LOADING;
            else return VIEW_TYPE_ITEM;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.series_search_card, parent, false);
                return new SearchResultViewHolder(view);
            } else if (viewType == VIEW_TYPE_LOADING) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.series_search_loading, parent, false);
                return new ProgressViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof SearchResultViewHolder) {

                Series series = mShowedSeriesList.get(position);
                SearchResultViewHolder searchResultViewHolder = (SearchResultViewHolder) holder;
                searchResultViewHolder.mPoster.setVisibility(View.VISIBLE);
                searchResultViewHolder.mTitle.setText(series.getTitle());
                searchResultViewHolder.mGenre.setText(series.getGenre());
                searchResultViewHolder.mRating.setText(series.getImdbRating());
                searchResultViewHolder.mVotes.setText(series.getImdbVotes());
                searchResultViewHolder.mRuntime.setText(series.getRuntime());
                searchResultViewHolder.mYear.setText(series.getYear());
                if (series.getPosterURL().equals("N/A"))
                    Picasso.with(getContext()).load(R.drawable.no_poster).into(searchResultViewHolder.mPoster);
                else
                    Picasso.with(getContext()).load(series.getPosterURL()).into(searchResultViewHolder.mPoster);

                if (series.isFollowing()) {
                    searchResultViewHolder.mFollowingButton.setImageResource(R.drawable.ic_favorite_black_48dp);
                } else {
                    searchResultViewHolder.mFollowingButton.setImageResource(R.drawable.ic_favorite_border_black_48dp);
                }

                if (series.isWatched()) {
                    searchResultViewHolder.mWatchedButton.setImageResource(R.drawable.ic_watched_black);
                } else {
                    searchResultViewHolder.mWatchedButton.setImageResource(R.drawable.ic_not_watched_black);
                }


            } else if (holder instanceof ProgressViewHolder) {
                ProgressViewHolder loadingViewHolder = (ProgressViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
        }

        @Override
        public int getItemCount() {
            return mShowedSeriesList.size();
        }


        class SearchResultViewHolder extends RecyclerView.ViewHolder {

            private myOnClick mListener = new myOnClick();

            TextView mTitle;
            TextView mYear;
            TextView mGenre;
            TextView mRating;
            TextView mVotes;
            TextView mRuntime;

            ImageView mWatchedButton;
            ImageView mFollowingButton;
            ImageView mPoster;
            CardView mCardView;

            SearchResultViewHolder(View itemView) {
                super(itemView);
                mTitle = (TextView) itemView.findViewById(R.id.title);
                mYear = (TextView) itemView.findViewById(R.id.year);
                mGenre = (TextView) itemView.findViewById(R.id.genre);
                mRating = (TextView) itemView.findViewById(R.id.imdbRating);
                mVotes = (TextView) itemView.findViewById(R.id.imdbVotes);
                mRuntime = (TextView) itemView.findViewById(R.id.runtime);
                mFollowingButton = (ImageView) itemView.findViewById(R.id.followingButton);
                mWatchedButton = (ImageView) itemView.findViewById(R.id.watchedButton);
                mPoster = (ImageView) itemView.findViewById(R.id.poster);
                mCardView = (CardView) itemView.findViewById(R.id.cardView);


                mCardView.setOnClickListener(mListener);
                mFollowingButton.setOnClickListener(mListener);
                mWatchedButton.setOnClickListener(mListener);


            }

            class myOnClick implements View.OnClickListener {

                @Override
                public void onClick(View v) {
                    Log.i("onClick","id "+v.getId());
                    Series series = mShowedSeriesList.get(getAdapterPosition());

                    switch (v.getId()) {
                        case R.id.followingButton: {
                            if (series.isFollowing()) {
                                mDatabase.removeFollowingSeries(series);
                            } else {
                                mDatabase.addFollowingSeries(series);
                            }
                            mAdapter.notifyItemChanged(getAdapterPosition());
                            break;
                        }
                        case R.id.watchedButton: {
                            if (series.isWatched()) {
                                mDatabase.removeWatchedSeries(series);
                            } else {
                                mDatabase.addWatchedSeries(series);
                            }
                            mAdapter.notifyItemChanged(getAdapterPosition());
                            break;
                        }
                        case R.id.cardView:{

                            Intent intent = new Intent(getContext(),DetailSeries.class);
                            intent.putExtra(DetailSeries.SERIES,series);

                            startActivity(intent);

                            break;
                        }

                    }


                }
            }

        }


        class ProgressViewHolder extends RecyclerView.ViewHolder {
            ProgressBar progressBar;

            ProgressViewHolder(View v) {
                super(v);
                progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            }
        }
    }

    private class Loading extends Series {

    }


    @Override
    public void onWatchingButtonClick(View view, int position) {

    }


    @Override
    public void onResume() {
        super.onResume();
        if (mShowedSeriesList != null && mDatabase != null) {
            mDatabase.update(mShowedSeriesList);
            mAdapter.notifyDataSetChanged();
        }
    }
}


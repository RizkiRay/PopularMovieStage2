package com.rizkiraynaldy.popularmoviestage2.list_movies;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.rizkiraynaldy.popularmoviestage2.R;
import com.rizkiraynaldy.popularmoviestage2.SpacesItemDecoration;
import com.rizkiraynaldy.popularmoviestage2.callback.OnLoadMoreListener;
import com.rizkiraynaldy.popularmoviestage2.data.MovieContract;
import com.rizkiraynaldy.popularmoviestage2.detail_movie.MovieDetailActivity;
import com.rizkiraynaldy.popularmoviestage2.model.Movie;
import com.rizkiraynaldy.popularmoviestage2.model.MovieResponse;
import com.rizkiraynaldy.popularmoviestage2.network.ApiClient;
import com.rizkiraynaldy.popularmoviestage2.network.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener,
        MovieCursorAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<Cursor>, OnLoadMoreListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FAVORITE_MOVIES = 100;
    private static final String RECYCLER_MOVIE_STATE = "movie_list_state";
    private RecyclerView mRecyclerView;
    private List<Movie> movies;
    private Parcelable mGridState;
    int page = 1;
    private MovieAdapter adapter;
    private ProgressBar mProgressLoading;
    private int movieType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(" ");

        movies = new ArrayList<>();

        mProgressLoading = (ProgressBar) findViewById(R.id.progress_loading);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_movie);



//        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
//        } else {
//            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
//        }
//        int spacingPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
//        mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingPixels));

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new MovieAdapter(movies, this, mRecyclerView);

        mRecyclerView.setAdapter(adapter);


        adapter.setOnLoadMoreListener(this);

        getPopularMovie(page);

        setLoading();
    }

    private void getPopularMovie(final int page) {
//        adapter.setLoading(true);
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = service.getMoviePopular(ApiClient.API_KEY, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse apiResponse = response.body();
                Log.i(TAG, "onResponse: page " + apiResponse.getPage());
                movies.addAll(apiResponse.getMoviePopulars());
                adapter.notifyDataSetChanged();
                adapter.setLoading(false);
                if (apiResponse.getPage() < apiResponse.getTotalPages()) MainActivity.this.page++;
                else if (apiResponse.getPage() == apiResponse.getTotalPages())
                    adapter.setFinished(true);

                setStopLoading();

                Log.i(TAG, "onResponse: jalan");

            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void getTopRatedMovie(int page) {
//        adapter.setLoading(true);
        Log.i(TAG, "getPopularMovie: called");
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = service.getMovieTopRated(ApiClient.API_KEY, page);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse apiResponse = response.body();
                movies.addAll(apiResponse.getMoviePopulars());
                adapter.notifyDataSetChanged();
                adapter.setLoading(false);
                if (apiResponse.getPage() < apiResponse.getTotalPages()) MainActivity.this.page++;
                else if (apiResponse.getPage() == apiResponse.getTotalPages())
                    adapter.setFinished(true);

                setStopLoading();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }

    //on poster click
    @Override
    public void onListItemClick(int clickedItemIndex, ImageView image, Movie movie) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movie", movies.get(clickedItemIndex));
        intent.putExtra("image_url", Movie.IMAGE_URL + movie.moviePoster);
        intent.putExtra("transition", ViewCompat.getTransitionName(image));
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this, image, ViewCompat.getTransitionName(image)).toBundle());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mGridState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RECYCLER_MOVIE_STATE, mGridState);
        outState.putInt("movieType", movieType);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            movieType = savedInstanceState.getInt("movieType");
            try {
                mRecyclerView = savedInstanceState.getParcelable(RECYCLER_MOVIE_STATE);
                mRecyclerView.getLayoutManager().onRestoreInstanceState(mGridState);
            } catch (ClassCastException e) {
                Log.e(TAG, "onRestoreInstanceState: " + e.toString());
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (movieType == 2) {
            getSupportLoaderManager().restartLoader(FAVORITE_MOVIES, null, this);
        }
    }

    private void getFavoritedMovie() {
        getSupportLoaderManager().restartLoader(FAVORITE_MOVIES, null, this);
        getSupportLoaderManager().initLoader(FAVORITE_MOVIES, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {
            PopupMenu popUp = new PopupMenu(this, findViewById(R.id.action_sort));
            popUp.getMenuInflater().inflate(R.menu.menu_sort, popUp.getMenu());
            popUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Log.i(TAG, "onItemSelected: " + item.getItemId());
                    setLoading();
                    page = 1;
                    movies.clear();
                    mRecyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.resetAdapter();
                    switch (item.getItemId()) {
                        case R.id.action_sort_popular:
                            movieType = 0;
                            getPopularMovie(page);
                            break;
                        case R.id.action_sort_top_rated:
                            movieType = 1;
                            getTopRatedMovie(page);
                            break;
                        case R.id.action_sort_favorite:
                            movieType = 2;
                            getFavoritedMovie();
                            break;
                    }
                    return true;
                }
            });
            popUp.show();
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) deliverResult(mMovieData);
                else forceLoad();
            }

            @Override
            public Cursor loadInBackground() {

                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                } catch (Exception e) {
                    Log.e(TAG, "loadInBackground: " + e.toString());
                    return null;
                }

            }

            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        MovieCursorAdapter adapter = new MovieCursorAdapter(data, MainActivity.this);
        mRecyclerView.setAdapter(adapter);
        setStopLoading();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onListItemClick(String movieId, String imageUrl, ImageView image) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        Log.i(TAG, "onListItemClick: id " + movieId);
        intent.setData(MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(movieId).build());
        intent.putExtra("image_url", imageUrl);
        startActivity(intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, image, ViewCompat.getTransitionName(image)).toBundle());
    }

    @Override
    public void onLoadMore() {
        Log.i(TAG, "onLoadMore: called");
        Log.i(TAG, "onLoadMore: called " + page);
        if (movieType == 0) {
            getPopularMovie(page);
        } else if (movieType == 1) {
            getTopRatedMovie(page);
        }
    }

    private void setLoading() {
        mProgressLoading.setVisibility(View.VISIBLE);
    }

    private void setStopLoading() {
        mProgressLoading.setVisibility(View.GONE);
    }
}

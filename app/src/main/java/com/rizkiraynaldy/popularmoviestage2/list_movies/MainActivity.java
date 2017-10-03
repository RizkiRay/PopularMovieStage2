package com.rizkiraynaldy.popularmoviestage2.list_movies;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.rizkiraynaldy.popularmoviestage2.R;
import com.rizkiraynaldy.popularmoviestage2.SpacesItemDecoration;
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
        MovieCursorAdapter.ListItemClickListener,
        AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FAVORITE_MOVIES = 100;
    private static final String RECYCLER_MOVIE_STATE = "movie_list_state";
    private RecyclerView mRecyclerView;
    private List<Movie> movies;
    private AppCompatSpinner mSpinnerSort;
    private Parcelable mGridState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movies = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_movie);
         mSpinnerSort = (AppCompatSpinner) findViewById(R.id.spinner_sort);


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        int spacingPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingPixels));

        mSpinnerSort.setOnItemSelectedListener(this);

        getPopularMovie();
    }

    private void getPopularMovie() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = service.getMoviePopular(ApiClient.API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse apiResponse = response.body();
                movies = apiResponse.getMoviePopulars();
                MovieAdapter adapter = new MovieAdapter(movies, MainActivity.this);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void getTopRatedMovie() {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = service.getMovieTopRated(ApiClient.API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse apiResponse = response.body();
                movies = apiResponse.getMoviePopulars();
                MovieAdapter adapter = new MovieAdapter(movies, MainActivity.this);
                mRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }

    //on poster click
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movie",movies.get(clickedItemIndex));
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mGridState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RECYCLER_MOVIE_STATE, mGridState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null){
            mRecyclerView = savedInstanceState.getParcelable(RECYCLER_MOVIE_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mGridState);
        }

    }



    //on spinner item selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemSelected: " + position);
        switch (position){
            case 0:
                getPopularMovie();
                break;
            case 1:
                getTopRatedMovie();
                break;
            case 2:
                getFavoritedMovie();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSpinnerSort.getSelectedItemPosition() == 2) {
            getSupportLoaderManager().restartLoader(FAVORITE_MOVIES, null, this);
        }
    }

    private void getFavoritedMovie() {
        getSupportLoaderManager().restartLoader(FAVORITE_MOVIES, null, this);
        getSupportLoaderManager().initLoader(FAVORITE_MOVIES, null, this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) deliverResult(mMovieData);
                else  forceLoad();
            }

            @Override
            public Cursor loadInBackground() {

                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, MovieContract.MovieEntry.COLUMN_MOVIE_ID);
                } catch (Exception e){
                    Log.e(TAG, "loadInBackground: " + e.toString() );
                    return null;
                }

            }
            public void deliverResult(Cursor data){
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        MovieCursorAdapter adapter = new MovieCursorAdapter(data, MainActivity.this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onListItemClick(String movieId) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        Log.i(TAG, "onListItemClick: id " + movieId);
        intent.setData(MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(movieId).build());
        startActivity(intent);
    }
}

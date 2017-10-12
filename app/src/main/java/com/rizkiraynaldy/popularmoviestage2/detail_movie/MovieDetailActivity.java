package com.rizkiraynaldy.popularmoviestage2.detail_movie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.rizkiraynaldy.popularmoviestage2.R;
import com.rizkiraynaldy.popularmoviestage2.data.MovieContract;
import com.rizkiraynaldy.popularmoviestage2.model.Movie;
import com.rizkiraynaldy.popularmoviestage2.model.MovieResponse;
import com.rizkiraynaldy.popularmoviestage2.model.Review;
import com.rizkiraynaldy.popularmoviestage2.model.ReviewsResponse;
import com.rizkiraynaldy.popularmoviestage2.model.Trailer;
import com.rizkiraynaldy.popularmoviestage2.model.TrailerResponse;
import com.rizkiraynaldy.popularmoviestage2.network.ApiClient;
import com.rizkiraynaldy.popularmoviestage2.network.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.rizkiraynaldy.popularmoviestage2.data.MovieContract.MovieEntry.COLUMN_DATE;
import static com.rizkiraynaldy.popularmoviestage2.data.MovieContract.MovieEntry.COLUMN_MOVIE_ID;
import static com.rizkiraynaldy.popularmoviestage2.data.MovieContract.MovieEntry.COLUMN_POSTER;
import static com.rizkiraynaldy.popularmoviestage2.data.MovieContract.MovieEntry.COLUMN_RATING;
import static com.rizkiraynaldy.popularmoviestage2.data.MovieContract.MovieEntry.COLUMN_SYNOPSIS;
import static com.rizkiraynaldy.popularmoviestage2.data.MovieContract.MovieEntry.COLUMN_TITLE;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerClickListener, View.OnClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerTrailers;
    private final static String TAG = MovieDetailActivity.class.getSimpleName();
    private RecyclerView mRecyclerReviews;
    private ImageView imageFavorite;
    private boolean isFavorited;
    private Movie mMovie = null;

    private String firstTrailer = "";

    private static final int CHECK_FAVORITE = 101;
    private ImageView imagePoster;
    private TextView textTitle;
    private TextView textReleaseDate;
    private TextView textSynopsis;
    private TextView textRate;
    private TextView textRate1;

    private String movieId;
    private NestedScrollView scrollView;

    private final String KEY_RECYCLER_TRAILER_STATE = "recycler_trailer_state";
    private final String KEY_RECYCLER_REVIEW_STATE = "recycler_review_state";
    private static Bundle mBundleRecyclerViewState;

    private int scrolY = 0;
    private int scrolX = 0;
    private Parcelable listState;
    private Parcelable listReviewState;
    private LinearLayoutManager mLinearLayout;
    private LinearLayoutManager mLayoutManagerReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        scrollView = (NestedScrollView) findViewById(R.id.scroll_view);
        imagePoster = (ImageView) findViewById(R.id.image_poster);
        textTitle = (TextView) findViewById(R.id.text_title);
        textReleaseDate = (TextView) findViewById(R.id.text_release_date);
        textSynopsis = (TextView) findViewById(R.id.text_synopsis);
        textRate1 = (TextView) findViewById(R.id.text_rate);

        imageFavorite = (ImageView) findViewById(R.id.image_favorite);
        imageFavorite.setOnClickListener(this);
        Picasso.with(this).load(R.drawable.ic_love_empty).fit().into(imageFavorite);

        mLinearLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerTrailers = (RecyclerView) findViewById(R.id.recycler_trailers);
        mRecyclerTrailers.setLayoutManager(mLinearLayout);

        mRecyclerTrailers.setNestedScrollingEnabled(false);

        mLayoutManagerReviews = new LinearLayoutManager(this);
        mRecyclerReviews = (RecyclerView) findViewById(R.id.recycler_reviews);
        mRecyclerReviews.setLayoutManager(mLayoutManagerReviews);

        mRecyclerReviews.setNestedScrollingEnabled(false);


        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerTrailers);

        imagePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.scrollTo(0, scrollView.getBottom());
                    }
                });
            }
        });

        if (getIntent().getData() == null) {
            mMovie = getIntent().getExtras().getParcelable("movie");
            ViewCompat.setTransitionName(imagePoster, getIntent().getExtras().getString("transition"));
            supportPostponeEnterTransition();
            Picasso.with(this).load(Movie.IMAGE_URL + mMovie.moviePoster).fit().noFade().centerCrop().into(imagePoster, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    supportStartPostponedEnterTransition();
                }

                @Override
                public void onError() {
                    supportStartPostponedEnterTransition();
                }
            });
            textTitle.setText(mMovie.originalTitle);
            textReleaseDate.setText(mMovie.releaseDate);
            textSynopsis.setText(mMovie.plotSynopsis);
            textRate1.setText(mMovie.userRating);

            movieId = mMovie.id + "";

        } else {
            Uri uri = getIntent().getData();
            movieId = uri.getPathSegments().get(1);
        }

        showTrailers(movieId + "");
        showReviews(movieId + "");

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                scrolX = scrollX;
                scrolY = scrollY;
                Log.i(TAG, "onScrollChange: Y " + scrolY);
            }
        });


        getSupportLoaderManager().initLoader(CHECK_FAVORITE, null, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.i(TAG, "onPause: y " + scrolY);
//        mBundleRecyclerViewState = new Bundle();
//        listState = mRecyclerTrailers.getLayoutManager().onSaveInstanceState();
//        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_TRAILER_STATE, listState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("SCROLL_POS", new int[]{scrollView.getScrollX(), scrollView.getScrollY()});

        listState = mLinearLayout.onSaveInstanceState();
        outState.putParcelable(KEY_RECYCLER_TRAILER_STATE, listState);

        listReviewState = mLayoutManagerReviews.onSaveInstanceState();
        outState.putParcelable(KEY_RECYCLER_REVIEW_STATE, listReviewState);

        Log.i(TAG, "onSaveInstanceState: Y " + listReviewState.describeContents());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray("SCROLL_POS");

        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(KEY_RECYCLER_TRAILER_STATE);
            mLinearLayout.onRestoreInstanceState(listState);

            listReviewState = savedInstanceState.getParcelable(KEY_RECYCLER_REVIEW_STATE);
            mLayoutManagerReviews.onRestoreInstanceState(listReviewState);

        }
        if (position != null)
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(position[0], position[1]);
                    Log.i(TAG, "run: Y " + position[1]);
                }
            });
    }

    private void favoriteState() {
        if (isFavorited) {
            Picasso.with(this).load(R.drawable.ic_love_empty).fit().into(imageFavorite);
            deleteFavorite();
            isFavorited = false;
        } else {
            Picasso.with(this).load(R.drawable.ic_love_filled).fit().into(imageFavorite);
            addFavorite();
            isFavorited = true;
        }
    }

    private int addFavorite() {

        if (mMovie == null) return 0;

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MOVIE_ID, mMovie.id);
        cv.put(COLUMN_TITLE, mMovie.originalTitle);
        cv.put(COLUMN_POSTER, mMovie.moviePoster);
        cv.put(COLUMN_DATE, mMovie.releaseDate);
        cv.put(COLUMN_RATING, mMovie.userRating);
        cv.put(COLUMN_SYNOPSIS, mMovie.plotSynopsis);

        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);

        if (uri == null) return 0;

        Log.i(TAG, "addFavorite: uri " + uri.toString());
        return 1;
    }

    private int deleteFavorite() {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(movieId + "").build();
        int data = getContentResolver().delete(uri, null, null);
        return data;
    }

    private void showTrailers(String movieId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<TrailerResponse> response = apiService.getMovieTrailers(movieId, ApiClient.API_KEY);
        response.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                TrailerResponse resp = response.body();
                List<Trailer> trailers = resp.getTrailers();
                firstTrailer = getString(R.string.youtube_base_url) + trailers.get(0).getYoutubeKey();
                TrailerAdapter adapter = new TrailerAdapter(trailers, MovieDetailActivity.this);
                mRecyclerTrailers.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }

    private void showReviews(String movieId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ReviewsResponse> call = apiService.getMovieReviews(movieId, ApiClient.API_KEY);
        call.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                List<Review> reviews = response.body().getMovieReviews();
                ReviewAdapter adapter = new ReviewAdapter(reviews);
                mRecyclerReviews.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_share) {
            if (!firstTrailer.isEmpty()) {
                shareFirstTrailer();
            } else {
                Toast.makeText(this, "No link to Share", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(String youtubeKey) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeKey));
        if (intent.resolveActivity(getPackageManager()) != null) startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.image_favorite) {
            favoriteState();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mFavoriteData = null;

            @Override
            protected void onStartLoading() {
                if (mFavoriteData != null) deliverResult(mFavoriteData);
                else forceLoad();
            }

            @Override
            public Cursor loadInBackground() {

                try {
                    return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI.buildUpon().appendPath(movieId + "").build(),
                            null, null, null, null);

                } catch (Exception e) {
                    Log.e(TAG, "loadInBackground: error " + e.toString());
                    return null;
                }

            }

            public void deliverResult(Cursor data) {
                mFavoriteData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() > 0) {
            Picasso.with(this).load(R.drawable.ic_love_filled).fit().into(imageFavorite);
            isFavorited = true;
            data.moveToFirst();
            Picasso.with(this)
                    .load(Movie.IMAGE_URL + data.getString(data.getColumnIndex(COLUMN_POSTER)))
                    .fit().centerInside()
                    .into(imagePoster);
            textTitle.setText(data.getString(data.getColumnIndex(COLUMN_TITLE)));
            textRate1.setText(data.getString(data.getColumnIndex(COLUMN_RATING)));
            textReleaseDate.setText(data.getString(data.getColumnIndex(COLUMN_DATE)));
            textSynopsis.setText(data.getString(data.getColumnIndex(COLUMN_SYNOPSIS)));

        } else {
            Picasso.with(this).load(R.drawable.ic_love_empty).fit().into(imageFavorite);
            isFavorited = false;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void shareFirstTrailer() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, firstTrailer);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Popular Movie Stage 2");
        startActivity(Intent.createChooser(intent, "Share"));
    }

}

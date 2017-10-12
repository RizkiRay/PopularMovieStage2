package com.rizkiraynaldy.popularmoviestage2.list_movies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.rizkiraynaldy.popularmoviestage2.R;
import com.rizkiraynaldy.popularmoviestage2.data.MovieContract;
import com.rizkiraynaldy.popularmoviestage2.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ray on 14/06/2017.
 */

public class MovieCursorAdapter extends RecyclerView.Adapter<MovieCursorAdapter.ViewHolder> {

    private static final String TAG = MovieCursorAdapter.class.getSimpleName();
    private Cursor mCursor;
    private Context context;
    private ListItemClickListener mOnClickListener;
    private String movieId;

    public MovieCursorAdapter(Cursor cursor, ListItemClickListener listener) {
        mCursor = cursor;
        mOnClickListener = listener;
    }

    public interface ListItemClickListener {
        void onListItemClick(String movieId, String imageUrl, ImageView image);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        mCursor.moveToPosition(position);
        String imagePoster = Movie.IMAGE_URL + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER));
        movieId = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));

        Log.i(TAG, "onBindViewHolder: id " + movieId);

        Picasso.with(context).load(imagePoster).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imagePoster);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imagePoster;

        public ViewHolder(View itemView) {
            super(itemView);
            imagePoster = (ImageView) itemView.findViewById(R.id.image_poster);
            ViewCompat.setTransitionName(imagePoster, context.getString(R.string.share));
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCursor.moveToPosition(getAdapterPosition());
            mOnClickListener.onListItemClick(mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID)), mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER)), imagePoster);
        }
    }
}

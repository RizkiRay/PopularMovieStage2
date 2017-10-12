package com.rizkiraynaldy.popularmoviestage2.list_movies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rizkiraynaldy.popularmoviestage2.R;
import com.rizkiraynaldy.popularmoviestage2.callback.OnLoadMoreListener;
import com.rizkiraynaldy.popularmoviestage2.data.MovieContract;
import com.rizkiraynaldy.popularmoviestage2.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ray on 14/06/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private List<Movie> movies;
    private Context context;
    private ListItemClickListener mOnClickListener;
    private int VIEW_TYPE_LOADING = 0;
    private int VIEW_TYPE_NORMAL = 1;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean isLoading;
    private boolean isFinished;
    private OnLoadMoreListener onLoadMoreListener;

    public MovieAdapter(List<Movie> movies, ListItemClickListener listener, RecyclerView recyclerView) {
        this.movies = movies;
        mOnClickListener = listener;

        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
//                    Log.i(TAG, "onScrolled: jalan");
                    totalItemCount = gridLayoutManager.getItemCount();
                    totalItemCount = totalItemCount <= 0 ? 20 : totalItemCount;
                    lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                    Log.i(TAG, "onScrolled: total ? lastvisibleitem + visibleThreshold " + totalItemCount + " ? " + lastVisibleItem + " + " + visibleThreshold + isLoading);

                    if (!isFinished && !isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold) && totalItemCount % 20 == 0) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                            Log.i(TAG, "onScrolled: called");
                        }
                        isLoading = true;
                    }
                }
            });
        }

    }

    public void resetAdapter() {
        isLoading = false;
        isFinished = false;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, ImageView image, Movie movie);
    }

    @Override
    public int getItemViewType(int position) {
        Log.i(TAG, "getItemViewType: posisi " + position + " " + isFinished);
        return (position == movies.size() - 1) && !isFinished ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_TYPE_NORMAL) {
            View v = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
            vh = new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    public void setLoading(boolean isLoading) {
        this.isLoading = false;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final Movie movie = movies.get(position);
            String imagePoster = Movie.IMAGE_URL + movie.moviePoster;
            final ImageView image = ((ViewHolder) holder).imagePoster;

            Picasso.with(context).load(imagePoster).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(((ViewHolder) holder).imagePoster);
            ((ViewHolder) holder).imagePoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onListItemClick(position, image, movie);
                }
            });
        } else {
            ((ProgressViewHolder) holder).pBar.setIndeterminate(true);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

//    @Override
//    public void onBindViewHolder(ViewHolder holder, final int position) {
//
//        Movie movie = movies.get(position);
//        String imagePoster = Movie.IMAGE_URL + movie.moviePoster;
//
//        Picasso.with(context).load(imagePoster).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imagePoster);
//
//        holder.imagePoster.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnClickListener.onListItemClick(position);
//            }
//        });
//    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagePoster;

        public ViewHolder(View itemView) {
            super(itemView);
            imagePoster = (ImageView) itemView.findViewById(R.id.image_poster);
            ViewCompat.setTransitionName(imagePoster, context.getString(R.string.share));
        }
    }

    public class ProgressViewHolder extends RecyclerView.ViewHolder {

        ProgressBar pBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            pBar = (ProgressBar) itemView.findViewById(R.id.pbar_load_more);
            Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show();
        }
    }

}

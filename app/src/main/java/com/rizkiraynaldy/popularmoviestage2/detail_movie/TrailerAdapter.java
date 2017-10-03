package com.rizkiraynaldy.popularmoviestage2.detail_movie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rizkiraynaldy.popularmoviestage2.R;
import com.rizkiraynaldy.popularmoviestage2.list_movies.MovieAdapter;
import com.rizkiraynaldy.popularmoviestage2.model.Trailer;
import com.rizkiraynaldy.popularmoviestage2.network.ApiClient;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ray on 30/07/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<Trailer> mTrailers;
    private Context mContext;
    private TrailerClickListener onClickListener;

    public TrailerAdapter(List<Trailer> mTrailers, TrailerClickListener onClickListener) {
        this.mTrailers = mTrailers;
        this.onClickListener = onClickListener;
    }


    public interface TrailerClickListener{
        void onListItemClick(String youtubeKey);
    };


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_movie_trailer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Trailer mTrailer = mTrailers.get(position);
        String thumbnailUrl = ApiClient.getYoutubeImageLink(mTrailer.getYoutubeKey());
        Picasso.with(mContext).load(thumbnailUrl).fit().into(holder.imageThumbnail);
        Picasso.with(mContext).load(R.drawable.ic_youtube).fit().into(holder.imagePlay);
        holder.imagePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onListItemClick(mContext.getString(R.string.youtube_base_url) + mTrailer.getYoutubeKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageThumbnail;
        ImageView imagePlay;

        public ViewHolder(final View itemView) {
            super(itemView);
            imageThumbnail = (ImageView) itemView.findViewById(R.id.image_trailer);
            imagePlay = (ImageView) itemView.findViewById(R.id.image_play);
        }
    }
}

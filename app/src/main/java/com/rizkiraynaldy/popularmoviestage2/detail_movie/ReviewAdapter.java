package com.rizkiraynaldy.popularmoviestage2.detail_movie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rizkiraynaldy.popularmoviestage2.R;
import com.rizkiraynaldy.popularmoviestage2.model.Review;

import java.util.List;

/**
 * Created by ray on 31/07/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Context mContext;
    private List<Review> reviews;

    public ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_movie_review, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.textAuthor.setText(review.getAuthorName());
        holder.textContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textAuthor;
        TextView textContent;
        public ViewHolder(View itemView) {
            super(itemView);
            textAuthor = (TextView) itemView.findViewById(R.id.text_author);
            textContent = (TextView) itemView.findViewById(R.id.text_content);
        }
    }
}

package com.anshaysaboo.socalbeach4life.Adapters;

import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anshaysaboo.socalbeach4life.Objects.Review;
import com.anshaysaboo.socalbeach4life.R;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> dataSet;

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView;
        final TextView dateView;
        final TextView reviewBodyView;
        final RatingBar ratingBar;
        final TextView recommendView;

        public ReviewViewHolder(View view) {
            super(view);

            nameView = view.findViewById(R.id.review_name);
            dateView = view.findViewById(R.id.review_date);
            reviewBodyView = view.findViewById(R.id.review_body);
            ratingBar = view.findViewById(R.id.review_rating);
            recommendView = view.findViewById(R.id.review_recommend);
        }
    }

    public ReviewAdapter(List<Review> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_cell, viewGroup, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder viewHolder, final int position) {

        Review r = dataSet.get(position);
        viewHolder.nameView.setText(r.getAuthor());
        viewHolder.reviewBodyView.setText(r.getBody());
        viewHolder.ratingBar.setRating((float) r.getRating());

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy HH:mm");
        viewHolder.dateView.setText(formatter.format(r.getDate()));

        if (r.getWouldRecommend().equals("Yes")) {
            viewHolder.recommendView.setText("Would recommend");
        } else if (r.getWouldRecommend().equals("No")) {
            viewHolder.recommendView.setText("Would not recommend");
        } else {
            viewHolder.recommendView.setText("Would maybe recommend");
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
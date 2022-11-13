package com.anshaysaboo.socalbeach4life;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Rating;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.anshaysaboo.socalbeach4life.Adapters.ReviewAdapter;
import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Managers.BeachManager;
import com.anshaysaboo.socalbeach4life.Objects.Beach;
import com.anshaysaboo.socalbeach4life.Objects.Review;
import com.google.api.Distribution;

import java.util.List;

public class ViewReviewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView noReviewsView;
    private LinearLayout summaryView;
    private TextView reviewCountView;
    private RatingBar ratingBar;

    private Beach beach;
    private List<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);

        recyclerView = findViewById(R.id.reviews_recycler_view);
        noReviewsView = findViewById(R.id.no_reviews_text_view);
        summaryView = findViewById(R.id.reviews_summary_view);
        reviewCountView = findViewById(R.id.view_reviews_count_view);
        ratingBar = findViewById(R.id.reviews_rating_bar);

        recyclerView.setLayoutManager(new LinearLayoutManager(ViewReviewsActivity.this));

        noReviewsView.setVisibility(View.GONE);
        summaryView.setVisibility(View.GONE);

        beach = getIntent().getParcelableExtra("beach");

        loadReviews();
    }

    private void loadReviews() {
        BeachManager.getReviewsForBeach(beach, new ResultHandler<List<Review>>() {
            @Override
            public void onSuccess(List<Review> data) {
                if (data.isEmpty()) {
                    noReviewsView.setVisibility(View.VISIBLE);
                }
                reviews = data;
                summaryView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(new ReviewAdapter(data, false));
                fillSummary();
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    void fillSummary() {
        int sum = 0;
        for (Review r: reviews) {
            sum += r.getRating();
        }

        double average = (double) sum / reviews.size();
        ratingBar.setRating((float) average);
        reviewCountView.setText(reviews.size() + " Reviews");
    }
}
package com.anshaysaboo.socalbeach4life;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.anshaysaboo.socalbeach4life.Adapters.ReviewAdapter;
import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Managers.BeachManager;
import com.anshaysaboo.socalbeach4life.Objects.Beach;
import com.anshaysaboo.socalbeach4life.Objects.Review;

import java.util.List;

public class ViewReviewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView noReviewsView;

    private Beach beach;
    private List<Review> reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);

        recyclerView = findViewById(R.id.reviews_recycler_view);
        noReviewsView = findViewById(R.id.no_reviews_text_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(ViewReviewsActivity.this));

        noReviewsView.setVisibility(View.GONE);

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
                recyclerView.setAdapter(new ReviewAdapter(data));
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
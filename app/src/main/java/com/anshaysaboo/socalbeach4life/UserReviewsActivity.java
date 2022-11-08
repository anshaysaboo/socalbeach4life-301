package com.anshaysaboo.socalbeach4life;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.anshaysaboo.socalbeach4life.Adapters.ReviewAdapter;
import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Managers.AccountManager;
import com.anshaysaboo.socalbeach4life.Managers.BeachManager;
import com.anshaysaboo.socalbeach4life.Objects.Beach;
import com.anshaysaboo.socalbeach4life.Objects.Review;

import java.util.List;

public class UserReviewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView noReviewsView;

    private ReviewAdapter adapter;
    private List<Review> reviews;

    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reviews);

        accountManager = new AccountManager(this);

        recyclerView = findViewById(R.id.reviews_recycler_view);
        noReviewsView = findViewById(R.id.no_reviews_text_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(UserReviewsActivity.this));

        noReviewsView.setVisibility(View.GONE);

        loadReviews();
    }

    private void loadReviews() {
        accountManager.getReviewsForUser(accountManager.getUserEmail(), new ResultHandler<List<Review>>() {
            @Override
            public void onSuccess(List<Review> data) {
                if (data.isEmpty()) {
                    noReviewsView.setVisibility(View.VISIBLE);
                }
                adapter = new ReviewAdapter(data, true);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Review r = (Review) view.getTag(R.string.tag_key);
                        confirmDelete(r);
                    }
                });
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void confirmDelete(Review review) {
        new AlertDialog.Builder(this)
                .setTitle("Delete review?")
                .setMessage("Are you sure you want to delete?")
                .setIcon(R.drawable.ic_baseline_delete_24)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteReview(review);
                    }
                })
                .setNeutralButton("No", null)
                .show();
    }

    private void deleteReview(Review review) {
        BeachManager.deleteReview(review, new ResultHandler<Review>() {
            @Override
            public void onSuccess(Review data) {
                loadReviews();
            }

            @Override
            public void onFailure(Exception e) {
                // TODO: Handle error
                e.printStackTrace();
            }
        });
    }
}
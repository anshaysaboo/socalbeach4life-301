package com.anshaysaboo.socalbeach4life;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Managers.AccountManager;
import com.anshaysaboo.socalbeach4life.Managers.BeachManager;
import com.anshaysaboo.socalbeach4life.Objects.Beach;
import com.anshaysaboo.socalbeach4life.Objects.Review;

import java.util.Date;

public class ReviewFormActivity extends AppCompatActivity {

    private TextView beachNameView;
    private RatingBar ratingBar;
    private RadioGroup recommendRadioGroup;
    private TextView reviewTextView;
    private CheckBox anonymousCheckbox;

    private Beach beach;

    private String recommendOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviewform);

        beachNameView = findViewById(R.id.review_beach_name);
        ratingBar = findViewById(R.id.review_rating_bar);
        recommendRadioGroup = findViewById(R.id.review_recommend_group);
        reviewTextView = findViewById(R.id.review_body_text);
        anonymousCheckbox = findViewById(R.id.review_anonymous_checkbox);

        this.beach = getIntent().getParcelableExtra("beach");

        beachNameView.setText("For " + beach.getName());

        recommendRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.radioButton) {
                    recommendOption = "Yes";
                } else if (i == R.id.radioButton2) {
                    recommendOption = "No";
                } else if (i == R.id.radioButton3) {
                    recommendOption = "Maybe";
                }
            }
        });
    }

    public void submitClicked(View view) {
        if (!validate()) return;

        AccountManager accountManager = new AccountManager(this);
        Review review = new Review(
                anonymousCheckbox.isChecked() ? "Anonymous" : accountManager.getUserName(),
                reviewTextView.getText().toString(),
                beach.getId(),
                recommendOption,
                ratingBar.getNumStars(),
                new Date(),
                beach.getName()
        );

        BeachManager.createReview(review, new ResultHandler<Review>() {
            @Override
            public void onSuccess(Review data) {

            }

            @Override
            public void onFailure(Exception e) {
                // TODO: Handle error
                e.printStackTrace();
            }
        });

    }

    private boolean validate() {
        boolean isValid = true;

        if (reviewTextView.getText().toString().isEmpty()) {
            reviewTextView.setError("This field is required.");
            isValid = false;
        }

        if (recommendOption == null) {
            Toast t = Toast.makeText(this, "Please select a recommendation option.", Toast.LENGTH_SHORT);
            t.show();
            isValid = false;
        }

        return isValid;
    }
}

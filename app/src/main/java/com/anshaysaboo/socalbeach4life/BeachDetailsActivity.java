package com.anshaysaboo.socalbeach4life;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.anshaysaboo.socalbeach4life.Objects.Beach;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class BeachDetailsActivity extends AppCompatActivity {

    private TextView titleTv;
    private TextView addressTv;
    private TextView reviewCountTv;
    private RatingBar ratingBar;
    private ImageView imageView;
    private Button restaurantsButton;

    private Beach beach;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beachdetails);

        // Setup views
        titleTv = (TextView) findViewById(R.id.beach_title_view);
        addressTv = (TextView) findViewById(R.id.beach_address_view);
        reviewCountTv = (TextView) findViewById(R.id.review_count_view);
        ratingBar = (RatingBar) findViewById(R.id.beach_rating_view);
        imageView = (ImageView) findViewById(R.id.beach_details_image_view);

        Intent intent = getIntent();
        // TODO: Handle possible error
        beach = intent.getParcelableExtra("beach");

        // Fill out view with beach information
        titleTv.setText(beach.getName());
        addressTv.setText(beach.getAddress());
        reviewCountTv.setText("(" + beach.getReviewCount() + ")");
        ratingBar.setRating((float) beach.getRating());

        Log.d("ABABABAB", beach.getName() + " " + beach.getYelpUrl());
        if (!beach.getImageUrl().isEmpty())
            Picasso.get().load(beach.getImageUrl()).into(imageView);

    }

    public void findRestaurantsSelected(View view) {
        Intent i = new Intent(BeachDetailsActivity.this, RestaurantsMapActivity.class);
        i.putExtra("location", beach.getLocation());
        startActivity(i);
    }

    public void addReviewSelected(View view) {
        Intent i = new Intent(BeachDetailsActivity.this, ReviewFormActivity.class);
        i.putExtra("beach", beach);
        startActivity(i);
    }
}

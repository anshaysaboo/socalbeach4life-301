package com.anshaysaboo.socalbeach4life;

import android.app.AlertDialog;
import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Managers.RestaurantManager;
import com.anshaysaboo.socalbeach4life.Objects.Restaurant;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class RestaurantDetailsActivity extends AppCompatActivity {

    private TextView nameView;
    private TextView addressView;
    private TextView reviewCountView;
    private RatingBar ratingBar;
    private ImageView imageView;
    private TextView descriptionView;
    private TextView priceView;

    Restaurant restaurant;
    Restaurant.Details details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurantdetails);

        nameView = findViewById(R.id.restaurant_title_view);
        addressView = findViewById(R.id.restaurant_address_view);
        reviewCountView = findViewById(R.id.restaurant_review_count_view);
        ratingBar = findViewById(R.id.restaurant_rating_view);
        imageView = findViewById(R.id.restaurant_details_image_view);
        descriptionView = findViewById(R.id.restaurant_description_view);
        priceView = findViewById(R.id.restaurant_details_price_view);

        restaurant = getIntent().getParcelableExtra("restaurant");
        Log.d("ABABABAB", restaurant.getAddress() + " " + restaurant.getName() + " " + restaurant.getId());
        // Fill out known restaurant information
        nameView.setText(restaurant.getName());
        addressView.setText(restaurant.getAddress());
        reviewCountView.setText("(" + restaurant.getReviewCount() + ")");
        ratingBar.setRating((float) restaurant.getRating());
        priceView.setText(restaurant.getPriceLevelString());

        if (!restaurant.getImageUrl().isEmpty())
            Picasso.get().load(restaurant.getImageUrl()).into(imageView);

        RestaurantManager.getRestaurantDetails(restaurant, new ResultHandler<Restaurant.Details>() {
            @Override
            public void onSuccess(Restaurant.Details data) {
                setDetails(data);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void setDetails(Restaurant.Details details) {
        this.details = details;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                descriptionView.setText(details.getDescription());
            }
        });
    }

    public void showHoursClicked(View view) {
        if (details == null) return;
        // display hours dialog
        String hoursString = details.getHours().size() > 0 ? String.join("\n\n", details.getHours()) : "No hours provided by owner.";
        new AlertDialog.Builder(this)
                .setTitle("Hours")
                .setMessage(hoursString)
                .setIcon(R.drawable.ic_baseline_access_time_24)
                .setPositiveButton("Done", null)
                .show();
    }

    public void showWebsiteClicked(View view) {
        if (details == null) return;
        String url = "";
        if (details.getWebsite().isEmpty()) {
            // Show the google maps view
            url = "https://www.google.com/maps/search/?api=1&query_place_id=" + restaurant.getId();
        } else {
            url = details.getWebsite();
        }
        Intent urlIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(urlIntent);
    }
}

package com.anshaysaboo.socalbeach4life;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Managers.BeachManager;
import com.anshaysaboo.socalbeach4life.Managers.RestaurantManager;
import com.anshaysaboo.socalbeach4life.Objects.Beach;
import com.anshaysaboo.socalbeach4life.Objects.ParkingLot;
import com.anshaysaboo.socalbeach4life.Objects.Restaurant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantsMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private CardView detailCard;
    private TextView nameView;
    private ImageView imageView;
    private RatingBar ratingBar;
    private TextView addressView;
    private TextView reviewCountView;
    private TextView priceView;

    private List<Button> distanceButtons = new ArrayList<>();

    private List<Restaurant> restaurants;
    private Map<Marker, Restaurant> markerToRestaurant = new HashMap<>();
    private Map<Integer, List<Restaurant>> restaurantCache = new HashMap<>();

    private Marker selectedMarker = null;
    private Circle circle = null;
    private Restaurant selectedRestaurant = null;

    private LatLng location;

    int currentRadius = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_map);

        // Setup views
        detailCard = (CardView) findViewById(R.id.restaurant_card_view);
        nameView = (TextView) findViewById(R.id.restaurant_card_title_view);
        imageView = (ImageView) findViewById(R.id.restaurant_card_image_view);
        addressView = (TextView) findViewById(R.id.restaurant_card_address_view);
        ratingBar = (RatingBar) findViewById(R.id.restaurant_card_rating);
        reviewCountView = (TextView) findViewById(R.id.restaurant_card_review_count_view);
        priceView = (TextView) findViewById(R.id.restaurant_card_pricing_view);

        distanceButtons.add((Button) findViewById(R.id.distance_button_1));
        distanceButtons.add((Button) findViewById(R.id.distance_button_2));
        distanceButtons.add((Button) findViewById(R.id.distance_button_3));

        // Add on click listeners for distance buttons
        for (int i = 0; i < 3; i ++) {
            Button b = distanceButtons.get(i);
            int finalI = i;
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    distanceButtonSelected(finalI);
                }
            });
        }

        // Initially hide detail card
        detailCard.setVisibility(View.GONE);

        // Set card view onClickListener
        detailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCardSelected();
            }
        });

        // Set up the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        location = new LatLng(33.542242, -117.784785);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Zoom into the beach location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        distanceButtonSelected(1);

        // Listen for marker selection, can be a parking lot marker or a restaurant marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (markerToRestaurant.containsKey(marker)) {
                    // Marker is for a restaurant
                    // Show the detail card for the given restaurant
                    Restaurant r = markerToRestaurant.get(marker);
                    selectedRestaurant = r;
                    showDetailsForRestaurant(r, marker);
                    marker.showInfoWindow();
                }
                return true;
            }
        });

        // Listen for any moves the user/code makes to the camera view of the map
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                // Check to ensure a marker is currently selected
                if (selectedMarker == null) {
                    return;
                }
                LatLng target = mMap.getCameraPosition().target;
                LatLng current = selectedMarker.getPosition();
                float[] result = new float[1];
                Location.distanceBetween(target.latitude, target.longitude, current.latitude, current.longitude, result);
                if (result[0] > 500) {
                    hideDetails();
                }
            }
        });
    }

    // The card was selected, show the current restaurant information
    public void onCardSelected() {
//        if (selectedRestaurant == null) return;
//        Intent i = new Intent(MapActivity.this, BeachDetailsActivity.class);
//        i.putExtra("restaurant", selectedRestaurant);
//        this.startActivity(i);
    }

    private void distanceButtonSelected(int index) {
        // Deselect all distance buttons
        for (Button b: distanceButtons) {
            b.setBackgroundColor(getColor(R.color.primary_orange));
            b.setTextColor(getColor(R.color.white));
        }
        // Select current distance button
        distanceButtons.get(index).setBackgroundColor(getColor(R.color.purple_500));
        distanceButtons.get(index).setTextColor(getColor(R.color.purple_500));
        currentRadius = (index + 1) * 1000;

        addRestaurantMarkersForRadius(currentRadius);
        drawCircle(currentRadius);
    }

    // Fills data in and displays the restaurant information card
    void showDetailsForRestaurant(Restaurant res, Marker marker) {
        nameView.setText(res.getName());
        reviewCountView.setText("(" + res.getReviewCount() + ")");
        addressView.setText(res.getAddress());
        ratingBar.setRating((float) res.getRating());
        priceView.setText(res.getPriceLevelString());

        if (!res.getImageUrl().isEmpty())
            Picasso.get().load(res.getImageUrl()).into(imageView);

        // Zoom camera onto marked restaurant
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(res.getLocation(), 17));

        selectedMarker = null;
        // Delay setting the selected marker to allow the camera time to focus
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                detailCard.setVisibility(View.GONE);
                selectedMarker = marker;
            }
        }, 1500);

        // Animate the card in if it is not visible
        if (detailCard.getVisibility() != View.VISIBLE) {
            detailCard.setVisibility(View.VISIBLE);
            TranslateAnimation anim = new TranslateAnimation(0, 0, 500, 0);
            anim.setDuration(500);
            anim.setFillAfter(true);
            detailCard.startAnimation(anim);
        }
    }

    // Hides the details card view for the given restaurant
    void hideDetails() {
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, 500);
        anim.setDuration(500);
        detailCard.startAnimation(anim);
        // Make card hidden after animation completes
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                detailCard.setVisibility(View.GONE);
            }
        }, 500);
        selectedMarker = null;
    }

    // Performs request to fetch nearby restaurant locations
    void addRestaurantMarkersForRadius(int radius) {
        // Remove all current markers
        for (Marker m: markerToRestaurant.keySet()) {
            m.remove();
        }
        markerToRestaurant = new HashMap<>();

        // Check the cache if we already loaded these restaurants
        if (restaurantCache.containsKey(radius)) {
            addMarkerForRestaurants(radius, restaurantCache.get(radius));
            return;
        }

        RestaurantManager.getRestaurantsNearLocation(location, radius, new ResultHandler<List<Restaurant>>() {
            @Override
            public void onSuccess(List<Restaurant> data) {
                addMarkerForRestaurants(radius, data);
                restaurantCache.put(radius, data);
            }

            @Override
            public void onFailure(Exception e) {
                // TODO: Handle error
                e.printStackTrace();
            }
        });
    }

    // Adds markers for restaurants, is in a separate method to run on main thread
    void addMarkerForRestaurants(int radius, List<Restaurant> restaurants) {
        this.restaurants = restaurants;
        markerToRestaurant = new HashMap<>();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                drawCircle(radius);
                for (Restaurant res: restaurants) {
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(res.getLocation())
                            .title(res.getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    );
                    markerToRestaurant.put(m, res);
                }
            }
        });
    }

    // Draws a circle with the given radius around the given location
    void drawCircle(int radius) {
        if (circle != null) circle.remove();
        circle = mMap.addCircle(new CircleOptions()
                .center(location)
                .radius(radius * 0.3048)
                .strokeColor(Color.RED)
        );
    }

    // Setup the initial map features
    void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Displays the route display screen with the given parameters
    void displayRouteToParkingLot(LatLng currentLocation, ParkingLot lot, Restaurant restaurant) {
        Intent intent = new Intent(getBaseContext(), RouteActivity.class);
        intent.putExtra("destination_name", lot.getName() + " at " + restaurant.getName());
        intent.putExtra("destination_location", lot.getLocation());
        intent.putExtra("origin_location", currentLocation);
        startActivity(intent);
    }
}
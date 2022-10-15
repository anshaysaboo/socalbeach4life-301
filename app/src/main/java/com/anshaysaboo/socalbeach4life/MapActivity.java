package com.anshaysaboo.socalbeach4life;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.location.GnssAntennaInfo;
import android.location.Location;
import android.media.Rating;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Managers.BeachManager;
import com.anshaysaboo.socalbeach4life.Objects.Beach;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient locationProviderClient;

    private CardView detailCard;
    private TextView nameView;
    private ImageView imageView;
    private RatingBar ratingBar;
    private TextView addressView;
    private TextView reviewCountView;

    private List<Beach> beaches;
    private Map<Marker, Beach> markerToBeach;

    private Marker selectedMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Setup views
        detailCard = (CardView) findViewById(R.id.beach_details_card);
        nameView = (TextView) findViewById(R.id.beach_title);
        imageView = (ImageView) findViewById(R.id.beach_image_view);
        addressView = (TextView) findViewById(R.id.address_text);
        ratingBar = (RatingBar) findViewById(R.id.card_rating_stars);
        reviewCountView = (TextView) findViewById(R.id.review_count_text);

        // Initially hide detail card
        detailCard.setVisibility(View.GONE);

        setupPermission();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                // Show the detail card for the given beach
                Beach b = markerToBeach.get(marker);
                showDetailsForBeach(b, marker);
                marker.showInfoWindow();
                return true;
            }
        });

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
                if (result[0] > 3000) {
                    hideDetails();
                }
            }
        });
        // Zoom map into current location of the user
        locationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                addBeachMarkersForLocation(loc);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 9));
            }
        });
    }

    void showDetailsForBeach(Beach beach, Marker marker) {
        nameView.setText(beach.getName());
        reviewCountView.setText("(" + beach.getReviewCount() + ")");
        addressView.setText(beach.getAddress());
        ratingBar.setRating((float) beach.getRating());

        if (!beach.getImageUrl().isEmpty())
            Picasso.get().load(beach.getImageUrl()).into(imageView);

        // Zoom camera onto marked beach
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(beach.getLocation(), 13));

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

    // Performs network request to fetch nearby beach locations
    void addBeachMarkersForLocation(LatLng location) {
        BeachManager.getBeachesNearLocation(location, new ResultHandler<List<Beach>>() {
            @Override
            public void onSuccess(List<Beach> data) {
                Log.d("ABABABA", "Got beaches");
                addMarkerForBeaches(data);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("ABABABA", "Failed to get beaches");
                e.printStackTrace();
            }
        });
    }

    // Adds markers for beaches, is in a separate method to run on main thread
    void addMarkerForBeaches(List<Beach> beaches) {
        this.beaches = beaches;
        markerToBeach = new HashMap<>();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (Beach beach: beaches) {
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(beach.getLocation())
                            .title(beach.getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))

                    );
                    markerToBeach.put(m, beach);
                }
            }
        });

    }

    // Setup the initial map features
    void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Request the permissions from the user
    void setupPermission() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                setupMap();
                                Log.d("ANSHAY", "PRECISE GRANTED");
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                Log.d("ANSHAY", "COARSE GRANTED");
                            } else {
                                Log.d("ANSHAY", "NOT GRANTED");
                            }
                        }
                );
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }
}
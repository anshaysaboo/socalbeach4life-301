package com.anshaysaboo.socalbeach4life;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Managers.BeachManager;
import com.anshaysaboo.socalbeach4life.Objects.Beach;
import com.anshaysaboo.socalbeach4life.Objects.ParkingLot;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient locationProviderClient;
    private LatLng userLocation;

    private CardView detailCard;
    private TextView nameView;
    private ImageView imageView;
    private RatingBar ratingBar;
    private TextView addressView;
    private TextView reviewCountView;

    private List<Beach> beaches;
    private Map<Marker, Beach> markerToBeach = new HashMap<>();
    private Map<Marker, ParkingLot> markerToLot = new HashMap<>();
    private Map<Beach, List<ParkingLot>> lotCache = new HashMap<>();

    private Marker selectedMarker = null;
    private Beach selectedBeach = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Setup views
        detailCard = (CardView) findViewById(R.id.route_details_card);
        nameView = (TextView) findViewById(R.id.beach_title);
        imageView = (ImageView) findViewById(R.id.beach_image_view);
        addressView = (TextView) findViewById(R.id.address_text);
        ratingBar = (RatingBar) findViewById(R.id.card_rating_stars);
        reviewCountView = (TextView) findViewById(R.id.review_count_text);

        // Initially hide detail card
        detailCard.setVisibility(View.GONE);

        // Set card view onClickListener
        detailCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCardSelected();
            }
        });

        setupPermission();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        // Listen for marker selection, can be a parking lot marker or a beach marker
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                // Check if marker is for a beach or parking lot
                if (markerToBeach.containsKey(marker)) {
                    // Marker is for a beach
                    // Show the detail card for the given beach
                    Beach b = markerToBeach.get(marker);
                    selectedBeach = b;
                    showDetailsForBeach(b, marker);
                    marker.showInfoWindow();
                } else {
                    marker.showInfoWindow();
                }
                return true;
            }
        });

        // Listen for selection on the parking window, can be a parking lot window or a beach window
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                // Check if the marker is for a parking lot
                if (markerToLot.containsKey(marker)) {
                    ParkingLot lot = markerToLot.get(marker);
                    displayRouteToParkingLot(userLocation, lot, selectedBeach);
                }
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
                if (result[0] > 3000) {
                    hideDetails();
                }
            }
        });
        // Zoom map into current location of the user
        locationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                userLocation = loc;
                addBeachMarkersForLocation(loc);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 9));
            }
        });
    }

    // The card was selected, show the current beach information
    public void onCardSelected() {
        if (selectedBeach == null) return;
        Intent i = new Intent(MapActivity.this, BeachDetailsActivity.class);
        i.putExtra("beach", selectedBeach);
        this.startActivity(i);
    }

    // Fills data in and displays the beach information card
    void showDetailsForBeach(Beach beach, Marker marker) {
        nameView.setText(beach.getName());
        reviewCountView.setText("(" + beach.getReviewCount() + ")");
        addressView.setText(beach.getAddress());
        ratingBar.setRating((float) beach.getRating());

        if (!beach.getImageUrl().isEmpty())
            Picasso.get().load(beach.getImageUrl()).into(imageView);

        // Zoom camera onto marked beach
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(beach.getLocation(), 14));

        // Remove existing markers for parking lots
        for (Marker m: markerToLot.keySet()) {
            m.remove();
        }
        markerToLot = new HashMap<>();

        // Display the parking lots for the newly selected beach
        showParkingLotsForBeach(beach);

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

    void showParkingLotsForBeach(Beach beach) {
        if (lotCache.containsKey(beach)) {
            addMarkersForParkingLots(lotCache.get(beach));
            return;
        }
        BeachManager.getParkingLotsNearLocation(beach.getLocation(), new ResultHandler<List<ParkingLot>>() {
            @Override
            public void onSuccess(List<ParkingLot> data) {
                lotCache.put(beach, data);
                addMarkersForParkingLots(data);
            }

            @Override
            public void onFailure(Exception e) {
                // TODO: Handle this failure
            }
        });
    }

    // Adds markers for beaches, is in a separate method to run on main thread
    void addMarkersForParkingLots(List<ParkingLot> lots) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (ParkingLot lot: lots) {
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(lot.getLocation())
                            .title(lot.getName())
                            .snippet("Tap for route")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    );
                    markerToLot.put(m, lot);
                }
            }
        });
    }

    // Hides the details card view for the given beach
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

    // Performs request to fetch nearby beach locations
    void addBeachMarkersForLocation(LatLng location) {
        BeachManager.getBeachesNearLocation(location, new ResultHandler<List<Beach>>() {
            @Override
            public void onSuccess(List<Beach> data) {
                addMarkerForBeaches(data);
            }

            @Override
            public void onFailure(Exception e) {
                // TODO: Handle this failure=
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
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // TODO: Handle location permission not granted
                            } else {
                                // TODO: Handle location permission not granted
                            }
                        }
                );
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    // Displays the route display screen with the given parameters
    void displayRouteToParkingLot(LatLng currentLocation, ParkingLot lot, Beach beach) {
        Intent intent = new Intent(getBaseContext(), RouteActivity.class);
        intent.putExtra("destination_name", lot.getName() + " at " + beach.getName());
        intent.putExtra("destination_location", lot.getLocation());
        intent.putExtra("origin_location", currentLocation);
        startActivity(intent);
    }
}
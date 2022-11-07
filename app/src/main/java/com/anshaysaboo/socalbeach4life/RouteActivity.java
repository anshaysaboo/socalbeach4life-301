package com.anshaysaboo.socalbeach4life;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Managers.RouteManager;
import com.anshaysaboo.socalbeach4life.Objects.Beach;
import com.anshaysaboo.socalbeach4life.Objects.ParkingLot;
import com.anshaysaboo.socalbeach4life.Objects.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.util.List;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private CardView detailsCard;
    private TextView durationView;
    private TextView distanceView;
    private TextView destinationView;
    private ProgressBar progressBar;
    private LinearLayout contentView;

    private LatLng origin;
    private LatLng destination;
    private String destinationName;
    private String method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        // TODO: Add checks to ensure that all fields are filled when intent is parsed

        // Initialize views
        detailsCard = (CardView) findViewById(R.id.route_details_card);
        durationView = (TextView) findViewById(R.id.duration_text);
        distanceView = (TextView) findViewById(R.id.distance_text);
        destinationView = (TextView) findViewById(R.id.destination_text);
        progressBar = (ProgressBar) findViewById(R.id.route_progress_bar);
        contentView = (LinearLayout) findViewById(R.id.route_card_content_view);

        // Get route points from intent
        Intent intent = getIntent();
        origin = (LatLng) intent.getParcelableExtra("origin_location");
        destination = (LatLng) intent.getParcelableExtra("destination_location");
        destinationName = intent.getStringExtra("destination_name");
        method = intent.getStringExtra("method");

        // Initialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.route_map);
        mapFragment.getMapAsync(this);

        // Hide route details card to start
        contentView.setVisibility(View.GONE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                // Add marker at the end point®
                mMap.addMarker(new MarkerOptions()
                        .title(destinationName)
                        .position(destination)
                );

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 9));

                calculateRoute(origin, destination, method);
            }
        });

    }

    void zoomToFitRoute(List<LatLng> steps, LatLng start, LatLng end) {
        // Calculate padding based on the bearing between start and end. If route is too vertical
        // we will need more padding to account for the details card and header
        float[] result = new float[3];
        Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, result);
        float bearing = result[1];
        int padding = 170;
        if (bearing < -150 || bearing > 150 || (bearing < 30 && bearing > -30)) {
            padding = 400;
        }
        // Zoom onto the start and end points
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(origin);
        builder.include(destination);
        for (LatLng step: steps) {
            builder.include(step);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), padding));
    }

    void calculateRoute(LatLng start, LatLng end, String mode) {
        RouteManager.calculateRoute(start, end, mode, new ResultHandler<Route>() {
            @Override
            public void onSuccess(Route route) {
                displayRoute(route);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    void displayRoute(Route route) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Add information to details card
                durationView.setText(route.getDurationString());
                distanceView.setText("(" + route.getDistanceString() + ")");
                String methodCap = method.substring(0, 1).toUpperCase() + method.substring(1);
                destinationView.setText(methodCap + " to " + destinationName);
                detailsCard.setVisibility(View.VISIBLE);
                // Adjust camera to display the full route
                zoomToFitRoute(route.getSteps(), origin, destination);
                // Hide the progress bar and show the content
                contentView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                // Draw the route line
                PolylineOptions lineOptions = new PolylineOptions()
                        .color(0xff4A89F3)
                        .jointType(JointType.ROUND)
                        .endCap(new RoundCap())
                        .width(13)
                        .addAll(route.getSteps());
                mMap.addPolyline(lineOptions);
            }
        });
    }

    public void openInGoogleMaps(View view) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.google.com")
                .path("/maps/dir/")
                .appendQueryParameter("api", "1")
                .appendQueryParameter("origin", origin.latitude + "," + origin.longitude)
                .appendQueryParameter("destination", destination.latitude + "," + destination.longitude)
                .appendQueryParameter("travelmode", method);
        Uri uri = builder.build();
        Intent i = new Intent(Intent.ACTION_VIEW, uri);
        this.startActivity(i);
    }

}
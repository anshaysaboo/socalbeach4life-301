package com.anshaysaboo.socalbeach4life.Managers;

import android.util.Log;

import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Objects.Deserializers;
import com.anshaysaboo.socalbeach4life.Objects.Keys;
import com.anshaysaboo.socalbeach4life.Objects.ParkingLot;
import com.anshaysaboo.socalbeach4life.Objects.Route;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RouteManager {

    private static final OkHttpClient client = new OkHttpClient();

    // Returns a route between a given start and end location, and a mode of transport
    public static void calculateRoute(LatLng origin, LatLng destination, String mode, ResultHandler<Route> handler) {
        // TODO: Add check to make sure mode provided is valid
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://maps.googleapis.com/maps/api/directions/json").newBuilder();
        urlBuilder.addQueryParameter("origin", origin.latitude + "," + origin.longitude);
        urlBuilder.addQueryParameter("destination", destination.latitude + "," + destination.longitude);
        urlBuilder.addQueryParameter("mode", mode);
        urlBuilder.addQueryParameter("key", Keys.GOOGLE_MAPS_API_KEY);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                handler.onFailure(e);
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        handler.onFailure(new IOException("Unexpected code " + responseBody.string()));
                        return;
                    }

                    JsonObject obj = JsonParser.parseString(responseBody.string()).getAsJsonObject();
                    JsonArray routesJson = obj.getAsJsonArray("routes");
                    JsonObject routeJson = routesJson.get(0).getAsJsonObject();

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    JsonDeserializer<Route> deserializer = new Deserializers.RouteDeserializer();
                    gsonBuilder.registerTypeAdapter(Route.class, deserializer);
                    Gson customGson = gsonBuilder.create();
                    Route route = customGson.fromJson(routeJson, Route.class);

                    handler.onSuccess(route);
                }
            }
        });
    }

    // Decodes an encoded polyline string and returns a list of points we can use to construct
    // the polyline
    public static List<LatLng> decode(final String encodedPath) {
        int len = encodedPath.length();
        final List<LatLng> path = new ArrayList<LatLng>();
        int index = 0;
        int lat = 0;
        int lng = 0;
        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);
            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);
            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }
        return path;
    }
}

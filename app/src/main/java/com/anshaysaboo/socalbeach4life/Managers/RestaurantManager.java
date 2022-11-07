package com.anshaysaboo.socalbeach4life.Managers;

import android.util.Log;

import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Objects.Deserializers;
import com.anshaysaboo.socalbeach4life.Objects.Keys;
import com.anshaysaboo.socalbeach4life.Objects.ParkingLot;
import com.anshaysaboo.socalbeach4life.Objects.Restaurant;
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

public class RestaurantManager {
    private static final OkHttpClient client = new OkHttpClient();

    // Uses the Google Maps API to find restaurants within a given radius in feet
    public static void getRestaurantsNearLocation(LatLng location, int radius, ResultHandler<List<Restaurant>> handler) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://maps.googleapis.com/maps/api/place/nearbysearch/json").newBuilder();
        urlBuilder.addQueryParameter("type", "restaurant");
        urlBuilder.addQueryParameter("location", location.latitude + "," + location.longitude);
        urlBuilder.addQueryParameter("key", Keys.GOOGLE_MAPS_API_KEY);
        urlBuilder.addQueryParameter("radius", "" + Math.floor(radius * 0.3048));
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
                    JsonArray restaurantsJson = obj.get("results").getAsJsonArray();

                    ArrayList<Restaurant> restaurants = new ArrayList<>();

                    for (int i = 0; i < Math.min(restaurantsJson.size(), 50); i ++) {
                        JsonObject resObj = restaurantsJson.get(i).getAsJsonObject();
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        JsonDeserializer<Restaurant> deserializer = new Deserializers.RestaurantDeserializer();
                        gsonBuilder.registerTypeAdapter(Restaurant.class, deserializer);
                        Gson customGson = gsonBuilder.create();
                        Restaurant res = customGson.fromJson(resObj, Restaurant.class);
                        restaurants.add(res);
                    }

                    handler.onSuccess(restaurants);
                }
            }
        });
    }
}

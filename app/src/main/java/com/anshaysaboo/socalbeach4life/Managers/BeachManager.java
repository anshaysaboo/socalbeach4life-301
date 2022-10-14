package com.anshaysaboo.socalbeach4life.Managers;

import android.util.Log;

import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Objects.Beach;
import com.anshaysaboo.socalbeach4life.Objects.Deserializers;
import com.anshaysaboo.socalbeach4life.Objects.Keys;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BeachManager {

    private static final OkHttpClient client = new OkHttpClient();

    public static void getBeachesNearLocation(LatLng location, ResultHandler<List<Beach>> handler) {
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.yelp.com/v3/businesses/search").newBuilder();
        urlBuilder.addQueryParameter("term", "beach");
        urlBuilder.addQueryParameter("categories", "beaches");
        urlBuilder.addQueryParameter("limit", "10");
        urlBuilder.addQueryParameter("latitude", location.latitude + "");
        urlBuilder.addQueryParameter("longitude", location.longitude + "");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + Keys.YELP_API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                handler.onFailure(e);
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    JsonObject obj = JsonParser.parseString(responseBody.string()).getAsJsonObject();
                    JsonArray beachesJson = obj.get("businesses").getAsJsonArray();

                    ArrayList<Beach> beaches = new ArrayList<>();

                    for (JsonElement beachJson: beachesJson) {
                        JsonObject beachObj = beachJson.getAsJsonObject();
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        JsonDeserializer<Beach> deserializer = new Deserializers.BeachDeserializer();
                        gsonBuilder.registerTypeAdapter(Beach.class, deserializer);
                        Gson customGson = gsonBuilder.create();
                        Beach beach = customGson.fromJson(beachObj, Beach.class);
                        beaches.add(beach);
                    }

                    handler.onSuccess(beaches);
                }
            }
        });
    }

}

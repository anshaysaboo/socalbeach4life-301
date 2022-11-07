package com.anshaysaboo.socalbeach4life.Objects;

import com.anshaysaboo.socalbeach4life.Managers.RouteManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Deserializers {

    public static class BeachDeserializer implements JsonDeserializer<Beach> {
        @Override
        public Beach deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            JsonObject coordinatesObject = jsonObject.getAsJsonObject("coordinates");
            LatLng loc = new LatLng(
                coordinatesObject.get("latitude").getAsDouble(),
                coordinatesObject.get("longitude").getAsDouble()
            );

            JsonArray locationObject = jsonObject.getAsJsonObject("location").getAsJsonArray("display_address");

            String address = "";
            for (int i = 0; i < locationObject.size(); i ++) {
                JsonElement line = locationObject.get(i);
                if (i == locationObject.size() - 1) {
                    address += line.getAsString();
                } else {
                    address += line.getAsString() + "\n";
                }
            }

            return new Beach(
                jsonObject.get("name").getAsString(),
                loc,
                jsonObject.get("rating").getAsDouble(),
                jsonObject.get("review_count").getAsInt(),
                address,
                jsonObject.get("image_url").getAsString(),
                jsonObject.get("id").getAsString(),
                jsonObject.get("url").getAsString()
            );
        }
    }

    public static class ParkingLotDeserializer implements JsonDeserializer<ParkingLot> {
        @Override
        public ParkingLot deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            JsonObject coordinatesObject = jsonObject.getAsJsonObject("geometry").getAsJsonObject("location");
            LatLng loc = new LatLng(
                    coordinatesObject.get("lat").getAsDouble(),
                    coordinatesObject.get("lng").getAsDouble()
            );

            return new ParkingLot(
                    jsonObject.get("name").getAsString(),
                    loc
            );
        }
    }

    public static class RouteDeserializer implements JsonDeserializer<Route> {
        @Override
        public Route deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            // TODO: Handle possible exceptions with different route types
            String polylineString = json.getAsJsonObject().getAsJsonObject("overview_polyline").get("points").getAsString();
            List<LatLng> steps = RouteManager.decode(polylineString);

            JsonObject legsJson = json.getAsJsonObject().getAsJsonArray("legs").get(0).getAsJsonObject();
            JsonObject distanceJson = legsJson.getAsJsonObject("distance");
            JsonObject durationJson = legsJson.getAsJsonObject("duration");

            return new Route(
                    durationJson.get("value").getAsInt(),
                    distanceJson.get("value").getAsInt(),
                    durationJson.get("text").getAsString(),
                    distanceJson.get("text").getAsString(),
                    steps
            );
        }
    }

    public static class RestaurantDeserializer implements JsonDeserializer<Restaurant> {
        @Override
        public Restaurant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            JsonObject coordinatesObject = jsonObject.getAsJsonObject("geometry").getAsJsonObject("location");
            LatLng loc = new LatLng(
                    coordinatesObject.get("lat").getAsDouble(),
                    coordinatesObject.get("lng").getAsDouble()
            );

            // TODO: Handle very possible crash here
            JsonArray photoArray = jsonObject.getAsJsonArray("photos");
            String photoRef = null;
            if (photoArray != null && photoArray.size() > 0) {
                JsonElement photoJson = photoArray.get(0);
                photoRef = photoJson.getAsJsonObject().get("photo_reference").getAsString();
            }

            int priceLevel = -1;
            if (jsonObject.get("price_level") != null) {
                priceLevel = jsonObject.get("price_level").getAsInt();
            }

            String address = jsonObject.get("vicinity").getAsString().replace(", ", "\n");

            return new Restaurant(
                    jsonObject.get("name").getAsString(),
                    jsonObject.get("place_id").getAsString(),
                    loc,
                    jsonObject.get("rating") != null ? jsonObject.get("rating").getAsDouble() : 0.0,
                    jsonObject.get("user_ratings_total") != null ? jsonObject.get("user_ratings_total").getAsInt() : 0,
                    address,
                    photoRef,
                    priceLevel
            );
        }
    }
    public static class RestaurantDetailsDeserializer implements JsonDeserializer<Restaurant.Details> {
        @Override
        public Restaurant.Details deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            String description = "";
            if (jsonObject.getAsJsonObject("editorial_summary") != null) {
                description = jsonObject.getAsJsonObject("editorial_summary").get("overview").getAsString();
            }

            List<String> hours = new ArrayList<>();
            JsonObject hoursObj = jsonObject.getAsJsonObject("current_opening_hours");
            if (hoursObj != null) {
                JsonArray hoursArray = hoursObj.getAsJsonArray("weekday_text");
                if (hoursArray != null) {
                    for (JsonElement el: hoursArray) {
                        hours.add(el.getAsString());
                    }
                }
            }

            return new Restaurant.Details(
                    description,
                    jsonObject.get("website") != null ? jsonObject.get("website").getAsString() : "",
                    hours
            );
        }
    }


}

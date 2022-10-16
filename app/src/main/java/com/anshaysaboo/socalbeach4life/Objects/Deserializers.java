package com.anshaysaboo.socalbeach4life.Objects;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

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

}

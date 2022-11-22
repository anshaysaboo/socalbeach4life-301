package com.anshaysaboo.socalbeach4life;

import static org.junit.Assert.assertEquals;

import com.anshaysaboo.socalbeach4life.Objects.Beach;
import com.anshaysaboo.socalbeach4life.Objects.Deserializers;
import com.anshaysaboo.socalbeach4life.Objects.ParkingLot;
import com.anshaysaboo.socalbeach4life.Objects.Restaurant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.Test;

import java.util.ArrayList;

public class DeserializerUnitTests {


    @Test
    public void testBeachDeserializer() {
        String sampleJson = "{\"id\": \"sMG1GbvluG4eM5hM2XiNIw\", \"alias\": \"half-moon-bay-state-beach-half-moon-bay-2\", \"name\": \"Half Moon Bay State Beach\", \"image_url\": \"https://s3-media2.fl.yelpcdn.com/bphoto/mH0lVWAA4D8kzGfrE6c_ig/o.jpg\", \"is_closed\": false, \"url\": \"https://www.yelp.com/biz/half-moon-bay-state-beach-half-moon-bay-2?adjust_creative=TrPJPd1jgnH-gOo_UfltTg&utm_campaign=yelp_api_v3&utm_medium=api_v3_business_search&utm_source=TrPJPd1jgnH-gOo_UfltTg\", \"review_count\": 331, \"categories\": [{\"alias\": \"beaches\", \"title\": \"Beaches\"}], \"rating\": 4.0, \"coordinates\": {\"latitude\": 37.466974, \"longitude\": -122.446977}, \"transactions\": [], \"location\": {\"address1\": \"95 Kelly Ave\", \"address2\": null, \"address3\": \"\", \"city\": \"Half Moon Bay\", \"zip_code\": \"94019\", \"country\": \"US\", \"state\": \"CA\", \"display_address\": [\"95 Kelly Ave\", \"Half Moon Bay, CA 94019\"]}, \"phone\": \"+16507268819\", \"display_phone\": \"(650) 726-8819\", \"distance\": 32433.02361611283}";
        JsonObject obj = JsonParser.parseString(sampleJson).getAsJsonObject();
        GsonBuilder gsonBuilder = new GsonBuilder();
        JsonDeserializer<Beach> deserializer = new Deserializers.BeachDeserializer();
        gsonBuilder.registerTypeAdapter(Beach.class, deserializer);
        Gson customGson = gsonBuilder.create();
        Beach beach = customGson.fromJson(obj, Beach.class);

        assertEquals(beach.getName(), "Half Moon Bay State Beach");
        assertEquals(beach.getId(), "sMG1GbvluG4eM5hM2XiNIw");
        assertEquals(beach.getAddress(), "95 Kelly Ave\nHalf Moon Bay, CA 94019");
        assertEquals(beach.getLocation().toString(), "lat/lng: (37.466974,-122.446977)");
        assertEquals(beach.getRating(), 4, 0.1);
        assertEquals(beach.getReviewCount(), 331);
        assertEquals(beach.getImageUrl(), "https://s3-media2.fl.yelpcdn.com/bphoto/mH0lVWAA4D8kzGfrE6c_ig/o.jpg");
    }

    @Test
    public void testParkingLotDeserializer() {
        String sampleJson = "{\"business_status\":\"OPERATIONAL\",\"geometry\":{\"location\":{\"lat\":33.9876493,\"lng\":-118.4734615},\"viewport\":{\"northeast\":{\"lat\":33.9889936802915,\"lng\":-118.4721194697085},\"southwest\":{\"lat\":33.9862957197085,\"lng\":-118.4748174302915}}},\"icon\":\"https:\\/\\/maps.gstatic.com\\/mapfiles\\/place_api\\/icons\\/v1\\/png_71\\/parking-71.png\",\"icon_background_color\":\"#7B9EB0\",\"icon_mask_base_uri\":\"https:\\/\\/maps.gstatic.com\\/mapfiles\\/place_api\\/icons\\/v2\\/parking_pinlet\",\"name\":\"46 Market St Parking\",\"place_id\":\"ChIJRdkzNLi6woARptGEtsh_2UU\",\"plus_code\":{\"compound_code\":\"XGQG+3J Los Angeles, CA, USA\",\"global_code\":\"8553XGQG+3J\"},\"reference\":\"ChIJRdkzNLi6woARptGEtsh_2UU\",\"scope\":\"GOOGLE\",\"types\":[\"parking\",\"point_of_interest\",\"establishment\"],\"vicinity\":\"46 Market Street, Los Angeles\"}";
        JsonObject obj = JsonParser.parseString(sampleJson).getAsJsonObject();
        GsonBuilder gsonBuilder = new GsonBuilder();
        JsonDeserializer<ParkingLot> deserializer = new Deserializers.ParkingLotDeserializer();
        gsonBuilder.registerTypeAdapter(ParkingLot.class, deserializer);
        Gson customGson = gsonBuilder.create();
        ParkingLot lot = customGson.fromJson(obj, ParkingLot.class);

        assertEquals(lot.getName(), "46 Market St Parking");
        assertEquals(lot.getLocation().toString(), "lat/lng: (33.9876493,-118.4734615)");
    }

    @Test
    public void testRestaurantDeserializer() {
        String sampleJson = "{\"business_status\":\"OPERATIONAL\",\"geometry\":{\"location\":{\"lat\":33.9880465,\"lng\":-118.4723601},\"viewport\":{\"northeast\":{\"lat\":33.9893428302915,\"lng\":-118.4710937697085},\"southwest\":{\"lat\":33.9866448697085,\"lng\":-118.4737917302915}}},\"icon\":\"https:\\/\\/maps.gstatic.com\\/mapfiles\\/place_api\\/icons\\/v1\\/png_71\\/restaurant-71.png\",\"icon_background_color\":\"#FF9E67\",\"icon_mask_base_uri\":\"https:\\/\\/maps.gstatic.com\\/mapfiles\\/place_api\\/icons\\/v2\\/restaurant_pinlet\",\"name\":\"Mao's Kitchen\",\"opening_hours\":{\"open_now\":true},\"photos\":[{\"height\":1344,\"html_attributions\":[\"<a href=\\\"https:\\/\\/maps.google.com\\/maps\\/contrib\\/101147649768885040689\\\">jessica schwabacher<\\/a>\"],\"photo_reference\":\"AW30NDweUwlNMWrxtVRmhUm5KQejwnT3s-aKy4c9QoL5dHIVEWfPR01GlBI-n8YIZPsR0-28bWOPwIi2h9FAqgrI_vtyqTg56vG9XOA5aYry2vi37NXj35x7OvfsM9mpbjvFSXRewa5Yvnjqh--IUTLqftmjtQVM8XFLkHMYsdesMCwlEUlB\",\"width\":1792}],\"place_id\":\"ChIJ3VPZU7i6woARdtKogDCfCrI\",\"plus_code\":{\"compound_code\":\"XGQH+63 Venice, Los Angeles, CA, USA\",\"global_code\":\"8553XGQH+63\"},\"price_level\":1,\"rating\":4.1,\"reference\":\"ChIJ3VPZU7i6woARdtKogDCfCrI\",\"scope\":\"GOOGLE\",\"types\":[\"restaurant\",\"food\",\"point_of_interest\",\"establishment\"],\"user_ratings_total\":609,\"vicinity\":\"1512 Pacific Avenue, Venice\"}";
        JsonObject obj = JsonParser.parseString(sampleJson).getAsJsonObject();
        GsonBuilder gsonBuilder = new GsonBuilder();
        JsonDeserializer<Restaurant> deserializer = new Deserializers.RestaurantDeserializer();
        gsonBuilder.registerTypeAdapter(Restaurant.class, deserializer);
        Gson customGson = gsonBuilder.create();
        Restaurant res = customGson.fromJson(obj, Restaurant.class);

        assertEquals(res.getName(), "Mao's Kitchen");
        assertEquals(res.getLocation().toString(), "lat/lng: (33.9880465,-118.4723601)");
        assertEquals(res.getImageUrl(), "https://maps.googleapis.com/maps/api/place/photo?photo_reference=AW30NDweUwlNMWrxtVRmhUm5KQejwnT3s-aKy4c9QoL5dHIVEWfPR01GlBI-n8YIZPsR0-28bWOPwIi2h9FAqgrI_vtyqTg56vG9XOA5aYry2vi37NXj35x7OvfsM9mpbjvFSXRewa5Yvnjqh--IUTLqftmjtQVM8XFLkHMYsdesMCwlEUlB&key=AIzaSyDTsg6YiefowXLGQc04QmsJ-AgIExQbK7A&maxwidth=1600");
        assertEquals(res.getRating(), 4.1, 0.1);
        assertEquals(res.getReviewCount(), 609);
        assertEquals(res.getAddress(), "1512 Pacific Avenue\nVenice");
        assertEquals(res.getPriceLevelString(), "$");

    }


}

package com.anshaysaboo.socalbeach4life;


import static org.junit.Assert.*;

import com.anshaysaboo.socalbeach4life.Objects.Restaurant;
import com.google.android.gms.maps.model.LatLng;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

public class RestaurantUnitTests {

    static Restaurant restaurant1;
    static Restaurant restaurant2;

    @BeforeClass
    public static void setup() {
        restaurant1 = new Restaurant(
                "Demo Restaurant",
                "ChIJ-dlc5bi6woAR_J0lbXPfhRw",
                new LatLng(0,0),
                4.5,
                10,
                "123 Address St",
                "DEMO_PHOTO_REF",
                2
        );

        restaurant2 = new Restaurant(
                "Demo Restaurant",
                "ChIJ-dlc5bi6woAR_J0lbXPfhRw",
                new LatLng(0,0),
                4.5,
                10,
                "123 Address St",
                "DEMO_PHOTO_REF",
                0
        );
    }

    @Test
    public void testRestaurantPriceLevelString() {
        assertEquals(restaurant1.getPriceLevelString(), "$$");
    }

    @Test
    public void testRestaurantEmptyPriceLevelString() {
        assertEquals(restaurant2.getPriceLevelString(), "");
    }

    @Test
    public void testRestaurantImageUrlString() {
        assertEquals(restaurant1.getImageUrl(), "https://maps.googleapis.com/maps/api/place/photo?photo_reference=DEMO_PHOTO_REF&key=AIzaSyDTsg6YiefowXLGQc04QmsJ-AgIExQbK7A&maxwidth=1600");
    }

}

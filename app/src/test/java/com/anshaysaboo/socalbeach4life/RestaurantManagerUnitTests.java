package com.anshaysaboo.socalbeach4life;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Managers.BeachManager;
import com.anshaysaboo.socalbeach4life.Managers.RestaurantManager;
import com.anshaysaboo.socalbeach4life.Objects.Beach;
import com.anshaysaboo.socalbeach4life.Objects.Restaurant;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.junit.jupiter.api.Timeout;

import java.lang.annotation.Retention;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RestaurantManagerUnitTests {

    @Test
    @Timeout(8000)
    public void testGetRestaurantsNearBeach() throws InterruptedException {
        CountDownLatch lock = new CountDownLatch(1);
        final List<Restaurant>[] res = new List[]{null};
        LatLng location = new LatLng(33.996416, -118.482648);
        RestaurantManager.getRestaurantsNearLocation(location, 1000, new ResultHandler<List<Restaurant>>() {
            @Override
            public void onSuccess(List<Restaurant> data) {
                res[0] = data;
                lock.countDown();
            }

            @Override
            public void onFailure(Exception e) {
                fail("Unexpected error occurred");
            }
        });

        lock.await(5000, TimeUnit.MILLISECONDS);

        assertNotNull(res[0]);
        assertTrue(res[0].size() > 0);
    }

    @Test
    @Timeout(8000)
    public void testGetRestaurantDetails() throws InterruptedException {
        CountDownLatch lock = new CountDownLatch(1);
        final Restaurant.Details[] res = new Restaurant.Details[]{null};

        Restaurant r = new Restaurant(
                "Demo Restaurant",
                "ChIJ-dlc5bi6woAR_J0lbXPfhRw",
                new LatLng(0,0),
                4.5,
                10,
                "Address",
                "ref",
                2
        );

        RestaurantManager.getRestaurantDetails(r, new ResultHandler<Restaurant.Details>() {
            @Override
            public void onSuccess(Restaurant.Details data) {
                res[0] = data;
                lock.countDown();
            }

            @Override
            public void onFailure(Exception e) {
                fail("Unexpected error occurred");
            }
        });

        lock.await(5000, TimeUnit.MILLISECONDS);

        assertNotNull(res[0]);
        assertEquals(res[0].getWebsite(), "http://www.venicecucina.com/");
    }

}

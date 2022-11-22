package com.anshaysaboo.socalbeach4life;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Managers.BeachManager;
import com.anshaysaboo.socalbeach4life.Managers.RouteManager;
import com.anshaysaboo.socalbeach4life.Objects.Beach;
import com.anshaysaboo.socalbeach4life.Objects.Route;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class RouteManagerUnitTests {

    @Test
    @Timeout(8000)
    public void testGetRoute() throws InterruptedException {
        CountDownLatch lock = new CountDownLatch(1);
        final Route[] res = new Route[]{null};
        LatLng origin = new LatLng(33.646542, -117.774591);
        LatLng destination = new LatLng(33.612292, -117.933263);
        RouteManager.calculateRoute(origin, destination, "driving", new ResultHandler<Route>() {
            @Override
            public void onSuccess(Route data) {
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
        assertTrue(res[0].getSteps().size() > 0);
        assertNotEquals(res[0].getDurationString(), "");
        assertNotEquals(res[0].getDistanceString(), "");
    }

}

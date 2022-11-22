package com.anshaysaboo.socalbeach4life;

import org.checkerframework.checker.units.qual.Time;
import org.junit.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.Assert.*;

import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Managers.AccountManager;
import com.anshaysaboo.socalbeach4life.Managers.BeachManager;
import com.anshaysaboo.socalbeach4life.Objects.Beach;
import com.anshaysaboo.socalbeach4life.Objects.ParkingLot;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BeachManagerUnitTests {

    @Test
    @Timeout(8000)
    public void testGetBeaches() throws InterruptedException {
        CountDownLatch lock = new CountDownLatch(1);
        final List<Beach>[] res = new List[]{null};
        LatLng location = new LatLng(34.023023, -118.282395);
        BeachManager.getBeachesNearLocation(location, new ResultHandler<List<Beach>>() {
            @Override
            public void onSuccess(List<Beach> data) {
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
    public void testGetParkingLots() throws InterruptedException {
        CountDownLatch lock = new CountDownLatch(1);
        final List<ParkingLot>[] res = new List[]{null};
        LatLng location = new LatLng(33.996416, -118.482648);
        BeachManager.getParkingLotsNearLocation(location, new ResultHandler<List<ParkingLot>>() {
            @Override
            public void onSuccess(List<ParkingLot> data) {
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
}
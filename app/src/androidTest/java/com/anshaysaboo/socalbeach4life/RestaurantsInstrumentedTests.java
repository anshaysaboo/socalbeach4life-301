package com.anshaysaboo.socalbeach4life;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.intent.Intents.*;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.util.Checks;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RestaurantsInstrumentedTests {

    UiDevice device;

    @Rule
    public ActivityScenarioRule<MapActivity> activityRule =
            new ActivityScenarioRule<>(MapActivity.class);

    @Before
    public void setup() throws InterruptedException, UiObjectNotFoundException {
        Intents.init();
        Thread.sleep(5000);
        device = UiDevice.getInstance(getInstrumentation());
        UiObject marker = device.findObject(new UiSelector().descriptionContains("Venice Beach"));
        marker.click();
        Thread.sleep(800);
        UiObject card = device.findObject(new UiSelector().textContains("1500 Ocean Front Walk"));
        card.click();
        Thread.sleep(800);
        device.findObject(new UiSelector().textContains("View Restaurants")).click();
        Thread.sleep(2000);
    }

    @After
    public void teardown() {
        Intents.release();
    }

    @Test
    public void testOpenRestaurants() throws InterruptedException, UiObjectNotFoundException {
        UiObject rMarker = device.findObject(new UiSelector().descriptionContains("Venice Cucina"));
        assertTrue(rMarker.exists());
    }

    @Test
    public void testClickRestaurantMarker() throws InterruptedException, UiObjectNotFoundException {
        UiObject rMarker = device.findObject(new UiSelector().descriptionContains("Venice Cucina"));
        rMarker.click();
        Thread.sleep(1000);
        UiObject card = device.findObject(new UiSelector().textContains("209 Windward Avenue"));
        assertTrue(card.exists());
    }

    @Test
    public void testOpenRestaurantDetails() throws InterruptedException, UiObjectNotFoundException {
        UiObject rMarker = device.findObject(new UiSelector().descriptionContains("Venice Cucina"));
        rMarker.click();
        Thread.sleep(1000);
        UiObject card = device.findObject(new UiSelector().textContains("209 Windward Avenue"));
        assertTrue(card.exists());
        intending(hasComponent(RestaurantDetailsActivity.class.getName()));
    }
}
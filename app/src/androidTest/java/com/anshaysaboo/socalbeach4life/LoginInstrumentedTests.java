package com.anshaysaboo.socalbeach4life;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.*;

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
public class LoginInstrumentedTests {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setup() throws InterruptedException {
        Intents.init();
        Thread.sleep(2000);
    }

    @After
    public void teardown() {
        Intents.release();
    }

    @Test
    public void testEmptyLogin() throws InterruptedException {
        onView(withText("Login")).perform(click());
        Thread.sleep(2000);
        onView(withId(R.id.Email)).check(matches(hasErrorText("Email is required")));
        onView(withId(R.id.Password)).check(matches(hasErrorText("Please enter a password")));
    }

    @Test
    public void testSuccessfulLogin() {
        onView(withId(R.id.Email)).perform(typeText("demo@gmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.Password)).perform(typeText("12345678"), ViewActions.closeSoftKeyboard());
        onView(withText("Login")).perform(click());
        intending(hasComponent(MapActivity.class.getName()));
    }

    @Test
    public void testOpenRegister() {
        onView(withText("Register")).perform(click());
        intending(hasComponent(RegisterActivity.class.getName()));
    }

    @Test
    public void testInvalidEmail() throws InterruptedException {
        Thread.sleep(500);
        onView(withId(R.id.Email)).perform(typeText("wrong"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.Password)).perform(typeText("wrong"), ViewActions.closeSoftKeyboard());
        onView(withText("Login")).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.Email)).check(matches(hasErrorText("Invalid email.")));
    }

    @Test
    public void testInvalidPassword() {
        onView(withId(R.id.Email)).perform(typeText("demo@gmail.com"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.Password)).perform(typeText("wrong"), ViewActions.closeSoftKeyboard());
        onView(withText("Login")).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.Password)).check(matches(hasErrorText("Invalid password.")));
    }
}
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
public class RegisterInstrumentedTests {

    @Rule
    public ActivityScenarioRule<RegisterActivity> activityRule =
            new ActivityScenarioRule<>(RegisterActivity.class);

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
    public void testOpenLogin() {
        onView(withText("Login")).perform(click());
        intending(hasComponent(LoginActivity.class.getName()));
    }

    @Test
    public void testEmptyRegister() throws InterruptedException {
        onView(withText("Register")).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.register_email)).check(matches(hasErrorText("Please enter a valid email.")));
        onView(withId(R.id.register_password)).check(matches(hasErrorText("Password should be at least 8 characters")));
    }

    @Test
    public void testPasswordsNotMatching() throws InterruptedException {
        Thread.sleep(500);
        onView(withId(R.id.register_name)).perform(typeText("John Doe"), ViewActions.closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.register_email)).perform(typeText("demo@gmail.com"), ViewActions.closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.register_password)).perform(typeText("wrongwrong"), ViewActions.closeSoftKeyboard());
        Thread.sleep(500);
        onView(withId(R.id.register_repeat_password)).perform(typeText("wrongwrong1"), ViewActions.closeSoftKeyboard());

        Thread.sleep(2000);

        onView(withText("Register")).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.register_repeat_password)).check(matches(hasErrorText("Passwords don't match.")));
    }

    @Test
    public void testInvalidEmail() throws InterruptedException {
        Thread.sleep(500);
        onView(withId(R.id.register_email)).perform(ViewActions.closeSoftKeyboard());
        Thread.sleep(1000);
        onView(withId(R.id.register_email)).perform(typeText("Invalid Email"), ViewActions.closeSoftKeyboard());
        Thread.sleep(2000);
        onView(withText("Register")).perform(click());
        onView(withId(R.id.register_email)).check(matches(hasErrorText("Please enter a valid email.")));
    }
}
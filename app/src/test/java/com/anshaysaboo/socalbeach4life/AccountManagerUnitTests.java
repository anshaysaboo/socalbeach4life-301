package com.anshaysaboo.socalbeach4life;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Managers.AccountManager;
import com.anshaysaboo.socalbeach4life.Objects.Beach;
import com.anshaysaboo.socalbeach4life.Objects.Exceptions;
import com.anshaysaboo.socalbeach4life.Objects.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class AccountManagerUnitTests {

    SharedPreferences sharedPrefs;

    @Before
    public void before() throws Exception {
        this.sharedPrefs = Mockito.mock(SharedPreferences.class);
    }

    @Test
    public void testIsLoggedIn() {
        Mockito.when(sharedPrefs.getString("user_name", "")).thenReturn("Anshay Saboo");
        AccountManager manager = new AccountManager(sharedPrefs);
        assertTrue(manager.isLoggedIn());
    }

    @Test
    public void testIsLoggedOut() {
        Mockito.when(sharedPrefs.getString("user_name", "")).thenReturn("");
        AccountManager manager = new AccountManager(sharedPrefs);
        assertFalse(manager.isLoggedIn());
    }

    @Test
    public void testGetUserName() {
        Mockito.when(sharedPrefs.getString("user_name", "")).thenReturn("Anshay Saboo");
        AccountManager manager = new AccountManager(sharedPrefs);
        assertTrue(manager.getUserName().equals("Anshay Saboo"));
    }

    @Test
    public void testGetEmail() {
        Mockito.when(sharedPrefs.getString("email", "")).thenReturn("demo@gmail.com");
        AccountManager manager = new AccountManager(sharedPrefs);
        assertTrue(manager.getUserEmail().equals("demo@gmail.com"));
    }

}
package com.anshaysaboo.socalbeach4life;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;

import com.anshaysaboo.socalbeach4life.Managers.AccountManager;
import com.anshaysaboo.socalbeach4life.Managers.BeachManager;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AccountManager accountManager = new AccountManager(this);

        Intent i;
        if (accountManager.isLoggedIn()) {
            i = new Intent(MainActivity.this, MapActivity.class);
        } else {
            i = new Intent(MainActivity.this, LoginActivity.class);
        }
        MainActivity.this.startActivity(i);
    }
}
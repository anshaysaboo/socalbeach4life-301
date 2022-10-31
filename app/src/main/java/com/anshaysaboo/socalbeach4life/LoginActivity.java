package com.anshaysaboo.socalbeach4life;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Managers.AccountManager;
import com.anshaysaboo.socalbeach4life.Objects.Exceptions;
import com.anshaysaboo.socalbeach4life.Objects.User;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button bLogin, bRegister;
    EditText etEmail, etPassword;
    AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountManager = new AccountManager(this);

        bLogin = findViewById(R.id.LoginButton);
        bRegister = findViewById(R.id.RegisterButton);

        etEmail = findViewById(R.id.Email);
        etPassword = findViewById(R.id.Password);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAllFields()) {
                    login();
                }
            }
        });

        /*bRegister.setOnClickListener(new View.OnClickListener() { // Uncomment this code after merging with aditya-signup
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                LoginActivity.this.startActivity(i);
            }
        });*/
    }

    private void login() {
        // TODO: Add some activity indicator here
        accountManager.loginUser(etEmail.getText().toString(), etPassword.getText().toString(), new ResultHandler<User>() {
            @Override
            public void onSuccess(User data) {
                Intent i = new Intent(LoginActivity.this, MapActivity.class);
                LoginActivity.this.startActivity(i);
            }

            @Override
            public void onFailure(Exception e) {
                // TODO: Handle the network error
                if (e instanceof Exceptions.LoginException) {
                    // TODO: Better way of handling these errors
                    if (e.getMessage().equals("Invalid email.")) {
                        etEmail.setError("Invalid email.");
                    } else if (e.getMessage().equals("Invalid password.")) {
                        etPassword.setError("Invalid password.");
                    }
                }
            }
        });
    }

    private boolean checkAllFields() {
        boolean isValid = true;

        if (etEmail.length() == 0){
            etEmail.setError("Email is required");
            isValid = false;
        }

        if (etPassword.length() == 0){
            etPassword.setError("Please enter a password");
            isValid = false;
        }

        return isValid;
    }
}
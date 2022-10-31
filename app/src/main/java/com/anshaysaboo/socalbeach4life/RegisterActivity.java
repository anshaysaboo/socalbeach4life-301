package com.anshaysaboo.socalbeach4life;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
;import com.anshaysaboo.socalbeach4life.Interfaces.ResultHandler;
import com.anshaysaboo.socalbeach4life.Managers.AccountManager;
import com.anshaysaboo.socalbeach4life.Objects.Exceptions;
import com.anshaysaboo.socalbeach4life.Objects.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    Button bLogin, bSignup;
    EditText etName, etEmail, etPassword, etRePassword;
    AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        accountManager = new AccountManager(this);

        bLogin = (Button) findViewById(R.id.button_login);
        bSignup = (Button) findViewById(R.id.button_register);

        etName = findViewById(R.id.Name);
        etEmail = findViewById(R.id.Email);
        etPassword = findViewById(R.id.Password);
        etRePassword = findViewById(R.id.PasswordRepeat);

        bSignup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (checkAllFields()) {
                   register();
               }
           }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(i);
            }
        });
    }

    private void register() {
        accountManager.registerUser(etName.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString(), new ResultHandler<User>() {
            @Override
            public void onSuccess(User data) {
                // TODO: Handle account creation more elegantly, maybe allow user to directly enter the app
                Toast toast = Toast.makeText(RegisterActivity.this, "Sign up successful! Log in to continue.", Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onFailure(Exception e) {
                // TODO: Handle other errors
                if (e instanceof Exceptions.EmailExistsException) {
                    etEmail.setError("An account with this email already exists.");
                }
            }
        });
    }

    private boolean checkAllFields() {
        boolean isValid = true;

        if (etName.length() == 0) {
            etName.setError("Name is required");
            isValid = false;
        }

        if (etEmail.length() == 0) {
            etEmail.setError("Email is required");
            isValid = false;
        }

        if (etPassword.length() == 0) {
            etPassword.setError("Please enter a password");
            isValid = false;
        }

        if (etPassword.length() < 8) {
            etPassword.setError("Password should be at least 8 characters");
            isValid = false;
        }

        if (!etPassword.getText().toString().equals(etRePassword.getText().toString())) {
            etRePassword.setError("Passwords don't match.");
            isValid = false;
        }

        if (!isValidEmailAddress(etEmail.getText().toString())) {
            etEmail.setError("Please enter a valid email.");
            isValid = false;
        }

        return isValid;
    }

    private static boolean isValidEmailAddress(String email) {
        Pattern p = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        Matcher m = p.matcher(email);
        return m.matches();
    }
}
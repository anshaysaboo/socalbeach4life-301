package com.anshaysaboo.socalbeach4life;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    Button bLogin, bRegister;
    EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bLogin = findViewById(R.id.LoginButton);
        bRegister = findViewById(R.id.RegisterButton);

        etEmail = findViewById(R.id.Email);
        etPassword = findViewById(R.id.Password);

        boolean allFieldsChecked;

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAllFields()){
                    System.out.print(true);
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

    private boolean checkAllFields(){
        if(etEmail.length() == 0){

            etEmail.setError("Email is required");
            return false;
        }

        if(etPassword.length() == 0){

            etPassword.setError("Please enter a password");
            return false;
        }

        return true;
    }
}
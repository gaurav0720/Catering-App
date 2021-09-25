package com.example.cateringapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(() -> {
            if ((mAuth.getCurrentUser() != null) && (mAuth.getCurrentUser().isEmailVerified())) {
                Toast.makeText(MainActivity.this, "Login Successfully!!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, AfterLoginActivity.class));
            } else {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, 2*1000);

    }
}
package com.example.cateringapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout userEmail, userPassword;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        RelativeLayout passToRegistration = findViewById(R.id.getRegistered);
        passToRegistration.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_pass);
        Button login = findViewById(R.id.btnLogin);
        login.setOnClickListener(v -> {
            String emailID = Objects.requireNonNull(userEmail.getEditText()).getText().toString().trim();
            String password = Objects.requireNonNull(userPassword.getEditText()).getText().toString().trim();

            if (emailID.isEmpty()){
                userEmail.setError("Email Address is required");
                return;
            }
            if (password.isEmpty() || password.length() < 6){
                userPassword.setError("Password must be greater than 6 characters");
                return;
            }

            progressDialog.setTitle("Catering App");
            progressDialog.setMessage("Please wait while getting you logged in..");
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(emailID, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    if (Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "Logged-In Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, AfterLoginActivity.class));
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Please first verify your account!!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Error:"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> LoginActivity.super.onBackPressed()).create().show();
    }

}
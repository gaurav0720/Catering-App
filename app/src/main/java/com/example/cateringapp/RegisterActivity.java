package com.example.cateringapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout fname, cname, idNumber, mno, eadd, password;
    private ProgressDialog progressDialog;
    private String userIDNo;

    private FirebaseAuth mAuth;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference myRef = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fname = findViewById(R.id.user_fullName);
        cname = findViewById(R.id.user_clgName);
        idNumber = findViewById(R.id.user_idNumber);
        mno = findViewById(R.id.user_mobNumber);
        eadd = findViewById(R.id.user_emailAdd);
        password = findViewById(R.id.user_password);
        Button getRegister = findViewById(R.id.registerUser);

        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        getRegister.setOnClickListener(v -> {
            String fullName = Objects.requireNonNull(fname.getEditText()).getText().toString().trim();
            String clgName = Objects.requireNonNull(cname.getEditText()).getText().toString().trim();
            String IDNo = Objects.requireNonNull(idNumber.getEditText()).getText().toString().trim();
            String mobNumber = Objects.requireNonNull(mno.getEditText()).getText().toString().trim();
            String emailAdd = Objects.requireNonNull(eadd.getEditText()).getText().toString().trim();
            String pass = Objects.requireNonNull(password.getEditText()).getText().toString().trim();

            if (fullName.isEmpty()){
                fname.setError("Name is required");
                return;
            }
            if (clgName.isEmpty()){
                cname.setError("College Name is required");
                return;
            }
            if (IDNo.isEmpty()){
                idNumber.setError("User ID Number is required");
                return;
            }
            if (mobNumber.isEmpty()){
                mno.setError("Mobile Number is required");
                return;
            }
            if (emailAdd.isEmpty()){
                eadd.setError("Email Address is required");
                return;
            }
            if (pass.isEmpty() || pass.length()<6){
                password.setError("Password must be greater than 6 characters");
                return;
            }

            progressDialog.setTitle("Registration Board");
            progressDialog.setMessage("Please wait while registration gets complete..");
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(emailAdd, pass)
                    .addOnCompleteListener(RegisterActivity.this, task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            Objects.requireNonNull(mAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()){
                                    userIDNo = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                    User user = new User(fullName,
                                            clgName,
                                            IDNo,
                                            mobNumber,
                                            emailAdd,
                                            pass,
                                            userIDNo,
                                            "default");

                                    myRef.child(userIDNo).setValue(user).addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegisterActivity.this, "Registered Successfully, Before login please verify your email!!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        finish();
                                    });
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Error:"+ task1.getException(),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Error:"+task.getException(),Toast.LENGTH_SHORT).show();
                        }
                    });

        });

    }
}
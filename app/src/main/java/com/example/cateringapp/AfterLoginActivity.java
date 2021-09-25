package com.example.cateringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AfterLoginActivity extends AppCompatActivity {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        FirebaseUser mAuth= FirebaseAuth.getInstance().getCurrentUser();
        String userID = mAuth.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CartItem").child(userID);
        DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference("MyOrders").child(userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                bottomNavigationView.getOrCreateBadge(R.id.cart).setBadgeTextColor(R.color.black);
                bottomNavigationView.getOrCreateBadge(R.id.cart).setNumber((int) count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        orderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();
                bottomNavigationView.getOrCreateBadge(R.id.orders).setBadgeTextColor(R.color.black);
                bottomNavigationView.getOrCreateBadge(R.id.orders).setNumber((int) count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(AfterLoginActivity.this);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure, you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {
                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            firebaseAuth.signOut();
                            startActivity(new Intent(AfterLoginActivity.this, LoginActivity.class));
                        })
                        .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            case R.id.help:
                Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;

        switch (item.getItemId()){
            case R.id.home:
                selectedFragment = new HomeFragment();
                break;
            case R.id.cart:
                selectedFragment = new CartFragment();
                break;
            case R.id.profile:
                selectedFragment = new ProfileFragment();
                break;
            case R.id.orders:
                selectedFragment = new OrdersFragment();
                break;
        }

        assert selectedFragment != null;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        return true;

    };


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AfterLoginActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    Intent a = new Intent(Intent.ACTION_MAIN);
                    a.addCategory(Intent.CATEGORY_HOME);
                    a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(a);
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }
}
package com.example.cateringapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeFragment extends Fragment {

    private RecyclerView homeRecyclerView;
    private HomeAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        homeRecyclerView = view.findViewById(R.id.homeRecyclerView);
        homeRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), 2));

        homeRecyclerView.setHasFixedSize(true);
        fireAdapter();

        return view;
    }

    private void fireAdapter() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("FoodItems");

        FirebaseRecyclerOptions<Home> options = new FirebaseRecyclerOptions.Builder<Home>()
                .setQuery(reference, Home.class).build();
        adapter = new HomeAdapter(options,this.getActivity());

        homeRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();

    }
}
package com.example.cateringapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrdersFragment extends Fragment {

    private RecyclerView myOrdersRecyclerView;
    private OrdersAdapter adapter;

    private DatabaseReference reference;
    private ImageView emptyOrder;
    private long ordersCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        myOrdersRecyclerView = view.findViewById(R.id.myOrdersRecyclerView);
        myOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        myOrdersRecyclerView.setHasFixedSize(true);

        emptyOrder = view.findViewById(R.id.ordersEmpty);

        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        assert mAuth != null;
        String userID = mAuth.getUid();
        reference = FirebaseDatabase.getInstance().getReference("MyOrders").child(userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ordersCount = ordersCount + 1;
                }
                if (ordersCount > 0){
                    myOrdersRecyclerView.setVisibility(View.VISIBLE);
                    emptyOrder.setVisibility(View.GONE);
                }else {
                    emptyOrder.setVisibility(View.VISIBLE);
                    myOrdersRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fireAdapter();

        return view;
    }

    private void fireAdapter() {

        FirebaseRecyclerOptions<MyOrders> options = new FirebaseRecyclerOptions.Builder<MyOrders>()
                .setQuery(reference, MyOrders.class).build();
        adapter = new OrdersAdapter(options,this.getActivity());

        myOrdersRecyclerView.setAdapter(adapter);
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
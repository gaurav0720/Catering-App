package com.example.cateringapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartFragment extends Fragment {

    private RecyclerView cartRecyclerList;
    private CartAdapter adapter;
    private TextView showFinalAmount;
    private final ArrayList<Integer> finalAmt = new ArrayList<>();
    private final ArrayList<String> cartItems = new ArrayList<>();
    private final ArrayList<String> cartItemCounts = new ArrayList<>();
    private final ArrayList<String> cartItemCountsAmount = new ArrayList<>();
    private DatabaseReference reference;
    private DatabaseReference myOrderReference;
    private long maxId=0;
    private ImageView emptyCart;
    private long cartCount = 0;

    private String userName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecyclerList = view.findViewById(R.id.cartRecyclerView);
        cartRecyclerList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        cartRecyclerList.setHasFixedSize(true);

        showFinalAmount = view.findViewById(R.id.finalCartAmount);
        TextView placeOrderBtn = view.findViewById(R.id.placeOrderButton);
        emptyCart = view.findViewById(R.id.cartEmpty);
        LinearLayout bottomPlaceOrderBar = view.findViewById(R.id.bottom_cart_final);

        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        assert mAuth != null;
        String userID = mAuth.getUid();

        reference = FirebaseDatabase.getInstance().getReference("CartItem").child(userID);

        myOrderReference = FirebaseDatabase.getInstance().getReference("MyOrders").child(userID);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;
                userName = user.getfName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    cartCount = cartCount + 1;
                }
                if (cartCount > 0){
                    cartRecyclerList.setVisibility(View.VISIBLE);
                    bottomPlaceOrderBar.setVisibility(View.VISIBLE);
                    emptyCart.setVisibility(View.GONE);
                }else {
                    emptyCart.setVisibility(View.VISIBLE);
                    cartRecyclerList.setVisibility(View.GONE);
                    bottomPlaceOrderBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fireCartAdapter();

        showAmountPlaceOrder();

        placeOrderBtn.setOnClickListener(view1 -> emptyCartPlaceOrder());

        return view;
    }

    private void fireCartAdapter() {
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(reference, Cart.class).build();
        adapter = new CartAdapter(options,this.getActivity());
        cartRecyclerList.setAdapter(adapter);
    }

    private void showAmountPlaceOrder() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                finalAmt.clear();
                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    String value = userSnapshot.child("itemAmount").getValue(String.class);
                    finalAmt.add(Integer.parseInt(value));
                }
                int sum=0;
                for(int i=0;i<finalAmt.size();i++){
                    sum = sum + finalAmt.get(i);
                }
                showFinalAmount.setText(String.valueOf(sum));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void emptyCartPlaceOrder() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItems.clear();
                finalAmt.clear();
                cartItemCounts.clear();
                cartItemCountsAmount.clear();

                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    String value = userSnapshot.child("itemAmount").getValue(String.class);
                    finalAmt.add(Integer.parseInt(value));
                }

                int sum=0;
                for(int i=0;i<finalAmt.size();i++){
                    sum = sum + finalAmt.get(i);
                }

                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    String value = userSnapshot.child("itemName").getValue(String.class);
                    cartItems.add(value);

                    String counts = userSnapshot.child("itemCount").getValue(String.class);
                    cartItemCounts.add(counts);

                    String amount = userSnapshot.child("itemAmount").getValue(String.class);
                    cartItemCountsAmount.add(amount);
                }

                if(cartItems.size() == 0){
                    Toast.makeText(getContext(), "There is no item in cart, order can't be placed.", Toast.LENGTH_SHORT).show();
                }else {
                    int finalSum = sum;
                    myOrderReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                                maxId = snapshot.getChildrenCount();
                                MyOrders myOrders = new MyOrders(
                                    "Order_"+(maxId+1),
                                    cartItems,
                                    cartItemCounts,
                                    cartItemCountsAmount,
                                    String.valueOf(finalSum),
                                    userName
                            );
                            myOrderReference.child("Order_"+(maxId+1)).setValue(myOrders).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), "Thank you for your order.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("AllOrders");
                    ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                                maxId = snapshot.getChildrenCount();
                                MyOrders myOrders = new MyOrders(
                                    "Order_"+(maxId+1),
                                    cartItems,
                                    cartItemCounts,
                                    cartItemCountsAmount,
                                    String.valueOf(finalSum),
                                    userName
                            );
                            ref1.child("Order_"+(maxId+1)).setValue(myOrders);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    snapshot.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
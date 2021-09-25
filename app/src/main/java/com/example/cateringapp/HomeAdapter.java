package com.example.cateringapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeAdapter extends FirebaseRecyclerAdapter<Home,HomeAdapter.MyViewHolder> {

    private final Context context;
    private DatabaseReference snackRef;

    public HomeAdapter(FirebaseRecyclerOptions<Home> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_view,parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull Home home) {
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        assert mAuth != null;
        String userID = mAuth.getUid();
        snackRef = FirebaseDatabase.getInstance().getReference("CartItem").child(userID);

        myViewHolder.itemName.setText(home.getItemName());
        myViewHolder.itemOPrice.setText(home.getItemOPrice()+" ₹");
        myViewHolder.itemPrice.setText(home.getItemPrice()+" ₹");
        Glide.with(context).load(home.getItemPhoto()).into(myViewHolder.itemPhoto);

        myViewHolder.itemPrice.setPaintFlags(myViewHolder.itemPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        String avail = home.getItemAvailability();
        if (avail.equals("Available")) {
            myViewHolder.addSToCart.setOnClickListener(view ->
                    snackRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.child(home.getItemName()).exists()) {
                                AddToCart itemData = new AddToCart(
                                        home.getItemName(),
                                        home.getItemOPrice(),
                                        home.getItemPrice(),
                                        home.getItemPhoto(),
                                        "1",
                                        home.getItemOPrice()
                                );
                                snackRef.child(home.getItemName()).setValue(itemData).addOnCompleteListener(task -> {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(context, "Some Error Occurred!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    }));
        }
        else{
            Toast.makeText(context, "It's temporarily unavailable!!", Toast.LENGTH_SHORT).show();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemName;
        private final TextView itemOPrice;
        private final TextView itemPrice;
        private final CircleImageView itemPhoto;
        private final TextView addSToCart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.showItemName);
            itemOPrice = itemView.findViewById(R.id.showItemOfferPrice);
            itemPrice = itemView.findViewById(R.id.showItemPrice);
            itemPhoto = itemView.findViewById(R.id.showItemImage);
            addSToCart = itemView.findViewById(R.id.addItemToCart);

        }
    }
}


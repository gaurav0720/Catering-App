package com.example.cateringapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CartAdapter extends FirebaseRecyclerAdapter<Cart,CartAdapter.MyViewHolder> {

    private final Context context;

    public CartAdapter(FirebaseRecyclerOptions<Cart> options, Context context) {
        super(options);
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_view,parent, false);
        return new CartAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, Cart cart) {

        holder.itemCartName.setText(cart.getItemName());
        holder.showItemAmount.setText(cart.getItemAmount());
        holder.itemCount.setText(cart.getItemCount());

        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        assert mAuth != null;
        String userID = mAuth.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CartItem").child(userID);

        holder.increaseOne.setOnClickListener(view -> {
            int value = Integer.parseInt(cart.getItemOPrice());
            int count = Integer.parseInt(cart.getItemCount());
            if (count>=5){
                Toast.makeText(context, "Maximum 5 count is allowed", Toast.LENGTH_SHORT).show();
            }else {
                count = count+1;
                value = value*count;
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put("itemCount", String.valueOf(count));
                hashMap.put("itemAmount", String.valueOf(value));
                reference.child(cart.getItemName()).updateChildren(hashMap);
            }
        });

        holder.decreaseOne.setOnClickListener(view -> {
            int value = Integer.parseInt(cart.getItemOPrice());
            int count = Integer.parseInt(cart.getItemCount());
            if (count==1){
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Remove Item");
                builder.setMessage("Are you sure, you want to remove "+cart.getItemName()+" from Cart?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", (dialog, id) -> {

                            Query query = reference.orderByChild("itemName").equalTo(cart.getItemName());
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                        dataSnapshot.getRef().removeValue();
                                        Toast.makeText(context, cart.getItemName()+" has been removed from cart", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        })
                        .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            }else {
                count = count-1;
                value = value*count;
                reference.child(cart.getItemName()).child("itemCount").setValue(String.valueOf(count));
                reference.child(cart.getItemName()).child("itemAmount").setValue(String.valueOf(value));
            }
        });

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemCartName;
        private final TextView showItemAmount;
        private final TextView increaseOne;
        private final TextView decreaseOne;
        private final TextView itemCount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemCartName = itemView.findViewById(R.id.showCartItemName);
            showItemAmount = itemView.findViewById(R.id.showItemAmount);
            increaseOne = itemView.findViewById(R.id.increaseByOne);
            decreaseOne = itemView.findViewById(R.id.decreaseByOne);
            itemCount = itemView.findViewById(R.id.showItemCount);
        }
    }
}

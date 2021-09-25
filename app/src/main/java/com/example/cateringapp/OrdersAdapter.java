package com.example.cateringapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrdersAdapter extends FirebaseRecyclerAdapter<MyOrders, OrdersAdapter.MyViewHolder> {

    private Context context;

    public OrdersAdapter(@NonNull FirebaseRecyclerOptions<MyOrders> options, Context context) {
        super(options);
        this.context = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull MyOrders myOrders) {
        myViewHolder.orderName.setText(myOrders.getOrderName());
        myViewHolder.orderTotalAmount.setText(myOrders.getTotalAmount()+" ₹");
        myViewHolder.showDetails.setOnClickListener(view -> {

            final DialogPlus dialogOne = DialogPlus.newDialog(Objects.requireNonNull(context))
                    .setGravity(Gravity.CENTER)
                    .setMargin(50, 0, 50, 0)
                    .setContentHolder(new ViewHolder(R.layout.order_details_view))
                    .setExpanded(false)
                    .create();

            View holderView = dialogOne.getHolderView();

            final TextInputLayout oName = holderView.findViewById(R.id.showOrderDetailsName);
            final TextInputLayout oAmount = holderView.findViewById(R.id.showOrderDetailsTotalAmount);
            final TextInputLayout oItems = holderView.findViewById(R.id.showOrderDetailsItems);

            oName.setEnabled(false);
            oAmount.setEnabled(false);
            oItems.setEnabled(false);

            Button backBtn = holderView.findViewById(R.id.backToMyOrders);

            String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("MyOrders").child(userID).child(myOrders.getOrderName());

            List<String> orderedItems = new ArrayList<>();
            List<String> orderedItemsCount = new ArrayList<>();
            List<String> orderedItemsCountsAmount = new ArrayList<>();

            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Objects.requireNonNull(oName.getEditText()).setText(myOrders.getOrderName());
                    Objects.requireNonNull(oAmount.getEditText()).setText(myOrders.getTotalAmount());

                    orderedItems.clear();
                    orderedItemsCount.clear();
                    orderedItemsCountsAmount.clear();

                    for (DataSnapshot dataSnapshot : snapshot.child("orderItems").getChildren()){
                        String itemNames = dataSnapshot.getValue(String.class);
                        orderedItems.add(itemNames);
                    }

                    for (DataSnapshot dataSnapshot : snapshot.child("orderItemCounts").getChildren()){
                        String itemNames = dataSnapshot.getValue(String.class);
                        orderedItemsCount.add(itemNames);
                    }

                    for (DataSnapshot dataSnapshot : snapshot.child("orderItemCountsAmount").getChildren()){
                        String itemNames = dataSnapshot.getValue(String.class);
                        orderedItemsCountsAmount.add(itemNames);
                    }

                    if (orderedItems.size()>0){
                        String items = "";
                        for (int i=0; i<orderedItems.size(); i++){
                            if ( i == (orderedItems.size()-1) )
                                items = items+orderedItems.get(i)+" x "+orderedItemsCount.get(i)+" = "+orderedItemsCountsAmount.get(i);
                            else
                                items = items+orderedItems.get(i)+" x "+orderedItemsCount.get(i)+" = "+orderedItemsCountsAmount.get(i)+"\n";
                        }
                        Objects.requireNonNull(oItems.getEditText()).setText(items);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            backBtn.setOnClickListener(view1 -> dialogOne.dismiss());

            dialogOne.show();
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myorders_view,parent, false);
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView orderName, orderTotalAmount;
        private Button showDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            orderName = itemView.findViewById(R.id.showOrderItemNumber);
            orderTotalAmount = itemView.findViewById(R.id.showOrderTotalAmount);
            showDetails = itemView.findViewById(R.id.showOrderDetails);

        }
    }
}

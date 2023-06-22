package com.example.trucksharing.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trucksharing.ItemClickListener;
import com.example.trucksharing.R;
import com.example.trucksharing.fragment.activity.OrdersActivity;
import com.example.trucksharing.model.Order;

import java.util.List;

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {
    private List<Order> orderList; // List of orders to be displayed
    private Context context; // The activity or application context

    public OrderRecyclerViewAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each row of the RecyclerView
        View itemView = LayoutInflater.from(context).inflate(R.layout.order_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the current order
        Order order = orderList.get(position);

        // Check if the order has an image name and update the ImageView accordingly
        if (order.getImageName().length() > 0) {
            int resourceId = context.getResources().getIdentifier(order.getImageName(), "drawable",
                    context.getPackageName());
            holder.imageView.setImageResource(resourceId);
        } else {
            int resourceId = context.getResources().getIdentifier("ic_good_placholder", "drawable",
                    context.getPackageName());
            holder.imageView.setImageResource(resourceId);
        }

        // Set the text fields
        holder.textViewName.setText(order.getReceiverName());
        holder.textViewPickupTime.setText(order.getPickupDate() + " " + order.getPickupTime());

        // Set a click listener for the order row
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (context instanceof OrdersActivity) {
                    Log.d(this.toString(), "Postition = " + String.valueOf(position));
                    ((OrdersActivity)context).showOrderDetails(orderList.get(position));
                }
            }
        });

//        holder.shareButton.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textViewName;
        TextView textViewPickupTime;
        ImageFilterButton shareButton;

        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewOrder);
            textViewName = itemView.findViewById(R.id.textViewReceiverName);
            textViewPickupTime = itemView.findViewById(R.id.textViewPickupTime);
//            shareButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {

            if (v == shareButton) {
                if (context instanceof OrdersActivity) {
                    ((OrdersActivity)context).shareContent(orderList.get(getAdapterPosition()));
                }
            } else {
                itemClickListener.onClick(v,getAdapterPosition());
            }
        }
    }
}
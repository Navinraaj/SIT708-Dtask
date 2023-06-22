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

// This class acts as the bridge between the RecyclerView and the data source (the Order list in this case).
public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {
    // This list holds the data that will be displayed in the RecyclerView.
    private List<Order> orderList;
    // The activity or application context is needed to retrieve resources and to
    // start new activities.
    private Context context;

    // Constructor receives the data source and context.
    public OrderRecyclerViewAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each row of the RecyclerView from the XML layout file.
        View itemView = LayoutInflater.from(context).inflate(R.layout.order_row, parent, false);
        // Return a new ViewHolder instance and pass the inflated layout to it.
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the current order that will be displayed in this row.
        Order order = orderList.get(position);

        // Check if the order has an image name and update the ImageView accordingly.
        // If not, set a placeholder image.
        if (order.getImageName().length() > 0) {
            int resourceId = context.getResources().getIdentifier(order.getImageName(), "drawable",
                    context.getPackageName());
            holder.imageView.setImageResource(resourceId);
        } else {
            int resourceId = context.getResources().getIdentifier("ic_good_placholder", "drawable",
                    context.getPackageName());
            holder.imageView.setImageResource(resourceId);
        }

        // Set the text fields with the data from the current order.
        holder.textViewName.setText(order.getReceiverName());
        holder.textViewPickupTime.setText(order.getPickupDate() + " " + order.getPickupTime());

        // Set a click listener for the order row.
        // When a row is clicked, the detailed view of the order will be shown in the
        // OrdersActivity.
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (context instanceof OrdersActivity) {
                    Log.d(this.toString(), "Postition = " + String.valueOf(position));
                    ((OrdersActivity) context).showOrderDetails(orderList.get(position));
                }
            }
        });

        // holder.shareButton.setOnClickListener((View.OnClickListener) this);
    }

    @Override
    public int getItemCount() {
        // Return the total count of items in the list.
        return orderList.size();
    }

    // This class holds the views that will be recycled and reused as the user
    // scrolls.
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textViewName;
        TextView textViewPickupTime;
        ImageFilterButton shareButton;

        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Get the views from the layout.
            imageView = itemView.findViewById(R.id.imageViewOrder);
            textViewName = itemView.findViewById(R.id.textViewReceiverName);
            textViewPickupTime = itemView.findViewById(R.id.textViewPickupTime);
            // shareButton.setOnClickListener(this);
            // Set the click listener for the entire row.
            itemView.setOnClickListener(this);
        }

        // This method is used to set a custom click listener.
        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {

            // Check which view was clicked.
            if (v == shareButton) {
                // If it was the shareButton, start the share content functionality.
                if (context instanceof OrdersActivity) {
                    ((OrdersActivity) context).shareContent(orderList.get(getAdapterPosition()));
                }
            } else {
                // If it was the row, call the custom click listener that was set.
                itemClickListener.onClick(v, getAdapterPosition());
            }
        }
    }
}

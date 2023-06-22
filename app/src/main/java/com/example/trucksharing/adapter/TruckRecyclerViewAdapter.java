package com.example.trucksharing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trucksharing.R;
import com.example.trucksharing.fragment.activity.HomeActivity;
import com.example.trucksharing.model.Truck;

import java.util.List;

// This class acts as the bridge between the RecyclerView and the data source (the Truck list in this case).
public class TruckRecyclerViewAdapter extends RecyclerView.Adapter<TruckRecyclerViewAdapter.ViewHolder> {

    // This list holds the data that will be displayed in the RecyclerView.
    private List<Truck> truckList;
    // The activity or application context is needed to retrieve resources and to
    // start new activities.
    private Context context;

    // Constructor receives the data source and context.
    public TruckRecyclerViewAdapter(List<Truck> truckList, Context context) {
        this.truckList = truckList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each row of the RecyclerView from the XML layout file.
        View itemView = LayoutInflater.from(context).inflate(R.layout.truck_row, parent, false);
        // Return a new ViewHolder instance and pass the inflated layout to it.
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the current truck that will be displayed in this row.
        Truck truck = truckList.get(position);
        // Get the resource id for the image from the truck object and set the image.
        int resourceId = context.getResources().getIdentifier(truck.getImageName(), "drawable",
                context.getPackageName());
        holder.imageView.setImageResource(resourceId);
        // Set the text fields with the data from the current truck.
        holder.textViewName.setText(truck.getName());
        holder.textViewStatus.setText(truck.getStatus());
    }

    @Override
    public int getItemCount() {
        // Return the total count of items in the list.
        return truckList.size();
    }

    // This class holds the views that will be recycled and reused as the user
    // scrolls.
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textViewName;
        TextView textViewStatus;
        ImageFilterButton shareButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Get the views from the layout.
            imageView = itemView.findViewById(R.id.imageViewTruck);
            textViewName = itemView.findViewById(R.id.textViewReceiverName);
            textViewStatus = itemView.findViewById(R.id.textViewPickupTime);
            shareButton = itemView.findViewById(R.id.truckRowSharingButton);

            // Set the click listener for the share button.
            shareButton.setOnClickListener((View.OnClickListener) this);
        }

        @Override
        public void onClick(View view) {
            // If the share button was clicked, start the share content functionality in the
            // HomeActivity.
            if (context instanceof HomeActivity) {
                ((HomeActivity) context).shareContent(truckList.get(getAdapterPosition()));
            }
        }
    }
}

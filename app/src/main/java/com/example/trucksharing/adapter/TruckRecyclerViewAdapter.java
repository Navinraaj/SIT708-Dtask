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

public class TruckRecyclerViewAdapter extends RecyclerView.Adapter<TruckRecyclerViewAdapter.ViewHolder> {


    private List<Truck> truckList; // List of trucks to be displayed
    private Context context; // The activity or application context

    public TruckRecyclerViewAdapter(List<Truck> truckList, Context context) {
        this.truckList = truckList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.truck_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the current truck
        Truck truck = truckList.get(position);
        int resourceId = context.getResources().getIdentifier(truck.getImageName(), "drawable",
                context.getPackageName());
        holder.imageView.setImageResource(resourceId);
        holder.textViewName.setText(truck.getName());
        holder.textViewStatus.setText(truck.getStatus());
    }

    @Override
    public int getItemCount() {
        return truckList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        ImageView imageView;
        TextView textViewName;
        TextView textViewStatus;
        ImageFilterButton shareButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewTruck);
            textViewName = itemView.findViewById(R.id.textViewReceiverName);
            textViewStatus = itemView.findViewById(R.id.textViewPickupTime);
            shareButton = itemView.findViewById(R.id.truckRowSharingButton);

            shareButton.setOnClickListener((View.OnClickListener) this);
        }

        @Override
        public void onClick(View view) {
            if (context instanceof HomeActivity) {
                ((HomeActivity)context).shareContent(truckList.get(getAdapterPosition()));
            }
        }
    }
}
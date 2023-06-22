package com.example.trucksharing.fragment.activity;

import static com.example.trucksharing.util.Util.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.trucksharing.R;
import com.example.trucksharing.adapter.TruckRecyclerViewAdapter;
import com.example.trucksharing.data.DatabaseHelper;
import com.example.trucksharing.model.Truck;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements  PopupMenu.OnMenuItemClickListener {


    int userId = 0; // Initialize User ID

    DatabaseHelper database; // DatabaseHelper instance

    RecyclerView recyclerView; // RecyclerView to display trucks
    TruckRecyclerViewAdapter recyclerViewAdapter; // Adapter for RecyclerView

    ImageFilterButton buttonAdd; // Button for adding trucks

    List<Truck> truckList = new ArrayList<>(); // List of trucks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Retrieve User ID from intent
        Intent intent = getIntent();
        userId = intent.getExtras().getInt(USER_ID);

        // Initialize DatabaseHelper
        database = new DatabaseHelper(this);

        // Fetch all trucks from the database
        truckList = database.fetchAllTrucks();

        // Initialize RecyclerView and its Adapter

        recyclerView = findViewById(R.id.recyclerTrucks);
        recyclerViewAdapter = new TruckRecyclerViewAdapter(truckList, this);
        recyclerView.setAdapter(recyclerViewAdapter);

        LinearLayoutManager topLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(topLayoutManager);

        buttonAdd = findViewById(R.id.buttonHomeAdd);
        buttonAdd.bringToFront();
    }
    // Method to handle option click events
    public void optionClick(View view) {
        PopupMenu popup = new PopupMenu(this, view); // Initialize PopupMenu
        popup.setOnMenuItemClickListener(this); // Set MenuItemClickListener
        MenuInflater inflater = popup.getMenuInflater(); // Get MenuInflater
        inflater.inflate(R.menu.menu_popup, popup.getMenu()); // Inflate menu
        popup.show(); // Show PopupMenu
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                return true;
            case R.id.account:
                Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                profileIntent.putExtra(USER_ID, userId);
                startActivity(profileIntent);
                return true;
            case R.id.order:
                Intent orderIntent = new Intent(HomeActivity.this, OrdersActivity.class);
                orderIntent.putExtra(USER_ID, userId);
                startActivity(orderIntent);
                return true;
            default:
                return false;
        }
    }


    // Method to handle add click events
    public void addClick(View view) {
        Intent newOrderIntent = new Intent(HomeActivity.this, NewOrderActivity.class);
        newOrderIntent.putExtra(USER_ID, userId);
        startActivity(newOrderIntent); // Start NewOrderActivity
    }

    // Method to share truck content
    public void shareContent(Truck truck) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, truck.shareContent());
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent); // Start Share Intent
    }

    // Handle onBackPressed events
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack(); // Pop the FragmentManager backstack
        }
    }
}
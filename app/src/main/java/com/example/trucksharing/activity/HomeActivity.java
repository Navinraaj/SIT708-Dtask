package com.example.trucksharing.activity;

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
import android.widget.Button;
import android.widget.PopupMenu;

import com.example.trucksharing.R;
import com.example.trucksharing.adapter.TruckRecyclerViewAdapter;
import com.example.trucksharing.data.DatabaseHelper;
import com.example.trucksharing.model.Truck;
import com.example.trucksharing.model.User;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements  PopupMenu.OnMenuItemClickListener {

    int userId = 0;

    DatabaseHelper database;

    RecyclerView recyclerView;
    TruckRecyclerViewAdapter recyclerViewAdapter;

    ImageFilterButton buttonAdd;

    List<Truck> truckList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        userId = intent.getExtras().getInt(USER_ID);

        database = new DatabaseHelper(this);

        truckList = database.fetchAllTrucks();

        recyclerView = findViewById(R.id.recyclerTrucks);
        recyclerViewAdapter = new TruckRecyclerViewAdapter(truckList, this);
        recyclerView.setAdapter(recyclerViewAdapter);

        LinearLayoutManager topLayoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(topLayoutManager);

        buttonAdd = findViewById(R.id.buttonHomeAdd);
        buttonAdd.bringToFront();
    }

    public void optionClick(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup, popup.getMenu());
        popup.show();
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

    public void addClick(View view) {
        Intent newOrderIntent = new Intent(HomeActivity.this, NewOrderActivity.class);
        newOrderIntent.putExtra(USER_ID, userId);
        startActivity(newOrderIntent);
    }

    public void shareContent(Truck truck) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, truck.shareContent());
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            this.finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
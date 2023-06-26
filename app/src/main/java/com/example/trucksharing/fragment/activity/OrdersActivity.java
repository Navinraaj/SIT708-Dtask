package com.example.trucksharing.fragment.activity;

import static com.example.trucksharing.util.Util.USER_ID;
import com.example.trucksharing.model.User;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.trucksharing.adapter.OrderRecyclerViewAdapter;
import com.example.trucksharing.R;
import com.example.trucksharing.data.DatabaseHelper;
import com.example.trucksharing.model.Order;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    int userId = 0;
    User user;

    DatabaseHelper database;

    TextView textViewEmpty;

    RecyclerView recyclerView;
    OrderRecyclerViewAdapter recyclerViewAdapter;

    List<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        userId = intent.getExtras().getInt(USER_ID);

        database = new DatabaseHelper(this);

        List<User> users = database.fetchAllUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId() == userId) {
                user = users.get(i);
            }
        }

        orderList = database.fetchAllOrders(userId);

        recyclerView = findViewById(R.id.recyclerTrucks);
        recyclerViewAdapter = new OrderRecyclerViewAdapter(orderList, this);
        recyclerView.setAdapter(recyclerViewAdapter);

        LinearLayoutManager topLayoutManager = new LinearLayoutManager(OrdersActivity.this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(topLayoutManager);

        if (orderList.size() == 0) {
            AlertDialog alertDialog = new AlertDialog.Builder(OrdersActivity.this).create();
            alertDialog.setTitle("");
            alertDialog.setMessage("Currently there is no Present Order for you.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        }
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
                Intent homeIntent = new Intent(OrdersActivity.this, HomeActivity.class);
                homeIntent.putExtra(USER_ID, userId);
                startActivity(homeIntent);
                return true;
            case R.id.account:
                Intent profileIntent = new Intent(OrdersActivity.this, ProfileActivity.class);
                profileIntent.putExtra(USER_ID, userId);
                startActivity(profileIntent);
                return true;
            case R.id.order:

                return true;
            default:
                return false;
        }
    }

    public void addClick(View view) {
        Intent newOrderIntent = new Intent(OrdersActivity.this, NewOrderActivity.class);
        newOrderIntent.putExtra(USER_ID, userId);
        startActivity(newOrderIntent);
    }

    public void showOrderDetails(Order order) {
        Intent intent = new Intent(OrdersActivity.this, MapViewActivity.class);
        intent.putExtra("ORDER", order);
        startActivity(intent);
    }

    public void shareContent(Order order) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, order.shareContent());
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
package com.example.trucksharing.activity;

import static com.example.trucksharing.util.Util.USER_ID;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trucksharing.R;
import com.example.trucksharing.adapter.TruckRecyclerViewAdapter;
import com.example.trucksharing.data.DatabaseHelper;
import com.example.trucksharing.model.Truck;
import com.example.trucksharing.model.User;

import java.util.ArrayList;
import java.util.List;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    DatabaseHelper database;

    int userId = 0;
    User user;

    TextView textViewUsername;
    TextView textViewFullName;
    TextView textViewPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textViewUsername = findViewById(R.id.profileTextViewUsernameValue);
        textViewFullName = findViewById(R.id.profileTextViewFullNameValue);
        textViewPhoneNumber = findViewById(R.id.profileTextViewPhoneNumberValue);

        database = new DatabaseHelper(this);

        Intent intent = getIntent();
        userId = intent.getExtras().getInt(USER_ID);

        List<User> users = database.fetchAllUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId() == userId) {
                user = users.get(i);
            }
        }

        textViewUsername.setText(user.getUsername());
        textViewFullName.setText(user.getFullName());
        textViewPhoneNumber.setText(user.getPhoneNumber());
    }

    public void logoutClick(View view) {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
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
                Intent homeIntent = new Intent(ProfileActivity.this, HomeActivity.class);
                homeIntent.putExtra(USER_ID, userId);
                startActivity(homeIntent);
                return true;
            case R.id.account:

                return true;
            case R.id.order:
                Intent orderIntent = new Intent(ProfileActivity.this, OrdersActivity.class);
                orderIntent.putExtra(USER_ID, userId);
                startActivity(orderIntent);
                return true;
            default:
                return false;
        }
    }
}
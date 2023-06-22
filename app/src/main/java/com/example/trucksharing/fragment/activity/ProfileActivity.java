package com.example.trucksharing.fragment.activity;

import static com.example.trucksharing.util.Util.USER_ID;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trucksharing.R;
import com.example.trucksharing.data.DatabaseHelper;
import com.example.trucksharing.model.User;

import java.util.List;

public class ProfileActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {


    DatabaseHelper database; // Database helper to interact with the database

    int userId = 0; // User ID
    User user; // User object

    TextView textViewUsername; // Display for username
    TextView textViewFullName; // Display for full name
    TextView textViewPhoneNumber; // Display for phone number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize TextViews
        textViewUsername = findViewById(R.id.profileTextViewUsernameValue);
        textViewFullName = findViewById(R.id.profileTextViewFullNameValue);
        textViewPhoneNumber = findViewById(R.id.profileTextViewPhoneNumberValue);

        // Initialize database
        database = new DatabaseHelper(this);

        // Get the user ID from the intent
        Intent intent = getIntent();
        userId = intent.getExtras().getInt(USER_ID);

        // Fetch all users from the database and find the user with the matching user ID
        List<User> users = database.fetchAllUsers();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId() == userId) {
                user = users.get(i);
            }
        }

        // Set the TextViews with user details
        textViewUsername.setText(user.getUsername());
        textViewFullName.setText(user.getFullName());
        textViewPhoneNumber.setText(user.getPhoneNumber());
    }

    // Log out and return to MainActivity when the logout button is clicked
    public void logoutClick(View view) {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }

    // Create a popup menu when the options button is clicked
    public void optionClick(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_popup, popup.getMenu());
        popup.show();
    }

    // Handle menu item clicks
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent homeIntent = new Intent(ProfileActivity.this, HomeActivity.class);
                homeIntent.putExtra(USER_ID, userId);
                startActivity(homeIntent);
                return true;
            case R.id.account:
                // Do nothing if account item is clicked as we're already on the ProfileActivity
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
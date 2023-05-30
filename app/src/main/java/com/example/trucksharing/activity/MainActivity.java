package com.example.trucksharing.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trucksharing.R;
import com.example.trucksharing.data.DatabaseHelper;
import com.example.trucksharing.model.Truck;
import com.example.trucksharing.util.Util;

import java.util.List;


import com.example.trucksharing.GeofenceBroadcastReceiver;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper database;

    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;
    private static final int REQUEST_FINE_LOCATION = 1234;
    EditText editTextUsername;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new DatabaseHelper(this);

        // Fetch all trucks from the database

        List<Truck> trucks = database.fetchAllTrucks();
        if (trucks.size() == 0) {
            // Add Trucks to database
            String[] imageNames = {"ford", "chevrolet", "hyundai", "nissan"};
            String[] names = {"Ford", "Chevrolet", "Hyundai", "Nissan"};
            String[] status = {
                    "3.5L EcoBoost® High-Output V6",
                    "6.5L EcoBoost® High-Performance",
                    "4.5L EcoBoost® High-Output V8",
                    "2.0L EcoBoost® High-Output V6"
            };
            for (int i = 0; i < imageNames.length; i++) {
                Truck truck = new Truck(imageNames[i], names[i], status[i]);
                database.insertTruck(truck);
            }
        }

           ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1234);

        // Bind EditTexts for user input
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextSignupPassword);
        // If the database is empty, populate it with initial data
        // Initialize geofencing client
        geofencingClient = LocationServices.getGeofencingClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        } else {
            createGeofences();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // Handle the result of permission request

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Check if the permission request was successful
        if (requestCode == 1234) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // The permission was granted, you can now proceed with geofencing operations
                createGeofences();
            } else {
                // The permission was denied, you should handle this case
                Toast.makeText(MainActivity.this, "Fine location permission required for geofencing", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void createGeofences() {
        // Create and add geofences

        GeofencingRequest geofencingRequest = createGeofence();

        try {
            geofencingClient.addGeofences(geofencingRequest, getGeofencePendingIntent())
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(MainActivity.this, "Geofence added", Toast.LENGTH_SHORT).show();
                        Log.i(MainActivity.this.toString(), "Geofence added successfully");
                    })
                    .addOnFailureListener(e -> {
                        String message = "Failed to add geofence";
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            switch (apiException.getStatusCode()) {
                                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                                    message = "Geofence not available";
                                    break;
                                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                                    message = "Too many Geofences";
                                    break;
                                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                                    message = "Too many pending intents";
                                    break;
                            }
                        }
                        Log.e(MainActivity.this.toString(), message, e);
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    });
        } catch (SecurityException e) {
            Log.e(MainActivity.this.toString(), "Fine location permission not granted", e);
            Toast.makeText(MainActivity.this, "Fine location permission not granted", Toast.LENGTH_SHORT).show();
        }
    }


    private GeofencingRequest createGeofence() {
        // Create a Geofence object

        Geofence geofence = new Geofence.Builder()
                .setRequestId("Deakin_Burwood_Campus")
                .setCircularRegion(-37.841265, 145.113075, 1000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(geofence);
        return builder.build();
    }



    private PendingIntent getGeofencePendingIntent() {
        // Get or create a PendingIntent for Geofence transitions

        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    public void loginClick(View view) {
        // Handle the login button click

        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("");
            alertDialog.setMessage("Please enter username and password!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
            return;
        }

        int userId = database.fetchUser(username, password);
        if (userId >= 0) {
            Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
            homeIntent.putExtra(Util.USER_ID, userId);
            startActivity(homeIntent);
        } else {
            Log.d(MainActivity.this.toString(), "The user does not exist");
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Oops!");
            alertDialog.setMessage("The user does not exist");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        }
    }

    public void signupClick(View view) {
        // Handle the signup button click
        Intent intent = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}
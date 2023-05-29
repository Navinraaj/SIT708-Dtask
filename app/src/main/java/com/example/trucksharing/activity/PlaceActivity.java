package com.example.trucksharing.activity;
import static com.example.trucksharing.util.Util.DROP_OFF_CODE;
import static com.example.trucksharing.util.Util.PICK_UP_CODE;

import com.example.trucksharing.model.Order;
import com.example.trucksharing.util.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.trucksharing.R;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class PlaceActivity extends AppCompatActivity {

    Boolean isPickupPlocation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        Intent intent = getIntent();
        isPickupPlocation = intent.getExtras().getBoolean("IS_PICKUP_LOCATION");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("Place Activity", "Place: " + place.getName() + ", " + place.getId());
                Intent intent = new Intent();
                if (isPickupPlocation) {
                    intent.putExtra("pickUpPlace", place);
                    setResult(PICK_UP_CODE, intent);
                } else {
                    intent.putExtra("dropOffPlace", place);
                    setResult(DROP_OFF_CODE, intent);
                }

                finish();
            }


            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("Place Activity", "An error occurred: " + status);
            }
        });


    }
}
package com.example.trucksharing.activity;

import static android.Manifest.permission.CALL_PHONE;
import static com.example.trucksharing.util.Util.ORDER_FARE;
import static com.example.trucksharing.util.Util.USER_ID;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.trucksharing.R;
import com.example.trucksharing.fragment.MapsFragment;
import com.example.trucksharing.model.Order;
import com.example.trucksharing.util.Constants;

import java.util.Arrays;

public class MapViewActivity extends FragmentActivity {

    ConstraintLayout mapViewContainer;
    TextView textViewPickupLocation;
    TextView textViewDropOffLocation;
    TextView textViewApproxFare;
    TextView textViewTravelTime;
    Order order;
    Double fare = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        Intent intent = getIntent();
        order = (Order)intent.getExtras().getSerializable("ORDER");

        textViewPickupLocation = (TextView) findViewById(R.id.textViewMapPickUpLocation);
        textViewPickupLocation.setText("Pickup location: " + order.getPickupLocation());

        textViewDropOffLocation = (TextView) findViewById(R.id.textViewMapDropOffLocation);
        textViewDropOffLocation.setText("Drop-off location: " + order.getDropOffLocation());

        int timeInMinute = order.getApproxTravelTime();

        textViewApproxFare = findViewById(R.id.textViewFare);
        fare = Double.valueOf(timeInMinute) * 2.0;
        textViewApproxFare.setText("Approx Fare: " + order.getApproxFare(fare));

        textViewTravelTime = findViewById(R.id.textViewTravelTime);
        textViewTravelTime.setText("Approx Travel Time: "+ order.getApproxTravelTimeInString(timeInMinute));

        mapViewContainer = findViewById(R.id.mapView);

        mapViewContainer.bringChildToFront(textViewPickupLocation);
        mapViewContainer.bringChildToFront(textViewDropOffLocation);

        Fragment fragment = MapsFragment.newInstance(order.getPickupLocation(), order.getPickupLatitude(), order.getPickupLongitude(), order.getDropOffLocation(), order.getDropOffLatitude(), order.getDropOffLongitude());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mapView, fragment).disallowAddToBackStack().commit();
    }

    public void callDriverClick(View view) {
        callDriver();
    }

    private void callDriver() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + order.getDriverPhoneNumber()));

        if (ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        } else {
            requestPermissions(new String[]{CALL_PHONE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0) { return; }
        if ((Arrays.asList(permissions).contains(CALL_PHONE)) && grantResults[0] == getPackageManager().PERMISSION_GRANTED) {
            this.callDriver();
        }
    }

    public void bookClick(View view) {
        Intent intent = new Intent(MapViewActivity.this, CheckOutActivity.class);
        intent.putExtra(USER_ID, order.getUserId());
        intent.putExtra(ORDER_FARE, fare);
        startActivity(intent);
    }
}
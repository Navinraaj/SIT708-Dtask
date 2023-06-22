package com.example.trucksharing.fragment.activity;

import static com.example.trucksharing.util.Util.ORDER_FARE;
import static com.example.trucksharing.util.Util.USER_ID;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trucksharing.R;
import com.example.trucksharing.util.ConfirmOrderViewModel;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.pay.PayClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.PaymentData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class CheckOutActivity extends AppCompatActivity {

    // User ID and fare initialized
    int userId = 0;
    Double fare = 0.0;

    private ConfirmOrderViewModel model;

    // Request code for adding to Google Wallet
    private static final int ADD_TO_GOOGLE_WALLET_REQUEST_CODE = 999;

    // Define variables for Google Pay button and text view
    private AppCompatButton googlePayButton;
    private TextView textViewTitle;

    // Handle potential conflict from calling loadPaymentData.
    ActivityResultLauncher<IntentSenderRequest> resolvePaymentForResult = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(),
            result -> {
                switch (result.getResultCode()) {
                    case Activity.RESULT_OK:
                        // Handle payment success
                        Intent resultData = result.getData();
                        if (resultData != null) {
                            PaymentData paymentData = PaymentData.getFromIntent(result.getData());
                            if (paymentData != null) {
                                handlePaymentSuccess(paymentData);
                            }
                        }
                        break;

                    case Activity.RESULT_CANCELED:
                        // Handle payment cancellation
                        break;
                }
            });

    // OnCreate method to initialize the Google Pay API
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        // Get User ID and fare from the intent

        Intent intent = getIntent();
        userId = intent.getExtras().getInt(USER_ID);
        fare = intent.getExtras().getDouble(ORDER_FARE);

        // Initialize Google Pay button and title text view

        googlePayButton = findViewById(R.id.buttonGooglePay);
        textViewTitle = findViewById(R.id.textViewGooglePayTitle);
        textViewTitle.setText("You will need to pay " + "$" + fare + ".\nPlease press the button below to finish your payment by Google Pay");

        // Check Google Pay availability
        model = new ViewModelProvider(this).get(ConfirmOrderViewModel.class);
        model.canUseGooglePay.observe(this, this::setGooglePayAvailable);
    }

    private void setGooglePayAvailable(boolean available) {
        if (available) {
            googlePayButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, R.string.google_pay_status_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    // Method to handle Google Pay availability
    public void requestPayment(View view) {


        // Disable the button to prevent multiple clicks
        googlePayButton.setClickable(false);

        // Calculate the total price in cents
        long totalPriceCents = (long) (fare * 100);

        // Create the payment task
        final Task<PaymentData> task = model.getLoadPaymentDataTask(totalPriceCents);


        task.addOnCompleteListener(completedTask -> {
            if (completedTask.isSuccessful()) {
                handlePaymentSuccess(completedTask.getResult());
            } else {
                Exception exception = completedTask.getException();
                if (exception instanceof ResolvableApiException) {
                    PendingIntent resolution = ((ResolvableApiException) exception).getResolution();
                    resolvePaymentForResult.launch(new IntentSenderRequest.Builder(resolution).build());

                } else if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    handleError(apiException.getStatusCode(), apiException.getMessage());

                } else {
                    handleError(CommonStatusCodes.INTERNAL_ERROR, "Unexpected non API" +
                            " exception when trying to deliver the task result to an activity!");
                }
            }

            // Re-enables the Google Pay payment button.
            googlePayButton.setClickable(true);
        });
    }

    // Method to request payment using Google Pay
    private void handlePaymentSuccess(PaymentData paymentData) {


        final String paymentInfo = paymentData.toJson();

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".

            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");
            Toast.makeText(
                    this, getString(R.string.payments_show_name, billingName),
                    Toast.LENGTH_LONG).show();

            // Logging token string.
            Log.d("Google Pay token", paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token"));

            // Back to Order List screen
            Intent orderIntent = new Intent(CheckOutActivity.this, OrdersActivity.class);
            orderIntent.putExtra(USER_ID, userId);
            startActivity(orderIntent);

        } catch (JSONException e) {
            Log.e("handlePaymentSuccess", "Error: " + e);
        }
    }

    private void handleError(int statusCode, @Nullable String message) {
        Log.e("loadPaymentData failed",
                String.format(Locale.getDefault(), "Error code: %d, Message: %s", statusCode, message));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TO_GOOGLE_WALLET_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK: {
                    Toast
                            .makeText(this, getString(R.string.add_google_wallet_success), Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                case RESULT_CANCELED: {
                    // Save canceled
                    break;
                }

                case PayClient.SavePassesResult.SAVE_ERROR: {
                    if (data != null) {
                        String apiErrorMessage = data.getStringExtra(PayClient.EXTRA_API_ERROR_MESSAGE);
                        handleError(resultCode, apiErrorMessage);
                    }
                    break;
                }

                default: handleError(
                        CommonStatusCodes.INTERNAL_ERROR, "Unexpected non API" +
                                " exception when trying to deliver the task result to an activity!"
                );
            }
        }
    }
}
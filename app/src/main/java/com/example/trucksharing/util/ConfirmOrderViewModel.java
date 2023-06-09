package com.example.trucksharing.util;

import com.example.trucksharing.util.*;
import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.pay.Pay;
import com.google.android.gms.pay.PayApiAvailabilityStatus;
import com.google.android.gms.pay.PayClient;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;

import org.json.JSONObject;

public class ConfirmOrderViewModel extends AndroidViewModel {

    // A Google Pay API interaction client.
    private final PaymentsClient paymentsClient;

    // A Google Wallet API interaction client.
    private final PayClient walletClient;

    // LiveData representing whether the user can utilize Google Pay
    private final MutableLiveData<Boolean> _canUseGooglePay = new MutableLiveData<>();

    // LiveData representing whether the user can store passes with Google Wallet
    private final MutableLiveData<Boolean> _canAddPasses = new MutableLiveData<>();

    public ConfirmOrderViewModel(@NonNull Application application) {
        super(application);
        paymentsClient = PaymentsUtil.initializePaymentsClient(application);
        walletClient = Pay.getClient(application);

        ascertainGooglePayUsage();
        ascertainPassAdditionCapabilityToGoogleWallet();
    }

    public final LiveData<Boolean> canUseGooglePay = _canUseGooglePay;
    public final LiveData<Boolean> canAddPasses = _canAddPasses;

    private void ascertainGooglePayUsage() {
        final JSONObject isReadyToPayJson = PaymentsUtil.readyToPayRequest();
        if (isReadyToPayJson == null) {
            _canUseGooglePay.setValue(false);
            return;
        }

        // The isReadyToPay call is asynchronous and returns a Task. We attach an
        // OnCompleteListener to be invoked when the result is known.
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString());
        Task<Boolean> task = paymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(
                completedTask -> {
                    if (completedTask.isSuccessful()) {
                        _canUseGooglePay.setValue(completedTask.getResult());
                    } else {
                        Log.w("isReadyToPay failed", completedTask.getException());
                        _canUseGooglePay.setValue(false);
                    }
                });
    }

    public Task<PaymentData> getLoadPaymentDataTask(final long priceCents) {
        JSONObject paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(priceCents);
        if (paymentDataRequestJson == null) {
            return null;
        }

        PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString());
        return paymentsClient.loadPaymentData(request);
    }

    private void ascertainPassAdditionCapabilityToGoogleWallet() {
        walletClient
                .getPayApiAvailabilityStatus(PayClient.RequestType.SAVE_PASSES)
                .addOnSuccessListener(
                        status -> _canAddPasses.setValue(status == PayApiAvailabilityStatus.AVAILABLE))
                // If the API is not available, the recommended options are:
                // 1) Conceal the save button
                // 2) Resort to a different Save Passes integration (like JWT link)
                // Remember that a user's eligibility might change later.

                // Google Play Services is outdated. Hence, API availability cannot be checked.
                .addOnFailureListener(exception -> _canAddPasses.setValue(false));
    }

    public void savePassesJwt(String jwtString, Activity activity, int requestCode) {
        walletClient.savePassesJwt(jwtString, activity, requestCode);
    }

    public void savePasses(String objectString, Activity activity, int requestCode) {
        walletClient.savePassesJwt(objectString, activity, requestCode);
    }

    // This is a test generic object created for API usage.
    public final String genericObjectJwt = "e240eXAiOiAiSldUIiwgImF2sZyI6ICJSUzI1NiIsICJraWQiOiAiMTY4M2VjZDA1MmU5NTgyZWZhNGU5YTQxNjVmYzE5N24jNmJlYTJhMCJ9.eyJpcdzbMiOiAid2FsbGV0LWxhfbi10b29sc0BhcHBzcG90LmdzZXJ2dbfWNlYWNjb3VudC5jb20iLCAiYXVkIjogImdvb2dsZSIsICJ0eXAiOiAic2F2ZXRvdsbFsbGV0IiwgImlhdCI6IDE2NTA1MzI2MjMsICJwYXlsb2FkIjogeyJnZW5lcmljT2JqZWN0cyI6IFt7ImdvkIjogIjMzODgwMDAwMDAwMjIwOTUxNzcuZjUyZDRhZjYtMjQxMS00ZDU5LWFlNDktNzg2ZDY3N2FkOTJiIn1dfX0.fYKw6fpLfwwNMi5OGr4ybOadvybuCU7RYjQhw-QM_Z71mfOyv2wFUzf6dKgpspJKQmkiaBWBr1L9n8jq8ZMfj6iOA_9_njfUe9GepCwVLC0nZBdbd2EqS3UrBYT7tEmk7W2-Cpy5FJFTt_eiqXBZgwa6vMw6e6mMp-GzSD5_ls39fjOPziboLyG-GDmph3f6UhBkjnUjYyY_FoYdlqkTkCWad7AFPcy-FbRyVDpIaHfVk4eYQi4Vzk0fwxaWWTfP3gSXXT6UJ9aFvaPYs0gnlV2WPVgGGKCMtYHFRGYX1t0WRpN2kbxfO5VuMKWJlz3TCnxp-9Axz-enuCgnq2cLvCk6Tw";
}

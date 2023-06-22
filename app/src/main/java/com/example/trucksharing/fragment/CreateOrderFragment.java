package com.example.trucksharing.fragment;

import static com.example.trucksharing.util.Util.DROP_OFF_LAT;
import static com.example.trucksharing.util.Util.DROP_OFF_LNG;
import static com.example.trucksharing.util.Util.PICKUP_LAT;
import static com.example.trucksharing.util.Util.PICKUP_LNG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.trucksharing.R;
import com.example.trucksharing.fragment.activity.NewOrderActivity;
import com.example.trucksharing.model.Order;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateOrderFragment extends Fragment {

    RadioGroup radioGroupGoodType;
    RadioButton radioButtonFurniture;
    RadioButton radioButtonDryFood;
    RadioButton radioButtonFood;
    RadioButton radioButtonBuildingMaterial;
    EditText editTextGoodTypeOther;

    EditText editTextWeight;
    EditText editTextWidth;
    EditText editTextLength;
    EditText editTextHeight;

    RadioGroup radioGroupVhicleType;
    RadioButton radioButtonTruck;
    RadioButton radioButtonVan;
    RadioButton radioButtonRefrigeratedTruck;
    RadioButton radioButtonMiniTruck;
    EditText editTextVhicleTypeOther;

    AppCompatButton buttonCreateOrder;

    String goodType = "";
    Double weight = 0.0;
    Double width = 0.0;
    Double length = 0.0;
    Double height = 0.0;
    String vhicleType = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String USER_ID = "USER_ID";
    private static final String RECEIVER_NAME = "RECEIVER_NAME";
    private static final String PICKUP_DATE = "PICKUP_DATE";
    private static final String PICKUP_TIME = "PICKUP_TIME";
    private static final String PICKUP_LOCATION = "PICKUP_LOCATION";
    private static final String DROP_OFF_LOCATION = "DROP_OFF_LOCATION";

    private static final String DRIVER_PHONE_NUMBER = "DRIVER_PHONE_NUMBER";


    // TODO: Rename and change types of parameters
    private int userId;
    private String receiverName;
    private String pickupDate;
    private String pickupTime;

    private String pickupLocation;
    private String dropOffLocation;

    private double pickupLat;
    private double pickupLng;

    private double dropOffLat;
    private double dropOffLng;

    private String driverPhoneNumber;

    public CreateOrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment CreateOrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateOrderFragment newInstance(int userId, String receiverName, String pickupDate, String pickupTime, String pickupLocation, double pickupLat, double pickupLng, String dropOffLocation, double dropOffLat, double dropOffLng, String driverPhoneNumber) {
        CreateOrderFragment fragment = new CreateOrderFragment();
        Bundle args = new Bundle();
        args.putInt(USER_ID, userId);
        args.putString(RECEIVER_NAME, receiverName);
        args.putString(PICKUP_DATE, pickupDate);
        args.putString(PICKUP_TIME, pickupTime);
        args.putString(PICKUP_LOCATION, pickupLocation);
        args.putString(DROP_OFF_LOCATION, dropOffLocation);

        args.putDouble(PICKUP_LAT, pickupLat);
        args.putDouble(PICKUP_LNG, pickupLng);

        args.putDouble(DROP_OFF_LAT, dropOffLat);
        args.putDouble(DROP_OFF_LNG, dropOffLng);

        args.putString(DRIVER_PHONE_NUMBER, driverPhoneNumber);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(USER_ID);
            receiverName = getArguments().getString(RECEIVER_NAME);
            pickupDate = getArguments().getString(PICKUP_DATE);
            pickupTime = getArguments().getString(PICKUP_TIME);

            pickupLocation = getArguments().getString(PICKUP_LOCATION);
            pickupLat = getArguments().getDouble(PICKUP_LAT);
            pickupLng = getArguments().getDouble(PICKUP_LNG);

            dropOffLocation = getArguments().getString(DROP_OFF_LOCATION);
            dropOffLat = getArguments().getDouble(DROP_OFF_LAT);
            dropOffLng = getArguments().getDouble(DROP_OFF_LNG);

            driverPhoneNumber = getArguments().getString(DRIVER_PHONE_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        radioGroupGoodType = view.findViewById(R.id.radioGroupGoodType);
        radioButtonFurniture = view.findViewById(R.id.radioFurniture);
        radioButtonDryFood = view.findViewById(R.id.radioDryGood);
        radioButtonFood = view.findViewById(R.id.radioDryGood);
        radioButtonBuildingMaterial = view.findViewById(R.id.radioBuildingMaterial);

        radioGroupGoodType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {
                    goodType = checkedRadioButton.getText().toString();
                }
            }
        });


        editTextGoodTypeOther = view.findViewById(R.id.editTextNewOrderOtherGoodType);
        editTextGoodTypeOther.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                radioButtonFurniture.setChecked(false);
                radioButtonDryFood.setChecked(false);
                radioButtonFood.setChecked(false);
                radioButtonBuildingMaterial.setChecked(false);

                String newText = s.toString();
                goodType = newText;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


        radioGroupVhicleType = view.findViewById(R.id.radioVhicleType);
        radioButtonTruck = view.findViewById(R.id.radioTruck);
        radioButtonVan = view.findViewById(R.id.radioVan);
        radioButtonRefrigeratedTruck = view.findViewById(R.id.radioRefrigeratedTruck);
        radioButtonMiniTruck = view.findViewById(R.id.radioMiniTruck);

        radioGroupVhicleType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked) {
                    vhicleType = checkedRadioButton.getText().toString();
                }
            }
        });

        editTextVhicleTypeOther = view.findViewById(R.id.editTextNewOrderOtherVhicle);
        editTextVhicleTypeOther.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                radioButtonTruck.setChecked(false);
                radioButtonVan.setChecked(false);
                radioButtonRefrigeratedTruck.setChecked(false);
                radioButtonMiniTruck.setChecked(false);

                String newText = s.toString();
                vhicleType = newText;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        editTextWeight = view.findViewById(R.id.editTextNewOrderWeight);
        editTextWidth = view.findViewById(R.id.editTextNewOrderWidth);
        editTextLength = view.findViewById(R.id.editTextNewOrderLength);
        editTextHeight = view.findViewById(R.id.editTextNewOrderHeight);

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        buttonCreateOrder = view.findViewById(R.id.buttonNewOrderCreate);
        buttonCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateOrder();
            }
        });

    }

    public void validateOrder() {
        if (TextUtils.isEmpty(goodType)) {
            showValidateMessage("Please input good type");
            return;
        }

        String weightValue = editTextWeight.getText().toString();
        if (weightValue == null || TextUtils.isEmpty(weightValue)) {
            showValidateMessage("Please input weight");
            return;
        } else {
            weight = Double.parseDouble(weightValue);
        }

        String widthValue = editTextWidth.getText().toString();
        if (widthValue == null || TextUtils.isEmpty(widthValue)) {
            showValidateMessage("Please input width");
            return;
        } else {
            width = Double.parseDouble(widthValue);
        }

        String lengthValue = editTextLength.getText().toString();
        if (lengthValue == null || TextUtils.isEmpty(lengthValue)) {
            showValidateMessage("Please input length");
            return;
        } else {
            length = Double.parseDouble(lengthValue);
        }

        String heightValue = editTextHeight.getText().toString();
        if (heightValue == null || TextUtils.isEmpty(heightValue)) {
            showValidateMessage("Please input height");
            return;
        } else {
            height = Double.parseDouble(heightValue);
        }

        if (TextUtils.isEmpty(vhicleType)) {
            showValidateMessage("Please input vhicle type");
            return;
        }

        Order newOrder = new Order(
                userId, receiverName,"", pickupLocation, pickupLat, pickupLng, dropOffLocation, dropOffLat, dropOffLng, pickupDate, pickupTime, goodType, vhicleType, "", driverPhoneNumber, weight, width, height, length, 1
        );

        NewOrderActivity newOrderActivity = (NewOrderActivity)getActivity();
        newOrderActivity.createNewOrder(newOrder);
    }

    public void showValidateMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
}
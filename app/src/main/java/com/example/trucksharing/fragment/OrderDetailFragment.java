package com.example.trucksharing.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.trucksharing.R;
import com.example.trucksharing.model.Order;
import com.example.trucksharing.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderDetailFragment extends Fragment {

    TextView orderDetailsTextViewSender;
    TextView orderDetailsTextViewPickupTime;
    TextView orderDetailsTextViewReceiver;
    TextView orderDetailsTextViewDropTime;

    TextView orderDetailsTextViewGoodType;
    TextView orderDetailsTextViewVehicleType;
    TextView orderDetailsTextViewWeight;
    TextView orderDetailsTextViewHeight;
    TextView orderDetailsTextViewLength;
    TextView orderDetailsTextViewWidth;

    AppCompatButton buttonCallDriver;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ORDER = "ORDER";
    private static final String USER = "USER";

    // TODO: Rename and change types of parameters
    private Order order;
    private User user;

    public OrderDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment OrderDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderDetailFragment newInstance(Order anOrder, User anUser) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ORDER, anOrder);
        args.putSerializable(USER, anUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            order = (Order) getArguments().getSerializable(ORDER);
            user = (User) getArguments().getSerializable(USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        orderDetailsTextViewSender = view.findViewById(R.id.textViewOrderDetailsFromSender);
        orderDetailsTextViewSender.setText("From " + user.getFullName());

        orderDetailsTextViewPickupTime = view.findViewById(R.id.textViewOrderDetailsPickupTime);
        orderDetailsTextViewPickupTime.setText("Pick up time: " + order.getPickupDate() + " at " + order.getPickupTime());

        orderDetailsTextViewReceiver = view.findViewById(R.id.textViewOrderDetailsToReceiver);
        orderDetailsTextViewReceiver.setText("To " + order.getReceiverName());

        orderDetailsTextViewDropTime = view.findViewById(R.id.textViewOrderDetailsDropOffTime);
        orderDetailsTextViewDropTime.setText("Est. delivery time: ");

        orderDetailsTextViewWeight = view.findViewById(R.id.textViewOrderDetailsWeight);
        orderDetailsTextViewWeight.setText("Weight: \n" + order.getWeight().toString() + " kg");

        orderDetailsTextViewWidth = view.findViewById(R.id.textViewOrderDetailsWidth);
        orderDetailsTextViewWidth.setText("Width: \n" + order.getWidth().toString() + " m");

        orderDetailsTextViewHeight = view.findViewById(R.id.textViewOrderDetailsHeight);
        orderDetailsTextViewHeight.setText("Height: \n" + order.getHeight().toString() + " m");

        orderDetailsTextViewLength = view.findViewById(R.id.textViewOrderDetailsLength);
        orderDetailsTextViewLength.setText("Length: \n" + order.getLength().toString() + " m");

        orderDetailsTextViewGoodType = view.findViewById(R.id.textViewOrderDetailsType);
        orderDetailsTextViewGoodType.setText("Type: \n" + order.getGoodType());

        orderDetailsTextViewVehicleType = view.findViewById(R.id.textViewOrderDetailsVehicleType);
        orderDetailsTextViewVehicleType.setText("Vehicle: \n" + order.getVehicleType());

        buttonCallDriver = view.findViewById(R.id.buttonCallDriver);
        buttonCallDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("");
                alertDialog.setMessage("This will be implemented in 10.1");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();
            }
        });

    }
}
package com.example.trucksharing.activity;

import static com.example.trucksharing.util.Util.USER_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trucksharing.R;
import com.example.trucksharing.data.DatabaseHelper;
import com.example.trucksharing.model.User;
import com.example.trucksharing.util.Util;

public class SignupActivity extends AppCompatActivity {

    DatabaseHelper database; // Database helper to interact with the database

    EditText editTextFullName; // Input field for full name
    EditText editTextUsername; // Input field for username
    EditText editTextPassword; // Input field for password
    EditText editTextConfirmPassword; // Input field for confirm password
    EditText editTextPhoneNumber; // Input field for phone number


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        // Initialize database
        database = new DatabaseHelper(this);

        // Initialize EditTexts
        editTextUsername = findViewById(R.id.editTextSignupUsername);
        editTextPassword = findViewById(R.id.editTextSignupPassword);
        editTextConfirmPassword = findViewById(R.id.editTextSignupConfirmPassword);
        editTextFullName = findViewById(R.id.editTextSignupFullName);
        editTextPhoneNumber = findViewById(R.id.editTextSignupPhoneNumber);
    }

    // Show a dialog when the add image button is clicked
    public void addImageClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Allow the app to access photos, media and files on your device?");
        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SignupActivity.this, "Allow Access",
                        Toast.LENGTH_LONG).show();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    // Handle the create account button click
    public void createAccountClick(View view) {

        String fullName = editTextFullName.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        String phoneNumber = editTextPhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            showValidateMessage("Please enter full name!");
            return;
        }

        if (TextUtils.isEmpty(username)) {
            showValidateMessage("Please enter username!");
            return;
        }

        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            showValidateMessage("Please enter password & confirm password!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showValidateMessage("Two passwords do not match!");
            return;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            showValidateMessage("Please enter phone number!");
            return;
        }

        // Ignore Avatar in this version

        User user = new User(username, password, fullName, phoneNumber);
        int insertedUserId = (int)database.insertUser(user);

        if (insertedUserId >= 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Congratulations!");
            builder.setMessage("You have registered successfully");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent homeIntent = new Intent(SignupActivity.this, HomeActivity.class);
                    homeIntent.putExtra(Util.USER_ID, insertedUserId);
                    startActivity(homeIntent);
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(SignupActivity.this).create();
            alertDialog.setTitle("Oops!");
            alertDialog.setMessage("Registration error");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    (dialog, which) -> dialog.dismiss());
            alertDialog.show();
        }
    }

    public void showValidateMessage(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(SignupActivity.this).create();
        alertDialog.setTitle("");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
}
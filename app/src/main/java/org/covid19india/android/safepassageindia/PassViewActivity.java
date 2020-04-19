package org.covid19india.android.safepassageindia;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PassViewActivity extends AppCompatActivity {
    ImageView userImage, passVerified;
    TextView nameText, phoneText, contentText;
    User user;
    Pass pass;
    Dictionary<Integer, String> passValidity = new Hashtable<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        getPass();
        setPass();
    }

    private void init() {
        passValidity.put(0, "Pending");
        passValidity.put(1, "Approved");
        passValidity.put(2, "Expired");
        passValidity.put(3, "Revoked");
        userImage = findViewById(R.id.user_pic);
        nameText = findViewById(R.id.title1);
        phoneText = findViewById(R.id.title2);
        contentText = findViewById(R.id.content_text);
        passVerified = findViewById(R.id.verified_image);
    }

    private void getPass() {
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        pass = intent.getParcelableExtra("pass");
        int i = 6;
    }

    private void setPass() {
        String passStatus = passValidity.get(pass.getPass_validityState());
        String name = user.getUser_firstName() + (user.getUser_middleName().equals("") ? " " : " " + user.getUser_middleName() + " ") + user.getUser_lastName();
        String phoneNumber = user.getUser_phoneNumber();
        String type = "";
        String startDate = "";
        String endDate = "";
        String medicalVerification = "";
        type = pass.getPass_type();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy',' HH:mm", Locale.ENGLISH);

            LocalDateTime date = LocalDateTime.parse(pass.getPass_createdOn(), inputFormatter);
            startDate = date.format(outputFormatter);

            date = LocalDateTime.parse(pass.getPass_expiryDate(), inputFormatter);
            endDate = date.format(outputFormatter);

        } else {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy',' HH:mm", Locale.ENGLISH);
            try {
                Date date = inputFormat.parse(pass.getPass_createdOn());
                startDate = outputFormat.format(date);
                date = inputFormat.parse(pass.getPass_expiryDate());
                endDate = outputFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (pass.getPass_medicalVerification().equals("Y")) {
            medicalVerification = "Required";
        } else if (pass.getPass_medicalVerification().equals("N")) {
            medicalVerification = "Not Required";
        }
        String content = "Pass Status: " + passStatus + "\n"
                + "Pass Type: " + type + "\n"
                + "From: " + startDate + "\n"
                + "Upto: " + endDate + "\n"
                + "Medical Verification: " + medicalVerification;

        // Inserting the details into the textview
        nameText.setText(name);
        phoneText.setText(phoneNumber);
        contentText.setText(content);
        switch (pass.getPass_validityState()) {
            case 0:
                passVerified.setImageResource(R.drawable.pending_icon);
                break;
            case 1:
                passVerified.setImageResource(R.drawable.valid_icon);
                break;
            case 2:
            case 3:
                passVerified.setImageResource(R.drawable.invalid_icon);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package org.covid19india.android.safepassageindia.passscanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.covid19india.android.safepassageindia.BroadcastReceiverService;
import org.covid19india.android.safepassageindia.LoginActivity;
import org.covid19india.android.safepassageindia.R;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    TextView textView;
    Button scanButton, signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();
        String welcomeMessage = "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        textView.setText(welcomeMessage);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this, ScannerActivity.class));
            }
        });
        /*
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                finish();
            }
        });
        */
    }

    private void init() {
        textView = findViewById(R.id.welcome_text);
        scanButton = findViewById(R.id.scan_button);
//        signOutButton = findViewById(R.id.sign_out);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent service = new Intent(MenuActivity.this, BroadcastReceiverService.class);
        startService(service);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //Do nothing
    }
}

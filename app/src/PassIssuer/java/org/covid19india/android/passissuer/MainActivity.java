package org.covid19india.android.passissuer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.covid19india.android.safepassageindia.LoginActivity;
import org.covid19india.android.safepassageindia.R;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.welcome_text);
        textView.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        button = findViewById(R.id.sign_out);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            finish();
        }
    }
    @Override
    public void onBackPressed() {
        //Do nothing
    }
}

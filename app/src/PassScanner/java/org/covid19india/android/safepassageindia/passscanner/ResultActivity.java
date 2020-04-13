package org.covid19india.android.safepassageindia.passscanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.covid19india.android.safepassageindia.R;

public class ResultActivity extends AppCompatActivity {
    TextView contentText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        contentText=findViewById(R.id.content_text);
        contentText.setText(content);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            finish();
        }
    }
}
/*
package org.covid19india.android.safepassageindia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class SimChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Broadcast Receiver", "Receiver executed");
        String state = intent.getExtras().getString("ss");
        if (state == null) {
            Log.d("Broadcast Receiver", "SIM null");
        }
        try {
            if ("NOT_READY".equals(state)) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    FirebaseAuth.getInstance().signOut();
                }
                Log.d("Broadcast Receiver", "SIM not ready");
            }
        } catch (Exception e){
            Log.e("Broadcast Receiver","State is null");
        }

    }
}

 */
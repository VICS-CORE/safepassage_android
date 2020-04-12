package org.covid19india.android.safepassageindia.passscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.covid19india.android.safepassageindia.R;

public class ResultActivity extends AppCompatActivity {
    TextView contentText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        contentText=findViewById(R.id.content_text);
        contentText.setText(content);
    }
}

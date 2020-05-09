package org.covid19india.android.safepassageindia.passissuer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import org.covid19india.android.safepassageindia.R;
import org.covid19india.android.safepassageindia.passissuer.fragment.PassFormFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FormActivity extends AppCompatActivity {
    private FrameLayout frameLayout;
    private String type;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        init();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_icon);
        if (type.equals("pass")) {
            loadFragment(PassFormFragment.newInstance(bitmap));
        }
    }

    private void init() {
        frameLayout = findViewById(R.id.frame_layout);
        Intent intent = getIntent();
        type = intent.getStringExtra("form_type");
        if (type.equals("pass")) {
            byte[] bytes = intent.getByteArrayExtra("image");
//            bitmap = intent.getParcelableExtra("bitmap");
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
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

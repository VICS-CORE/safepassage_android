package org.covid19india.android.safepassageindia.passissuer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.covid19india.android.safepassageindia.R;
import org.covid19india.android.safepassageindia.passissuer.fragment.ExistingUserFragment;
import org.covid19india.android.safepassageindia.passissuer.fragment.MemberFormFragment;
import org.covid19india.android.safepassageindia.passissuer.fragment.NewUserFormFragment;
import org.covid19india.android.safepassageindia.passissuer.fragment.PassFormFragment;
import org.covid19india.android.safepassageindia.passissuer.fragment.TeamFormFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FormActivity extends AppCompatActivity {
    private static final String TAG = "FormActivity";
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
        getSupportActionBar().setTitle(Html.fromHtml("<font color=\"#FFFFFF\">" + getString(R.string.app_name) + "</font>"));
        switch (type) {
            case "new_user":
                Log.d(TAG, "New User");
                loadFragment(NewUserFormFragment.newInstance());
                break;
            case "existing_user":
                Log.d(TAG, "Existing User");
                loadFragment(ExistingUserFragment.newInstance());
                break;
            case "pass":
                Log.d(TAG, "Pass");
                loadFragment(PassFormFragment.newInstance(bitmap));
                break;
            case "team":
                Log.d(TAG, "Team");
                loadFragment(TeamFormFragment.newInstance());
                break;
            case "member":
                Log.d(TAG, "Member");
                loadFragment(MemberFormFragment.newInstance());
                break;
            default:
                Log.d(TAG, "default - wrong call");
                Toast.makeText(this, "Wrong call", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void init() {
        frameLayout = findViewById(R.id.frame_layout);
        Intent intent = getIntent();
        type = intent.getStringExtra("form_type");
        if (type != null && type.equals("pass")) {
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

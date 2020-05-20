package org.covid19india.android.safepassageindia.passissuer.activity;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FormActivity extends AppCompatActivity {
    private static final String TAG = "FormActivity";
    private FrameLayout frameLayout;
    private Fragment passFormFragment;
    private String type, userId, phoneNumber;
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
                passFormFragment = PassFormFragment.newInstance(bitmap, userId, phoneNumber);
                loadFragment(passFormFragment);
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
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            userId = intent.getStringExtra("user_id");
            phoneNumber = intent.getStringExtra("user_phoneNumber");
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (passFormFragment != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogStyle)
                    .setTitle("Do you want to Cancel?")
                    .setMessage("You have come a long way. If you go back all data will be lost. Are you sure you want this?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

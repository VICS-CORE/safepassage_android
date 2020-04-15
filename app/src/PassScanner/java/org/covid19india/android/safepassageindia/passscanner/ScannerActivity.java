package org.covid19india.android.safepassageindia.passscanner;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.Result;

import org.covid19india.android.safepassageindia.PassList;
import org.covid19india.android.safepassageindia.R;
import org.covid19india.android.safepassageindia.UserApi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.Manifest.permission.CAMERA;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA = 1;
    ZXingScannerView scannerView;
    RelativeLayout relativeLayout;
    ImageButton button;
    EditText phoneEdit;
    private String responseFormat = "json";
    private String userType = "3";
    private static final String TAG = "ScannerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        addScanner();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = phoneEdit.getText().toString().trim();
                if (content.length() != 10) {
                    phoneEdit.setError("Invalid Number");
                    phoneEdit.setText("");
                    phoneEdit.requestFocus();
                    return;
                }
                phoneEdit.setText("");
                scannerView.stopCamera();
                requestApi(content);
//                Intent intent = new Intent(ScannerActivity.this, ResultActivity.class);
//                intent.putExtra("content", content);
//                startActivity(intent);
            }
        });
    }

    private void requestApi(String content) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi userApi = retrofit.create(UserApi.class);
        Call<PassList> passListCall = userApi.getPasses(responseFormat, content, userType);
        passListCall.enqueue(new Callback<PassList>() {
            @Override
            public void onResponse(Call<PassList> call, Response<PassList> response) {
                Log.d(TAG, "API Call success");
                PassList passList = response.body();
                if (passList != null && passList.isUniqueUser()) {
                    Intent intent = new Intent(ScannerActivity.this, ResultActivity.class);
                    intent.putExtra("passList", passList);
                    startActivity(intent);
                } else {
                    Toast.makeText(ScannerActivity.this, "Pass is null", Toast.LENGTH_SHORT).show();
                    scannerView.startCamera();
                }
            }

            @Override
            public void onFailure(Call<PassList> call, Throwable t) {
                t.printStackTrace();
                Log.d(TAG, "API Response Failure");
            }
        });

    }

    private void addScanner() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW, R.id.linear_layout);
        relativeLayout.addView(scannerView, params);
        verifyPermission();
    }

    private void init() {
        button = findViewById(R.id.phone_button);
        phoneEdit = findViewById(R.id.phone_edit);
        relativeLayout = findViewById(R.id.relative_layout);
        scannerView = new ZXingScannerView(ScannerActivity.this);
    }

    public void verifyPermission() {
        int currentApiVersion = Build.VERSION.SDK_INT;

        if (currentApiVersion >= Build.VERSION_CODES.M) {
            if (!isPermissionGranted()) {
                requestPermission();
            }
        }
    }

    private boolean isPermissionGranted() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= android.os.Build.VERSION_CODES.M) {
            if (isPermissionGranted()) {
                if (scannerView != null) {
                    scannerView.setResultHandler(this);
                    scannerView.startCamera();
                }
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void handleResult(Result result) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            finish();
        }
        final String text = result.getText();
        Log.d(TAG, result.getText());
        Log.d(TAG, result.getBarcodeFormat().toString());
        requestApi(text);
//        Intent intent = new Intent(ScannerActivity.this, ResultActivity.class);
//        intent.putExtra("content", text);
//        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (scannerView != null) {
            scannerView.stopCamera();
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

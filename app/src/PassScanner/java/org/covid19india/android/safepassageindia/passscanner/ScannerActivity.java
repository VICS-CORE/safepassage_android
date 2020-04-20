package org.covid19india.android.safepassageindia.passscanner;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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
    ProgressBar progressBar;
    Vibrator vibrator;
    private static final String responseFormat = "json";
    private static final String userType = "3";
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
                progressBar.setVisibility(View.VISIBLE);
                phoneEdit.onEditorAction(EditorInfo.IME_ACTION_DONE);
                String content = phoneEdit.getText().toString().trim();
                if (content.length() != 10) {
                    phoneEdit.setError("Invalid Number");
                    phoneEdit.setText("");
                    phoneEdit.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                phoneEdit.setText("");
                scannerView.stopCameraPreview();
                requestApi(content);
            }
        });
    }

    private void init() {
        button = findViewById(R.id.phone_button);
        phoneEdit = findViewById(R.id.phone_edit);
        relativeLayout = findViewById(R.id.relative_layout);
        scannerView = new ZXingScannerView(ScannerActivity.this);
        progressBar = new ProgressBar(getApplicationContext());
        progressBar.setVisibility(View.GONE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        disableAutoSuggestion();
    }

    private void disableAutoSuggestion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow()
                    .getDecorView()
                    .setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
        }
    }

    private void startVibration(int milliseconds) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(milliseconds);
        }
    }

    private void addScanner() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW, R.id.linear_layout);
        relativeLayout.addView(scannerView, params);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(200, 200);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        relativeLayout.addView(progressBar, layoutParams);
        verifyPermission();
    }

    private void requestApi(String content) {
        if (content.length() != 10) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(ScannerActivity.this, "QR invalid", Toast.LENGTH_SHORT).show();
            startVibration(200);
            scannerView.resumeCameraPreview(ScannerActivity.this);
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi userApi = retrofit.create(UserApi.class);
        Call<PassList> passListCall = userApi.getPasses(responseFormat, content, userType);
        passListCall.enqueue(new Callback<PassList>() {
            @Override
            public void onResponse(Call<PassList> call, Response<PassList> response) {
                Log.d(TAG, "API Call success, Response code: " + response.code());
                progressBar.setVisibility(View.GONE);
                startVibration(200);
                PassList passList = response.body();
                if (passList != null && passList.isUniqueUser()) {
                    Intent intent = new Intent(ScannerActivity.this, ResultActivity.class);
                    intent.putExtra("passList", passList);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "User doesn't exist", Toast.LENGTH_LONG).show();
                    scannerView.resumeCameraPreview(ScannerActivity.this);
                }
            }

            @Override
            public void onFailure(Call<PassList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.d(TAG, "API Response Failure");
                Toast.makeText(ScannerActivity.this, "Server cannot be accessed!", Toast.LENGTH_SHORT).show();
                startVibration(200);
                scannerView.resumeCameraPreview(ScannerActivity.this);
            }
        });

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
    public void handleResult(Result result) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            finish();
        }
        final String text = result.getText();
        Log.d(TAG, result.getText());
        Log.d(TAG, result.getBarcodeFormat().toString());
        progressBar.setVisibility(View.VISIBLE);
        requestApi(text);
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
            }
        }
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

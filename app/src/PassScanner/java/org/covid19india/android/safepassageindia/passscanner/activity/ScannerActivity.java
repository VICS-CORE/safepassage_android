package org.covid19india.android.safepassageindia.passscanner.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
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

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.Result;

import org.covid19india.android.safepassageindia.R;
import org.covid19india.android.safepassageindia.RetrofitClient;
import org.covid19india.android.safepassageindia.ServerApi;
import org.covid19india.android.safepassageindia.model.UserPassList;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA_AND_LOCATION = 1;
    private static final String responseFormat = "json";
    private static final String userType = "3";
    private static final String TAG = "ScannerActivity";
    private static final int REQUEST_CHECK_SETTINGS = 2;
    private ZXingScannerView scannerView;
    private RelativeLayout relativeLayout;
    private ImageButton button;
    private EditText phoneEdit;
    private ProgressBar progressBar;
    private Vibrator vibrator;
    private LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

        getPermission();
        createLocationRequest();
        if (isLocationPermissionGranted()) {
            requestLocationSettings(null);
            addScanner();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                phoneEdit.onEditorAction(EditorInfo.IME_ACTION_DONE);
                final String content = phoneEdit.getText().toString().trim();
                if (content.length() != 10) {
                    phoneEdit.setError("Invalid Number");
                    phoneEdit.setText("");
                    phoneEdit.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                phoneEdit.setText("");
                scannerView.stopCameraPreview();
                requestLocationSettings(new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        requestApi(content);
                    }
                });
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
    }

    private void requestApi(String content) {
        if (content.length() != 10) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(ScannerActivity.this, "QR invalid", Toast.LENGTH_SHORT).show();
            startVibration(200);
            scannerView.resumeCameraPreview(ScannerActivity.this);
            return;
        }
        Retrofit retrofit = RetrofitClient.getClient(ServerApi.BASE_URL);
        ServerApi serverApi = retrofit.create(ServerApi.class);

        SharedPreferences sf = getSharedPreferences("session_cookie", Context.MODE_PRIVATE);
        String cookie = sf.getString("Set-Cookie", "NA");
        Call<UserPassList> passListCall = serverApi.getUserPasses(cookie, responseFormat, content, userType);
        passListCall.enqueue(new Callback<UserPassList>() {
            @Override
            public void onResponse(Call<UserPassList> call, Response<UserPassList> response) {
                Log.d(TAG, "API Call success, Response code: " + response.code());
                progressBar.setVisibility(View.GONE);
                startVibration(200);
                if (response.code() / 100 == 2) {
                    UserPassList userPassList = response.body();
                    if (userPassList != null && userPassList.isUniqueUser()) {
                        Intent intent = new Intent(ScannerActivity.this, ResultActivity.class);
                        intent.putExtra("userPassList", userPassList);
                        startActivity(intent);
                    } else {
                        Log.d(TAG, "UserPassList is either empty or the users are not unique");
                    }
                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        int i = 1;
                        Log.d(TAG, jObjError.get("detail").toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    Toast.makeText(getApplicationContext(), "User doesn't exist", Toast.LENGTH_LONG).show();
                }
                scannerView.resumeCameraPreview(ScannerActivity.this);
            }

            @Override
            public void onFailure(Call<UserPassList> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Log.d(TAG, "API Response Failure");
                Toast.makeText(ScannerActivity.this, "Server cannot be accessed!", Toast.LENGTH_SHORT).show();
                startVibration(200);
                scannerView.resumeCameraPreview(ScannerActivity.this);
            }
        });

    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void requestLocationSettings(OnSuccessListener<LocationSettingsResponse> listener) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build())
                .addOnSuccessListener(this, listener)
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Location not turned on, requesting user dialog");
                        progressBar.setVisibility(View.GONE);
                        if (e instanceof ResolvableApiException) {
                            // Location settings are not satisfied, but this can be fixed
                            // by showing the user a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(ScannerActivity.this,
                                        REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendEx) {
                                // Ignore the error.
                            }
                        }
                    }
                });
    }

    private void getPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA, ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION}, REQUEST_CAMERA_AND_LOCATION);
    }

    private boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(ScannerActivity.this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(ScannerActivity.this, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isCameraPermissionGranted() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
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
        requestLocationSettings(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                requestApi(text);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case AppCompatActivity.RESULT_CANCELED:
                    Toast.makeText(ScannerActivity.this, "Turn on location to proceed", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Location request settings canceled");
                    finish();
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_AND_LOCATION) {
            if (isLocationPermissionGranted()) {
                requestLocationSettings(null);
                addScanner();
            } else {
                Toast.makeText(ScannerActivity.this, "Location Permission denied", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= android.os.Build.VERSION_CODES.M) {
            if (isCameraPermissionGranted()) {
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

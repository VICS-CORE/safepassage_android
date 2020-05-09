package org.covid19india.android.safepassageindia.passscanner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import org.covid19india.android.safepassageindia.BroadcastReceiverService;
import org.covid19india.android.safepassageindia.R;
import org.covid19india.android.safepassageindia.RetrofitClient;
import org.covid19india.android.safepassageindia.ServerApi;
import org.covid19india.android.safepassageindia.activity.LoginActivity;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "MenuActivity";
    TextView textView;
    Button scanButton, signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();
        FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                String token = getTokenResult.getToken();
                Log.d(TAG, "TokenId = " + token);
                callApi(token);
            }
        });
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

    private void callApi(String token) {
        if (RetrofitClient.isEmpty(getApplicationContext())) {
            Log.d(TAG, "Creating new Session");
            RequestBody idToken = RequestBody.create(MediaType.parse("multipart/form-data"), token);
            Retrofit retrofit = RetrofitClient.getClient(ServerApi.BASE_URL);
            ServerApi serverApi = retrofit.create(ServerApi.class);
            serverApi.sessionLogin(idToken).enqueue(new Callback<List<String>>() {
                @Override
                public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                    Log.d(TAG, "Response Code: " + response.code());
                    if (response.isSuccessful() && response.code() == 200) {
                        List<String> message = response.body();
                        RetrofitClient.storeCookie(getApplicationContext(), response.headers().get("Set-Cookie"));
                        HttpCookie cookie = new HttpCookie("hi", "hello");
                        String value = cookie.getValue();
                        String name = cookie.getName();

                        Log.d(TAG, "Cookie: " + response.headers().get("Set-Cookie") + "\nMessage: " + message.get(0));
                    } else {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            Log.d(TAG, jObjError.get("detail").toString());
                        } catch (Exception e) {
                            Log.d(TAG, e.getMessage());
                        }
                        logOut();
                    }
                }

                @Override
                public void onFailure(Call<List<String>> call, Throwable t) {
                    Log.d(TAG, "Failure\n" + t.getMessage());
                    t.printStackTrace();
                }
            });
        } else {
            if (RetrofitClient.isExpired(getApplicationContext())) {
                Log.d(TAG, "Session Expired!");
                RetrofitClient.removeCookie(getApplicationContext());
                logOut();
            } else {
                Log.d(TAG, "Session already present");
            }
        }
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MenuActivity.this, LoginActivity.class));
        finish();
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
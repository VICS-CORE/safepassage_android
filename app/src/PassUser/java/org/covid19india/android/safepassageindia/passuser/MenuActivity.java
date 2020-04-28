package org.covid19india.android.safepassageindia.passuser;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.covid19india.android.safepassageindia.R;
import org.covid19india.android.safepassageindia.UserApi;
import org.covid19india.android.safepassageindia.model.PassList;
import org.covid19india.android.safepassageindia.model.User;
import org.covid19india.android.safepassageindia.model.UserList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "MenuActivity";
    TextView textView;
    ImageView imageView;
    Button signOutButton;
    User user;
    ProgressBar passProgressBar;
    PassList passList;
    RecyclerView recyclerView;
    private QRCodeWriter writer;
    private static final String responseFormat = "json";
    private static final String userType = "1";
    private static final String passType = "4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();
        String welcomeMessage = "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        textView.setText(welcomeMessage);
        String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().substring(3);
//        phoneNumber = "9488834767";
        generateQR(phoneNumber);
        requestUserApi(phoneNumber);
        /*signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                finish();
            }
        });*/
    }

    private void requestPassApi(String phoneNumber) {
        passProgressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi userApi = retrofit.create(UserApi.class);
        Call<PassList> passListCall = userApi.getPasses(responseFormat, phoneNumber, passType);
        passListCall.enqueue(new Callback<PassList>() {
            @Override
            public void onResponse(Call<PassList> call, Response<PassList> response) {
                Log.d(TAG, "API Call success, Response code: " + response.code());
                passProgressBar.setVisibility(View.GONE);
                if (response.code() == 200) {
                    passList = response.body();
                    if (passList != null) {
                        passList.renamePassType();
                        setRecyclerView();
                    }
                }
            }

            @Override
            public void onFailure(Call<PassList> call, Throwable t) {
                Log.d(TAG, "Server not accessible - pass access");
                passProgressBar.setVisibility(View.GONE);
                Toast.makeText(MenuActivity.this, "Server is not accessible", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PassAdapter(passList, user));
    }

    private void requestUserApi(String phoneNumber) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UserApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserApi userApi = retrofit.create(UserApi.class);
        Call<UserList> userListCall = userApi.getUsers(responseFormat, phoneNumber, userType);
        final String finalPhoneNumber = phoneNumber;
        userListCall.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                Log.d(TAG, "API Call success, Response code: " + response.code());
                if (response.code() == 200) {
                    UserList list = response.body();
                    if (list != null) {
                        if (list.getUsers().size() == 1) {
                            user = list.getUsers().get(0);
                            requestPassApi(finalPhoneNumber);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                Log.d(TAG, "Server not accessible - user access");
                Toast.makeText(MenuActivity.this, "Server is not accessible", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void init() {
        textView = findViewById(R.id.welcome_text);
        imageView = findViewById(R.id.qr_pic);
        recyclerView = findViewById(R.id.recycler_view);
        passProgressBar = findViewById(R.id.pass_progress);
//        signOutButton = findViewById(R.id.sign_out);
    }

    private void generateQR(String string) {
        imageView.setVisibility(View.GONE);
        writer = new QRCodeWriter();
        int width = 700;
        int height = 700;
        try {
            BitMatrix bitMatrix = writer.encode(string, BarcodeFormat.QR_CODE, width, height);
            Bitmap bmp = createBitmap(bitMatrix);
            imageView.setImageBitmap(bmp);
            imageView.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

    public Bitmap createBitmap(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        //Do nothing
    }
}

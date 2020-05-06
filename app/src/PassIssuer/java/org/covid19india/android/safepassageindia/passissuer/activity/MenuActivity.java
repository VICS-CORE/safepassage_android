package org.covid19india.android.safepassageindia.passissuer.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;

import org.covid19india.android.safepassageindia.R;
import org.covid19india.android.safepassageindia.RetrofitClient;
import org.covid19india.android.safepassageindia.UserApi;
import org.covid19india.android.safepassageindia.passissuer.adapter.SectionsPagerAdapter;
import org.covid19india.android.safepassageindia.passissuer.fragment.PassesFragment;
import org.covid19india.android.safepassageindia.passissuer.fragment.TeamsFragment;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "MenuActivity";
    ViewPager viewPager;
    TabLayout tabLayout;
    SectionsPagerAdapter pagerAdapter;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();
        setViewPager();
        FirebaseAuth.getInstance().getCurrentUser().getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                String token = getTokenResult.getToken();
                RequestBody idToken = RequestBody.create(MediaType.parse("multipart/form-data"), token);
                retrofit = RetrofitClient.getClient(UserApi.BASE_URL);
                UserApi userApi = retrofit.create(UserApi.class);
                userApi.sessionLogin(idToken).enqueue(new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        Log.d(TAG, "Response Code: " + response.code());
                        if (response.isSuccessful()) {
                            Toast.makeText(MenuActivity.this, "Successfully posted", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Cookie: " + response.headers().get("Set-Cookie"));
                            List<String> message = response.body();
                            Log.d(TAG, message.get(0));
                        } else {
                            try {
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Log.d(TAG, jObjError.get("detail").toString());
                            } catch (Exception e) {
                                Log.d(TAG, e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Log.d(TAG, "Failure\n" + t.getMessage());
                        t.printStackTrace();
                    }
                });
                Log.d(TAG, "TokenId = " + token);
            }
        });
        /*textView = findViewById(R.id.welcome_text);
        textView.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        button = findViewById(R.id.sign_out);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
                finish();
            }
        });*/
    }

    private void setViewPager() {
        List<Fragment> fragmentList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        fragmentList.add(PassesFragment.newInstance("Similar to intent.putExtra method", "Similar to intent.putExtra method"));
        titleList.add("Passes");
        fragmentList.add(TeamsFragment.newInstance("Similar to intent.putExtra method", "Similar to intent.putExtra method"));
        titleList.add("Teams");

        pagerAdapter.setFragments(fragmentList, titleList);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void init() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this, null, null);
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
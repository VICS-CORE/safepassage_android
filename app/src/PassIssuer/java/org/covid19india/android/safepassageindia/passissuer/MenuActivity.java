package org.covid19india.android.safepassageindia.passissuer;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import org.covid19india.android.safepassageindia.R;
import org.covid19india.android.safepassageindia.passissuer.adapter.SectionsPagerAdapter;
import org.covid19india.android.safepassageindia.passissuer.fragment.PassesFragment;
import org.covid19india.android.safepassageindia.passissuer.fragment.TeamsFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class MenuActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    SectionsPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();
        setViewPager();
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
        fragmentList.add(new PassesFragment());
        titleList.add("Passes");
        fragmentList.add(new TeamsFragment());
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
package org.covid19india.android.safepassageindia.passissuer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import org.covid19india.android.safepassageindia.R;

public class OrgFormActivity extends AppCompatActivity {
    public static final String USER_INFO = "org.covid19india.android.safepassageindia.passissue.USER_INFO";
    private OrgUserInfo mOrgUserInfo;
    private boolean mIsNewOrgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_form);

        readDisplayStateValues();
        EditText textUserName = findViewById(R.id.text_name);
        EditText textUserMobileNo = findViewById(R.id.text_mobile_number);
        EditText textUserDesignation = findViewById(R.id.text_designation);
        EditText textUserRole = findViewById(R.id.text_role);

        if (!mIsNewOrgUser)
            displayNote(textUserName,textUserMobileNo,textUserDesignation,textUserRole);

    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        mOrgUserInfo = intent.getParcelableExtra(USER_INFO);
        mIsNewOrgUser = mOrgUserInfo == null;
    }

    private void displayNote(EditText textUserName, EditText textUserMobileNo, EditText textUserDesignation, EditText textUserRole) {
        textUserName.setText(mOrgUserInfo.getUserName());
        textUserMobileNo.setText(mOrgUserInfo.getUserMobileNumber());
        textUserDesignation.setText(mOrgUserInfo.getUserDesignation());
        textUserRole.setText(mOrgUserInfo.getUserType().toString());
    }
}

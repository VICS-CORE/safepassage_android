package org.covid19india.android.safepassageindia.passissuer;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.covid19india.android.safepassageindia.R;

import java.util.List;

public class PassIssuerListActivity extends AppCompatActivity {
    private OrgAdapter mOrgAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_issuer_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PassIssuerListActivity.this, OrgFormActivity.class));

            }
        });

        initializeDisplayContent();
    }

    private void initializeDisplayContent() {
        final ListView listNotes = findViewById(R.id.list_members);

        List<OrgUserInfo> orgUsers = OrgDataManager.getInstance().getOrgUser();
        mOrgAdapter = new OrgAdapter(this,orgUsers);
        listNotes.setAdapter(mOrgAdapter);

        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(PassIssuerListActivity.this, OrgFormActivity.class);
                OrgUserInfo orgUserInfo = (OrgUserInfo) listNotes.getItemAtPosition(position);
                intent.putExtra(OrgFormActivity.USER_INFO, orgUserInfo);
                startActivity(intent);
            }
        });
    }

}

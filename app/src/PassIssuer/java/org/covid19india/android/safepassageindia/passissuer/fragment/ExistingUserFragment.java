package org.covid19india.android.safepassageindia.passissuer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import org.covid19india.android.safepassageindia.R;
import org.covid19india.android.safepassageindia.passissuer.activity.CameraActivity;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExistingUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExistingUserFragment extends Fragment {
    private TextInputEditText phoneText;
    private Button verifyButton;

    public ExistingUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ExistingUserFragment.
     */
    public static ExistingUserFragment newInstance() {
        ExistingUserFragment fragment = new ExistingUserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_existing_user, container, false);
        init(view);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Button) view).setEnabled(false);
                if (validate()) {
                    verifyUser();
                } else {
                    ((Button) view).setEnabled(true);
                }
            }
        });
        return view;
    }

    private void verifyUser() {
        //TODO verify user
        startCameraActivity();
    }

    private boolean validate() {
        if (phoneText.getText().toString().trim().isEmpty()) {
            phoneText.setError("Cannot be blank");
            phoneText.requestFocus();
            return false;
        } else if (phoneText.getText().toString().trim().length() != 10) {
            phoneText.setError("Enter 10 digit mobile number");
            phoneText.requestFocus();
            return false;
        }
        return true;
    }

    private void startCameraActivity() {
        Intent intent = new Intent(getActivity().getApplicationContext(), CameraActivity.class);
        //TODO add whatever details have to be sent through intent
        startActivity(intent);
        getActivity().finish();
    }

    private void init(View view) {
        verifyButton = view.findViewById(R.id.btn_verify);
        verifyButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.blue_button));

        phoneText = view.findViewById(R.id.phoneEdit);
    }
}

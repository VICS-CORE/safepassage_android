package org.covid19india.android.safepassageindia.passissuer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;

import org.covid19india.android.safepassageindia.R;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewUserFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewUserFormFragment extends Fragment {
    private Button createButton;
    private TextInputEditText firstNameText, lastNameText, middleNameText, phoneText;
    private RadioGroup radioGroup;
    private String gender;
    MaterialRadioButton radioButton;

    public NewUserFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewUserFormFragment.
     */
    public static NewUserFormFragment newInstance() {
        NewUserFormFragment fragment = new NewUserFormFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_user_form, container, false);
        init(view);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    createUser();
                }
            }
        });
        return view;
    }

    private void createUser() {
        //TODO implement the create user retrofit
    }

    private boolean validate() {
        if (firstNameText.getText().toString().trim().isEmpty()) {
            firstNameText.setError("Cannot be blank");
            firstNameText.requestFocus();
            return false;
        } else if (lastNameText.getText().toString().trim().isEmpty()) {
            lastNameText.setError("Cannot be blank");
            lastNameText.requestFocus();
            return false;
        } else if (phoneText.getText().toString().trim().isEmpty()) {
            phoneText.setError("Cannot be blank");
            phoneText.requestFocus();
            return false;
        } else if (phoneText.getText().toString().trim().length() != 10) {
            phoneText.setError("Enter 10 digit mobile number");
            phoneText.requestFocus();
            return false;
        } else if (gender == null || gender.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Gender is not selected", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void init(final View view) {
        radioButton = view.findViewById(R.id.sample);
        createButton = view.findViewById(R.id.btn_create);
        createButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.blue_button));
        firstNameText = view.findViewById(R.id.firstNameEdit);
        middleNameText = view.findViewById(R.id.middleNameEdit);
        lastNameText = view.findViewById(R.id.lastNameEdit);
        phoneText = view.findViewById(R.id.phoneEdit);
        radioGroup = view.findViewById(R.id.gender_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                MaterialRadioButton radioButton = radioGroup.findViewById(id);
                gender = radioButton.getText().toString().trim();
            }
        });
    }
}

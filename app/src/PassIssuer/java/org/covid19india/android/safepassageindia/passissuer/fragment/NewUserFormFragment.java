package org.covid19india.android.safepassageindia.passissuer.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.internal.LinkedTreeMap;

import org.covid19india.android.safepassageindia.PersistentCookieStore;
import org.covid19india.android.safepassageindia.R;
import org.covid19india.android.safepassageindia.RetrofitClient;
import org.covid19india.android.safepassageindia.ServerApi;
import org.covid19india.android.safepassageindia.model.User;
import org.covid19india.android.safepassageindia.passissuer.activity.CameraActivity;
import org.covid19india.android.safepassageindia.passissuer.activity.FormActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewUserFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewUserFormFragment extends Fragment {
    private static final String[] states = {"Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
            "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Goa", "Gujarat", "Haryana",
            "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Ladakh", "Lakshadweep", "Madhya Pradesh",
            "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim",
            "Tamil Nadu", "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"};
    private static final String TAG = "NewUserFormFragment";

    private static class EnclosedUser {
        private User user;

        public EnclosedUser(User user) {
            this.user = user;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    private Button createButton;
    private TextInputEditText firstNameText, lastNameText, middleNameText, phoneText, address1Text, address2Text, address3Text, cityText, stateText, countryText, zipCodeText;
    private TextInputLayout tilFirstName, tilLastName, tilMiddleName, tilPhone, tilAddress1, tilAddress2, tilAddress3, tilCity, tilState, tilCountry, tilZipCode;
    private RadioGroup radioGroup;
    private String gender;
    private ProgressBar loadingBar;

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
        PersistentCookieStore cookieStore = new PersistentCookieStore(view.getContext());
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    User user = createUserObject();
                    loadingBar.setVisibility(View.VISIBLE);
                    createUser(user);
                }
            }
        });
        return view;
    }

    private User createUserObject() {
        User user = new User();
        user.setUser_firstName(firstNameText.getText().toString().trim());
        user.setUser_lastName(lastNameText.getText().toString().trim());
        user.setUser_middleName(middleNameText.getText().toString().trim());
        user.setUser_gender(gender);
        user.setUser_phoneNumber(phoneText.getText().toString().trim());
        user.setUser_addressName(address1Text.getText().toString().trim());
        user.setUser_addressStreetLine1(address2Text.getText().toString().trim());
        user.setUser_addressStreetLine2(address3Text.getText().toString().trim());
        user.setUser_addressStreetLine3("");
        user.setUser_addressCountry(countryText.getText().toString().trim());
        user.setUser_addressState(stateText.getText().toString().trim());
        user.setUser_addressCity(cityText.getText().toString().trim());
        user.setUser_addressZipCode(zipCodeText.getText().toString().trim());
        user.setUser_identity(2);
        return user;
    }

    private void createUser(User user) {
        Retrofit retrofit = RetrofitClient.getClient(ServerApi.BASE_URL);
        EnclosedUser enclosedUser = new EnclosedUser(user);
        ServerApi serverApi = retrofit.create(ServerApi.class);
        serverApi.createUser(RetrofitClient.getSession(getContext()), enclosedUser)
                .enqueue(new Callback<LinkedTreeMap<String, String>>() {
                    @Override
                    public void onResponse(Call<LinkedTreeMap<String, String>> call, Response<LinkedTreeMap<String, String>> response) {
                        loadingBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.code() / 100 == 2) {
                            Log.d(TAG, "createUser successful, code = " + response.code());
                            LinkedTreeMap<String, String> task = response.body();
                            String msg = task.get("success");
                            if (msg != null) {
                                Log.d(TAG, msg);
                            }
                            Toast.makeText(getContext(), "User Created", Toast.LENGTH_SHORT).show();
                            callAlertDialog();
                        } else {
                            Log.d(TAG, "createUser not successful, code = " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<LinkedTreeMap<String, String>> call, Throwable t) {
                        loadingBar.setVisibility(View.GONE);
                        Log.d(TAG, "Server cannot be accessed");
                        t.printStackTrace();
                    }
                });
    }

    private boolean validate() {
        tilFirstName.setError(null);
        tilLastName.setError(null);
        tilPhone.setError(null);
        tilAddress1.setError(null);
        tilCity.setError(null);
        tilState.setError(null);
        tilCountry.setError(null);
        tilZipCode.setError(null);
        if (firstNameText.getText().toString().trim().isEmpty()) {
            tilFirstName.setError("Cannot be blank");
            tilFirstName.requestFocus();
            return false;
        } else if (lastNameText.getText().toString().trim().isEmpty()) {
            tilLastName.setError("Cannot be blank");
            tilLastName.requestFocus();
            return false;
        } else if (phoneText.getText().toString().trim().isEmpty()) {
            tilPhone.setError("Cannot be blank");
            tilPhone.requestFocus();
            return false;
        } else if (phoneText.getText().toString().trim().length() != 10) {
            tilPhone.setError("Enter 10 digit mobile number");
            tilPhone.requestFocus();
            return false;
        } else if (gender == null || gender.isEmpty()) {
            Toast.makeText(getActivity().getApplicationContext(), "Gender is not selected", Toast.LENGTH_SHORT).show();
            return false;
        } else if (address1Text.getText().toString().trim().isEmpty()) {
            tilAddress1.setError("Cannot be blank");
            tilAddress1.requestFocus();
            return false;
        } else if (!isCountryValid()) {
            tilCountry.setError("Invalid");
            tilCountry.requestFocus();
            return false;
        } else if (!isStateValid()) {
            tilState.setError("Invalid");
            tilState.requestFocus();
            return false;
        } else if (cityText.getText().toString().trim().isEmpty()) {
            tilCity.setError(("Invalid"));
            tilCity.requestFocus();
            return false;
        } else if (zipCodeText.getText().toString().trim().length() < 6) {
            tilZipCode.setError("Invalid");
            tilZipCode.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isStateValid() {
        List<String> list = new ArrayList<>(Arrays.asList(states));
        return list.contains(stateText.getText().toString().trim());
    }

    private boolean isCountryValid() {
        return countryText.getText().toString().trim().equals("India");
    }

    private void init(final View view) {
        createButton = view.findViewById(R.id.btn_create);
        createButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.blue_button));

        loadingBar = view.findViewById(R.id.loading_progress);

        firstNameText = view.findViewById(R.id.firstNameEdit);
        tilFirstName = view.findViewById(R.id.til_firstName);

        middleNameText = view.findViewById(R.id.middleNameEdit);
        tilMiddleName = view.findViewById(R.id.til_middleName);

        lastNameText = view.findViewById(R.id.lastNameEdit);
        tilLastName = view.findViewById(R.id.til_lastName);

        phoneText = view.findViewById(R.id.phoneEdit);
        tilPhone = view.findViewById(R.id.til_phone);

        radioGroup = view.findViewById(R.id.gender_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                MaterialRadioButton radioButton = radioGroup.findViewById(id);
                gender = radioButton.getText().toString().trim().substring(0, 1);
            }
        });

        address1Text = view.findViewById(R.id.address1Edit);
        tilAddress1 = view.findViewById(R.id.til_address1);

        address2Text = view.findViewById(R.id.address2Edit);
        tilAddress2 = view.findViewById(R.id.til_address2);

        address3Text = view.findViewById(R.id.address3Edit);
        tilAddress3 = view.findViewById(R.id.til_address3);

        cityText = view.findViewById(R.id.cityEdit);
        tilCity = view.findViewById(R.id.til_city);

        stateText = view.findViewById(R.id.stateEdit);
        tilState = view.findViewById(R.id.til_state);

        countryText = view.findViewById(R.id.countryEdit);
        tilCountry = view.findViewById(R.id.til_country);

        zipCodeText = view.findViewById(R.id.zipCodeEdit);
        tilZipCode = view.findViewById(R.id.til_zipCode);
    }

    public void callAlertDialog() {
        final Activity activity = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle)
//                .setTitle("Do you want to continue?")
                .setMessage("User is created, do you want to continue?")
                .setPositiveButton("Continue to pass creation", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO should decide which method to call
//                        startCameraActivity();
                        startExistingUserFragment();
                    }
                })
                .setNegativeButton("Home page", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.finish();
                    }
                })
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startCameraActivity() {
        Intent intent = new Intent(getActivity().getApplicationContext(), CameraActivity.class);
        //TODO add user id and phone number
//        intent.putExtra("user_id",);
        intent.putExtra("user_phoneNumber", phoneText.getText().toString().trim());
        startActivity(intent);
        getActivity().finish();
    }

    private void startExistingUserFragment() {
        Intent intent = new Intent(getActivity().getApplicationContext(), FormActivity.class);
        intent.putExtra("form_type", "existing_user");
        startActivity(intent);
        getActivity().finish();
    }
}

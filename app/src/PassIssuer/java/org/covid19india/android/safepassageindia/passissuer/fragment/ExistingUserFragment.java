package org.covid19india.android.safepassageindia.passissuer.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.covid19india.android.safepassageindia.R;
import org.covid19india.android.safepassageindia.RetrofitClient;
import org.covid19india.android.safepassageindia.ServerApi;
import org.covid19india.android.safepassageindia.model.User;
import org.covid19india.android.safepassageindia.model.UserList;
import org.covid19india.android.safepassageindia.passissuer.activity.CameraActivity;
import org.covid19india.android.safepassageindia.passissuer.activity.FormActivity;

import java.util.Objects;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExistingUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExistingUserFragment extends Fragment {
    private static final String TAG = "ExistingUserFragment";
    private TextInputEditText phoneText;
    private Button verifyButton;
    private User user;
    private ProgressBar progressBar;

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
                view.setEnabled(false);
                if (validate()) {
                    progressBar.setVisibility(View.VISIBLE);
                    verifyUser();
                } else {
                    view.setEnabled(true);
                }
            }
        });
        return view;
    }

    private void verifyUser() {
        Retrofit retrofit = RetrofitClient.getClient(ServerApi.BASE_URL);
        ServerApi serverApi = retrofit.create(ServerApi.class);
        serverApi.getUsers(RetrofitClient.getSession(Objects.requireNonNull(getContext())), "json", phoneText.getText().toString().trim(), "2")
                .enqueue(new Callback<UserList>() {
                    @Override
                    public void onResponse(Call<UserList> call, Response<UserList> response) {
                        if (response.isSuccessful() && response.code() / 100 == 2) {
                            if (response.body() != null && response.body().getUsers().size() == 1) {
                                user = response.body().getUsers().get(0);
                                Intent intent = new Intent(getContext(), CameraActivity.class);
                                intent.putExtra("user_id", user.getUser_id() + "");
                                intent.putExtra("user_phoneNumber", user.getUser_phoneNumber());
                                startActivity(intent);
                                Objects.requireNonNull(getActivity()).finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Log.d(TAG, "No user found");
                                Intent intent = new Intent(getContext(), FormActivity.class);
                                intent.putExtra("form_type", "new_user");
                                startActivity(intent);
                                Objects.requireNonNull(getActivity()).finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserList> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "Connection Failure");
                        Toast.makeText(getContext(), "Unable to connect to server", Toast.LENGTH_SHORT).show();
                    }
                });
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

    private void init(View view) {
        verifyButton = view.findViewById(R.id.btn_verify);
        verifyButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.blue_button));

        phoneText = view.findViewById(R.id.phoneEdit);
        progressBar = view.findViewById(R.id.loading_progress);
    }
}

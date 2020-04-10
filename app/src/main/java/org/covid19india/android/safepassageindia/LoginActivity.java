package org.covid19india.android.safepassageindia;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.covid19india.android.passissuer.MainActivity;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class LoginActivity extends AppCompatActivity {
    private EditText editText, otpEdit;
    private ImageButton button;
    private Button verifyButton;
    private ProgressBar sendProgress, verifyProgress;
    private FirebaseAuth mAuth;
    private ConstraintLayout layout;
    private String mVerificationId;
    private RelativeLayout relativeLayout;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private static final String TAG = "LoginActivity";
    private final TextWatcher phoneWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.length() == 0) {
                button.setVisibility(View.GONE);
                button.setEnabled(false);
            } else {
                button.setVisibility(View.VISIBLE);
                button.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    private final TextWatcher otpWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(charSequence.length()==6){
                verifyButton.setVisibility(View.VISIBLE);
                otpEdit.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
            else{
                verifyButton.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            sendProgress.setVisibility(View.GONE);
            Log.d(TAG, "Authorization verified");
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            sendProgress.setVisibility(View.GONE);
            Log.d(TAG, "Authorization failed", e);
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
                final Snackbar snackbar = Snackbar.make(layout, "Invalid Request", Snackbar.LENGTH_LONG);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                }).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                sendProgress.setVisibility(View.GONE);
                // The SMS quota for the project has been exceeded
                // ...
                Log.d(TAG, "SMS quota for the project is exceeded");
                final Snackbar snackbar = Snackbar.make(layout, "SMS quota for the project is exceeded", Snackbar.LENGTH_LONG);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                }).show();
            }
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            sendProgress.setVisibility(View.GONE);
            Log.d(TAG, "onCodeSent:" + verificationId);
            final Snackbar snackbar = Snackbar.make(layout, "OTP sent to your Mobile number", Snackbar.LENGTH_LONG);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            }).show();
            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;
            relativeLayout.setVisibility(View.VISIBLE);
            // ...
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(LoginActivity.this);
        mAuth = FirebaseAuth.getInstance();
        editText = findViewById(R.id.phoneEdit);
        relativeLayout = findViewById(R.id.relative_otp);
        button = findViewById(R.id.sendButton);
        editText.addTextChangedListener(phoneWatcher);
        otpEdit = findViewById(R.id.otpEdit);
        otpEdit.addTextChangedListener(otpWatcher);
        verifyButton = findViewById(R.id.verifyButton);
        sendProgress = findViewById(R.id.sendProgress);
        verifyProgress = findViewById(R.id.verifyProgress);
        layout = findViewById(R.id.parent_layout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendProgress.setVisibility(View.VISIBLE);
                editText.onEditorAction(EditorInfo.IME_ACTION_DONE);
                String phoneNumber = editText.getText().toString().trim();
                if (phoneNumber.length() < 10) {
                    editText.setError("Invalid number");
                    editText.requestFocus();
                    sendProgress.setVisibility(View.GONE);
                } else {
                    startPhoneNumberVerification("+91" + phoneNumber);
                }
            }
        });
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyProgress.setVisibility(View.VISIBLE);
                String code = otpEdit.getText().toString().trim();
                if (code.length() < 6) {
                    otpEdit.requestFocus();
                    verifyProgress.setVisibility(View.GONE);
                } else {
                    verifyPhoneNumberWithCode(mVerificationId, code);
                }
            }
        });
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]
//        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                verifyProgress.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success");

                    FirebaseUser user = task.getResult().getUser();
                    if (getString(R.string.app_name).equals("Pass Issuer")) {
                        login();
                    }
                    // [START_EXCLUDE]
//                    updateUI(STATE_SIGNIN_SUCCESS, user);
                    // [END_EXCLUDE]
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.d(TAG, "signInWithCredential:failure", task.getException());
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        // [START_EXCLUDE silent]
                        otpEdit.setText("");
                        otpEdit.setError("Invalid code.");
                        otpEdit.requestFocus();
                        // [END_EXCLUDE]
                    }
                    // [START_EXCLUDE silent]
                    // Update UI
//                    updateUI(STATE_SIGNIN_FAILED);
                    // [END_EXCLUDE]
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() != null) {
            login();
        }
    }

    private void login() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

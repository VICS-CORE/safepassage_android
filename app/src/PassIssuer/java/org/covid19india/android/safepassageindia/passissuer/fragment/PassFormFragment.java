package org.covid19india.android.safepassageindia.passissuer.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.internal.LinkedTreeMap;

import org.covid19india.android.safepassageindia.R;
import org.covid19india.android.safepassageindia.RetrofitClient;
import org.covid19india.android.safepassageindia.ServerApi;
import org.covid19india.android.safepassageindia.model.Pass;

import java.io.File;
import java.util.Date;
import java.util.Objects;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassFormFragment extends Fragment {
    private static final String ARG_FILENAME = "file_name";

    private static final String ARG_ID = "user_id";
    private static final String ARG_PHONE = "user_phoneNumber";
    private static final String TAG = "PassFormFragment";

    private String userId, phoneNumber, selectedPassType, selectedMedVerification, fileName;
    private ImageView userImage;
    private View fromLayout, tillLayout;
    private Spinner typeSpinner, medicalSpinner;
    private TextInputEditText fromDateEdit, fromTimeEdit;
    private TextInputEditText tillDateEdit, tillTimeEdit;
    private TextInputEditText reasonEdit, zipCodeEdit, areaEdit;
    private TextInputLayout tilZipCode, tilArea;
    private ProgressBar progressBar;
    private Button createButton;
    private DateTime fromDateTime, tillDateTime;
    private static final String[] types = {"Pass Type", "Permanent pass", "One Time pass", "Temporary pass"}, medicalVerification = {"Medical Verification", "Yes", "No"};

    private static class DateTime {
        private int year, month, day, hour, min, sec;

        DateTime() {
        }

        public DateTime(int year, int month, int day, int hour, int min, int sec) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.min = min;
            this.sec = sec;
        }

        void setDate(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        void setTime(int hour, int min, int sec) {
            this.hour = hour;
            this.min = min;
            this.sec = 0;
        }

        String getDateFormat() {
            String strDate;
            Date date;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, hour, min, sec);
                date = calendar.getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                strDate = dateFormat.format(date);
            } else {
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.set(year, month, day, hour, min, sec);
                date = calendar.getTime();
                @SuppressLint("SimpleDateFormat") java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                strDate = dateFormat.format(date);
            }
            return strDate;
        }
    }

    public PassFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id          Id of the issuer using this app.
     * @param phoneNumber Phone number of the pass user.
     * @param fileName    Name of the file containing the user image
     * @return A new instance of fragment PassFormFragment.
     */
    public static PassFormFragment newInstance(String id, String phoneNumber, String fileName) {
        PassFormFragment fragment = new PassFormFragment();
        Bundle args = new Bundle();

        args.putString(ARG_FILENAME, fileName);
        args.putString(ARG_ID, id);
        args.putString(ARG_PHONE, phoneNumber);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fileName = getArguments().getString(ARG_FILENAME);

            userId = getArguments().getString(ARG_ID);
            phoneNumber = getArguments().getString(ARG_PHONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pass_form, container, false);
        init(view);
        setTypeSpinner(view);
        setMedicalSpinner(view);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateForm()) {
                    Pass pass = createPassObject();
                    progressBar.setVisibility(View.VISIBLE);
                    submitPass(pass);
                }
            }
        });

        fromLayout = view.findViewById(R.id.from_layout);
        tillLayout = view.findViewById(R.id.till_layout);
        setDateAndTimePicker((TextInputLayout) fromLayout.findViewById(R.id.date_picker), (TextInputLayout) fromLayout.findViewById(R.id.time_picker),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        ((TextInputLayout) fromLayout.findViewById(R.id.date_picker)).setEnabled(true);
                        fromDateTime.setDate(year, month, day);
                        String dateFormat = day + "/" + (month + 1) + "/" + year;
                        fromDateEdit.setText(dateFormat);
                    }
                },
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        ((TextInputLayout) fromLayout.findViewById(R.id.time_picker)).setEnabled(true);
                        fromDateTime.setTime(hour, minute, 0);
                        String setHour = hour + "";
                        String setMinute = minute + "";
                        if (hour < 10) {
                            setHour = "0" + setHour;
                        }
                        if (minute < 10) {
                            setMinute = "0" + setMinute;
                        }
                        String timeFormat = setHour + ":" + setMinute;
                        fromTimeEdit.setText(timeFormat);
                    }
                });
        setDateAndTimePicker((TextInputLayout) tillLayout.findViewById(R.id.date_picker), (TextInputLayout) tillLayout.findViewById(R.id.time_picker),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        ((TextInputLayout) tillLayout.findViewById(R.id.date_picker)).setEnabled(true);
                        tillDateTime.setDate(year, month, day);
                        String dateFormat = day + "/" + (month + 1) + "/" + year;
                        tillDateEdit.setText(dateFormat);
                    }
                },
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        ((TextInputLayout) tillLayout.findViewById(R.id.time_picker)).setEnabled(true);
                        tillDateTime.setTime(hour, minute, 0);
                        String setHour = hour + "";
                        String setMinute = minute + "";
                        if (hour < 10) {
                            setHour = "0" + setHour;
                        }
                        if (minute < 10) {
                            setMinute = "0" + setMinute;
                        }
                        String timeFormat = setHour + ":" + setMinute;
                        tillTimeEdit.setText(timeFormat);
                    }
                });
        return view;
    }

    private void submitPass(Pass pass) {
        ServerApi.EnclosedPass enclosedPass = new ServerApi.EnclosedPass(pass);
        Retrofit retrofit = RetrofitClient.getClient(ServerApi.BASE_URL);
        ServerApi serverApi = retrofit.create(ServerApi.class);
        serverApi.submitPass(RetrofitClient.getSession(getContext()), enclosedPass)
                .enqueue(new Callback<LinkedTreeMap<String, String>>() {
                    @Override
                    public void onResponse(Call<LinkedTreeMap<String, String>> call, Response<LinkedTreeMap<String, String>> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.code() / 100 == 2) {
                            Log.d(TAG, "submitPass successful, code = " + response.code());
                            LinkedTreeMap<String, String> task = response.body();
                            String msg = task.get("success");
                            if (msg != null) {
                                Log.d(TAG, msg);
                            }
                            Toast.makeText(getContext(), "Pass Created", Toast.LENGTH_SHORT).show();
                            Objects.requireNonNull(getActivity()).finish();
                        } else {
                            Log.d(TAG, "submitPass not successful, code = " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<LinkedTreeMap<String, String>> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.d(TAG, "Server cannot be accessed");
                        t.printStackTrace();
                    }
                });
    }

    private boolean validateForm() {
        tilZipCode.setError(null);
        tilArea.setError(null);
        if (typeSpinner.getSelectedItemPosition() != 0 && medicalSpinner.getSelectedItemPosition() != 0 &&
                !fromDateEdit.getText().toString().trim().isEmpty() && !fromTimeEdit.getText().toString().trim().isEmpty()
                && !tillDateEdit.getText().toString().trim().isEmpty() && !tillTimeEdit.getText().toString().isEmpty() &&
                !zipCodeEdit.getText().toString().trim().isEmpty() && !areaEdit.getText().toString().trim().isEmpty()) {
            return true;
        } else if (typeSpinner.getSelectedItemPosition() == 0) {
            typeSpinner.performClick();
        } else if (medicalSpinner.getSelectedItemPosition() == 0) {
            medicalSpinner.performClick();
        } else if (fromDateEdit.getText().toString().trim().isEmpty()) {
            ((TextInputLayout) fromLayout.findViewById(R.id.date_picker)).requestFocus();
        } else if (fromTimeEdit.getText().toString().trim().isEmpty()) {
            ((TextInputLayout) fromLayout.findViewById(R.id.time_picker)).requestFocus();
        } else if (tillDateEdit.getText().toString().trim().isEmpty()) {
            ((TextInputLayout) tillLayout.findViewById(R.id.date_picker)).requestFocus();
        } else if (tillTimeEdit.getText().toString().trim().isEmpty()) {
            ((TextInputLayout) tillLayout.findViewById(R.id.time_picker)).requestFocus();
        } else if (zipCodeEdit.getText().toString().trim().length() != 6) {
            if (zipCodeEdit.getText().toString().trim().isEmpty()) {
                tilZipCode.setError("Zip code is mandatory");
            } else {
                tilZipCode.setError("Invalid zip code");
            }
            tilZipCode.requestFocus();
        } else if (areaEdit.getText().toString().trim().isEmpty()) {
            tilArea.setError("Area is mandatory");
            tilArea.requestFocus();
        }
        return false;
    }

    private Pass createPassObject() {
        Pass pass = new Pass();
        pass.setPass_type(selectedPassType);
        pass.setPass_reason(reasonEdit.getText().toString().trim());
        pass.setPass_createdOn(fromDateTime.getDateFormat());
        pass.setPass_expiryDate(tillDateTime.getDateFormat());
        pass.setPass_updatedOn(fromDateTime.getDateFormat());
        pass.setPass_zipCode(zipCodeEdit.getText().toString().trim());
        pass.setPass_radius(areaEdit.getText().toString().trim());
        pass.setPass_medicalVerification(selectedMedVerification);
        pass.setPass_issuedTo(Integer.parseInt(userId));

        String issuerId = RetrofitClient.getUserId(getContext());
        pass.setPass_issuedBy(Integer.parseInt(issuerId));
        pass.setPass_createdBy(Integer.parseInt(issuerId));
        pass.setPass_updatedBy(Integer.parseInt(issuerId));
        return pass;
    }

    private void setDateAndTimePicker(final TextInputLayout dateInputLayout, final TextInputLayout timeInputLayout, final DatePickerDialog.OnDateSetListener dateListener, final TimePickerDialog.OnTimeSetListener timeListener) {
        dateInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CheckableImageButton) view).setEnabled(false);
                int day, month, year;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Calendar calendar = Calendar.getInstance();
                    year = calendar.get(Calendar.YEAR);
                    month = calendar.get(Calendar.MONTH);
                    day = calendar.get(Calendar.DAY_OF_MONTH);
                } else {
                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    year = calendar.get(java.util.Calendar.YEAR);
                    month = calendar.get(java.util.Calendar.MONTH);
                    day = calendar.get(java.util.Calendar.DAY_OF_MONTH);
                }
                DatePickerDialog dialog = new DatePickerDialog(view.getContext(), dateListener, year, month, day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dateInputLayout.setEnabled(true);
                    }
                });
                dialog.show();
            }
        });
        timeInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CheckableImageButton) view).setEnabled(false);
                int hour, minute;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Calendar calendar = Calendar.getInstance();
                    hour = calendar.get(Calendar.HOUR_OF_DAY);
                    minute = calendar.get(Calendar.MINUTE);
                } else {
                    java.util.Calendar calendar = java.util.Calendar.getInstance();
                    hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
                    minute = calendar.get(java.util.Calendar.MINUTE);
                }
                TimePickerDialog dialog = new TimePickerDialog(view.getContext(), timeListener, hour, minute, false);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        timeInputLayout.setEnabled(true);
                    }
                });
                dialog.show();
            }
        });
    }

    private void setTypeSpinner(View view) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_item, types) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = ((TextView) view).getText().toString();
                switch (selected) {
                    case "Permanent pass":
                        selectedPassType = "P";
                        break;
                    case "One Time pass":
                        selectedPassType = "O";
                        break;
                    case "Temporary pass":
                        selectedPassType = "T";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void setMedicalSpinner(View view) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_item, medicalVerification) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        medicalSpinner.setAdapter(adapter);
        medicalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = ((TextView) view).getText().toString().trim();
                switch (selected) {
                    case "Yes":
                        selectedMedVerification = "Y";
                        break;
                    case "No":
                        selectedMedVerification = "N";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void init(View view) {
        createButton = view.findViewById(R.id.btn_create);
        createButton.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.blue_button));

        View fromLayout = view.findViewById(R.id.from_layout);
        View tillLayout = view.findViewById(R.id.till_layout);

        fromDateEdit = fromLayout.findViewById(R.id.dateEdit);
        fromTimeEdit = fromLayout.findViewById(R.id.timeEdit);
        tillDateEdit = tillLayout.findViewById(R.id.dateEdit);
        tillTimeEdit = tillLayout.findViewById(R.id.timeEdit);

        reasonEdit = view.findViewById(R.id.reasonEdit);
        zipCodeEdit = view.findViewById(R.id.zipCodeEdit);
        areaEdit = view.findViewById(R.id.areaEdit);

        typeSpinner = view.findViewById(R.id.pass_type);
        medicalSpinner = view.findViewById(R.id.medical_verification);

        userImage = view.findViewById(R.id.user_pic);
        userImage.setImageBitmap(getUserImage());

        fromDateTime = new DateTime();
        tillDateTime = new DateTime();

        tilZipCode = view.findViewById(R.id.til_zipCode);
        tilArea = view.findViewById(R.id.til_area);

        progressBar = view.findViewById(R.id.loading_progress);

        //Set phone number in edit text
        ((TextInputEditText) view.findViewById(R.id.phoneEdit)).setText(phoneNumber);
    }

    private Bitmap getUserImage() {
        return BitmapFactory.decodeFile(getContext().getCacheDir() + "/SafePassage/" + fileName + ".jpg");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        File file = new File(getContext().getCacheDir() + "/SafePassage/" + fileName + ".jpg");
        boolean isFileDeleted = file.delete();
        Log.d(TAG, "File Status : " + isFileDeleted);
    }
}

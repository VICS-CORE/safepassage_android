package org.covid19india.android.safepassageindia.passissuer.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.covid19india.android.safepassageindia.R;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassFormFragment extends Fragment {
    private static final String ARG_BITMAP = "bitmap";


    private Bitmap bitmap;
    private ImageView userImage;
    private View fromLayout, tillLayout;
    private Spinner typeSpinner, medicalSpinner;
    private TextInputEditText fromDateEdit, fromTimeEdit;
    private TextInputEditText tillDateEdit, tillTimeEdit;
    private Button createButton;
    private Calendar start;
    private static final String[] types = {"Pass Type", "Daily pass", "Permanent pass", "One Time pass"}, medicalVerification = {"Medical Verification", "Yes", "No"};

    public PassFormFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param bitmap Bitmap of User image.
     * @return A new instance of fragment PassFormFragment.
     */
    public static PassFormFragment newInstance(Bitmap bitmap) {
        PassFormFragment fragment = new PassFormFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_BITMAP, bitmap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bitmap = getArguments().getParcelable(ARG_BITMAP);
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
                //TODO implement the post request to pass form here
            }
        });

        fromLayout = view.findViewById(R.id.from_layout);
        tillLayout = view.findViewById(R.id.till_layout);
        setDateAndTimePicker((TextInputLayout) fromLayout.findViewById(R.id.date_picker), fromDateEdit, (TextInputLayout) fromLayout.findViewById(R.id.time_picker), fromTimeEdit);
        setDateAndTimePicker((TextInputLayout) tillLayout.findViewById(R.id.date_picker), tillDateEdit, (TextInputLayout) tillLayout.findViewById(R.id.time_picker), tillTimeEdit);
        return view;
    }

    private void setDateAndTimePicker(TextInputLayout dateInputLayout, TextInputEditText dateEditText, TextInputLayout timeInputLayout, TextInputEditText timeEditText) {
        final TextInputEditText editText1 = dateEditText;
        final TextInputEditText editText2 = timeEditText;
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
                DatePickerDialog dialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        ((TextInputLayout) fromLayout.findViewById(R.id.date_picker)).setEnabled(true);
                        String dateFormat = day + "/" + (month + 1) + "/" + year;
                        editText1.setText(dateFormat);
                    }
                }, year, month, day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        ((TextInputLayout) fromLayout.findViewById(R.id.date_picker)).setEnabled(true);
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
                TimePickerDialog dialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        ((TextInputLayout) fromLayout.findViewById(R.id.time_picker)).setEnabled(true);
                        String setHour = hour + "";
                        String setMinute = minute + "";
                        if (hour < 10) {
                            setHour = "0" + setHour;
                        }
                        if (minute < 10) {
                            setMinute = "0" + setMinute;
                        }
                        String timeFormat = setHour + ":" + setMinute;
                        editText2.setText(timeFormat);
                    }
                }, hour, minute, false);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        ((TextInputLayout) fromLayout.findViewById(R.id.time_picker)).setEnabled(true);
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
        typeSpinner = view.findViewById(R.id.pass_type);
        medicalSpinner = view.findViewById(R.id.medical_verification);
        userImage = view.findViewById(R.id.user_pic);
        userImage.setImageBitmap(bitmap);
    }
}

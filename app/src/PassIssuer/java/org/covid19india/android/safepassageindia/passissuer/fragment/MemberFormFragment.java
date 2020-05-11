package org.covid19india.android.safepassageindia.passissuer.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.covid19india.android.safepassageindia.R;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemberFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberFormFragment extends Fragment {
    private Spinner spinner;
    private static final String[] teams = {"Select team", "Team A", "Team B", "Team C", "Team D"};

    public MemberFormFragment() {
        // Required empty public constructor
    }

    public static MemberFormFragment newInstance() {
        MemberFormFragment fragment = new MemberFormFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_member_form, container, false);
        init(view);
        setTypeSpinner(view);
        return view;
    }

    private void init(View view) {
        spinner = view.findViewById(R.id.spinner);
    }

    private void setTypeSpinner(View view) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_item, teams) {
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
        spinner.setAdapter(adapter);
    }
}

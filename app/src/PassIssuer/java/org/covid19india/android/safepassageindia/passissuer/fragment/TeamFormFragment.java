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
 * Use the {@link TeamFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamFormFragment extends Fragment {
    private static final String[] teams = {"Select team", "Team A", "Team B", "Team C", "Team D"};
    private static final String[] roles = {"Select role", "Admin", "Issuer"};
    private Spinner teamSpinner, roleSpinner;

    public TeamFormFragment() {
        // Required empty public constructor
    }

    public static TeamFormFragment newInstance() {
        TeamFormFragment fragment = new TeamFormFragment();
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
        View view = inflater.inflate(R.layout.fragment_team_form, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        teamSpinner = view.findViewById(R.id.spinner_team);
        setSpinner(view, teamSpinner, teams);
        roleSpinner = view.findViewById(R.id.spinner_role);
        setSpinner(view, roleSpinner, roles);
    }

    private void setSpinner(View view, Spinner spinner, String[] list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_item, list) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
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

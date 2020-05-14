package org.covid19india.android.safepassageindia.passissuer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.covid19india.android.safepassageindia.R;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExistingUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExistingUserFragment extends Fragment {
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
        return view;
    }
}

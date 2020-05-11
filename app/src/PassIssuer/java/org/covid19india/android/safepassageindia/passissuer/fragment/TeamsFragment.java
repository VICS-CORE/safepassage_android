package org.covid19india.android.safepassageindia.passissuer.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.covid19india.android.safepassageindia.R;
import org.covid19india.android.safepassageindia.passissuer.activity.FormActivity;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeamsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamsFragment extends Fragment {
    private Button button;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TeamsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeamsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamsFragment newInstance(String param1, String param2) {
        TeamsFragment fragment = new TeamsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teams, container, false);
        button = view.findViewById(R.id.btn_new);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Button) view).setEnabled(false);
                inflateRoleButtons(view);
            }
        });
        return view;
    }

    private void inflateRoleButtons(View view) {
        View dialogView = getLayoutInflater().inflate(R.layout.content_role_button, null);
        Button teamButton = dialogView.findViewById(R.id.btn_team);
        Button memberButton = dialogView.findViewById(R.id.btn_member);
        final BottomSheetDialog dialog = new BottomSheetDialog(view.getContext());
        teamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Button) view).setEnabled(false);
                Intent intent = new Intent(getContext(), FormActivity.class);
                intent.putExtra("form_type", "team");
                startActivity(intent);
                dialog.cancel();
            }
        });
        memberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Button) view).setEnabled(false);
                Intent intent = new Intent(getContext(), FormActivity.class);
                intent.putExtra("form_type", "member");
                startActivity(intent);
                dialog.cancel();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                button.setEnabled(true);
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();
    }
}

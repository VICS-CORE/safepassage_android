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
import org.covid19india.android.safepassageindia.passissuer.activity.CameraActivity;
import org.covid19india.android.safepassageindia.passissuer.activity.FormActivity;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassesFragment extends Fragment {
    private Button newPassButton;

    public PassesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PassesFragment.
     */
    public static PassesFragment newInstance() {
        PassesFragment fragment = new PassesFragment();
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
        View view = inflater.inflate(R.layout.fragment_passes, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        newPassButton = view.findViewById(R.id.btn_new_pass);
        newPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Button) view).setEnabled(false);
                startActivity(new Intent(view.getContext(), CameraActivity.class));
//                inflatePassDialog(view);
            }
        });
    }

    private void inflatePassDialog(View view) {
        View dialogView = getLayoutInflater().inflate(R.layout.content_role_button, null);
        Button newButton = dialogView.findViewById(R.id.btn_1);
        Button existingButton = dialogView.findViewById(R.id.btn_2);
        newButton.setText("New User");
        existingButton.setText("Existing user");
        final BottomSheetDialog dialog = new BottomSheetDialog(view.getContext());
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Button) view).setEnabled(false);
                Intent intent = new Intent(getContext(), FormActivity.class);
                intent.putExtra("form_type", "new_user");
                startActivity(intent);
                dialog.cancel();
            }
        });
        existingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Button) view).setEnabled(false);
                Intent intent = new Intent(getContext(), FormActivity.class);
                intent.putExtra("form_type", "existing_user");
                startActivity(intent);
                dialog.cancel();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                newPassButton.setEnabled(true);
            }
        });
        dialog.setContentView(dialogView);
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        newPassButton.setEnabled(true);
    }
}

package org.covid19india.android.safepassageindia.passissuer.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.covid19india.android.safepassageindia.R;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassFormFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassFormFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_BITMAP = "bitmap";


    private Bitmap bitmap;
    private ImageView userImage;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pass_form, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        userImage = view.findViewById(R.id.user_pic);
        userImage.setImageBitmap(bitmap);
    }
}

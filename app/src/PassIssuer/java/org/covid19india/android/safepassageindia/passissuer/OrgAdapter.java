package org.covid19india.android.safepassageindia.passissuer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.covid19india.android.safepassageindia.R;

import java.util.ArrayList;
import java.util.List;


public class OrgAdapter extends ArrayAdapter<OrgUserInfo> {

    private Context mContext;
    private List<OrgUserInfo> mOrgUserInfoList = new ArrayList<>();

    public OrgAdapter(Context context, List<OrgUserInfo> list) {
        super(context, 0 , list);
        mContext = context;
        mOrgUserInfoList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_list,parent,false);

        OrgUserInfo orgUserInfoList = mOrgUserInfoList.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.imageView_poster);
        image.setImageResource(R.drawable.img);

        TextView name = (TextView) listItem.findViewById(R.id.textView_name);
        name.setText(orgUserInfoList.getUserName());

        TextView release = (TextView) listItem.findViewById(R.id.textView_role);
        release.setText(orgUserInfoList.getUserType().toString());

        return listItem;
    }
}

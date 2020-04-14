package org.covid19india.android.safepassageindia.passissuer;

import android.os.Parcel;
import android.os.Parcelable;

public class OrgUserInfo implements Parcelable {
    private String mDesignation;
    private UserInfo mUserInfo;

    public OrgUserInfo(UserInfo userInfo, String designation){
        mUserInfo = userInfo;
        mDesignation = designation;
    }

    private OrgUserInfo(Parcel parcel) {
        mUserInfo = parcel.readParcelable(UserInfo.class.getClassLoader());
        mDesignation = parcel.readString();
    }

    public String getUserName() { return mUserInfo.getFirstName() +' ' + mUserInfo.getLastName(); }

    public String getUserMobileNumber() { return mUserInfo.getUserMobileNumber(); }

    public UserInfo.UserType getUserType() { return mUserInfo.getUserType(); }

    public String getUserDesignation(){return mDesignation;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mUserInfo, 0);
        dest.writeString(mDesignation);
    }

    public static final Parcelable.Creator<OrgUserInfo> CREATOR =
            new Parcelable.Creator<OrgUserInfo>() {
                @Override
                public OrgUserInfo createFromParcel(Parcel parcel) {
                    return new OrgUserInfo(parcel);
                }

                @Override
                public OrgUserInfo[] newArray(int size) {
                    return new OrgUserInfo[size];
                }
            };
}

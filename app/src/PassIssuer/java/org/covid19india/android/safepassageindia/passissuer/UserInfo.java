package org.covid19india.android.safepassageindia.passissuer;

import android.os.Parcel;
import android.os.Parcelable;

public final class UserInfo implements Parcelable {

    public enum UserType {USER, ADMIN, ISSUER}
    private String mFirstName;
    private String mLastName;
    private String mMobileNumber;
    private UserType mUserType;

    public UserInfo(String firstName, String lastName, String mobileNumber, UserType userType) {
        mFirstName = firstName;
        mLastName = lastName;
        mMobileNumber = mobileNumber;
        mUserType = userType;
    }

    private UserInfo(Parcel source) {
        mFirstName = source.readString();
        mLastName = source.readString();
        mMobileNumber = source.readString();
        mUserType = UserType.valueOf(source.readString());

    }

    public String getFirstName() { return mFirstName; }

    public UserType getUserType(){ return mUserType; }

    public void setUserType(UserType userType){ mUserType = userType; }

    public void setFirstName(String firstName) { mFirstName = firstName; }

    public String getLastName() { return mLastName; }

    public String getUserMobileNumber() { return  mMobileNumber;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mMobileNumber);
        dest.writeString(mUserType.name());
    }

    public static final Creator<UserInfo> CREATOR =
            new Creator<UserInfo>() {

                @Override
                public UserInfo createFromParcel(Parcel source) {
                    return new UserInfo(source);
                }

                @Override
                public UserInfo[] newArray(int size) {
                    return new UserInfo[size];
                }
            };

}

package org.covid19india.android.safepassageindia.passissuer;

import android.os.Parcel;
import android.os.Parcelable;

public final class Address implements Parcelable {
    UserInfo mUserInfo;
    private int mHouseNumber;
    private String mStreetAddress;
    private String mCity;
    private String mState;
    private String mZipCode;

    private Address(Parcel source) {
        mUserInfo = source.readParcelable(UserInfo.class.getClassLoader());
        mHouseNumber = source.readInt();
        mStreetAddress = source.readString();
        mCity = source.readString();
        mState = source.readString();
        mZipCode = source.readString();
    }

    public UserInfo getPerson() {
        return mUserInfo;
    }

    public void setPerson(UserInfo userInfo) {
        mUserInfo = userInfo;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String city) {
        mCity = city;
    }

    public String getState() {
        return mState;
    }

    public void setState(String state) {
        mState = state;
    }

    public String getZipCode() {
        return mZipCode;
    }

    public void setZipCode(String zipCode) {
        mZipCode = zipCode;
    }

    public int getHouseNumber() {
        return mHouseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        mHouseNumber = houseNumber;
    }

    public String getStreetAddress() {
        return mStreetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        mStreetAddress = streetAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mUserInfo,0);
        dest.writeInt(mHouseNumber);
        dest.writeString(mStreetAddress);
        dest.writeString(mCity);
        dest.writeString(mState);
        dest.writeString(mZipCode);
    }

    public static final Parcelable.Creator<Address> CREATOR =
            new Parcelable.Creator<Address>() {
                @Override
                public Address createFromParcel(Parcel parcel) {
                    return new Address(parcel);
                }

                @Override
                public Address[] newArray(int size) {
                    return new Address[size];
                }
            };

}

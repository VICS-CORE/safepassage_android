package org.covid19india.android.safepassageindia.passissuer;

import android.os.Parcel;
import android.os.Parcelable;

public class EndUserInfo implements Parcelable {

    private UserInfo mUserInfo;
    public enum IdCardType {AADHAAR, DL, PAN};
    private String mGender;
    private Address mAddress;
    private String mOrganisationId;
    private IdCardType mIdcardType;

    public EndUserInfo(UserInfo userInfo, String gender, String mobileNumber, Address address, String organisationId, IdCardType idcardType) {
        mUserInfo = userInfo;
        mGender = gender;
        mAddress = address;
        mIdcardType = idcardType;
        mOrganisationId = organisationId;
    }

    private EndUserInfo(Parcel parcel) {
        mUserInfo = parcel.readParcelable(UserInfo.class.getClassLoader());
        mGender = parcel.readString();
        mAddress = parcel.readParcelable(Address.class.getClassLoader());
        mIdcardType = IdCardType.valueOf(parcel.readString());
        mOrganisationId = parcel.readString();
    }

    public Address getAddress() { return mAddress; }


    public void setAddress(Address address) { mAddress = address; }

    public String getPersonAddress() {
        String actualAddress= mAddress.getHouseNumber() + mAddress.getStreetAddress() + ", " + mAddress.getCity() + ", " + mAddress.getState() + "  " + mAddress.getZipCode();
        return actualAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mUserInfo, 0);
        dest.writeString(mGender);
        dest.writeParcelable(mAddress,0);
        dest.writeString(mIdcardType.name());
        dest.writeString(mOrganisationId);
    }

    public static final Parcelable.Creator<EndUserInfo> CREATOR =
            new Parcelable.Creator<EndUserInfo>() {
                @Override
                public EndUserInfo createFromParcel(Parcel parcel) {
                    return new EndUserInfo(parcel);
                }

                @Override
                public EndUserInfo[] newArray(int size) {
                    return new EndUserInfo[size];
                }
            };

}

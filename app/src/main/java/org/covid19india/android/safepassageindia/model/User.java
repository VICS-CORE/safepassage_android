package org.covid19india.android.safepassageindia.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {
    private Integer user_id;
    @SerializedName("user_createdon")
    private String user_createdOn;
    @SerializedName("user_updatedon")
    private String user_updatedOn;
    @SerializedName("user_firstname")
    private String user_firstName;
    @SerializedName("user_lastname")
    private String user_lastName;
    @SerializedName("user_middlename")
    private String user_middleName;
    private String user_gender;
    private String user_image;
    @SerializedName("user_phonenumber")
    private String user_phoneNumber;
    @SerializedName("user_altphonenumber")
    private String user_altPhoneNumber;
    @SerializedName("user_address_name")
    private String user_addressName;
    @SerializedName("user_address_streetline1")
    private String user_addressStreetLine1;
    @SerializedName("user_address_streetline2")
    private String user_addressStreetLine2;
    @SerializedName("user_address_streetline3")
    private String user_addressStreetLine3;
    @SerializedName("user_address_country")
    private String user_addressCountry;
    @SerializedName("user_address_state")
    private String user_addressState;
    @SerializedName("user_address_city")
    private String user_addressCity;
    @SerializedName("user_address_zipcode")
    private String user_addressZipCode;
    @SerializedName("user_address_latitude")
    private String user_addressLatitude;
    @SerializedName("user_address_longitude")
    private String user_addressLongitude;
    private Integer user_identity;
    @SerializedName("user_teamid")
    private String user_teamId;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_createdOn() {
        return user_createdOn;
    }

    public void setUser_createdOn(String user_createdOn) {
        this.user_createdOn = user_createdOn;
    }

    public String getUser_updatedOn() {
        return user_updatedOn;
    }

    public void setUser_updatedOn(String user_updatedOn) {
        this.user_updatedOn = user_updatedOn;
    }

    public String getUser_firstName() {
        return user_firstName;
    }

    public void setUser_firstName(String user_firstName) {
        this.user_firstName = user_firstName;
    }

    public String getUser_lastName() {
        return user_lastName;
    }

    public void setUser_lastName(String user_lastName) {
        this.user_lastName = user_lastName;
    }

    public String getUser_middleName() {
        return user_middleName;
    }

    public void setUser_middleName(String user_middleName) {
        this.user_middleName = user_middleName;
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_phoneNumber() {
        return user_phoneNumber;
    }

    public void setUser_phoneNumber(String user_phoneNumber) {
        this.user_phoneNumber = user_phoneNumber;
    }

    public String getUser_altPhoneNumber() {
        return user_altPhoneNumber;
    }

    public void setUser_altPhoneNumber(String user_altPhoneNumber) {
        this.user_altPhoneNumber = user_altPhoneNumber;
    }

    public Integer getUser_identity() {
        return user_identity;
    }

    public void setUser_identity(Integer user_identity) {
        this.user_identity = user_identity;
    }

    public String getUser_addressName() {
        return user_addressName;
    }

    public void setUser_addressName(String user_addressName) {
        this.user_addressName = user_addressName;
    }

    public String getUser_addressStreetLine1() {
        return user_addressStreetLine1;
    }

    public void setUser_addressStreetLine1(String user_addressStreetLine1) {
        this.user_addressStreetLine1 = user_addressStreetLine1;
    }

    public String getUser_addressStreetLine2() {
        return user_addressStreetLine2;
    }

    public void setUser_addressStreetLine2(String user_addressStreetLine2) {
        this.user_addressStreetLine2 = user_addressStreetLine2;
    }

    public String getUser_addressStreetLine3() {
        return user_addressStreetLine3;
    }

    public void setUser_addressStreetLine3(String user_addressStreetLine3) {
        this.user_addressStreetLine3 = user_addressStreetLine3;
    }

    public String getUser_addressCountry() {
        return user_addressCountry;
    }

    public void setUser_addressCountry(String user_addressCountry) {
        this.user_addressCountry = user_addressCountry;
    }

    public String getUser_addressState() {
        return user_addressState;
    }

    public void setUser_addressState(String user_addressState) {
        this.user_addressState = user_addressState;
    }

    public String getUser_addressCity() {
        return user_addressCity;
    }

    public void setUser_addressCity(String user_addressCity) {
        this.user_addressCity = user_addressCity;
    }

    public String getUser_addressZipCode() {
        return user_addressZipCode;
    }

    public void setUser_addressZipCode(String user_addressZipCode) {
        this.user_addressZipCode = user_addressZipCode;
    }

    public String getUser_addressLatitude() {
        return user_addressLatitude;
    }

    public void setUser_addressLatitude(String user_addressLatitude) {
        this.user_addressLatitude = user_addressLatitude;
    }

    public String getUser_addressLongitude() {
        return user_addressLongitude;
    }

    public void setUser_addressLongitude(String user_addressLongitude) {
        this.user_addressLongitude = user_addressLongitude;
    }

    public String getUser_teamId() {
        return user_teamId;
    }

    public void setUser_teamId(String user_teamId) {
        this.user_teamId = user_teamId;
    }

    public User() {
    }

    public User(Integer user_id, String user_createdOn, String user_updatedOn, String user_firstName, String user_lastName, String user_middleName, String user_gender, String user_image, String user_phoneNumber, String user_altPhoneNumber, String user_addressName, String user_addressStreetLine1, String user_addressStreetLine2, String user_addressStreetLine3, String user_addressCountry, String user_addressState, String user_addressCity, String user_addressZipCode, String user_addressLatitude, String user_addressLongitude, Integer user_identity, String user_teamId) {
        this.user_id = user_id;
        this.user_createdOn = user_createdOn;
        this.user_updatedOn = user_updatedOn;
        this.user_firstName = user_firstName;
        this.user_lastName = user_lastName;
        this.user_middleName = user_middleName;
        this.user_gender = user_gender;
        this.user_image = user_image;
        this.user_phoneNumber = user_phoneNumber;
        this.user_altPhoneNumber = user_altPhoneNumber;
        this.user_addressName = user_addressName;
        this.user_addressStreetLine1 = user_addressStreetLine1;
        this.user_addressStreetLine2 = user_addressStreetLine2;
        this.user_addressStreetLine3 = user_addressStreetLine3;
        this.user_addressCountry = user_addressCountry;
        this.user_addressState = user_addressState;
        this.user_addressCity = user_addressCity;
        this.user_addressZipCode = user_addressZipCode;
        this.user_addressLatitude = user_addressLatitude;
        this.user_addressLongitude = user_addressLongitude;
        this.user_identity = user_identity;
        this.user_teamId = user_teamId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.user_id);
        dest.writeString(this.user_createdOn);
        dest.writeString(this.user_updatedOn);
        dest.writeString(this.user_firstName);
        dest.writeString(this.user_lastName);
        dest.writeString(this.user_middleName);
        dest.writeString(this.user_gender);
        dest.writeString(this.user_image);
        dest.writeString(this.user_phoneNumber);
        dest.writeString(this.user_altPhoneNumber);
        dest.writeString(this.user_addressName);
        dest.writeString(this.user_addressStreetLine1);
        dest.writeString(this.user_addressStreetLine2);
        dest.writeString(this.user_addressStreetLine3);
        dest.writeString(this.user_addressCountry);
        dest.writeString(this.user_addressState);
        dest.writeString(this.user_addressCity);
        dest.writeString(this.user_addressZipCode);
        dest.writeString(this.user_addressLatitude);
        dest.writeString(this.user_addressLongitude);
        dest.writeValue(this.user_identity);
        dest.writeString(this.user_teamId);
    }

    protected User(Parcel in) {
        this.user_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_createdOn = in.readString();
        this.user_updatedOn = in.readString();
        this.user_firstName = in.readString();
        this.user_lastName = in.readString();
        this.user_middleName = in.readString();
        this.user_gender = in.readString();
        this.user_image = in.readString();
        this.user_phoneNumber = in.readString();
        this.user_altPhoneNumber = in.readString();
        this.user_addressName = in.readString();
        this.user_addressStreetLine1 = in.readString();
        this.user_addressStreetLine2 = in.readString();
        this.user_addressStreetLine3 = in.readString();
        this.user_addressCountry = in.readString();
        this.user_addressState = in.readString();
        this.user_addressCity = in.readString();
        this.user_addressZipCode = in.readString();
        this.user_addressLatitude = in.readString();
        this.user_addressLongitude = in.readString();
        this.user_identity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.user_teamId = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

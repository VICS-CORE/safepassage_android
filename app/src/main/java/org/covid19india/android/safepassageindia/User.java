package org.covid19india.android.safepassageindia;

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
    @SerializedName("user_addressid")
    private String user_addressId;
    private String user_identity;

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

    public String getUser_addressId() {
        return user_addressId;
    }

    public void setUser_addressId(String user_addressId) {
        this.user_addressId = user_addressId;
    }

    public String getUser_identity() {
        return user_identity;
    }

    public void setUser_identity(String user_identity) {
        this.user_identity = user_identity;
    }

    public User(Integer user_id, String user_createdOn, String user_updatedOn, String user_firstName, String user_lastName, String user_middleName, String user_gender, String user_image, String user_phoneNumber, String user_altPhoneNumber, String user_addressId, String user_identity) {
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
        this.user_addressId = user_addressId;
        this.user_identity = user_identity;
    }

    public User() {
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
        dest.writeString(this.user_addressId);
        dest.writeString(this.user_identity);
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
        this.user_addressId = in.readString();
        this.user_identity = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
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

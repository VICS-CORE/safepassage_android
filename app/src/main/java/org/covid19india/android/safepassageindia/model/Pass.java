package org.covid19india.android.safepassageindia.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Pass implements Parcelable {
    @SerializedName("pass_id")
    private Integer pass_id;
    @SerializedName("pass_passtype")
    private String pass_type;
    @SerializedName("pass_passreason")
    private String pass_reason;
    @SerializedName("pass_createdon")
    private String pass_createdOn;
    @SerializedName("pass_expirydate")
    private String pass_expiryDate;
    @SerializedName("pass_validitystate")
    private Integer pass_validityState;
    @SerializedName("pass_zipcode")
    private String pass_zipCode;
    @SerializedName("pass_radius")
    private String pass_radius;
    @SerializedName("pass_medicalverification")
    private String pass_medicalVerification;
    @SerializedName("pass_issuedby")
    private Integer pass_issuedBy;
    @SerializedName("pass_issuedto")
    private Integer pass_issuedTo;
    @SerializedName("pass_createdby")
    private Integer pass_createdBy;
    @SerializedName("pass_updatedby")
    private Integer pass_updatedBy;

    public Pass() {
    }

    public Integer getPass_id() {
        return pass_id;
    }

    public void setPass_id(Integer pass_id) {
        this.pass_id = pass_id;
    }

    public String getPass_type() {
        return pass_type;
    }

    public void setPass_type(String pass_type) {
        this.pass_type = pass_type;
    }

    public String getPass_reason() {
        return pass_reason;
    }

    public void setPass_reason(String pass_reason) {
        this.pass_reason = pass_reason;
    }

    public String getPass_createdOn() {
        return pass_createdOn;
    }

    public void setPass_createdOn(String pass_createdOn) {
        this.pass_createdOn = pass_createdOn;
    }

    public String getPass_expiryDate() {
        return pass_expiryDate;
    }

    public void setPass_expiryDate(String pass_expiryDate) {
        this.pass_expiryDate = pass_expiryDate;
    }

    public Integer getPass_validityState() {
        return pass_validityState;
    }

    public void setPass_validityState(Integer pass_validityState) {
        this.pass_validityState = pass_validityState;
    }

    public String getPass_zipCode() {
        return pass_zipCode;
    }

    public void setPass_zipCode(String pass_zipCode) {
        this.pass_zipCode = pass_zipCode;
    }

    public String getPass_radius() {
        return pass_radius;
    }

    public void setPass_radius(String pass_radius) {
        this.pass_radius = pass_radius;
    }

    public String getPass_medicalVerification() {
        return pass_medicalVerification;
    }

    public void setPass_medicalVerification(String pass_medicalVerification) {
        this.pass_medicalVerification = pass_medicalVerification;
    }

    public Integer getPass_issuedBy() {
        return pass_issuedBy;
    }

    public void setPass_issuedBy(Integer pass_issuedBy) {
        this.pass_issuedBy = pass_issuedBy;
    }

    public Integer getPass_issuedTo() {
        return pass_issuedTo;
    }

    public void setPass_issuedTo(Integer pass_issuedTo) {
        this.pass_issuedTo = pass_issuedTo;
    }

    public Integer getPass_createdBy() {
        return pass_createdBy;
    }

    public void setPass_createdBy(Integer pass_createdBy) {
        this.pass_createdBy = pass_createdBy;
    }

    public Integer getPass_updatedBy() {
        return pass_updatedBy;
    }

    public void setPass_updatedBy(Integer pass_updatedBy) {
        this.pass_updatedBy = pass_updatedBy;
    }

    public Pass(Integer pass_id, String pass_type, String pass_reason, String pass_createdOn, String pass_expiryDate, Integer pass_validityState, String pass_zipCode, String pass_radius, String pass_medicalVerification, Integer pass_issuedBy, Integer pass_issuedTo, Integer pass_createdBy, Integer pass_updatedBy) {
        this.pass_id = pass_id;
        this.pass_type = pass_type;
        this.pass_reason = pass_reason;
        this.pass_createdOn = pass_createdOn;
        this.pass_expiryDate = pass_expiryDate;
        this.pass_validityState = pass_validityState;
        this.pass_zipCode = pass_zipCode;
        this.pass_radius = pass_radius;
        this.pass_medicalVerification = pass_medicalVerification;
        this.pass_issuedBy = pass_issuedBy;
        this.pass_issuedTo = pass_issuedTo;
        this.pass_createdBy = pass_createdBy;
        this.pass_updatedBy = pass_updatedBy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.pass_id);
        dest.writeString(this.pass_type);
        dest.writeString(this.pass_reason);
        dest.writeString(this.pass_createdOn);
        dest.writeString(this.pass_expiryDate);
        dest.writeValue(this.pass_validityState);
        dest.writeString(this.pass_zipCode);
        dest.writeString(this.pass_radius);
        dest.writeString(this.pass_medicalVerification);
        dest.writeValue(this.pass_issuedBy);
        dest.writeValue(this.pass_issuedTo);
        dest.writeValue(this.pass_createdBy);
        dest.writeValue(this.pass_updatedBy);
    }

    protected Pass(Parcel in) {
        this.pass_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pass_type = in.readString();
        this.pass_reason = in.readString();
        this.pass_createdOn = in.readString();
        this.pass_expiryDate = in.readString();
        this.pass_validityState = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pass_zipCode = in.readString();
        this.pass_radius = in.readString();
        this.pass_medicalVerification = in.readString();
        this.pass_issuedBy = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pass_issuedTo = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pass_createdBy = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pass_updatedBy = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Pass> CREATOR = new Parcelable.Creator<Pass>() {
        @Override
        public Pass createFromParcel(Parcel source) {
            return new Pass(source);
        }

        @Override
        public Pass[] newArray(int size) {
            return new Pass[size];
        }
    };
}

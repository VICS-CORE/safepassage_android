package org.covid19india.android.safepassageindia;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PassList implements Parcelable {
    @SerializedName("user")
    private List<User> users;
    @SerializedName("userpass")
    private List<Pass> passes;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Pass> getPasses() {
        return passes;
    }

    public void setPasses(List<Pass> passes) {
        this.passes = passes;
    }

    public PassList(List<User> users, List<Pass> passes) {
        this.users = users;
        this.passes = passes;
    }

    public boolean isUniqueUser() {
        return users.size() == 1;
    }

    public void renamePassType() {
        for (Pass pass : passes) {
            if (pass.getPass_type().equals("O")) {
                pass.setPass_type("One Time");
            } else if (pass.getPass_type().equals("P")) {
                pass.setPass_type("Permanent");
            } else if (pass.getPass_type().equals("T")) {
                pass.setPass_type("Temporary");
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.users);
        dest.writeList(this.passes);
    }

    protected PassList(Parcel in) {
        this.users = new ArrayList<User>();
        in.readList(this.users, User.class.getClassLoader());
        this.passes = new ArrayList<Pass>();
        in.readList(this.passes, Pass.class.getClassLoader());
    }

    public static final Parcelable.Creator<PassList> CREATOR = new Parcelable.Creator<PassList>() {
        @Override
        public PassList createFromParcel(Parcel source) {
            return new PassList(source);
        }

        @Override
        public PassList[] newArray(int size) {
            return new PassList[size];
        }
    };
}

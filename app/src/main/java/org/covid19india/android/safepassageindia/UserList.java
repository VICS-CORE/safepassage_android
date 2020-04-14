package org.covid19india.android.safepassageindia;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserList {
    @SerializedName("user")
    List<User> users;

    public UserList(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}

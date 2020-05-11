package org.covid19india.android.safepassageindia.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserList {
    @SerializedName("user")
    private List<User> users;

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

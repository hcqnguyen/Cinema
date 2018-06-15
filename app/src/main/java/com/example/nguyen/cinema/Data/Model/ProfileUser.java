package com.example.nguyen.cinema.Data.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileUser {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("phone")
    @Expose
    private String phone;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

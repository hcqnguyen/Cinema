package com.example.nguyen.cinema.Data.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Film {

    @SerializedName("title")
    @Expose
    String mTitle;
    @SerializedName("genre")
    @Expose
    String mGenre;
    String mRelease;
    String mIdUser;
    String mDiscription;
    String mCover;

    public String getmCover() {
        return mCover;
    }

    public void setmCover(String mCover) {
        this.mCover = mCover;
    }



    public String getmRelease() {
        return mRelease;
    }

    public void setmRelease(String mRelease) {
        this.mRelease = mRelease;
    }


    public Film() {
    }

    public Film(String mTitle, String mGenre, String mRelease, String mIdUser, String mDiscription) {
        this.mTitle = mTitle;
        this.mGenre = mGenre;

        this.mIdUser = mIdUser;
        this.mDiscription = mDiscription;
       // this.mCover = mCover;
        this.mRelease = mRelease;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmGenre() {
        return mGenre;
    }

    public void setmGenre(String mGenre) {
        this.mGenre = mGenre;
    }



    public String getmIdUser() {
        return mIdUser;
    }

    public void setmIdUser(String mIdUser) {
        this.mIdUser = mIdUser;
    }

    public String getmDiscription() {
        return mDiscription;
    }

    public void setmDiscription(String mDiscription) {
        this.mDiscription = mDiscription;
    }
}
package com.example.nguyen.cinema.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.example.nguyen.cinema.Data.Model.IconTextView;
import com.example.nguyen.cinema.Data.Model.ProfileUser;
import com.example.nguyen.cinema.Data.Remote.APIService;
import com.example.nguyen.cinema.Data.Remote.ApiUtils;
import com.example.nguyen.cinema.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    IconTextView mIconTextViewEditUsername, getmIconTextViewEditPhoneNumber;
    Button mButtonChangePassword, mButtonSignOut;
    RecyclerView mRecyclerViewListFilm;
    APIService mAPIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mIconTextViewEditUsername = findViewById(R.id.icon_text_view_edit_username);
        getmIconTextViewEditPhoneNumber = findViewById(R.id.icon_text_view_edit_phone_number);
        mButtonChangePassword = findViewById(R.id.button_change_password);
        mButtonSignOut = findViewById(R.id.button_sign_out);
        mRecyclerViewListFilm = findViewById(R.id.recyclerview_list_film);
        mAPIService = ApiUtils.getAPIService();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProfileActivity.this);
        mRecyclerViewListFilm.setLayoutManager(layoutManager);
        mRecyclerViewListFilm.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL);
        mRecyclerViewListFilm.addItemDecoration(dividerItemDecoration);


    }


}

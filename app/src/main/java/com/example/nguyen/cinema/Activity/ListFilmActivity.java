package com.example.nguyen.cinema.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.nguyen.cinema.Data.Adapter.ListFilmAdapter;
import com.example.nguyen.cinema.Data.Model.ResponeApi;
import com.example.nguyen.cinema.Data.Remote.APIService;
import com.example.nguyen.cinema.Data.Remote.ApiUtils;
import com.example.nguyen.cinema.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ListFilmActivity extends AppCompatActivity implements  SwipeRefreshLayout.OnRefreshListener {
    RecyclerView mRecyclerView;
    private ListFilmAdapter mAdapter;
    private APIService mAPIService;
    LinearLayout mLinearLayoutOpenProfile, mLinearLayoutOpenCreateFilm;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_film);

        mRecyclerView = findViewById(R.id.recyclerview_list_film);
        mLinearLayoutOpenCreateFilm = findViewById(R.id.linear_layout_open_create_film);
        mLinearLayoutOpenProfile = findViewById(R.id.linear_layout_go_open_profile);
        SharedPreferences pre = getSharedPreferences("access_token", MODE_PRIVATE);
        mAPIService = ApiUtils.getAPIService(pre.getString("token", ""));

        mAdapter = new ListFilmAdapter(new ArrayList<ResponeApi.Movie>(), ListFilmActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ListFilmActivity.this);


        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFilm();
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        mLinearLayoutOpenProfile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListFilmActivity.this, ProfileActivity.class);
                startActivity(intent, ActivityOptions.makeCustomAnimation(ListFilmActivity.this, R.anim.anim_change_activity_from_left, R.anim.anim_change_activity_from_center_to_right).toBundle());
            }
        });

        mLinearLayoutOpenCreateFilm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListFilmActivity.this, CreateFilmActivity.class);
                startActivity(intent, ActivityOptions.makeCustomAnimation(ListFilmActivity.this, R.anim.anim_change_activity_from_right, R.anim.anim_change_activity_from_center_to_left).toBundle());
            }
        });

        loadFilm();


    }



    private void loadFilm() {
        mAPIService.getFilm().enqueue(new Callback<ResponeApi>() {
            @Override
            public void onResponse(Call<ResponeApi> call, Response<ResponeApi> response) {
                if (response.isSuccessful()) {
                    mAdapter.updateAnswers(response.body().getMovies());
                    Log.i("LISTFILM ACITIVTY", "__");
                    mAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    int statusCode = response.code();
                    Log.e(TAG, response.message() + "__");
                }
            }

            @Override
            public void onFailure(Call<ResponeApi> call, Throwable t) {
                t.printStackTrace();
                Log.d("MainActivity", "error loading from API");

            }
        });
    }



    // TODO clickItemRecyc




    @Override
    public void onRefresh() {

    }
}

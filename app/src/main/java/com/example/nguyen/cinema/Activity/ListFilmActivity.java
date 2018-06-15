package com.example.nguyen.cinema.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.example.nguyen.cinema.Data.Adapter.ListFilmAdapter;
import com.example.nguyen.cinema.Data.Model.Film;
import com.example.nguyen.cinema.Data.Model.IconTextView;
import com.example.nguyen.cinema.Data.Model.ResponeApi;
import com.example.nguyen.cinema.Data.Remote.APIService;
import com.example.nguyen.cinema.Data.Remote.ApiUtils;
import com.example.nguyen.cinema.R;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ListFilmActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    private ListFilmAdapter mAdapter;
    private APIService mAPIService;
    LinearLayout mLinearLayoutOpenProfile, mLinearLayoutOpenCreateFilm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_film);

        mRecyclerView = findViewById(R.id.recyclerview_list_film);
        mLinearLayoutOpenCreateFilm = findViewById(R.id.linear_layout_open_create_film);
        mLinearLayoutOpenProfile = findViewById(R.id.linear_layout_go_open_profile);
        mAPIService = ApiUtils.getAPIService();

        mAdapter = new ListFilmAdapter(new ArrayList<ResponeApi.Movie>(),ListFilmActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListFilmActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerView.setAdapter(mAdapter);

        mLinearLayoutOpenProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(ListFilmActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        mLinearLayoutOpenCreateFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(ListFilmActivity.this,CreateFilmActivity.class);
                startActivity(intent);
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
}

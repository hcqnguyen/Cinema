package com.example.nguyen.cinema.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListAdapter;

import com.example.nguyen.cinema.Data.Adapter.ListFilmAdapter;
import com.example.nguyen.cinema.Data.Model.Film;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_film);

        mRecyclerView = findViewById(R.id.recyclerview_list_film);
        mAPIService = ApiUtils.getAPIService();

        mAdapter = new ListFilmAdapter(new ArrayList<ResponeApi.Movie>(),ListFilmActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListFilmActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerView.setAdapter(mAdapter);

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

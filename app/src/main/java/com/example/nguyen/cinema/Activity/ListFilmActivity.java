package com.example.nguyen.cinema.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListAdapter;

import com.example.nguyen.cinema.Data.Adapter.ListFilmAdapter;
import com.example.nguyen.cinema.Data.Model.Film;
import com.example.nguyen.cinema.R;

import java.util.ArrayList;
import java.util.List;

public class ListFilmActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    private ListFilmAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_film);

        mRecyclerView = findViewById(R.id.recyclerview_list_film);
        List<Film> film = new ArrayList<>();
        film.add(new Film("one piece","Hanh dong","20/02/2018","123458","qua hay"));
        film.add(new Film("Naruto","Hanh dong","20/02/2018","123458","qua hay"));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListFilmActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter = new ListFilmAdapter(film ,getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);


    }
}

package com.example.nguyen.cinema.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.nguyen.cinema.Data.Adapter.ListFilmAdapter;
import com.example.nguyen.cinema.Data.Model.ResponeApi;
import com.example.nguyen.cinema.Data.Remote.APIService;
import com.example.nguyen.cinema.Data.Remote.ApiUtils;
import com.example.nguyen.cinema.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ListFilmActivity extends AppCompatActivity implements  SwipeRefreshLayout.OnRefreshListener, ListFilmAdapter.onClickRecyclerView {
    RecyclerView mRecyclerView;
    private ListFilmAdapter mAdapter;
    private APIService mAPIService;

    TextView mTextViewTitle;
    android.support.v7.widget.SearchView mSearchViewFilm;
    RelativeLayout mRelativeListFilm;
    SwipeRefreshLayout mSwipeRefreshLayout;
    FloatingActionButton mFloatingButtonOpenCreateFilm, mFloatingButtonOpenProfile;

    ArrayList<ResponeApi.Movie> mList;
    final int RESULT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_film);

        mRecyclerView = findViewById(R.id.recyclerview_list_film);
        mFloatingButtonOpenCreateFilm = findViewById(R.id.floattingbutton_openn_create_film);
        mFloatingButtonOpenProfile = findViewById(R.id.floattingbutton_openn_profile);
        mRelativeListFilm = findViewById(R.id.linear_layout_list_film);
        mSearchViewFilm = findViewById(R.id.search_view_film);
        mTextViewTitle  = findViewById(R.id.text_view_list_film_title);
        SharedPreferences pre = getSharedPreferences("access_token", MODE_PRIVATE);

        mAPIService = ApiUtils.getAPIService(pre.getString("token", ""));



        mAdapter = new ListFilmAdapter(new ArrayList<ResponeApi.Movie>(), ListFilmActivity.this,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ListFilmActivity.this);
        loadFilm();
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


        mFloatingButtonOpenProfile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListFilmActivity.this, ProfileActivity.class);


                startActivity(intent, ActivityOptions.makeCustomAnimation(ListFilmActivity.this,
                        R.anim.anim_change_activity_from_left, R.anim.anim_change_activity_from_center_to_right).toBundle());


            }
        });



        mFloatingButtonOpenCreateFilm.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListFilmActivity.this, CreateFilmActivity.class);
               // ListFilmActivity.this.overridePendingTransition(R.anim.anim_change_activity_from_right,R.anim.anim_change_activity_from_center_to_left);

                startActivityForResult(intent,12,ActivityOptions.makeCustomAnimation(ListFilmActivity.this,
                        R.anim.anim_change_activity_from_right, R.anim.anim_no_move).toBundle());
               // startActivity(intent, ActivityOptions.makeCustomAnimation(ListFilmActivity.this, R.anim.anim_change_activity_from_right, R.anim.anim_change_activity_from_center_to_left).toBundle());
                //finish();
            }
        });

      //  loadFilm();




//        int id = mSearchViewFilm.getContext().getResources().getIdentifier("android:id/item_search_view", null, null);
//        TextView textView = mSearchViewFilm.findViewById(id);
//        textView.setTextColor("@Android.colorText");
        mSearchViewFilm.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTextViewTitle.setVisibility(View.GONE);
            }
        });
        // TODO search film
        mSearchViewFilm.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadFilm();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mTextViewTitle.setVisibility(View.GONE);
                mAdapter.filter(newText);
                return true;
            }
        });

        mSearchViewFilm.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mTextViewTitle.setVisibility(View.VISIBLE);
                return false;
            }
        });


    }

//    public void filter(String charText){
//        if (TextUtils.isEmpty(charText)) {
//            mDataSet.clear();
//            mDataSet.addAll(mListBackup);
//            notifyDataSetChanged();
//        }
//        else{
//            charText = charText.toLowerCase(Locale.getDefault());
//            mDataSet.clear();
//            for (ResponseAllMovies.Movie s : mListBackup) {
//                if (s.getTitle().toLowerCase(Locale.getDefault()).contains(charText)
//                        || VNCharacterUtils.removeAccent(s.getTitle().toLowerCase(Locale.getDefault())).contains(charText)) {
//                    mDataSet.add(s.clone());
//                }
//            }
//            notifyDataSetChanged();
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 12)
            if (resultCode == Activity.RESULT_OK) {

                loadFilm();
            }
            else {

            }
    }

    private void loadFilm() {
        mAPIService.getFilm().enqueue(new Callback<ResponeApi>() {
            @Override
            public void onResponse(Call<ResponeApi> call, Response<ResponeApi> response) {
                if (response.isSuccessful()) {
                    mList = response.body().getMovies();
                    Collections.reverse(mList);
                    mAdapter.updateAnswers(mList);
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

    @Override
    public void onClickItem(int position) {
        Intent intent = new Intent(ListFilmActivity.this, FilmInfoActivity.class);
        intent.putExtra("FILM", (Serializable) mList.get(position));
        startActivity(intent);
    }
}

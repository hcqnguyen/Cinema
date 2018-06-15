package com.example.nguyen.cinema.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.nguyen.cinema.Data.Adapter.ListFilmAdapter;
import com.example.nguyen.cinema.Data.Adapter.UserListFilmAdapter;
import com.example.nguyen.cinema.Data.Model.IconTextView;
import com.example.nguyen.cinema.Data.Model.Login;
import com.example.nguyen.cinema.Data.Model.ResponeApi;
import com.example.nguyen.cinema.Data.Remote.APIService;
import com.example.nguyen.cinema.Data.Remote.ApiUtils;
import com.example.nguyen.cinema.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout mLinearLayoutProfileBackToListFilm, mLinearLayoutEditPhoneNumber, mLInearLayoutEditUsername;
    Button mButtonChangePassword, mButtonSignOut, mButtonOk, mButtonCancel;
    EditText mEditTextOldPassword,mEditTextNewPassword,mEditTextReinputNewPassword ;
    RecyclerView mRecyclerViewListFilm;
    APIService mAPIService;
    private UserListFilmAdapter mAdapter;
    private Dialog mDialogChangePassword;
    String mOldPassword, mNewPassword, mReinputNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mEditTextOldPassword = findViewById(R.id.edit_text_old_password);
        mLInearLayoutEditUsername = findViewById(R.id.linear_layout_edit_username);
        mLinearLayoutEditPhoneNumber = findViewById(R.id.linear_layout_edit_phone_number);
        mButtonChangePassword = findViewById(R.id.button_change_password);
        mButtonSignOut = findViewById(R.id.button_sign_out);
        mRecyclerViewListFilm = findViewById(R.id.recyclerview_profile_list_film);
        mLinearLayoutProfileBackToListFilm = findViewById(R.id.linear_layout_profile_back_to_list_film);
        mAPIService = ApiUtils.getAPIService();
        mAdapter = new UserListFilmAdapter(new ArrayList<ResponeApi.Movie>(), ProfileActivity.this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProfileActivity.this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerViewListFilm.setLayoutManager(layoutManager);
        mRecyclerViewListFilm.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        mRecyclerViewListFilm.addItemDecoration(dividerItemDecoration);
        mRecyclerViewListFilm.setAdapter(mAdapter);


        addEvents();
        loadMyFilm();

    }

    private void addEvents() {
        mButtonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseDialog();
            }
        });
        mLinearLayoutProfileBackToListFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,ListFilmActivity.class);
                startActivity(intent);
            }
        });
        mButtonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignoutDialog();
            }
        });
    }

    private void showSignoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn có muốn đăng xuất không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showSignoutDialog();
                SharedPreferences pre = getSharedPreferences("access_token",MODE_PRIVATE);
                SharedPreferences.Editor editor = pre.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(ProfileActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // TODO change password
    private void showChooseDialog() {
        mDialogChangePassword = new Dialog(ProfileActivity.this);
        mDialogChangePassword.setContentView(R.layout.dialog_change_password);
        mEditTextOldPassword = mDialogChangePassword.findViewById(R.id.edit_text_old_password);
        mEditTextNewPassword = mDialogChangePassword.findViewById(R.id.edit_text_new_password);
        mEditTextReinputNewPassword = mDialogChangePassword.findViewById(R.id.edit_text_reinput_new_password);
        mButtonCancel = mDialogChangePassword.findViewById(R.id.button_cancel);
        mButtonOk = mDialogChangePassword.findViewById(R.id.button_ok);


        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOldPassword = mEditTextOldPassword.getText().toString().trim();
                mNewPassword = mEditTextNewPassword.getText().toString().trim();
                mReinputNewPassword  = mEditTextReinputNewPassword.getText().toString().trim();
                if (mOldPassword == null){
                    Toast.makeText(mDialogChangePassword.getContext(),"Vui lòng nhập mật khẩu cũ",Toast.LENGTH_LONG).show();
                } else if (mNewPassword == null){
                    Toast.makeText(mDialogChangePassword.getContext(),"Vui lòng nhập mật khẩu mới",Toast.LENGTH_LONG).show();
                } else if (mReinputNewPassword == null){
                    Toast.makeText(mDialogChangePassword.getContext(),"Vui lòng nhập lại mật khẩu mới",Toast.LENGTH_LONG).show();
                } else if (mNewPassword.equalsIgnoreCase(mReinputNewPassword) == false){
                    Toast.makeText(mDialogChangePassword.getContext(),"Xác nhận lại mật khẩu không đúng",Toast.LENGTH_LONG).show();
                } else {

                }
            }
        });

        mButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogChangePassword.dismiss();
            }
        });
        mDialogChangePassword.show();
    }



    // TODO recyclerview user's list film
    private void loadMyFilm() {
        mAPIService.getFilm().enqueue(new Callback<ResponeApi>() {
            @Override
            public void onResponse(Call<ResponeApi> call, Response<ResponeApi> response) {
                if (response.isSuccessful()) {
                    mAdapter.upDateListFilm(response.body().getMovies());
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




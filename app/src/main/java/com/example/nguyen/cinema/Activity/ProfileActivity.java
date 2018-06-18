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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyen.cinema.Data.Adapter.UserListFilmAdapter;
import com.example.nguyen.cinema.Data.Model.Login;
import com.example.nguyen.cinema.Data.Model.ResponeApi;
import com.example.nguyen.cinema.Data.Remote.APIService;
import com.example.nguyen.cinema.Data.Remote.ApiUtils;
import com.example.nguyen.cinema.R;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ProfileActivity extends AppCompatActivity {

    LinearLayout mLinearLayoutProfileBackToListFilm, mLinearLayoutEditPhoneNumber, mLInearLayoutEditUsername;
    Button mButtonChangePassword, mButtonSignOut, mButtonOk, mButtonCancel;

    // Button change username
    Button mButtonOkChangeUsername, mButtonCancelChangeUsername;
    // Button change phone number
    Button mButtonOkChangePhoneNumber, mButtonCancelChangePhoneNumber;

    EditText mEditTextOldPassword,mEditTextNewPassword,mEditTextReinputNewPassword, mEditTextChangeUsername, mEditTextChangePhoneNumber ;
    RecyclerView mRecyclerViewListFilm;
    TextView mTextViewEmail, mTextViewPhone, mTextViewUsername;
    APIService mAPIService;
    private UserListFilmAdapter mAdapter;
    private Dialog mDialogChangePassword, mDialogChangeUsername, mDialogChangePhoneNumber;
    String mOldPassword, mNewPassword, mReinputNewPassword, mPhoneNumber,token, mUsername;

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
        mTextViewEmail = findViewById(R.id.text_view_profile_email);
        mTextViewPhone = findViewById(R.id.text_view_profile_number_phone);
        mTextViewUsername = findViewById(R.id.text_view_profile_username);


        SharedPreferences pre = getSharedPreferences("access_token",MODE_PRIVATE);
        mAPIService = ApiUtils.getAPIService(pre.getString("token",""));
        token = pre.getString("token","");

        mAdapter = new UserListFilmAdapter(new ArrayList<ResponeApi.Movie>(), ProfileActivity.this);



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ProfileActivity.this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerViewListFilm.setLayoutManager(layoutManager);
        mRecyclerViewListFilm.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        mRecyclerViewListFilm.addItemDecoration(dividerItemDecoration);
        mRecyclerViewListFilm.setAdapter(mAdapter);


        addEvents();
        loadMyFilm();
        loadProfile(pre.getString("token",""));

    }

    private void loadProfile(String token) {
        mAPIService.getProfile(token).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if(response.isSuccessful()){
                    mTextViewUsername.setText(response.body().getUser().getUsername().toString().trim());
                    mUsername = mTextViewUsername.getText().toString().trim();
                    mTextViewEmail.setText(response.body().getUser().getEmail().toString().trim());
                   if (response.body().getUser().getPhone()== null){
                       mTextViewPhone.setText("");
                       mPhoneNumber = mTextViewPhone.getText().toString();
                   } else {
                       mTextViewPhone.setText(response.body().getUser().getPhone().toString());
                       mPhoneNumber = mTextViewPhone.getText().toString();
                   }

                }
                else {
                    Toast.makeText(ProfileActivity.this,response.message(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {

            }
        });
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
                finish();
                ProfileActivity.this.overridePendingTransition(R.anim.anim_change_activity_from_left,R.anim.anim_change_activity_from_center_to_right);
            }
        });
        mButtonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignoutDialog();
            }
        });


        mLInearLayoutEditUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogChangeUsername = new Dialog(ProfileActivity.this);
                mDialogChangeUsername.setContentView(R.layout.dialog_change_username);
                mEditTextChangeUsername = mDialogChangeUsername.findViewById(R.id.edit_text_chang_username);

                mButtonOkChangeUsername = mDialogChangeUsername.findViewById(R.id.button_ok_change_username);
                mButtonCancelChangeUsername = mDialogChangeUsername.findViewById(R.id.button_cancel_change_username);

                mButtonOkChangeUsername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAPIService.changeUsername(token,mEditTextChangeUsername.getText().toString().trim()).enqueue(new Callback<Login>() {
                            @Override
                            public void onResponse(Call<Login> call, Response<Login> response) {
                                if (response.isSuccessful() == true){
                                    Toast.makeText(ProfileActivity.this,"Bạn đã đổi username thành công",Toast.LENGTH_LONG).show();
                                    mDialogChangeUsername.dismiss();
                                    mTextViewUsername.setText(mEditTextChangeUsername.getText().toString().trim());
                                }
                            }

                            @Override
                            public void onFailure(Call<Login> call, Throwable t) {
                                Toast.makeText(ProfileActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                                t.printStackTrace();
                            }
                        });
                    }
                });

                mButtonCancelChangeUsername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialogChangeUsername.dismiss();
                    }
                });
                mDialogChangeUsername.show();
                mEditTextChangeUsername.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(ProfileActivity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            }
        });
        mLinearLayoutEditPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogChangePhoneNumber = new Dialog(ProfileActivity.this);
                mDialogChangePhoneNumber.setContentView(R.layout.dialog_change_phone_number);
                mEditTextChangePhoneNumber = mDialogChangePhoneNumber.findViewById(R.id.edit_text_change_phone_number);

                mEditTextChangePhoneNumber.setText(mTextViewPhone.getText().toString());
                mButtonOkChangePhoneNumber = mDialogChangePhoneNumber.findViewById(R.id.button_ok_change_phone_number);
                mButtonCancelChangePhoneNumber = mDialogChangePhoneNumber.findViewById(R.id.button_cancel_change_phone_number);

                mButtonOkChangePhoneNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAPIService.changePhonenumber(token,mEditTextChangePhoneNumber.getText().toString().trim()).enqueue(new Callback<Login>() {
                            @Override
                            public void onResponse(Call<Login> call, Response<Login> response) {
                                if (response.isSuccessful() == true){
                                    Toast.makeText(ProfileActivity.this,"Bạn đã đổi username thành công",Toast.LENGTH_LONG).show();
                                    mDialogChangePhoneNumber.dismiss();
                                    mTextViewPhone.setText(mEditTextChangePhoneNumber.getText().toString().trim());
                                }
                            }

                            @Override
                            public void onFailure(Call<Login> call, Throwable t) {
                                Toast.makeText(ProfileActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                                t.printStackTrace();
                            }
                        });
                    }
                });

                mButtonCancelChangePhoneNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialogChangePhoneNumber.dismiss();
                    }
                });
                mDialogChangePhoneNumber.show();
                mEditTextChangePhoneNumber.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(ProfileActivity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ProfileActivity.this.overridePendingTransition(R.anim.anim_change_activity_from_left,R.anim.anim_change_activity_from_center_to_right);
    }


    // TODO sign out

    private void showSignoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Bạn có muốn đăng xuất không?");
        builder.setCancelable(false);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //showSignoutDialog();
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
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorTitle));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorTitle));
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
                if (mOldPassword.equalsIgnoreCase("")){
                    Toast.makeText(mDialogChangePassword.getContext(),"Vui lòng nhập mật khẩu cũ",Toast.LENGTH_LONG).show();
                } else if (mNewPassword.equalsIgnoreCase("")){
                    Toast.makeText(mDialogChangePassword.getContext(),"Vui lòng nhập mật khẩu mới",Toast.LENGTH_LONG).show();
                } else if (mReinputNewPassword.equalsIgnoreCase("")){
                    Toast.makeText(mDialogChangePassword.getContext(),"Vui lòng nhập lại mật khẩu mới",Toast.LENGTH_LONG).show();
                } else if (mNewPassword.equalsIgnoreCase(mReinputNewPassword) == false){
                    Toast.makeText(mDialogChangePassword.getContext(),"Xác nhận lại mật khẩu không đúng",Toast.LENGTH_LONG).show();
                } else {
                    postChangePassword();
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
        mEditTextOldPassword.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(ProfileActivity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void postChangePassword() {
        mAPIService.changePassword(token,mOldPassword,mNewPassword).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() ==  true){
                    Toast.makeText(ProfileActivity.this, "Đổi mật khẩu thành công",Toast.LENGTH_LONG).show();
                    mDialogChangePassword.dismiss();
                }
                else {
                    Toast.makeText(ProfileActivity.this,response.message(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProfileActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }


    // TODO recyclerview user's list film
    private void loadMyFilm() {
        mAPIService.getUsermovie(token).enqueue(new Callback<ResponeApi>() {
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




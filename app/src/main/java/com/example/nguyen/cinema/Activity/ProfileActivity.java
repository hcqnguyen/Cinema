package com.example.nguyen.cinema.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nguyen.cinema.Data.Adapter.UserListFilmAdapter;
import com.example.nguyen.cinema.Data.Model.Login;
import com.example.nguyen.cinema.Data.Model.ResponeApi;
import com.example.nguyen.cinema.Data.Remote.APIService;
import com.example.nguyen.cinema.Data.Remote.ApiUtils;
import com.example.nguyen.cinema.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    CircleImageView mCicrcleImgVAvatar;

    APIService mAPIService;
    private UserListFilmAdapter mAdapter;
    private Dialog mDialogChangePassword, mDialogChangeUsername, mDialogChangePhoneNumber;
    String mOldPassword, mNewPassword, mReinputNewPassword, mPhoneNumber,token, mUsername;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    int REQUEST_IMAGE_CAPTURE = 1, GALLERY  = 0;
    File mImageFile;
    boolean isChooseImage = false;

    final String DOMAIN = "https://nam-cinema.herokuapp.com";

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

        mCicrcleImgVAvatar = findViewById(R.id.circle_avatar);




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

    // TODO load profile

    private void loadProfile(String token) {
        mAPIService.getProfile(token).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if(response.isSuccessful()){
                    mTextViewUsername.setText(response.body().getUser().getUsername().toString().trim());
                    mUsername = mTextViewUsername.getText().toString().trim();
                    mTextViewEmail.setText(response.body().getUser().getEmail().toString().trim());

                    Glide.with(ProfileActivity.this)
                            .load(DOMAIN+response.body().getUser().getAvatar().toString())
                            .override(300, 400)
                            .error(R.drawable.ic_launcher_background)
                            .into(mCicrcleImgVAvatar);

                   if (response.body().getUser().getPhone()== null){
                       mTextViewPhone.setText("");
                       mPhoneNumber = mTextViewPhone.getText().toString();
                   } else {
                       mTextViewPhone.setText(response.body().getUser().getPhone().toString());
                       mPhoneNumber = mTextViewPhone.getText().toString();
                   }

                }
                else {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Load thông tin không thành công");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
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


        //TODO edit Username

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
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toast,
                                        (ViewGroup) findViewById(R.id.toast_layout_root));


                                TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                                text.setText("Đổi username thành công");

                                Toast toast = new Toast(getApplicationContext());

                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
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

        // TODO edit Phone number
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
                                    Toast.makeText(ProfileActivity.this,"Bạn đã đổi số điện thoại thành công",Toast.LENGTH_LONG).show();
                                    mDialogChangePhoneNumber.dismiss();
                                    mTextViewPhone.setText(mEditTextChangePhoneNumber.getText().toString().trim());
                                }
                            }

                            @Override
                            public void onFailure(Call<Login> call, Throwable t) {
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toast,
                                        (ViewGroup) findViewById(R.id.toast_layout_root));


                                TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                                text.setText("Đổi số điện thoại không thành công");

                                Toast toast = new Toast(getApplicationContext());

                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
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

        // TODO edit Avatar
        mCicrcleImgVAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();

            }
        });
    }

    public void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    1001);
            return;
        }
        showBrowserImage();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED )
                    &&( grantResults[1] == PackageManager.PERMISSION_GRANTED )
                    ) {
                showBrowserImage();
            }
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                  //  Toast.makeText(ProfileActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    mCicrcleImgVAvatar.setImageBitmap(bitmap);
                    isChooseImage = true;
                    editAvatar();
                } catch (IOException e) {
                    e.printStackTrace();
                   // Toast.makeText(ProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            mCicrcleImgVAvatar.setImageBitmap(thumbnail);
            saveImage(thumbnail);
           // Toast.makeText(ProfileActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
            isChooseImage = true;
            editAvatar();
        }

    }

    private void showBrowserImage() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(ProfileActivity.this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void editAvatar() {
        if (mImageFile == null) return;
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"),mImageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar",mImageFile.getName(),reqFile);


        mAPIService.changeAvatar(token,body).enqueue(new Callback<RequestBody>() {
            @Override
            public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
                if (response.isSuccessful()){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Bạn đã đổi avatar thành công");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
                else
                {
                    Toast.makeText(ProfileActivity.this,response.message(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RequestBody> call, Throwable t) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast,
                        (ViewGroup) findViewById(R.id.toast_layout_root));


                TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                text.setText("Đổi avatar không thành công");

                Toast toast = new Toast(getApplicationContext());

                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
                t.printStackTrace();
            }
        });
    }

    private String saveImage(Bitmap mybitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mybitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            mImageFile = f;
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
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
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Vui lòng nhập mật khẩu cũ");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                } else if (mNewPassword.equalsIgnoreCase("")){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Vui lòng nhập mật khẩu mới");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                } else if (mReinputNewPassword.equalsIgnoreCase("")){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Vui lòng nhập lại mật khẩu mới");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                } else if (mNewPassword.equalsIgnoreCase(mReinputNewPassword) == false){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Xác nhận lại mật khẩu mới không đúngs");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
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
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Đổi mật khẩu thành công");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                    mDialogChangePassword.dismiss();
                }
                else {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Đổi mật khẩu không thành công");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.toast,
                        (ViewGroup) findViewById(R.id.toast_layout_root));


                TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                text.setText("Đổi mật khẩu không thành công");

                Toast toast = new Toast(getApplicationContext());

                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
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




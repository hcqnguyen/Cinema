package com.example.nguyen.cinema.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nguyen.cinema.Data.Model.Login;
import com.example.nguyen.cinema.Data.Remote.APIService;
import com.example.nguyen.cinema.Data.Remote.ApiUtils;
import com.example.nguyen.cinema.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    EditText mEditTextEmail2, mEditTextPassword2;
    Button mButtonSigIn2, mButtonSigUp2;
    String mEmail,mPassword;
    APIService mAPIService;
    final String TAG = "--- SIGN IN ACITIVTY";
    public static String token;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SharedPreferences pre = getSharedPreferences("access_token",MODE_PRIVATE);
        if (pre.getBoolean("isLogin",false) == true)
        {
            Intent intent = new Intent(SignInActivity.this, ListFilmActivity.class);
            startActivity(intent);
            finish();
        }

        mButtonSigIn2 =findViewById(R.id.button_Sign_in_2);
        mButtonSigUp2 = findViewById(R.id.button_Sign_up_2);
        mEditTextPassword2 = findViewById(R.id.edit_text_password_2);
        mEditTextEmail2 = findViewById(R.id.edit_text_email_2);

        mAPIService = ApiUtils.getAPIService(pre.getString("token",""));


        mButtonSigUp2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Intent intetn = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intetn, ActivityOptions.makeCustomAnimation(SignInActivity.this,R.anim.anim_change_activity_from_bottom,R.anim.anim_change_activity_from_right).toBundle());


            }
        });
        mButtonSigIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mEditTextEmail2.getText().toString();
                mPassword = mEditTextPassword2.getText().toString();
                if (mEmail.equalsIgnoreCase("")){
                    Toast.makeText(SignInActivity.this,"Vui lòng nhập email!",Toast.LENGTH_LONG).show();
                }else if (validate(mEmail) == false){
                    Toast.makeText(SignInActivity.this,"Vui lòng nhập đúng định dạng email!",Toast.LENGTH_LONG).show();
                } else if (mPassword.equalsIgnoreCase("")){
                    Toast.makeText(SignInActivity.this,"Vui lòng nhập password!",Toast.LENGTH_LONG).show();
                }
                else{
                    signIn();
                }
            }
        });
    }

    public void signIn() {
        mAPIService.signIn(mEmail,mPassword).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful() == false)
                {
                    Toast.makeText(SignInActivity.this,response.message(),Toast.LENGTH_LONG).show();
                }
                else {
                    Boolean isLogin = true;
                    token = "";
                     token = response.body().getToken();
                    SharedPreferences pre = getSharedPreferences("access_token",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.putString("token",token);
                    editor.putBoolean("isLogin",isLogin);
                    editor.commit();



                    Intent intent = new Intent(SignInActivity.this, ListFilmActivity.class);
                    startActivity(intent);
                    finish();

                    Log.e(TAG,"DANG NHAP THANH CONG");
                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.e(TAG,"DANG NHAP THAT BAI");
            }
        });
    }
    public String getToken(){
        return this.token;
    }
}

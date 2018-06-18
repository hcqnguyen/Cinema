package com.example.nguyen.cinema.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nguyen.cinema.Data.Remote.APIService;
import com.example.nguyen.cinema.Data.Remote.ApiUtils;
import com.example.nguyen.cinema.R;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.nguyen.cinema.Activity.SignInActivity.validate;

public class SignUpActivity extends AppCompatActivity {

    EditText mEditTextUsername, mEditTextEmail, mEditTextPassWord, mEditTextReinputPassword;
    Button mButtonSignUp, mButtonSignIn;
    private String mUsername, mEmail, mPassword, mReinputPassword;
    private APIService mAPIService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEditTextEmail = findViewById(R.id.edit_text_email);
        mEditTextPassWord = findViewById(R.id.edit_text_password);
        mEditTextUsername = findViewById(R.id.edit_text_username);
        mEditTextReinputPassword = findViewById(R.id.edit_text_reinput_password);
        mButtonSignIn = findViewById(R.id.button_Sign_in);
        mButtonSignUp = findViewById(R.id.button_Sign_up);
        SharedPreferences pre = getSharedPreferences("access_token",MODE_PRIVATE);
        mAPIService = ApiUtils.getAPIService(pre.getString("token",""));


        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mEditTextEmail.getText().toString().trim();
                mUsername = mEditTextUsername.getText().toString().trim();
                mPassword = mEditTextPassWord.getText().toString().trim();
                mReinputPassword = mEditTextReinputPassword.getText().toString().trim();
                if (mUsername.equalsIgnoreCase("")){
                    Toast.makeText(SignUpActivity.this,"Vui lòng nhập username!",Toast.LENGTH_LONG).show();
                }else if (mEmail.equalsIgnoreCase("")){
                    Toast.makeText(SignUpActivity.this,"Vui lòng nhập email!",Toast.LENGTH_LONG).show();
                } else if (validate(mEmail)== false){
                    Toast.makeText(SignUpActivity.this,"Vui lòng nhập đúng định dạng email!",Toast.LENGTH_LONG).show();
                } else  if (mPassword.equalsIgnoreCase("")) {
                    Toast.makeText(SignUpActivity.this,"Vui lòng nhập password",Toast.LENGTH_LONG).show();
                }else if (mReinputPassword.equalsIgnoreCase("")){
                    Toast.makeText(SignUpActivity.this,"Vui lòng xác nhận lại mật khẩu",Toast.LENGTH_LONG).show();
                } else if (mPassword.equalsIgnoreCase(mReinputPassword) == false){
                    Toast.makeText(SignUpActivity.this,"Nhập lại mật khẩu không đúng!",Toast.LENGTH_LONG).show();
                } else {
                    createAccount();
                }
            }
        });
    }

    public void createAccount() {
        mAPIService.signUpAccount(mEmail,mUsername,mPassword).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Toast.makeText(SignUpActivity.this,"Bạn đã đăng ký thành công!",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(SignUpActivity.this,response.message(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(SignUpActivity.this,"Đăng ký không thành công!",Toast.LENGTH_LONG).show();

            }
        });
    }
}

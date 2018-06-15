package com.example.nguyen.cinema.Activity;

import android.content.Intent;
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

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        mAPIService = ApiUtils.getAPIService();


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
                if (mEditTextEmail.getText().toString() == null || mEditTextPassWord.getText().toString() == null
                        || mEditTextUsername.getText().toString() == null || mEditTextReinputPassword.getText().toString() ==  null ){
                    Toast.makeText(SignUpActivity.this,"Vui lòng nhập đầy đủ thông tin!",Toast.LENGTH_LONG).show();
                }
                else if (mPassword.equals(mReinputPassword) == false){
                    Toast.makeText(SignUpActivity.this,"Nhập lại mật khẩu không đúng!",Toast.LENGTH_LONG).show();
                }
                else {
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
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SignUpActivity.this,"Đăng ký không thành công!",Toast.LENGTH_LONG).show();

            }
        });
    }
}

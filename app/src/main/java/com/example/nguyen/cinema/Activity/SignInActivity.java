package com.example.nguyen.cinema.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nguyen.cinema.Data.Remote.APIService;
import com.example.nguyen.cinema.Data.Remote.ApiUtils;
import com.example.nguyen.cinema.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    EditText mEditTextEmail2, mEditTextPassword2;
    Button mButtonSigIn2, mButtonSigUp2;
    String mEmail,mPassword;
    APIService mAPIService;
    final String TAG = "--- SIGN IN ACITIVTY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mButtonSigIn2 =findViewById(R.id.button_Sign_in_2);
        mButtonSigUp2 = findViewById(R.id.button_Sign_up_2);
        mEditTextPassword2 = findViewById(R.id.edit_text_password_2);
        mEditTextEmail2 = findViewById(R.id.edit_text_email_2);

        mAPIService = ApiUtils.getAPIService();



        mButtonSigUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intetn = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intetn);
            }
        });
        mButtonSigIn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mEditTextEmail2.getText().toString().trim();
                mPassword = mEditTextPassword2.getText().toString().trim();
                if (mEmail == null){
                    Toast.makeText(SignInActivity.this,"Vui lòng nhập email!",Toast.LENGTH_LONG).show();
                }
                else if (mEmail == null){
                    Toast.makeText(SignInActivity.this,"Vui lòng nhập password!",Toast.LENGTH_LONG).show();
                }
                else{
                    signIn();
                }
            }
        });
    }

    public void signIn() {
        mAPIService.signIn(mEmail,mPassword).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() == false)
                {
                    Toast.makeText(SignInActivity.this,response.message(),Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(SignInActivity.this, ProfileActivity.class);
                    SharedPreferences pre = getSharedPreferences("my_account",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.putString("email",mEmail);
                    editor.putString("password",mPassword);
                    editor.commit();

                    startActivity(intent);
                    Log.e(TAG,"DANG NHAP THANH CONG");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG,"DANG NHAP THAT BAI");
            }
        });
    }
}

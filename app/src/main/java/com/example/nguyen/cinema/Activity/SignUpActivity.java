package com.example.nguyen.cinema.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    LinearLayout mLinearLayoutSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mLinearLayoutSignUp = findViewById(R.id.linear_layout_sign_up);

        mEditTextEmail = findViewById(R.id.edit_text_email);
        mEditTextPassWord = findViewById(R.id.edit_text_password);
        mEditTextUsername = findViewById(R.id.edit_text_username);
        mEditTextReinputPassword = findViewById(R.id.edit_text_reinput_password);
        mButtonSignIn = findViewById(R.id.button_Sign_in);
        mButtonSignUp = findViewById(R.id.button_Sign_up);
        SharedPreferences pre = getSharedPreferences("access_token",MODE_PRIVATE);
        mAPIService = ApiUtils.getAPIService(pre.getString("token",""));

//        TranslateAnimation translateYAnimation = new TranslateAnimation(0f, 0f, 800f, 0f);
//        translateYAnimation.setDuration(1000l);
//        mLinearLayoutSignUp.startAnimation(translateYAnimation);


        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent, ActivityOptions.makeCustomAnimation(SignUpActivity.this,R.anim.anim_change_activity_from_bottom,R.anim.anim_change_activity_from_bottom).toBundle());
                finish();
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
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Vui lòng nhập username");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }else if (mEmail.equalsIgnoreCase("")){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Vui lòng nhập email!");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                } else if (validate(mEmail)== false){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Vui lòng nhập đúng định dạng email!");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                } else  if (mPassword.equalsIgnoreCase("")) {
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Vui lòng nhập password");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }else if (mReinputPassword.equalsIgnoreCase("")){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Vui lòng nhập xác nhận lại mật khẩu");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                } else if (mPassword.equalsIgnoreCase(mReinputPassword) == false){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Nhập lại mật khẩu không đúng");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
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
                    Toast.makeText(SignUpActivity.this,"Đăng ký không thành công!",Toast.LENGTH_LONG).show();
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

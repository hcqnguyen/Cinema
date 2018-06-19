package com.example.nguyen.cinema.Activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
    Button mButtonSigIn2, mButtonSigUp2, mButtonResetPassword;
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
        mButtonResetPassword = findViewById(R.id.button_reset_password);

        if  (pre.getBoolean("isLogin",false) == false) {
            mEditTextPassword2.setText(pre.getString("password","").toString());
            mEditTextEmail2.setText(pre.getString("email","").toString());
        }
        mAPIService = ApiUtils.getAPIService(pre.getString("token",""));

        mButtonResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

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
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Vui lòng nhập email!");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }else if (validate(mEmail) == false){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Vui lòng nhập đúng định dạng email");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                } else if (mPassword.equalsIgnoreCase("")){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Vui lòng nhập password!");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
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
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast,
                            (ViewGroup) findViewById(R.id.toast_layout_root));


                    TextView text = (TextView) layout.findViewById(R.id.text_view_toast);
                    text.setText("Sai tài khoản hoặc mật khẩu");

                    Toast toast = new Toast(getApplicationContext());

                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
                else {
                    Boolean isLogin = true;
                    token = "";
                     token = response.body().getToken();
                    SharedPreferences pre = getSharedPreferences("access_token",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pre.edit();
                    editor.clear();
                    editor.putString("token",token);
                    editor.putBoolean("isLogin",isLogin);
                    editor.putString("email",response.body().getUser().getEmail());
                    editor.putString("password",mEditTextPassword2.getText().toString());
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

}

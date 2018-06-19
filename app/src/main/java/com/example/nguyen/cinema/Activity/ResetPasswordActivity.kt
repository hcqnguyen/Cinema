package com.example.nguyen.cinema.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.nguyen.cinema.Data.Remote.APIService
import com.example.nguyen.cinema.Data.Remote.ApiUtils
import com.example.nguyen.cinema.R
import com.example.nguyen.cinema.R.id.*

import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.android.synthetic.main.toast.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Matcher
import java.util.regex.Pattern


class ResetPasswordActivity : AppCompatActivity() {

    val VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
    fun validate(emailStr :String) : Boolean {
        var matcher : Matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr)
        return matcher.find()
    }
    var mAPIService : APIService? = null

    //var pre :SharedPreferences = getSharedPreferences("access_token", Context.MODE_PRIVATE)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        var mEmail :String
        mAPIService = ApiUtils.getAPIService("")



        button_cancel_reset_password.setOnClickListener(View.OnClickListener {
            finish()
        })

        button_ok_reset_password.setOnClickListener(View.OnClickListener {
            mEmail = edit_text_reset_password.text.toString().trim()
            if (mEmail == ""){
                val inflater = layoutInflater
                val layout = inflater.inflate(R.layout.toast,
                        findViewById<ViewGroup>(R.id.toast_layout_root))
                var text_view :TextView = layout.findViewById<TextView>(R.id.text_view_toast)
                text_view.text = "Vui lòng nhập email"
                val toast = Toast(applicationContext)

                toast.duration = Toast.LENGTH_LONG
                toast.view = layout
                toast.show()

            } else if (validate(mEmail) == false){
                val inflater = layoutInflater
                val layout = inflater.inflate(R.layout.toast,
                        findViewById<ViewGroup>(R.id.toast_layout_root))
                var text_view :TextView = layout.findViewById<TextView>(R.id.text_view_toast)
                text_view.text = "Vui lòng nhập đúng định dạng email"
                val toast = Toast(applicationContext)

                toast.duration = Toast.LENGTH_LONG
                toast.view = layout
                toast.show()
            } else {
                mAPIService?.let {
                    resetPassword(it, mEmail)
                }
            }
        })

        //var pre : SharedPreferences = getSharedPreferences()



    }
    fun resetPassword(APIService: APIService, Email:String){


        APIService.resetPassword(Email).enqueue( object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                val inflater = layoutInflater
                val layout = inflater.inflate(R.layout.toast,
                        findViewById<ViewGroup>(R.id.toast_layout_root))
                var text_view :TextView = layout.findViewById<TextView>(R.id.text_view_toast)
                text_view.text = "Đặt lại mật khẩu không thành công"
                val toast = Toast(applicationContext)

                toast.duration = Toast.LENGTH_LONG
                toast.view = layout
                toast.show()
                t?.printStackTrace()

            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                response?.let {
                    if (it.isSuccessful == true){
                        val inflater = getLayoutInflater()
                        val layout = inflater.inflate(R.layout.toast,
                                findViewById<ViewGroup>(R.id.toast_layout_root))


                        var text_view :TextView = layout.findViewById<TextView>(R.id.text_view_toast)
                        text_view.text = "Đặt lại mật khẩu thành công, vui lòng kiểm tra email"

                        val toast = Toast(getApplicationContext())

                        toast.duration = Toast.LENGTH_LONG
                        toast.view = layout
                        toast.show()
                        finish()
                    }
                    else {
                       val inflater = layoutInflater
                        val layout = inflater.inflate(R.layout.toast,
                                findViewById<ViewGroup>(R.id.toast_layout_root))


                        var text_view :TextView = layout.findViewById<TextView>(R.id.text_view_toast)
                        text_view.text = "Đặt lại mật khẩu không thành công"
                        val toast = Toast(applicationContext)

                        toast.duration = Toast.LENGTH_LONG
                        toast.view = layout
                        toast.show()
                    }
                }
            }

        })

    }
}

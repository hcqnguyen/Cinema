package com.example.nguyen.cinema.Activity;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.nguyen.cinema.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateFilmActivity extends AppCompatActivity {
    Button mButtonChoseImage, mButtonCreateFilm;
    TextView mTextViewStartDay;
    EditText mEditTextFilmName, mEditTextDiscription;
    ImageView mImageViewAvatar;
    Spinner mSpinnerKind;
    Calendar cal;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_film);


        addControls();
        addEvents();

    }



    private void addEvents() {

        // TODO set Data for spiner
        String arr[] = {
                "Hành động",
                "Lãng mạn",
                "Hài hước",
                "Kinh dị"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arr);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        mSpinnerKind.setAdapter(adapter);

        cal = Calendar.getInstance();
        SimpleDateFormat dft = null;
        dft = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate = dft.format(cal.getTime());
        mTextViewStartDay.setText(strDate);


        mButtonChoseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mButtonCreateFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mSpinnerKind.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mTextViewStartDay.setOnClickListener(showDatePicker);
    }
    View.OnClickListener showDatePicker = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog.OnDateSetListener startDay = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mTextViewStartDay.setText(dayOfMonth+"/"+(month-1) +"/" + year);
                    cal.set(year,month,dayOfMonth);
                    date = cal.getTime();
                }
            };
            String s =  mTextViewStartDay.getText()+  "";
            //Lấy ra chuỗi của textView Date
            String strArrtmp[]=s.split("/");
            int ngay=Integer.parseInt(strArrtmp[0]);
            int thang=Integer.parseInt(strArrtmp[1]) - 1;
            int nam=Integer.parseInt(strArrtmp[2]);
            DatePickerDialog pic=new DatePickerDialog(
                    CreateFilmActivity.this,
                    startDay, nam, thang, ngay);
            pic.setTitle("Chọn ngày hoàn thành");
            pic.show();
        }
    };

    private void addControls() {
        mImageViewAvatar = findViewById(R.id.image_view_avatar);
        mButtonChoseImage = findViewById(R.id.button_choose_image);
        mButtonCreateFilm = findViewById(R.id.button_create_film);
        mTextViewStartDay = findViewById(R.id.text_view_input_star_day);
        mEditTextFilmName = findViewById(R.id.edit_text_film_name);
        mEditTextDiscription = findViewById(R.id.edit_text_discription);
        mSpinnerKind = findViewById(R.id.spinner_kind);
    }

}

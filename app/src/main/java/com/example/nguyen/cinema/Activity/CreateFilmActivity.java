package com.example.nguyen.cinema.Activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nguyen.cinema.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class CreateFilmActivity extends AppCompatActivity {
    private static final String TAG = "CREATE FILM" ;
    Button mButtonChoseImage, mButtonCreateFilm;
    TextView mTextViewStartDay;
    EditText mEditTextFilmName, mEditTextDiscription;
    ImageView mImageViewAvatar;
    Spinner mSpinnerKind;
    Calendar cal;
    Date date;
    int CAMERA = 2, GALLERY  = 1;
    private Bitmap myBitmap;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private static final int RECORD_REQUEST_CODE = 101;

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
                showChooseDialog();
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

    // TODO button choose image
    private void showChooseDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
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

    public void choosePhotoFromGallary() {

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");
            makeRequestGalerry();
        }

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent,GALLERY);
    }

    private void makeRequestGalerry() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                RECORD_REQUEST_CODE);
    }


    private void takePhotoFromCamera() {
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission to record denied");
            makeRequestCamera();
        }
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }
    private void makeRequestCamera() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                RECORD_REQUEST_CODE);
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
                    Toast.makeText(CreateFilmActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    mImageViewAvatar.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateFilmActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            mImageViewAvatar.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(CreateFilmActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(CreateFilmActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    mImageViewAvatar.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateFilmActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            mImageViewAvatar.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(CreateFilmActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
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
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    // TODO set Start day
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
        mImageViewAvatar.setImageResource(R.drawable.ic_avatar);
    }

}

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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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


import com.example.nguyen.cinema.Data.Model.IconTextView;
import com.example.nguyen.cinema.Data.Remote.APIService;
import com.example.nguyen.cinema.Data.Remote.ApiUtils;
import com.example.nguyen.cinema.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class CreateFilmActivity extends AppCompatActivity {
    private static final String TAG = "CREATE FILM" ;
    Button mButtonChoseImage, mButtonCreateFilm;
    TextView mTextViewRelease;
    EditText mEditTextTitle, mEditTextDiscription;
    IconTextView mIconTextViewBackToCreateFilm;
    ImageView mImageViewAvatar;
    Spinner mSpinnerGenre;
    Calendar cal;
    Date date;
    int REQUEST_IMAGE_CAPTURE = 1, GALLERY  = 0;
    private Bitmap myBitmap;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private static final int RECORD_REQUEST_CODE = 101;
    private String mTitle, mGenre, mRelease, mDiscription;
    private APIService mAPIService;
    File mImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_film);


        addControls();
        addEvents();

    }



    private void addEvents() {

        // TODO set Data for spiner
        final String arr[] = {
                "Hành động",
                "Lãng mạn",
                "Hài hước",
                "Kinh dị"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,arr);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        mSpinnerGenre.setAdapter(adapter);

        cal = Calendar.getInstance();
        SimpleDateFormat dft = null;
        dft = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String strDate = dft.format(cal.getTime());
        mTextViewRelease.setText(strDate);

        mIconTextViewBackToCreateFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateFilmActivity.this,ListFilmActivity.class);
                startActivity(intent);
            }
        });

        mButtonChoseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseDialog();
            }
        });

        mSpinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGenre = "";
            }
        });

        mTextViewRelease.setOnClickListener(showDatePicker);

        //TODO button create film
        mButtonCreateFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditTextTitle.getText().toString() =="" || mImageFile.isFile() == false){
                    Toast.makeText(CreateFilmActivity.this,"Vui lòng nhập đầy đủ thông tin!",Toast.LENGTH_LONG).show();
                }
                else{
                mTitle = mEditTextTitle.getText().toString().trim();
                mRelease = mTextViewRelease.getText().toString().trim();
                mDiscription = mEditTextDiscription.getText().toString().trim();
                mGenre = mSpinnerGenre.getSelectedItem().toString().trim();
                requestPermission();
                }
            }
        });
    }

    public void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
            return;
        }
        createFilm();
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
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createFilm();
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
                    Toast.makeText(CreateFilmActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    mImageViewAvatar.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateFilmActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            mImageViewAvatar.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(CreateFilmActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }

    }

    //TODO create film
    public void createFilm(){
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"),mImageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("cover",mImageFile.getName(),reqFile);

        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), mTitle);
        RequestBody genre = RequestBody.create(MediaType.parse("text/plain"), mGenre);
        RequestBody release = RequestBody.create(MediaType.parse("text/plain"), mRelease);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), mDiscription);

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("title", title);
        map.put("genre", genre);
        map.put("release", release);
        map.put("description", description);

        mAPIService = ApiUtils.getAPIService();
        mAPIService.uploadFileWithPartMap( map, body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(CreateFilmActivity.this,"Bạn đã tạo phim thành công",Toast.LENGTH_LONG).show();
                Log.e("onResponse", response.message() + "__" + response.toString());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(CreateFilmActivity.this,"Tạo phim không thành công",Toast.LENGTH_LONG).show();
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

    // TODO set Start day
    View.OnClickListener showDatePicker = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog.OnDateSetListener startDay = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mTextViewRelease.setText(dayOfMonth+"/"+(month-1) +"/" + year);
                    cal.set(year,month,dayOfMonth);
                    date = cal.getTime();
                }
            };
            String s =  mTextViewRelease.getText()+  "";
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
        mIconTextViewBackToCreateFilm =findViewById(R.id.icon_text_view_back_to_list_film);
        mImageViewAvatar = findViewById(R.id.image_view_cover);
        mButtonChoseImage = findViewById(R.id.button_choose_cover);
        mButtonCreateFilm = findViewById(R.id.button_create_film);
        mTextViewRelease = findViewById(R.id.text_view_input_release);
        mEditTextTitle = findViewById(R.id.edit_text_title);
        mEditTextDiscription = findViewById(R.id.edit_text_discription);
        mSpinnerGenre = findViewById(R.id.spinner_genre);
        mImageViewAvatar.setImageResource(R.drawable.ic_avatar);
    }

}

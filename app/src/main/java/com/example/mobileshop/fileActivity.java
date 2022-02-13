package com.example.mobileshop;

import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.mobileshop.Object.Cart.CartMessage;
import com.example.mobileshop.Object.Message;
import com.example.mobileshop.Object.Message1;
import com.example.mobileshop.Object.Person.InfoPerson;
import com.example.mobileshop.Object.Person.Person;
import com.example.mobileshop.Others.DataToken;
import com.example.mobileshop.Others.GetNewToken;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.example.mobileshop.adapter.ProductAdapter;
import com.google.android.gms.cast.framework.media.ImagePicker;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class fileActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private ImageButton imgbtnFileActivityLui,imgbtnFileActivitySubmit;
    private ConstraintLayout constraintLayout14,constraintLayout22;
    TextInputLayout textInputLayout3;
    private TextInputEditText editFileActivityName,editFileActivitySDT,editFileActivityEmail,editFileActivityDate;
    private RadioButton radioButtonMan,radioButtonWomen;
    private RadioGroup radioGroupSex;
    private DataToken dataToken;
    private String user;
    private ShapeableImageView imgFileActivityPicture;

    private ImageView imgFileActivityEdit;
    private SharedPreferences preferences;
    private Person person,personChange;
    private GetNewToken getNewToken;
    private Calendar calendar;
    private String imagePath;

    int yearf,monthf,dayf;



    DatePickerDialog.OnDateSetListener setListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        imgbtnFileActivityLui = findViewById(R.id.imgbtnFileActivityLui);
        imgFileActivityEdit = findViewById(R.id.imgFileActivityEdit);
        imgFileActivityPicture = findViewById(R.id.imgFileActivityPicture);


        editFileActivityName = findViewById(R.id.editFileActivityName);
        editFileActivitySDT = findViewById(R.id.editFileActivitySDT);
        editFileActivityEmail = findViewById(R.id.editFileActivityEmail);
        editFileActivityDate = findViewById(R.id.editFileActivityDate);
        radioButtonMan = findViewById(R.id.radioButtonMan);
        radioButtonWomen = findViewById(R.id.radioButtonWomen);
        radioGroupSex = findViewById(R.id.radioGroupSex);
        constraintLayout22 = findViewById(R.id.constraintLayout22);
        imgbtnFileActivitySubmit = findViewById(R.id.imgbtnFileActivitySubmit);
        constraintLayout14=findViewById(R.id.constraintLayout14);
        textInputLayout3=findViewById(R.id.textInputLayout3);

        radioGroupSex.check(radioButtonMan.getId());

        preferences = getSharedPreferences("data", MODE_PRIVATE);
        user = preferences.getString("user","");
        dataToken = new DataToken(fileActivity.this);

        verifyStorePermission(fileActivity.this);

        Intent intent = getIntent();
        if(intent.getStringExtra("imgPath")!= null){
            Glide.with(getApplication()).load(intent.getStringExtra("imgPath")).into(imgFileActivityPicture);
        }

        CallAPIGetAccount();
        imgbtnFileActivityLui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgbtnFileActivitySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallAPIChangePerson();
                if(imagePath != null){
                    UploadFile();
                }
            }
        });

        constraintLayout14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fileActivity.this,RemovePasswordActivity.class);
                startActivity(intent);
            }
        });

        imgFileActivityEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                Intent result = Intent.createChooser(intent, getText(R.string.choose_file));
                startActivityForResult(result, 10);
            }
        });

        calendar = Calendar.getInstance();

        editFileActivityDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(fileActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,yearf,monthf-1,dayf);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = dayOfMonth+"/"+month+"/"+year;
                editFileActivityDate.setText(date);
            }
        };

    }

    private  void CallAPIGetAccount(){
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.GetPersonInfo(dataToken.getToken(),user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(InfoPerson info){
        person = info.getData();
        if(info.getStatus() == 1){
            editFileActivityName.setText((person.getFullName())!= null ? person.getFullName() : "");
            editFileActivityEmail.setText((person.getEmail())!= null ? person.getEmail() : "");
            editFileActivitySDT.setText((person.getPhoneNumber())!= null ? person.getPhoneNumber() : "");
            if(person.getSex().equals(false)){
                radioGroupSex.check(radioButtonWomen.getId());
            }
            String date = person.getDate();

            String [] dateParts = date.split("-");
            yearf = Integer.parseInt(dateParts[0]);
            monthf = Integer.parseInt(dateParts[1]);
            dayf = Integer.parseInt(dateParts[2]);
            String dateConv = dayf + "/"+monthf+"/" +yearf;
            calendar.set(yearf,monthf,dayf);
            editFileActivityDate.setText(dateConv);
        }
        else{
            Toast.makeText(fileActivity.this, info.getNotification().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private  void CallAPIChangePerson(){
        person.setUser(user);
        person.setDate(editFileActivityDate.getText().toString());
        person.setFullName(editFileActivityName.getText().toString());
        person.setPhoneNumber(editFileActivitySDT.getText().toString());
        if(!radioButtonMan.isChecked()){
            person.setSex(false);
        }else
            person.setSex(true);
        person.setEmail(editFileActivityEmail.getText().toString());

        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.ChangePerson(dataToken.getToken(),person)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleError(Throwable throwable) {
//        Toast.makeText(fileActivity.this, "Error", Toast.LENGTH_SHORT).show();
    }

    private void handleResponse(Message message) {
        Toast.makeText(fileActivity.this, message.getNotification().toString(), Toast.LENGTH_SHORT).show();
        if(message.getStatus()==1){
            SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            if(editFileActivityName.getText().toString()!= null){
                editor.putString("fullNameLogin", editFileActivityName.getText().toString());
            }
            if(editFileActivitySDT.getText().toString()!=null){
                editor.putString("phoneNumber",editFileActivitySDT.getText().toString());
            }
            editor.commit();
        }
        onBackPressed();
    }

    private void UploadFile() {
        DataToken dataToken = new DataToken(this);

        File file = new File(imagePath);
        RequestBody photoContext = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), photoContext);

        RequestBody des = RequestBody.create(MediaType.parse("text/plain"), "t");

        InterfaceAPIService requestInterface =
                new Retrofit.Builder().baseUrl(BASE_Service)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create()).build().create(InterfaceAPIService.class);
        new CompositeDisposable()
                .add(requestInterface.Upload(dataToken.getToken(), photo, des, user).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()).subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Message1 message1) {
        Toast.makeText(fileActivity.this, message1.getNotification().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();
                imagePath = getRealPathFromURI(uri);
                if(imagePath != null){
                    imgFileActivityPicture.setImageURI(uri);
                }else{
                    Toast.makeText(getApplicationContext(), "Lấy hình thất bại", Toast.LENGTH_SHORT).show();
                }
//                Glide.with(getApplication()).load(imagePath).into(imgFileActivityPicture);
            }
        }
    }

    private String getRealPathFromURI(Uri contentUri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private static void verifyStorePermission(Activity activity){
        int permission = ActivityCompat .checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }




}
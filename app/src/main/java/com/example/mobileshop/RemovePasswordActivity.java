package com.example.mobileshop;

import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mobileshop.Object.Message;
import com.example.mobileshop.Object.Person.ChangePassword;
import com.example.mobileshop.Others.DataToken;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.google.android.material.textfield.TextInputEditText;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RemovePasswordActivity extends AppCompatActivity {


    private ImageButton imgbtnRemovePassActivityLui,imgbtnRemovePassActivitySubmit;
    private TextInputEditText editFileActivityPassword,editFileActivityPassword1,editFileActivityPassword2;
    private DataToken dataToken;
    private String user;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_password);

        imgbtnRemovePassActivityLui = findViewById(R.id.imgbtnRemovePassActivityLui);
        editFileActivityPassword = findViewById(R.id.editFileActivityPassword);
        editFileActivityPassword1 = findViewById(R.id.editFileActivityPassword1);
        editFileActivityPassword2 = findViewById(R.id.editFileActivityPassword2);
        imgbtnRemovePassActivitySubmit = findViewById(R.id.imgbtnRemovePassActivitySubmit);

        dataToken = new DataToken(RemovePasswordActivity.this);
        preferences = getSharedPreferences("data", MODE_PRIVATE);
        user = preferences.getString("user","");

        imgbtnRemovePassActivityLui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgbtnRemovePassActivitySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editFileActivityPassword.getText().toString().equals("") || editFileActivityPassword1.getText().toString().equals("") || editFileActivityPassword2.getText().toString().equals("")){
                    Toast.makeText(RemovePasswordActivity.this, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!editFileActivityPassword2.getText().toString().equals(editFileActivityPassword1.getText().toString())){
                    Toast.makeText(RemovePasswordActivity.this, "Mật khẩu mới phải trùng nhau", Toast.LENGTH_SHORT).show();
                    return;
                }

                CallAPIChangePass();
            }
        });
    }

    private  void CallAPIChangePass(){

        ChangePassword changePassword = new ChangePassword(user,editFileActivityPassword.getText().toString(),editFileActivityPassword1.getText().toString());
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.ChangePassword(dataToken.getToken(),changePassword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(Message message) {
        if(message.getStatus()==1){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Save_Pass",editFileActivityPassword1.getText().toString());
            editor.putString("pass",editFileActivityPassword1.getText().toString());
            editor.commit();
            onBackPressed();
        }
        Toast.makeText(RemovePasswordActivity.this, message.getNotification().toString(), Toast.LENGTH_SHORT).show();

    }

    private void handleError(Throwable error){
        Toast.makeText(RemovePasswordActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
    }

}
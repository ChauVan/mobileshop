package com.example.mobileshop;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileshop.Object.Message;
import com.example.mobileshop.Object.SignUp.SignUpUser;
import com.example.mobileshop.Others.MD5;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp2Activity extends AppCompatActivity implements View.OnClickListener {

    private EditText editSignUpActivityEmail,editSignUpActivityPhone,editSignUpActivityCode;
    private Button btnSignUpActivityNext2;
    private SignUpUser signUpUser;
    private ImageButton btnSignUpActivityBack;
    private String OTP = "";
    private TextView txtSignUpActivityCode;
    private String User,FullName,Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);

        editSignUpActivityEmail = findViewById(R.id.editSignUpActivityEmail);
        editSignUpActivityPhone = findViewById(R.id.editSignUpActivityPhone);
        editSignUpActivityCode = findViewById(R.id.editSignUpActivityCode);
        btnSignUpActivityNext2 = findViewById(R.id.btnSignUpActivityNext2);
        btnSignUpActivityBack = findViewById(R.id.btnSignUpActivityBack);
        txtSignUpActivityCode = findViewById(R.id.txtSignUpActivityCode);

        Intent intent = getIntent();
        User = intent.getStringExtra("intend_User");
        FullName = intent.getStringExtra("intend_FullName");
        Password = intent.getStringExtra("intend_Password");

        btnSignUpActivityNext2.setOnClickListener(this);
        btnSignUpActivityBack.setOnClickListener(this);
        txtSignUpActivityCode.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSignUpActivityNext2:{
                if(editSignUpActivityEmail.getText().toString().equals("") || editSignUpActivityPhone.getText().toString().equals("")
                        ||editSignUpActivityCode.getText().toString().equals("") ){
                    Toast.makeText(SignUp2Activity.this, "Vui l??ng nh???p ?????y ????? th??ng tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                signUpUser = new SignUpUser( User,FullName,Password,editSignUpActivityPhone.getText().toString(),editSignUpActivityEmail.getText().toString());
                String hashed =  MD5.HashMD5(editSignUpActivityCode.getText().toString());
                //b??m m?? OTP ???? nh???p ????? so s??nh v???i OTP ??c b??m tr??n Server, ????ng th?? v?? ????ng k??
                if(hashed.toUpperCase().equals(OTP)) {
                    CallAPISignup();
                }
                else{
                    Toast.makeText(SignUp2Activity.this, "L???u l???u OTP sai k??a ", Toast.LENGTH_SHORT).show();
                }
            }break;

            case R.id.btnSignUpActivityBack:{
                onBackPressed();
            }break;

            case R.id.txtSignUpActivityCode:{
                //G???i ????? l???y m?? OTP qua mail
                CallAPIOTP();
            }break;
        }
    }

    private  void CallAPISignup(){
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.PostSignup(signUpUser )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );

    }

    private void handleResponse(Message info){

        try{
            if(info.getStatus() == 1)
            {
                Intent intent = new Intent(SignUp2Activity.this,LoginActivity.class);
                startActivity(intent);
            }
            Toast.makeText(SignUp2Activity.this, info.getNotification().toString(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(SignUp2Activity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void handleError(Throwable error){
        Toast.makeText(SignUp2Activity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
    }

    private  void CallAPIOTP(){
        //Token n??y l?? m???c ?????nh ????? t???o m???t get OTP
        String tokenOTP = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJDb3ZpZDIxVGVhbVRTUCIsImp0aSI6IjdlOTk1ODAwLTljNWYtNGIxZS04OWFmLTgzZTczMTI2MTQ0NCIsImlhdCI6IjkvMTcvMjAyMSA1OjA1OjMxIFBNIiwiR3JvdXAiOiJUU1BUZWFtIiwiRW1haWwiOiJjb3ZpZDIxdHNwQGdtYWlsLmNvbSIsIkhvc3RpbmciOiJjb3ZpZDIxdHNwLnNwYWNlIiwiU2VuZEVtYWlsIjoiWUVTIiwiZXhwIjoxNzE4MjczMTMxLCJpc3MiOiJDb3ZpZDIxVGVhbVRTUCIsImF1ZCI6IkNvdmlkMjFUZWFtVFNQIn0.r3-iHPTOBgQ7hbm96mfj_0vQrSRatyVvs1_Td-KC7oU";
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.GetOTP(tokenOTP, editSignUpActivityEmail.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseOTP, this::handleError) );
    }

    private void handleResponseOTP(Message message) {
        try{
            if(message.getStatus() == 1)
            {
                Toast.makeText(SignUp2Activity.this, "Ki???m tra mail ????? l???y OTP", Toast.LENGTH_SHORT).show();
                OTP = message.getNotification();
            }else
            Toast.makeText(SignUp2Activity.this, message.getNotification().toString(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(SignUp2Activity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }





}
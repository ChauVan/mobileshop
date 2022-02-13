package com.example.mobileshop;

import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;

import com.example.mobileshop.Object.Login.LoginMessage;
import com.example.mobileshop.Object.Login.LoginSocial;
import com.example.mobileshop.Object.Login.LoginUser;
import com.example.mobileshop.Object.Person.InfoPerson;
import com.example.mobileshop.Object.Person.Person;
import com.example.mobileshop.Others.DataToken;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editLoginActivityUsername,editLoginActivityPassword;
    private CheckBox cbLoginActivitySave;
    private Button btnLoginActivityLogin;
    private LoginButton login_fb;
    private ImageView imgLoginActivityFB, imgLoginActivityGG;
    private TextView txtBack;
    private String user, pass,userLogin;
    private Boolean save;
    private LoginUser loginUser;
    private TextView txtLoginActivitySignUp;
    private DataToken dataToken;
    private SharedPreferences preferences ;

    GoogleSignInClient mGoogleSignInClient;
    String WEB_CLIENT_ID ="980304948102-kbnltmrr6st174leej0qbilnhbuhanam.apps.googleusercontent.com";

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editLoginActivityPassword = findViewById(R.id.editLoginActivityPassword);
        editLoginActivityUsername = findViewById(R.id.editLoginActivityUsername);
        cbLoginActivitySave = findViewById(R.id.cbLoginActivitySave);
        btnLoginActivityLogin = findViewById(R.id.btnLoginActivityLogin);
        imgLoginActivityFB = findViewById(R.id.imgLoginActivityFB);
        login_fb = (LoginButton)findViewById(R.id.login_fb);
        imgLoginActivityGG = findViewById(R.id.imgLoginActivityGG);
        txtBack = findViewById(R.id.txtBack);

        txtLoginActivitySignUp = findViewById(R.id.txtLoginActivitySignUp);

        preferences = getSharedPreferences("data",MODE_PRIVATE);

        user = preferences.getString("Save_User","");
        pass = preferences.getString("Save_Pass","");

        //Nếu có bấm nút lưu mật khẩu thì set lại mật khẩu
        save = preferences.getBoolean("save",false);
        if (save){
            editLoginActivityPassword.setText(pass);
            editLoginActivityUsername.setText(user);
            cbLoginActivitySave.setChecked(true);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        txtLoginActivitySignUp.setOnClickListener(this);
        btnLoginActivityLogin.setOnClickListener(this);
        imgLoginActivityFB.setOnClickListener(this);
        imgLoginActivityGG.setOnClickListener(this);
        login_fb.setOnClickListener(this);
        txtBack.setOnClickListener(this);
    }

    private  void CallAPILogin(){
        loginUser = new LoginUser(editLoginActivityUsername.getText().toString(),editLoginActivityPassword.getText().toString());
        userLogin = editLoginActivityUsername.getText().toString();
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.PostLogin(loginUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(LoginMessage info){
        try{

            if(info.getStatus() == 1) {
                SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                dataToken = new DataToken(this);
                dataToken.saveToken(info.getToken().toString());
                editor.putString("fullNameLogin", info.getFullName().toString());
                editor.putString("user",userLogin);
//                editor.putString("pass",editLoginActivityPassword.getText().toString());
                editor.commit();
                CallAPIGetAccount();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
            Toast.makeText(LoginActivity.this, info.getNotification().toString(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(LoginActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            Log.e("wwwwwwwwww", e.getMessage());
        }
    }

    private void handleError(Throwable error){
 //       Toast.makeText(LoginActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txtLoginActivitySignUp:{
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }break;

            case R.id.btnLoginActivityLogin:{
                dataToken = new DataToken(LoginActivity.this);
                SharedPreferences.Editor editor = preferences.edit();

                if(editLoginActivityPassword.getText().toString().equals("") || editLoginActivityUsername.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đầy đủ user và password", Toast.LENGTH_SHORT).show();
                    return;
                }
                //Nếu có bấm lưu mật khẩu thì lưu lại
                if(cbLoginActivitySave.isChecked()){
                    editor.putString("Save_User",editLoginActivityUsername.getText().toString());
                    editor.putString("Save_Pass",editLoginActivityPassword.getText().toString());
                    editor.putBoolean("save",true);
                    editor.commit();
                }
                else {
                    editor.putBoolean("save",false);
                    editor.commit();
                }
                CallAPILogin();
            }break;
            case R.id.imgLoginActivityFB:{
                login_fb.performClick();
            }break;
            case R.id.imgLoginActivityGG:{
                signInSocial();
            }break;
            case R.id.login_fb:{
                LoginFB();
            }break;
            case R.id.txtBack:{
                Intent t = new Intent(LoginActivity.this,MainActivity.class);
                t.putExtra("code", 1);
                startActivity(t);
            }break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }
    //Gọi API để lưu tài thông tin cần thiết
    private  void CallAPIGetAccount(){
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.GetPersonInfo(dataToken.getToken(),editLoginActivityUsername.getText().toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(InfoPerson info){
       Person person = info.getData();
        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("phoneNumber", person.getPhoneNumber().toString());
        editor.putString("email", person.getEmail().toString());
        editor.commit();
    }

    // Login GG
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // LoginFB
//        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            //Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
            userLogin = account.getId();
            CallAPILoginSocial(new LoginSocial(account.getId(), account.getDisplayName(), account.getEmail()));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(this, "Fail", Toast.LENGTH_SHORT).show();
            Log.e("LoginFail", e.getMessage());
        }
    }

    private void signInSocial() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1);
    }

    private void CallAPILoginSocial(LoginSocial user){
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.PostLoginSocial(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }
    // end

    //LoginFB
    private void LoginFB(){
        // request take email of userFB
        login_fb.setReadPermissions("email");

        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        login_fb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Login(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    private void Login(LoginResult loginResult){
        FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if(object != null){
                            try{
                                //txtId.setText(object.getString("id"));
                                userLogin = object.getString("id");
                                CallAPILoginSocial(new LoginSocial(object.getString("id"), object.getString("name"), object.getString("email")));
                            }catch (NullPointerException | JSONException ex){
//                                Log.e("wwwwwwwwwwwwwwwwwww", ex+"");
                            }
                        }
                    }
                }
        );

        Bundle parameters = new Bundle();
        parameters.putString(
                "fields",
                "id, name, email, gender, birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }
    //end
}
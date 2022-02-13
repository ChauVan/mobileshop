package com.example.mobileshop.Others;

import static android.content.Context.MODE_PRIVATE;
import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.mobileshop.MainActivity;
import com.example.mobileshop.Object.Login.LoginMessage;
import com.example.mobileshop.Object.Login.LoginUser;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetNewToken {
    private Context context;

    public GetNewToken(Context context) {
        this.context = context;
    }

    private DataToken dataToken ;
    private SharedPreferences preferences;
    private String user,pass;
    private LoginUser loginUser;
    private String token;


    public void CallAPILoginGetToken(){
        dataToken = new DataToken(context);

        preferences = context.getSharedPreferences("data", MODE_PRIVATE);
        token = preferences.getString("token", "");
        user = preferences.getString("user","");
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.RefreshToken(token,user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    public void handleResponse(LoginMessage info){
        try{
            dataToken = new DataToken(context);
            if(info.getStatus() == 1) {
                dataToken.saveToken(info.getToken());
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("fullNameLogin", info.getFullName().toString());
//                editor.commit();
            }else
                Toast.makeText(context, info.getNotification(), Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleError(Throwable error){
        Toast.makeText(context, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
    }
}

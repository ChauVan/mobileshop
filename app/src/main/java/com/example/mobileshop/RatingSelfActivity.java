package com.example.mobileshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mobileshop.Object.HomeAllProduct.HomeAllProductMessage;
import com.example.mobileshop.Others.DataToken;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.example.mobileshop.adapter.PaidAndRatingAdapter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.mobileshop.Others.ShowNotify.dismissProgressDialog;
import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

public class RatingSelfActivity extends AppCompatActivity {

    private DataToken dataToken;
    private RecyclerView rclBillStatus;
    private String user;
    private PaidAndRatingAdapter homeProductAdapter;

    private ImageButton imgbtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_self);

        dataToken = new DataToken(this);
        rclBillStatus = findViewById(R.id.rclBillStatus);
        imgbtnBack = findViewById(R.id.imgbtnBack);

        SharedPreferences preferences = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        user = preferences.getString("user", "");
        CallApiGetProductPaid();

        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private  void CallApiGetProductPaid(){

//        showProgressDialog(getContext(),"Đang tải");
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.GetRating(dataToken.getToken(),user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(HomeAllProductMessage homeAllProductMessage) {
        if(homeAllProductMessage.getStatus()==1){
            rclBillStatus.setHasFixedSize(false);
            homeProductAdapter = new PaidAndRatingAdapter(homeAllProductMessage.getProducts(),this);
            rclBillStatus.setHasFixedSize(true);
            rclBillStatus.setLayoutManager(new GridLayoutManager(this, 1));
            rclBillStatus.setAdapter(homeProductAdapter);
        }else{
            Toast.makeText(this, homeAllProductMessage.getNotification().toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleError(Throwable error){
        dismissProgressDialog();
        Toast.makeText(this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
    }
}
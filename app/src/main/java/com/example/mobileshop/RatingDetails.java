package com.example.mobileshop;

import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.mobileshop.Object.Message;
import com.example.mobileshop.Object.ProductDetail.PersonRating;
import com.example.mobileshop.Object.ProductDetail.ProductCommentsDetail;
import com.example.mobileshop.Object.ProductDetail.Rating;
import com.example.mobileshop.Object.ProductDetail.RatingREQ;
import com.example.mobileshop.Others.DataToken;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.example.mobileshop.adapter.RatingAdapter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RatingDetails extends AppCompatActivity {

    private RecyclerView rclRatingDetails;
    private RatingAdapter ratingAdapter;
    private String productId;
    private Button btnRatingConfirm;
    private EditText editText;
    private RatingBar ratingBar_yours;
    private String user;
    private SharedPreferences preferences;
    private DataToken dataToken;
    private ImageButton imgbtnRaTingActivityLui;
    private Rating rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_details);
        Intent intent = getIntent();
        productId = intent.getStringExtra("ProductID");
        rating = (Rating) intent.getSerializableExtra("RatingData");

        rclRatingDetails = findViewById(R.id.rclRatingDetails);
        btnRatingConfirm = findViewById(R.id.btnRatingConfirm);
        editText = findViewById(R.id.editText);
        ratingBar_yours = findViewById(R.id.ratingBar_yours);
        imgbtnRaTingActivityLui = findViewById(R.id.imgbtnRaTingActivityLui);

        dataToken = new DataToken(RatingDetails.this);
        preferences = RatingDetails.this.getSharedPreferences("data", MODE_PRIVATE);
        user = preferences.getString("user", "");

        CallAPIGetRating();

        btnRatingConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingREQ ratingREQ= new RatingREQ(user,Integer.parseInt(productId),(int) ratingBar_yours.getRating(),editText.getText().toString());
                CallAPIAddRating(ratingREQ);
            }
        });

        imgbtnRaTingActivityLui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private  void CallAPIGetRating(){
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.GetRating(productId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(ArrayList<PersonRating> personRatings) {
        rclRatingDetails.setHasFixedSize(true);
        ratingAdapter = new RatingAdapter(personRatings,RatingDetails.this);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rclRatingDetails.setLayoutManager(linearLayoutManager);
        rclRatingDetails.setAdapter(ratingAdapter);
    }

    private void handleError(Throwable throwable) {
        Toast.makeText(RatingDetails.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private  void CallAPIAddRating(RatingREQ ratingREQ){

        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.AddRating(dataToken.getToken(),ratingREQ)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(Message message) {
        if(message.getStatus()==1){
            Toast.makeText(RatingDetails.this, "Cảm ơn vì đã đánh giá sản phẩm", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(RatingDetails.this, message.getNotification(), Toast.LENGTH_SHORT).show();
        finish();
        if(message.getStatus()!=1){
            Toast.makeText(RatingDetails.this, message.getNotification(), Toast.LENGTH_SHORT).show();
        }
    }

}
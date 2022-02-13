package com.example.mobileshop;

import static com.example.mobileshop.Others.ShowNotify.dismissProgressDialog;
import static com.example.mobileshop.Others.ShowNotify.showProgressDialog;
import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileshop.Object.AddCart.CartAdd;
import com.example.mobileshop.Object.HomeAllProduct.HomeAllProduct;
import com.example.mobileshop.Object.HomeAllProduct.HomeAllProductMessage;
import com.example.mobileshop.Object.Message;
import com.example.mobileshop.Object.ProductDetail.PersonRating;
import com.example.mobileshop.Object.ProductDetail.ProductDetail;
import com.example.mobileshop.Object.ProductDetail.Rating;
import com.example.mobileshop.Others.ConvertMoney;
import com.example.mobileshop.Others.DataToken;
import com.example.mobileshop.Others.GetNewToken;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.example.mobileshop.adapter.HomeProductAdapter;
import com.example.mobileshop.adapter.PhotoAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.mobileshop.Object.Photo;
import com.example.mobileshop.adapter.PhotoProductAdapter;
import com.example.mobileshop.adapter.ProductDetailAdapter;
import com.example.mobileshop.adapter.RatingAdapter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ProductActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewpagerpro;
    private CircleIndicator circleindicatorpro;
    private PhotoProductAdapter photoProductAdapter;
    private SharedPreferences preferences;
    private String productID;
    private ArrayList<Photo> alPhoto;
    private TextView txtProductActivityName, txtProductActivityGia, totalPay;
    private ArrayList<String> list;
    private Timer mTimer;
    private Button btnAddCart;
    private String user;
    private CartAdd cartAdd;
    private DataToken dataToken;
    private RatingBar ratingBar,ratingBar1;
    private ConvertMoney convertMoney;
    private ImageButton imgntnProductActivityHeart,imgbtnHomeActivityCart;
    //Trạng thái tim khi PÓT lên Serverr
    private Boolean hearted = false;
    private RecyclerView rclProductDetail;
    private ProductDetailAdapter productDetailAdapter;
    private ImageButton imageButton;
    private ConstraintLayout constraintLayout9;

    private TextView txtProductActivitySoSao, txtProductActivitySoBinhLuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        txtProductActivityName = findViewById(R.id.txtProductActivityName);
        txtProductActivityGia = findViewById(R.id.txtProductActivityGia);
        imgntnProductActivityHeart = findViewById(R.id.imgntnProductActivityHeart);
        btnAddCart = findViewById(R.id.btnAddCart);
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar1 = findViewById(R.id.ratingBar1);
        imgbtnHomeActivityCart = findViewById(R.id.imgbtnHomeActivityCart);
        rclProductDetail = findViewById(R.id.rclProductDetail);
        imageButton = findViewById(R.id.imageButton);
        constraintLayout9 = findViewById(R.id.constraintLayout9);
        txtProductActivitySoSao = findViewById(R.id.txtProductActivitySoSao);
        txtProductActivitySoBinhLuan = findViewById(R.id.txtProductActivitySoBinhLuan);
        totalPay = findViewById(R.id.textView2);

        Intent intent = getIntent();
        productID = intent.getStringExtra("ProductID");
        preferences = getSharedPreferences("data", MODE_PRIVATE);
        user = preferences.getString("user","");
        dataToken = new DataToken(ProductActivity.this);

        convertMoney = new ConvertMoney();
        CallAPIGetOneProduct();
        if(!user.equals(""))
        CallAPIGeFavoriteProduct();
        CallAPIGetPRodutcConfig();

        viewpagerpro = findViewById(R.id.viewpagerpro);
        circleindicatorpro = findViewById(R.id.circleindicatorpro);
        btnAddCart.setOnClickListener(this);
        imgntnProductActivityHeart.setOnClickListener(this);
        imgbtnHomeActivityCart.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        constraintLayout9.setOnClickListener(this);

    }

    private void CallAPIGetOneProduct() {
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.GetOneProduct(productID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponses, this::handleError));
    }

    private void handleResponses(HomeAllProduct info) {

        list = info.getListPic();
        txtProductActivityName.setText(info.getName());
        txtProductActivityGia.setText(convertMoney.StringToMoney(info.getPrice() + ""));
        photoProductAdapter = new PhotoProductAdapter(ProductActivity.this, list);
        viewpagerpro.setAdapter(photoProductAdapter);
        circleindicatorpro.setViewPager(viewpagerpro);
        photoProductAdapter.registerDataSetObserver(circleindicatorpro.getDataSetObserver());
        ratingBar.setRating(0);
        ratingBar1.setRating(0);

        totalPay.setText("Đã bán "+info.getTotalPay());

        if(info.getCountrate()!=0){
            double rating = info.getTotalrate()/info.getCountrate();
            ratingBar.setRating((int)rating);
            ratingBar1.setRating((int)rating);

            DecimalFormat twoDForm = new DecimalFormat("#.#");
            txtProductActivitySoSao.setText(Double.valueOf(twoDForm.format(rating))+"");
            txtProductActivitySoBinhLuan.setText(info.getCountrate()+" Đánh giá");
        }
//        autoIMG();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAddCart:{
                if(!user.equals("") ){
                    cartAdd = new CartAdd(user,productID,true,1);
                    CallAPIAddToCart();
                }else{
                    AskLogin();
                }
            }break;
            case R.id.imgntnProductActivityHeart:{
                if(hearted == false){
                    hearted = true;
                    imgntnProductActivityHeart.setImageResource(R.drawable.ic_baseline_favorite_red_24);
                }else{
                    hearted = false;
                    imgntnProductActivityHeart.setImageResource(R.drawable.ic_baseline_favorite_24);
                }break;
            }
            case R.id.imgbtnHomeActivityCart:{
                if(!user.equals("") ){
                    Intent intent = new Intent(ProductActivity.this,CartActivity.class);
                    startActivity(intent);
                }else{
                    AskLogin();
                }
            }break;
            case R.id.imageButton:{
                onBackPressed();
            }break;
            case R.id.constraintLayout9:{
                if(!user.equals("") ){
                    Intent intent = new Intent(ProductActivity.this, RatingDetails.class);
                    intent.putExtra("ProductID",productID);
                    startActivity(intent);
                }else{
                    AskLogin();
                }

            }break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!user.equals(""))
        if(hearted && !user.equals("")){
            CallAPIChangeFavorite();
        }else{
            CallAPIDelFavorite();
        }
    }

    private void AskLogin() {

        if(user.equals("")){
            AlertDialog.Builder b = new AlertDialog.Builder(ProductActivity.this);
            b.setTitle("Xác nhận");
            b.setMessage("Đăng nhập để tiếp tục");
            b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(ProductActivity.this, LoginActivity.class));
                }
            });

            b.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            AlertDialog al = b.create();
            al.show();
        }
    }

    private  void CallAPIAddToCart(){
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.AddToCart(dataToken.getToken(),cartAdd)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(Message info){
        if(info.getStatus() == 1)
        Toast.makeText(ProductActivity.this, info.getNotification(), Toast.LENGTH_SHORT).show();
        if(info.getStatus() == 0)
            Toast.makeText(ProductActivity.this, "Sản phầm đã được thêm sẵn", Toast.LENGTH_SHORT).show();
        if(info.getStatus() != 1 && info.getStatus() != 0)
            Toast.makeText(ProductActivity.this, info.getNotification(), Toast.LENGTH_SHORT).show();
    }

    private  void CallAPIChangeFavorite(){
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.ChangeFavorite(dataToken.getToken(),user,productID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseFavorite, this::handleError) );
    }

    private void handleResponseFavorite(Message message) {
        if(message.getStatus() != 1)
            Toast.makeText(ProductActivity.this, message.getNotification(), Toast.LENGTH_SHORT).show();
    }

    private  void CallAPIDelFavorite(){
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.DelFavorite(dataToken.getToken(),user,productID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseDelFavorite, this::handleError) );
    }

    private void handleResponseDelFavorite(Message message) {

    }

    private void handleError(Throwable error){
        Toast.makeText(ProductActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
    }

    private  void CallAPIGeFavoriteProduct(){
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.GetFavorite(dataToken.getToken(),user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(HomeAllProductMessage homeAllProductMessage) {
        if(homeAllProductMessage.getStatus()==1){
//            Toast.makeText(ProductActivity.this, homeAllProductMessage.getNotification(), Toast.LENGTH_SHORT).show();
            for(int i=0;i<homeAllProductMessage.getProducts().size();i++){
                if(productID.equals( homeAllProductMessage.getProducts().get(i).getId()+"")){
                    hearted = true;
                    imgntnProductActivityHeart.setImageResource(R.drawable.ic_baseline_favorite_red_24);
                    break;
                }
            }
        }else{
            Toast.makeText(ProductActivity.this, homeAllProductMessage.getNotification(), Toast.LENGTH_SHORT).show();
        }
    }

    private  void CallAPIGetPRodutcConfig(){
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.GetProductConfig(productID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(ArrayList<ProductDetail> productDetails) {
        rclProductDetail.setHasFixedSize(true);
        productDetailAdapter = new ProductDetailAdapter(productDetails, ProductActivity.this);
        rclProductDetail.setLayoutManager(new GridLayoutManager(ProductActivity.this, 1));
        rclProductDetail.setAdapter(productDetailAdapter);
    }

};




package com.example.mobileshop.fragment;

import static com.example.mobileshop.Others.ShowNotify.dismissProgressDialog;
import static com.example.mobileshop.Others.ShowNotify.showProgressDialog;
import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mobileshop.CartActivity;
import com.example.mobileshop.MainActivity;
import com.example.mobileshop.Object.HomeAllProduct.HomeAllProduct;
import com.example.mobileshop.Object.HomeAllProduct.HomeAllProductMessage;
import com.example.mobileshop.Others.DataToken;
import com.example.mobileshop.R;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.example.mobileshop.adapter.HomeProductAdapter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FavoriteFragment extends Fragment {

    private DataToken dataToken;
    private String user;
    private ImageButton imgbtnHomeActivityCart;
    private RecyclerView fragFavoProduct;
    private HomeProductAdapter homeProductAdapter;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        CallAPIGeFavoriteProduct();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite,container,false);

        fragFavoProduct = view.findViewById(R.id.fragFavoProduct);
        imgbtnHomeActivityCart = view.findViewById(R.id.imgbtnHomeActivityCart);

        dataToken = new DataToken(getContext());
        SharedPreferences preferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        user = preferences.getString("user", "");
        CallAPIGeFavoriteProduct();

        imgbtnHomeActivityCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private  void CallAPIGeFavoriteProduct(){

//        showProgressDialog(getContext(),"Đang tải");
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
            fragFavoProduct.setHasFixedSize(false);
            homeProductAdapter = new HomeProductAdapter(homeAllProductMessage.getProducts(),getContext());
            fragFavoProduct.setHasFixedSize(true);
            fragFavoProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
            fragFavoProduct.setAdapter(homeProductAdapter);
        }else{
            Toast.makeText(getContext(), homeAllProductMessage.getNotification().toString(), Toast.LENGTH_SHORT).show();
        }
        dismissProgressDialog();
    }


    private void handleError(Throwable error){
        dismissProgressDialog();
        Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
    }
}
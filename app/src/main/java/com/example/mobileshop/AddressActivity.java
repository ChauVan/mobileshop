package com.example.mobileshop;

import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileshop.Object.Address.Address;
import com.example.mobileshop.Object.Address.AllAddressREP;
import com.example.mobileshop.Object.Cart.CartMessage;
import com.example.mobileshop.Others.DataToken;
import com.example.mobileshop.Others.GetNewToken;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.example.mobileshop.adapter.AddressAdapter;
import com.example.mobileshop.adapter.PhotoProductAdapter;
import com.example.mobileshop.adapter.ProductAdapter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddressActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imgbtnAddressActivityLui;
    private ConstraintLayout constraintLayout2;
    private DataToken dataToken;
    private SharedPreferences preferences;
    private GetNewToken getNewToken;
    private String user;
    private RecyclerView rclAddress;
    private AddressAdapter addressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        imgbtnAddressActivityLui = findViewById(R.id.imgbtnAddressActivityLui);
        imgbtnAddressActivityLui.setOnClickListener(this);
        constraintLayout2 = findViewById(R.id.constraintLayout2);
        rclAddress = findViewById(R.id.rclAddress);
        constraintLayout2.setOnClickListener(this);

        preferences = getSharedPreferences("data",MODE_PRIVATE);
        user = preferences.getString("user","");

        dataToken = new DataToken(AddressActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        CallAPIGetAllAdderss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgbtnAddressActivityLui:onBackPressed();
                    break;
            case R.id.constraintLayout2: Intent intent = new Intent(AddressActivity.this, AddressNewActivity.class);
                startActivity(intent);
                break;

        }
    }

    private  void CallAPIGetAllAdderss(){
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.GetAllAddress(dataToken.getToken(),user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleError(Throwable throwable) {
        Toast.makeText(AddressActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
    }

    private void handleResponse(AllAddressREP allAddressREP) {
        rclAddress.setHasFixedSize(true);
        ArrayList<Address> all = new ArrayList<Address>();
        Address add;
        //hiện lên Recycle View những địa chỉ còn hoạt động
        for(int i =0;i<allAddressREP.getAddressList().size();i++){
            if(allAddressREP.getAddressList().get(i).getStatus()!=false){
                add = new Address(allAddressREP.getAddressList().get(i).getAddressId(),allAddressREP.getAddressList().get(i).getUser(),
                        allAddressREP.getAddressList().get(i).getFullname(),allAddressREP.getAddressList().get(i).getPhone(),
                        allAddressREP.getAddressList().get(i).getCity(),allAddressREP.getAddressList().get(i).getDistrict(),
                        allAddressREP.getAddressList().get(i).getWards(),allAddressREP.getAddressList().get(i).getAddress(),
                        allAddressREP.getAddressList().get(i).getAddressDefault(),allAddressREP.getAddressList().get(i).getStatus());
                all.add(add);
            }
        }

        addressAdapter = new AddressAdapter(all,AddressActivity.this);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rclAddress.setLayoutManager(linearLayoutManager);
        rclAddress.setAdapter(addressAdapter);
    }


}
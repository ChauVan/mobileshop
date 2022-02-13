package com.example.mobileshop;

import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileshop.Object.Address.Address;
import com.example.mobileshop.Object.Address.AddressOutAPI.AddressDistrictRep;
import com.example.mobileshop.Object.Address.AddressOutAPI.AddressDistrictData;
import com.example.mobileshop.Object.Address.AddressOutAPI.AddressProvinceData;
import com.example.mobileshop.Object.Address.AddressOutAPI.AddressProvinceREP;
import com.example.mobileshop.Object.Address.AddressOutAPI.AddressWardsData;
import com.example.mobileshop.Object.Address.AddressOutAPI.AddressWardsRep;
import com.example.mobileshop.Object.Message;
import com.example.mobileshop.Others.DataToken;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddressNewActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton imgbtnAddressNewActivityLui;
    private Spinner spnAddressAddProvince,spnAddressAddDistrict,spnAddressAddWards;
    private TextView editAddressActivitySonNha,editAddressActivityName,editAddressActivitySDT;
    private Switch swtAddressDefault;
    private Button btnAddressConfirm;
    private String user;
    private DataToken dataToken;
    ArrayList<AddressProvinceData> alAddressProvinceData;
    ArrayList<AddressDistrictData> alAddressDistrictData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_new);
        imgbtnAddressNewActivityLui = findViewById(R.id.imgbtnAddressNewActivityLui);
        spnAddressAddProvince = findViewById(R.id.spnAddressAddProvince);
        spnAddressAddDistrict = findViewById(R.id.spnAddressAddDistrict);
        spnAddressAddWards = findViewById(R.id.spnAddressAddWards);
        editAddressActivitySonNha = findViewById(R.id.editAddressActivitySonNha);
        editAddressActivityName = findViewById(R.id.editAddressActivityName);
        editAddressActivitySDT = findViewById(R.id.editAddressActivitySDT);
        swtAddressDefault = findViewById(R.id.swtAddressDefault);
        btnAddressConfirm = findViewById(R.id.btnAddressConfirm);
        imgbtnAddressNewActivityLui.setOnClickListener(this);

        dataToken = new DataToken(AddressNewActivity.this);

        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        user = preferences.getString("user","");

        //gọi 1 cái Procince thì 2 cái địa chỉ còn lại chạy theo luôn
        CallAPIGetProvince();

        //Này là khi các giá trị khi bấm trong 1 cái spinner nó đổi thì những cái ở dưới nó cũng đổi theo API của người ta
        spnAddressAddProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int ma = alAddressProvinceData.get(i).getProvinceID();
                CallAPIGetDistrict(ma);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnAddressAddDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int id = alAddressDistrictData.get(i).getDistrictID();
                CallAPIGetWards(id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnAddressConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean sw = false;
                if(swtAddressDefault.isChecked()){
                    sw = true;
                }
                Address address = new Address(0,user,editAddressActivityName.getText().toString(),editAddressActivitySDT.getText().toString(),
                        spnAddressAddProvince.getSelectedItem().toString(),spnAddressAddDistrict.getSelectedItem().toString(),
                        spnAddressAddWards.getSelectedItem().toString(),editAddressActivitySonNha.getText().toString(),sw,true);
                CallAPIAddAddress(address);

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgbtnAddressNewActivityLui:onBackPressed();
                break;
        }
    }

    private  void CallAPIGetProvince(){
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.GetProvince("260fcbb6-d4ae-11eb-aa92-d25db748eae9")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(AddressProvinceREP addressProvinceREP) {
        List listProv = new ArrayList<>();
        List listIdProv = new ArrayList<>();
        alAddressProvinceData = addressProvinceREP.getData();
        for( int i = 0;i<alAddressProvinceData.size();i++){
            listIdProv.add(alAddressProvinceData.get(i).getProvinceID());
            listProv.add(alAddressProvinceData.get(i).getProvinceName());
        }
        ArrayAdapter spinnerAdapterProv = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, listProv);
        spnAddressAddProvince.setAdapter(spinnerAdapterProv);
    }

    private void handleError(Throwable throwable) {
        Toast.makeText(AddressNewActivity.this, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
    }

    private  void CallAPIGetDistrict(int provinceId){
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.GetDistrict("260fcbb6-d4ae-11eb-aa92-d25db748eae9",provinceId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(AddressDistrictRep addressDistrictRep) {
        List listDis = new ArrayList<>();
        List listIdDis = new ArrayList<>();
        alAddressDistrictData = addressDistrictRep.getData();
        for(int i =0;i<alAddressDistrictData.size();i++){
            listIdDis.add(alAddressDistrictData.get(i).getDistrictID());
            listDis.add(alAddressDistrictData.get(i).getDistrictName());
        }
        ArrayAdapter spinnerAdapterDis = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, listDis);
        spnAddressAddDistrict.setAdapter(spinnerAdapterDis);
    }

    private  void CallAPIGetWards(int districtID){
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.GetWards("260fcbb6-d4ae-11eb-aa92-d25db748eae9",districtID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(AddressWardsRep addressWardsRep) {
        List listWar = new ArrayList<>();
        ArrayList<AddressWardsData> alAddressWardsData = addressWardsRep.getData();
        for(int i=0;i<alAddressWardsData.size();i++){
            listWar.add(alAddressWardsData.get(i).getWardName());
        }
        ArrayAdapter spinnerAdapterWar = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, listWar);
        spnAddressAddWards.setAdapter(spinnerAdapterWar);
    }

    private  void CallAPIAddAddress(Address address){
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.AddAddress(dataToken.getToken(),address)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(Message message) {
        Toast.makeText(AddressNewActivity.this, message.getNotification(), Toast.LENGTH_SHORT).show();
        if(message.getStatus()!=1){
            Toast.makeText(AddressNewActivity.this, message.getNotification(), Toast.LENGTH_SHORT).show();
        }else{
            finish();
        }
    }

}
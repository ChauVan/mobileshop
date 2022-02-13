package com.example.mobileshop;

import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileshop.Object.Address.Address;
import com.example.mobileshop.Object.Address.AllAddressREP;
import com.example.mobileshop.Object.Cart.CartChange;
import com.example.mobileshop.Object.Cart.CartChangeProduct;
import com.example.mobileshop.Object.Cart.CartMessage;
import com.example.mobileshop.Object.Cart.CartProduct;
import com.example.mobileshop.Object.Message;
import com.example.mobileshop.Others.ConvertMoney;
import com.example.mobileshop.Others.DataToken;
import com.example.mobileshop.Others.GetNewToken;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.example.mobileshop.adapter.AddressAdapter;
import com.example.mobileshop.adapter.ProductAdapter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartActivity extends AppCompatActivity {

    private TextView txtCartActivityName,txtCartActivityPhone,txtCartActivityNumber,txtCartActivityGia,txtCartActivityNumberProduct;
    private RecyclerView rclCardProduct;
    private Button btnartActivityThanhToan;
    private String user;
    private ProductAdapter productAdapter;
    private DataToken dataToken;
    private SharedPreferences preferences;
    private GetNewToken getNewToken;
    private String fullName,phoneNumber;
    private ImageButton immBtnCartBack;
    private long sumPrice;
    private ConvertMoney cvt;
    ArrayList<CartProduct> all;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        txtCartActivityName = findViewById(R.id.txtCartActivityName);
        txtCartActivityPhone = findViewById(R.id.txtCartActivityPhone);
        txtCartActivityNumber = findViewById(R.id.txtCartActivityNumber);
        txtCartActivityGia = findViewById(R.id.txtCartActivityGia);
        txtCartActivityNumberProduct = findViewById(R.id.txtCartActivityNumberProduct);
        rclCardProduct = findViewById(R.id.rclCardProduct);
        btnartActivityThanhToan = findViewById(R.id.btnartActivityThanhToan);
        immBtnCartBack = findViewById(R.id.immBtnCartBack);

        cvt = new ConvertMoney();

        preferences = getSharedPreferences("data",MODE_PRIVATE);
        user = preferences.getString("user","");
        fullName = preferences.getString("fullNameLogin","");
        phoneNumber = preferences.getString("phoneNumber","");

        txtCartActivityName.setText(fullName);
        txtCartActivityPhone.setText(phoneNumber);

        dataToken = new DataToken(CartActivity.this);
        CallAPIGetCart();

        immBtnCartBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnartActivityThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Này để lấy cái list các sản phẫm đã sửa đổi trong RecycleView ra ngoài
                all = productAdapter.getAlcartProduct();
                getPriceNumber(all);
                
                //Kiểm tra tối thiểu 1 sản phầm
                boolean check = false;
                for(int i=all.size()-1;i>=0;i--){
                    if(all.get(i).getProductStatus().equals("true")){
                        check = true;
                    }
                }
                if (check==false){
                    Toast.makeText(CartActivity.this, "Bạn phải chọn ít nhất một sản phầm", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(CartActivity.this,CheckOutActivity.class);
                    intent.putExtra("sendList",all);
                    intent.putExtra("productAmount",txtCartActivityNumber.getText()+"");
                    intent.putExtra("sumPrice",sumPrice+"");
                    startActivity(intent);
                }
            }
        });

    }

    private  void CallAPIGetCart(){
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.GetCart(dataToken.getToken(),user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(CartMessage info){
        rclCardProduct.setHasFixedSize(true);
        productAdapter = new ProductAdapter(info.getCart(),CartActivity.this);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rclCardProduct.setLayoutManager(linearLayoutManager);
        rclCardProduct.setAdapter(productAdapter);
        all = productAdapter.getAlcartProduct();
        getPriceNumber(all);
    }

    //Này để ghi cái price khi get API về
    public void getPriceNumber(ArrayList<CartProduct> allist){
        sumPrice=0;
        int productAmount = allist.size();
        int productAmountCheck = 0;

        for (int i=0;i<allist.size();i++){
            if(allist.get(i).getProductStatus().equals("true")){
                productAmountCheck = productAmountCheck + allist.get(i).getAmount();
                sumPrice += allist.get(i).getAmount() * allist.get(i).getPrice();
            }
        }

        txtCartActivityNumber.setText(productAmountCheck+"");
        txtCartActivityGia.setText(cvt.StringToMoney(sumPrice+"") );
        txtCartActivityNumberProduct.setText(productAmount+" sản phầm");
    }

    private void handleError(Throwable error){
        Toast.makeText(CartActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
    }

    //Nếu mà chuyển cảnh thì phải lưu các giá trị mình đã thay dổi vào Server
    @Override
    protected void onPause() {
        getPriceNumber(all);
        super.onPause();
        all = productAdapter.getAlcartProduct();
        ArrayList<CartChangeProduct> alCartChangeProduct = new ArrayList<>();
        for (int i = 0 ;i < all.size();i++){
            CartChangeProduct cartChangeProduct1 = new CartChangeProduct(all.get(i).getProductId(),all.get(i).getProductStatus(),all.get(i).getAmount());
            alCartChangeProduct.add(cartChangeProduct1);
        }
        CallAPIChangeCart(alCartChangeProduct);

    }

    private  void CallAPIChangeCart(ArrayList<CartChangeProduct> alCartChangeProduct){

        CartChange cartChange = new CartChange(user,alCartChangeProduct);
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.ChangeCart(dataToken.getToken(),cartChange)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(Message message) {
        if(message.getStatus()!=1)
            Toast.makeText(CartActivity.this, message.getNotification().toString(), Toast.LENGTH_SHORT).show();
    }

//    public void setDataActivity(String sumPrice,String productAmount,String productAmountCheck){
//        txtCartActivityNumberProduct.setText(productAmount);
//        txtCartActivityNumber.setText(productAmountCheck);
//        txtCartActivityGia.setText(sumPrice);
//    }



}
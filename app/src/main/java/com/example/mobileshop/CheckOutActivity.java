package com.example.mobileshop;

import static com.example.mobileshop.Others.ShowNotify.dismissProgressDialog;
import static com.example.mobileshop.Others.ShowNotify.showProgressDialog;
import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobileshop.Object.Address.Address;
import com.example.mobileshop.Object.Address.AllAddressREP;
import com.example.mobileshop.Object.Cart.CartMessage;
import com.example.mobileshop.Object.Cart.CartProduct;
import com.example.mobileshop.Object.CheckOut.CheckOutREQ;
import com.example.mobileshop.Object.Message;
import com.example.mobileshop.Others.ConvertMoney;
import com.example.mobileshop.Others.DataToken;
import com.example.mobileshop.Others.GetNewToken;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.example.mobileshop.adapter.CheckOutAdapter;
import com.example.mobileshop.adapter.ProductAdapter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckOutActivity extends AppCompatActivity {

    private RecyclerView rclCheckOutActivityProduct;
    private CheckOutAdapter checkOutAdapter;
    private Button button6;
    private DataToken dataToken;
    private GetNewToken getNewToken;
    private CheckOutREQ checkOutREQ;
    private SharedPreferences preferences;
    private String user,productAmount,productPrice,productPriceToBill;
    private EditText edtCheckOutMess;
    private TextView txtCheckOutActivityTien,txtCheckOutActivityTienTemp,txtCheckOutActivityTienMain,txtCheckOutActivityCount,txtCheckOutActivityName,txtCheckOutActivitySDT;
    private TextView txtCheckOutActivitySoNha,txtCheckOutActivityArea,txtCheckOutActivityTienAll;
    private ConstraintLayout constraintLayout26;
    private ConvertMoney convertMoney = new ConvertMoney();
    private ImageButton imgbtnCheckOutActivityBack;
    private ImageView imageView14;
    //Để gọi API lấy địa chỉ mặc định không gọi thì mặc định -1
    private int addressID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        rclCheckOutActivityProduct = findViewById(R.id.rclCheckOutActivityProduct);
        button6 = findViewById(R.id.button6);
        edtCheckOutMess = findViewById(R.id.edtCheckOutMess);
        txtCheckOutActivityTien = findViewById(R.id.txtCheckOutActivityTien);
        txtCheckOutActivityTienTemp = findViewById(R.id.txtCheckOutActivityTienTemp);
        txtCheckOutActivityTienMain = findViewById(R.id.txtCheckOutActivityTienMain);
        txtCheckOutActivityCount = findViewById(R.id.txtCheckOutActivityCount);
        constraintLayout26 = findViewById(R.id.constraintLayout26);
        txtCheckOutActivityName = findViewById(R.id.txtCheckOutActivityName);
        txtCheckOutActivitySDT = findViewById(R.id.txtCheckOutActivitySDT);
        txtCheckOutActivitySoNha = findViewById(R.id.txtCheckOutActivitySoNha);
        txtCheckOutActivityArea = findViewById(R.id.txtCheckOutActivityArea);
        txtCheckOutActivityTienAll = findViewById(R.id.txtCheckOutActivityTienAll);
        imgbtnCheckOutActivityBack = findViewById(R.id.imgbtnCheckOutActivityBack);
        imageView14 = findViewById(R.id.imageView14);

        preferences = getSharedPreferences("data",MODE_PRIVATE);
        user = preferences.getString("user","");

        dataToken = new DataToken(CheckOutActivity.this);

        Intent intent = getIntent();
        productAmount = intent.getStringExtra("productAmount");
        productPrice = convertMoney.StringToMoney(intent.getStringExtra("sumPrice"));
        productPriceToBill = convertMoney.StringToMoney((Double.parseDouble(intent.getStringExtra("sumPrice"))*105/100)+"");
        txtCheckOutActivityTien.setText(productPrice);
        txtCheckOutActivityTienMain.setText(productPriceToBill);
        txtCheckOutActivityTienTemp.setText(productPrice);
        txtCheckOutActivityTienAll.setText(productPriceToBill);
        txtCheckOutActivityCount.setText("Tổng số tiền(" + productAmount + " sản phẩm)");

        //Nhập dữ liệu từ bên Cart qua rồi xóa những cái nào không tick true
        ArrayList<CartProduct> cartProduct = (ArrayList<CartProduct>)getIntent().getSerializableExtra("sendList");
        int k = cartProduct.size();
        for(int i=k-1;i>=0;i--){
            if(cartProduct.get(i).getProductStatus().equals("false")){
                cartProduct.remove(i);
            }
        }

        rclCheckOutActivityProduct.setHasFixedSize(true);
        checkOutAdapter = new CheckOutAdapter(cartProduct,CheckOutActivity.this);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rclCheckOutActivityProduct.setLayoutManager(linearLayoutManager);
        rclCheckOutActivityProduct.setAdapter(checkOutAdapter);

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addressID == -1){
                    Toast.makeText(CheckOutActivity.this, "Bạn phải chọn một địa chỉ để thành toán", Toast.LENGTH_SHORT).show();
                    return;
                }
                AskCreateBill();
            }
        });

        constraintLayout26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(CheckOutActivity.this,AddressActivity.class);
                startActivityForResult(intent1,1);
            }
        });

        imgbtnCheckOutActivityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imageView14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckOutActivity.this,PayActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Mới vô set lại mặc định nếu chuyển từ Chọn địa chỉ về lại nếu không nó sẽ lưu cái đã xóa false && mặc định true
        addressID = -1;
        txtCheckOutActivityArea.setText("Trống | Trống | Trống");
        txtCheckOutActivitySoNha.setText("Trống");
        txtCheckOutActivitySDT.setText("Trống");
        txtCheckOutActivityName.setText("Trống");
        //Này để set địa chỉ nếu có đủa chỉ mặc định
        CallAPIGetAdderss();
    }

    private void AskCreateBill() {
            AlertDialog.Builder b = new AlertDialog.Builder(CheckOutActivity.this);
            b.setTitle("Xác nhận");
            b.setMessage("Bạn có chắc muốn đặt hàng ?");
            b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    CallAPICreateBill();
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

    private  void CallAPICreateBill(){
        showProgressDialog(CheckOutActivity.this,"Đang tải");
        checkOutREQ = new CheckOutREQ(user,addressID,edtCheckOutMess.getText().toString());
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.CreateBill(dataToken.getToken(),checkOutREQ)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );
    }

    private void handleResponse(Message message) {
        dismissProgressDialog();

        if(message.getStatus()==1){
            Toast.makeText(CheckOutActivity.this, "Mua thành công vui lòng kiểm tra Email", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CheckOutActivity.this,MainActivity.class);
            startActivity(intent);
        }else
        Toast.makeText(CheckOutActivity.this, message.getNotification(), Toast.LENGTH_SHORT).show();

    }

    private void handleError(Throwable error){
        dismissProgressDialog();
        Toast.makeText(CheckOutActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
    }

    private  void CallAPIGetAdderss(){
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

    private void handleResponse(AllAddressREP allAddressREP) {
        ArrayList<Address> alAddress = allAddressREP.getAddressList();
        int i ;
        for( i = 0 ; i<alAddress.size() ; i++){
            if(alAddress.get(i).getAddressDefault().equals(true) && alAddress.get(i).getStatus().equals(true)){
                String area =  alAddress.get(i).getWards() + ", " + alAddress.get(i).getDistrict() + ", " + alAddress.get(i).getCity();
                addressID = alAddress.get(i).getAddressId();
                txtCheckOutActivityArea.setText(area);
                txtCheckOutActivitySoNha.setText(alAddress.get(i).getAddress());
                txtCheckOutActivitySDT.setText(alAddress.get(i).getPhone());
                txtCheckOutActivityName.setText(alAddress.get(i).getFullname());
                break;
            }
        }
    }

//    public void getPriceNumber(ArrayList<CartProduct> allist){
//        allist = productAdapter.getAlcartProduct();
//        long sumPrice=0;
//        int productAmount = allist.size()+1;
//        int productAmountCheck = 0;
//
//        for (int i=0;i<allist.size();i++){
//            if(allist.get(i).getProductStatus().equals("true")){
//                productAmountCheck = productAmountCheck + allist.get(i).getAmount();
//                sumPrice += allist.get(i).getAmount() * allist.get(i).getPrice();
//            }
//        }
//
//        txtCartActivityNumber.setText(productAmountCheck+"");
//        txtCartActivityGia.setText(sumPrice+"");
//        txtCartActivityNumberProduct.setText(productAmount+" sản phầm");
//    }
}
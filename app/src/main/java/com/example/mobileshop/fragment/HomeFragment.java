package com.example.mobileshop.fragment;


import static android.content.Context.MODE_PRIVATE;
import static com.example.mobileshop.Others.ShowNotify.dismissProgressDialog;
import static com.example.mobileshop.Others.ShowNotify.showProgressDialog;
import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.mobileshop.CartActivity;
import com.example.mobileshop.LoginActivity;
import com.example.mobileshop.Object.Cart.CartMessage;
import com.example.mobileshop.Object.HomeAllProduct.HomeAllProduct;
import com.example.mobileshop.Object.HomeAllProduct.HomeAllProductMessage;
import com.example.mobileshop.Object.Photo;
import com.example.mobileshop.R;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.example.mobileshop.adapter.HomeProductAdapter;
import com.example.mobileshop.adapter.PhotoAdapter;
import com.example.mobileshop.adapter.ProductAdapter;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

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

public class HomeFragment extends Fragment implements View.OnClickListener {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PhotoAdapter photoAdapter;
    private List<Photo> mListPhoto;
    private Timer mTimer;
    private ImageButton imgbtnHomeActivityCart;
    private RecyclerView rclHomeProduct;
    private HomeProductAdapter homeProductAdapter;
    private SharedPreferences preferences;
    private EditText editHomeActivitySearch;
    private TextView txtHomeActivitySmartPhone, txtHomeActivityLaptop, txtHomeActivityTablet, txtHomeActivityHeadphone, txtHomeActivitySmartWatch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        rclHomeProduct = view.findViewById(R.id.rclHomeProduct);
        CallAPIGetallProduct();

        imgbtnHomeActivityCart = view.findViewById(R.id.imgbtnHomeActivityCart);
        viewPager = view.findViewById(R.id.viewpager);
        circleIndicator = view.findViewById(R.id.circleindicator);
        editHomeActivitySearch = view.findViewById(R.id.editHomeActivitySearch);
        txtHomeActivitySmartPhone = view.findViewById(R.id.txtHomeActivitySmartPhone);
        txtHomeActivityLaptop = view.findViewById(R.id.txtHomeActivityLaptop);
        txtHomeActivityTablet = view.findViewById(R.id.txtHomeActivityTablet);
        txtHomeActivityHeadphone = view.findViewById(R.id.txtHomeActivityHeadphone);
        txtHomeActivitySmartWatch = view.findViewById(R.id.txtHomeActivitySmartWatch);

        mListPhoto = getListPhoto();
        photoAdapter = new PhotoAdapter(getContext(), mListPhoto);
        viewPager.setAdapter(photoAdapter);
        circleIndicator.setViewPager(viewPager);
        photoAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());
        autoIMG();

        imgbtnHomeActivityCart.setOnClickListener(this);
        txtHomeActivitySmartPhone.setOnClickListener(this);
        txtHomeActivityLaptop.setOnClickListener(this);
        txtHomeActivityTablet.setOnClickListener(this);
        txtHomeActivityHeadphone.setOnClickListener(this);
        txtHomeActivitySmartWatch.setOnClickListener(this);

        editHomeActivitySearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN || i == EditorInfo.IME_ACTION_SEARCH)
                    Search();
                return true;
            }

        });

        return view;
    }

    private void Search() {
        if (!editHomeActivitySearch.getText().toString().equals(""))
            CallAPISearch(editHomeActivitySearch.getText().toString());
        else
            CallAPIGetallProduct();
    }

    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.img1));
        list.add(new Photo(R.drawable.img2));
        list.add(new Photo(R.drawable.img3));
        list.add(new Photo(R.drawable.img1));

        return list;
    }

    //Tự động chaỵ hình
    private void autoIMG() {
        if (mListPhoto == null || mListPhoto.isEmpty() || viewPager == null) {
            return;
        }
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = mListPhoto.size() - 1;
                        if (currentItem < totalItem) {
                            currentItem++;
                            viewPager.setCurrentItem(currentItem);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtnHomeActivityCart: {
                CartPress();
            }
            break;
            case R.id.txtHomeActivitySmartPhone: {
                CallAPITypeDevice(2);
                viewPager.setVisibility(View.GONE);
                circleIndicator.setVisibility(View.GONE);
            }
            break;
            case R.id.txtHomeActivityLaptop: {
                CallAPITypeDevice(1);
                viewPager.setVisibility(View.GONE);
                circleIndicator.setVisibility(View.GONE);
            }
            break;
            case R.id.txtHomeActivityTablet: {
                CallAPITypeDevice(3);
                viewPager.setVisibility(View.GONE);
                circleIndicator.setVisibility(View.GONE);
            }
            break;
            case R.id.txtHomeActivityHeadphone: {
                CallAPITypeDevice(5);
                viewPager.setVisibility(View.GONE);
                circleIndicator.setVisibility(View.GONE);
            }
            break;
            case R.id.txtHomeActivitySmartWatch: {
                CallAPITypeDevice(4);
                viewPager.setVisibility(View.GONE);
                circleIndicator.setVisibility(View.GONE);
            }
            break;
        }
    }

    private void CartPress(){
        if(AskLogin());
        if (!preferences.getString("user", "").equals(""))
            startActivity(new Intent(getContext(), CartActivity.class));
    }

    private void CallAPIGetallProduct() {
        showProgressDialog(getContext(), "Đang tải");
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.GetAllProduct()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(ArrayList<HomeAllProduct> info) {
        dismissProgressDialog();
        rclHomeProduct.setHasFixedSize(true);
        homeProductAdapter = new HomeProductAdapter(info, getContext());
        rclHomeProduct.setHasFixedSize(true);
        rclHomeProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rclHomeProduct.setAdapter(homeProductAdapter);
        dismissProgressDialog();
    }

    private void handleError(Throwable error) {
        dismissProgressDialog();
        Toast.makeText(getContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();
    }

    public boolean AskLogin() {
        preferences = getContext().getSharedPreferences("data", MODE_PRIVATE);
        if (preferences.getString("user", "").equals("")) {
            AlertDialog.Builder b = new AlertDialog.Builder(getContext());
            b.setTitle("Xác nhận");
            b.setMessage("Đăng nhập để tiếp tục");
            b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
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
            return true;
        }else
            return false;

    }

    private void CallAPISearch(String name) {
        showProgressDialog(getContext(), "Đang tải");
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.SearchDevice(name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(HomeAllProductMessage homeAllProductMessage) {
        dismissProgressDialog();
        ArrayList<HomeAllProduct> alhomeAllProduct = homeAllProductMessage.getProducts();
        rclHomeProduct.setHasFixedSize(true);
        homeProductAdapter = new HomeProductAdapter(alhomeAllProduct, getContext());
        rclHomeProduct.setHasFixedSize(true);
        rclHomeProduct.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rclHomeProduct.setAdapter(homeProductAdapter);
        viewPager.setVisibility(View.GONE);
        circleIndicator.setVisibility(View.GONE);
        dismissProgressDialog();
    }

    private void CallAPITypeDevice(int name) {
//        showProgressDialog(getContext(),"Đang tải");
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.TypeDevice(name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError));
    }


}
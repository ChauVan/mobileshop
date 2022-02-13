package com.example.mobileshop.fragment;

import static android.content.Context.MODE_PRIVATE;

import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mobileshop.AddressActivity;
import com.example.mobileshop.BillStatus;
import com.example.mobileshop.CartActivity;
import com.example.mobileshop.LoginActivity;
import com.example.mobileshop.MainActivity;
import com.example.mobileshop.Object.Message;
import com.example.mobileshop.Others.DataToken;
import com.example.mobileshop.R;
import com.example.mobileshop.RatingSelfActivity;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.example.mobileshop.SignUpActivity;
import com.example.mobileshop.fileActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.concurrent.Executor;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class PersonFragment extends Fragment implements View.OnClickListener {

    String imgPath;
    private ConstraintLayout constraintLayout19, constraintLayout20, productPaids, ratingSelf;
    private ImageButton imgbtnInformationSetting,imgbtnHomeActivityCart;
    private Button btnPersonFragLogOut;
    private SharedPreferences preferences;
    private String user,fullName;
    private ShapeableImageView imgPersonActivityPicture;
    private TextView txtFraPersonName;
    private GoogleSignInClient mGoogleSignInClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);
        constraintLayout20 = view.findViewById(R.id.constraintLayout20);
        constraintLayout19 = view.findViewById(R.id.constraintLayout19);
        imgbtnInformationSetting = view.findViewById(R.id.imgbtnInformationSetting);
        btnPersonFragLogOut = view.findViewById(R.id.btnPersonFragLogOut);
        txtFraPersonName = view.findViewById(R.id.txtFraPersonName);
        imgPersonActivityPicture = view.findViewById(R.id.imgPersonActivityPicture);
        imgbtnHomeActivityCart = view.findViewById(R.id.imgbtnHomeActivityCart);
        ratingSelf = view.findViewById(R.id.constraintLayout14);
        productPaids = view.findViewById(R.id.constraintLayout15);

        constraintLayout19.setOnClickListener(this);
        constraintLayout20.setOnClickListener(this);
        imgbtnInformationSetting.setOnClickListener(this);
        btnPersonFragLogOut.setOnClickListener(this);
        imgbtnHomeActivityCart.setOnClickListener(this);
        productPaids.setOnClickListener(this);
        ratingSelf.setOnClickListener(this);

        preferences = getContext().getSharedPreferences("data", MODE_PRIVATE);
        user = preferences.getString("user","");
        fullName = preferences.getString("fullNameLogin","");
        txtFraPersonName.setText(fullName);

        UploadFile();

//        //Nếu mà không có user thì nó ẩn nút đăng xuất
//        if(!user.equals(""))
//            btnPersonFragLogOut.setVisibility(View.VISIBLE);
//        else
//            btnPersonFragLogOut.setVisibility(View.INVISIBLE);
        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.constraintLayout19:
                if(user.equals("")){
                    AskLogin();
                }else{
                    Intent intent = new Intent(getActivity().getApplication(), fileActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.constraintLayout20:
                if(user.equals("")){
                    AskLogin();
                }else{
                Intent intent1 = new Intent(getActivity().getApplication(), AddressActivity.class);
                startActivity(intent1);
                }
                break;
            case R.id.btnPersonFragLogOut:
//                Intent intent3 = new Intent(getActivity().getApplication(), LoginActivity.class);
//                startActivity(intent3);
                AskLogOut();
                break;
//            case R.id.imgbtnInformationSetting:
//                Intent intent2 = new Intent(getContext(), SignUpActivity.class);
//                startActivity(intent2);
//                break;
            case R.id.constraintLayout14: {
                Intent intent = new Intent(getContext(), RatingSelfActivity.class);
                startActivity(intent);
            }break;
            case R.id.imgbtnHomeActivityCart:{
                Intent intent = new Intent(getContext(), CartActivity.class);
                startActivity(intent);
            }break;
            case R.id.constraintLayout15:{
                Intent intent = new Intent(getContext(), BillStatus.class);
                startActivity(intent);
            }break;

            case R.id.imgbtnInformationSetting:{
                Intent intent = new Intent(getActivity().getApplication(), fileActivity.class);
                intent.putExtra("imgPath",imgPath);
                startActivity(intent);

            }break;
        }

    }

    private void AskLogin() {
//        preferences = getContext().getSharedPreferences("data", MODE_PRIVATE);
        if(user.equals("")){
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
        }
    }

    private void AskLogOut(){
        AlertDialog.Builder b = new AlertDialog.Builder(getContext());
        b.setTitle("Xác nhận");
        b.setMessage("Bạc có chắc muốn đăng xuất");
        b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                preferences = getContext().getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("user", "");
                editor.putString("pass", "");
                editor.putString("fullNameLogin", "");
                editor.putString("phoneNumber", "");
                editor.putString("email", "");
                editor.putString("token", "");
                editor.commit();
                startActivity(new Intent(getContext(),MainActivity.class));
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

    private void signOutSocial() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener((Executor) this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        task.addOnSuccessListener(v -> {
                        });
                        task.addOnFailureListener(v -> Toast.makeText(getContext(), "Fail", Toast.LENGTH_SHORT).show());
                    }
                });
    }


    @Override
    public void onStop() {
        super.onStop();
//        startActivity(new Intent(getContext(), MainActivity.class));
    }

    private void UploadFile() {
        DataToken dataToken = new DataToken(getContext());

        InterfaceAPIService requestInterface =
                new Retrofit.Builder().baseUrl(BASE_Service)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create()).build().create(InterfaceAPIService.class);
        new CompositeDisposable()
                .add(requestInterface.getLink(dataToken.getToken(), user).observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io()).subscribe(this::handleResponse, this::handleError));
    }

    private void handleResponse(Message message) {
        if(message.getStatus()==1){
            Glide.with(getContext()).load(message.getNotification()).into(imgPersonActivityPicture);
            imgPath=message.getNotification();
        }


    }

    private void handleError(Throwable throwable) {
    }
}

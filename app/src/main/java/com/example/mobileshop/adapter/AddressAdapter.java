package com.example.mobileshop.adapter;

import static com.example.mobileshop.Others.ShowNotify.dismissProgressDialog;
import static com.example.mobileshop.Others.ShowNotify.showProgressDialog;
import static com.example.mobileshop.Service.InterfaceAPIService.BASE_Service;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileshop.AddressActivity;
import com.example.mobileshop.LoginActivity;
import com.example.mobileshop.MainActivity;
import com.example.mobileshop.Object.Address.Address;
import com.example.mobileshop.Object.Address.AddressOutAPI.AddressProvinceData;
import com.example.mobileshop.Object.Address.AllAddressREP;
import com.example.mobileshop.Object.Message;
import com.example.mobileshop.Others.DataToken;
import com.example.mobileshop.Others.ItemClickListener;
import com.example.mobileshop.R;
import com.example.mobileshop.Service.InterfaceAPIService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>{

    ArrayList<Address> alAddress ;
    Context context;
    private DataToken dataToken;
    int row_index;int addressID;
    boolean changeDefault = false;
    String user;

    public AddressAdapter(ArrayList<Address> alAddress, Context context) {
        this.alAddress = alAddress;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_address,parent,false);
        SharedPreferences preferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        user = preferences.getString("user","");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int index = position;
        String area =  alAddress.get(index).getWards() + ", " + alAddress.get(index).getDistrict() + ", " + alAddress.get(index).getCity();
        holder.txtAddressArea.setText(area);
        holder.txtAddressSoNha.setText(alAddress.get(index).getAddress());
        holder.txtAddressSĐT.setText(alAddress.get(index).getPhone());
        holder.txtAddressName.setText(alAddress.get(index).getFullname());
        //Chọn đỉa chỉ nào thì lên server cái đó
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                row_index = position;
                addressID = alAddress.get(position).getAddressId();
                holder.addressItemChangeBackground.setBackgroundColor(100);
                Address address = new Address(addressID,user,alAddress.get(position).getFullname(),alAddress.get(position).getPhone(),
                        alAddress.get(position).getCity(),alAddress.get(position).getDistrict(),alAddress.get(position).getWards(),alAddress.get(position).getAddress(),
                        true,alAddress.get(position).getStatus());
                showProgressDialog(context,"Đang đổi");
                CallAPIUpdateAdderss(address);

                changeDefault = true;

                notifyDataSetChanged();

            }
        });

//        if(alAddress.get(position).getAddressDefault()){
//            holder.addressItemChangeBackground.setBackgroundColor(Color.parseColor("#03A9F4"));
//        }


        //hiện sáng nút chọn địa chỉ
        if(row_index==index && addressID == alAddress.get(index).getAddressId()){
            holder.addressItemChangeBackground.setBackgroundColor(Color.parseColor("#03A9F4"));
        }else{
            if(changeDefault){
                holder.addressItemChangeBackground.setBackgroundColor(Color.parseColor("#ffffff"));
            }else{
                if(alAddress.get(position).getAddressDefault()){
                    holder.addressItemChangeBackground.setBackgroundColor(Color.parseColor("#03A9F4"));
                }
            }
        }

        holder.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskDelete(index);
            }
        });
    }

    private void AskDelete(int index) {
            AlertDialog.Builder b = new AlertDialog.Builder(context);
            b.setTitle("Xác nhận");
            b.setMessage("Bạn có chắc chắn muốn xóa địa chỉ này");
            b.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Address address = new Address(alAddress.get(index).getAddressId(),user,alAddress.get(index).getFullname(),alAddress.get(index).getPhone(),
                            alAddress.get(index).getCity(),alAddress.get(index).getDistrict(),alAddress.get(index).getWards(),alAddress.get(index).getAddress(),
                            false,false);
                    showProgressDialog(context,"Đang xóa");
                    CallAPIUpdateAdderss(address);
                    alAddress.remove(index);
                    notifyDataSetChanged();
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

    @Override
    public int getItemCount() {
        return alAddress.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView txtAddressName,txtAddressSĐT,txtAddressSoNha,txtAddressArea;
        public ItemClickListener itemClickListener;
        public ConstraintLayout addressItemChangeBackground;
        public ImageView imageView3;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAddressName = itemView.findViewById(R.id.txtAddressName);
            txtAddressSĐT = itemView.findViewById(R.id.txtAddressSĐT);
            txtAddressSoNha = itemView.findViewById(R.id.txtAddressSoNha);
            txtAddressArea = itemView.findViewById(R.id.txtAddressArea);
            addressItemChangeBackground = itemView.findViewById(R.id.addressItemChangeBackground);
            imageView3 = itemView.findViewById(R.id.imageView3);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            this.itemClickListener.onItemClick(view, getAdapterPosition());
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
    }

    private  void CallAPIUpdateAdderss(Address address){

        dataToken = new DataToken(context);
        InterfaceAPIService requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(InterfaceAPIService.class);

        new CompositeDisposable().add(requestInterface.UpdateAddress(dataToken.getToken(),address)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError) );

    }

    private void handleError(Throwable throwable) {
        dismissProgressDialog();
        Toast.makeText(context, throwable.getMessage()+"", Toast.LENGTH_SHORT).show();
    }

    private void handleResponse(Message message) {
        dismissProgressDialog();
        if(message.getStatus()!=1)
        Toast.makeText(context, message.getNotification()+"", Toast.LENGTH_SHORT).show();
    }


}

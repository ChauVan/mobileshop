package com.example.mobileshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileshop.Object.Cart.CartProduct;
import com.example.mobileshop.Others.ConvertMoney;
import com.example.mobileshop.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CheckOutAdapter extends  RecyclerView.Adapter<CheckOutAdapter.ViewHolder>{

    private ArrayList<CartProduct> alCheckOutProduct;
    private Context context;
    private ConvertMoney convertMoney = new ConvertMoney();

    public CheckOutAdapter(ArrayList<CartProduct> alcartProduct, Context context) {
        this.alCheckOutProduct = alcartProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_checkout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtItemCartProductName.setText(alCheckOutProduct.get(position).getProductName()+"");
        holder.txtProductNumber.setText("x"+alCheckOutProduct.get(position).getAmount()+"");
        holder.txtItemCartProductPrice.setText(convertMoney.StringToMoney(alCheckOutProduct.get(position).getPrice()+""));
        Glide.with(context).load(alCheckOutProduct.get(position).getPic()).into(holder.imgItemCartPhotoCheckout);
    }

    @Override
    public int getItemCount() {
        return alCheckOutProduct.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgItemCartPhotoCheckout;
        public TextView txtItemCartProductName,txtProductNumber,txtItemCartProductPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemCartPhotoCheckout = itemView.findViewById(R.id.imgItemCartPhotoCheckout);
            txtItemCartProductName = itemView.findViewById(R.id.txtItemCartProductName);
            txtProductNumber = itemView.findViewById(R.id.txtProductNumber);
            txtItemCartProductPrice = itemView.findViewById(R.id.txtItemCartProductPrice);
        }
    }

}

package com.example.mobileshop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mobileshop.Object.HomeAllProduct.HomeAllProduct;
import com.example.mobileshop.Others.ConvertMoney;
import com.example.mobileshop.Others.ItemClickListener;
import com.example.mobileshop.ProductActivity;
import com.example.mobileshop.R;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.ArrayList;

public class PaidAndRatingAdapter extends RecyclerView.Adapter<PaidAndRatingAdapter.ViewHolder> {

    private ArrayList<HomeAllProduct> alHomeAllProduct;
    private Context context;
    private ConvertMoney cvt = new ConvertMoney();

    public PaidAndRatingAdapter(ArrayList<HomeAllProduct> alHomeAllProduct, Context context) {
        this.alHomeAllProduct = alHomeAllProduct;
        this.context = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_paid_and_rating,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        float a = 0;
        //Tính toán để tạo Rating
        if(alHomeAllProduct.get(position).getCountrate()!= 0){
            a = (int) alHomeAllProduct.get(position).getTotalrate() / alHomeAllProduct.get(position).getCountrate();
        }
        holder.ratingProduct.setRating(a);

        holder.txtProductPrice.setText(cvt.StringToMoney(alHomeAllProduct.get(position).getPrice()+""));
        holder.txtProductName.setText(alHomeAllProduct.get(position).getName()+"");
        Glide.with(context).load(alHomeAllProduct.get(position).getPicMain()).into(holder.imgProduct);

        //Bấm thì chuyển qua cái sản phẩm muốn coi
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("ProductID",alHomeAllProduct.get(position).getId()+"");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alHomeAllProduct.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imgProduct;
        public TextView txtProductName,txtProductPrice;
        public ItemClickListener itemClickListener;
        public RatingBar ratingProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            ratingProduct = itemView.findViewById(R.id.ratingProduct);
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
}

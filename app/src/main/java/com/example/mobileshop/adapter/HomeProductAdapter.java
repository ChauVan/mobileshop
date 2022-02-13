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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileshop.Object.HomeAllProduct.HomeAllProduct;
import com.example.mobileshop.Others.ConvertMoney;
import com.example.mobileshop.Others.ItemClickListener;
import com.example.mobileshop.ProductActivity;
import com.example.mobileshop.R;

import java.io.InputStream;
import java.util.ArrayList;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ViewHolder> {

    private ArrayList<HomeAllProduct> alHomeAllProduct;
    private Context context;
    private ConvertMoney cvt = new ConvertMoney();

    public HomeProductAdapter(ArrayList<HomeAllProduct> alHomeAllProduct, Context context) {
        this.alHomeAllProduct = alHomeAllProduct;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sp,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        new DownloadImageFromInternet((ImageView) holder.imgItemProductMainPic).execute(String.valueOf(alHomeAllProduct.get(position).getPicMain()));
        holder.txtItemProductName.setText(String.valueOf(alHomeAllProduct.get(position).getName()));
        holder.txtItemProductProductPrice.setText(cvt.StringToMoney(alHomeAllProduct.get(position).getPrice()+""));
        float a = 0;
        //Tính toán để tạo Rating
        if(alHomeAllProduct.get(position).getCountrate()!= 0){
            a = (int) alHomeAllProduct.get(position).getTotalrate() / alHomeAllProduct.get(position).getCountrate();
        }
        holder.imgItemProductRating.setRating(a);

        holder.txtItemProductProductAmountPay.setText("Đã bán "+ alHomeAllProduct.get(position).getTotalPay());

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

        public ImageView imgItemProductMainPic;
        public TextView txtItemProductName,txtItemProductProductPrice, txtItemProductProductAmountPay;
        public ItemClickListener itemClickListener;
        public RatingBar imgItemProductRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItemProductMainPic = itemView.findViewById(R.id.imgItemProductMainPic);
            txtItemProductName = itemView.findViewById(R.id.txtItemProductName);
            txtItemProductProductPrice = itemView.findViewById(R.id.txtItemProductProductPrice);
            imgItemProductRating = itemView.findViewById(R.id.imgItemProductRating);
            txtItemProductProductAmountPay = itemView.findViewById(R.id.txtItemProductProductAmountPay);
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

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;
        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView=imageView;
        }
        protected Bitmap doInBackground(String... urls) {
            String imageURL=urls[0];
            Bitmap bimage=null;
            try {
                InputStream in=new java.net.URL(imageURL).openStream();
                bimage= BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bimage;
        }
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }





}

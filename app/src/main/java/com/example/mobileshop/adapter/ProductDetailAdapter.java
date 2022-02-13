package com.example.mobileshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileshop.Object.ProductDetail.ProductDetail;
import com.example.mobileshop.R;

import java.util.ArrayList;

public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailAdapter.ViewHolder>{

    ArrayList<ProductDetail> alProductDetail;
    Context context;

    public ProductDetailAdapter(ArrayList<ProductDetail> alProductDetail, Context context) {
        this.alProductDetail = alProductDetail;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detailproduct,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtDetailProName.setText(alProductDetail.get(position).getConfigName().toString());
        holder.txtDetailProAttribute.setText(alProductDetail.get(position).getConfigContent().toString());
    }

    @Override
    public int getItemCount() {
        return alProductDetail.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtDetailProName,txtDetailProAttribute;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDetailProName = itemView.findViewById(R.id.txtDetailProName);
            txtDetailProAttribute = itemView.findViewById(R.id.txtDetailProAttribute);
        }
    }
}

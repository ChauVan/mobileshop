package com.example.mobileshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileshop.Object.ProductDetail.PersonRating;
import com.example.mobileshop.Object.ProductDetail.Rating;
import com.example.mobileshop.R;

import java.util.ArrayList;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder>{

    private ArrayList<PersonRating> alPersonRating;
    private Context context;

    public RatingAdapter(ArrayList<PersonRating> alPersonRating, Context context) {
        this.alPersonRating = alPersonRating;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_danhgia,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ratingBar.setRating(alPersonRating.get(position).getRate());
        holder.txtProductActivityName.setText(alPersonRating.get(position).getUserName()+"");

        String s = "";
        for(int i = 0;i<alPersonRating.get(position).getProductComments().size();i++){
//            holder.txtProductActivityDate.setText(rating.getRatings().get(position).getProductComments().get(0).getDatecomment()+"");
            s += alPersonRating.get(position).getProductComments().get(i).getCommentText().toString() + "\n";
        }
        holder.textView7.setText(s);
    }

    @Override
    public int getItemCount() {
        return alPersonRating.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtProductActivityName,textView7,txtProductActivityDate;
        public RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductActivityName = itemView.findViewById(R.id.txtProductActivityName);
            textView7 = itemView.findViewById(R.id.textView7);
            ratingBar = itemView.findViewById(R.id.ratingBar);
//            txtProductActivityDate = itemView.findViewById(R.id.txtProductActivityDate);
        }
    }
}

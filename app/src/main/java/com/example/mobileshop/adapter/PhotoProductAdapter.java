package com.example.mobileshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.mobileshop.Object.Photo;
import com.example.mobileshop.R;

import java.util.List;

public class PhotoProductAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> mListPhoto;

    public PhotoProductAdapter(Context mContext, List<String> mListPhoto) {
        this.mContext = mContext;
        this.mListPhoto = mListPhoto;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_photo,container,false);
        ImageView imgPhoto = view.findViewById(R.id.img_photo);

        String photo = mListPhoto.get(position);
        if(photo != null){
//            Glide.with(mContext).load(photo.getId()).into(imgPhoto);
            Glide.with(mContext).load(photo.toString()).into(imgPhoto);
        }
        container.addView(view);
        return view;

    }

    @Override
    public int getCount() {
        if(mListPhoto != null){
            return mListPhoto.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

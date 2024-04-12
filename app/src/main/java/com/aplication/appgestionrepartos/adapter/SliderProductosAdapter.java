package com.aplication.appgestionrepartos.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.aplication.appgestionrepartos.R;
import com.aplication.appgestionrepartos.model.SliderItem;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class SliderProductosAdapter extends RecyclerView.Adapter<SliderProductosAdapter.ImageViewHolder>{


    private ViewPager2 viewPager2;
    private List<SliderItem> mSliderItems;



    public SliderProductosAdapter(List<SliderItem> mSliderItems, ViewPager2 viewPager2) {
        this.mSliderItems = mSliderItems;
        this.viewPager2 = viewPager2;

    }


    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_container, parent, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder viewHolder, int position) {

        SliderItem sliderItem = mSliderItems.get(position);



        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImagen())
                .fitCenter()
                .into(viewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return mSliderItems.size();
    }


    protected class ImageViewHolder extends RecyclerView.ViewHolder{
        RoundedImageView imageView;

        public ImageViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);


        }
    }







}

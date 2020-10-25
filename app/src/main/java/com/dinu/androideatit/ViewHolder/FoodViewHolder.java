package com.dinu.androideatit.ViewHolder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinu.androideatit.Interface.ItemClickListner;
import com.dinu.androideatit.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {



    public TextView food_name;
    public ImageView food_image;

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    private ItemClickListner itemClickListner;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        food_name=(TextView)itemView.findViewById(R.id.food_name);
        food_image=(ImageView)itemView.findViewById(R.id.food_image);

        //Log.i("food name",""+food_name);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v,getAdapterPosition(),false);

    }
}

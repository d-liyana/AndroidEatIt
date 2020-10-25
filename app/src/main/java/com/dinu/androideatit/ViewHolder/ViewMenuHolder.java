package com.dinu.androideatit.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dinu.androideatit.R;
import com.dinu.androideatit.Interface.ItemClickListner;

import androidx.recyclerview.widget.RecyclerView;

public class ViewMenuHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

public TextView txtMenuName;
public ImageView imageView;

private ItemClickListner itemClickListner;

    public ViewMenuHolder(View itemView) {
        super(itemView);
        txtMenuName=(TextView)itemView.findViewById(R.id.menu_name);
        imageView=(ImageView)itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View v) {

        itemClickListner.onClick(v,getAdapterPosition(),false);

    }
}
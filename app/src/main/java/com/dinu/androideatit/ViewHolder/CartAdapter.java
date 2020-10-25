package com.dinu.androideatit.ViewHolder;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.dinu.androideatit.Interface.ItemClickListner;
import com.dinu.androideatit.Model.OrderDetail;
import com.dinu.androideatit.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txt_cart_name,txt_price;
    public ImageView img_cart_count;

    private ItemClickListner itemClickListner;

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CardViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cart_name=(TextView)itemView.findViewById(R.id.cart_item_name);
        txt_price=(TextView)itemView.findViewById(R.id.cart_item_price);
        img_cart_count=(ImageView)itemView.findViewById(R.id.cart_item_count);
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CardViewHolder> {

    private List<OrderDetail> orderList=new ArrayList<>();
    private Context context;

    public CartAdapter(List<OrderDetail> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.cart_layout,parent,false);
        Log.i("khkshksaa","llllllllllllll");
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        Log.i("Am  here","     ");
        TextDrawable textDrawable=TextDrawable.builder().buildRound(""+orderList.get(position).getQuantity(), Color.RED);
        holder.img_cart_count.setImageDrawable(textDrawable);
        Log.i("sssssss","        ");
        Locale locale=new Locale("en","AU");
        Log.i("Locale  ","LLLLL"+locale);
        NumberFormat numberFormat=NumberFormat.getCurrencyInstance(locale);
        int price=(Integer.parseInt(orderList.get(position).getPrice()))*(Integer.parseInt(orderList.get(position).getQuantity()));
        holder.txt_price.setText(numberFormat.format(price));
        holder.txt_cart_name.setText(orderList.get(position).getProductName());

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}

package com.dinu.androideatit.ViewHolder;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dinu.androideatit.Interface.ItemClickListner;
import com.dinu.androideatit.R;

import androidx.recyclerview.widget.RecyclerView;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId,txtOrderStatus,txtOrderPhone,txtOrderAddress;
    private ItemClickListner itemClickListner;



    public OrderViewHolder( View itemView) {

        super(itemView);
        Log.i("OrderViewHolder","  "+(TextView)itemView.findViewById(R.id.order_address));
        txtOrderAddress=(TextView)itemView.findViewById(R.id.order_address);
        txtOrderId=(TextView)itemView.findViewById(R.id.order_id);
        txtOrderPhone=(TextView)itemView.findViewById(R.id.order_phone);
        txtOrderStatus=(TextView)itemView.findViewById(R.id.order_status);

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

package com.dinu.androideatit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.dinu.androideatit.Common.Common;
import com.dinu.androideatit.Model.Request;
import com.dinu.androideatit.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        //Firebase
        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        recyclerView=(RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Log.i("ViewAllItemshere","Orders  "+(RecyclerView)findViewById(R.id.listOrders));
        Log.i("PhoneNo","CurrentUser  "+Common.currentUser.getPhone());

        loadOrders(Common.currentUser.getPhone());

    }

    private void loadOrders(String phone) {

        Log.i("Loadorders"," Order Status");
        Log.i("requestPhone","Id "+phone);
        Log.i("requests"," Order Status"+requests);


        adapter=new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)

        )

        {
            @Override

            protected void populateViewHolder(OrderViewHolder orderViewHolder, Request request, int i) {

                Log.i("Populate","OrderDetails");

                orderViewHolder.txtOrderId.setText(adapter.getRef(i).getKey());
                orderViewHolder.txtOrderStatus.setText(convertCodeToStatus(request.getStatus()));
                orderViewHolder.txtOrderPhone.setText(request.getPhone());
                orderViewHolder.txtOrderAddress.setText(request.getAddress());
            }
        };
        recyclerView.setAdapter(adapter);

    }

    private String convertCodeToStatus(String status) {
        if (status.equals("0")){
            return "Placed";
        }else if (status.equals("1")){
            return "Order is on the way";

        }else

        return "Order has been Shipped";

    }
}

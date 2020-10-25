package com.dinu.androideatit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import info.hoang8f.widget.FButton;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dinu.androideatit.Common.Common;
import com.dinu.androideatit.Database.Database;
import com.dinu.androideatit.Model.OrderDetail;
import com.dinu.androideatit.Model.Request;
import com.dinu.androideatit.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    FButton btnPlace;

    List<OrderDetail> listCart=new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        //Init
        recyclerView=(RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice=(TextView)findViewById(R.id.total);
        btnPlace=(FButton)findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create new request
                showAlertDialog();
               // Request request=new Request(Common.currentUser.getPhone().);

            }
        });

        loadListFood();

    }

    private void showAlertDialog() {

        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Just one more step");
        alertDialog.setMessage("Please enter your address");

        final EditText editAddress=new EditText(Cart.this);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editAddress.setLayoutParams(lp);
        alertDialog.setView(editAddress);//Add edit text to alert box
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request=new Request(Common.currentUser.getPhone(),Common.currentUser.getName(),
                        editAddress.getText().toString(),txtTotalPrice.getText().toString(),listCart);

                //Submit to firebase
                //We will using System.CurrentMilli to key

                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                //Delete the cart item
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Thank you,Your order has been placed", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialog.show();
    }

    private void loadListFood() {
        listCart=new Database(this).getCarts();
        adapter=new CartAdapter(listCart,this);
        recyclerView.setAdapter(adapter);

        //Calculate teh total
        int total=0;
        for (OrderDetail orderDetail:listCart){

            total+=((Integer.parseInt(orderDetail.getPrice()))*(Integer.parseInt(orderDetail.getQuantity())));
            Locale locale=new Locale("en","AU");
            NumberFormat numberFormat=NumberFormat.getCurrencyInstance(locale);

            txtTotalPrice.setText(numberFormat.format(total));
        }

    }
}

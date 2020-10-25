

package com.dinu.androideatit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.dinu.androideatit.R;

import com.dinu.androideatit.Interface.ItemClickListner;
import com.dinu.androideatit.Model.Foods;
import com.dinu.androideatit.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    String categoryId ="";

    FirebaseRecyclerAdapter<Foods, FoodViewHolder> adapter;

    //Seach Functionality
    FirebaseRecyclerAdapter<Foods,FoodViewHolder> searchAdapter;
    List<String> suggestList=new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //R.layout.
            setContentView(R.layout.activity_food_list);
        }catch (Exception ex){
            Log.e("Error on Activity food list loading", ex.getMessage());
        }

        //Firebase Initializing
        database =FirebaseDatabase.getInstance();
        foodList=database.getReference("Foods");

        recyclerView=(RecyclerView)findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Get Intent here

        if(getIntent()!=null){
            categoryId=getIntent().getStringExtra("CategoryId");

            Log.i("cId "+categoryId,"CCCCIIIDDD");
            if(!categoryId.isEmpty() && categoryId !=null){
                Log.i("cId22222 "+categoryId,"CCCCIIIDDD2222");

                loadListFood(categoryId);
            }

            materialSearchBar=(MaterialSearchBar)findViewById(R.id.searchBar);
            materialSearchBar.setHint("Enter your food");
           // materialSearchBar.setSpeechMode(false);
            loadSuggest();
            materialSearchBar.setLastSuggestions(suggestList);
            materialSearchBar.setCardViewElevation(10);
            materialSearchBar.addTextChangeListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    List<String> suggest = new ArrayList<>();

                    for (String serch:suggestList)
                        if (serch.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){
                            suggest.add(serch);
                        }
                        materialSearchBar.setLastSuggestions(suggest);

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                @Override
                public void onSearchStateChanged(boolean enabled) {
                    if (!enabled)
                        recyclerView.setAdapter(adapter);

                }

                @Override
                public void onSearchConfirmed(CharSequence text) {
                    //When Search finish
                    //Show result of search
                    startSearchItem(text);
                }

                @Override
                public void onButtonClicked(int buttonCode) {

                }
            });
        }
    }

    private void startSearchItem(CharSequence text) {

        searchAdapter=new FirebaseRecyclerAdapter<Foods, FoodViewHolder>(
                Foods.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("Name").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Foods foods, int i) {
                foodViewHolder.food_name.setText(foods.getName());
                Picasso.with(getBaseContext()).load(foods.getImage()).into(foodViewHolder.food_image);

                final  Foods local = foods;
                foodViewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int postion, boolean isLongClick) {
                        // Toast.makeText(FoodList.this,""+local.getName(),Toast.LENGTH_SHORT).show();
                        //Start new activity

                        Intent foodDetail = new Intent(FoodList.this,FoodDetail.class);
                        foodDetail.putExtra("FoodId",searchAdapter.getRef(postion).getKey());//Send food id to new activity
                        startActivity(foodDetail);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggest() {

        foodList.orderByChild("MenuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Foods item=postSnapshot.getValue(Foods.class);
                    suggestList.add(item.getName()); //Add name of food suggetion
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void loadListFood(String categoryId) {

       adapter= new FirebaseRecyclerAdapter<Foods, FoodViewHolder>(Foods.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("MenuId").equalTo(categoryId))//Like : select * from Foods where MenuId=CategoryId {

        {

            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Foods foods, int i) {
                foodViewHolder.food_name.setText(foods.getName());
                Picasso.with(getBaseContext()).load(foods.getImage()).into(foodViewHolder.food_image);

                final  Foods local = foods;
                foodViewHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int postion, boolean isLongClick) {
                       // Toast.makeText(FoodList.this,""+local.getName(),Toast.LENGTH_SHORT).show();
                        //Start new activity

                        Intent foodDetail = new Intent(FoodList.this,FoodDetail.class);
                        foodDetail.putExtra("FoodId",adapter.getRef(postion).getKey());//Send food id to new activity
                        startActivity(foodDetail);
                    }
                });

            }
        };
        Log.d("TAG", "     "+adapter.getItemCount() );
        recyclerView.setAdapter(adapter);

    }


}






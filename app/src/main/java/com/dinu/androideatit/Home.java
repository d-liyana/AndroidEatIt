package com.dinu.androideatit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.dinu.androideatit.Common.Common;
import com.dinu.androideatit.Interface.ItemClickListner;
import com.dinu.androideatit.Model.Category;
import com.dinu.androideatit.ViewHolder.ViewMenuHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.ActionBarContainer;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    FirebaseDatabase database;
    DatabaseReference category;
    TextView txtFullName ;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, ViewMenuHolder> adapter;
    NavigationView navigationView;

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //Initialize the Db

        database= FirebaseDatabase.getInstance();
        category=database.getReference("Category");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cartIntent=new Intent(Home.this,Cart.class);
                startActivity(cartIntent);
            }
        });
        navigationView = (NavigationView)findViewById(R.id.nav_view);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                Log.i("ItemId","  "+id);
                switch (id){
                    case R.id.nav_home:
                        Log.i("Navigation","Home");
                        break;
                    case R.id.nav_menu:
                        Log.i("Navigation","Menu");
                        break;
                    case R.id.nav_cart:
                        Log.i("Navigation","Cart");
                        Intent cartIntent=new Intent(Home.this,Cart.class);
                        startActivity(cartIntent);
                        break;
                    case R.id.nav_orders:
                        Log.i("Navigation","Orders");
                        Intent orderIntent=new Intent(Home.this,OrderStatus.class);
                        startActivity(orderIntent);
                        break;
                    case  R.id.nav_log_out:
                        Log.i("Navigation","Log out");
                        Intent signIn=new Intent(Home.this,SignIn.class);
                        signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(signIn);
                        break;
                }
                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                item.setChecked(true);
                return false;
            }
        });
        drawer = findViewById(R.id.drawer_layout);



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
          mAppBarConfiguration = new AppBarConfiguration.Builder(
               R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
               .setDrawerLayout(drawer)
              .build();

        toggle =new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open ,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
         // NavigationUI.setupWithNavController(navigationView, navController);





        //Set name for user
        View headerView=navigationView.getHeaderView(0);

        Log.i("hgjggjjgjgjgjhgh  "+(TextView)findViewById(R.id.txtFullName),"vbvfhfgfhfhhfh");
        txtFullName=(TextView)headerView.findViewById(R.id.txtFullName);
        Log.i("hgjggjjgjgjgjhgh  "+txtFullName,"aaaaaaaaaaaaaa");

        txtFullName.setText(Common.currentUser.getName());

        //Load menu
        recycler_menu=(RecyclerView)findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();
    }

    private void loadMenu() {
        adapter = new FirebaseRecyclerAdapter<Category, ViewMenuHolder>(Category.class,R.layout.menu_item,ViewMenuHolder.class,category) {
            @Override
            protected void populateViewHolder(ViewMenuHolder viewMenuHolder, Category category, int i) {
                viewMenuHolder.txtMenuName.setText(category.getName());
                Picasso.with(getBaseContext()).load(category.getImage()).into(viewMenuHolder.imageView);
                final Category clickItem=category;
                viewMenuHolder.setItemClickListner(new ItemClickListner() {
                    @Override
                    public void onClick(View view, int postion, boolean isLongClick) {
                        //Toast.makeText(Home.this,""+clickItem.getName(),Toast.LENGTH_SHORT).show();

                        // Get categoryId and send to new activity
                        Intent foodList= new Intent(Home.this,FoodList.class);

                        //Because category id is a key and just get key of the item
                        foodList.putExtra("CategoryId",adapter.getRef(postion).getKey());
                        startActivity(foodList);
                    }
                });
            }
        };
        recycler_menu.setAdapter(adapter);
    }

    @Override
    public void  onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Toast.makeText(getApplicationContext(),"Toast : " + String.valueOf(id),Toast.LENGTH_SHORT);
        return super.onOptionsItemSelected(item);
    }


  // @Override
  // public boolean onSupportNavigateUp() {
    //    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
   //     return NavigationUI.navigateUp(navController, mAppBarConfiguration)
   //             || super.onSupportNavigateUp();
  //  }


}


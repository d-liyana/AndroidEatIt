package com.dinu.androideatit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dinu.androideatit.Common.Common;
import com.dinu.androideatit.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {

    EditText edtPhone,edtPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        //Initialize the firebase

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        Log.i("DBBBB","edtPW  "+database);

        Log.i("jgghfhffhdhfdhg","edtPW  "+table_user);


        btnSignIn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                final ProgressDialog msgDialog =  new ProgressDialog(SignIn.this);
                msgDialog.setMessage("Please wait...");
                msgDialog.show();

                Log.i("my tag111","edtPW"+table_user);

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //  Check if user doesn't exit


                       // Log.i("my tag111","edtPW"+dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class));
                        Log.i("PWWWWW","edtPW    "+dataSnapshot.child(edtPassword.getText().toString()));

                        Log.i("ERRRRRRRRRR","edtPW    "+dataSnapshot.child(edtPhone.getText().toString()).exists());

                       // Log.i("my tag","password  "+user.getPassword());

                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()){

                            Log.i("innnnnnnn","edtPW "+dataSnapshot.child(edtPassword.getText().toString()));

                        // Get user information
                        msgDialog.dismiss();
                        User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                        user.setPhone(edtPhone.getText().toString());
                            Log.i("my tag","password  "+user.getPassword());
                            Log.i("my tag2","edtPW"+edtPassword.getText().toString());


                            if (user.getPassword().equals(edtPassword.getText().toString())){
                                Log.i("my tag33333333","edtPW"+edtPassword.getText().toString());
                                //Toast.makeText(SignIn.this ,"Sign In Successfully", Toast.LENGTH_LONG).show();

                                Intent homeIntent= new Intent(SignIn.this,Home.class);
                                Common.currentUser=user;
                                startActivity(homeIntent);
                                finish();
                        }
                            else {
                                Toast.makeText(SignIn.this, "Sign In Failed, Check your Password", Toast.LENGTH_LONG).show();
                            }
                        }
                        else {
                            msgDialog.dismiss();
                            Toast.makeText(SignIn.this, "User does not exits", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
}
}

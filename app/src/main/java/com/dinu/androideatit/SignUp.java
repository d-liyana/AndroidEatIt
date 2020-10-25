package com.dinu.androideatit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dinu.androideatit.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    MaterialEditText edtPhone,edtName,edtPassword;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtName = (MaterialEditText)findViewById(R.id.edtName);
        edtPhone = (MaterialEditText)findViewById(R.id.edtPhone);
        edtPassword = (MaterialEditText)findViewById(R.id.edtPassword);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        //Initialize the firebase

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog msgDialog =  new ProgressDialog(SignUp.this);
                msgDialog.setMessage("Please wait...");
                msgDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i("DBName","DBName "+database);

                        //check if user already add
                        Log.i("am ???????","user "+dataSnapshot.child(edtPhone.getText().toString()).exists());
                        if(dataSnapshot.child(edtPhone.getText().toString()).exists()){
                            Log.i("am here1111111111111","user "+edtName.getText().toString());
                            Log.i("am here111111111111111","user "+edtPassword.getText().toString());
                        msgDialog.dismiss();
                        Toast.makeText(SignUp.this, "User already exits", Toast.LENGTH_LONG).show();
                        }else {

                            Log.i("am here","user "+edtName.getText().toString());
                            Log.i("am here","user "+edtPassword.getText().toString());

                            msgDialog.dismiss();
                            User user= new User(edtName.getText().toString(),edtPassword.getText().toString());
                            table_user.child(edtPhone.getText().toString()).setValue(user);

                            Toast.makeText(SignUp.this, "Successfully sign up", Toast.LENGTH_LONG).show();
                            finish();

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

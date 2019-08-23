package com.toxic.salonapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.toxic.salonapp.LodMaps.MapsLoad;

public class UserActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1 ;
    private Button mButton, cButton, login_reg_btn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mAuth = FirebaseAuth.getInstance();
        mButton = findViewById(R.id.btn_list_map);
        cButton = findViewById(R.id.btn_cari_map);
        login_reg_btn = findViewById(R.id.login_reg_btn);


        login_reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newPostIntent = new Intent(UserActivity.this, MainActivity.class);
                startActivity(newPostIntent);

            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newPostIntent = new Intent(UserActivity.this, ListSalonActivity.class);
                startActivity(newPostIntent);

            }
        });

        cButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent newPostIntent = new Intent(UserActivity.this, MapsLoad.class);
                startActivity(newPostIntent);

            }
        });
    }
}

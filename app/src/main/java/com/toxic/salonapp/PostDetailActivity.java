package com.toxic.salonapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = "Done";
    ImageView imgPostSpa, imgPostFac;
    TextView redLok, txtPostDesc,txtPostNo,txtPostTitle, txtPostDewasa, txtPostAnak, txtPostSpa, txtPostFac;
    String PostKey;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    private Toolbar mainToolbar;
    private Button mapButton, waButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        getSupportActionBar().setTitle("Salon App");

        imgPostSpa =findViewById(R.id.post_detail_img_spa);
        imgPostFac = findViewById(R.id.post_detail_img_fac);

        txtPostTitle = findViewById(R.id.post_detail_title);
        txtPostDewasa = findViewById(R.id.det_dewasa);
        txtPostAnak = findViewById(R.id.det_anak);
        txtPostSpa = findViewById(R.id.det_spa);
        txtPostNo = findViewById(R.id.post_detail_nomor);
        txtPostFac = findViewById(R.id.det_facial);
        txtPostDesc = findViewById(R.id.post_lok);
        redLok = findViewById(R.id.red_lok);
        mapButton = findViewById(R.id.map_btn);
        waButton = findViewById(R.id.wa_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

//        String postImage = getIntent().getExtras().getString("image_url") ;
//        Glide.with(this).load(postImage).into(imgPost);

        String postImageFac = getIntent().getExtras().getString("image_fac");
        Glide.with(this).load(postImageFac).into(imgPostFac);

        String postImageSpa = getIntent().getExtras().getString("image_spa");
        Glide.with(this).load(postImageSpa).into(imgPostSpa);

        final String postTitle = getIntent().getExtras().getString("title");
        txtPostTitle.setText(postTitle);

        final String postDewasa = getIntent().getExtras().getString("dewasa");
        txtPostDewasa.setText(postDewasa);

        final String postAnak = getIntent().getExtras().getString("anak");
        txtPostAnak.setText(postAnak);

        final String postSpa = getIntent().getExtras().getString("spa");
        txtPostSpa.setText(postSpa);

        final String postNo = getIntent().getExtras().getString("nomor_wa");
        txtPostNo.setText(postNo);

        final String postFac = getIntent().getExtras().getString("fac");
        txtPostFac.setText(postFac);

        final String postDescription = getIntent().getExtras().getString("loklok");
        txtPostDesc.setText(postDescription);

//        String date = timestampToString(getIntent().getExtras().getLong("timestamp"));
//        txtPostDateName.setText(date);

        PostKey = getIntent().getExtras().getString("postKey");
        Log.d(TAG, "onComplete: Url: "+ postTitle);

        if(txtPostDesc != null){
            redLok.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(PostDetailActivity.this, "Maaf Lokasi Tidak Terditeksi", Toast.LENGTH_LONG).show();
        }

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(postDescription));
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        waButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    String text = "Assalamualaikum Wr.Wb";

                    String noWa = txtPostNo.getText().toString();

                    String toNumber = noWa;

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+toNumber +"&text="+text));
                    startActivity(intent);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


    }

    private String timestampToString(long time) {

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("dd-MM-yyyy",calendar).toString();
        return date;

    }
}

package com.toxic.salonapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class NewPostActivity extends AppCompatActivity implements LocationListener {
    private int GPS_PERMISSION_CODE = 1;

    private Toolbar newPostToolbar;

    private ImageView newPostImage;
    private EditText newPostDesc;
    private Button newPostBtn, newPostBtnSpa, newPostBtnfac, upDefault, upSpa, upFacial;
    private ConstraintLayout halRambut, halSpa, halFacial;

    private Uri postImageUri = null;

    private ProgressBar newPostProgress;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String current_user_id;

    private Bitmap compressedImageFile;
    private CheckBox Dewasa, Anak;
    private TextView lokasiDirect, pilka;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();

        newPostToolbar = findViewById(R.id.new_post_toolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("Update Data Baru");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        halFacial = findViewById(R.id.hal_facial);
        halRambut = findViewById(R.id.hal_rambut);
        halSpa = findViewById(R.id.hal_spa);

        newPostImage = findViewById(R.id.new_post_image);
        newPostBtn = findViewById(R.id.post_btn);
        newPostBtnSpa = findViewById(R.id.post_btn_spa);
        newPostBtnfac = findViewById(R.id.post_btn_facial);
        upDefault = findViewById(R.id.up_default);
        upSpa = findViewById(R.id.up_spa);
        upFacial = findViewById(R.id.up_facial);
        newPostProgress = findViewById(R.id.new_post_progress);

        Dewasa = findViewById(R.id.cekDewasa);
        Anak = findViewById(R.id.cekAnak);

        lokasiDirect = findViewById(R.id.lokasiDirect);
        pilka = findViewById(R.id.pilka);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        upDefault.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                halFacial.setVisibility(v.INVISIBLE);
                halSpa.setVisibility(v.INVISIBLE);
                halRambut.setVisibility(v.VISIBLE);
            }
        });

        upSpa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                halFacial.setVisibility(v.INVISIBLE);
                halSpa.setVisibility(v.VISIBLE);
                halRambut.setVisibility(v.INVISIBLE);
            }
        });

        upFacial.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                halFacial.setVisibility(v.VISIBLE);
                halSpa.setVisibility(v.INVISIBLE);
                halRambut.setVisibility(v.INVISIBLE);
            }
        });


        if (ContextCompat.checkSelfPermission(NewPostActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(NewPostActivity.this, "Lokasi anda harus terditeksi oleh aplikasi!",
                    Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

            onLocationChanged(location);
        }

        newPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512, 512)
                        .setAspectRatio(1, 1)
                        .start(NewPostActivity.this);

            }
        });

        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String desc = newPostDesc.getText().toString();
                final String lokdek = lokasiDirect.getText().toString();

                if(!TextUtils.isEmpty(desc) && (!TextUtils.isEmpty(lokdek)) && postImageUri != null){

                    newPostProgress.setVisibility(View.VISIBLE);

                    final String randomName = UUID.randomUUID().toString();

                    File newImageFile = new File(postImageUri.getPath());
                    try {

                        compressedImageFile = new Compressor(NewPostActivity.this)
                                .setMaxHeight(720)
                                .setMaxWidth(720)
                                .setQuality(50)
                                .compressToBitmap(newImageFile);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageData = baos.toByteArray();

//                    UploadTask filePath = storageReference.child("post_images").child(randomName + ".jpg").putBytes(imageData);

                    Uri file = Uri.fromFile(new File(String.valueOf(postImageUri.getPath())));
                    final StorageReference riversRef = storageReference.child("post_images").child(randomName + ".jpg");
                    final UploadTask uploadTask = riversRef.putFile(file);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful()) {
                                        throw task.getException();
                                    }


                                    return riversRef.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                private static final String TAG = "Done";

                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        Uri downloadUri = task.getResult();

                                        Log.d(TAG, "onComplete: Url: "+ downloadUri.toString());
                                        Map<String, Object> postMap = new HashMap<>();
                                        postMap.put("image_url", downloadUri.toString());
                                        postMap.put("desc", desc);
                                        postMap.put("user_id", current_user_id);
                                        postMap.put("timestamp", ServerValue.TIMESTAMP);
                                        postMap.put("lokasi_direct", lokdek);

                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Salon_Post").child(current_user_id);
                                        reference.setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){

                                                    Toast.makeText(NewPostActivity.this, "Post was added", Toast.LENGTH_LONG).show();
                                                    Intent mainIntent = new Intent(NewPostActivity.this, MainActivity.class);
                                                    startActivity(mainIntent);
                                                    finish();

                                                } else {


                                                }

                                                newPostProgress.setVisibility(View.INVISIBLE);

                                            }
                                        });
                                    } else {
                                        newPostProgress.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });

                        }
                    });
                }

            }
        });


    }

    public void onCekClicked(View view) {
        // Is the button now checked?
           switch(view.getId()) {
            case R.id.cekDewasa:
                if (Anak.isChecked()){
                    Anak.setChecked(false);
                    String txtDewasa = Dewasa.getText().toString();
                    Toast.makeText(this, txtDewasa, Toast.LENGTH_SHORT).show();
                    pilka.setText(txtDewasa);
                }
            else
                    Dewasa.setChecked(true);
                break;

            case R.id.cekAnak:
                if (Dewasa.isChecked()){
                   Dewasa.setChecked(false);
                    String txtAnak = Anak.getText().toString();
                    Toast.makeText(this, txtAnak, Toast.LENGTH_SHORT).show();
                    pilka.setText(txtAnak);
                }

            else
                Anak.setChecked(true);
                break;

        }
    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("Dibutuhkan akses lokasi untuk pemilik toko!")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(NewPostActivity.this,
                                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, GPS_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, GPS_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == GPS_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                postImageUri = result.getUri();
                newPostImage.setImageURI(postImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!= null){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            lokasiDirect.setText("https://www.google.com/maps/dir/Current+Location/"+latitude+","+longitude);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}

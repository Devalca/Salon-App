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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class NewPostActivity extends AppCompatActivity implements LocationListener {
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1 ;
    private int GPS_PERMISSION_CODE = 1;

    private Toolbar newPostToolbar;

    private ImageView newPostImage, newPostImageFac;
    private EditText catH, newPostDesc, formDewasa, formAnak, formFacial, formSpa ;
    private Button newPostBtn, newPostBtnSpa, newPostBtnfac, upDefault, upSpa, upFacial;
    private ConstraintLayout halRambut, halSpa, halFacial;

    private Uri postImageUri = null;
    private Uri postImageUriFac = null;

    private ProgressBar newPostProgress;

    private StorageReference storageReference;
    private DatabaseReference reference, referenceX;
    private FirebaseAuth firebaseAuth;

    private String current_user_id;

    private Bitmap compressedImageFile;
    private TextView lokasiDirect, terditeksi, txtLong, txtLat;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        storageReference = FirebaseStorage.getInstance().getReference();
        reference = FirebaseDatabase.getInstance().getReference("Salon_Post");
        referenceX = FirebaseDatabase.getInstance().getReference("Bank_Data");
        firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();

        newPostToolbar = findViewById(R.id.new_post_toolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("Update Data Baru");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        halFacial = findViewById(R.id.hal_facial);
        halRambut = findViewById(R.id.hal_rambut);
        halSpa = findViewById(R.id.hal_spa);

        formDewasa = findViewById(R.id.hargaDewasa);
        formAnak = findViewById(R.id.hargaAnak);
        formSpa = findViewById(R.id.hargaSpa);
        formFacial = findViewById(R.id.hargaFacial);

        newPostImage = findViewById(R.id.new_post_image);
        newPostImageFac = findViewById(R.id.new_post_image_facial);
        newPostBtn = findViewById(R.id.post_btn);
        newPostBtnSpa = findViewById(R.id.post_btn_spa);
        newPostBtnfac = findViewById(R.id.post_btn_facial);
        upDefault = findViewById(R.id.up_default);
        upSpa = findViewById(R.id.up_spa);
        upFacial = findViewById(R.id.up_facial);
        newPostProgress = findViewById(R.id.new_post_progress);

        lokasiDirect = findViewById(R.id.lokasiDirect);
        terditeksi = findViewById(R.id.terditek);
        txtLat = findViewById(R.id.textLat);
        txtLong = findViewById(R.id.textLong);
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

        newPostImageFac.setOnClickListener(new View.OnClickListener() {
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
                newPostProgress.setVisibility(View.VISIBLE);
                addHarga();
            }
        });

        newPostBtnSpa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addPotoSpa();

            }
        });

        newPostBtnfac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addPotoFac();

            }
        });

    }

    private void addPotoFac() {

        if(postImageUriFac != null){

            newPostProgress.setVisibility(View.VISIBLE);

            final String randomName = UUID.randomUUID().toString();

            File newImageFile = new File(postImageUriFac.getPath());
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

            Uri file = Uri.fromFile(new File(String.valueOf(postImageUriFac.getPath())));
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
                                String id = reference.push().getKey();
                                DatabaseReference hopperRef = reference.child(current_user_id);
                                final DatabaseReference hopperRefX = referenceX.child(current_user_id).child("Gambar_Facial").child(id);
                                final Map<String, Object> postMap = new HashMap<>();
                                postMap.put("image_fac", downloadUri.toString());
                                postMap.put("user_id", current_user_id);
//                                    postMap.put("timestamp", ServerValue.TIMESTAMP);

                                hopperRef.updateChildren(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){

                                            hopperRefX.setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(NewPostActivity.this, "Update Data Selesai", Toast.LENGTH_LONG).show();
                                                    Intent mainIntent = new Intent(NewPostActivity.this, MainActivity.class);
                                                    startActivity(mainIntent);
                                                    finish();

                                                }
                                            });

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

        private  void  addPotoSpa(){

            if(postImageUri != null){

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
                                    String id = reference.push().getKey();
                                    DatabaseReference hopperRef = reference.child(current_user_id);
                                    final DatabaseReference hopperRefX = referenceX.child(current_user_id).child("Gambar_Spa").child(id);
                                    final Map<String, Object> postMap = new HashMap<>();
                                    postMap.put("image_spa", downloadUri.toString());
                                    postMap.put("user_id", current_user_id);
//                                    postMap.put("timestamp", ServerValue.TIMESTAMP);

                                    hopperRef.updateChildren(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){

                                                hopperRefX.setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(NewPostActivity.this, "Update Data Selesai", Toast.LENGTH_LONG).show();
                                                        Intent mainIntent = new Intent(NewPostActivity.this, MainActivity.class);
                                                        startActivity(mainIntent);
                                                        finish();

                                                    }
                                                });

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

      private void addHarga() {
        newPostProgress.setVisibility(View.VISIBLE);

        String anak = formAnak.getText().toString().trim();
        String dewasa = formDewasa.getText().toString().trim();
        String spa = formSpa.getText().toString().trim();
        String facial = formFacial.getText().toString().trim();
        String latitude = txtLat.getText().toString().trim();
        String longitude = txtLong.getText().toString().trim();
        double dLat = Double.parseDouble(latitude);
        double dLong = Double.parseDouble(longitude);
        final String lokdek = lokasiDirect.getText().toString();

        if (!TextUtils.isEmpty(anak) &&
            !TextUtils.isEmpty(dewasa) &&
            ! TextUtils.isEmpty(spa) &&
            ! TextUtils.isEmpty(facial) &&
            ! TextUtils.isEmpty(latitude) &&
            ! TextUtils.isEmpty(longitude) &&
            ! TextUtils.isEmpty(lokdek)){

            String id = reference.push().getKey();

            DatabaseReference hopperRefX = referenceX.child(current_user_id).child("Harga");
            DatabaseReference hopperRef = reference.child(current_user_id);

            Map<String, Object> hopperUpdates = new HashMap<>();
            hopperUpdates.put("anak", anak);
            hopperUpdates.put("dewasa", dewasa);
            hopperUpdates.put("spa", spa);
            hopperUpdates.put("facial", facial);
            hopperUpdates.put("lokasi_direct", lokdek);
            hopperUpdates.put("latitude", dLat);
            hopperUpdates.put("longitude", dLong);

            hopperRefX.setValue(hopperUpdates);
            hopperRef.updateChildren(hopperUpdates);


            Toast.makeText(NewPostActivity.this, "Update Data Selesai", Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(NewPostActivity.this, "Update Data Gagal", Toast.LENGTH_LONG).show();

        }
        Intent mainIntent = new Intent(NewPostActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
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
                postImageUriFac = result.getUri();
                newPostImage.setImageURI(postImageUri);
                newPostImageFac.setImageURI(postImageUriFac);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            lokasiDirect.setText("https://www.google.com/maps/dir/Current+Location/"+latitude+","+longitude);
            txtLat.setText(""+latitude);
            txtLong.setText(""+longitude);
            lokasiDirect.setVisibility(View.INVISIBLE);
            terditeksi.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(NewPostActivity.this, "Maaf Lokasi Tidak Terditeksi", Toast.LENGTH_LONG).show();
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

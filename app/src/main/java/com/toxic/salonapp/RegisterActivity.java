package com.toxic.salonapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText reg_email_field;
    private EditText reg_pass_field;
    private EditText reg_confirm_pass_field;
    private EditText reg_name_field;
    private EditText reg_wa_field;
    private Button reg_btn;
    private Button reg_login_btn;
    private ProgressBar reg_progress;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        reg_email_field = findViewById(R.id.reg_email);
        reg_pass_field = findViewById(R.id.reg_pass);
        reg_confirm_pass_field = findViewById(R.id.reg_confirm_pass);
        reg_name_field = findViewById(R.id.reg_name);
        reg_wa_field = findViewById(R.id.reg_wa);
        reg_btn = findViewById(R.id.reg_btn);
//      reg_login_btn = findViewById(R.id.reg_login_btn);
        reg_progress = findViewById(R.id.reg_progress);

//      reg_login_btn.setOnClickListener(new View.OnClickListener() {
//           @Override
//            public void onClick(View v) {
//              finish();
//            }
//      });

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = reg_email_field.getText().toString();
                String pass = reg_pass_field.getText().toString();
                String confirm_pass = reg_confirm_pass_field.getText().toString();
                String nama_salon = reg_name_field.getText().toString();
                String nomor_wa = reg_wa_field.getText().toString();


                if (TextUtils.isEmpty(nama_salon) ||
                        TextUtils.isEmpty(email) ||
                        TextUtils.isEmpty(nomor_wa) ||
                        TextUtils.isEmpty(pass) ||
                        TextUtils.isEmpty(confirm_pass)) {
                    Toast.makeText(RegisterActivity.this, "Silahkan Lengkapi Data!", Toast.LENGTH_SHORT).show();
                } else if (pass.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show();
                } else {
                    register(email, nama_salon, nomor_wa, pass);
                }
            }
        });
    }

    private void register(final String email, final String nama_salon, final String nomor_wa, String password) {

        reg_progress.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            final DatabaseReference referenceX = FirebaseDatabase.getInstance().getReference("Salon_Post").child(userid);


                            final HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("nama_salon", nama_salon);
                            hashMap.put("nomor_wa", "62"+nomor_wa);
//                          hashMap.put("search", nama_salon.toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        referenceX.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();

                                            }
                                        });
                                    } else {

                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(RegisterActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                    reg_progress.setVisibility(View.INVISIBLE);
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "You can't register woth this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

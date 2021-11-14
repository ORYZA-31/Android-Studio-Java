package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {

    private  EditText nama, email, password, passwordKonfirmasi;
    private Button tblLoggin, tblRegister;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nama = findViewById(R.id.nama);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordKonfirmasi = findViewById(R.id.passwordKomfirmasi);
        tblLoggin = findViewById(R.id.tblLogin);
        tblRegister = findViewById(R.id.tblRegister);

        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silakan Tunggu");
        progressDialog.setCancelable(false);



        tblLoggin.setOnClickListener(v -> {
//            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });

        tblRegister.setOnClickListener(v -> {
            if (nama.getText().length()>0 && email.getText().length()>0 && password.getText().length()>0 && passwordKonfirmasi.getText().length()>0){

                if (password.getText().toString().equals(passwordKonfirmasi.getText().toString())){

                    register(nama.getText().toString(), email.getText().toString(), password.getText().toString());

                } else {
                    Toast.makeText(getApplicationContext(), "Password dan Konfirmasi Password anda tidak cocok", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Silakan isi semua data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reload(){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }



    private void register(String name, String email, String password){

        progressDialog.show();
       mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {

               if (task.isSuccessful() && task.getResult() != null){
                   FirebaseUser firebaseUser = task.getResult().getUser();

                   if (firebaseUser != null) {
                       UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                               .setDisplayName(name)
                               .build();
                       firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {

                               reload();

                           }
                       });
                   } else {
                       Toast.makeText(getApplicationContext(), "Register gagal", Toast.LENGTH_SHORT).show();
                   }



               } else {
                   Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
               }
           }
       });


    }
}
package com.adi.projet2023.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adi.projet2023.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActivityRegisterAdmin extends AppCompatActivity {

    final String PATH_USER_DATABASE= "Users";
    private CollectionReference collectionUsers;


    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    Button btnSave;
    EditText txtNom, txtMail, txtPassword, txtTelephone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initFirebaseComponents();
        init();

        btnSave.setOnClickListener(
                v->{
                    String nom= txtNom.getText().toString();
                    String mail= txtMail.getText().toString();
                    String password= txtPassword.getText().toString();
                    String telephone= txtTelephone.getText().toString();

                    Toast.makeText(getApplicationContext(), nom, Toast.LENGTH_SHORT).show();
                }
        );
    }

    private void init(){
        btnSave= findViewById(R.id.btnSave);
        txtNom= findViewById(R.id.txtNom);
        txtMail= findViewById(R.id.txtMail);
        txtPassword= findViewById(R.id.txtPassword);
        txtTelephone= findViewById(R.id.txtTelephoneUserRegister);
    }

    private void initFirebaseComponents() {
        mAuth= FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionUsers= firebaseFirestore.collection(PATH_USER_DATABASE);
    }
}
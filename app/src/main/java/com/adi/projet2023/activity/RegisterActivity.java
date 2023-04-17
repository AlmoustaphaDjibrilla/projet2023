package com.adi.projet2023.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adi.projet2023.R;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends Activity {

    final String PATH_USER_DATABASE= "Users";

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionUsers;
    private UserModel userModel;

    TextView  txtNom, txtMail, txtPassword1, txtTelephone;
    Button btnSave;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialiser les elements
        init();

        //Create a new User
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom= txtNom.getText().toString();
                String mail= txtMail.getText().toString();
                String password= txtPassword1.getText().toString();
                String telephone= txtTelephone.getText().toString();

                createUserWithEmailAndPassword(mail, password, nom, telephone);
            }
        });

    }

    private void init(){
        txtNom= findViewById(R.id.txtNom);
        txtMail= findViewById(R.id.txtMail);
        txtPassword1= findViewById(R.id.txtPassword);
        txtTelephone = findViewById(R.id.txtTelephoneUserRegister);

        btnSave= findViewById(R.id.btnSave);

        //Initialisation Firebase Auth
        mAuth= FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        collectionUsers= firebaseFirestore.collection(PATH_USER_DATABASE);
    }


    /**
     * Ajouter un nouvel utilisateur
     * @param mail
     * @param password
     */
    private void createUserWithEmailAndPassword(String mail, String password, @Nullable String nom, @Nullable String telephone){
        if (mail==null || mail.equals("") || password==null || password.equals("")){
            Toast.makeText(RegisterActivity.this, "Veuillez saisir tous les champs", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.createUserWithEmailAndPassword(mail, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(RegisterActivity.this, "User crée avec succès", Toast.LENGTH_SHORT).show();

                            FirebaseUser firebaseUser= mAuth.getCurrentUser();
                            UserModel userModel= new UserModel(firebaseUser.getUid(), telephone, mail, nom, password);
                            setUserModel(userModel);
                            ajoutUserDataBase(firebaseUser, userModel);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Echec d'ajout!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    /**
     * En plus de l'enregistrement
     * ajouter le nouvel user
     * dans une base de données
     * pour faciliter les traffics
     * @param firebaseUser
     * @param userModel
     */
    private void ajoutUserDataBase(FirebaseUser firebaseUser, UserModel userModel){
        DocumentReference documentReference=
                collectionUsers
                        .document(firebaseUser.getUid());
        userModel.setUid(firebaseUser.getUid());
        documentReference.set(userModel);
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
package com.adi.projet2023.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class ActivityRegisterUser extends AppCompatActivity {

    final String PATH_USER_DATABASE= "Users";
    private CollectionReference collectionUsers;

    ImageView imgQuitterRegister;


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
                v-> {
                    String nom = txtNom.getText().toString();
                    String mail = txtMail.getText().toString();
                    String password = txtPassword.getText().toString();
                    String telephone = txtTelephone.getText().toString();

                    ajouterUserModel(mail, password, nom, telephone);

                }
        );

        imgQuitterRegister.setOnClickListener(
                view -> {
                    finish();
                }
        );
    }


    private void init(){
        btnSave= findViewById(R.id.btnSave);
        txtNom= findViewById(R.id.txtNom);
        txtMail= findViewById(R.id.txtMail);
        txtPassword= findViewById(R.id.txtPassword);
        txtTelephone= findViewById(R.id.txtTelephoneUserRegister);
        imgQuitterRegister= findViewById(R.id.imgQuitterRegister);
    }

    private void initFirebaseComponents() {
        mAuth= FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionUsers= firebaseFirestore.collection(PATH_USER_DATABASE);
    }

    /**
     * Pour ajouter un compte user UserModel
     * @param mail representant le mail de l'user
     * @param password representant le mot de passe de l'user
     * @param nom le nom de l'user
     * @param telephone son num de tel
     */
    private void ajouterUserModel(@NonNull String mail, @NonNull String password, @Nullable String nom, @Nullable String telephone){
        if (mail==null || mail.equals("")){
            txtMail.setError("Veuillez saisir le mail");
            return;
        }
        if (password== null || password.equals("")){
            txtPassword.setError("Veuillez saisir le password");
        }

        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        firebaseUser= mAuth.getCurrentUser();
                        UserModel userModel= new UserModel(mail, password, nom, telephone);
                        userModel.setUid(firebaseUser.getUid());
                        userModel.setAdmin(false);
                        userModel.setUser(true);
                        addUserModelInDatabase(userModel);
                        Toast.makeText(getApplicationContext(), userModel.getEmail()+" ajouté avec succès", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Désolé...Enregistrement échoué", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Ajouter Admin dans la base de données
     * @param userModel
     */
    private void addUserModelInDatabase(UserModel userModel){
        DocumentReference documentReference=
                collectionUsers
                        .document(userModel.getUid());
        documentReference.set(userModel);
    }
}
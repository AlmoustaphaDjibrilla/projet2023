package com.adi.projet2023.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.adi.projet2023.R;
import com.adi.projet2023.activity.main_page.MainPage;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

public class ActivityRegisterUser extends AppCompatActivity {

    final String PATH_USER_DATABASE= "Users";
    String adminEmail, adminPassword;
    private CollectionReference collectionUsers;

    ImageView imgQuitterRegister;


    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    ProgressBar progressBarRegister;
    int counter=0;

    Button btnSave;
    EditText txtNom, txtMail, txtPassword, txtTelephone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Récupérer l'adresse e-mail stockée dans les préférences partagées
        SharedPreferences prefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        adminEmail = prefs.getString("adminEmail", "");
        adminPassword = prefs.getString("adminPassword", "");

        initFirebaseComponents();
        init();

        btnSave.setOnClickListener(
                v-> {
                    String newNom = txtNom.getText().toString();
                    String newMail = txtMail.getText().toString();
                    String newPassword = txtPassword.getText().toString();
                    String newTelephone = txtTelephone.getText().toString();
                    ajouterUserModel(newMail, newPassword, newNom, newTelephone);
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
        progressBarRegister= findViewById(R.id.progressBarRegister);
        progressBarRegister.setVisibility(View.INVISIBLE);
    }

    private void initFirebaseComponents() {
        mAuth= FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionUsers= firebaseFirestore.collection(PATH_USER_DATABASE);
    }

    /**
     * Pour ajouter un compte user UserModel
     * @param newUserMail representant le mail de l'user
     * @param newUserPassword representant le mot de passe de l'user
     * @param newUserNom le nom de l'user
     * @param newUserTelephone son num de tel
     */
    private void ajouterUserModel(@NonNull String newUserMail, @NonNull String newUserPassword, @Nullable String newUserNom, @Nullable String newUserTelephone) {
        UserModel userModel = new UserModel(newUserMail, newUserPassword, newUserNom, newUserTelephone);
        if (newUserMail==null || newUserMail.equals("")) {
            txtMail.setError("Veuillez saisir le mail");
            return;
        }
        if (newUserPassword== null || newUserPassword.equals("")) {
            txtPassword.setError("Veuillez saisir le password");
        }
        else{
            progressBarRegister.setVisibility(View.VISIBLE);
            Timer timer = new Timer();
            TimerTask timerTask= new TimerTask() {
                @Override
                public void run() {
                    counter++;
                    progressBarRegister.setProgress(counter);
                    if (counter==100){
                        timer.cancel();
                    }
                }
            };
            timer.schedule(timerTask, 100, 100);

            mAuth.createUserWithEmailAndPassword(newUserMail, newUserPassword)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            firebaseUser= authResult.getUser();
                            userModel.setUid(firebaseUser.getUid());
                            userModel.setAdmin(false);
                            userModel.setUser(true);
                            addUserModelInDatabase(userModel);
                            mAuth.signInWithEmailAndPassword(adminEmail, adminPassword)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            // L'administrateur reconnecte avec succes
                                            Toast.makeText(getApplicationContext(), userModel.getNom()+" ajouté avec succès", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Désolé...Enregistrement échoué", Toast.LENGTH_SHORT).show();
                            progressBarRegister.setVisibility(View.INVISIBLE);
                        }
                    });
        }
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
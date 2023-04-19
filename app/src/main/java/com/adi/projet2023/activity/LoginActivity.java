package com.adi.projet2023.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;

import com.adi.projet2023.R;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends Activity implements Serializable {

    final String PATH_USER_DATABASE= "Users";
    private CollectionReference collectionUsers;


    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    ProgressBar progressBarLogin;
    int counter=0;


    TextView txtPasswordOublie;
    EditText txtMailLogin, txtPasswordLogin;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);
        initFirebaseComponents();
        init();

        collectionUsers
                .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                ArrayList<UserModel> lesUsers= new ArrayList<>();
                                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                    lesUsers.add(documentSnapshot.toObject(UserModel.class));
                                }
                                if (lesUsers.size()==0){

                                    // S'il n'y a aucun User, ajouter un premier, qui sera l'admin
                                    startActivity(new Intent(getApplicationContext(), ActivityRegisterAdmin.class));
                                    finish();
                                    Toast.makeText(getApplicationContext(), "Ajouter un compte Administrateur", Toast.LENGTH_LONG).show();
                                }
                            }
                        });



        /**
         * Click sur le button login pour se connecter
         */
        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String mail= txtMailLogin.getText().toString();
                        String password= txtPasswordLogin.getText().toString();

                        loginMethod(mail, password);
                    }
                }
        );

        /**
         * Click sur le lien "mot de passe oublié"
         * pour recevoir un lien de réinitialisation de mot de passe
         */
        txtPasswordOublie.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mail= txtMailLogin.getText().toString();
                        resetPasswordMethod(mail);
                    }
                }
        );
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    /**
     * Initialisation des différents composants de l'interface graphique
     */
    private void init(){
        btnLogin= findViewById(R.id.btnLoginAdmin);
        txtPasswordOublie= findViewById(R.id.txtPasswordOublieAdmin);

        txtMailLogin=findViewById(R.id.txtMailLoginAdmin);
        txtPasswordLogin= findViewById(R.id.txtPasswordLoginAdmin);

        progressBarLogin= findViewById(R.id.progressBarLogin);
        progressBarLogin.setVisibility(View.INVISIBLE);
    }

    private void initFirebaseComponents() {
        mAuth= FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionUsers= firebaseFirestore.collection(PATH_USER_DATABASE);
    }


    /**
     * Cette méthode permet à
     * un quelconque utilisateur
     * de se connecter au système
     * grâce à son mail et password
     * @param mail
     * @param password
     */
    private void loginMethod(String mail, String password){

        if (mail==null || mail.equals("")){
            txtMailLogin.setError("Veuillez saisir votre email");
            return;
        }
        if (password==null || password.equals("")){
            txtPasswordLogin.setError("Veuillez saisir votre mot de passe");
            return;
        }


        if (mail==null || password==null || mail.equals("") || password.equals("")){
            Toast.makeText(getApplicationContext(), "Veuillez saisir tous les champs", Toast.LENGTH_SHORT).show();
        }
        else {
            progressBarLogin.setVisibility(View.VISIBLE);
            Timer timer= new Timer();
            TimerTask timerTask= new TimerTask() {
                @Override
                public void run() {

                    counter++;

                    progressBarLogin.setProgress(counter);

                    if (counter==100){
                        timer.cancel();

//                        loginMethod(mail, password);
                    }

                }
            };
            timer.schedule(timerTask, 100, 100);
            mAuth.signInWithEmailAndPassword(mail, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            firebaseUser= mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), ChoixLocalActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Données non correspondants", Toast.LENGTH_SHORT).show();
                            progressBarLogin.setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }

    /**
     * envoyer un mail pour pouvoir réinitialiser le mot de passe
     * du user ayant le mail spécifié
     * @param mail represente le mail de celui
     *             qui voudrait réinitialiser son mot de passe
     */
    private void resetPasswordMethod(String mail){
        if (mail==null || mail.equals("")){
            txtMailLogin.setError("Veuillez saisir votre mail");
        }
        else {
            mAuth.sendPasswordResetEmail(mail)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Consultez votre messagerie électronique", Toast.LENGTH_SHORT).show();
                            Intent mailClient = new Intent(Intent.ACTION_VIEW);
                            mailClient.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivityGmail");
                            if (mailClient !=null){
                                startActivity(mailClient);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Echec d'envoi de mail", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


}
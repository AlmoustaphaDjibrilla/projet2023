package com.adi.projet2023.activity.main_page;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adi.projet2023.R;
import com.adi.projet2023.Utils.LocalUtils;
import com.adi.projet2023.activity.ActivityRegisterUser;
import com.adi.projet2023.activity.ChoixLocalActivity;
import com.adi.projet2023.activity.main_page.fragment.FragmentUsers;
import com.adi.projet2023.creation.CreationLocal;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChercherUser extends AppCompatActivity {

    final String PATH_USER_DATABASE= "Users";

    CollectionReference collectionReference;

    Local localEnCours;

    ImageView imgQuitter;

    EditText txtMailSearchUser, txtNomSearchUser, txtDateEnregistrementSearchUser;
    Button btnSearchUser, btnAddUser;
    Dialog dialog;



    TextView txtNomModelUser, txtMailModelUser, txtDateEnregistrementUser, txtUidUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche_user);

        localEnCours= (Local) getIntent().getSerializableExtra("localEnCours");

        dialog= new Dialog(this);

        init();

        imgQuitter.setOnClickListener(
                v->finish()
        );


        txtNomModelUser= dialog.findViewById(R.id.nomUserSupprime);
        txtMailModelUser= dialog.findViewById(R.id.mailUserSupprime);
        txtDateEnregistrementUser= dialog.findViewById(R.id.dateEnregistrementUserSupprime);

        btnSearchUser.setOnClickListener(
                v->{
                    String mail= txtMailSearchUser.getText().toString();
                    String nom= txtNomSearchUser.getText().toString();
                    String dateEnregistrement= txtDateEnregistrementSearchUser.getText().toString();

                    collectionReference=
                            FirebaseFirestore.getInstance()
                                    .collection(PATH_USER_DATABASE);

                    if (mail!= null && !mail.equals("")){
                        chercherDonnes("email", mail);
                    } else if (nom!=null && !nom.equals("")) {
                        chercherDonnes("nom", nom);
                    }
                    else if(dateEnregistrement!=null && !dateEnregistrement.equals("")){
                        chercherDonnes("dateEnregistrement", dateEnregistrement);
                    }

                }
        );

    }

    /**
     * Premiere initilisation des
     * composants de l'actvite courante
     */
    private void init(){
        txtMailSearchUser= findViewById(R.id.txtMailSearchUser);
        txtNomSearchUser= findViewById(R.id.txtNomSearchUser);
        txtDateEnregistrementSearchUser= findViewById(R.id.txtDateEnregisrementSearchUser);
        btnSearchUser= findViewById(R.id.btnSearchUser);
        imgQuitter= findViewById(R.id.imgQuitterRechercheUser);
    }

    /**
     * Initialiser les composants du dialog qui va
     * afficher les users trouvers
     */
    private void composantsDialog(){
        txtNomModelUser= dialog.findViewById(R.id.nomUserSupprime);
        txtMailModelUser= dialog.findViewById(R.id.mailUserSupprime);
        txtDateEnregistrementUser= dialog.findViewById(R.id.dateEnregistrementUserSupprime);
        txtUidUser= dialog.findViewById(R.id.uidUserSupprime);

        btnAddUser= dialog.findViewById(R.id.btnAddUser);
    }

    /***
     * Remplissage des composants graphiques
     * du Dialog avec les infos de l'utilisateur recherché
     * @param userModel, est un UserModel recherche
     *                   avec des parametres quelconques
     */
    private void remplirChamps(UserModel userModel){
        if (userModel!=null){
            if (userModel.getNom()!=null){
                txtNomModelUser.setText(userModel.getNom());
            }
            if (userModel.getEmail()!=null){
                txtMailModelUser.setText(userModel.getEmail());
            }
            if (userModel.getDateEnregistrement()!=null){
                txtDateEnregistrementUser.setText(userModel.getDateEnregistrement());
            }
            if (userModel.getUid()!=null){
                txtUidUser.setText(userModel.getUid());
            }
        }
    }

    /**
     * Cette fonction permet de rechercher un UserModel dans
     * la base de données via différents types de parametre
     * afin de ne pas sous-traiter la recherche d'un UserModel
     * uniquement à un type de field
     * @param nomField , qui est le nom du parametre
     *                 qui est un quelconque element
     *                 des attributs du UserModel
     *                 enregistré dans la Base de données
     * @param value, qui est la valeur dont on veut faire le
     *               mappage avec le field ou attribut du
     *               UserModel qu'on recherche dans la BD
     */
    private void chercherDonnes(String nomField, String value){

        collectionReference
                .whereEqualTo(nomField, value)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<UserModel> lesUsersRecherche= new ArrayList<>();

                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots){
                            UserModel userModel= doc.toObject(UserModel.class);
                            lesUsersRecherche.add(userModel);
                        }

                        if (lesUsersRecherche.size()>0) {
                            UserModel userModel = queryDocumentSnapshots.getDocuments().get(0).toObject(UserModel.class);
                            dialog.setContentView(R.layout.modele_user_recherche);
                            composantsDialog();
                            remplirChamps(userModel);

                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();

                            btnAddUser.setOnClickListener(
                                    v->{
                                        Toast.makeText(ChercherUser.this, userModel.getNom()+" ajouté", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        ajouterUserLocal(userModel);
                                    }
                            );
                        }
                        else{
                            Toast.makeText(ChercherUser.this, " L'utilisateur n'existe pas", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Erreur...", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void ajouterUserLocal(UserModel userAAjouter){

        Map<String, Object> newUser= new HashMap<>();
        newUser.put("uid", userAAjouter.getUid());
        newUser.put("password", userAAjouter.getPassword());
        newUser.put("email", userAAjouter.getEmail());
        newUser.put("nom", userAAjouter.getNom());
        newUser.put("dateEnregistrement", userAAjouter.getDateEnregistrement());
        newUser.put("admin", userAAjouter.isAdmin());
        newUser.put("user", userAAjouter.isUser());

        CollectionReference localRef = FirebaseFirestore.getInstance().collection("Local");
        Query req = localRef.whereEqualTo("idLocal", localEnCours.getIdLocal());
        req.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                DocumentSnapshot localDoc = querySnapshot.getDocuments().get(0);
                List<Map<String, Object>> lesUsers = (List<Map<String, Object>>) localDoc.get("lesUsers");
                if (lesUsers == null) {
                    lesUsers = new ArrayList<>();
                }
                lesUsers.add(newUser);
                localDoc.getReference().update("lesUsers",lesUsers)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Intent intent = new Intent(getApplicationContext(), ChoixLocalActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG",e.getMessage());
            }
        });

    }
}
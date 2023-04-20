package com.adi.projet2023.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.projet2023.R;
import com.adi.projet2023.activity.main_page.MainPage;
import com.adi.projet2023.activity.main_page.fragment.FragmentHome;
import com.adi.projet2023.adapter.AdapterChoixLocal;
import com.adi.projet2023.adapter.AdapterLocal;
import com.adi.projet2023.creation.CreationLocal;
import com.adi.projet2023.databinding.ActivityChoixLocalBinding;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.local.AutreLocal;
import com.adi.projet2023.model.local.Entreprise;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.local.Maison;
import com.adi.projet2023.model.local.TypeLocal;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChoixLocalActivity extends AppCompatActivity {

    final String PATH_LOCAL_DATABASES= "Local";
    final String PATH_USERS_DATABASE= "Users";

    private CollectionReference collectionLocal;
    private CollectionReference collectionUsers;

    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    UserModel userModel;


    private AppBarConfiguration appBarConfiguration;
    private ActivityChoixLocalBinding binding;
    RecyclerView listLocaux;
//    ArrayList<Local> lesLocaux;
    Dialog dialog;

    /**
     * Les composants du dialog qui va s'afficher
     * pour ajouter un nouveau local
     */
    RadioButton rdMaison, rdEntreprise, rdAutre;
    EditText txtNomLocal, txtQuartierLocal, txtVilleLocal;
    Button btnAjouterLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_local);
        dialog= new Dialog(this);

        binding = ActivityChoixLocalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initFirebaseComponents();
        init();
//        initComponentsOfDialog();

        binding.addlocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajoutLocal();
                verifierAdmin(mAuth.getCurrentUser());
                btnAjouterLocal.setOnClickListener(
                        v->{
                            TypeLocal typeLocal;
                            String nomLocal= txtNomLocal.getText().toString();
                            String quartierLocal= txtQuartierLocal.getText().toString();
                            String villeLocal= txtVilleLocal.getText().toString();

                            if (rdMaison.isChecked())
                                typeLocal= TypeLocal.MAISON;
                            else if (rdEntreprise.isChecked())
                                typeLocal= TypeLocal.ENTREPRISE;
                            else
                                typeLocal= TypeLocal.AUTRE;

                            nouveauLocal(typeLocal, nomLocal, quartierLocal, villeLocal);
                        }
                );
            }
        });

        collectionLocal.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                ArrayList<Local> lesLocaux= new ArrayList<>();
                                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){

                                    //Récuperer manuellement tous les attributs du local
                                    String designationLocal= documentSnapshot.getString("designationLocal");
                                    String adresseLocal= documentSnapshot.getString("adresseLocal");
                                    String idLocal= documentSnapshot.getString("idLocal");
                                    List<Piece> lesPieces= (List<Piece>) documentSnapshot.get("lesPieces");
                                    List<UserModel> lesUsers= (List<UserModel>) documentSnapshot.get("lesUsers");
                                    String nomLocal= documentSnapshot.getString("nomLocal");
                                    String quartierLocal= documentSnapshot.getString("quartierLocal");
                                    TypeLocal typeLocal= TypeLocal.valueOf(documentSnapshot.getString("typeLocal"));
                                    String villeLocal= documentSnapshot.getString("villeLocal");

                                    //creer un nouvel local et lui affecter les attributs recuperés ci-dessus
                                    Local local= new Local();
                                    local.setDesignationLocal(designationLocal);
                                    local.setAdresseLocal(adresseLocal);
                                    local.setIdLocal(idLocal);
                                    local.setLesPieces(lesPieces);
                                    local.setLesUsers(lesUsers);
                                    local.setNomLocal(nomLocal);
                                    local.setQuartierLocal(quartierLocal);
                                    local.setTypeLocal(typeLocal);
                                    local.setVilleLocal(villeLocal);

                                    //Ajouter le local créé à la liste des locaux
                                    lesLocaux.add(local);
                                }

                                //afficher la liste des locaux dans la liste
                                AdapterLocal adapterLocal= new AdapterLocal(getApplicationContext(), lesLocaux);
                                adapterLocal.setOnItemClickListener(new AdapterLocal.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(String localId) {
                                         Intent intent = new Intent(getApplicationContext(), MainPage.class);
                                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                         intent.putExtra("localId", localId);
                                         getApplicationContext().startActivity(intent);
                                    }
                                });
                                listLocaux.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                listLocaux.setAdapter(adapterLocal);
                            }
                        });
    }

    /**
     * Initialisation des composants
     */
    private void init(){
        listLocaux= findViewById(R.id.listChoixLocal);
        //lesLocaux= new ArrayList<>();
    }

    private void initFirebaseComponents(){
        mAuth= FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        collectionLocal= firebaseFirestore.collection(PATH_LOCAL_DATABASES);
        collectionUsers= firebaseFirestore.collection(PATH_USERS_DATABASE);
    }

    private void initComponentsOfDialog(){
        rdMaison= dialog.findViewById(R.id.rdBtnMaison);
        rdMaison.setChecked(true);
        rdEntreprise= dialog.findViewById(R.id.rdBtnEntreprise);
        rdAutre= dialog.findViewById(R.id.rdBtnAutre);

        txtNomLocal= dialog.findViewById(R.id.txtNomAjoutLocal);
        txtQuartierLocal= dialog.findViewById(R.id.txtQuartierAjoutLocal);
        txtVilleLocal= dialog.findViewById(R.id.txtVilleAjoutLocal);

        btnAjouterLocal= dialog.findViewById(R.id.btnEnregistrerLocal);
    }

    /**
     * Affichage du dialog
     * pour ajouter un nouveau local
     */
    void ajoutLocal(){
        dialog.setContentView(R.layout.layout_ajout_local);

        initComponentsOfDialog();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();
    }

    /**
     * Ajout du local dans la base de données
     * @param local represente le local à ajouter dans la BD
     *              pour rappel, le local peut être
     *              une MAISON, une ENTREPRISE, ou AUTRE
     */
    private void ajouterNouveauLocal(Local local){
        DocumentReference documentReference=
                collectionLocal
                        .document(local.getIdLocal());
        documentReference.set(local)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "New "+local.getDesignationLocal()+" added succesfully!!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
                    }
                });
        dialog.dismiss();
    }

    /**
     * Méthode pour vérifier si le user courant
     * est un admin afin de lui permettre
     * d'ajouter un nouveau Local
     * @param firebaseUser
     */
    private void verifierAdmin(FirebaseUser firebaseUser){
        DocumentReference docUsermodel=
                collectionUsers
                        .document(firebaseUser.getUid());

        docUsermodel.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getBoolean("admin")){
//                            ajoutLocal();
                            dialog.show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Désolé vous n'êtes pas administrateur", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Aucune réponse", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void nouveauLocal(TypeLocal typeLocal, String nomLocal, String quartierLocal, String villeLocal){
        if(nomLocal==null || nomLocal.equals("")){
            txtNomLocal.setError("Saisissez le nom du local");
            return;
        }
        if(quartierLocal==null || quartierLocal.equals("")){
            txtQuartierLocal.setError("Saisissez le quartier du local");
            return;
        }
        if(villeLocal==null || villeLocal.equals("")){
            txtVilleLocal.setError("Saisissez la ville du local");
            return;
        }

        switch (typeLocal){
            case MAISON:
                CreationLocal.creationMaison(nomLocal, quartierLocal, villeLocal);
                Toast.makeText(getApplicationContext(), nomLocal+" added successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;

            case ENTREPRISE:
                CreationLocal.creationEntreprise(nomLocal, quartierLocal, villeLocal);
                Toast.makeText(getApplicationContext(), nomLocal+" added successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;

            case AUTRE:
                CreationLocal.creationAutreLocal(nomLocal, quartierLocal, villeLocal);
                Toast.makeText(getApplicationContext(), nomLocal+" added successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;

            default:
                Toast.makeText(getApplicationContext(), "Error...", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                break;
        }
    }
}
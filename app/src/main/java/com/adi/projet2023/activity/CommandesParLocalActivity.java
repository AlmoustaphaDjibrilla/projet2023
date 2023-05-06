package com.adi.projet2023.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.adi.projet2023.R;
import com.adi.projet2023.adapter.AdapterListCommandes;
import com.adi.projet2023.model.Commande.Commande;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CommandesParLocalActivity extends AppCompatActivity {
    final String PATH_COMMANDE= "Commandes";
    ListView listCommandes;
    ArrayList<Commande> lesCommandes;

    ImageView imgQuitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commandes_user);
        init();
        String idLocal = getIntent().getStringExtra("IdLocal");

        CollectionReference collectionReference=
                FirebaseFirestore.getInstance()
                        .collection(PATH_COMMANDE);

        collectionReference.whereEqualTo("local",idLocal)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        ArrayList<Commande> resultatCommandes= new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            resultatCommandes.add(documentSnapshot.toObject(Commande.class));
                        }
                        AdapterListCommandes adapterListCommandes= new AdapterListCommandes(getApplicationContext(), resultatCommandes);
                        listCommandes.setAdapter(adapterListCommandes);
                    }
                });

        imgQuitter.setOnClickListener(
                v->finish()
        );

    }

    /**
     * Initialisation des composants permettant
     * d'afficher les commandes qu'un UserModel a exécutées
     */
    private void init(){
        listCommandes= findViewById(R.id.listCommandes);
        lesCommandes= new ArrayList<>();
        imgQuitter= findViewById(R.id.imgQuitter);
    }
}
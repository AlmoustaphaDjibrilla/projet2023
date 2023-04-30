package com.adi.projet2023.activity.main_page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.adi.projet2023.R;
import com.adi.projet2023.adapter.AdapterListCommandes;
import com.adi.projet2023.model.Commande.Commande;
import com.adi.projet2023.model.user.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AffichageCommandeUser extends AppCompatActivity {
    UserModel userModel;

    final String PATH_COMMANDE= "Commandes";
    ListView listCommandes;
    ArrayList<Commande> lesCommandes;

    TextView txtMailUser1Commande, txtTitreUserCommande;
    ImageView imgQuitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commandes_user);
        init();

        userModel= (UserModel) getIntent().getSerializableExtra("userEnCours");

        String mail="";
        if (userModel!=null)
            mail= userModel.getEmail();

        txtMailUser1Commande.setText("User selected: \n"+mail);

        CollectionReference collectionReference=
                FirebaseFirestore.getInstance()
                        .collection(PATH_COMMANDE);

        //Faire le mappage avec le mail du UserModel courant
        //avec le emailUser inscrit dans toutes les commandes
        collectionReference.whereEqualTo("emailUser", mail)
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


    private void init(){
        listCommandes= findViewById(R.id.listCommandes);
        lesCommandes= new ArrayList<>();
        txtMailUser1Commande= findViewById(R.id.textAdmin);
        imgQuitter= findViewById(R.id.imgQuitter);
        txtTitreUserCommande= findViewById(R.id.txtTitreUserCommande);
        txtTitreUserCommande.setText("Historique");
    }

}
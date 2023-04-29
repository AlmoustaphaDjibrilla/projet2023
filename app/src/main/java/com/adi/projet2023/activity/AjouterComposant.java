package com.adi.projet2023.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.adi.projet2023.R;
import com.adi.projet2023.Utils.LocalUtils;
import com.adi.projet2023.activity.main_page.MainPage;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.composant.Composant;
import com.adi.projet2023.model.local.Local;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AjouterComposant extends AppCompatActivity {

    EditText nomComposantEdit;
    private String typeComposant;
    private Button addComposant;
    Local localEnCours;
    Piece pieceEnCours;
    AutoCompleteTextView autoCompleteTextView;

    ImageView imgQuitterAddComposant;
    @Override
    protected void onResume() {
        super.onResume();
        init();
        if(pieceEnCours.getTypePiece().equals("JARDIN")){
            String []type = getResources().getStringArray(R.array.type_composant_jardin);
            ArrayAdapter<String> adapter= new ArrayAdapter<>(this,R.layout.type_composants, type);
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    typeComposant = adapterView.getItemAtPosition(i).toString().toUpperCase();
                }
            });
        }
        else if (pieceEnCours.getTypePiece().equals("DOUCHE")) {

            String []type = getResources().getStringArray(R.array.type_composant_douche);
            ArrayAdapter<String> adapter= new ArrayAdapter<>(this,R.layout.type_composants, type);
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    typeComposant = adapterView.getItemAtPosition(i).toString().toUpperCase();
                }
            });
        }
        else if (pieceEnCours.getTypePiece().equals("CUISINE")) {

            String []type = getResources().getStringArray(R.array.type_composant_cuisine);
            ArrayAdapter<String> adapter= new ArrayAdapter<>(this,R.layout.type_composants, type);
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    typeComposant = adapterView.getItemAtPosition(i).toString().toUpperCase();
                }
            });
        }
        else {
            String []type = getResources().getStringArray(R.array.type_composant);
            ArrayAdapter<String> adapter= new ArrayAdapter<>(this,R.layout.type_composants, type);
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    typeComposant = adapterView.getItemAtPosition(i).toString().toUpperCase();
                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_composant);
        init();
        pieceEnCours = (Piece) getIntent().getSerializableExtra("pieceEnCours");
        localEnCours = (Local) getIntent().getSerializableExtra("localEnCours");
        addComposant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verifier_champ()){
                    ajouterComposant();
                    finish();
                }
            }
        });

        imgQuitterAddComposant.setOnClickListener(
                view -> finish()
        );

    }

    private void init(){
        autoCompleteTextView=findViewById(R.id.txtTypeAjoutComposant);
        addComposant=findViewById(R.id.btnAjouterComposant);
        nomComposantEdit=findViewById(R.id.txtNomAjoutComposant);

        imgQuitterAddComposant= findViewById(R.id.imgQuitterAddComposant);
    }

    private boolean verifier_champ(){
        String nomComposant = nomComposantEdit.getText().toString().trim();
        String typeComposant = autoCompleteTextView.getText().toString();

        if(nomComposant==null || nomComposant.equals("")){
            nomComposantEdit.setError("Saisissez un nom pour le composant");
            return false;
        }
        if(typeComposant==null || typeComposant.equals("")){
            autoCompleteTextView.setError("Choisissez le type du composant");
            return false;
        }
        return true;
    }

    private void ajouterComposant(){
        String nomComposant = nomComposantEdit.getText().toString().trim();
        typeComposant = autoCompleteTextView.getText().toString();
        String c ="/";
        String chemin = pieceEnCours.getAdressePiece()+c+nomComposant.toLowerCase();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Créer un objet Map pour représenter les données du nouveau composant
        Map<String, Object> nouveauComposant = new HashMap<>();
        nouveauComposant.put("idComposant",nomComposant.replaceAll("\\s", "").toLowerCase());
        nouveauComposant.put("nom", nomComposant);
        nouveauComposant.put("typeComposant", typeComposant);
        nouveauComposant.put("chemin",chemin);

        CollectionReference localref = db.collection("Local");
        Query requete = localref.whereEqualTo("idLocal",localEnCours.getIdLocal());

        requete.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                DocumentSnapshot localDoc = querySnapshot.getDocuments().get(0);
                List<Map<String, Object>> lesPieces = (List<Map<String, Object>>) localDoc.get("lesPieces");

                Map<String, Object> pieceActu= null;
                for (Map<String, Object> piece : lesPieces) {
                    String pieceId = (String) piece.get("idPiece");
                    if (pieceId != null && pieceId.equals(pieceEnCours.getIdPiece())) {
                        pieceActu = piece;
                        break;
                    }
                }
                List<Map<String, Object>> composants = (List<Map<String, Object>>) pieceActu.get("composants");
                composants.add(nouveauComposant);

                Map<String, Object> data = new HashMap<>();
                data.put("lesPieces", lesPieces);
                localref.document(localDoc.getId()).update(data).addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Nouveau composant ajouté avec succès", Toast.LENGTH_SHORT).show();
                        ajouter_a_realTime((String) nouveauComposant.get("typeComposant"), (String) nouveauComposant.get("chemin"));
                        LocalUtils.getLocalById(localEnCours.getIdLocal(), new OnSuccessListener<Local>() {
                            @Override
                            public void onSuccess(Local local) {
                                // Aller vers Main Page avec local mis a jour
                                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("localId",local);
                                startActivity(intent);
                            }
                        }, new OnFailureListener() {
                            @Override
                            public void onFailure(@org.checkerframework.checker.nullness.qual.NonNull Exception e) {
                                // Erreur lors de la recuperation du local mis a jour
                                Toast.makeText(getApplicationContext(), "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    } else {
                        Toast.makeText(getApplicationContext(), "Erreur Ajout", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("localId",localEnCours);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Erreur recherche document", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void ajouter_a_realTime(String type,String chemin){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> composant_valeur = new HashMap<>();
        if(type.equals("AMPOULE") || type.equals("REFRIGERATEUR") || type.equals("CLIMATISEUR") || type.equals("AUTRE") || type.equals("TONDEUSE")){
            composant_valeur.put(chemin, "OFF");
            ref.updateChildren(composant_valeur);
        }
        else{
            Long init= 0L;
            composant_valeur.put(chemin, init);
            ref.updateChildren(composant_valeur);
        }
    }
}
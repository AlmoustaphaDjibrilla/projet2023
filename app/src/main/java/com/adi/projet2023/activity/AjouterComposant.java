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
    @Override
    protected void onResume() {
        super.onResume();
        init();
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
                ajouterComposant();
                finish();
            }
        });

    }

    private void init(){
        autoCompleteTextView=findViewById(R.id.txtTypeAjoutComposant);
        addComposant=findViewById(R.id.btnAjouterComposant);
        nomComposantEdit=findViewById(R.id.txtNomAjoutComposant);
    }

    private boolean verifier_champ(String nomComposant, String typeComposant){
        Boolean check = true;
        if (TextUtils.isEmpty(nomComposant)) {
            nomComposantEdit.setError("Nom de la pièce requis",null);
            check=false;
        }

        else if (TextUtils.isEmpty(typeComposant)) {
            autoCompleteTextView.setError("Type de la pièce requis");
            check = false;
        }
        return check;
    }

    private void ajouterComposant(){
        String nomComposant = nomComposantEdit.getText().toString().trim();
        typeComposant = autoCompleteTextView.getText().toString();
        String c ="/";
        String chemin = pieceEnCours.getAdressePiece()+c+nomComposant.toLowerCase();

        if(verifier_champ(nomComposant, typeComposant)){
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

                    List<Composant> composantsList = new ArrayList<>();
                    for (Map<String, Object> map : composants) {
                        Composant composant = new Composant();
                        String nom = (String) map.get("nom");
                        String id = (String) map.get("idComposant");
                        String type = (String) map.get("typeComposant");
                        String chemin = (String) map.get("chemin");
                        composant.setIdComposant(id);
                        composant.setNomComposant(nom);
                        composant.setTypeComposant(type);
                        composant.setChemin(chemin);
                        composantsList.add(composant);
                    }

                    Map<String, Object> data = new HashMap<>();
                    data.put("lesPieces", lesPieces);
                    localref.document(localDoc.getId()).update(data).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Nouveau composant ajouté avec succès", Toast.LENGTH_SHORT).show();
                            LocalUtils.getLocalById(localEnCours.getIdLocal(), new OnSuccessListener<Local>() {
                                @Override
                                public void onSuccess(Local local) {
                                    // Aller vers Main Page avec local mis a jour
                                    Intent intent = new Intent(getApplicationContext(), MainPage.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
        else{
            return;
        }
    }
}
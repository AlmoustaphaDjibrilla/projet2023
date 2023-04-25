package com.adi.projet2023.activity;

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

import androidx.appcompat.app.AppCompatActivity;

import com.adi.projet2023.R;
import com.adi.projet2023.Utils.LocalUtils;
import com.adi.projet2023.activity.main_page.MainPage;
import com.adi.projet2023.model.local.Local;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AjoutPieceActivity extends AppCompatActivity {
    private String typePiece;
    private Button addPiece;
    Local localEnCours;
    EditText nomPieceEdit;
    AutoCompleteTextView autoCompleteTextView;
    @Override
    protected void onResume() {
        super.onResume();
        init();
        String []type = getResources().getStringArray(R.array.type_piece);
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this,R.layout.type_pieces, type);

        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                typePiece = adapterView.getItemAtPosition(i).toString().toUpperCase();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_piece);
        init();
        localEnCours = (Local) getIntent().getSerializableExtra("localEnCours");
        addPiece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ajouterPiece();
                finish();
            }
        });
    }

    private void init(){
        autoCompleteTextView=findViewById(R.id.txtTypeAjoutPiece);
        addPiece=findViewById(R.id.btnAjouterPiece);
        nomPieceEdit=findViewById(R.id.txtNomAjoutPiece);
    }

    private void ajouterPiece() {
        String nomPiece = nomPieceEdit.getText().toString().trim();
        typePiece = autoCompleteTextView.getText().toString();
        String c ="/";
        String chemin = c+localEnCours.getNomLocal().toLowerCase()+c+nomPiece.toLowerCase();

        if(verifier_champ(nomPiece,typePiece)){
            // Créer un objet Map pour représenter les données de la nouvelle pièce
            Map<String, Object> piece = new HashMap<>();
            piece.put("idPiece",nomPiece.replaceAll("\\s", "").toLowerCase());
            piece.put("nom", nomPiece);
            piece.put("chemin",chemin);
            piece.put("typePiece", typePiece);
            piece.put("composants", new ArrayList<Map<String, Object>>());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference localRef = db.collection("Local");

            // Ajouter la nouvelle pièce à la liste "lesPieces" du document "Local" correspondant à l'ID "test"
            localRef.whereEqualTo("idLocal", localEnCours.getIdLocal())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                DocumentSnapshot localDoc = queryDocumentSnapshots.getDocuments().get(0);
                                List<Map<String, Object>> lesPieces = (List<Map<String, Object>>) localDoc.get("lesPieces");
                                if (lesPieces == null) {
                                    lesPieces = new ArrayList<>();
                                }
                                lesPieces.add(piece);
                                localDoc.getReference().update("lesPieces", lesPieces)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(AjoutPieceActivity.this, "Nouvelle pièce ajoutée avec succès", Toast.LENGTH_SHORT).show();
                                                //Aller vers MainPage
                                                LocalUtils.getLocalById(localEnCours.getIdLocal(), new OnSuccessListener<Local>() {
                                                    @Override
                                                    public void onSuccess(Local local) {
                                                        // Aller vers Main Page avec local mis a jour
                                                        Intent intent = new Intent(getApplicationContext(), MainPage.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.putExtra("localId",local);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }, new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Erreur lors de la recuperation du local mis a jour
                                                        Toast.makeText(AjoutPieceActivity.this, "Erreur : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AjoutPieceActivity.this, "Erreur lors de l'ajout de la pièce : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), MainPage.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.putExtra("localId",localEnCours);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                            } else {
                                Toast.makeText(AjoutPieceActivity.this, "Local introuvable", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AjoutPieceActivity.this, "Erreur lors de la récupération Local : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            return;
        }

    }

    private boolean verifier_champ(String nomPiece, String typePiece){
        Boolean check = true;
        if (TextUtils.isEmpty(nomPiece)) {
            nomPieceEdit.setError("Nom de la pièce requis",null);
            check=false;
        }

        else if (TextUtils.isEmpty(typePiece)) {
            autoCompleteTextView.setError("Type de la pièce requis");
            check = false;
        }
        return check;
    }

}
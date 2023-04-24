package com.adi.projet2023.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.adi.projet2023.R;
import com.adi.projet2023.creation.CreationPiece;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.Piece.TypePiece;
import com.adi.projet2023.model.local.Local;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AjoutPieceActivity extends AppCompatActivity {

    final String PATH_USERS_DATABASE= "Users";


    Local localEnCours;
    EditText txtNomPiece;
    AutoCompleteTextView txtTypePiece;
    String [] lesTypesPiece;
    TypePiece []lesTypesPieces;

    Button btnAjouterPiece;
    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_piece);
        init();

        Intent intentGetLocal= getIntent();
        localEnCours= (Local) intentGetLocal.getSerializableExtra("localEnCours");

        btnAjouterPiece.setOnClickListener(
                v->{



                    String nomPiece= txtNomPiece.getText().toString();
                    String choix= txtTypePiece.getText().toString();

                    ajouterPiece(choix, nomPiece);
                }
        );
    }

    private void init(){
        txtNomPiece= findViewById(R.id.txtNomAjoutPiece);
        txtTypePiece= findViewById(R.id.txtTypeAjoutPiece);

        lesTypesPieces= TypePiece.values();

//        lesTypesPiece= getResources().getStringArray(R.array.type_piece);
//        ArrayAdapter<String> adapter= new ArrayAdapter<>(this,R.layout.type_pieces, lesTypesPiece);

        ArrayAdapter<TypePiece> adapter= new ArrayAdapter<>(this, R.layout.type_pieces, lesTypesPieces);
        txtTypePiece.setAdapter(adapter);

        btnAjouterPiece= findViewById(R.id.btnAjouterPiece);
    }

    private void ajouterPiece(String typePiece, String nomPiece){
        if (typePiece==null || typePiece.equals("")){
            txtTypePiece.setError("Veuillez Choisir le type de piece");
            return;
        }
        if (nomPiece==null || nomPiece.equals("")){
            txtNomPiece.setError("Veuillez saisir le nom de la pièce");
            return;
        }

        CollectionReference collectionUsers=
                FirebaseFirestore.getInstance().collection(PATH_USERS_DATABASE);


        DocumentReference docUsermodel=
                collectionUsers
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        docUsermodel.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.getBoolean("admin")){

                            Piece nouvellePiece= new Piece(TypePiece.valueOf(typePiece), nomPiece);

                            String nouvelIdPiece= localEnCours.getAdresseLocal()+"_"+nouvellePiece.getAdressePiece();

                            nouvellePiece.setIdPiece(nouvelIdPiece);
                            nouvellePiece.setAdressePiece(nouvelIdPiece);
                            nouvellePiece.setAdresseLocalEnCours(localEnCours.getAdresseLocal());

                            CreationPiece.creationPiece(nouvellePiece);
                            Toast.makeText(getApplicationContext(), nouvellePiece.getNomPiece()+" ajouté...", Toast.LENGTH_SHORT).show();
                            finish();

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
}
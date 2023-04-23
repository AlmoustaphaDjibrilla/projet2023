package com.adi.projet2023.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.adi.projet2023.R;
import com.adi.projet2023.creation.CreationPiece;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.Piece.TypePiece;
import com.adi.projet2023.model.local.Local;

public class AjoutPieceActivity extends AppCompatActivity {

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
//                    TypePiece typePiece;
//
//                    if (choix==null || choix.equals("")){
//                        typePiece= TypePiece.AUTRE;
//                    }
//                    else {
//                        typePiece= TypePiece.valueOf(choix);
//                    }
//                    Piece nouvellePiece= new Piece(typePiece, nomPiece);
//                    Toast.makeText(this, nouvellePiece.toString(), Toast.LENGTH_SHORT).show();

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

        Piece nouvellePiece= new Piece(TypePiece.valueOf(typePiece), nomPiece);
        nouvellePiece.setAdresseLocalEnCours(localEnCours.getAdresseLocal());
        CreationPiece.creationPiece(nouvellePiece);
        Toast.makeText(this, nouvellePiece.getNomPiece()+" ajouté...", Toast.LENGTH_SHORT).show();
        finish();
    }
}
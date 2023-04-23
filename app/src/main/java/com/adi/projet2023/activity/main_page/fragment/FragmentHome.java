package com.adi.projet2023.activity.main_page.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.adi.projet2023.R;
import com.adi.projet2023.activity.AjoutPieceActivity;
import com.adi.projet2023.activity.Composants;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.composant.Composant;
import com.adi.projet2023.model.local.Local;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.List;

public class FragmentHome extends Fragment {
    GridLayout gridPiece;
     Local localEnCours;
    List<Piece> piecesList;

    FloatingActionButton btnAddPiece;

    public FragmentHome(Local local) {
        // Required empty public constructor
        this.localEnCours= local;
        if( local!=null){
            piecesList= localEnCours.getLesPieces();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home,null);
        btnAddPiece = root.findViewById(R.id.addPiece);

        btnAddPiece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AjoutPieceActivity.class);
                startActivity(intent);
            }
        });

        gridPiece = (GridLayout) root.findViewById(R.id.GridPieces);
        int marginPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        if(piecesList != null && piecesList.size()!=0){
            int numColumns = 2; // Nombre de colonnes de la grille
            int numRows = (int) Math.ceil(piecesList.size() / (float) numColumns); // Nombre de lignes de la grille
            gridPiece.setColumnCount(numColumns); // Définir le nombre de colonnes de la grille
            gridPiece.setRowCount(numRows); // Définir le nombre de lignes de la grille
            for (int i = 0; i < piecesList.size(); i++) {
                List<Composant> composants = piecesList.get(i).getLesComposants();
                View cardView = inflater.inflate(R.layout.piece_card, gridPiece, false);
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                layoutParams.columnSpec = GridLayout.spec(i % numColumns, 1f);
                layoutParams.rowSpec = GridLayout.spec(i / numColumns, 1f);
                cardView.setLayoutParams(layoutParams);
                layoutParams.setMargins(marginPx, marginPx, marginPx, marginPx);
                TextView nomP = cardView.findViewById(R.id.nomPiece);
                ImageView img = cardView.findViewById(R.id.imagePiece);
                nomP.setText(piecesList.get(i).getNomPiece());
                switch (piecesList.get(i).getTypePiece()) {
                    case "CHAMBRE":
                        img.setImageResource(R.drawable.chambre);
                        break;
                    case "SALON":
                        img.setImageResource(R.drawable.salon);
                        break;
                    case "CUISINE":
                        img.setImageResource(R.drawable.cuisine);
                        break;
                    case "DOUCHE":
                        img.setImageResource(R.drawable.douche);
                        break;
                    case "AUTRE":
                        img.setImageResource(R.drawable.autre_piece);
                        break;
                    default:
                        break;
                }
                gridPiece.addView(cardView);
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Composants.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("composants",(Serializable) composants) ;
                        startActivity(intent);
                    }
                });
            }
        }
        else {
            Log.d("TAG", "Aucune piece");
        }
        return root;
    }

}
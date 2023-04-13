package com.adi.projet2023.activity.main_page;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.adi.projet2023.R;
import com.adi.projet2023.model.Piece.Chambre;
import com.adi.projet2023.model.Piece.Cuisine;
import com.adi.projet2023.model.Piece.Douche;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.Piece.Salon;

import java.util.ArrayList;
import java.util.List;
public class ActivityAffichagePieces extends AppCompatActivity {
    List<Piece> piecesList;
    GridLayout gridPiece;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_pieces);
        init();
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        piecesList = List.of(new Chambre("Ma Chambre"),
                new Salon("Cuisine 1"),
                new Douche("Douche 4"),
                new Chambre("Chambre 2"),
                new Cuisine("Cuisine 2"),
                new Cuisine("Cuisine 2"),
                new Douche("Cuisine 2"),
                new Salon("Cuisine 2"));
        int marginPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        for(int i =0; i<piecesList.size(); i++){
            View cardView = inflater.inflate(R.layout.piece_card, gridPiece,false);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.columnSpec=GridLayout.spec(i%2, 1f);
            cardView.setLayoutParams(layoutParams);
            layoutParams.setMargins(marginPx,marginPx, marginPx, marginPx);
            TextView nom = cardView.findViewById(R.id.nomPiece);
            ImageView img = cardView.findViewById(R.id.imagePiece);
            nom.setText(piecesList.get(i).getNomPiece());
            switch (piecesList.get(i).getTypePiece().toString()){
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
                    img.setImageResource(R.drawable.img_autre);
                    break;
                default:
                    break;
            }
            gridPiece.addView(cardView);
        }
    }
    private void init(){
        gridPiece= findViewById(R.id.GridPieces);
        piecesList= new ArrayList<>();
    }
}
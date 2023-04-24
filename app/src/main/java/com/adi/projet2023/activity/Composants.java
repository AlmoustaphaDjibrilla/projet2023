package com.adi.projet2023.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.adi.projet2023.R;
import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.composant.Composant;
import com.adi.projet2023.model.local.Local;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class Composants extends AppCompatActivity {
    LinearLayout layout;
    List<Composant> composantList;
    Local localEnCours;
    Piece pieceEnCours;
    FloatingActionButton btnAddComposant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_composants);
        init();
        composantList = (List<Composant>) getIntent().getSerializableExtra("composants");
        localEnCours = (Local) getIntent().getSerializableExtra("localEnCours");
        pieceEnCours = (Piece) getIntent().getSerializableExtra("pieceEnCours");


        btnAddComposant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AjouterComposant.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("localEnCours",localEnCours);
                intent.putExtra("pieceEnCours",pieceEnCours);
                startActivity(intent);
            }
        });

        //Initialisation des composants
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        for(int i =0; i<composantList.size(); i++){
            View cardView = inflater.inflate(R.layout.composant_card, layout,false);
            View cardView2 = inflater.inflate(R.layout.composant2_card, layout,false);
            if(composantList.get(i).getTypeComposant().toString().equals("AMPOULE")||
                    composantList.get(i).getTypeComposant().toString().equals("REFRIGERATEUR")||
                    composantList.get(i).getTypeComposant().toString().equals("CLIMATISEUR")||
                    composantList.get(i).getTypeComposant().toString().equals("AUTRE")){
                TextView nom = cardView.findViewById(R.id.nom_composant);
                ImageView img = cardView.findViewById(R.id.img_composant);
                nom.setText(composantList.get(i).getNomComposant());
                switch (composantList.get(i).getTypeComposant().toString()){
                    case "AMPOULE":
                        img.setImageResource(R.drawable.ampoules);
                        break;
                    case "CLIMATISEUR":
                        img.setImageResource(R.drawable.clim);
                        break;
                    case "PORTE":
                        img.setImageResource(R.drawable.porte);
                        break;
                    case "REFRIGERATEUR":
                        img.setImageResource(R.drawable.frigo);
                        break;
                    case "VENTILATEUR":
                        img.setImageResource(R.drawable.ventilateur);
                        break;
                    case "AUTRE":
                        img.setImageResource(R.drawable.autre);
                        break;
                    default:
                        break;
                }
                layout.addView(cardView);
            }else {
                TextView nom = cardView2.findViewById(R.id.nom_composant);
                ImageView img = cardView2.findViewById(R.id.img_composant);
                nom.setText(composantList.get(i).getNomComposant());
                switch (composantList.get(i).getTypeComposant().toString()){
                    case "AMPOULE":
                        img.setImageResource(R.drawable.ampoule_animee);
                        break;
                    case "CLIMATISEUR":
                        img.setImageResource(R.drawable.clim);
                        break;
                    case "PORTE":
                        img.setImageResource(R.drawable.porte);
                        break;
                    case "REFRIGERATEUR":
                        img.setImageResource(R.drawable.frigo);
                        break;
                    case "VENTILATEUR":
                        img.setImageResource(R.drawable.ventilateur);
                        break;
                    case "AUTRE":
                        img.setImageResource(R.drawable.autre);
                        break;
                    default:
                        break;
                }
                layout.addView(cardView2);
            }

        }
    }

    private void init(){
        layout=findViewById(R.id.composant);
        btnAddComposant = findViewById(R.id.addComposant);
    }
}
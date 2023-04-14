package com.adi.projet2023.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.adi.projet2023.R;
import com.adi.projet2023.model.composant.Ampoule;
import com.adi.projet2023.model.composant.Autre;
import com.adi.projet2023.model.composant.Climatiseur;
import com.adi.projet2023.model.composant.Composant;
import com.adi.projet2023.model.composant.Porte;
import com.adi.projet2023.model.composant.Refrigerateur;
import com.adi.projet2023.model.composant.Ventilateur;

import java.util.ArrayList;
import java.util.List;

public class Composants extends AppCompatActivity {
    LinearLayout layout;
    List<Composant> composantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composants);
        init();
    }

    private void init(){
        layout=findViewById(R.id.composant);
        composantList = new ArrayList<>();
        composantList = List.of(new Ampoule("Lampe"),
                new Climatiseur("clim 1"),
                new Refrigerateur("frigo 4"),
                new Ventilateur("venti 2"),
                new Porte("porte 2"),
                new Autre("Machine a laver"),
                new Ampoule("lampe 2"),
                new Climatiseur("clim salon"));

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
                layout.addView(cardView2);
            }

        }
    }
}
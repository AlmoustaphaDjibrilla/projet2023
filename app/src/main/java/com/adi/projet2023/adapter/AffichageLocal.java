package com.adi.projet2023.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.projet2023.R;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.local.TypeLocal;

public class AffichageLocal extends RecyclerView.ViewHolder {

    ImageView imgLocal;
    TextView txtTypeLocal, txtNomLocal, txtQuartierLocal, txtVilleLocal;
    ConstraintLayout layoutLocal;

    public AffichageLocal(@NonNull View itemView) {
        super(itemView);
        init(itemView);
    }


    private void init(View view){
        imgLocal= view.findViewById(R.id.imgChoixLocal);
        txtTypeLocal= view.findViewById(R.id.txtTypeLocal);
        txtNomLocal= view.findViewById(R.id.txtNomChoixLocal);
        txtQuartierLocal= view.findViewById(R.id.txtQuartierChoixLocal);
        txtVilleLocal= view.findViewById(R.id.txtVilleChoixLocal);
        layoutLocal= view.findViewById(R.id.layoutLocal);
    }

    public void remplirChamps(Local local){
        if (local!=null){
            String designation= local.getDesignationLocal();
            switch (designation){
                case "MAISON":
                    imgLocal.setImageResource(R.drawable.img_maison);
                    break;

                case "ENTREPRISE":
                    imgLocal.setImageResource(R.drawable.img_entreprise);
                    break;

                case "AUTRE":
                    imgLocal.setImageResource(R.drawable.img_autre);
                    break;

                default:
                    break;
            }

            String nom= local.getNomLocal();
            if (nom!=null)
                txtNomLocal.setText(nom);

            String quartier= local.getQuartierLocal();
            if (quartier!=null)
                txtQuartierLocal.setText(quartier);

            String ville= local.getVilleLocal();
            txtVilleLocal.setText(ville);

            TypeLocal typeLocal= local.getTypeLocal();
            if (typeLocal!=null)
                txtTypeLocal.setText(typeLocal.toString());
        }
    }
}

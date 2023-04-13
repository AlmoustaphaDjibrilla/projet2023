package com.adi.projet2023.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adi.projet2023.R;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.local.TypeLocal;

import java.util.ArrayList;
import java.util.List;

public abstract class AdapterChoixLocal extends BaseAdapter {

    /**
     * Propriétés de la liste
     */
    Context context;
    ArrayList<Local> lesLocaux;
    LayoutInflater inflater;

    /***
     * Proprietes de chaque local
     */
    ImageView imgLocal;
    TextView txtTypeLocal, txtNomLocal, txtQuartierLocal, txtVilleLocal;

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    /**
     * Initialisation de la liste
     * affichant les locaux diso
     * @param context, qui est le context actuel
     * @param lesLocaux, les locaux enregistrés dans la BD
     */
    public AdapterChoixLocal(Context context, ArrayList<Local> lesLocaux) {
        this.context = context;
        this.lesLocaux = lesLocaux;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lesLocaux.size();
    }

    @Override
    public Object getItem(int i) {
        return lesLocaux.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view= inflater.inflate(R.layout.modele_local, null);
        init(view);

        Local local= (Local) getItem(i);

        remplirChamps(local);

        return view;
    }

    /**
     * Initialisation des composants dans
     * la liste view
     * @param view le view
     */
    private void init(View view){
        imgLocal= view.findViewById(R.id.imgChoixLocal);
        txtTypeLocal= view.findViewById(R.id.txtTypeLocal);
        txtNomLocal= view.findViewById(R.id.txtNomChoixLocal);
        txtQuartierLocal= view.findViewById(R.id.txtQuartierChoixLocal);
        txtVilleLocal= view.findViewById(R.id.txtVilleChoixLocal);
    }

    /**
     * Remplir les champs dynamiquement
     * @param local !=null
     */
    private void remplirChamps(Local local){
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

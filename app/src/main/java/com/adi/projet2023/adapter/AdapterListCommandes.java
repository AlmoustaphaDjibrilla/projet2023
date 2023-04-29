package com.adi.projet2023.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.adi.projet2023.R;
import com.adi.projet2023.model.Commande.Commande;

import java.util.ArrayList;

public class AdapterListCommandes extends BaseAdapter {

    private final String detail_allumer_ampoule= "Allumage";
    private final String detail_extinction_ampoule= "Extinction";

    TextView txtDateCommande, txtMailUserCommande, txtComposantCommande, txtDetailCommande;
    ImageView imgActionCommande;

    Context context;
    ArrayList<Commande> lesCommandes;
    LayoutInflater inflater;

    /**
     * Constructeur
     * @param context, qui est le context en cours
     * @param lesCommandes la liste des commandes
     */
    public AdapterListCommandes(Context context, ArrayList<Commande> lesCommandes) {
        this.context = context;
        this.lesCommandes = lesCommandes;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return lesCommandes.size();
    }

    @Override
    public Object getItem(int i) {
        return lesCommandes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view= inflater.inflate(R.layout.modele_commande, null);

        init(view);

        Commande commandeEnCours= (Commande) lesCommandes.get(position);

        String dateCommande= commandeEnCours.getDate_commande();
        String heureCommande= commandeEnCours.getHeure_commande();
        txtDateCommande.setText(dateCommande+"\n"+heureCommande);

        txtMailUserCommande.setText(commandeEnCours.getEmailUser());
        txtComposantCommande.setText(commandeEnCours.getComposant().getNomComposant());

        String detailCommande= commandeEnCours.getDetail_commande();
        txtDetailCommande.setText(detailCommande);


        if (detailCommande.contains(detail_allumer_ampoule)){
            String typeComposant= commandeEnCours.getComposant().getTypeComposant();

            switch (typeComposant){
                case "AMPOULE":
                    imgActionCommande.setImageResource(R.drawable.icon_lumiere_allumee);
                    break;

                case "CLIMATISEUR":
                    imgActionCommande.setImageResource(R.drawable.icon_allumage_climatiseur);
                    break;

                case "VENTILATEUR":
                    imgActionCommande.setImageResource(R.drawable.icon_allumage_ventilateur);
                    break;

                case "PORTE":
                    imgActionCommande.setImageResource(R.drawable.icon_ouverture_porte);
                    break;

                case "REFRIGERATEUR":
                    imgActionCommande.setImageResource(R.drawable.icon_allumage_frigo);

                default:
                    break;
            }

        }
        else if (detailCommande.contains(detail_extinction_ampoule)){
            String typeComposant= commandeEnCours.getComposant().getTypeComposant();

            switch (typeComposant){
                case "AMPOULE":
                    imgActionCommande.setImageResource(R.drawable.icon_lumiere_eteinte);
                    break;

                case "CLIMATISEUR":
                    imgActionCommande.setImageResource(R.drawable.icon_extinction_climatiseur);
                    break;

                case "VENTILATEUR":
                    imgActionCommande.setImageResource(R.drawable.icon_extinction_ventilateur);
                    break;

                case "PORTE":
                    imgActionCommande.setImageResource(R.drawable.icon_fermeture_porte);
                    break;

                case "REFRIGERATEUR":
                    imgActionCommande.setImageResource(R.drawable.icon_extinction_frigo);

                default:
                    break;
            }
        }


        return view;
    }

    private void init(View view){
        txtDateCommande= view.findViewById(R.id.txtDateCommande);
        txtMailUserCommande= view.findViewById(R.id.txtMailUserCommande);
        txtComposantCommande= view.findViewById(R.id.txtComposantCommande);
        txtDetailCommande= view.findViewById(R.id.txtDetailCommande);
        imgActionCommande= view.findViewById(R.id.imgActionCommande);
    }
}

package com.adi.projet2023.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.projet2023.R;
import com.adi.projet2023.model.user.UserModel;

public class AffichageUserModel extends RecyclerView.ViewHolder{


    TextView txtNomUser, txtMailUser, txtDateEnregistrement;

    CardView cardUserModel;

    public AffichageUserModel(@NonNull View itemView) {
        super(itemView);
        init(itemView);
    }

    private void init(View view){
        txtNomUser= view.findViewById(R.id.nomUserModel);
        txtMailUser= view.findViewById(R.id.mailUserModel);
        txtDateEnregistrement= view.findViewById(R.id.dateEnregistrementUserModel);
        cardUserModel= view.findViewById(R.id.cardViewModeleUser);
    }

    public void remplirChampsUserModel(UserModel userModel){
        if (userModel!=null){
            txtNomUser.setText(userModel.getNom());
            txtMailUser.setText(userModel.getEmail());
            txtDateEnregistrement.setText(userModel.getDateEnregistrement());
        }
    }
}

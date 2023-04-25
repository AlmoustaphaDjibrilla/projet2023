package com.adi.projet2023.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.projet2023.R;
import com.adi.projet2023.model.user.UserModel;

import java.util.ArrayList;

public class AdapterUserModel extends RecyclerView.Adapter<AffichageUserModel>{

    Context context;
    ArrayList<UserModel> lesUsers;
    LayoutInflater inflater;

    public AdapterUserModel(Context context, ArrayList<UserModel> lesUsers) {
        this.context = context;
        this.lesUsers = lesUsers;
    }

    @NonNull
    @Override
    public AffichageUserModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AffichageUserModel affichageUserModel= new AffichageUserModel(LayoutInflater.from(context).inflate(R.layout.modele_user, parent, false));

        return affichageUserModel;
    }

    @Override
    public void onBindViewHolder(@NonNull AffichageUserModel holder, int position) {

        UserModel userCourant= lesUsers.get(position);
        holder.remplirChampsUserModel(userCourant);

        holder.cardUserModel.setOnClickListener(
                v->{
                    Toast.makeText(context, "User: "+userCourant.getNom(), Toast.LENGTH_SHORT).show();
                }
        );

    }

    @Override
    public int getItemCount() {
        return lesUsers.size();
    }
}

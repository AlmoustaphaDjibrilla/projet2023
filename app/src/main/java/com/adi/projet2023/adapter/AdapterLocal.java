package com.adi.projet2023.adapter;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.projet2023.R;
import com.adi.projet2023.activity.main_page.MainPage;
import com.adi.projet2023.activity.main_page.fragment.FragmentHome;
import com.adi.projet2023.model.local.Local;

import java.util.ArrayList;

public class AdapterLocal extends RecyclerView.Adapter<AffichageLocal> {

    Context context;
    ArrayList<Local> lesLocaux;
    LayoutInflater inflater;

    public interface  OnItemClickListener{
        void onItemClick(String localId);
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public AdapterLocal(Context context, ArrayList<Local> lesLocaux) {
        this.context = context;
        this.lesLocaux = lesLocaux;
    }

    @NonNull
    @Override
    public AffichageLocal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AffichageLocal(LayoutInflater.from(context).inflate(R.layout.modele_local, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AffichageLocal holder, int position) {
        Local local= lesLocaux.get(position);
        holder.remplirChamps(local);
        holder.layoutLocal.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      if(listener != null){
                          listener.onItemClick(local.getIdLocal());
                      }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return lesLocaux.size();
    }


}

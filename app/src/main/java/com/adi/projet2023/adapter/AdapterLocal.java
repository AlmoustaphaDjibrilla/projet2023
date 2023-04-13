package com.adi.projet2023.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.projet2023.R;
import com.adi.projet2023.activity.main_page.MainPage;
import com.adi.projet2023.model.local.Local;

import java.util.ArrayList;

public class AdapterLocal extends RecyclerView.Adapter<AffichageLocal> {

    Context context;
    ArrayList<Local> lesLocaux;
    LayoutInflater inflater;

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
//                        Toast.makeText(context.getApplicationContext(), local.toString(), Toast.LENGTH_SHORT).show();
                        Intent sendLocal= new Intent(context.getApplicationContext(), MainPage.class);
                        sendLocal.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        sendLocal.putExtra("envoiLocal", local);
                        context.startActivity(sendLocal);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return lesLocaux.size();
    }


}

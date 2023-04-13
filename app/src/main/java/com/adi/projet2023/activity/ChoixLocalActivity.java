package com.adi.projet2023.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adi.projet2023.R;
import com.adi.projet2023.adapter.AdapterChoixLocal;
import com.adi.projet2023.adapter.AdapterLocal;
import com.adi.projet2023.model.local.AutreLocal;
import com.adi.projet2023.model.local.Entreprise;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.local.Maison;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class ChoixLocalActivity extends AppCompatActivity {

//    private AppBarConfiguration appBarConfiguration;
//    private ActivityChoixLocalBinding binding;
    RecyclerView listLocaux;
    ArrayList<Local> lesLocaux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_local);

//        binding = ActivityChoixLocalBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
        init();

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        lesLocaux.add(new Maison("maison secondaire", "Cite El khadra", "Tunis"));
        lesLocaux.add(new AutreLocal("jardin", "Île de Nice", "Djerba"));
        lesLocaux.add(new Maison("maison Parents", "Cite caisse", "Mahdia"));
        lesLocaux.add(new Entreprise("Fac des sciences", "Cite el Omrane", "Monastir"));
        lesLocaux.add(new Maison ("maison copine", "avenue CDG", "Paris"));

        AdapterLocal adapterLocal= new AdapterLocal(getApplicationContext(), lesLocaux);

        listLocaux.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listLocaux.setAdapter(adapterLocal);

    }

    /**
     * Initialisation des composants
     */
    private void init(){
        listLocaux= findViewById(R.id.listChoixLocal);
        lesLocaux= new ArrayList<>();
    }
}
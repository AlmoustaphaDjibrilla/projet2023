package com.adi.projet2023.activity.main_page;

import static java.util.List.of;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.adi.projet2023.R;
import com.adi.projet2023.adapter.AdapterChoixLocal;
import com.adi.projet2023.adapter.AdapterLocal;
import com.adi.projet2023.model.local.Entreprise;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.local.Maison;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

public class ListEssai extends AppCompatActivity {
    RecyclerView listEssai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_essai);
        init();

        ArrayList<Local> locals= new ArrayList<>();

        locals.add(new Maison("maison", "maison", "maison"));
        locals.add(new Entreprise("enterprise", "niamey", "niamey"));
        AdapterLocal adapterLocal= new AdapterLocal(getApplicationContext(), locals);

        listEssai.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listEssai.setAdapter(adapterLocal);


//        String[] items = {"item 1", "item 2", "item 3"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
//        listEssai.setAdapter(adp);
//        listEssai.setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Local local=(Local) adp.getItem(i);
//                        Toast.makeText(getApplicationContext(), "Item : "+i, Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
    }

    void init(){
        listEssai= findViewById(R.id.listEssai);
        listEssai.setHasFixedSize(true);
    }
}
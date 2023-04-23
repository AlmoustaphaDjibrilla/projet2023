package com.adi.projet2023.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.adi.projet2023.R;

public class Ajout_Piece extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        String []type = getResources().getStringArray(R.array.type_piece);
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this,R.layout.type_pieces, type);

        AutoCompleteTextView autoCompleteTextView=findViewById(R.id.txtTypeAjoutPiece);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_piece);
    }
}
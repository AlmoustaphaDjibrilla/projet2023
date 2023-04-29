package com.adi.projet2023.model.composant;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.Serializable;
import java.util.HashMap;

public class Composant implements Serializable {

    DatabaseReference databaseReference;
    String idComposant;
    String chemin;
    String nomComposant;
    String typeComposant;
    public Composant(){
    }

    public Composant(String id, String nom, String chemin, String type){
        this.idComposant=id;
        this.nomComposant=nom;
        this.chemin=chemin;
        this.typeComposant=type;
    }

    public String getIdComposant() {
        return idComposant;
    }

    public void setIdComposant(String idComposant) {
        this.idComposant = idComposant;
    }

    public String getNomComposant() {
        return nomComposant;
    }

    public void setNomComposant(String nomComposant) {
        this.nomComposant = nomComposant;
    }

    public String getTypeComposant() {
        return typeComposant;
    }

    public void setTypeComposant(String typeComposant) {
        this.typeComposant = typeComposant;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }

    public void setEtat(Boolean b){
        databaseReference = FirebaseDatabase.getInstance().getReference().child(getChemin());
        if(b){
            HashMap<String, Object> hashMap =new HashMap<>();
            hashMap.put(getNomComposant(), "ON");
            databaseReference.setValue(hashMap);
        }
        else{
            HashMap<String, Object> hashMap =new HashMap<>();
            hashMap.put(getNomComposant(), "OFF");
            databaseReference.setValue(hashMap);
        }
    }

    public void setValeur(int p){
        databaseReference = FirebaseDatabase.getInstance().getReference().child(getChemin());
        HashMap<String, Object> hashMap =new HashMap<>();
        hashMap.put(getNomComposant(), p);
        databaseReference.setValue(hashMap);
    }



}
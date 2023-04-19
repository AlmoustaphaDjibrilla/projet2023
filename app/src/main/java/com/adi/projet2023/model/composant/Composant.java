package com.adi.projet2023.model.composant;

import java.io.Serializable;

public abstract class Composant implements Serializable {
    String idComposant;
    String chemin;
    String nomComposant;
    TypeComposant typeComposant;
    public Composant(){

    }
    public Composant(String nom, TypeComposant type){
        this.nomComposant=nom;
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

    public TypeComposant getTypeComposant() {
        return typeComposant;
    }

    public void setTypeComposant(TypeComposant typeComposant) {
        this.typeComposant = typeComposant;
    }

    public String getChemin() {
        return chemin;
    }

    public void setChemin(String chemin) {
        this.chemin = chemin;
    }
}
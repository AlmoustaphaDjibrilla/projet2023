package com.adi.projet2023.model.composant;

public abstract class Composant {
    String idComposant;
    String nomComposant;
    TypeComposants typeComposant;
    public Composant(){

    }
    public Composant(String nom, TypeComposants type){
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

    public TypeComposants getTypeComposant() {
        return typeComposant;
    }

    public void setTypeComposant(TypeComposants typeComposant) {
        this.typeComposant = typeComposant;
    }
}
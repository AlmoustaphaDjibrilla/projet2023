package com.adi.projet2023.model.composant;

import java.io.Serializable;

public class Composant implements Serializable {
    String idComposant;

    String adresseComposant;
    String adressePieceEnCours;
    String chemin;
    String nomComposant;
    String typeComposant;
    public Composant(){

    }
    public Composant(String nom, String type){
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

    public String getAdresseComposant() {
        return adresseComposant;
    }

    public void setAdresseComposant(String adresseComposant) {
        this.adresseComposant = adresseComposant;
    }

    public String getAdressePieceEnCours() {
        return adressePieceEnCours;
    }

    public void setAdressePieceEnCours(String adressePieceEnCours) {
        this.adressePieceEnCours = adressePieceEnCours;
    }
}
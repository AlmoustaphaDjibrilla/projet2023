package com.adi.projet2023.model.composant;

import java.io.Serializable;

public class Composant implements Serializable {
    String idComposant;
    String adresseComposant;
    String adressePieceEnCours;
    String adresseLocalEnCours;
    String chemin;
    String nomComposant;
    String typeComposant;
    public Composant(){
    }

    public Composant(String nom, String chemin, String type){
        this.idComposant= nom.toLowerCase().replaceAll(" ", "");
        this.nomComposant=nom;
        this.chemin=chemin;
        this.typeComposant=type;
    }

    public Composant(TypeComposant typeComposant, String nom){
        this.typeComposant= typeComposant.toString();
        this.nomComposant= nom;
        this.adresseComposant= nomComposant.toLowerCase().replaceAll(" ", "");
        this.idComposant= adresseComposant;
        this.chemin= idComposant;
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

    public String getAdresseLocalEnCours() {
        return adresseLocalEnCours;
    }

    public void setAdresseLocalEnCours(String adresseLocalEnCours) {
        this.adresseLocalEnCours = adresseLocalEnCours;
    }

    @Override
    public String toString() {
        return "Composant{" +
                "idComposant='" + idComposant + '\'' +
                ", adresseComposant='" + adresseComposant + '\'' +
                ", adressePieceEnCours='" + adressePieceEnCours + '\'' +
                ", chemin='" + chemin + '\'' +
                ", nomComposant='" + nomComposant + '\'' +
                ", typeComposant='" + typeComposant + '\'' +
                '}';
    }
}
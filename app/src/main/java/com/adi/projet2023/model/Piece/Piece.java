package com.adi.projet2023.model.Piece;

import com.adi.projet2023.model.composant.Composant;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.user.UserModel;

import java.io.Serializable;
import java.util.List;

public abstract class Piece implements Serializable {
    String idPiece;
    String adressePiece;
    String nomPiece;
    String typePiece;
    List<UserModel> lesUsers;
    List<Composant> lesComposants;

    public Piece(){
    }
    public Piece(String type, String nom){
        this.typePiece=type;
        this.nomPiece=nom;
    }
    public void setIdPiece(String idPiece){
        this.idPiece =idPiece;
    }

    public String getIdPiece() {
        return idPiece;
    }

    public void setTypePiece(String typePiece) {
        this.typePiece = typePiece;
    }

    public String getTypePiece() {
        return typePiece;
    }

    public void setNomPiece(String nomPiece) {
        this.nomPiece = nomPiece;
    }

    public String getNomPiece() {
        return nomPiece;
    }

    public String getAdressePiece() {
        return adressePiece;
    }

    public void setAdressePiece(String adressePiece) {
        this.adressePiece = adressePiece;
    }

    public List<UserModel> getLesUsers() {
        return lesUsers;
    }

    public void setLesUsers(List<UserModel> lesUsers) {
        this.lesUsers = lesUsers;
    }

    public List<Composant> getLesComposants() {
        return lesComposants;
    }

    public void setLesComposants(List<Composant> lesComposants) {
        this.lesComposants = lesComposants;
    }


    @Override
    public String toString() {
        return "Piece{" +
                "idPiece='" + idPiece + '\'' +
                ", adressePiece='" + adressePiece + '\'' +
                ", nomPiece='" + nomPiece + '\'' +
                ", typePiece=" + typePiece +
                ", lesUsers=" + lesUsers +
                ", lesComposants=" + lesComposants +
                '}';
    }
}

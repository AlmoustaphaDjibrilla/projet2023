package com.adi.projet2023.model.Piece;

import com.adi.projet2023.model.composant.Composant;
import com.adi.projet2023.model.local.Local;
import com.adi.projet2023.model.user.UserModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Piece implements Serializable {
    String idPiece;
    String adressePiece;
    String nomPiece;
    String typePiece;
    List<Composant> lesComposants;

    public Piece(){
    }
    public Piece(String type, String nom, List<HashMap<String, Object>> listHashMap){
        this.typePiece=type;
        this.nomPiece=nom;
        this.lesComposants = convertirListHashMapEnListCustomObject(listHashMap);
    }
    private List<Composant> convertirListHashMapEnListCustomObject(List<HashMap<String, Object>> listHashMap) {
        List<Composant> composantList = new ArrayList<>();
        for (HashMap<String, Object> hashMap : listHashMap) {
            Composant composant = new Composant();
            composant.setNomComposant((String) hashMap.get("nom"));
            composant.setChemin((String) hashMap.get("chemin"));
            composant.setTypeComposant((String) hashMap.get("typeComposant"));
            // etc.
            composantList.add(composant);
        }
        return composantList;
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
                ", lesComposants=" + lesComposants +
                '}';
    }
}

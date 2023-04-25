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
    String chemin;
    String nomPiece;
    String typePiece;
    List<Composant> lesComposants;

    public Piece(){
    }

    public Piece(String idPiece,String type, String nom,String chemin, List<HashMap<String, Object>> listHashMap){
            this.idPiece=idPiece;
            this.typePiece=type;
            this.nomPiece=nom;
            this.chemin=chemin;
            this.lesComposants = convertirListHashMapEnListCustomObject(listHashMap);
        }
        private List<Composant> convertirListHashMapEnListCustomObject(List<HashMap<String, Object>> listHashMap) {
            List<Composant> composantList = new ArrayList<>();
            if (listHashMap!=null){
                for (HashMap<String, Object> hashMap : listHashMap) {
                    Composant composant = new Composant();
                    composant.setIdComposant((String) hashMap.get("idComposant"));
                    composant.setNomComposant((String) hashMap.get("nom"));
                    composant.setChemin((String) hashMap.get("chemin"));
                    composant.setTypeComposant((String) hashMap.get("typeComposant"));
                    // etc.
                    composantList.add(composant);
                }
                return composantList;
            }
            else{
                return composantList;
            }
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
            return chemin;
        }

        public void setAdressePiece(String adressePiece) {
            this.chemin = adressePiece;
        }

        public List<Composant> getLesComposants() {
            return lesComposants;
        }

        public void setLesComposants(List<Composant> lesComposants) {
            this.lesComposants = lesComposants;
        }
        public void setLesComposants2(List<HashMap<String, Object>> listHashMap){
            this.lesComposants = convertirListHashMapEnListCustomObject(listHashMap);
        }


        @Override
        public String toString() {
            return "Piece{" +
                    "idPiece='" + idPiece + '\'' +
                    ", adressePiece='" + chemin + '\'' +
                    ", nomPiece='" + nomPiece + '\'' +
                    ", typePiece=" + typePiece +
                    ", lesComposants=" + lesComposants +
                    '}';
        }
    }

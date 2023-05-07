package com.adi.projet2023.model.local;

import com.adi.projet2023.model.Piece.Piece;
import com.adi.projet2023.model.user.UserModel;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Local implements Serializable{

    String idLocal;
    String adresseLocal;
    TypeLocal typeLocal;

    String designationLocal;
    String nomLocal;
    String quartierLocal;
    String villeLocal;
    String dateEnregistrement;
    List<UserModel> lesUsers;
    List<Piece> lesPieces;

    /**
     * Default constructor
     */
    public Local(){
    }

    /**
     * Contructeur parametré
     * @param typeLocal, generé automatiquement
     * @param nomLocal désigne le nom du local
     * @param quartierLocal désigne le quartier du local
     * @param villeLocal désigne la ville du local
     */
    public Local(TypeLocal typeLocal, String nomLocal, String quartierLocal, String villeLocal) {
        this.typeLocal = typeLocal;
        this.designationLocal= typeLocal.toString();
        this.nomLocal = nomLocal;
        this.quartierLocal = quartierLocal;
        this.villeLocal = villeLocal;
        LocalDate date= LocalDate.now();
        this.dateEnregistrement= date.toString();
        this.adresseLocal= nomLocal.toLowerCase().replaceAll(" ", "");
        this.idLocal= adresseLocal;
    }

    public String getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(String idLocal) {
        this.idLocal = idLocal;
    }

    public TypeLocal getTypeLocal() {
        return typeLocal;
    }

    public void setTypeLocal(TypeLocal typeLocal) {
        this.typeLocal = typeLocal;
    }

    public String getNomLocal() {
        return nomLocal;
    }

    public void setNomLocal(String nomLocal) {
        this.nomLocal = nomLocal;
    }

    public String getQuartierLocal() {
        return quartierLocal;
    }

    public void setQuartierLocal(String quartierLocal) {
        this.quartierLocal = quartierLocal;
    }

    public String getDesignationLocal() {
        return designationLocal;
    }

    public void setDesignationLocal(String designationLocal) {
        this.designationLocal = designationLocal;
    }

    public String getVilleLocal() {
        return villeLocal;
    }

    public void setVilleLocal(String villeLocal) {
        this.villeLocal = villeLocal;
    }

    public List<UserModel> getLesUsers() {
        return lesUsers;
    }

    public void setLesUsers(List<UserModel> lesUsers) {
        this.lesUsers = lesUsers;
    }

    public List<Piece> getLesPieces() {
        return lesPieces;
    }

    public void setLesPieces(List<Piece> lesPieces) {
        this.lesPieces = lesPieces;
    }

    public String getAdresseLocal() {
        return adresseLocal;
    }

    public void setAdresseLocal(String adresseLocal) {
        this.adresseLocal = adresseLocal;
    }

    public String getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(String dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public boolean ajouterPiece(Piece nouvellePiece){
        return lesPieces.add(nouvellePiece);
    }

    public boolean supprimerPiece(Piece pieceASupprimer){
        return lesPieces.remove(pieceASupprimer);
    }

    public void ajouterUser(UserModel userModel){
        this.lesUsers.add(userModel);
    }

    public void retirerUser(UserModel userModel){
        if (lesUsers!=null)
            lesUsers.remove(userModel);
    }

    public boolean containsUser(UserModel userModel){
        boolean contains= false;

        if (lesUsers!=null){
            for (UserModel currentUser: lesUsers){
                if (currentUser.getEmail().equals(userModel.getEmail()))
                    contains=true;
            }
        }
        return contains;
    }

    @Override
    public String toString() {
        return "Local{" +
//                "typeLocal=" + typeLocal +
                ", nomLocal='" + nomLocal + '\'' +
                ", quartierLocal='" + quartierLocal + '\'' +
                ", villeLocal='" + villeLocal + '\'' +
                '}';
    }
}
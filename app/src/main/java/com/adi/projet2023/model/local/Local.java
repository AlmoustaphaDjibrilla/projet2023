package com.adi.projet2023.model.local;

import com.adi.projet2023.model.user.UserModel;

import java.io.Serializable;
import java.util.List;

public abstract class Local implements Serializable{

    String idLocal;
    TypeLocal typeLocal;
    String designationLocal;
    String nomLocal;
    String quartierLocal;
    String villeLocal;
    List<UserModel> lesUsers;

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

    @Override
    public String toString() {
        return "Local{" +
                "typeLocal=" + typeLocal +
                ", nomLocal='" + nomLocal + '\'' +
                ", quartierLocal='" + quartierLocal + '\'' +
                ", villeLocal='" + villeLocal + '\'' +
                '}';
    }
}

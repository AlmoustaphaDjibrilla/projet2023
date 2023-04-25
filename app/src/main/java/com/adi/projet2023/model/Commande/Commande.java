package com.adi.projet2023.model.Commande;

import com.adi.projet2023.model.composant.Composant;
import com.adi.projet2023.model.user.UserModel;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Commande implements Serializable {

    private UserModel userModel;
    private String emailUser;
    private Composant composant;
    private String nomComposant;
    private String date_commande;
    private String heure_commande;
    private String detail_commande;

    public Commande() {
    }

    public Commande(UserModel userModel, Composant composant) {
        this.userModel = userModel;
        if (userModel!=null)
            this.emailUser= userModel.getEmail();
        if (composant!=null)
            this.nomComposant= composant.getNomComposant();
        this.composant = composant;
        LocalDate date= LocalDate.now();
        this.date_commande= date.toString();
        this.heure_commande= LocalTime.now().toString();
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public Composant getComposant() {
        return composant;
    }

    public void setComposant(Composant composant) {
        this.composant = composant;
    }

    public String getNomComposant() {
        return nomComposant;
    }

    public void setNomComposant(String nomComposant) {
        this.nomComposant = nomComposant;
    }

    public String getDate_commande() {
        return date_commande;
    }

    public void setDate_commande(String date_commande) {
        this.date_commande = date_commande;
    }

    public String getHeure_commande() {
        return heure_commande;
    }

    public void setHeure_commande(String heure_commande) {
        this.heure_commande = heure_commande;
    }

    public String getDetail_commande() {
        return detail_commande;
    }

    public void setDetail_commande(String detail_commande) {
        this.detail_commande = detail_commande;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "userModel=" + userModel +
                ", emailUser='" + emailUser + '\'' +
                ", composant=" + composant +
                ", nomComposant='" + nomComposant + '\'' +
                ", date_commande='" + date_commande + '\'' +
                ", heure_commande='" + heure_commande + '\'' +
                ", detail_commande='" + detail_commande + '\'' +
                '}';
    }
}

package com.adi.projet2023.model.composant;

import java.io.Serializable;

public class Climatiseur extends Composant implements Serializable {
    public Climatiseur(){

    }
    public Climatiseur(String nom){
        super(nom, TypeComposant.CLIMATISEUR.toString());
    }
}
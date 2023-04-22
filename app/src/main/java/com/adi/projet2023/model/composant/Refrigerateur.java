package com.adi.projet2023.model.composant;

import java.io.Serializable;

public class Refrigerateur extends Composant implements Serializable {
    public Refrigerateur(){

    }
    public Refrigerateur(String nom){
        super(nom, TypeComposant.REFRIGERATEUR.toString());
    }

}
package com.adi.projet2023.model.composant;

import java.io.Serializable;

public class Autre extends Composant implements Serializable {
    public Autre(){

    }
    public Autre(String nom){
        super(nom, TypeComposant.AUTRE);
    }
}
package com.adi.projet2023.model.composant;


import java.io.Serializable;

public class Ampoule extends Composant implements Serializable {
    public Ampoule(){

    }
    public Ampoule(String nom){
        super(nom, TypeComposants.AMPOULE);
    }

}
package com.adi.projet2023.model.composant;

import java.io.Serializable;

public class Porte extends Composant implements Serializable {
    public Porte(){

    }
    public Porte(String nom){
        super(nom, TypeComposant.PORTE.toString());
    }

}
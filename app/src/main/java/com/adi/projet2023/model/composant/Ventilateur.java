package com.adi.projet2023.model.composant;

import java.io.Serializable;

public class Ventilateur extends Composant implements Serializable {
    public Ventilateur(){

    }
    public Ventilateur(String nom){
        super(nom, TypeComposant.VENTILATEUR);
    }
}
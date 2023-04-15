package com.adi.projet2023.model.Piece;

import java.io.Serializable;

public class Salon extends Piece implements Serializable {
    public Salon(){

    }
    public Salon(String nom){
        super(TypePiece.SALON, nom);
    }
}
